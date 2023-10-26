package util.factories;

import java.util.HashMap;
import java.util.Map;
import enums.Opcode;

public class OpcodeNumArgsMappingFactory {

	@SuppressWarnings("serial")
	public static Map<Opcode, Integer> create() {
		return new HashMap<Opcode, Integer>() {{
			put(Opcode.NON_EXISTENT, 0);
			put(Opcode.MOVE, 2);
			put(Opcode.PUSH, 1);
			put(Opcode.POP, 1);
			put(Opcode.LOAD_EFFECTIVE_ADDRESS, 2);
			put(Opcode.ADD, 2);
			put(Opcode.SUBTRACT, 2);
			put(Opcode.INCREMENT, 1);
			put(Opcode.DECREMENT, 1);
			put(Opcode.DIVIDE, 1);
			put(Opcode.AND, 2);
			put(Opcode.OR, 2);
			put(Opcode.XOR, 2);
			put(Opcode.NOT, 1);
			put(Opcode.NEGATE, 1);
			put(Opcode.SHIFT_LEFT, 2);
			put(Opcode.SHIFT_RIGHT, 2);
			put(Opcode.JUMP_STATIC, 1);
			put(Opcode.JUMP_CONDITIONAL, 1);
			put(Opcode.COMPARE, 2);
			put(Opcode.TEST, 2);
			put(Opcode.CALL, 1);
			put(Opcode.RETURN, 0);
		}};
	}
	
}
