package util.converters;

import java.util.ArrayList;
import java.util.Map;
import enums.JumpCondition;
import enums.Opcode;
import enums.OpcodeType;
import enums.Register;
import model.memory.Instructions;
import model.memory.Stack;
import model.registers.GeneralPurposeRegisters;
import model.registers.OverallRegisters;
import util.commands.ArithmeticCommands;
import util.commands.JumpCommands;
import util.commands.MovementCommands;
import util.commands.SpecialCommands;
import util.factories.JumpMappingFactory;
import util.factories.OpcodeMappingFactory;
import util.factories.OpcodeNumArgsMappingFactory;
import util.factories.OpcodeTypeMappingFactory;
import util.factories.RegisterMappingFactory;

public class CommandExecutioner {
	
	private Instructions instructions;
	private OverallRegisters allRegisters;
	private RegisterSubsectionConverter registerSubsectionConverter;
	
	//Whether the Currently Running Command Is Successful Or Not
	private boolean isCmdSuccess;
	
	private Map<String, Opcode> opcodeMapping;
	private Map<String, JumpCondition> jumpMapping;
	private Map<String, Register> registerMapping;
	private Map<Opcode, Integer> opcodeNumArgsMapping;
	private Map<Opcode, OpcodeType> opcodeTypeMapping;
	
	private ArithmeticCommands arithmeticCommands;
	private JumpCommands jumpCommands;
	private MovementCommands movementCommands;
	private SpecialCommands specialCommands;
	
	public CommandExecutioner() {
		constructorCore(null, null);
	}
	
	public CommandExecutioner(Instructions instructions, OverallRegisters allRegisters) {
		constructorCore(instructions, allRegisters);
	}
	
	//Does the Constructor Code; Removes Duplicate Code for the 2 Different Constructors!
	private void constructorCore(Instructions instructions, OverallRegisters allRegisters) {
		this.opcodeMapping = OpcodeMappingFactory.create();
		this.jumpMapping = JumpMappingFactory.create();
		this.registerMapping = RegisterMappingFactory.create();
		this.opcodeNumArgsMapping = OpcodeNumArgsMappingFactory.create();
		this.opcodeTypeMapping = OpcodeTypeMappingFactory.create();
		this.instructions = instructions;
		this.allRegisters = allRegisters;
		if (allRegisters == null) {
			this.registerSubsectionConverter = new RegisterSubsectionConverter(
				new GeneralPurposeRegisters(),
				new Stack()
			);
		}
		else {
			this.registerSubsectionConverter = new RegisterSubsectionConverter(
				allRegisters.getGeneralPurposeRegisters(),
				allRegisters.getStack()
			);
			this.arithmeticCommands = new ArithmeticCommands(allRegisters);
			this.jumpCommands = new JumpCommands(allRegisters);
			this.movementCommands = new MovementCommands(allRegisters);
			this.specialCommands = new SpecialCommands(allRegisters);
		}
	}
	
	public boolean isJmpCmd(String cmd) {
		//Get Main 2 Parts (Opcode & Operands)
		String [] cmdMainParts = getCmdMainParts(cmd);
		
		//Get Opcode & Check if Jump Command
		Opcode opcode = getOpcode(cmdMainParts[0]);
		if (opcode == Opcode.JUMP_CONDITIONAL || opcode == Opcode.JUMP_STATIC) {
			return true;
		}
		return false;
	}
	
	public boolean isRetCmd(String cmd) {
		//Get Main 2 Parts (Opcode & Operands)
		String [] cmdMainParts = getCmdMainParts(cmd);
		
		//Get Opcode & Check if Jump Command
		Opcode opcode = getOpcode(cmdMainParts[0]);
		if (opcode == Opcode.RETURN) {
			return true;
		}
		return false;
	}
	
	public boolean isLabel(String cmd) {
		//Get Main 2 Parts (Opcode & Operands)
		String [] cmdMainParts = getCmdMainParts(cmd);
		
		//Check if Ends in Colon & Only One in Length
		if ( (cmdMainParts.length == 1) && (cmdMainParts[0].endsWith(":")) ) {
			return true;
		}
		return false;
	}
	
	public boolean executeCommand(String cmd) {
		//Get Main 2 Parts (Opcode & Operands)
		if (isLabel(cmd)) {
			return true;	//Skip Labels
		}
		String [] cmdMainParts = getCmdMainParts(cmd);
		
		//Get Opcode & Ensure Supported Command
		Opcode opcode = getOpcode(cmdMainParts[0]);
		JumpCondition jumpCondition = getJumpCondition(cmdMainParts[0]);
		if (opcode == Opcode.NON_EXISTENT ||
			(opcode == Opcode.JUMP_CONDITIONAL && jumpCondition == JumpCondition.NON_EXISTENT))
		{
			return false;	//Unsupported Command
		}
		if (cmdMainParts.length != 2) {
			if (opcode == Opcode.RETURN) {
				return true;	//The Only Exception Command
			}
			return false;	//Unsupported Syntax
		}
		
		//TODO -> Currently unsupported opcode that may be supported in the future
		//This particular instruction is a lot more complex to implement...
		if (opcode == Opcode.CALL) {
			return false;	//Only as these are currently unsupported; might add later, if spend time on it
		}
		
		//Execute the Command
		String [] operands = cmdMainParts[1].split(",");
		return performCmd(opcode, jumpCondition, operands);
	}
	
	private String [] getCmdMainParts(String originalCmd) {
		String cmd = originalCmd.strip();
		cmd = cmd.toLowerCase();	//Ensures Case Sensitivity
		String [] cmdMainParts = cmd.split("\\s", 2);	//e.g. ["mov", "eax, 0x42"]
		if (cmdMainParts.length == 2) {
			cmdMainParts[1] = cmdMainParts[1].replaceAll("\\s|\\t", "");	//Removes Spaces & Tabs from operands
		}
		return cmdMainParts;
	}
	
	private Opcode getOpcode(String cmdOpcode) {
		Opcode opcode = opcodeMapping.get(cmdOpcode);
		return (opcode != null ? opcode : Opcode.NON_EXISTENT);
	}
	
	private JumpCondition getJumpCondition(String cmdOpcode) {
		JumpCondition jumpCondition = jumpMapping.get(cmdOpcode);
		return (jumpCondition != null ? jumpCondition : JumpCondition.NON_EXISTENT);
	}
	
	private int getNumArgsOfOpcode(Opcode opcode) {
		if (opcode == Opcode.MULTIPLY) {
			return -1;	//Multiply Is a Special Case!
		}
		return opcodeNumArgsMapping.get(opcode);
	}
	
	private OpcodeType getOpcodeType(Opcode opcode) {
		OpcodeType opcodeType = opcodeTypeMapping.get(opcode);
		return (opcodeType != null ? opcodeType : OpcodeType.NON_EXISTENT);
	}
	
	private Register getRegister(String registerAsString) {
		Register register = registerMapping.get(registerAsString);
		if (registerAsString.startsWith("[") && registerAsString.endsWith("]")) {
			return Register.MEMORY_LOCATION;
		}
		if (register == null) {
			//First Hex Form - 123h OR 123H
			registerAsString = Filters.filterOnlyOneHexForm(registerAsString);
			char lastChar = registerAsString.charAt(registerAsString.length()-1);
			String otherChars = registerAsString.substring(0, registerAsString.length()-1);
			if (registerAsString.length() == 1) {	//If Single Character
				otherChars = registerAsString;
			}
			boolean isRegNumber = false;
			boolean isHexNumber = false;
			String isRegNumberRegex = "\\d+";
			String isHexNumberRegex = "(\\d|a|b|c|d|e|f)+";
			if (otherChars.matches(isHexNumberRegex)) {
				isHexNumber = true;
			}
			if (otherChars.matches(isRegNumberRegex)) {
				isRegNumber = true;
			}
			if ( (lastChar == 'h' || lastChar == 'H') && isHexNumber ) {
				return Register.CONSTANT_VALUE_HEXADECIMAL;
			}
			
			//Non-Hex Form - 123
			if (isRegNumber && (lastChar+"").matches(isRegNumberRegex)) {
				return Register.CONSTANT_VALUE;
			}
			
			//Second Hex Form - 0x123
			isHexNumber = false;
			if (registerAsString.length() > 2) {
				char firstChar = registerAsString.charAt(0);
				char secondChar = registerAsString.charAt(1);
				otherChars = registerAsString.substring(2);
				if (otherChars.matches(isHexNumberRegex)) {
					isHexNumber = true;
				}
				if ( firstChar == '0' && (secondChar == 'x' || secondChar == 'X') && isHexNumber ) {
					return Register.CONSTANT_VALUE_HEXADECIMAL;
				}
			}
			return Register.NONEXISTENT;	//Typo Somewhere; Do NOT Allow This Value!
		}
		return register;
	}
	
	private int getConstant(String registerAsString, boolean isHexadecimal) {
		registerAsString = Filters.filterOnlyOneHexForm(registerAsString);
		boolean isHFormOfHexadecimal = false;
		char lastChar = registerAsString.charAt(registerAsString.length()-1);
		if (lastChar == 'h' || lastChar == 'H') {
			isHFormOfHexadecimal = true;
		}
		if (isHexadecimal && isHFormOfHexadecimal) {
			registerAsString = "0x" + registerAsString.substring(0, registerAsString.length()-1);
		}
		return Integer.decode(registerAsString);
	}
	
	private int getMemoryLocation(String operand) {
		String whatsInsideBrackets = operand.substring(1, operand.length()-1);
		String [] addends = whatsInsideBrackets.split("\\+");
		int sum = 0;
		for (String curAddend : addends) {
			sum += getFromMemoryLocationOrRegisterOrConstant(curAddend);
		}
		return sum;
	}
	
	private int getDataFromMemoryLocation(String operand) {
		//Determine if from Stack
		boolean isStack = false;
		if (operand.contains("ebp") || operand.contains("esp")) {
			isStack = true;
		}
		
		//Retrieve from Stack (If Stack)
		int memoryLocation = getMemoryLocation(operand);
		if (isStack) {
			return this.allRegisters.getStack().getFromMemAddress(memoryLocation);
		}
		
		//Retrieve from Memory (If Memory)
		return this.allRegisters.getMainMemory().getValueAtMemoryLocation(memoryLocation);
	}
	
	/**
	 * Note: Opcode Optional Parameter MUST Be Used if a Jump Or Special Command (e.g. Return Or Call)
	 * Opcode Optional Parameter Is NOT Needed for ANY Other Commands!
	 * @param operand
	 * @param opcodeList
	 * @return
	 */
	private int getFromMemoryLocationOrRegisterOrConstant(String operand, Opcode ... opcodeList) {
		//Space Checks (To Prevent Bad Input)
		Register register = getRegister(operand);
		if (operand.contains(" ")) {
			register = Register.NONEXISTENT;
		}
		
		//Unique Opcodes That Don't Use Data (Mainly Jumps!)
		if (opcodeList.length > 0) {
			Opcode opcode = opcodeList[0];
			if (
				opcode == Opcode.CALL || opcode == Opcode.RETURN ||
				opcode == Opcode.JUMP_CONDITIONAL || opcode == Opcode.JUMP_STATIC ||
				opcode == Opcode.NON_EXISTENT
			) {
				return 0;	//Value Should NOT Matter, As This Particular Opcode Does Not Use Numbers!
			}
		}
		
		//Memory
		if (register == Register.MEMORY_LOCATION) {
			return getDataFromMemoryLocation(operand);
		}
		
		//Constant
		else if (register == Register.CONSTANT_VALUE) {
			return getConstant(operand, false);
		}
		else if (register == Register.CONSTANT_VALUE_HEXADECIMAL) {
			return getConstant(operand, true);
		}
		
		//Error Found
		else if (register == Register.NONEXISTENT) {
			isCmdSuccess = false;
			return -1;	//Which Number Shouldn't Matter Here (Because of the Boolean Set), but -1 for Convention Purposes
		}
		
		
		//Register
		return registerSubsectionConverter.getRegisterVal(register);
	}
	
	private boolean performCmd(Opcode opcode, JumpCondition jumpCondition, String [] allOperands) {
		//Assume Successful Command, Until Unsupported Formatting Found
		isCmdSuccess = true;
		
		//Filter allOperands to Remove $'s and %'s (Some X86 Formats Use This to Indicate Constants Clearly)
		allOperands = Filters.removeDollarSignsAndPercentSigns(allOperands);
		
		//Check Number of Arguments for Opcode
		int numExpectedArgs = getNumArgsOfOpcode(opcode);
		int numActualArgs = allOperands.length;
		if (numExpectedArgs != numActualArgs && opcode != Opcode.MULTIPLY) {
			isCmdSuccess = false;
		}
		if (opcode == Opcode.MULTIPLY && numActualArgs != 2 && numActualArgs != 3) {
			isCmdSuccess = false;
		}
		
		//Check Jumps Specifically, Making Sure Label Is Valid
		if (isCmdSuccess == true && (
			opcode == Opcode.JUMP_CONDITIONAL || opcode == Opcode.JUMP_STATIC
		)) {
			ArrayList<String> labelsList = this.instructions.getLabelsList();
			if (!labelsList.contains(allOperands[0])) {
				isCmdSuccess = false;
			}
		}
		
		//If Error, Return; Otherwise, Continue Normally
		return (isCmdSuccess
			? performCmd_afterFilter(opcode, jumpCondition, allOperands)
			: false);
	}
	
	private boolean performCmd_afterFilter(Opcode opcode, JumpCondition jumpCondition, String [] allOperands) {
		//Get Values for All Operands
		int [] allOperandVals = new int[allOperands.length];
		for (int i = 0; i < allOperands.length; i++) {
			allOperandVals[i]= getFromMemoryLocationOrRegisterOrConstant(allOperands[i], opcode);
		}
		
		//Determine if Successful Command, & Get the Opcode Type
		OpcodeType opcodeType = getOpcodeType(opcode);
		boolean firstOperandIsConst = (
			getRegister(allOperands[0]) == Register.CONSTANT_VALUE ||
			getRegister(allOperands[0]) == Register.CONSTANT_VALUE_HEXADECIMAL ||
			getRegister(allOperands[0]) == Register.NONEXISTENT
		);
		boolean isOpcodeThatWritesToFirstOperand = (
			opcodeType == OpcodeType.ARITHMETIC || opcodeType == OpcodeType.LOAD_EFFECTIVE_ADDRESS ||
			opcode == Opcode.POP || opcodeType == OpcodeType.MOVEMENT_STD
		);
		if (
			!isCmdSuccess ||
			opcodeType == OpcodeType.NON_EXISTENT ||
			(isOpcodeThatWritesToFirstOperand && firstOperandIsConst)
		) {
			return false;	//UNSuccessful Command! Syntax Error Somewhere
		}
		
		//Get the Register and/or Memory Location (And Stack Status)
		Register register = getRegister(allOperands[0]);
		int memLocation = 0;
		boolean isStack = false;
		if (register == Register.MEMORY_LOCATION) {
			memLocation = getMemoryLocation(allOperands[0]);
			if (allOperands[0].contains("ebp") || allOperands[0].contains("esp")) {
				isStack = true;
			}
		}
		
		//Arithmetic Commands
		if (opcodeType == OpcodeType.ARITHMETIC) {
			if (allOperands.length == 1) {
				arithmeticCommands.arithmeticCmd(
					opcode, register, memLocation, allOperands.length,
					allOperandVals[0], allOperandVals[0], isStack
				);
			}
			else if (allOperands.length == 2) {
				arithmeticCommands.arithmeticCmd(
						opcode, register, memLocation, allOperands.length,
						allOperandVals[0], allOperandVals[1], isStack
					);
			}
			else if (allOperands.length == 3) {
				arithmeticCommands.arithmeticCmd(
						opcode, register, memLocation, allOperands.length,
						allOperandVals[0], allOperandVals[1], isStack, allOperandVals[2]
					);
			}
		}
		
		//Jump & Compare/Test Commands
		else if (opcodeType == OpcodeType.JUMP) {
			//Note: Jumping Is a Special Case, Where It Can Fail Due to Incorrect Labels! That's Why the Return Here!
			return jumpCommands.jumpCmd(opcode, jumpCondition, allOperands[0]);
		}
		else if (opcodeType == OpcodeType.COMPARE_OR_TEST) {
			jumpCommands.compareOrTest(opcode, allOperandVals[0], allOperandVals[1]);
		}
		
		//Movement Commands
		else if (opcodeType == OpcodeType.MOVEMENT_STD) {
			movementCommands.moveCmd(register, memLocation, allOperandVals[1], isStack);
		}
		else if (opcodeType == OpcodeType.MOVEMENT_PUSH_OR_POP) {
			movementCommands.pushOrPop(opcode, register, memLocation, allOperandVals[0]);
		}
		
		//Special Commands
		else if (opcodeType == OpcodeType.LOAD_EFFECTIVE_ADDRESS) {
			specialCommands.specialCmd_loadEffectiveAddress(register, getMemoryLocation(allOperands[1]));
		}
		else if (opcodeType == OpcodeType.CALL) {
			specialCommands.specialCmd_call(allOperands[0]);
		}
		else if (opcodeType == OpcodeType.RETURN) {
			this.allRegisters.setShouldExit(true);
		}
		return true;
	}

}
