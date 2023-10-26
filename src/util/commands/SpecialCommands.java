package util.commands;

import enums.Register;
import model.registers.OverallRegisters;

public class SpecialCommands {
	
	private MovementCommands movementCommands;
	
	public SpecialCommands(OverallRegisters allRegisters) {
		this.movementCommands = new MovementCommands(allRegisters);
	}
	
	/**
	 * memoryLocation is ONLY the computed memory location of the second argument, and NOT gotten from memory.
	 * register is the first operand.
	 * @param register
	 * @param memoryLocation
	 */
	public void specialCmd_loadEffectiveAddress(Register register, int memoryLocation) {
		assert(register != Register.MEMORY_LOCATION);
		movementCommands.moveCmd(register, 0, memoryLocation, false);
	}
	
	/**
	 * The operand is the method name of what to call. For example, "call _time" would call the _time() method.
	 * @param operand
	 */
	public void specialCmd_call(String operand) {
		//TODO -> NOT Currently Implemented!!!
	}
	
}
