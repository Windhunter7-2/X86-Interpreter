package model.registers;

import util.converters.ToStringUtils;

public class GeneralPurposeRegisters {
	
	private int eax;
	private int ebx;
	private int ecx;
	private int edx;
	private int esi;
	private int edi;
	
	private final int toString_numEntriesPerRow = 3;
	private final int toString_tabsInBetween = 2;
	
	public GeneralPurposeRegisters() {
		eax = 0;
		ebx = 0;
		ecx = 0;
		edx = 0;
		esi = 0;
		edi = 0;
	}
	
	public int getEax() {
		return eax;
	}
	public void setEax(int eax) {
		this.eax = eax;
	}
	public int getEbx() {
		return ebx;
	}
	public void setEbx(int ebx) {
		this.ebx = ebx;
	}
	public int getEcx() {
		return ecx;
	}
	public void setEcx(int ecx) {
		this.ecx = ecx;
	}
	public int getEdx() {
		return edx;
	}
	public void setEdx(int edx) {
		this.edx = edx;
	}
	public int getEsi() {
		return esi;
	}
	public void setEsi(int esi) {
		this.esi = esi;
	}
	public int getEdi() {
		return edi;
	}
	public void setEdi(int edi) {
		this.edi = edi;
	}
	
	@Override
	public String toString() {
		String [] keys = {"EAX", "EBX", "ECX", "EDX", "ESI", "EDI"};
		int [] vals = {this.eax, this.ebx, this.ecx, this.edx, this.esi, this.edi};
		return ToStringUtils.createContainersAndContents(keys, vals, false,
			this.toString_numEntriesPerRow, this.toString_tabsInBetween);
	}
	
	public GeneralPurposeRegisters clone() {
		GeneralPurposeRegisters newRegisters = new GeneralPurposeRegisters();
		newRegisters.setEax(this.eax);
		newRegisters.setEbx(this.ebx);
		newRegisters.setEcx(this.ecx);
		newRegisters.setEdx(this.edx);
		newRegisters.setEsi(this.esi);
		newRegisters.setEdi(this.edi);
		return newRegisters;
	}

	public int getToString_numEntriesPerRow() {
		return this.toString_numEntriesPerRow;
	}
	
	public int getToString_tabsInBetween() {
		return this.toString_tabsInBetween;
	}

}
