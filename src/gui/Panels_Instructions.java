package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import util.converters.ToStringUtils;
import util.run.GUI_ButtonFunctions;

public class Panels_Instructions extends ButtonMaker {
	
	private Icon breakpoint_buttonIcon_on;
	private Icon breakpoint_buttonIcon_off;
	
	private int longestInstrCharLen;
	
	private ButtonMaker buttonMaker;
		
	private final String iconLocation_breakpointIcon_on = "icon_breakpoint.png";
	private final String iconLocation_breakpointIcon_off = "icon_breakpoint_toggledOff.png";
	private final double breakpointIconScale = (
		new Canvas().getFontMetrics(ButtonMaker.commonFont1).getHeight() / 150.0
	);
	
	private final int maxNumInstructionsBeforeResize = 20;
	private float resizeScale;
	private Font font1;
	private Font font2;
	
	private final String spacesForPadding_HTML = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	private final String bgColorForHighlight = "yellow";
	private final String breakpointsBgColor = "black";
	private final String breakpointsFgColor = "yellow";
	
	public Panels_Instructions(String progTitle) {
		super(progTitle);
	}
	public Panels_Instructions(String progTitle, String mainDirectory_images, int longestInstrCharLen, int instrLen) {
		super(progTitle);
		this.buttonMaker = new ButtonMaker(progTitle);
		
		//Setup Font
		double newBreakpointIconScale = breakpointIconScale;
		this.resizeScale = 1.0f;
		while (instrLen > maxNumInstructionsBeforeResize) {
			this.resizeScale *= (3.0f / 4.0f);
			instrLen -= maxNumInstructionsBeforeResize;
			instrLen += (maxNumInstructionsBeforeResize * 3.0 / 4.0);
			newBreakpointIconScale = (newBreakpointIconScale * this.resizeScale);
		}
		this.font1 = ToStringUtils.fontResizer(ButtonMaker.commonFont1, resizeScale);
		this.font2 = ToStringUtils.fontResizer(ButtonMaker.commonFont2, resizeScale);
		
		//Create Icons for Breakpoints
		this.breakpoint_buttonIcon_on = createIcon_forButtons(
			mainDirectory_images + iconLocation_breakpointIcon_on, newBreakpointIconScale
		);
		this.breakpoint_buttonIcon_off = createIcon_forButtons(
			mainDirectory_images + iconLocation_breakpointIcon_off, newBreakpointIconScale
		);
		this.longestInstrCharLen = longestInstrCharLen;
	}
	
	private JPanel [] createInstrRelatedPanels(int numLines, ArrayList<JPanel> baseLines) {
		JPanel [] allLines = new JPanel[numLines];
		for (int i = 0; i < numLines; i++) {
			allLines[i] = baseLines.get(i);
		}
		return allLines;
	}

	public JPanel createInstructionsPanel(String [] instructions, int lineNumberHighlighted, JPanel breakpointsPanel) {
		//Instructions Themselves
		ArrayList<JPanel> baseLines = new ArrayList<JPanel>();
		for (int i = 0; i < instructions.length; i++) {
			String curInstr = instructions[i];
			for (int j = curInstr.length(); j < this.longestInstrCharLen; j++) {
				curInstr += "&nbsp;&nbsp;";
			}
			String instructionText = ("<html>" + curInstr + spacesForPadding_HTML + "</html>");
			if (i == lineNumberHighlighted) {
				instructionText = ("<html><span style=\"background-color:" + this.bgColorForHighlight + ";\">"
					+ curInstr + spacesForPadding_HTML + "</span></html>");
			}
			JLabel curInstruction = createJLabel(null, 0, instructionText, font1);
			JButton emptyJButton = new JButton(this.breakpoint_buttonIcon_off);
			emptyJButton.setOpaque(false);
			emptyJButton.setContentAreaFilled(false);
			emptyJButton.setBorderPainted(false);
			baseLines.add( createGenericJPanel(true, false, 2, font1, emptyJButton, curInstruction) );
		}
		
		JPanel [] allLines = createInstrRelatedPanels(instructions.length, baseLines);
		JPanel instructionsPanel = createGenericJPanel(false, false, 0, font1, allLines);
		JPanel semiOverallPanel = createGenericJPanel(true, false, 0, font1, breakpointsPanel, instructionsPanel);
		
		//"Instructions" Label
		String labelText = ("<html>" + spacesForPadding_HTML + spacesForPadding_HTML
				+ spacesForPadding_HTML + "<U>&nbsp;Instructions&nbsp;</U>"
				+ spacesForPadding_HTML + spacesForPadding_HTML + spacesForPadding_HTML + "</html>");
		JLabel instructionsLabel = createJLabel(null, 0, labelText, font2);
		semiOverallPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		instructionsLabel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		JPanel overallPanel = createGenericJPanel(
			false, true, allLines.length, font1, instructionsLabel, buttonMaker.createEmptyJLabel(font2), semiOverallPanel
		);
		
		//Add Border to Overall Panel
		Border border = BorderFactory.createLineBorder(Color.black);
		overallPanel.setBorder(border);
		return overallPanel;
	}
	
	//Note: This Method Should ONLY Ever Be Called Once, Since This Is the Only Panel That Modifies Itself!!!
	public JPanel createBreakpointsPanel(int numLines, GUI_ButtonFunctions gui_ButtonFunctions) {
		ArrayList<JPanel> baseBreakpointChecks = new ArrayList<JPanel>();
		for (int i = 0; i < numLines; i++) {
			JPanel curBreakpointPanel = createJPanelForBreakpoints(i, gui_ButtonFunctions);
			baseBreakpointChecks.add(curBreakpointPanel);
		}
		JPanel [] allBreakpoints = createInstrRelatedPanels(numLines, baseBreakpointChecks);
		JPanel breakpointsPanel = createGenericJPanel(false, false, 0, font1, allBreakpoints);
		return breakpointsPanel;
	}
	
	private JPanel createJPanelForBreakpoints(int lineNum, GUI_ButtonFunctions gui_ButtonFunctions) {
		String breakpointText = ("<html><span style=\"background-color:" + this.breakpointsBgColor
				+ "; color:" + this.breakpointsFgColor + ";\">"
				+ "&nbsp;" + (lineNum+1) + "&nbsp;" + "</span></html>");
		JLabel curBreakpointLabel = createJLabel(null, 0, breakpointText, font1);
		JButton curBreakpointButton = createJButtonForBreakpoints(
			lineNum, gui_ButtonFunctions, this.breakpoint_buttonIcon_on, this.breakpoint_buttonIcon_off
		);
		return createGenericJPanel(true, false, 2, font1, curBreakpointButton, curBreakpointLabel);
	}
	
	private JButton createJButtonForBreakpoints(int buttonNum, GUI_ButtonFunctions gui_ButtonFunctions,
			Icon buttonIcon_on, Icon buttonIcon_off) {
		JButton jButton = new JButton(buttonIcon_off);
		jButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui_ButtonFunctions.toggleBreakpoint(buttonNum);
				if (jButton.getIcon().equals(buttonIcon_off)) {
					jButton.setIcon(buttonIcon_on);
				}
				else {
					jButton.setIcon(buttonIcon_off);
				}
			}
		});
		return jButton;
	}
	
}
