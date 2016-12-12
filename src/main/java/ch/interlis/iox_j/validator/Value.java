package ch.interlis.iox_j.validator;

import ch.interlis.iom.IomObject;

public class Value {
	
	private boolean booleanValue;
	private static Value stringValue;
	private static IomObject iomObj;
	
	public Value(boolean booleanValue) {
		this.booleanValue = booleanValue;
	}
	
	public Value(Value stringValue){
		this.stringValue = stringValue;
	}
	
	public Value(IomObject iomObj){
		this.iomObj = iomObj;
	}
	
	public static Value createSkipEvaluation(){
		// TODO
		return null;
	}
	
	public boolean isTrue(){
		return booleanValue;
	}
	
	// if boolean is false skip this evaluation.
	public boolean skipEvaluation(){
		if (booleanValue == false){
			return false;
		} else {
			return true;
		}
	}
}