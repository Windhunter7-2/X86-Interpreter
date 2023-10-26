package util.factories;

import java.util.HashMap;
import java.util.Map;
import enums.Register;

public class RegisterMappingFactory {

	@SuppressWarnings("serial")
	public static Map<String, Register> create() {
		return new HashMap<String, Register>() {{
			put("ah", Register.AH);
			put("bh", Register.BH);
			put("ch", Register.CH);
			put("dh", Register.DH);
			put("al", Register.AL);
			put("bl", Register.BL);
			put("cl", Register.CL);
			put("dl", Register.DL);
			put("ax", Register.AX);
			put("bx", Register.BX);
			put("cx", Register.CX);
			put("dx", Register.DX);
			
			put("eax", Register.EAX);
			put("ebx", Register.EBX);
			put("ecx", Register.ECX);
			put("edx", Register.EDX);
			
			put("esi", Register.ESI);
			put("edi", Register.EDI);
			put("esp", Register.ESP);
			put("ebp", Register.EBP);
		}};
	}
	
}
