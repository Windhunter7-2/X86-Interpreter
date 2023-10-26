package util.commands;

import enums.Opcode;
import enums.Register;
import model.registers.OverallRegisters;
import util.converters.RegisterSubsectionConverter;

public class MovementCommands {
	
	private OverallRegisters allRegisters;
	private RegisterSubsectionConverter registerSubsectionConverter;
	
	public MovementCommands(OverallRegisters allRegisters) {
		this.allRegisters = allRegisters;
		this.registerSubsectionConverter = new RegisterSubsectionConverter(
			allRegisters.getGeneralPurposeRegisters(),
			allRegisters.getStack()
		);
	}
	
	/**
	 * The data is the value that is in the register given as the second operand (OR the value at the given memory location).
	 * Reminder: The first data is not used for movement instructions.
	 * @param register
	 * @param memAddress
	 * @param data
	 * @param isStack
	 */
	public void moveCmd(Register register, int memAddress, int data, boolean isStack) {
		if (register == Register.MEMORY_LOCATION) {
			moveToMemory(memAddress, data, isStack);
		}
		else {
			moveToRegister(register, data);
		}
	}
	
	private void moveToMemory(int memoryAddress, int data, boolean isStack) {
		if (isStack) {
			allRegisters.getStack().setAtMemAddress(memoryAddress, data);
			return;
		}
		allRegisters.getMainMemory().updateMemory(memoryAddress, data);
	}
	
	private void moveToRegister(Register register, int data) {
		registerSubsectionConverter.setRegisterVal(register, data);
	}
	
	/**
	 * The data is the value that is in the register given as the operand (OR the value at the given memory location).
	 * @param opcode
	 * @param register
	 * @param memAddress
	 * @param data
	 */
	public void pushOrPop(Opcode opcode, Register register, int memAddress, int data) {
		if (opcode == Opcode.PUSH) {
			push(data);
		}
		else if (register == Register.MEMORY_LOCATION && opcode == Opcode.POP) {
			popToMemory(memAddress);
		}
		else {
			popToRegister(register);
		}
	}
	
	private void push(int data) {
		allRegisters.getStack().push(data);
	}
	
	private void popToMemory(int memoryAddress) {
		moveToMemory(
			memoryAddress,
			allRegisters.getStack().pop(),
			false
		);
	}
	
	private void popToRegister(Register register) {
		moveToRegister(
			register,
			allRegisters.getStack().pop()
		);
	}

}
