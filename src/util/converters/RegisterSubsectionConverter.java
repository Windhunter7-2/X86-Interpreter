package util.converters;

import enums.Register;
import model.memory.Stack;
import model.registers.GeneralPurposeRegisters;

public class RegisterSubsectionConverter {
	
	private GeneralPurposeRegisters mainRegisters;
	private Stack stack;
	
	//The Masks Themselves
	private static int mask_0 = 0xFFFFFFFF;	//NO MASK
	private static int mask_X = 0x0000FFFF;
	private static int mask_H = 0x0000FF00;
	private static int mask_L = 0x000000FF;
	
	//The Masks, but Inverted (The Mask "Negative")
	private static int mask_inverted_0 = 0x00000000;	//NO MASK
	private static int mask_inverted_X = 0xFFFF0000;
	private static int mask_inverted_H = 0xFFFF00FF;
	private static int mask_inverted_L = 0xFFFFFF00;
	
	public RegisterSubsectionConverter(GeneralPurposeRegisters generalPurposeRegisters, Stack stack) {
		this.mainRegisters = generalPurposeRegisters;
		this.stack = stack;
	}
	
	public void setRegisterVal(Register register, int value) {
		//Setup Part 1 (Masks)
		int regMask = getMask(register);
		int invertedMask = getInvertedMask(regMask);
		int shiftAmt = getShiftAmount(regMask);
		Register parentRegister = getParentRegister(register);
		
		//Setup Part 2 (Calculating the New Value)
		int oldValMasked = (getRegisterVal(parentRegister) & invertedMask);
		int newValShifted = (value << shiftAmt) & regMask;
		value = (oldValMasked + newValShifted);
		
		//Main Registers
		if (parentRegister == Register.EAX) {
			mainRegisters.setEax(value);
		}
		else if (parentRegister == Register.EBX) {
			mainRegisters.setEbx(value);
		}
		else if (parentRegister == Register.ECX) {
			mainRegisters.setEcx(value);
		}
		else if (parentRegister == Register.EDX) {
			mainRegisters.setEdx(value);
		}
		
		//Other Registers
		else if (parentRegister == Register.ESI) {
			mainRegisters.setEsi(value);
		}
		else if (parentRegister == Register.EDI) {
			mainRegisters.setEdi(value);
		}
		else if (parentRegister == Register.EBP) {
			stack.setEbp(value);
		}
		else if (parentRegister == Register.ESP) {
			stack.setEsp(value);
		}
	}
	
	public int getRegisterVal(Register register) {
		//Setup
		int regMask = getMask(register);
		Register parentRegister = getParentRegister(register);
		int shiftAmt = getShiftAmount(regMask);
		
		//Main Registers
		if (parentRegister == Register.EAX) {
			return (regMask & mainRegisters.getEax()) >>> shiftAmt;
		}
		else if (parentRegister == Register.EBX) {
			return (regMask & mainRegisters.getEbx()) >>> shiftAmt;
		}
		else if (parentRegister == Register.ECX) {
			return (regMask & mainRegisters.getEcx()) >>> shiftAmt;
		}
		else if (parentRegister == Register.EDX) {
			return (regMask & mainRegisters.getEdx()) >>> shiftAmt;
		}
		
		//Other Registers (Should Just Do get() Only,
		//but Other Stuff There Just to Match the Above Pattern)
		else if (parentRegister == Register.ESI) {
			return (regMask & mainRegisters.getEsi()) >>> shiftAmt;
		}
		else if (parentRegister == Register.EDI) {
			return (regMask & mainRegisters.getEdi()) >>> shiftAmt;
		}
		else if (parentRegister == Register.EBP) {
			return (regMask & stack.getEbp()) >>> shiftAmt;
		}
		else if (parentRegister == Register.ESP) {
			return (regMask & stack.getEsp()) >>> shiftAmt;
		}
		System.out.println("RegisterSubsectionConverter: Error! Should NOT Be Able to Get to Here!");
		return 0;	//Can't Find Appropriate Register
	}
	
	private static int getMask(Register register) {
		if (register == Register.AX || register == Register.BX || register == Register.CX
			|| register == Register.DX) {
			return mask_X;
		}
		else if (register == Register.AH || register == Register.BH || register == Register.CH
			|| register == Register.DH) {
			return mask_H;
		}
		else if (register == Register.AL || register == Register.BL || register == Register.CL
			|| register == Register.DL) {
			return mask_L;
		}
		return mask_0;	//Leave All Bits if Normal Register
	}
	
	private static int getInvertedMask(int mask) {
		if (mask == mask_X) {
			return mask_inverted_X;
		}
		else if (mask == mask_H) {
			return mask_inverted_H;
		}
		else if (mask == mask_L) {
			return mask_inverted_L;
		}
		return mask_inverted_0;
	}
	
	private static int getShiftAmount(int mask) {
		String maskAsString = ToStringUtils.padByteWithZeros(mask);
		int shiftAmt = 0;
		for (int i = maskAsString.length()-1; i >= 0; i--) {
			if (maskAsString.charAt(i) == '0') {
				shiftAmt++;
			}
			else {
				break;
			}
		}
		return (shiftAmt * 4);
	}
	
	private static Register getParentRegister(Register register) {
		if (register == Register.AX || register == Register.AH || register == Register.AL) {
			return Register.EAX;
		}
		else if (register == Register.BX || register == Register.BH || register == Register.BL) {
			return Register.EBX;
		}
		else if (register == Register.CX || register == Register.CH || register == Register.CL) {
			return Register.ECX;
		}
		else if (register == Register.DX || register == Register.DH || register == Register.DL) {
			return Register.EDX;
		}
		return register;	//Leave As Same Register if NOT a Subregister
	}

}
