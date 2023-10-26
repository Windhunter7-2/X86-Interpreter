package util.commands;

import enums.Opcode;
import enums.Register;
import model.registers.OverallRegisters;
import util.converters.RegisterSubsectionConverter;

public class ArithmeticCommands {

	private OverallRegisters allRegisters;
	private MovementCommands movementCommands;
	private RegisterSubsectionConverter registerSubsectionConverter;

	public ArithmeticCommands(OverallRegisters allRegisters) {
		this.allRegisters = allRegisters;
		this.movementCommands = new MovementCommands(allRegisters);
		this.registerSubsectionConverter = new RegisterSubsectionConverter(
			allRegisters.getGeneralPurposeRegisters(),
			allRegisters.getStack()
		);
	}
	
	/**
	 * The data1 is the value that is in the register given as the first operand (OR the value at the given memory location),
	 * and data2 is the same principle, but for the second operand; data3 is only for multiplying, if it exists, but the same principle.
	 * For data2, for Opcodes that only support one data/operand, just copying the same data for both 1 & 2 is fine!
	 * @param opcode
	 * @param register
	 * @param memAddress
	 * @param numOperands
	 * @param data1
	 * @param data2
	 * @param isStack
	 * @param data3
	 */
	public void arithmeticCmd(Opcode opcode, Register register, int memAddress, int numOperands,
			int data1, int data2, boolean isStack, int ... data3) {
		if (opcode == Opcode.MULTIPLY) {
			if (numOperands == 3) {
				multiplyToRegister(register, data1, data2, data3);
			}
			else if (numOperands == 2) {
				multiplyToRegister(register, data1, data2);
			}
		}
		else if (register == Register.MEMORY_LOCATION) {
			if (numOperands == 2) {
				operationToMemory(opcode, memAddress, isStack, data1, data2);
			}
			else if (numOperands == 1) {
				operationToMemory(opcode, memAddress, isStack, data1);
			}
		}
		else {
			if (numOperands == 2) {
				operationToRegister(opcode, register, data1, data2);
			}
			else if (numOperands == 1) {
				operationToRegister(opcode, register, data1);
			}
		}
	}

	/**
	 * Reminder #1: operand1 already is the retrieved Register value (The old value) for the operation
	 * Reminder #2: operand3 is not required, and has a maximum of 1 supported for this function
	 * @param register
	 * @param operand1
	 * @param operand2
	 * @param operand3
	 */
	private void multiplyToRegister(Register register, int operand1, int operand2, int ... operand3) {
		//If 3 Arguments, Use the Latter Two in Place of the First Two for the Math
		if (operand3.length > 0) {
			operand1 = operand3[0];
		}
		
		//The Math
		int product = (operand1 * operand2);
		assert(register != Register.MEMORY_LOCATION);
		movementCommands.moveCmd(register, 0, product, false);
	}

	/**
	 * Reminder #1: operandVals already includes the retrieved Register value (The old value) for the arithmetic operation
	 * Reminder #2: operandVals is required, and has a maximum of 2 supported for this function
	 * @param opcode
	 * @param memAddress
	 * @param isStack
	 * @param operandVals
	 */
	private void operationToMemory(Opcode opcode, int memAddress, boolean isStack, int ... operandVals) {
		//Special Case: Dividing (Redirect to "ToRegister" Variation, Since Cannot Write to Memory!)
		if (opcode == Opcode.DIVIDE) {
			operationToRegister(opcode, null, operandVals);
			return;
		}
		
		//Normal Cases
		int newMemVal = allRegisters.getMainMemory().getValueAtMemoryLocation(memAddress);
		if (operandVals.length == 1) {
			newMemVal = calcNewValue(opcode, operandVals[0]);
		}
		else if (operandVals.length > 1) {
			newMemVal = calcNewValue(opcode, operandVals[0], operandVals[1]);
		}
		movementCommands.moveCmd(Register.MEMORY_LOCATION, memAddress, newMemVal, isStack);
	}

	/**
	 * Reminder #1: operandVals already includes the retrieved Register value (The old value) for the arithmetic operation
	 * Reminder #2: operandVals is required, and has a maximum of 2 supported for this function
	 * @param opcode
	 * @param register
	 * @param operandVals
	 */
	private void operationToRegister(Opcode opcode, Register register, int ... operandVals) {
		//Special Case: Dividing
		if (opcode == Opcode.DIVIDE) {
			long edxVal = registerSubsectionConverter.getRegisterVal(Register.EDX);
			long eaxVal = registerSubsectionConverter.getRegisterVal(Register.EAX);
			long divisor = ( (edxVal << 32) + eaxVal );
			movementCommands.moveCmd(Register.EAX, 0, (int) (divisor / operandVals[0]), false);
			movementCommands.moveCmd(Register.EDX, 0, (int) (divisor % operandVals[0]), false);
			return;
		}
		
		//Normal Cases
		int newRegVal = registerSubsectionConverter.getRegisterVal(register);
		if (operandVals.length == 1) {
			newRegVal = calcNewValue(opcode, operandVals[0]);
		}
		else if (operandVals.length > 1) {
			newRegVal = calcNewValue(opcode, operandVals[0], operandVals[1]);
		}
		assert(register != Register.MEMORY_LOCATION);
		movementCommands.moveCmd(register, 0, newRegVal, false);
	}
	
	private int calcNewValue(Opcode opcode, int value) {
		if (opcode == Opcode.INCREMENT) {
			return (value + 1);
		} else if (opcode == Opcode.DECREMENT) {
			return (value - 1);
		} else if (opcode == Opcode.NOT) {
			return ~value;
		} else if (opcode == Opcode.NEGATE) {
			return (~value + 1);
		}
		System.out.println("Warning! Did not find value for Opcode (" + opcode.name() + ") with value ("
				+ Integer.toHexString(value) + ")");
		return value; // Default, if Not Appropriate Opcode
	}

	private int calcNewValue(Opcode opcode, int val1, int val2) {
		int shiftVal = (val2 > 32 ? val2 % 32 : val2); // For Shifting Opcodes, Specifically
		if (opcode == Opcode.ADD) {
			return (val1 + val2);
		} else if (opcode == Opcode.SUBTRACT) {
			return (val1 - val2);
		} else if (opcode == Opcode.AND) {
			return (val1 & val2);
		} else if (opcode == Opcode.OR) {
			return (val1 | val2);
		} else if (opcode == Opcode.XOR) {
			return (val1 ^ val2);
		} else if (opcode == Opcode.SHIFT_LEFT) {
			return (val1 << shiftVal);
		} else if (opcode == Opcode.SHIFT_RIGHT) {
			return (val1 >>> shiftVal);
		}
		System.out.println("Warning! Did not find value for Opcode (" + opcode.name() + ") with values ["
				+ Integer.toHexString(val1) + ", " + Integer.toHexString(val2) + "]");
		return val1; // Default, if Not Appropriate Opcode
	}

}
