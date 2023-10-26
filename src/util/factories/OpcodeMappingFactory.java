package util.factories;

import java.util.HashMap;
import java.util.Map;

import enums.Opcode;

public class OpcodeMappingFactory {
	
	@SuppressWarnings("serial")
	public static Map<String, Opcode> create() {
		return new HashMap<String, Opcode>() {{
			put("mov", Opcode.MOVE);
			put("push", Opcode.PUSH);
			put("pop", Opcode.POP);
			put("lea", Opcode.LOAD_EFFECTIVE_ADDRESS);
			put("add", Opcode.ADD);
			put("sub", Opcode.SUBTRACT);
			put("inc", Opcode.INCREMENT);
			put("dec", Opcode.DECREMENT);
			put("imul", Opcode.MULTIPLY);
			put("idiv", Opcode.DIVIDE);
			put("and", Opcode.AND);
			put("or", Opcode.OR);
			put("xor", Opcode.XOR);
			put("not", Opcode.NOT);
			put("neg", Opcode.NEGATE);
			put("shl", Opcode.SHIFT_LEFT);
			put("shr", Opcode.SHIFT_RIGHT);
			put("jmp", Opcode.JUMP_STATIC);
			put("je", Opcode.JUMP_CONDITIONAL);
			put("jne", Opcode.JUMP_CONDITIONAL);
			put("jz", Opcode.JUMP_CONDITIONAL);
			put("jg", Opcode.JUMP_CONDITIONAL);
			put("jge", Opcode.JUMP_CONDITIONAL);
			put("jl", Opcode.JUMP_CONDITIONAL);
			put("jle", Opcode.JUMP_CONDITIONAL);
			put("cmp", Opcode.COMPARE);
			put("test", Opcode.TEST);
			put("call", Opcode.CALL);
			
			put("ret", Opcode.RETURN);
			put("retn", Opcode.RETURN);
			put("retrn", Opcode.RETURN);
			put("return", Opcode.RETURN);
		}};
	}

}
