package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class InstructionsGUI extends JPanel {
	
	private JFrame jf;
	private JTextArea textBox;
	
	private String progTitle;
	private String x86ProgName;
	private String mainDirectory_windsPrograms;
	
	public InstructionsGUI(String progTitle, String x86ProgName, String mainDirectory_windsPrograms) {
		this.progTitle = progTitle;
		this.x86ProgName = x86ProgName;
		this.mainDirectory_windsPrograms = mainDirectory_windsPrograms;
	}
	
	public void startInstructions(String programName, String buttonText) {
		textBoxGUI(programName, "", true, buttonText);
	}
	
	public void updateInstructions(JFrame curX86Program, String [] curInstructions, String curProgName, String buttonText) {
		if (curX86Program != null) {
			curX86Program.setVisible(false);
			curX86Program.dispose();
		}
		String overallInstructions = "";
		for (String curLine : curInstructions) {
			overallInstructions += curLine + "\n";
		}
		textBoxGUI(curProgName, overallInstructions, true, buttonText);
	}
	
	private void startX86Program(String originalString) {
		String [] instructions = originalString.split("(\\r|\\n)(\\r|\\n)*");
		String titleOfDebugger = ("Debugger for " + x86ProgName);
		MainGUI mainGUI = new MainGUI(this.progTitle, this.mainDirectory_windsPrograms, instructions);
		JFrame curJFrame = mainGUI.createWindow_curX86Program(titleOfDebugger);
		MenuGUI.mostRecentX86ProgramFrame = curJFrame;
		MenuGUI.mostRecentX86ProgramInstructions = mainGUI.getInstructions();
	}
	
	private void textBoxGUI(String fullDisplayTitle, String initialText, boolean hasButton, String buttonText)
	{
		//Set Up Text Box
		textBox = new JTextArea();
		textBox.setText(initialText);
		textBox.setLineWrap(true);
		textBox.setWrapStyleWord(true);

		//Set Up JScrollPane for Text Box
		JScrollPane jScrollPane = new JScrollPane(textBox);
		jScrollPane.setPreferredSize(new Dimension(1500, 750));
		jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		//Set Up Panel and JFrame
		JPanel mainPanel = new JPanel();
		mainPanel.setOpaque(true);
		jf = new JFrame(fullDisplayTitle);
		jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		jf.setContentPane(mainPanel);
		jf.setLayout(new BorderLayout());
		jf.setPreferredSize(new Dimension(1500, 750));
		jf.add(jScrollPane, BorderLayout.CENTER);
		
		//Set Up / Add Button (If Applicable), & Finalize JFrame
		if (hasButton) {
			JButton button = new JButton(buttonText);
			jf.add(button, BorderLayout.SOUTH);
			button.addActionListener(evt -> {
				//End This GUI, and Start the GUI for the Current X86 Program
				String textToUse = textBox.getText();
				jf.setVisible(false);
				jf.dispose();
				startX86Program(textToUse);
			});
		}
		jf.pack();
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
	}

}