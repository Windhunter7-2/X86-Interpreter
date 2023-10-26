package util.run;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class MainDirectoryInfo {
	
	/**
	 * Gets the main directory for all of my programs.
	 * @return mainDirectory The main directory for all of my programs
	 */
	public String getMainDirectory()
	{
		//Variables
		String mainDirectory = "";
		String locationDataLoc = "C:/Users/Public/Windhunter's Programs - Main Folder Location.txt";
		
		//Read File Data
		try
		{
			BufferedReader fileReader = new BufferedReader( new FileReader(locationDataLoc) );
			String curLine = fileReader.readLine();
			while (curLine != null)
			{
				//Read Current Line
				mainDirectory = (mainDirectory + curLine);
				curLine = fileReader.readLine();
			}
			fileReader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		//Return
		return mainDirectory;
	}
	
	/**
	 * Sets the main directory for my programs, storing the data in a single-line text file. If this data already exists, returns true.
	 * It will also warn the user not to edit it.
	 * @return isAlreadySet Whether or not the directory is already set / the text file exists
	 */
	public boolean setMainDirectory()
	{
		//Setup Stuff
		boolean isAlreadySet = true;
		String mainDirectory = "";
		String locationDataLoc = "C:/Users/Public/Windhunter's Programs - Main Folder Location.txt";
		File locationData = new File(locationDataLoc);
		
		//If Already Existing, Return True
		if( (locationData.exists()) && (!locationData.isDirectory()) )
			return isAlreadySet;
		
		//Otherwise, Have User Select Directory
		else
		{
			//Have User Choose Folder & Warn User About Editing Settings
			JOptionPane.showMessageDialog(null, "Please use the next GUI to select where you want all of Windhunter's programs installed."
					+ " You should only need to\ndo this once. A lengthier explanation is below, mostly as a warning about the settings."
					+ "\n\n\nThe file that says where the files for Windhunter's programs is missing or not yet"
					+ " created. Please use the\nnext GUI to select which folder you would like to use to store all the programs and"
					+ " program data for\nWindhunter's programs. Keep in mind that you can always add shortcuts to programs; the location"
					+ " data is mainly for\nconsistency, as well as for storage of any necessary files that certain programs and mini-"
					+ "programs use. Also, this\nlocation data of where these programs will be stored is going to be the text file\n\""
					+ "C:/Users/Public/Windhunter's Programs - Main Folder Location.txt\". Since this text file is for a major setting,"
					+ " please do not delete or\nedit it. If you make a mistake and edit it, or think you have edited it, you can delete"
					+ " the file, and the next\ntime you run any of Windhunter's programs, this prompt will appear again; otherwise, this"
					+ " is the only time you\nneed to do it, as you only should need to do it once. :)",
					"Select Directory for\nWindhunter's Programs", JOptionPane.WARNING_MESSAGE);
			mainDirectory = chooseFolder();
			
			//Write Location Data to File
			try
			{
				BufferedWriter fileWriter = new BufferedWriter( new FileWriter(locationDataLoc) );
				fileWriter.write(mainDirectory);
				fileWriter.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			//Return
			isAlreadySet = false;
			return isAlreadySet;
		}
	}
	
	/**
	 * Helper method for setMainDirectory(). Has the user select which directory to use as the main directory for my programs.
	 * @return mainDirectory The directory to use for my programs
	 */
	private String chooseFolder()
	{
		JFileChooser jFileChooser = new JFileChooser();
		jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jFileChooser.setCurrentDirectory(new File (System.getProperty("user.home") ));
		int whichFolder = jFileChooser.showOpenDialog(null);
		String mainDirectory = "";
		if (whichFolder == JFileChooser.APPROVE_OPTION)
		{
			File selectedFolder = jFileChooser.getSelectedFile();
			mainDirectory = selectedFolder.getAbsolutePath();
			mainDirectory = mainDirectory.replaceAll("\\\\", "/");
		}
		else
			mainDirectory = "C:/Users/Public/Windhunter's Programs";
		mainDirectory = (mainDirectory + "/");
		return mainDirectory;
	}

	/**
	 * Main method. Strictly for testing.
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		
		MainDirectoryInfo directory = new MainDirectoryInfo();
		directory.setMainDirectory();
		System.out.println(
				directory.getMainDirectory()
		);

	}

}
