package ch.interlis.iox_j.validator;

import java.util.Collection;
import java.util.List;

import ch.interlis.ili2c.metamodel.Constant.Numeric;
import ch.interlis.ili2c.metamodel.EnumerationType;
import ch.interlis.ili2c.metamodel.FormattedType;
import ch.interlis.ili2c.metamodel.NumericType;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.ili2c.metamodel.TextType;
import ch.interlis.ili2c.metamodel.Type;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.iom.IomObject;

public class Value {
	private boolean notYetImplemented=false;
	private boolean error=false;
	private boolean booleanIsDefined=false;
	private boolean booleanValue;
	private IomObject complexValue=null; // léschen. bisherige Referenz auf complexObjects zuweisen. (Achtung Liste).
	
	private String refTypeName;
	private String value=null;
	private List<IomObject> complexObjects;
	private RoleDef role=null;
	private int numeric=0;
	private boolean numericIsDefined=false;
	private Viewable viewable=null;
	private Type type=null;
	private String functionName=null;
	
	public Value(boolean booleanValue) {
		this.booleanValue = booleanValue;
		booleanIsDefined = true;
	}
	
	public Value(int numeric){
		this.numeric = numeric;
		numericIsDefined = true;
	}
	
	public Value(Viewable viewable){
		this.viewable = viewable;
	}
	
	public Value(Type type,String valueStr){
		this.value = valueStr;
		this.type=type;
	}
	
	public Value(List<IomObject> complexObjects){
		this.complexObjects = complexObjects;
	}
	
	public Value(IomObject value){
		this.complexValue = value;
	}
	
	public Value(RoleDef role){
		this.role = role;
	}
	
	private Value(){
	}
	
	public Value(Type type, String valueStr, String refTypeName) {
		this.value = valueStr;
		this.type=type;
		this.refTypeName = refTypeName;
	}

	public boolean isTrue(){
		if(skipEvaluation()){
			throw new IllegalArgumentException();
		}
		return booleanValue;
	}
	
	public String getRefTypeName(){
		if(skipEvaluation()){
			throw new IllegalArgumentException();
		}
		return refTypeName;
	}
	
	public Viewable getViewable(){
		if(skipEvaluation()){
			throw new IllegalArgumentException();
		}
		return viewable;
	}
	
	public Type getType(){
		if(skipEvaluation()){
			throw new IllegalArgumentException();
		}
		return type;
	}
	
	public String getValue(){
		if(skipEvaluation()){
			throw new IllegalArgumentException();
		}
		return value;
	}
	
	public int getNumeric(){
		if(skipEvaluation()){
			throw new IllegalArgumentException();
		}
		return numeric;
	}
	
	public RoleDef getRole(){
		if(skipEvaluation()){
			throw new IllegalArgumentException();
		}
		return role;
	}
	
	public IomObject getComplexValue(){
		if(skipEvaluation()){
			throw new IllegalArgumentException();
		}
		return complexValue;
	}
	
	public Collection<IomObject> getComplexObjects(){
		if(skipEvaluation()){
			throw new IllegalArgumentException();
		}
		return complexObjects;
	}
	
	public static Value createUndefined(){
		return new Value((IomObject) null);
	}
	
	public boolean isUndefined(){
		return !(getComplexValue() != null ||
						getValue() != null ||
						getComplexObjects() != null ||
						booleanIsDefined ||
						getRole() != null ||
						numericIsDefined ||
						getViewable() != null);
	}
	
	public static Value createSkipEvaluation(){
		Value ret=new Value();
		ret.error = true;
		return ret;
	}
	
	public boolean isNotYetImplemented(){
		return notYetImplemented;
	}
	
	public static Value createNotYetImplemented() {
		Value ret=new Value();
		ret.notYetImplemented = true;
		return ret;
	}
	
	public boolean skipEvaluation(){
		if(notYetImplemented==true){
			return notYetImplemented;
		} else {
			return error;
		}
	}
	
	// compare in appropriate type.
	public int compareTo(Value other){
		// if value is error, return exception.
		if(skipEvaluation() || other.skipEvaluation()){
			throw new IllegalArgumentException();
		}
		// intercept complex value
		if (this.complexValue != null && other.complexValue != null){
			// intercept coords and arcs
			if((this.complexValue.getobjecttag().equals("COORD") && other.complexValue.getobjecttag().equals("COORD"))
				|| (this.complexValue.getobjecttag().equals("ARC") && other.complexValue.getobjecttag().equals("ARC"))){
				return compareCoordsTo(this.complexValue, other);
			}
			// intercept polylines
			if(this.complexValue.getobjecttag().equals("POLYLINE") && other.complexValue.getobjecttag().equals("POLYLINE")){
				return comparePolylineTo(this.complexValue, other);
			}
			// intercept surfaces and areas
			if(this.complexValue.getobjecttag().equals("MULTISURFACE") && other.complexValue.getobjecttag().equals("MULTISURFACE")){
				return compareSurfaceOrAreaTo(this.complexValue, other);
			}
		}
		// intercept value
		if(this.value!=null && other.value!=null){
			if(type instanceof NumericType){
				return Integer.valueOf(value).compareTo(Integer.valueOf(other.value));
			// intercept text
			} else if(type instanceof TextType){
				return value.compareTo(other.value);
			// intercept formatted type
			} else if(type instanceof FormattedType){
				return value.compareTo(other.value);
			// enumeration type
			} else if(type instanceof EnumerationType){
				EnumerationType enumeration = (EnumerationType) type;
				// if ordered = true (>,<,>=,<=)
				if(enumeration.isOrdered() && !enumeration.isCircular()){
					int thisIndex = enumeration.getValues().indexOf(this.value);
					int otherIndex = enumeration.getValues().indexOf(other.value);
					return compareInteger(thisIndex, otherIndex);
				} else {
					// if ordered = false (==, !=, <>)
					return this.value.compareTo(other.value);
				}
			}
		// intercept numeric
		} else if(this.numericIsDefined && other.numericIsDefined){
			return compareInteger(numeric, other.numeric);
		} else if(this.numericIsDefined && other.value!=null){
			return compareInteger(numeric, Integer.valueOf(other.value));
		} else if(this.value!=null && other.numericIsDefined){
			return compareInteger(Integer.valueOf(this.value), other.numeric);
		} else if(this.viewable!=null && other.viewable!=null){
			return compareViewable(this.viewable, other.viewable);
		}else if(this.value==null && other.value==null){
			return compareBoolean(this.booleanValue, other.booleanValue);
		}
		// incompatible type
		throw new IllegalArgumentException("incompatible values");
	}

	private int compareViewable(Viewable viewable2, Viewable viewable3) {
		return (viewable3.equals(viewable2) ? 0 : (!viewable3.equals(viewable2) ? 1 : -1));
	}

	private int compareBoolean(boolean thisValue, boolean otherValue) {
		return (otherValue == thisValue ? 0 : (thisValue ? 1 : -1));
	}

	private int compareInteger(int thisVal, int anotherVal) {
		return (thisVal<anotherVal ? -1 : (thisVal==anotherVal ? 0 : 1));
	}

	private int compareSurfaceOrAreaTo(IomObject complexValue2, Value other) {
		for(int l=0;l<complexValue2.getattrvaluecount("surface");l++){
			IomObject thisSurfaceValue=complexValue2.getattrobj("surface",l);
			IomObject otherSurfaceValue=other.complexValue.getattrobj("surface",l);
			for(int m=0;m<thisSurfaceValue.getattrvaluecount("boundary");m++){
				IomObject thisBoundaryValue = thisSurfaceValue.getattrobj("boundary",m);
				IomObject otherBoundaryValue = otherSurfaceValue.getattrobj("boundary", m);
				for(int n=0;n<thisBoundaryValue.getattrvaluecount("polyline");n++){
					IomObject thisPolylineValue = thisBoundaryValue.getattrobj("polyline",n);
					Value otherPolylineValue = new Value(otherBoundaryValue.getattrobj("polyline", n));
					if (comparePolylineTo(thisPolylineValue, otherPolylineValue) !=0){
						return comparePolylineTo(thisPolylineValue, otherPolylineValue);
					}
				}
			}
		}
		return 0;
	}
	
	private int comparePolylineTo(IomObject complexValue2, Value other) {
		for(int l=0;l<complexValue2.getattrvaluecount("sequence");l++){
			IomObject thisSegmentsValue=complexValue2.getattrobj("sequence",l);
			IomObject otherSegmentsValue=other.complexValue.getattrobj("sequence",l);
			for(int m=0;m<thisSegmentsValue.getattrvaluecount("segment");m++){
				IomObject thisCoord=thisSegmentsValue.getattrobj("segment",m);
				Value otherCoord = new Value(otherSegmentsValue.getattrobj("segment", m));
				if (compareCoordsTo(thisCoord, otherCoord) !=0){
					return compareCoordsTo(thisCoord, otherCoord);
				}
			}
		}
		return 0;
	}
	
	private int compareCoordsTo(IomObject complexValue, Value other){
		// if complexValue, return boolean of complex value comparison.
		if(complexValue.getattrvalue("C1") != null && other.complexValue.getattrvalue("C1") != null){
			if (complexValue.getattrvalue("C1").compareTo(other.complexValue.getattrvalue("C1"))!=0){
				return complexValue.getattrvalue("C1").compareTo(other.complexValue.getattrvalue("C1"));
			}
		}
		if(complexValue.getattrvalue("C2") != null && other.complexValue.getattrvalue("C2") != null){
			if (complexValue.getattrvalue("C2").compareTo(other.complexValue.getattrvalue("C2"))!=0){
				return complexValue.getattrvalue("C2").compareTo(other.complexValue.getattrvalue("C2"));
			}
		}
		if(complexValue.getattrvalue("C3") != null && other.complexValue.getattrvalue("C3") != null){
			if (complexValue.getattrvalue("C3").compareTo(other.complexValue.getattrvalue("C3"))!=0){
				return complexValue.getattrvalue("C3").compareTo(other.complexValue.getattrvalue("C3"));
			}
		}
		if(complexValue.getattrvalue("A1") != null && other.complexValue.getattrvalue("A1") != null){
			if (complexValue.getattrvalue("A1").compareTo(other.complexValue.getattrvalue("A1"))!=0){
				return complexValue.getattrvalue("A1").compareTo(other.complexValue.getattrvalue("A1"));
			}
		}
		if(complexValue.getattrvalue("A2") != null && other.complexValue.getattrvalue("A2") != null){
			if (complexValue.getattrvalue("A2").compareTo(other.complexValue.getattrvalue("A2"))!=0){
				return complexValue.getattrvalue("A2").compareTo(other.complexValue.getattrvalue("A2"));
			}
		}
		return 0;
	}
}