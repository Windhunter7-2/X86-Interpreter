package gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MenuGUI {
	
	private String mainDirectory_windsPrograms;
	
	private String progTitle;
	
	private InstructionsGUI instructionsGUI;
	
	private String mostRecentX86ProgramName;
	public static String [] mostRecentX86ProgramInstructions;
	public static JFrame mostRecentX86ProgramFrame;
	
	public MenuGUI(String mainDirectory_windsPrograms, String progTitle) {
		this.mainDirectory_windsPrograms = mainDirectory_windsPrograms;
		this.progTitle = progTitle;
	}
	
	public void startMainMenu() {
		ButtonMaker buttonMaker = new ButtonMaker(this.progTitle);
		JButton debugProgramButton = buttonMaker.createButton(
			null, "Add X86 Program Code to Debug", 0, evt -> {
				debugProgramButtonFunction();
			}
		);
		JButton updateProgramButton = buttonMaker.createButton(
				null, "Update Most Recent X86 Program Code", 0, evt -> {
					if (!(mostRecentX86ProgramInstructions == null || mostRecentX86ProgramInstructions.length == 0)) {
						updateProgramButtonFunction();
					}
				}
			);
		JButton exitButton = buttonMaker.createButton(null, "Exit Program", 0, evt -> { System.exit(0); });
		debugProgramButton.setFont(ButtonMaker.commonFont4);
		updateProgramButton.setFont(ButtonMaker.commonFont4);
		exitButton.setFont(ButtonMaker.commonFont4);
		debugProgramButton.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		updateProgramButton.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		exitButton.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		JLabel extraSpace1 = buttonMaker.createJLabel(null, 0, " ", ButtonMaker.commonFont4);
		JLabel extraSpace2 = buttonMaker.createJLabel(null, 0, " ", ButtonMaker.commonFont4);
		JLabel [] extraTopSpace = new JLabel[3];
		for (int i = 0; i < extraTopSpace.length; i++) {
			extraTopSpace[i] = buttonMaker.createJLabel(null, 0, " ", ButtonMaker.commonFont4);
		}
		JPanel extraTopSpacePanel = buttonMaker.createGenericJPanel(false, false, 3, ButtonMaker.commonFont1, extraTopSpace);
		JPanel mainMenuPanel = buttonMaker.createGenericJPanel(
			false, false, 2, ButtonMaker.commonFont1,
			extraTopSpacePanel, debugProgramButton,
			extraSpace1, updateProgramButton, extraSpace2, exitButton
		);
		buttonMaker.createJFrame(null, mainMenuPanel, ": Main Menu");
	}
	
	private void debugProgramButtonFunction() {
		String x86ProgName = JOptionPane.showInputDialog(
				"What would you like the name of this X86 program to be called?"
				+ "\n(Note that this is in case you want to debug multiple X86 programs simultaneously)"
			);
		if (x86ProgName == null || x86ProgName.isEmpty()) {
			x86ProgName = "Generic X86 Program";
		}
		this.mostRecentX86ProgramName = x86ProgName;
		String instructionsTitle = ("Input for " + x86ProgName + ": Enter Code Here");
		this.instructionsGUI = new InstructionsGUI(this.progTitle, x86ProgName, this.mainDirectory_windsPrograms);
		this.instructionsGUI.startInstructions(instructionsTitle, "Start GUI for X86 Program \""+x86ProgName+"\"");
	}

	private void updateProgramButtonFunction() {
		String curProgName = ("Input for " + this.mostRecentX86ProgramName + ": Enter Code Here");
		String buttonText = ("Update GUI for X86 Program \"" + this.mostRecentX86ProgramName + "\"");
		this.instructionsGUI.updateInstructions(
			MenuGUI.mostRecentX86ProgramFrame, MenuGUI.mostRecentX86ProgramInstructions, curProgName, buttonText
		);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//FOR TESTING!!!
	public static void main(String[] args) {
		
					
		String mainDirectory = "Z:/Windhunter's Programs/X86-Interpreter/";
		MenuGUI menuGUI = new MenuGUI(mainDirectory, "X86 Program");
		menuGUI.startMainMenu();
		

	}

}
