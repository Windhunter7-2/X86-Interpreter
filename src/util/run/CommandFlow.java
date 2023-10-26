package util.run;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.memory.Instructions;
import model.memory.MainMemory;
import model.memory.Stack;
import model.registers.GeneralPurposeRegisters;
import model.registers.OverallRegisters;
import util.converters.CommandExecutioner;

public class CommandFlow {
	
	private String [] instructions;
	
	/**
	 * The saved states of each instruction.
	 */
	private ArrayList<MainMemory> allMainMemories;
	private ArrayList<Stack> allStacks;
	private ArrayList<GeneralPurposeRegisters> allRegisters;
	private ArrayList<Integer> allInstrIndices;
	
	/**
	 * The current index of the saved states themselves.
	 */
	private int curIndex;
	
	/**
	 * The current index in the instructions.
	 */
	private int curInstrIndex;
	
	/**
	 * Whether or not the return line (Or last line) is the current line being looked at. If it is, do NOT generate the next
	 * saved state.
	 */
	private boolean returnLineReached;
	
	/**
	 * The latest generated save state (So far).
	 */
	private OverallRegisters curOverallRegisters;
	
	private ArrayList<Integer> allBreakpoints;
	
	public CommandFlow(String [] instructions) {
		this.instructions = instructions;
		this.allMainMemories = new ArrayList<MainMemory>();
		this.allStacks = new ArrayList<Stack>();
		this.allRegisters = new ArrayList<GeneralPurposeRegisters>();
		this.allInstrIndices = new ArrayList<Integer>();
		this.curIndex = 0;
		this.curInstrIndex = -1;
		this.curOverallRegisters = new OverallRegisters(instructions, false);
		this.returnLineReached = false;
		this.allBreakpoints = new ArrayList<Integer>();
		
		//Generate All the States, and Apply Fixes Afterward
		generateAllSavedStates();
		this.curIndex = -1;
		fixInstructionIndicesForJumps();
		getCurSavedState();
	}
	
	/**
	 * Toggles an Instruction line number on or off as a breakpoint.
	 * @param instrIndex
	 */
	public void toggleBreakpoint(int instrIndex) {
		//If Currently On, Toggle Off
		if (this.allBreakpoints.contains(instrIndex)) {
			for (int i = 0; i < this.allBreakpoints.size(); i++) {
				if (this.allBreakpoints.get(i) == instrIndex) {
					this.allBreakpoints.remove(i);
					return;
				}
			}
		}
		
		//If Currently Off, Toggle On
		else {
			this.allBreakpoints.add(instrIndex);
		}
	}
	
	/**
	 * Gets the memory state at the next available breakpoint. (AKA go to next breakpoint)
	 * @return
	 */
	public OverallRegisters getSavedState_nextBreakpoint() {
		OverallRegisters overallRegisters = getCurSavedState();
		boolean didAtLeastOneStep = false;
		while (true) {
			int instrIndex = overallRegisters.getCurInstrIndex();
			if (didAtLeastOneStep && (
				this.allBreakpoints.contains(instrIndex) || getIsEndOfCode(instrIndex)
			)) {
				break;
			}
			overallRegisters = getSavedState_down();
			didAtLeastOneStep = true;
		}
		return overallRegisters;
	}
	
	public OverallRegisters getCurSavedState() {
		OverallRegisters overallRegisters = getSavedState(this.curIndex);
		this.curInstrIndex = overallRegisters.getCurInstrIndex();
		return overallRegisters;
	}
	
	/**
	 * Gets the previous memory state. (AKA go back/up by 1)
	 * @return
	 */
	public OverallRegisters getSavedState_up() {
		if (this.curIndex != -1) {	//-1 Is Directly Before the X86 Program Has Executed!
			this.curIndex--;
		}
		return getCurSavedState();
	}
	
	/**
	 * Gets the next memory state. (AKA go forward/down by 1)
	 * @return
	 */
	public OverallRegisters getSavedState_down() {
		if (this.curIndex != this.allRegisters.size()-1) {
			this.curIndex++;
		}
		return getCurSavedState();
	}
	
	private boolean getIsEndOfCode(int curInstrIndex) {
		CommandExecutioner commandExecutioner = new CommandExecutioner();
		boolean instructionsHaveRetCmd = false;
		for (String cmd : this.instructions) {
			if (commandExecutioner.isRetCmd(cmd)) {
				instructionsHaveRetCmd = true;
				break;
			}
		}
		
		boolean isRetCmd = commandExecutioner.isRetCmd(this.instructions[curInstrIndex]);
		boolean isLastInstr = (curInstrIndex >= (this.instructions.length - 1));
		boolean isEndOfCode = (
			isRetCmd ||
			(isLastInstr && !instructionsHaveRetCmd) ||
			(isLastInstr && !commandExecutioner.isJmpCmd(this.instructions[curInstrIndex]))
		);
		return isEndOfCode;
	}
	
	/**
	 * When allInstrIndices is initially created, because of the way jumps work in this debugger, there are offsets
	 * from jumping in these indices. This method fixes those offsets.
	 */
	private void fixInstructionIndicesForJumps() {
		//Fix Indices More, to Put Them As BEFORE the Instruction Happened, Instead of AFTER
		for (int i = 0; i < allInstrIndices.size(); i++) {
			int curInstrIndex = allInstrIndices.get(i);
			if ( curInstrIndex < (this.instructions.length-1) ) {
				allInstrIndices.set( i, (curInstrIndex + 1) );
			}
		}
	}
	
	private void generateAllSavedStates() {
		while (true) {
			if ( (curIndex == allMainMemories.size()) && !this.returnLineReached) {
				generateNextSavedState();
			}
			else {
				return;
			}
			OverallRegisters overallRegisters = getSavedState(curIndex);
			int curInstrIndex = overallRegisters.getCurInstrIndex();
			
			if (getIsEndOfCode(curInstrIndex)) {
				this.returnLineReached = true;
			}
			else {
				curIndex++;
			}
			this.curInstrIndex = overallRegisters.getCurInstrIndex();
		}
	}
	
	private void generateNextSavedState() {
		//Clone the Current State
		Stack clonedStack = this.curOverallRegisters.getStack().clone();
		MainMemory clonedMemory = this.curOverallRegisters.getMainMemory().clone(clonedStack);
		GeneralPurposeRegisters clonedRegisters = this.curOverallRegisters.getGeneralPurposeRegisters().clone();
		OverallRegisters newState = createOverallRegisters(clonedMemory, clonedStack, clonedRegisters,
			this.curOverallRegisters.getCurInstrIndex());
		
		//Command Execution & Label Handling
		CommandExecutioner commandExecutioner = new CommandExecutioner(newState.getInstructions(), newState);
		String command = newState.getInstructions().getNextInstruction(false);
		String curJmpLabel = newState.getNextJumpLabel();
		boolean executedCmd = false;
		boolean isLabel = commandExecutioner.isLabel(command);
		if (!isLabel) {	//Only Execute Command if Non-Label
			executedCmd = commandExecutioner.executeCommand(command);
		}
		boolean isJmpCmd = commandExecutioner.isJmpCmd(command);
		
		//Jump Handling
		if (executedCmd && isJmpCmd) {
			//If Not Jumping (AKA Acts Like a Normal Instruction)
			String jmpLabel = newState.getNextJumpLabel();
			if (jmpLabel.equals(curJmpLabel)) {
				newState.setCurInstrIndex(newState.getCurInstrIndex() + 1);
			}
			
			//If Jumping
			else {
				newState.setCurInstrIndex(
					newState.getInstructions().jumpToLabelAndGetLineNum(jmpLabel)
				);
				newState.setNextJumpLabel("");
			}
		}
		
		//Command Handling
		else if (executedCmd || isLabel) {
			newState.setCurInstrIndex(newState.getCurInstrIndex() + 1);
		}
		this.curInstrIndex = newState.getCurInstrIndex();
		if (executedCmd || isJmpCmd) {
			System.out.println("[Line #" + this.curInstrIndex + "] Successfully executed command: " + command);
		}
		else if (isLabel) {
			System.out.println("[Line #" + this.curInstrIndex + "] Successfully recognized label: "
				+ command.substring(0, command.length()-1));
		}
		else {	//AKA Failed/Invalid Command
			String errMsg = ("[Line #" + this.curInstrIndex + "] Error! Command was not a success: " + command);
			System.out.println(errMsg);
			JOptionPane.showMessageDialog(null, errMsg, "X86 Interpreter - Error!", JOptionPane.ERROR_MESSAGE);
		}
		
		//Saving the New State
		this.allMainMemories.add(newState.getMainMemory());
		this.allRegisters.add(newState.getGeneralPurposeRegisters());
		this.allStacks.add(newState.getStack());
		this.allInstrIndices.add(newState.getCurInstrIndex());
		this.curOverallRegisters = newState;
	}
	
	private OverallRegisters getSavedState(int indexInArrayLists) {
		if (indexInArrayLists == -1) {
			return new OverallRegisters(
				new Instructions(this.instructions, false), new GeneralPurposeRegisters(), new Stack(),
				new MainMemory(new Stack()), "", false, 0, 0, 0
			);
		}
		
		//Get the New Memory State
		MainMemory mainMemory = allMainMemories.get(indexInArrayLists);
		Stack stack = allStacks.get(indexInArrayLists);
		GeneralPurposeRegisters generalPurposeRegisters = allRegisters.get(indexInArrayLists);
		int curInstrIndex = allInstrIndices.get(indexInArrayLists);
		
		//Get the Other Needed Info & Return
		return createOverallRegisters(mainMemory, stack, generalPurposeRegisters, curInstrIndex);
	}

	private OverallRegisters createOverallRegisters(
			MainMemory mainMemory, Stack stack, GeneralPurposeRegisters generalPurposeRegisters,
			int curInstrIndex
			) {
		//Get the Other Needed Info
		Instructions instructions = curOverallRegisters.getInstructions();
		String nextJumpLabel = curOverallRegisters.getNextJumpLabel();
		boolean shouldExit = curOverallRegisters.isShouldExit();
		int firstValToCompare = curOverallRegisters.getFirstValToCompare();
		int secondValToCompare = curOverallRegisters.getSecondValToCompare();
		
		//Return the Object
		OverallRegisters overallRegisters = new OverallRegisters(
			instructions, generalPurposeRegisters, stack, mainMemory, nextJumpLabel, shouldExit, curInstrIndex,
			firstValToCompare, secondValToCompare
		);
		return overallRegisters;
	}
	
	public int getCurInstrIndex() {
		return this.curInstrIndex;
	}

}
