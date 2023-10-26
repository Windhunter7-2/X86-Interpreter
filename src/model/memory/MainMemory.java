package model.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import util.converters.ToStringUtils;

public class MainMemory {
	
	//The First Integer Is Which Memory Address Existing, and the Second Is the Value at That Address
	private Map<Integer, Integer> mainMemory;
	
	/**
	 * This is a copy of the pointer to the stack. The main pointer to the stack is in the OverallRegisters class.
	 */
	private Stack stack;
	
	private final int toString_numEntriesPerRow = 3;
	private final int toString_tabsInBetween = 2;
	
	public MainMemory(Stack stack) {
		this.mainMemory = new HashMap<Integer, Integer>();
		this.stack = stack;
	}
	
	public int getValueAtMemoryLocation(int memoryLocation) {
		if (mainMemory.containsKey(memoryLocation)) {
			return mainMemory.get(memoryLocation);
		}
		return stack.getFromMemAddress(memoryLocation);	//If Not Found Normally, Check Stack Memory
	}
	
	public void updateMemory(int memoryLocation, int newValue) {
		if (mainMemory.containsKey(memoryLocation)) {
			mainMemory.replace(memoryLocation, newValue);
			return;
		}
		mainMemory.put(memoryLocation, newValue);
	}
	
	public int getMainMemoryEntryCount() {
		return this.mainMemory.size();
	}
	
	public void setMainMemory(Map<Integer, Integer> mainMemory) {
		this.mainMemory = mainMemory;
	}
	
	public String toString_alt(int numToAddPerRow) {
		String [] keys = new String[this.mainMemory.entrySet().size()];
		int [] vals = new int[this.mainMemory.entrySet().size()];
		ArrayList<String> keys_list = new ArrayList<String>();
		for (Map.Entry<Integer, Integer> curEntry : this.mainMemory.entrySet()) {
			keys_list.add("" + curEntry.getKey());
		}
		Collections.sort(keys_list);
		for (int i = 0; i < keys.length; i++) {
			keys[i] = keys_list.get(i);
			vals[i] = this.mainMemory.get(Integer.parseInt(keys[i]));
		}
		return ToStringUtils.createContainersAndContents(keys, vals, true,
			(this.toString_numEntriesPerRow + numToAddPerRow), this.toString_tabsInBetween);
	}
	
	@Override
	public String toString() {
		String [] keys = new String[this.mainMemory.entrySet().size()];
		int [] vals = new int[this.mainMemory.entrySet().size()];
		ArrayList<String> keys_list = new ArrayList<String>();
		for (Map.Entry<Integer, Integer> curEntry : this.mainMemory.entrySet()) {
			keys_list.add("" + curEntry.getKey());
		}
		Collections.sort(keys_list);
		for (int i = 0; i < keys.length; i++) {
			keys[i] = keys_list.get(i);
			vals[i] = this.mainMemory.get(Integer.parseInt(keys[i]));
		}
		return ToStringUtils.createContainersAndContents(keys, vals, true,
			this.toString_numEntriesPerRow, this.toString_tabsInBetween);
	}
	
	/**
	 * Note: The stack passed in here should be a new (cloned) stack, not the original stack.
	 * @param newStack
	 * @return
	 */
	public MainMemory clone(Stack newStack) {
		Map<Integer, Integer> mainMemoryClone = new HashMap<Integer, Integer>();
		mainMemoryClone.putAll(mainMemory);
		MainMemory newMainMemory = new MainMemory(newStack);
		newMainMemory.setMainMemory(mainMemoryClone);
		return newMainMemory;
	}
	
	public int getToString_numEntriesPerRow() {
		return this.toString_numEntriesPerRow;
	}
	
	public int getToString_tabsInBetween() {
		return this.toString_tabsInBetween;
	}

}
