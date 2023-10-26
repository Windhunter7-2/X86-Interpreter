package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import model.memory.MainMemory;
import model.memory.Stack;
import model.registers.GeneralPurposeRegisters;
import util.run.GUI_ButtonFunctions;

public class MainGUI {

	private String [] instructions;
	
	private String mainDirectory_windsPrograms;
	
	private String progTitle;
	
	public MainGUI(String progTitle, String [] instructions) {
		this.instructions = instructions;
		this.progTitle = (progTitle + ": ");
	}
	
	public MainGUI(String progTitle, String mainDirectory_windsPrograms, String [] instructions) {
		this.instructions = instructions;
		this.mainDirectory_windsPrograms = mainDirectory_windsPrograms;
		this.progTitle = (progTitle + ": ");
	}
	
	public JFrame createWindow_curX86Program(String titleOfCurWindow) {
		//Setup
		GUI_ButtonFunctions gui_ButtonFunctions = new GUI_ButtonFunctions(instructions);
		ButtonMaker buttonMaker = new ButtonMaker(this.progTitle);
		String mainDirectory_Images = (mainDirectory_windsPrograms + "images/");
		
		//Find Length of Longest Command and/or Label in Instructions
		int longestInstrCharLen = 1;
		for (String curInstruction : instructions) {
			if (curInstruction.length() > longestInstrCharLen) {
				longestInstrCharLen = curInstruction.length();
			}
		}
		
		//Set Up Instructions & Breakpoints Panels
		Panels_Instructions panels_Instructions = new Panels_Instructions(
			progTitle, mainDirectory_Images, longestInstrCharLen, instructions.length
		);
		JPanel breakpointsPanel = panels_Instructions.createBreakpointsPanel(instructions.length, gui_ButtonFunctions);
		JPanel instructionsPanel = panels_Instructions.createInstructionsPanel(instructions, 0, breakpointsPanel);
		JPanel instructionsPanel_Parent = buttonMaker.createGenericSingleNestedJPanel(instructionsPanel);
		
		//Set Up Memory Contents Panels
		Panels_MemoryContents panels_MemoryContents = new Panels_MemoryContents(progTitle);
		JPanel mainMemoryPanel = panels_MemoryContents.createMainMemoryPanel(new MainMemory(new Stack()));
		JPanel registersPanel = panels_MemoryContents.createGeneralPurposeRegistersPanel(new GeneralPurposeRegisters(), 0);
		JPanel stackPanel = panels_MemoryContents.createStackPanel(new Stack());
		JPanel mainMemoryPanel_Parent = buttonMaker.createGenericSingleNestedJPanel(mainMemoryPanel);
		JPanel registersPanel_Parent = buttonMaker.createGenericSingleNestedJPanel(registersPanel);
		JPanel stackPanel_Parent = buttonMaker.createGenericSingleNestedJPanel(stackPanel);
		JPanel overallMemoryContentsPanel = panels_MemoryContents.mergePanels(
			mainMemoryPanel_Parent, stackPanel_Parent, registersPanel_Parent
		);
		
		//Create Debug Buttons & Return Overall JFrame
		JPanel overallJPanel = buttonMaker.createGenericJPanel(
			true, false, 0, ButtonMaker.commonFont1, instructionsPanel_Parent, overallMemoryContentsPanel
		);
		DebugButtons debugButtons = new DebugButtons(gui_ButtonFunctions, mainDirectory_Images, progTitle, instructions,
			panels_Instructions, breakpointsPanel, overallJPanel);
		return createWindow_updateCurX86Program(debugButtons, titleOfCurWindow, overallJPanel);
	}
	
	private JFrame createWindow_updateCurX86Program(DebugButtons debugButtons, String titleOfCurWindow,
			JPanel overallJPanel) {
		//Create Debug Buttons & Overall Panels
		ButtonMaker buttonMaker = new ButtonMaker(this.progTitle);
		JPanel debugButtonsPanel = debugButtons.createDebugButtons();
		
		//Create Overall JFrame (& Update Frame So Buttons Work Properly)
		JFrame overallJFrame = buttonMaker.createJFrame(debugButtonsPanel, overallJPanel, titleOfCurWindow);
		debugButtons.updateJFrame(overallJFrame);
		return overallJFrame;
	}
	
	public String [] getInstructions() {
		return this.instructions;
	}
	
	public String getProgTitle() {
		return this.progTitle;
	}

}
