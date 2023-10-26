package enums;

public enum Register {
	
	NONEXISTENT,
	CONSTANT_VALUE,
	CONSTANT_VALUE_HEXADECIMAL,
	MEMORY_LOCATION,
	EAX,
	EBX,
	ECX,
	EDX,
	ESI,
	EDI,
	ESP,
	EBP,
//	EIP,	//Although technically a register, does not count, as to not interfere with the sequentialness of the program
	
	AX,
	BX,
	CX,
	DX,
	
	AH,
	BH,
	CH,
	DH,
	
	AL,
	BL,
	CL,
	DL;

}
