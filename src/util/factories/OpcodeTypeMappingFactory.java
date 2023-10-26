package util.factories;

import java.util.HashMap;
import java.util.Map;
import enums.Opcode;
import enums.OpcodeType;

public class OpcodeTypeMappingFactory {

	@SuppressWarnings("serial")
	public static Map<Opcode, OpcodeType> create() {
		return new HashMap<Opcode, OpcodeType>() {{
			put(Opcode.NON_EXISTENT, OpcodeType.NON_EXISTENT);
			put(Opcode.MOVE, OpcodeType.MOVEMENT_STD);
			put(Opcode.PUSH, OpcodeType.MOVEMENT_PUSH_OR_POP);
			put(Opcode.POP, OpcodeType.MOVEMENT_PUSH_OR_POP);
			put(Opcode.LOAD_EFFECTIVE_ADDRESS, OpcodeType.LOAD_EFFECTIVE_ADDRESS);
			put(Opcode.MULTIPLY, OpcodeType.ARITHMETIC);
			put(Opcode.ADD, OpcodeType.ARITHMETIC);
			put(Opcode.SUBTRACT, OpcodeType.ARITHMETIC);
			put(Opcode.INCREMENT, OpcodeType.ARITHMETIC);
			put(Opcode.DECREMENT, OpcodeType.ARITHMETIC);
			put(Opcode.DIVIDE, OpcodeType.ARITHMETIC);
			put(Opcode.AND, OpcodeType.ARITHMETIC);
			put(Opcode.OR, OpcodeType.ARITHMETIC);
			put(Opcode.XOR, OpcodeType.ARITHMETIC);
			put(Opcode.NOT, OpcodeType.ARITHMETIC);
			put(Opcode.NEGATE, OpcodeType.ARITHMETIC);
			put(Opcode.SHIFT_LEFT, OpcodeType.ARITHMETIC);
			put(Opcode.SHIFT_RIGHT, OpcodeType.ARITHMETIC);
			put(Opcode.JUMP_STATIC, OpcodeType.JUMP);
			put(Opcode.JUMP_CONDITIONAL, OpcodeType.JUMP);
			put(Opcode.COMPARE, OpcodeType.COMPARE_OR_TEST);
			put(Opcode.TEST, OpcodeType.COMPARE_OR_TEST);
			put(Opcode.CALL, OpcodeType.CALL);
			put(Opcode.RETURN, OpcodeType.RETURN);
		}};
	}
	
}
