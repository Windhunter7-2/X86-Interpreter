package util.converters;

public class Filters {
	
	public static String filterOnlyOneHexForm(String hexNumber) {
		//Boolean Checks
		boolean has_0x = false;
		boolean has_h = false;
		if (hexNumber.length() >= 2) {
			if (hexNumber.charAt(0) == '0' && (
					hexNumber.charAt(1) == 'x') || hexNumber.charAt(1) == 'X'
				) {
					has_0x = true;
				}
		}
		if (hexNumber.endsWith("h") || hexNumber.endsWith("H")) {
			has_h = true;
		}
		
		//Performing the Filter
		String filteredNum = "";
		for (int i = 0; i < hexNumber.length(); i++) {
			char curChar = hexNumber.charAt(i);
			boolean isAddChar = true;
			if (has_0x && has_h && (curChar == 'h' || curChar == 'H')) {
				isAddChar = false;
			}
			if (isAddChar) {
				filteredNum += curChar;
			}
		}
		return filteredNum;
	}
	
	public static String [] removeDollarSignsAndPercentSigns(String [] allOperands) {
		for (int i = 0; i < allOperands.length; i++) {
			allOperands[i] = allOperands[i].replaceAll("\\$|%", "");
		}
		return allOperands;
	}
	
}
