package util.commands;

import enums.JumpCondition;
import enums.Opcode;
import model.registers.OverallRegisters;

public class JumpCommands {
	
	private OverallRegisters allRegisters;
	
	public JumpCommands(OverallRegisters allRegisters) {
		this.allRegisters = allRegisters;
	}
	
	/**
	 * label should be operand[0].
	 * @param opcode
	 * @param jumpCondition
	 * @param label
	 */
	public boolean jumpCmd(Opcode opcode, JumpCondition jumpCondition, String label) {
		//Static Jump
		boolean shouldJump = false;
		if (opcode == Opcode.JUMP_STATIC) {
			shouldJump = true;
		}
		
		//Conditional Jump
		else if (opcode == Opcode.JUMP_CONDITIONAL) {
			shouldJump = shouldJump(jumpCondition);
		}
		
		//The Jumping
		if (shouldJump) {
			this.allRegisters.setNextJumpLabel(label);
		}
		return shouldJump;
	}
	
	/**
	 * data1 and data2 are the values listed to compare. Assume parameters are for normal compare, as test functionality is
	 * handled by this method.
	 * @param opcode
	 * @param data1
	 * @param data2
	 */
	public void compareOrTest(Opcode opcode, int data1, int data2) {
		//Test
		if (opcode == Opcode.TEST) {
			data2 = 0;
		}
		
		//Compare (Or Test, if Reformatted to Compare)
		this.allRegisters.setFirstValToCompare(data1);
		this.allRegisters.setSecondValToCompare(data2);
	}
	
	private boolean shouldJump(JumpCondition jumpCondition) {
		int firstValToCompare = this.allRegisters.getFirstValToCompare();
		int secondValToCompare = this.allRegisters.getSecondValToCompare();
		if (jumpCondition == JumpCondition.EQUAL || jumpCondition == JumpCondition.LAST_RESULT_WAS_ZERO) {
			return (firstValToCompare == secondValToCompare);
		}
		else if (jumpCondition == JumpCondition.NOT_EQUAL) {
			return (firstValToCompare != secondValToCompare);
		}
		else if (jumpCondition == JumpCondition.GREATER_THAN) {
			return (firstValToCompare > secondValToCompare);
		}
		else if (jumpCondition == JumpCondition.GREATER_THAN_OR_EQUAL_TO) {
			return (firstValToCompare >= secondValToCompare);
		}
		else if (jumpCondition == JumpCondition.LESSER_THAN) {
			return (firstValToCompare < secondValToCompare);
		}
		else if (jumpCondition == JumpCondition.LESSER_THAN_OR_EQUAL_TO) {
			return (firstValToCompare <= secondValToCompare);
		}
		return false;	//If Unrecognized Jump, Don't Jump!
	}
	
}
