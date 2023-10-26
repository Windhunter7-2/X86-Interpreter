package gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import model.memory.MainMemory;
import model.memory.Stack;
import model.registers.GeneralPurposeRegisters;
import util.converters.ToStringUtils;

public class Panels_MemoryContents {
	
	private final String spacesForPadding_HTML = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	
	private String progTitle;
	
	private final int maxNumMemoriesBeforeResize = 15;
	private final int maxNumStacksBeforeResize = 8;
	private final int maxNumStacksBeforeRegistersResize = 10;
	
	public Panels_MemoryContents(String progTitle) {
		this.progTitle = progTitle;
	}
	
	private String convertIntoHtml(String whatToConvert) {
		whatToConvert = whatToConvert.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		return (
			"<html><span style=\"font-family: monospace;\">" + spacesForPadding_HTML +
			whatToConvert.replaceAll(" ", "&nbsp;").replaceAll(
				"\\n", spacesForPadding_HTML + spacesForPadding_HTML + "<br>" + spacesForPadding_HTML
			).replaceAll("\\t", "&emsp;")
			+ "</span></html>"
		);
	}
	
	public JPanel createMainMemoryPanel(MainMemory mainMemory) {
		//Configure Spacing Relating to Number of Memories Vs. Scrollbar
		int numMemoryEntries = mainMemory.getMainMemoryEntryCount();
		int toString_numEntriesPerRow = mainMemory.getToString_numEntriesPerRow();
		int toString_tabsInbetween = mainMemory.getToString_tabsInBetween();
		int toString_numRowsOfEachEntry = 7;
		int numRowsOfEachEntrySet = (toString_numRowsOfEachEntry + toString_tabsInbetween);
		int numRowsOfEntrySets = (int) Math.ceil(numMemoryEntries / toString_numEntriesPerRow);
		int numLinesOfMemory = (numRowsOfEachEntrySet * numRowsOfEntrySets);
		
		//Setup Font
		float resizeScale_memories = 1.0f;
		int memoriesLen = numMemoryEntries;
		int numTimesResized = 0;
		while (memoriesLen > maxNumMemoriesBeforeResize) {
			memoriesLen -= maxNumMemoriesBeforeResize;
			memoriesLen += (maxNumMemoriesBeforeResize * 3.0 / 4.0);
			numTimesResized++;
			if (
				(numTimesResized == 2 && numMemoryEntries <= 40) ||
				numTimesResized == 3
			) {
				break;
			}
		}
		resizeScale_memories = (float) Math.pow(3.0/4.0, numTimesResized);
		if (numMemoryEntries > 72) {
			numTimesResized += Math.ceil((numMemoryEntries - 72) / 6);
		}
		Font font2 = ToStringUtils.fontResizer(ButtonMaker.commonFont2, resizeScale_memories);
		Font font3 = ToStringUtils.fontResizer(ButtonMaker.commonFont3, resizeScale_memories);
		
		//Main Memory Itself
		JPanel [] allLines = new JPanel[1];
		ButtonMaker buttonMaker = new ButtonMaker(this.progTitle);
		String mainMemoryAsString = mainMemory.toString();
		if (numTimesResized > 0) {
			mainMemoryAsString = ( mainMemory.toString_alt(numTimesResized) );
		}
		String mainMemoryText = convertIntoHtml(mainMemoryAsString);
		JLabel mainMemoryLabel = buttonMaker.createJLabel(null, 0, mainMemoryText, font3);
		allLines[0] = buttonMaker.createGenericJPanel(true, false, numLinesOfMemory, font3, mainMemoryLabel);
		JPanel mainMemoryPanel = buttonMaker.createGenericJPanel(false, false, 0, font3, allLines);
		
		//"Main Memory" Label
		String labelText = ("<html>" + spacesForPadding_HTML + spacesForPadding_HTML +
			"<U>&nbsp;Main Memory&nbsp;</U>" + spacesForPadding_HTML + spacesForPadding_HTML + "</html>");
		JLabel mainMemoryNameLabel = buttonMaker.createJLabel(null, 0, labelText, font2);
		allLines[0].setAlignmentX(JPanel.LEFT_ALIGNMENT);
		mainMemoryPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		JPanel overallPanel = buttonMaker.createGenericJPanel(
			false, true, allLines.length, font2,
			mainMemoryNameLabel, buttonMaker.createEmptyJLabel(font2), mainMemoryPanel
		);
		
		//Add Border to Overall Panel
		Border border = BorderFactory.createLineBorder(Color.black);
		overallPanel.setBorder(border);
		return overallPanel;
	}
	
	public JPanel createGeneralPurposeRegistersPanel(GeneralPurposeRegisters generalPurposeRegisters, int numStackEntries) {
		//Registers Themselves
		JPanel [] allLines = new JPanel[1];
		ButtonMaker buttonMaker = new ButtonMaker(this.progTitle);
		String registersText = convertIntoHtml( generalPurposeRegisters.toString() );
		Font fontForRegisters = ButtonMaker.commonFont3;
		if (numStackEntries > maxNumStacksBeforeRegistersResize) {
			fontForRegisters = ButtonMaker.commonFont5;
		}
		JLabel registersLabel = buttonMaker.createJLabel(null, 0, registersText, fontForRegisters);
		allLines[0] = buttonMaker.createGenericJPanel(true, false, 1, fontForRegisters, registersLabel);
		JPanel registersPanel = buttonMaker.createGenericJPanel(false, false, 0, fontForRegisters, allLines);
		
		//"Registers" Label
		String labelText = ("<html>" + spacesForPadding_HTML + spacesForPadding_HTML +
			"<U>&nbsp;Registers&nbsp;</U>" + spacesForPadding_HTML + spacesForPadding_HTML + "</html>");
		JLabel registersNameLabel = buttonMaker.createJLabel(null, 0, labelText, ButtonMaker.commonFont2);
		allLines[0].setAlignmentX(JPanel.LEFT_ALIGNMENT);
		registersPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		JPanel overallPanel = buttonMaker.createGenericJPanel(
				false, false, allLines.length, fontForRegisters, registersNameLabel, new JLabel(" "), registersPanel
		);
		
		//Add Border to Overall Panel
		Border border = BorderFactory.createLineBorder(Color.black);
		overallPanel.setBorder(border);
		return overallPanel;
	}
	
	public JPanel createStackPanel(Stack stack) {
		//Configure Spacing Relating to Number of Stack Entries Vs. Scrollbar
		int numStackEntries = stack.getNumStackEntries();
		int numLinesOfStack = ( 1 + (numStackEntries * 2) );
		
		//Setup Font
		float resizeScale_stacks = 1.0f;
		int stacksLen = numStackEntries;
		int numTimesResized = 0;
		while (stacksLen > maxNumStacksBeforeResize) {
			stacksLen -= maxNumStacksBeforeResize;
			stacksLen += (maxNumStacksBeforeResize * 3.0 / 4.0);
			numTimesResized++;
			if (
				(numTimesResized == 1 && numStackEntries <= 24) ||
				(numTimesResized == 2 && numStackEntries <= 64) ||
				numTimesResized == 3
			) {
				break;
			}
		}
		resizeScale_stacks = (float) Math.pow(3.0/4.0, numTimesResized);
		if (numStackEntries > 64) {
			numTimesResized += Math.ceil((numStackEntries - 64) / 10);
		}
		numTimesResized++;
		Font font2 = ToStringUtils.fontResizer(ButtonMaker.commonFont2, resizeScale_stacks);
		Font font3 = ToStringUtils.fontResizer(ButtonMaker.commonFont3, resizeScale_stacks);
		
		//Stack Itself
		JPanel [] allLines = new JPanel[1];
		ButtonMaker buttonMaker = new ButtonMaker(this.progTitle);
		String stackAsString = stack.toString();
		String [] splitStack = splitStack(numTimesResized, stackAsString);
		JLabel [] allStackLabels = generateStackLabels(splitStack, numTimesResized, buttonMaker, font3);
		allLines[0] = buttonMaker.createGenericJPanel(true, false, numLinesOfStack, font3, allStackLabels);
		JPanel stackPanel = buttonMaker.createGenericJPanel(false, false, 0, font3, allLines);
		
		//"Stack" Label
		String labelText = ("<html>" + spacesForPadding_HTML + spacesForPadding_HTML +
			"<U>&nbsp;Stack&nbsp;</U>" + spacesForPadding_HTML + spacesForPadding_HTML + "</html>");
		JLabel stackNameLabel = buttonMaker.createJLabel(null, 0, labelText, font2);
		allLines[0].setAlignmentX(JPanel.LEFT_ALIGNMENT);
		stackPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		JPanel overallPanel = buttonMaker.createGenericJPanel(
				false, true, allLines.length, font2, stackNameLabel, buttonMaker.createEmptyJLabel(font2), stackPanel
		);
		
		//Add Border to Overall Panel
		Border border = BorderFactory.createLineBorder(Color.black);
		overallPanel.setBorder(border);
		return overallPanel;
	}
	
	private JLabel [] generateStackLabels(String [] splitStack, int numTimesResized, ButtonMaker buttonMaker, Font font3) {
		JLabel [] allStackLabels = new JLabel[numTimesResized];
		for (int i = 0; i < numTimesResized; i++) {
			String curPart = splitStack[i];
			String stackText = convertIntoHtml(curPart);
			JLabel stackLabel = buttonMaker.createJLabel(null, 0, stackText, font3);
			allStackLabels[i] = stackLabel;
		}
		return allStackLabels;
	}
	
	private String [] splitStack(int numTimesResized, String stackAsString) {
		String [] splitStack = new String[numTimesResized];
		int numCharsToSplit = (stackAsString.length() / numTimesResized);
		int curSubstringIndex = 0;
		for (int i = 0; i < numTimesResized; i ++) {
			splitStack[i] = stackAsString.substring(
				curSubstringIndex, Math.min(
					stackAsString.length(),
					curSubstringIndex + numCharsToSplit
				)
			);
			if ( i == (numTimesResized - 1) ) {
				splitStack[i] = stackAsString.substring(curSubstringIndex);
			}
			curSubstringIndex += numCharsToSplit;
		}
		return splitStack;
	}
	
	public JPanel mergePanels(JPanel mainMemoryParentPanel, JPanel stackParentPanel,
			JPanel generalPurposeRegistersParentPanel) {
		ButtonMaker buttonMaker = new ButtonMaker(this.progTitle);
		JPanel panel_left = buttonMaker.createGenericJPanel(
			false, false, 1, ButtonMaker.commonFont1, mainMemoryParentPanel
		);
		JPanel panel_right = buttonMaker.createGenericJPanel(
			false, false, 2, ButtonMaker.commonFont1, generalPurposeRegistersParentPanel, stackParentPanel
		);
		JPanel overallPanel = buttonMaker.createGenericJPanel(
			true, false, 2, ButtonMaker.commonFont1,
			new JLabel(" "), new JLabel(" "), panel_left, new JLabel(" "), panel_right
		);
		return overallPanel;
	}
	
}
