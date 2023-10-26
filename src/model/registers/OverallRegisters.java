package model.registers;

import model.memory.Instructions;
import model.memory.MainMemory;
import model.memory.Stack;

public class OverallRegisters {
	
	private GeneralPurposeRegisters generalPurposeRegisters;
	private Stack stack;
	private MainMemory mainMemory;
	private Instructions instructions;
	private String nextJumpLabel;
	private boolean shouldExit;	//If Return Command Has Been Called!!!
	private int curInstrIndex;	//Which Index in the Instructions the State of the Registers Is *Currently* Looking at
	
	//These Are the Values That Get Compared During a test Or cmp Command
	private int firstValToCompare;
	private int secondValToCompare;
	
	public OverallRegisters(String [] initialInstructions, boolean isAutoskippingLabelsOn) {
		generalPurposeRegisters = new GeneralPurposeRegisters();
		stack = new Stack();
		mainMemory = new MainMemory(stack);
		instructions = new Instructions(initialInstructions, isAutoskippingLabelsOn);
		nextJumpLabel = "";
		shouldExit = false;
		curInstrIndex = -1;	//For Instruction Offset
	}

	public OverallRegisters(Instructions instructions, GeneralPurposeRegisters generalPurposeRegisters,
			Stack stack, MainMemory mainMemory, String nextJumpLabel, boolean shouldExit, int curInstrIndex,
			int firstValToCompare, int secondValToCompare) {
		this.generalPurposeRegisters = generalPurposeRegisters;
		this.stack = stack;
		this.mainMemory = mainMemory;
		this.instructions = instructions;
		this.nextJumpLabel = nextJumpLabel;
		this.shouldExit = shouldExit;
		this.curInstrIndex = curInstrIndex;
		this.firstValToCompare = firstValToCompare;
		this.secondValToCompare = secondValToCompare;
	}

	public GeneralPurposeRegisters getGeneralPurposeRegisters() {
		return generalPurposeRegisters;
	}

	public void setGeneralPurposeRegisters(GeneralPurposeRegisters generalPurposeRegisters) {
		this.generalPurposeRegisters = generalPurposeRegisters;
	}

	public Stack getStack() {
		return stack;
	}
	
	public void setStack(Stack stack) {
		this.stack = stack;
	}
	
	public MainMemory getMainMemory() {
		return mainMemory;
	}
	
	public void setMainMemory(MainMemory mainMemory) {
		this.mainMemory = mainMemory;
	}
	
	public Instructions getInstructions() {
		return instructions;
	}
	
	public void setInstructions(Instructions instructions) {
		this.instructions = instructions;
	}

	public String getNextJumpLabel() {
		return nextJumpLabel;
	}

	public void setNextJumpLabel(String nextJumpLabel) {
		this.nextJumpLabel = nextJumpLabel;
	}

	public boolean isShouldExit() {
		return shouldExit;
	}

	public void setShouldExit(boolean shouldExit) {
		this.shouldExit = shouldExit;
	}
	
	public int getCurInstrIndex() {
		return curInstrIndex;
	}
	
	public void setCurInstrIndex(int curInstrIndex) {
		this.curInstrIndex = curInstrIndex;
	}

	public int getFirstValToCompare() {
		return firstValToCompare;
	}

	public void setFirstValToCompare(int firstValToCompare) {
		this.firstValToCompare = firstValToCompare;
	}

	public int getSecondValToCompare() {
		return secondValToCompare;
	}

	public void setSecondValToCompare(int secondValToCompare) {
		this.secondValToCompare = secondValToCompare;
	}
	
	/**
	 * Clones OverallRegisters, but only a variant where the instructions are at the initial stage, before any
	 * instructions have been yet executed. This also assumes that auto-skipping labels is off.
	 */
	public OverallRegisters clone(String [] instructions) {
		//Clone Objects
		Instructions instructionsObject = new Instructions(instructions, false);
		GeneralPurposeRegisters generalPurposeRegisters = this.getGeneralPurposeRegisters().clone();
		Stack stack = this.getStack().clone();
		MainMemory mainMemory = this.getMainMemory().clone(stack);
		
		//Get Values for Non-Objects
		String nextJumpLabel = this.getNextJumpLabel();
		boolean shouldExit = this.isShouldExit();
		int curInstrIndex = this.getCurInstrIndex();
		int firstValToCompare = this.getFirstValToCompare();
		int secondValToCompare = this.getSecondValToCompare();
		
		OverallRegisters newOverallRegisters = new OverallRegisters(
			instructionsObject, generalPurposeRegisters, stack, mainMemory, nextJumpLabel, shouldExit,
			curInstrIndex, firstValToCompare, secondValToCompare
		);
		return newOverallRegisters;
	}
	
	
	
}
