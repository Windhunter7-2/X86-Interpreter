package model.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import util.converters.ToStringUtils;

public class Stack {
	
	//Note - These Are the Common Values at Each Address, but Nothing Specific for These Is in Code Here:
	/* 
	 * MemAddress 4     ==> Return Address
	 * MemAddress > 4   ==> Parameters (e.g. Function Arguments)
	 * MemAddress >= 0  ==> Temporary Variables
	 * 
	 * */
	
	/**
	 * ebp Register
	 */
	private int basePointer;
	
	/**
	 * esp Register
	 */
	private int stackPointer;
	
	/**
	 * Has the following mapping for each element in the list:
	 * "index" -> Primarily a "just in case" scenario standardized index
	 * "memAddress" -> The primary index, which external classes would think of as the address in memory
	 * "value" -> The value stored at the location in memory
	 */
	private ArrayList<Map<String, Integer>> stack;
	
	public Stack() {
		basePointer = 0;
		stackPointer = 0;
		stack = new ArrayList<Map<String,Integer>>();
	}
	
	public void push(int value) {
		//Setup for the New Element
		stackPointer -= 4;
		Map<String, Integer> newElement = createElement(stackPointer, value);
		
		//if (exists) { insert }
		Map<String, Integer> curElement = null;
		boolean foundAddress = false;
		for (int i = 0; i < this.stack.size(); i++) {
			//Set Initial Replacement with New Element
			if (this.stack.get(i).get("memAddress") == stackPointer) {
				curElement = this.stack.get(i);
				this.stack.set(i, newElement);
				foundAddress = true;
				if (i == this.stack.size()-1) {
					return;	//Return if Found Address Is Last
				}
				continue;
			}
			
			//Set Every Element Afterwards with a Shifted Index/Address
			if (foundAddress) {
				//All Elements
				Map<String, Integer> shiftedElement = curElement;
				shiftedElement.replace(
					"memAddress",
					this.stack.get(i).get("memAddress")
				);
				curElement = this.stack.get(i);
				
				//All Elements
				this.stack.set(i, shiftedElement);
				
				//Last Element Only
				if (i == this.stack.size()-1) {
					Map<String, Integer> shiftedCurElement = curElement;
					shiftedCurElement.replace(
						"memAddress",
						this.stack.get(i).get("memAddress") - 4
					);
					this.stack.add(shiftedCurElement);
					return;
				}
			}
		}
		
		//else { append }
		this.stack.add(newElement);
		return;
	}
	
	public int pop() {
		//Remove Value
		int value = getValAtMemAddress(stackPointer);
		boolean wasSuccess = false;
		for (int i = 0; i < this.stack.size(); i++) {
			//if (esp == address of current element) { get and return }
			if (this.stack.get(i).get("memAddress") == stackPointer) {
				this.stack.remove(i);
				wasSuccess = true;
				break;
			}
		}
		
		//Shift Addresses As Needed
		if (wasSuccess) {
			for (int i = 0; i < this.stack.size(); i++) {
				//if (esp > address of current element) { increment address }
				if (this.stack.get(i).get("memAddress") < stackPointer) {
					this.stack.get(i).replace(
						"memAddress",
						this.stack.get(i).get("memAddress") + 4
					);
				}
			}
			stackPointer += 4;
		}
		return value;
	}
	
	/**
	 * Retrieves the value at the given memory address. If that data does not yet exist, return 0 instead.
	 * @param memAddress
	 * @return
	 */
	public int getFromMemAddress(int memAddress) {
		return getValAtMemAddress(memAddress);
	}
	
	/**
	 * Sets the value at the given memory address. If that data does not yet exist,
	 * ...
	 * Edit: Originally pushed new data there instead; changed to instead create data there directly, *without*
	 * changing any pointer locations!
	 * @param memAddress
	 * @param value
	 * @return
	 */
	public void setAtMemAddress(int memAddress, int value) {
		setValAtMemAddress(memAddress, value);
	}
	
	private Map<String, Integer> createElement(int memAddress, int value) {
		Map<String, Integer> element = new HashMap<String, Integer>();
		element.put("memAddress", memAddress);
		element.put("value", value);
		return element;
	}
	
	private int getValAtMemAddress(int memAddress) {
		for (Map<String, Integer> element : this.stack) {
			int key = element.get("memAddress");
			if (key == memAddress) {
				return element.get("value");
			}
		}
		System.out.println("Warning! Could not find anything at location 0x"
			+ ToStringUtils.padByteWithZeros(memAddress) + "! This is fine if it hasn't been set yet!");
		return 0;	//If Doesn't Exist, Just Assume Unset, AKA 0
	}
	
	private void setValAtMemAddress(int memAddress, int value) {
		for (Map<String, Integer> element : this.stack) {
			int key = element.get("memAddress");
			if (key == memAddress) {
				element.put("value", value);
				return;
			}
		}
		System.out.println("Warning! Could not find anything at location 0x"
			+ ToStringUtils.padByteWithZeros(memAddress) + "! Inserting anyways though!");
		int tempEsp = stackPointer;
		stackPointer = (memAddress + 4);
		push(value);
		stackPointer = tempEsp;
	}
	
	public int getEbp() {
		return basePointer;
	}
	
	public int getEsp() {
		return stackPointer;
	}
	
	public void setEbp(int ebpVal) {
		this.basePointer = ebpVal;
	}
	
	public void setEsp(int espVal) {
		this.stackPointer = espVal;
	}
	
	public void setStack(ArrayList<Map<String, Integer>> stack) {
		this.stack = stack;
	}
	
	@Override
	public String toString() {
		String lineSeparator = ToStringUtils.createLineSeparatorForTable(false);
		String endLineSeparator = ToStringUtils.createLineSeparatorForTable(true);
		String map = endLineSeparator;
		boolean ebpFound = false;	//Whether ebp Was Found in the Current Stack; False Most of the Time!
		for (int i = this.stack.size()-1; i >= 0; i--) {
			Map<String, Integer> element = this.stack.get(i);
			if (element.get("memAddress") == basePointer) {
				ebpFound = true;
			}
			map += (
				"\n" + ToStringUtils.createContentsOfTable(element.get("memAddress"), element.get("value"))
				+ (element.get("memAddress") == basePointer
					? (
						element.get("memAddress") == stackPointer
							? " <--- ebp, esp"
							: " <--- ebp"
					)
					: (
						element.get("memAddress") == stackPointer
							? " <--- esp"
							: ""
					)
				)
				+ (
					(!ebpFound && i == 0)
						? "\n" + lineSeparator + "\n" + ToStringUtils.createContentsOfTable(element.get("memAddress")+4, 0)
							+ " <--- ebp"
						: ""
				)
				+ "\n" + (i == 0 ? endLineSeparator : lineSeparator)
			);
		}
		return map;
	}
	
	public Stack clone() {
		Stack newStack = new Stack();
		newStack.setEbp(this.basePointer);
		newStack.setEsp(this.stackPointer);
		ArrayList<Map<String, Integer>> stackClone = new ArrayList<Map<String,Integer>>();
		for (Map<String, Integer> curMap : this.stack) {
			Map<String, Integer> curMapClone = new HashMap<String, Integer>();
			curMapClone.putAll(curMap);
			stackClone.add(curMapClone);
		}
		newStack.setStack(stackClone);
		return newStack;
	}
	
	public int getNumStackEntries() {
		return this.stack.size();
	}
	
}
