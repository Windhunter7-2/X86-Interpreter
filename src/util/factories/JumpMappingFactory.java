package util.factories;

import java.util.HashMap;
import java.util.Map;
import enums.JumpCondition;

public class JumpMappingFactory {

	@SuppressWarnings("serial")
	public static Map<String, JumpCondition> create() {
		return new HashMap<String, JumpCondition>() {{
			put("je", JumpCondition.EQUAL);
			put("jne", JumpCondition.NOT_EQUAL);
			put("jz", JumpCondition.LAST_RESULT_WAS_ZERO);
			put("jg", JumpCondition.GREATER_THAN);
			put("jge", JumpCondition.GREATER_THAN_OR_EQUAL_TO);
			put("jl", JumpCondition.LESSER_THAN);
			put("jle", JumpCondition.LESSER_THAN_OR_EQUAL_TO);
		}};
	}
	
}
