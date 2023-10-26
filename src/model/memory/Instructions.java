package model.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Instructions {
	
	/**
	 * The instructions themselves.
	 */
	private String [] instructions;
	
	/**
	 * A mapping of labels to the index of the instruction immediately following the label.
	 */
	Map<String, Integer> labelsMap;
	
	/**
	 * Index Pointer. Points to the current instruction. In the context of the simulation this program
	 * does, it points to which instruction line number to activate.
	 */
	private int eip;
	
	/**
	 * The list of the labels themselves.
	 */
	private ArrayList<String> labelsList;
	
	private boolean firstInstructionGotten;
	private boolean lastInstructionReached;
	
	public Instructions(String [] instructions, boolean isAutoskippingLabelsOn) {
		//If No Instructions (For Testing Only), Don't Do Anything, & Print Warning!!!
		if (instructions == null) {
			System.out.println("Warning! There are no instructions loaded; this should only be the case with testing!!!");
			this.instructions = new String[0];
			return;
		}
		
		//Copy Instructions
		this.instructions = new String[instructions.length];
		for (int i = 0; i < instructions.length; i++) {
			this.instructions[i] = instructions[i].strip();
		}
		
		//Generate Label Map
		this.labelsMap = new HashMap<String, Integer>();
		this.labelsList = new ArrayList<String>();
		for (int i = 0; i < instructions.length; i++) {
			if (isLabel(i)) {
				String curInst = this.instructions[i];
				String newLabel = curInst.substring(0, curInst.length() - 1);
				newLabel = newLabel.strip().toLowerCase().replaceAll("\\s|\\t", "");	//Removes Spaces & Tabs, As Well As Forces Lw-Case
				labelsMap.put(
					newLabel,
					i
//					(i != instructions.length-1) ? (i+1) : i	//Old Version; Might Be Useful Though
				);
				labelsList.add(newLabel);
			}
		}
		
		//Set EIP (Unique) Counter Register
		resetEipToInit(isAutoskippingLabelsOn);
		this.firstInstructionGotten = false;
		this.lastInstructionReached = false;
	}
	
	private void resetEipToInit(boolean isAutoskippingLabelsOn) {
		//Set EIP (Unique) Counter Register
		this.eip = 0;
		if (isLabel(0) && this.instructions.length > 1 && isAutoskippingLabelsOn) {
			this.eip = 1;
		}
	}
	
	private boolean isLabel(int lineNum) {
		String curInst = this.instructions[lineNum];
		if (curInst.endsWith(":"))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Primarily for the initial instruction, but can also be used to retrieve the current instruction, too.
	 * @return
	 */
	public String getInitOrCurInstruction() {
		return instructions[eip];
	}
	
	public String getNextInstruction(boolean isAutoskippingLabelsOn) {
		//If the First Instruction
		if (!firstInstructionGotten) {
			firstInstructionGotten = true;
			return getInitOrCurInstruction();
		}
		
		//If the Last Instruction (& Already Reached Previously)
		if ( lastInstructionReached && (eip == (instructions.length-1)) ) {
			return instructions[eip];
		}
		
		//The Normal Process
		eip++;
		if (eip == instructions.length) {
			eip--;
			this.lastInstructionReached = true;
		}
		else if (isAutoskippingLabelsOn && isLabel(eip)) {
			eip++;
			if (eip == instructions.length) {
				eip--;
				this.lastInstructionReached = true;
			}
		}
		return instructions[eip];
	}
	
	public String jumpToLabelAndGetInstruction(String label) {
		label = label.strip().toLowerCase().replaceAll("\\s|\\t", "");	//Removes Spaces & Tabs, As Well As Forces Lw-Case
		eip = (labelsMap.get(label) != null) ? labelsMap.get(label) : eip+1;
		if (eip == instructions.length) {
			eip--;
			this.lastInstructionReached = true;
		}
		return instructions[eip];
	}
	
	public int jumpToLabelAndGetLineNum(String label) {
		label = label.strip().toLowerCase().replaceAll("\\s|\\t", "");	//Removes Spaces & Tabs, As Well As Forces Lw-Case
		eip = (labelsMap.get(label) != null) ? labelsMap.get(label) : eip+1;
		if (eip != 0) {
			eip--;
		}
		//TODO -> Not Sure This Is Needed; if Bugs Happen, Uncomment This Code & See if That Fixes It
//		if (eip == instructions.length) {
//			eip--;
//			this.lastInstructionReached = true;
//		}
		return eip;
	}
	
	public ArrayList<String> getLabelsList() {
		return this.labelsList;
	}
	
	public int getEip() {
		return this.eip;
	}
	
}
