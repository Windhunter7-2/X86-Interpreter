package gui;

import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.registers.OverallRegisters;
import util.run.GUI_ButtonFunctions;

public class DebugButtons {
	
	/**
	 * The current state of the program, including stack, main memory, and all the registers.
	 * This is to be used in the GUI for displaying purposes.
	 */
	private OverallRegisters curOverallRegisters;
	
	/**
	 * Which line to currently have lit up in the instructions.
	 */
	private static int curLineToHighlight;
	
	private GUI_ButtonFunctions gui_ButtonFunctions;
	
	private String mainDirectory_images;
	
	private ButtonMaker buttonMaker;
	
	private Panels_Instructions panels_Instructions;
	
	private Panels_MemoryContents panels_MemoryContents;
	
	private boolean debugHasStarted;
	
	private String progName;
	
	//For GUI Use Only!
	private String [] instructions;
	
	private JButton curPlayResumeButton;
	private JButton curStepBackButton;
	private JButton curStepIntoButton;
	private JButton curStopButton;
	private JPanel overallDebugButtons;
	
	/**
	 * This is the overall JFrame for the current X86 program being analyzed. Its main reference purpose here is
	 * to be able to remotely exit that particular frame, to go back to the main menu.
	 */
	private JFrame overallJFrame;
	
	//These Are the Panels of Data Themselves, for Real-Time Data Show
	private JPanel overallJPanel;
	private JPanel jPanel_breakpoints;
	
	private final String buttonLocation_play = "debugButton_play.png";
	private final String buttonLocation_resume = "debugButton_resume.png";
	private final String buttonLocation_stepBack = "debugButton_stepBack.png";
	private final String buttonLocation_stepInto = "debugButton_stepInto.png";
	private final String buttonLocation_stop = "debugButton_stop.png";
	
	private final float buttonColor_H = (1.0f / 3.0f);
	private final float buttonColor_S = 1.0f;
	private final float buttonColor_V = 0.2f;
	
	public DebugButtons(GUI_ButtonFunctions gui_ButtonFunctions, String mainDirectory_images,
			String programName, String [] instructions, Panels_Instructions panels_Instructions,
			JPanel jPanel_breakpoints, JPanel overallJPanel) {
		this.gui_ButtonFunctions = gui_ButtonFunctions;
		this.mainDirectory_images = mainDirectory_images;
		this.jPanel_breakpoints = jPanel_breakpoints;
		this.buttonMaker = new ButtonMaker(programName);
		this.debugHasStarted = false;
		this.progName = programName;
		this.instructions = instructions;
		this.panels_Instructions = panels_Instructions;
		this.overallJPanel = overallJPanel;
	}
	
	public JPanel createDebugButtons() {
		if (this.overallDebugButtons == null) {
			this.overallDebugButtons = this.buttonMaker.createGenericJPanel(true, false, 0, ButtonMaker.commonFont1,
				createButton_playResume(), createButton_stepBack(),
				createButton_stepInto(), createButton_stop());
		}
		return this.overallDebugButtons;
	}
	
	public void updateJFrame(JFrame overallJFrame) {
		this.overallJFrame = overallJFrame;
	}
	
	private JButton makeButton(String imgName, String bttnTitle, ActionListener buttonFunctionality) {
		JButton debugButton = buttonMaker.createButton(imgName, bttnTitle, 0.5, buttonFunctionality);
		debugButton.setBackground(Color.getHSBColor(this.buttonColor_H, this.buttonColor_S, this.buttonColor_V));
		return debugButton;
	}
	
	private JButton createButton_playResume() {
		String playResumeIcon = this.buttonLocation_play;
		ActionListener actionListener = (evt -> {
			curLineToHighlight = this.gui_ButtonFunctions.button_playResume();
			this.curOverallRegisters = this.gui_ButtonFunctions.getCurOverallRegisters();
			updateAllJPanels();
		});
		String imgName = (this.mainDirectory_images + playResumeIcon);
		String bttnTitle = "Play/Resume Debug";
		this.curPlayResumeButton = makeButton(imgName, bttnTitle, actionListener);
		return this.curPlayResumeButton;
	}
	
	private JButton createButton_stepBack() {
		ActionListener actionListener = (evt -> {
			curLineToHighlight = this.gui_ButtonFunctions.button_stepBack();
			this.curOverallRegisters = this.gui_ButtonFunctions.getCurOverallRegisters();
			updateAllJPanels();
		});
		String imgName = (this.mainDirectory_images + this.buttonLocation_stepBack);
		String bttnTitle = "Step Back";
		this.curStepBackButton = makeButton(imgName, bttnTitle, actionListener);
		return this.curStepBackButton;
	}
	
	private JButton createButton_stepInto() {
		ActionListener actionListener = (evt -> {
			curLineToHighlight = this.gui_ButtonFunctions.button_stepInto();
			this.curOverallRegisters = this.gui_ButtonFunctions.getCurOverallRegisters();
			updateAllJPanels();
		});
		String imgName = (this.mainDirectory_images + this.buttonLocation_stepInto);
		String bttnTitle = "Step Into";
		this.curStepIntoButton = makeButton(imgName, bttnTitle, actionListener);
		return this.curStepIntoButton;
	}
	
	private JButton createButton_stop() {
		ActionListener actionListener = (evt -> {
			String msg = "Do you wish to exit the full X86 Interpreter program, as well?"
					+ "\n(If not, only exit the current assembly program)";
			int options = JOptionPane.showConfirmDialog(
				null, msg, this.progName + "Exit Confirmation", JOptionPane.YES_NO_CANCEL_OPTION
			);
			if (options != JOptionPane.CANCEL_OPTION) {
				this.overallJFrame.setVisible(false);
				this.overallJFrame.dispose();
			}
			if (options == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		});
		String imgName = (this.mainDirectory_images + this.buttonLocation_stop);
		String bttnTitle = "Terminate";
		this.curStopButton = makeButton(imgName, bttnTitle, actionListener);
		return this.curStopButton;
	}
	
	private void refreshPlayButton() {
		//Special Icon Stuff for Play Button
		if (debugHasStarted) {
			String playResumeIcon = this.buttonLocation_resume;
			
			//Normal Button Stuff
			ActionListener actionListener = (evt -> {
				curLineToHighlight = this.gui_ButtonFunctions.button_playResume();
				this.curOverallRegisters = this.gui_ButtonFunctions.getCurOverallRegisters();
				updateAllJPanels();
			});
			String imgName = (this.mainDirectory_images + playResumeIcon);
			String bttnTitle = "Play/Resume Debug";
			this.curPlayResumeButton = makeButton(imgName, bttnTitle, actionListener);
			this.overallDebugButtons.removeAll();
			this.overallDebugButtons.add(curPlayResumeButton);
			this.overallDebugButtons.add(curStepBackButton);
			this.overallDebugButtons.add(curStepIntoButton);
			this.overallDebugButtons.add(curStopButton);
			this.overallDebugButtons.revalidate();
			this.overallDebugButtons.repaint();
		}
		debugHasStarted = true;
	}
	
	private void updateFrame(JPanel newJPanel_instructions, JPanel newJPanel_memoryContents) {
		this.overallJPanel.removeAll();
		this.overallJPanel.add(newJPanel_instructions);
		this.overallJPanel.add(newJPanel_memoryContents);
		this.overallJPanel.revalidate();
		this.overallJPanel.repaint();
		this.overallJFrame.revalidate();
		this.overallJFrame.repaint();
	}
	
	private void updateAllJPanels() {
		refreshPlayButton();
		this.panels_MemoryContents = new Panels_MemoryContents(this.progName);
		JPanel instructionsPanel = buttonMaker.createGenericSingleNestedJPanel(
			this.panels_Instructions.createInstructionsPanel(
				this.instructions,
				DebugButtons.curLineToHighlight,
				this.jPanel_breakpoints
			)
		);
		JPanel mainMemoryPanel = buttonMaker.createGenericSingleNestedJPanel(
			this.panels_MemoryContents.createMainMemoryPanel(
				this.curOverallRegisters.getMainMemory()
			)
		);
		JPanel stackPanel = buttonMaker.createGenericSingleNestedJPanel(
			this.panels_MemoryContents.createStackPanel(
				this.curOverallRegisters.getStack()
			)
		);
		JPanel generalPurposeRegistersPanel = buttonMaker.createGenericSingleNestedJPanel(
			this.panels_MemoryContents.createGeneralPurposeRegistersPanel(
				this.curOverallRegisters.getGeneralPurposeRegisters(),
				this.curOverallRegisters.getStack().getNumStackEntries()
			)
		);
		JPanel memoryContentsPanel = this.panels_MemoryContents.mergePanels(
			mainMemoryPanel, stackPanel, generalPurposeRegistersPanel
		);
		updateFrame(instructionsPanel, memoryContentsPanel);
	}
	
}
