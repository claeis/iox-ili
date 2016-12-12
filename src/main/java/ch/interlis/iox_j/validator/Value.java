package ch.interlis.iox_j.validator;

import ch.interlis.iom.IomObject;

public class Value {
	
	private static boolean booleanValue;
	private static Value stringValue;
	private static IomObject iomObj;
	private static String left;
	private static String right;
	
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
		return new Value(false);
	}
	
	public boolean isTrue(){
		return booleanValue;
	}
	
	// if boolean is false, return true. --> left boolean is false, so skip the evaluation.
	public boolean skipEvaluation(){
		if (booleanValue == false){
			return true;
		} else {
			return false;
		}
	}

	public static void safeLeftValue() {
		left = "true";
	}

	public static void safeRightValue() {
		right = "true";
	}
	
	public static String getLeftValue(){
		return left;
	}
	
	public static String getRightValue(){
		return right;
	}
}