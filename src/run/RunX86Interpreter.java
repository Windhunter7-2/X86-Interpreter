package run;

import gui.MenuGUI;
import util.run.MainDirectoryInfo;

public class RunX86Interpreter {
	
	private final String rootDirectory = "X86-Interpreter/";
	private final String x86ProgTitle = "X86 Interpreter";
	
	public void runX86Interpreter() {
		String rootDirectory = getRootDirectory();
		MenuGUI menuGUI = new MenuGUI(rootDirectory, this.x86ProgTitle);
		menuGUI.startMainMenu();
	}
	
	private String getRootDirectory() {
		MainDirectoryInfo mainDirectoryInfo = new MainDirectoryInfo();
		mainDirectoryInfo.setMainDirectory();
		return (mainDirectoryInfo.getMainDirectory() + "/" + this.rootDirectory);
	}
	
	//Main Method of the Program; Runs the Program
	public static void main(String[] args) {
		RunX86Interpreter main = new RunX86Interpreter();
		main.runX86Interpreter();
	}

}
