package util.converters;

import java.awt.Font;

public class ToStringUtils {
	
	public static Font fontResizer(Font originalFont, float sizeToRescale) {
		return originalFont.deriveFont(originalFont.getSize2D() * sizeToRescale);
	}

	public static String createContentsOfTable(int memAddress, int value) {
		return (padByteWithZeros(memAddress) + " | " + padByteWithZeros(value) + " |");
	}
	
	public static String createLineSeparatorForTable(boolean isEndLine) {
		return (isEndLine
			? "         ============"
			: "         |----------|"
		);
	}
	
	public static String createContainersAndContents(String [] locations, int [] values,
			boolean locationIsNumber, int numEntriesPerRow, int tabsInBetween) {
		String spacingInBetween_S = "";
		for (int i = 0; i < tabsInBetween; i++) {
			spacingInBetween_S += "\t";
		}
		String spacingInBetween_N = "";
		for (int i = 0; i < (tabsInBetween); i++) {
			spacingInBetween_N += "\n";
		}
		String [] midNumbers = new String[values.length];
		for (int i = 0; i < midNumbers.length; i++) {
			midNumbers[i] = ("| " + padByteWithZeros(values[i]) + " |");
		}
		String sidesOfBox = "|          |";
		String endsOfBox = "============";
		String [] locationsOfBoxes = new String[values.length];
		for (int i = 0; i < locationsOfBoxes.length; i++) {
			locationsOfBoxes[i] = padStringWithSpaces(locations[i]);
			if (locationIsNumber) {
				locationsOfBoxes[i] = padByteWithZeros(Integer.parseInt(locations[i]));
			}
			locationsOfBoxes[i] = ("  " + locationsOfBoxes[i] + "  ");
		}
		String overallRows = "";
		int counter = 0;	//Starts As the Same As i!
		String [] locationsOfCurRow = new String[numEntriesPerRow];
		String [] midNumsOfCurRow = new String[numEntriesPerRow];
		for (int i = 0; i < values.length; i++) {
			locationsOfCurRow[counter] = locationsOfBoxes[i];
			midNumsOfCurRow[counter] = midNumbers[i];
			counter++;
			if ( (counter == numEntriesPerRow) || (i == values.length-1) ) {
				if (i == values.length-1) {
					int finalNumEntries = (values.length % numEntriesPerRow);
					if (finalNumEntries != 0) {
						numEntriesPerRow = (values.length % numEntriesPerRow);
					}
				}
				counter = 0;
				overallRows = appendWithSimpleForLoop_createContainersAndContentsHelper(
					false, numEntriesPerRow, spacingInBetween_S, overallRows, endsOfBox);
				overallRows = appendWithSimpleForLoop_createContainersAndContentsHelper(
					false, numEntriesPerRow, spacingInBetween_S, overallRows, sidesOfBox);
				overallRows = appendWithSimpleForLoop_createContainersAndContentsHelper(
					true, numEntriesPerRow, spacingInBetween_S, overallRows, midNumsOfCurRow);
				overallRows = appendWithSimpleForLoop_createContainersAndContentsHelper(
					false, numEntriesPerRow, spacingInBetween_S, overallRows, sidesOfBox);
				overallRows = appendWithSimpleForLoop_createContainersAndContentsHelper(
					false, numEntriesPerRow, spacingInBetween_S, overallRows, endsOfBox);
				overallRows += "\n";
				overallRows = appendWithSimpleForLoop_createContainersAndContentsHelper(
					true, numEntriesPerRow, spacingInBetween_S, overallRows, locationsOfCurRow);
				overallRows += spacingInBetween_N;
			}
		}
		return overallRows;
	}
	
	private static String appendWithSimpleForLoop_createContainersAndContentsHelper(
			boolean isArray, int numEntriesPerRow, String spacingInBetween_S, String overallRows, String ... whatToAppend) {
		//For Single Values
		if (!isArray) {
			for (int j = 0; j < numEntriesPerRow; j++) {
				overallRows += whatToAppend[0] + (j == (numEntriesPerRow-1) ? "" : spacingInBetween_S);
			}
			overallRows += "\n";
			return overallRows;
		}
		
		//For Arrays
		if (numEntriesPerRow > whatToAppend.length) {
			numEntriesPerRow = whatToAppend.length;
		}
		for (int j = 0; j < numEntriesPerRow; j++) {
			if (whatToAppend[j] != null) {
				overallRows += whatToAppend[j] + (j == (numEntriesPerRow-1) ? "" : spacingInBetween_S);
			}
		}
		overallRows += "\n";
		return overallRows;
	}
	
	private static String padStringWithSpaces(String stringToPad) {
		int numChars = stringToPad.length();
		int numPad = 8 - numChars;
		for (int i = 0; i < (numPad / 2); i++) {
			stringToPad = (" " + stringToPad);
		}
		for (int i = (numPad / 2); i < numPad; i++) {
			stringToPad += " ";
		}
		return stringToPad;
	}
	
	//This Also Converts the Number to Hexadecimal Form, As Well!
	public static String padByteWithZeros(int number) {
		String number_S = "" + Integer.toHexString(number);
		int numPad = 8 - number_S.length();
		for (int i = 0; i < numPad; i++) {
			number_S = ("0" + number_S);
		}
		return number_S;
	}
	
}
