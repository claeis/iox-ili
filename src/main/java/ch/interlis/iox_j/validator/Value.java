package ch.interlis.iox_j.validator;

import java.util.Collection;
import java.util.List;
import ch.interlis.ili2c.metamodel.EnumerationType;
import ch.interlis.ili2c.metamodel.FormattedType;
import ch.interlis.ili2c.metamodel.NumericType;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.ili2c.metamodel.TextType;
import ch.interlis.ili2c.metamodel.Type;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.iom.IomObject;

public class Value {
	// boolean
	private boolean booleanIsDefined=false;
	private boolean booleanValue;
	// numeric
	private double numeric=0;
	private boolean numericIsDefined=false;
	// type & value
	private Type type=null;
	private String value=null;
	private String[] values=null;
	private String refTypeName;
	private List<IomObject> complexObjects;
	private RoleDef role=null;
	private String oid=null;
	private Viewable viewable=null;
	
	// not yet implemented
	private boolean notYetImplemented=false;
	// error
	private boolean error=false;
	
	public Value(boolean booleanValue) {
		this.booleanValue = booleanValue;
		booleanIsDefined = true;
	}
	
	public Value(double numeric){
		this.numeric = numeric;
		numericIsDefined = true;
	}
	
	public Value(Viewable viewable){
		this.viewable = viewable;
	}
	
    public static Value createOidValue(String oid){
        Value ret=new Value();
        ret.oid=oid;
        return ret;
    }
	public Value(Type type,String valueStr){
		this.value = valueStr;
		this.type=type;
	}
	
    public Value(Type type,String[] valueStr){
        if(valueStr.length==1) {
            this.value=valueStr[0];
        }else {
            this.values = valueStr;
        }
        this.type=type;
    }
	
	public Value(List<IomObject> complexObjects){
		this.complexObjects = complexObjects;
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
	
    public String getOid(){
        if(skipEvaluation()){
            throw new IllegalArgumentException();
        }
        return oid;
    }
	
	public String getValue(){
		if(skipEvaluation()){
			throw new IllegalArgumentException();
		}
		return value;
	}
	
    public String[] getValues(){
        if(skipEvaluation()){
            throw new IllegalArgumentException();
        }
        return values;
    }
	
	public double getNumeric(){
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
	
	public Collection<IomObject> getComplexObjects(){
		if(skipEvaluation()){
			throw new IllegalArgumentException();
		}
		return complexObjects;
	}
	
	public static Value createUndefined(){
		return new Value((List<IomObject>) null);
	}
	
	public boolean isUndefined(){
		return !(getValue() != null ||
						getComplexObjects() != null ||
						booleanIsDefined ||
						getRole() != null ||
						oid != null ||
						numericIsDefined ||
						getViewable() != null || 
						getValues() != null);
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
	public double compareTo(Value other){
		// if value is error, return exception.
		if(skipEvaluation() || other.skipEvaluation()){
			throw new IllegalArgumentException();
		}
		// compare complex value
		if (this.complexObjects != null && other.complexObjects != null){
			for(IomObject iomObj : this.complexObjects){
				for(IomObject iomObjOther : other.complexObjects){
					// compare coords and arcs
					if((iomObj.getobjecttag().equals("COORD") && iomObjOther.getobjecttag().equals("COORD"))
							|| (iomObj.getobjecttag().equals("ARC") && iomObjOther.getobjecttag().equals("ARC"))){
						return compareCoordsTo(iomObj, iomObjOther);
					}
					// compare polylines
					if(iomObj.getobjecttag().equals("POLYLINE") && iomObjOther.getobjecttag().equals("POLYLINE")){
						return comparePolylineTo(iomObj, iomObjOther);
					}
					// compare surfaces and areas
					if(iomObj.getobjecttag().equals("MULTISURFACE") && iomObjOther.getobjecttag().equals("MULTISURFACE")){
						return compareSurfaceOrAreaTo(iomObj, iomObjOther);
					} else {
					    return compareIomObjTo(iomObj, iomObjOther);
					}
				}
			}
		}

		if (this.values != null && other.values != null) {
		    if (this.values.length != other.values.length) {
		        return -1;
		    } 
            for (int i=0;i<values.length;i++) {
                int result = compareSimpleValue(this.values[i], other.values[i]);
                if (result != 0) {
                    return result;
                }
            }
            return 0;               
		} 
		// compare simple value
		if(this.value!=null && other.value!=null){
			if(type instanceof NumericType){
				return Double.valueOf(value).compareTo(Double.valueOf(other.value));
			// compare text
			} else if(type instanceof TextType){
			    return value.compareTo(other.value);
			// compare formatted type
			} else if(type instanceof FormattedType){
				return value.compareTo(other.value);
			// compare enumeration type
			} else if(type instanceof EnumerationType){
				EnumerationType enumeration = (EnumerationType) type;
				// if ordered = true (>,<,>=,<=)
				if(enumeration.isOrdered() && !enumeration.isCircular()){
					int thisIndex = enumeration.getValues().indexOf(this.value);
					int otherIndex = enumeration.getValues().indexOf(other.value);
					return compareDouble(thisIndex, otherIndex);
				} else {
					// if ordered = false (==, !=, <>)
					return this.value.compareTo(other.value);
				}
			}
		// compare numeric
		} else if(this.numericIsDefined && other.numericIsDefined){
			return compareDouble(numeric, other.numeric);
		} else if(this.numericIsDefined && other.value!=null){
			return compareDouble(numeric, Double.valueOf(other.value));
		} else if(this.value!=null && other.numericIsDefined){
			return compareDouble(Double.valueOf(this.value), other.numeric);
		} else if (this.oid!=null && other.oid!=null) {
		    return compareOid(this.oid, other.oid);
		} else if(this.viewable!=null && other.viewable!=null){
			return compareViewable(this.viewable, other.viewable);
		}else if(this.value==null && other.value==null){
			return compareBoolean(this.booleanValue, other.booleanValue);
		}
		// incompatible type
		throw new IllegalArgumentException("incompatible values");
	}

    private double compareIomObjTo(IomObject iomObj, IomObject iomObjOther) {
        if (iomObj.getattrcount() != iomObjOther.getattrcount()) {
            return -1;
        }
        
        if (iomObj.getobjectrefbid() != null && iomObjOther.getobjectrefbid() != null) {
            if (!iomObj.getobjectrefbid().equals(iomObjOther.getobjectrefbid())) {
                return -1;
            }
        } else if ((iomObj.getobjectrefbid() != null && iomObjOther.getobjectrefbid() == null) ) {
            return 1;
        } else if ((iomObj.getobjectrefbid() == null && iomObjOther.getobjectrefbid() != null)) {
            return -1;
        }
        
        if (iomObj.getobjectoid() != null && iomObjOther.getobjectoid() != null) {
            if (!iomObj.getobjectoid().equals(iomObjOther.getobjectoid())) {
                return -1;
            }
        } else if (iomObj.getobjectoid() != null && iomObjOther.getobjectoid() == null) {
            return 1;
        } else if (iomObj.getobjectoid() == null && iomObjOther.getobjectoid() != null) {
            return -1;
        }
        
        if (iomObj.getobjecttag() != null && iomObjOther.getobjecttag() != null) {
            if (!iomObj.getobjecttag().equals(iomObjOther.getobjecttag())) {
                return -1;
            }            
        } else if (iomObj.getobjecttag() != null && iomObjOther.getobjecttag() == null) {
            return 1;
        } else if (iomObj.getobjecttag() == null && iomObjOther.getobjecttag() != null) {
            return -1;
        }

        for (int i = 0; i < iomObj.getattrcount(); i++) {
            String attrNameIomObj = iomObj.getattrname(i);
            
            //Has it the same values in two elements?
            int actualCount = iomObj.getattrvaluecount(attrNameIomObj);
            int otherCount = iomObjOther.getattrvaluecount(attrNameIomObj);
            if (actualCount != otherCount) {
                return -1;
            }
            for (int j = 0; j < actualCount; j++) {
                IomObject iomStructActual = iomObj.getattrobj(attrNameIomObj,j);
                IomObject iomStructOther = iomObjOther.getattrobj(attrNameIomObj,j);
                //Simple Value
                if (iomStructActual == null && iomStructOther == null) {
                    String attrNameValue = iomObj.getattrvalue(attrNameIomObj);
                    String attrNameValueOther = iomObjOther.getattrvalue(attrNameIomObj);
                    if (!attrNameValue.equals(attrNameValueOther)) {
                        return -1;
                    }
                } else {
                    //Complex Value
                    double returnValue = compareIomObjTo(iomStructActual, iomStructOther);
                    if (returnValue != 0) {
                        return returnValue;
                    }
                }
            }
        }
        return 0;
    }

    private int compareSimpleValue(String thisValue, String otherValue) {
        if ((thisValue != null && otherValue != null) || (!thisValue.isEmpty() && !otherValue.isEmpty())) {
            if (!thisValue.equals(otherValue)) {
                return -1;
            }                
        } else if ((thisValue != null && otherValue == null) || (!thisValue.isEmpty() && otherValue.isEmpty())) {
            return 1;
        } else if (thisValue == null && otherValue != null || (thisValue.isEmpty() && !otherValue.isEmpty())) {
            return -1;
        }
        return 0;
    }
    private int compareViewable(Viewable viewable2, Viewable viewable3) {
		return (viewable3.equals(viewable2) ? 0 : (!viewable3.equals(viewable2) ? 1 : -1));
	}

	private int compareBoolean(boolean thisValue, boolean otherValue) {
		return (otherValue == thisValue ? 0 : (thisValue ? 1 : -1));
	}

	private double compareDouble(double thisVal, double anotherVal) {
		return (thisVal<anotherVal ? -1 : (thisVal==anotherVal ? 0 : 1));
	}
	
	private int compareOid(String oid1, String oid2) {
	    return oid1.equals(oid2) ? 0 : -1;
	}

	private int compareSurfaceOrAreaTo(IomObject complexValue2, IomObject iomObjOther2) {
		for(int l=0;l<complexValue2.getattrvaluecount("surface");l++){
			IomObject thisSurfaceValue=complexValue2.getattrobj("surface",l);
			IomObject otherSurfaceValue=iomObjOther2.getattrobj("surface",l);
			for(int m=0;m<thisSurfaceValue.getattrvaluecount("boundary");m++){
				IomObject thisBoundaryValue = thisSurfaceValue.getattrobj("boundary",m);
				IomObject otherBoundaryValue = otherSurfaceValue.getattrobj("boundary", m);
				for(int n=0;n<thisBoundaryValue.getattrvaluecount("polyline");n++){
					IomObject thisPolylineValue = thisBoundaryValue.getattrobj("polyline",n);
					IomObject otherPolylineValue = otherBoundaryValue.getattrobj("polyline",n);
					if (comparePolylineTo(thisPolylineValue, otherPolylineValue) !=0){
						return comparePolylineTo(thisPolylineValue, otherPolylineValue);
					}
				}
			}
		}
		return 0;
	}
	
	private int comparePolylineTo(IomObject iomObj, IomObject iomObjOther) {
		for(int l=0;l<iomObj.getattrvaluecount("sequence");l++){
			IomObject thisSegmentsValue=iomObj.getattrobj("sequence",l);
			IomObject otherSegmentsValue=iomObjOther.getattrobj("sequence",l);
			for(int m=0;m<thisSegmentsValue.getattrvaluecount("segment");m++){
				IomObject thisCoord=thisSegmentsValue.getattrobj("segment",m);
				IomObject otherCoord=otherSegmentsValue.getattrobj("segment",m);
				if (compareCoordsTo(thisCoord, otherCoord) !=0){
					return compareCoordsTo(thisCoord, otherCoord);
				}
			}
		}
		return 0;
	}
	
	private int compareCoordsTo(IomObject complexValue, IomObject iomObjOther){
		// if complexValue, return boolean of complex value comparison.
		if(complexValue.getattrvalue("C1") != null && iomObjOther.getattrvalue("C1") != null){
			if (complexValue.getattrvalue("C1").compareTo(iomObjOther.getattrvalue("C1"))!=0){
				return complexValue.getattrvalue("C1").compareTo(iomObjOther.getattrvalue("C1"));
			}
		}
		if(complexValue.getattrvalue("C2") != null && iomObjOther.getattrvalue("C2") != null){
			if (complexValue.getattrvalue("C2").compareTo(iomObjOther.getattrvalue("C2"))!=0){
				return complexValue.getattrvalue("C2").compareTo(iomObjOther.getattrvalue("C2"));
			}
		}
		if(complexValue.getattrvalue("C3") != null && iomObjOther.getattrvalue("C3") != null){
			if (complexValue.getattrvalue("C3").compareTo(iomObjOther.getattrvalue("C3"))!=0){
				return complexValue.getattrvalue("C3").compareTo(iomObjOther.getattrvalue("C3"));
			}
		}
		if(complexValue.getattrvalue("A1") != null && iomObjOther.getattrvalue("A1") != null){
			if (complexValue.getattrvalue("A1").compareTo(iomObjOther.getattrvalue("A1"))!=0){
				return complexValue.getattrvalue("A1").compareTo(iomObjOther.getattrvalue("A1"));
			}
		}
		if(complexValue.getattrvalue("A2") != null && iomObjOther.getattrvalue("A2") != null){
			if (complexValue.getattrvalue("A2").compareTo(iomObjOther.getattrvalue("A2"))!=0){
				return complexValue.getattrvalue("A2").compareTo(iomObjOther.getattrvalue("A2"));
			}
		}
		return 0;
	}
}