package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class ButtonMaker {
	
	//Quick Reference of Methods for GUI Changes:
	/*
	public JButton createButton(String imgName, String bttnTitle,
			double scaleOfIcon, ActionListener buttonFunctionality) {
	public int getImgWidth(String imageIcon) {
	public int getImgHeight(String imageIcon) {
	public JLabel createJLabel(ImageIcon imageIcon, String bottomText, Font font) {
	public JPanel createGenericJPanel(boolean isHorizontal, Component ... objectsToAdd) {
	public JPanel createGenericSingleNestedJPanel(JPanel panelToNest) {
	public JFrame createJFrame(JPanel topButtonsPanel, JPanel overallJPanel, String titleOfCurPartOfProgram) {
	 * */
//	UNUSED/BUGGED => public JFrame insertAtTopOfJFrame(JPanel panelToInsert, JFrame jframe) {
	
	//Currently Unused
//	public final double fullScreenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
//	public final double fullScreenHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	protected String progTitle;
	
	public static Font commonFont1 = new Font(null, Font.BOLD, 28);
	public static Font commonFont2 = new Font(null, Font.BOLD, 36);
	public static Font commonFont3 = new Font(null, Font.BOLD, 14);
	public static Font commonFont4 = new Font(null, Font.BOLD, 72);
	public static Font commonFont5 = new Font(null, Font.BOLD, 10);
	
	public ButtonMaker(String progTitle) {
		this.progTitle = progTitle;
	}
	
	public JLabel createEmptyJLabel(Font font) {
		JLabel empty = createJLabel("", 0, " ", font);
		return empty;
	}
	
	/**
	 * Creates a button.
	 * @param imageIcon The full path of the image, including the extension; if null, don't generate any icon
	 * @param bttnTitle The name of the button when it's hovered over; if no image, also the display text, as well
	 * @param scaleOfIcon How big the image should be; 1.0 for same-size as the original
	 * @param buttonFunctionality The ActionEvent version of what the button does, in terms of function
	 * @return
	 */
	public JButton createButton(String imgName, String bttnTitle,
			double scaleOfIcon, ActionListener buttonFunctionality) {
		JButton jButton = new JButton(bttnTitle);
		if ( !(imgName == null || imgName.trim().isEmpty()) ) {
			Icon buttonIcon = createIcon_forButtons(imgName, scaleOfIcon);
			jButton = new JButton(buttonIcon);
		}
		jButton.setToolTipText(bttnTitle);
		jButton.setName(bttnTitle);
		jButton.addActionListener(buttonFunctionality);
		return jButton;
	}
	
	protected Icon createIcon_forButtons(String imageIcon, double scale)
	{
		Icon newImageIcon = new ImageIcon(imageIcon);
		ImageIcon newImage = new ImageIcon(imageIcon);
		int newWidth = (int) (scale * newImage.getIconWidth());
		int newHeight = (int) (scale * newImage.getIconHeight());
		if (newWidth != 0)
		{
			Image resizedImage = newImage.getImage().getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH);
			newImageIcon = new ImageIcon(resizedImage);
		}
		return newImageIcon;
	}
	
	public int getImgWidth(String imageIcon) {
		try {
			BufferedImage bufferedImage = ImageIO.read(new File(imageIcon));
			return bufferedImage.getWidth();
		} catch (IOException e) {
			System.out.println("Error! Could not find or read properly the file:\n\t" + imageIcon);
			return 0;
		}
	}
	
	public int getImgHeight(String imageIcon) {
		try {
			BufferedImage bufferedImage = ImageIO.read(new File(imageIcon));
			return bufferedImage.getHeight();
		} catch (IOException e) {
			System.out.println("Error! Could not find or read properly the file:\n\t" + imageIcon);
			return 0;
		}
	}
	
	/**
	 * Creates an image icon. This is for non-buttons that are still images.
	 * @param imageIcon The full path of the image to generate as an icon, including the extension, e.g. ".png" at the end
	 * @param scale How big the image should be; 1.0 for same-size as the original
	 * @return
	 */
	protected ImageIcon createIcon(String imageIcon, double scale)
	{
		ImageIcon newImage = new ImageIcon(imageIcon);
		int newWidth = (int) (scale * newImage.getIconWidth());
		int newHeight = (int) (scale * newImage.getIconHeight());
		if (newWidth != 0)
		{
			Image resizedImage = newImage.getImage().getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH);
			newImage = new ImageIcon(resizedImage);
		}
		return newImage;
	}
	
	/**
	 * Creates a JLabel.
	 * @param imgName The full path of the image, including the extension; if null, don't generate any icon
	 * @param scale How big the image should be; 1.0 for same-size as the original
	 * @param bottomText The text for the JLabel
	 * @param font The font to display the JLabel text
	 * @return
	 */
	public JLabel createJLabel(String imgName, double scale, String bottomText, Font font) {
		ImageIcon imageIcon = createIcon(imgName, scale);
		if (imgName == null || imgName.trim().isEmpty()) {
			JLabel newJLabel_noImg = new JLabel(bottomText);
			newJLabel_noImg.setFont(font);
			return newJLabel_noImg;
		}
		JLabel newJLabel = new JLabel(bottomText, imageIcon, 0);
		newJLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		newJLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
		newJLabel.setFont(font);
		return newJLabel;
	}
	
	protected JPanel addScrollPane(JPanel panel, int numComponents, Font font) {
		JScrollPane jScrollPane = new JScrollPane(panel);
		Dimension panelSize = panel.getPreferredSize();
		Dimension originalSize = panel.getPreferredSize();
		double width = panelSize.getWidth() + 50;
		int fontMultiplier = (font.getSize());
		double height = panelSize.getHeight() + (numComponents * fontMultiplier);
		panelSize.setSize(width, height);
		jScrollPane.setPreferredSize(originalSize);
		panel.setPreferredSize(panelSize);
		jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JPanel contentPane = new JPanel();
		contentPane.add(jScrollPane);
		return contentPane;
	}
	
	public JPanel createGenericJPanel(boolean isHorizontal, boolean hasScrollbar,
			int numComponentsOfChildPanels, Font font, Component ... objectsToAdd) {
		JPanel jPanel = new JPanel();
		if (!isHorizontal) {
			jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
		}
		for (Component curObject : objectsToAdd) {
			jPanel.add(curObject);
		}
		return (hasScrollbar ? addScrollPane(jPanel, numComponentsOfChildPanels, font) : jPanel);
	}
	
	public JPanel createGenericSingleNestedJPanel(JPanel panelToNest) {
		JPanel parentJPanel = new JPanel();
		parentJPanel.add(panelToNest);
		return parentJPanel;
	}
	
	public JFrame createJFrame(JPanel topButtonsPanel, JPanel overallJPanel, String titleOfCurPartOfProgram) {
		//Set Up JFrame
		JFrame mainJFrame = new JFrame();
		mainJFrame.toFront();
		mainJFrame.setTitle(this.progTitle + titleOfCurPartOfProgram);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		mainJFrame.setSize(screenSize);
		mainJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Add Overall JPanel to JFrame, and Show It
		mainJFrame.setLayout(new BoxLayout(mainJFrame.getContentPane(), BoxLayout.Y_AXIS));
		if (topButtonsPanel != null) {
			mainJFrame.add(topButtonsPanel);
		}
		if (overallJPanel != null) {
			mainJFrame.add(overallJPanel);
		}
		mainJFrame.setVisible(true);
		return mainJFrame;
	}
	
//	public JFrame insertAtTopOfJFrame(JPanel panelToInsert, JFrame jframe) {
//		jframe.setVisible(false);
//		jframe.add(panelToInsert, 0);
//		jframe.revalidate();
//		jframe.repaint();
//		jframe.setVisible(true);
//		return jframe;
//	}
	
	
	/**
	 * For testing GUI functionality. Leaving this test method for GUI functions, in case GUI gets edited in the future.
	 * @param args
	 */
	public static void main(String[] args) {
//		ButtonMaker buttonMaker = new ButtonMaker("X86-Interpreter: ");
//		String testImgString = "Path/To/Example/Image/Here.png";
		//The OLD Versions of the Calls!
//		JPanel testPanel1 = buttonMaker.createGenericJPanel(false, false, 0,
//				buttonMaker.createButton(testImgString, "Button Title", 2.0, event -> {
//					System.out.println("Button Was Pressed!");
//				}),
//				buttonMaker.createJLabel(testImgString, 4.0, "JLabel Title", ButtonMaker.commonFont1)
//				);
//		JPanel testPanel2 = buttonMaker.createGenericJPanel(false, true, 2, new JButton("Test Button"));
//		buttonMaker.createJFrame(testPanel2, testPanel1, "Example Screen...");

	}

}
