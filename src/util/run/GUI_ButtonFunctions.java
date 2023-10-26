package util.run;

import model.registers.OverallRegisters;

public class GUI_ButtonFunctions {
	
	/**
	 * The current state of the program, including stack, main memory, and all the registers.
	 * This is to be used in the GUI for displaying purposes.
	 */
	private OverallRegisters curOverallRegisters;
	
	private CommandFlow commandFlow;
	
	public GUI_ButtonFunctions(String [] instructions) {
		this.commandFlow = new CommandFlow(instructions);
	}
	
	private int saveState() {
		this.curOverallRegisters = this.commandFlow.getCurSavedState();
		return this.commandFlow.getCurInstrIndex();
	}
	
	public int button_playResume() {
		this.commandFlow.getSavedState_nextBreakpoint();
		return saveState();
	}
	
	public int button_stepBack() {
		this.commandFlow.getSavedState_up();
		return saveState();
	}
	
	public int button_stepInto() {
		this.commandFlow.getSavedState_down();
		return saveState();
	}
	
	public void toggleBreakpoint(int instrLineNum) {
		this.commandFlow.toggleBreakpoint(instrLineNum);
	}
	
	public OverallRegisters getCurOverallRegisters() {
		return this.curOverallRegisters;
	}

}
