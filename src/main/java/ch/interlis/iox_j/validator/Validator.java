package ch.interlis.iox_j.validator;

import java.beans.beancontext.BeanContextChildSupport;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import com.sun.xml.internal.fastinfoset.util.StringArray;
import com.vividsolutions.jts.algorithm.Angle;


import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.metamodel.AbstractClassDef;
import ch.interlis.ili2c.metamodel.AbstractLeafElement;
import ch.interlis.ili2c.metamodel.AreaType;
import ch.interlis.ili2c.metamodel.AssociationDef;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.AttributeRef;
import ch.interlis.ili2c.metamodel.Cardinality;
import ch.interlis.ili2c.metamodel.ClassType;
import ch.interlis.ili2c.metamodel.CompositionType;
import ch.interlis.ili2c.metamodel.ConditionalExpression;
import ch.interlis.ili2c.metamodel.Constant;
import ch.interlis.ili2c.metamodel.Constant.Enumeration;
import ch.interlis.ili2c.metamodel.Constant.EnumerationRange;
import ch.interlis.ili2c.metamodel.Constant.Text;
import ch.interlis.ili2c.metamodel.CoordType;
import ch.interlis.ili2c.metamodel.Domain;
import ch.interlis.ili2c.metamodel.Element;
import ch.interlis.ili2c.metamodel.EnumTreeValueType;
import ch.interlis.ili2c.metamodel.EnumerationType;
import ch.interlis.ili2c.metamodel.Evaluable;
import ch.interlis.ili2c.metamodel.ExistenceConstraint;
import ch.interlis.ili2c.metamodel.Expression;
import ch.interlis.ili2c.metamodel.Expression.Conjunction;
import ch.interlis.ili2c.metamodel.Expression.DefinedCheck;
import ch.interlis.ili2c.metamodel.Expression.Disjunction;
import ch.interlis.ili2c.metamodel.Expression.Equality;
import ch.interlis.ili2c.metamodel.Expression.GreaterThan;
import ch.interlis.ili2c.metamodel.Expression.GreaterThanOrEqual;
import ch.interlis.ili2c.metamodel.Expression.Inequality;
import ch.interlis.ili2c.metamodel.Expression.LessThan;
import ch.interlis.ili2c.metamodel.Expression.LessThanOrEqual;
import ch.interlis.ili2c.metamodel.Expression.Negation;
import ch.interlis.ili2c.metamodel.FormattedType;
import ch.interlis.ili2c.metamodel.FunctionCall;
import ch.interlis.ili2c.metamodel.InspectionFactor;
import ch.interlis.ili2c.metamodel.LengthOfReferencedText;
import ch.interlis.ili2c.metamodel.LineForm;
import ch.interlis.ili2c.metamodel.LineType;
import ch.interlis.ili2c.metamodel.LocalAttribute;
import ch.interlis.ili2c.metamodel.MandatoryConstraint;
import ch.interlis.ili2c.metamodel.NumericType;
import ch.interlis.ili2c.metamodel.NumericalType;
import ch.interlis.ili2c.metamodel.ObjectPath;
import ch.interlis.ili2c.metamodel.ObjectType;
import ch.interlis.ili2c.metamodel.Objects;
import ch.interlis.ili2c.metamodel.ParameterValue;
import ch.interlis.ili2c.metamodel.PathEl;
import ch.interlis.ili2c.metamodel.PathElAbstractClassRole;
import ch.interlis.ili2c.metamodel.PolylineType;
import ch.interlis.ili2c.metamodel.PrecisionDecimal;
import ch.interlis.ili2c.metamodel.ReferenceType;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.ili2c.metamodel.StructuredUnit;
import ch.interlis.ili2c.metamodel.StructuredUnitType;
import ch.interlis.ili2c.metamodel.SurfaceOrAreaType;
import ch.interlis.ili2c.metamodel.SurfaceType;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.ili2c.metamodel.TextOIDType;
import ch.interlis.ili2c.metamodel.TextType;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.Type;
import ch.interlis.ili2c.metamodel.TypeAlias;
import ch.interlis.ili2c.metamodel.UniquenessConstraint;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.ili2c.metamodel.ViewableAggregate;
import ch.interlis.ili2c.metamodel.ViewableAlias;
import ch.interlis.ili2c.metamodel.ViewableTransferElement;
import ch.interlis.iom.IomConstants;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iox.IoxLogEvent;
import ch.interlis.iox.IoxLogging;
import ch.interlis.iox.IoxValidationConfig;
import ch.interlis.iox_j.IoxInvalidDataException;
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.models.INTERLIS.*;
import ch.interlis.models.IlisMeta07.ModelData.AttributeRefType;


public class Validator implements ch.interlis.iox.IoxValidator {
	public static final String CONFIG_DO_ITF_LINETABLES="ch.interlis.iox_j.validator.doItfLinetables";
	public static final String CONFIG_DO_ITF_LINETABLES_DO="doItfLinetables";
	public static final String CONFIG_DO_ITF_OIDPERTABLE="ch.interlis.iox_j.validator.doItfOidPerTable";
	public static final String CONFIG_DO_ITF_OIDPERTABLE_DO="doItfOidPerTable";
	
	private ch.interlis.iox.IoxValidationConfig validationConfig=null;
	private IoxLogging errs=null;
	private LogEventFactory errFact=null;
	private TransferDescription td=null;
	private boolean doItfLineTables=false;
	private boolean doItfOidPerTable=false;
	private Settings config=null;
	private boolean validationOff=false;

	public Validator(TransferDescription td, IoxValidationConfig validationConfig,
			IoxLogging errs, LogEventFactory errFact, Settings config) {
		super();
		this.td = td;
		this.validationConfig = validationConfig;
		this.errs = errs;
		this.errFact = errFact;
		this.config=config;
		this.doItfLineTables = CONFIG_DO_ITF_LINETABLES_DO.equals(config.getValue(CONFIG_DO_ITF_LINETABLES));
		this.doItfOidPerTable = CONFIG_DO_ITF_OIDPERTABLE_DO.equals(config.getValue(CONFIG_DO_ITF_OIDPERTABLE));
		if(doItfLineTables){
			tag2class=ch.interlis.iom_j.itf.ModelUtilities.getTagMap(td);
		}else{
			tag2class=ch.interlis.ili2c.generator.XSDGenerator.getTagMap(td);
		}
		unknownTypev=new HashSet<String>();
		validationOff=ValidationConfig.OFF.equals(this.validationConfig.getConfigValue(ValidationConfig.PARAMETER, ValidationConfig.VALIDATION));
	}

	/** mappings from xml-tags to Viewable|AttributeDef
	 */
	private HashMap<String,Object> tag2class=null;
	/** list of seen but unknown types; maintained to prevent duplicate error messages
	 */
	private HashSet<String> unknownTypev=null;
	
	@Override
	public void close() {
	}

	@Override
	public ch.interlis.iox.IoxValidationDataPool getDataPool() {
		return null;
	}

	@Override
	public IoxLogging getLoggingHandler() {
		return errs;
	}
	@Override
	public void setLoggingHandler(IoxLogging handler) {
		errs=handler;
	}

	@Override
	public void validate(ch.interlis.iox.IoxEvent event) {
		if(validationOff){
			return;
		}
		if(event instanceof ch.interlis.iox.ObjectEvent){
			IomObject iomObj=((ch.interlis.iox.ObjectEvent)event).getIomObject();
			validateObject(iomObj,null);
		} else if (event instanceof ch.interlis.iox.EndTransferEvent){
			iterateThroughAllObjects();
		}
	}
	
	// HashSet of all Objects which where tested in the first run.
	HashSet<IomObject> allObjects = new HashSet<IomObject>();
	
	private void iterateThroughAllObjects(){
		if (allObjects != null){
			// iterate through iomObjects
			Iterator<IomObject> objectIterator = allObjects.iterator();
			while (objectIterator.hasNext()){
				IomObject iomObj = objectIterator.next();
				errFact.setDataObj(iomObj);
				Object modelElement=tag2class.get(iomObj.getobjecttag());
				Viewable currentClass= (Viewable) modelElement;
				Iterator constraintIterator=currentClass.iterator();
				while (constraintIterator.hasNext()) {
					Object objA = constraintIterator.next();
					// existence constraint
					if(objA instanceof ExistenceConstraint){
						ExistenceConstraint existenceConstraintObj=(ExistenceConstraint) objA;
						validateExistenceConstraint(iomObj, existenceConstraintObj);
					}
					// mandatory constraint
					if(objA instanceof MandatoryConstraint){
						MandatoryConstraint mandatoryConstraintObj=(MandatoryConstraint) objA;
						validateMandatoryConstraint(iomObj, mandatoryConstraintObj);
					}
				}
				Iterator<ViewableTransferElement> attrIterator=currentClass.getAttributesAndRoles2();
				while (attrIterator.hasNext()) {
					ViewableTransferElement objA = attrIterator.next();
					if (objA.obj instanceof LocalAttribute){
						Type type = ((LocalAttribute)objA.obj).getDomain();
						// composition
						if (type instanceof CompositionType){
							IomObject structValue = iomObj.getattrobj(iomObj.getattrname(0), 0);
							Table structure = ((CompositionType)type).getComponentType();
							validateReferenceAttrs(structValue, structure);
						}
					}else if(objA.obj instanceof RoleDef){
						validateRoleReference((RoleDef)objA.obj, iomObj);
					}
				}
				if(currentClass instanceof AbstractClassDef){
					Iterator<RoleDef> targetRoleIterator=((AbstractClassDef) currentClass).getOpposideRoles();
					while(targetRoleIterator.hasNext()){
						RoleDef role=targetRoleIterator.next();
						validateRoleCardinality(role, iomObj);						
					}
				}
			}
		}
	}

	private void validateMandatoryConstraint(IomObject iomObj, MandatoryConstraint mandatoryConstraintObj) {
		Evaluable condition = (Evaluable) mandatoryConstraintObj.getCondition();
		Value conditionValue = evaluateExpression(iomObj, condition);
		if (!conditionValue.isError()){
			if (conditionValue.isTrue()){
				// ok
			} else {
				errs.addEvent(errFact.logErrorMsg("Mandatory Constraint {0} is not true.", mandatoryConstraintObj.getName()));
			}
		}
	}

	private Value evaluateExpression(IomObject iomObj, Evaluable expression) {
		if(expression instanceof Equality){
			// ==
			Equality equality = (Equality) expression;
			Evaluable leftExpression = (Evaluable) equality.getLeft();
			Evaluable rightExpression = (Evaluable) equality.getRight();
			Value leftValue=evaluateExpression(iomObj,leftExpression);
			// if isError, return error.
			if (leftValue.isError()){
				return leftValue;
			}
			Value rightValue=evaluateExpression(iomObj,rightExpression);
			// if isError, return error.
			if (rightValue.isError()){
				return rightValue;
			}
			// if left and right value not errors, compare values.
			if (leftValue.compareTo(rightValue)==0){
				return new Value(true);
			} else {
				return new Value(false);
			}
		} else if(expression instanceof GreaterThan){
			// >
			GreaterThan greaterThan = (GreaterThan) expression;
			Evaluable leftExpression = (Evaluable) greaterThan.getLeft();
			Evaluable rightExpression = (Evaluable) greaterThan.getRight();
			Value leftValue=evaluateExpression(iomObj,leftExpression);
			// if isError, return error.
			if (leftValue.isError()){
				return leftValue;
			}
			Value rightValue=evaluateExpression(iomObj,rightExpression);
			// if isError, return error.
			if (rightValue.isError()){
				return rightValue;
			}
			// if left and right value not errors, compare values.
			if (leftValue.compareTo(rightValue)>0){
				return new Value(true);
			} else {
				return new Value(false);
			}
		} else if(expression instanceof GreaterThanOrEqual){
			// >=
			GreaterThanOrEqual greaterThanOrEqual = (GreaterThanOrEqual) expression;
			Evaluable leftExpression = (Evaluable) greaterThanOrEqual.getLeft();
			Evaluable rightExpression = (Evaluable) greaterThanOrEqual.getRight();
			Value leftValue=evaluateExpression(iomObj,leftExpression);
			// if isError, return error.
			if (leftValue.isError()){
				return leftValue;
			}
			Value rightValue=evaluateExpression(iomObj,rightExpression);
			// if isError, return error.
			if (rightValue.isError()){
				return rightValue;
			}
			// if left and right value not errors, compare values.
			if (leftValue.compareTo(rightValue)>=0){
				return new Value(true);
			} else {
				return new Value(false);
			}
		} else if(expression instanceof Inequality){
			// != or <>
			Inequality inEquality = (Inequality) expression;
			Evaluable leftExpression = (Evaluable) inEquality.getLeft();
			Evaluable rightExpression = (Evaluable) inEquality.getRight();
			Value leftValue=evaluateExpression(iomObj,leftExpression);
			// if isError, return error.
			if (leftValue.isError()){
				return leftValue;
			}
			Value rightValue=evaluateExpression(iomObj,rightExpression);
			// if isError, return error.
			if (rightValue.isError()){
				return rightValue;
			}
			// if left and right value not errors, compare values.
			if (leftValue.compareTo(rightValue)!=0){
				return new Value(true);
			} else {
				return new Value(false);
			}
		} else if(expression instanceof LessThan){
			// <
			LessThan lessThan = (LessThan) expression;
			Evaluable leftExpression = (Evaluable) lessThan.getLeft();
			Evaluable rightExpression = (Evaluable) lessThan.getRight();
			Value leftValue=evaluateExpression(iomObj,leftExpression);
			// if isError, return error.
			if (leftValue.isError()){
				return leftValue;
			}
			Value rightValue=evaluateExpression(iomObj,rightExpression);
			// if isError, return error.
			if (rightValue.isError()){
				return rightValue;
			}
			// if left and right value not errors, compare values.
			if (leftValue.compareTo(rightValue)<0){
				return new Value(true);
			} else {
				return new Value(false);
			}
		} else if(expression instanceof LessThanOrEqual){
			// <=
			LessThanOrEqual lessThanOrEqual = (LessThanOrEqual) expression;
			Evaluable leftExpression = (Evaluable) lessThanOrEqual.getLeft();
			Evaluable rightExpression = (Evaluable) lessThanOrEqual.getRight();
			Value leftValue=evaluateExpression(iomObj,leftExpression);
			// if isError, return error.
			if (leftValue.isError()){
				return leftValue;
			}
			Value rightValue=evaluateExpression(iomObj,rightExpression);
			// if isError, return error.
			if (rightValue.isError()){
				return rightValue;
			}
			// if left and right value not errors, compare values.
			if (leftValue.compareTo(rightValue)<=0){
				return new Value(true);
			} else {
				return new Value(false);
			}
		} else if(expression instanceof Negation){
			// NOT
			Negation negation = (Negation) expression;				
			Value arg=evaluateExpression(iomObj,negation.getNegated());
			if(arg.isTrue()){
				return new Value(false);
			} else {
				return new Value(true);
			}
		} else if(expression instanceof Conjunction){
			// AND
			Conjunction conjunction = (Conjunction) expression;
			Evaluable[] conjunctionArray = (Evaluable[]) conjunction.getConjoined();
			for (int i=0;i<conjunctionArray.length;i++){
				Value arg=evaluateExpression(iomObj,conjunctionArray[i]);
				if(!arg.isTrue()){
					return new Value(false);
				}
			}
			return new Value(true);
		} else if(expression instanceof DefinedCheck){
			// DEFINED
			DefinedCheck defined = (DefinedCheck) expression;
			Value arg=evaluateExpression(iomObj,defined.getArgument());
			if(arg.getComplexValue() != null || arg.getValue() != null || arg.isTrue()){
				return new Value(true);
			} else {
				return new Value(false);
			}
		} else if(expression instanceof Disjunction){
			// OR
			Disjunction disjunction = (Disjunction) expression;
			Evaluable[] disjunctionArray = (Evaluable[]) disjunction.getDisjoined();
			for (int i=0;i<disjunctionArray.length;i++){
				Value arg=evaluateExpression(iomObj,disjunctionArray[i]);
				if(arg.isTrue()){
					return new Value(true);
				}
			}
			return new Value(false);
		} else if(expression instanceof Constant){
			// constant
			Constant constantObj = (Constant) expression;
			if (constantObj instanceof Constant.EnumConstOrRange){
				Constant.EnumConstOrRange enumConstOrRange = (Constant.EnumConstOrRange) constantObj;
				// enumConstOrRange
				if(enumConstOrRange instanceof Enumeration){
					// enumeration
					Enumeration enumObj = (Enumeration) enumConstOrRange;
					String[] value = enumObj.getValue();
					if (value[0].equals("true")){
						return new Value(true);
					} else if (value[0].equals("false")){
						return new Value(false);
					}
				} else if (enumConstOrRange instanceof EnumerationRange){
					// constant.enumerationRange
				}
			}
		} else if(expression instanceof ConditionalExpression){
			// conditional expression	
			ConditionalExpression conditionalExpressionObj = (ConditionalExpression) expression;
			
		} else if(expression instanceof FunctionCall){
			// function call	
			FunctionCall functionCallObj = (FunctionCall) expression;
			
		} else if(expression instanceof InspectionFactor){
			// inspection factor	
			InspectionFactor inspectionFactorObj = (InspectionFactor) expression;
			
		} else if(expression instanceof LengthOfReferencedText){
			// length of referenced text	
			LengthOfReferencedText lengthOfReferencedTextObj = (LengthOfReferencedText) expression;
		} else if(expression instanceof ObjectPath){
			// object path	
			ObjectPath objectPathObj = (ObjectPath) expression;
			PathEl pathEl = (PathEl) objectPathObj.getLastPathEl();
			AttributeRef attrRef = (AttributeRef) pathEl;
			Type type = attrRef.getAttr().getDomain();
			String attrName = objectPathObj.getLastPathEl().getName();
			String objValue = iomObj.getattrvalue(attrName);
			if(objValue != null){
				if (objValue.equals("true")){
					return new Value(true);
				} else if (objValue.equals("false")){
					return new Value(false);
					// if null, then complex value.
				} else {
					if (type instanceof TypeAlias){
						TypeAlias typeAlias = (TypeAlias) type;
						Type aliasType = typeAlias.getAliasing().getType();
						return new Value(aliasType, objValue);
					}
					return new Value(type, objValue);
				}
			} else {
				return new Value(iomObj.getattrobj(attrName, 0));
			}
		} else if(expression instanceof Objects){
			// objects
			Objects objectsObj = (Objects) expression;
			
		} else if(expression instanceof ParameterValue){
			// parameter value
			ParameterValue parameterValueObj = (ParameterValue) expression;
			
		} else if(expression instanceof ViewableAggregate){
			// viewable aggregate	
			ViewableAggregate viewableAggregateObj = (ViewableAggregate) expression;
			
		} else if(expression instanceof ViewableAlias){
			// viewable alias
			ViewableAlias viewableAliasObj = (ViewableAlias) expression;
		}
		return null;
	}

	private void validateRoleCardinality(RoleDef role, IomObject iomObj) {
		int nrOfTargetObjs=linkPool.getTargetObjectCount(iomObj,role);
		long cardMin=role.getCardinality().getMinimum();
		long cardMax=role.getCardinality().getMaximum();
		if((nrOfTargetObjs>=cardMin && nrOfTargetObjs<=cardMax)){
			// valid
		}else{
			// not valid
			if(role.getCardinality().getMaximum() == Cardinality.UNBOUND){
				String cardMaxStr = "*";
				errs.addEvent(errFact.logErrorMsg(role.getName()+" should associate "+cardMin+" to "+cardMaxStr+" target objects (instead of "+nrOfTargetObjs+")"));
			} else {
				errs.addEvent(errFact.logErrorMsg(role.getName()+" should associate "+cardMin+" to "+cardMax+" target objects (instead of "+nrOfTargetObjs+")"));
			}
		}
	}

	private void validateRoleReference(RoleDef role, IomObject iomObj) {
		String targetOid = null;
		// get target oid
		IomObject roleDefValue = iomObj.getattrobj(role.getName(), 0);
		if (roleDefValue != null){
			targetOid = roleDefValue.getobjectrefoid();
		}
		if(targetOid==null){
			return;
		}
		// OID has to be unique in each table (ili 1.0)
		if(doItfOidPerTable){
			StringBuffer possibleTargetClasses=new StringBuffer();
			for (IomObject targetObj:allObjects){
				// validate target class
				String targetObjOid = targetObj.getobjectoid();
				String targetObjClassname = targetObj.getobjecttag();
				Viewable targetObjClass=(Viewable) tag2class.get(targetObjClassname);
				Iterator<AbstractClassDef> targetIterator = role.iteratorDestination();
				String sep="";
				while (targetIterator.hasNext()){
					Viewable targetClass = (Viewable) targetIterator.next();
					if(targetClass.equals(targetObjClass)){
						if(targetObjOid!=null){
							if(targetObjOid.equals(targetOid)){
								// target ok
								return;
							}
						}
						possibleTargetClasses.append(sep);
						sep=",";
						possibleTargetClasses.append(targetClass.getScopedName(null));
					}
				}
			}
			// no object with this oid found
			errs.addEvent(errFact.logErrorMsg("OID {0} not found in target class {1}.", targetOid, possibleTargetClasses.toString()));
		} else {
			// OID has to be unique in the whole file (ili 2.3)
			// find target object
			for (IomObject targetObj:allObjects){
				// validate target class
				String targetObjOid = targetObj.getobjectoid();
				String targetObjClassname = targetObj.getobjecttag();
				Viewable targetObjClass=(Viewable) tag2class.get(targetObjClassname);
				Iterator<AbstractClassDef> targetIterator = role.iteratorDestination();
				StringBuffer possibleTargetClasses=new StringBuffer();
				String sep="";
				if(targetObjOid!=null && targetObjOid.equals(targetOid)){
					while (targetIterator.hasNext()){
						Viewable targetClass = (Viewable) targetIterator.next();
						if(targetObjClass.isExtending(targetClass)){
							// target is ok
							return;
						}
						possibleTargetClasses.append(sep);
						sep=",";
						possibleTargetClasses.append(targetClass.getScopedName(null));
					}
					// object found, but wrong class
					errs.addEvent(errFact.logErrorMsg("Object {1} with OID {0} must be of {2}", targetOid,targetObjClassname,possibleTargetClasses.toString()));
					return;
				}
			}
			// no object with this oid found
			errs.addEvent(errFact.logErrorMsg("No object found with OID {0}.", targetOid));
		}
	}
	
	LinkPool linkPool=new LinkPool();
	
	private void validateReferenceAttrs(IomObject structValue, Table structure) {
		Iterator attrIter=structure.getAttributesAndRoles();
		while (attrIter.hasNext()){
			Object refAttrO = attrIter.next();
			if (refAttrO instanceof LocalAttribute){
				Type type = ((LocalAttribute)refAttrO).getDomain();
				// ReferenceType validation
				if (type instanceof ReferenceType){
					ReferenceType refAttrType = (ReferenceType) type;
					AbstractClassDef targetClass = refAttrType.getReferred();
					String refAttrName = ((Element) refAttrO).getName();
					IomObject refAttrValue = structValue.getattrobj(refAttrName, 0);
					if (refAttrValue != null){
						String targetOid = refAttrValue.getobjectrefoid();
						String targetObjOid = null;
						String targetObjClassStr = null;
						for (IomObject targetObj:allObjects ){
							Iterator refAttrIter = refAttrType.iteratorRestrictedTo();
							Object modelElement=tag2class.get(targetObj.getobjecttag());
							Table targetObjClass = (Table) modelElement;
							targetObjClassStr = targetObjClass.getScopedName(null);
							targetObjOid = targetObj.getobjectoid();
							// compare OID of objects
							if(targetObjOid.equals(targetOid)){
								if(refAttrIter.hasNext()){
									StringBuffer classNames=new StringBuffer();
									String sep="";
									while (refAttrIter.hasNext()){
										Object refAttr = refAttrIter.next();
										targetClass = (Table) refAttr;
										// compare class
										if (targetObjClass.isExtending(targetClass)){
											// ok
											return;
										} else {
											// try next restriction
										}
										classNames.append(sep);
										classNames.append(targetClass.getScopedName(null));
										sep=", ";
									}
									errs.addEvent(errFact.logErrorMsg("object {0} with OID {1} referenced by {3} is not an instance of {2}.", targetObjClassStr,targetObjOid,classNames.toString(),refAttrName ));
									return;
								}else{
									// compare class
									if (targetObjClass.isExtending(targetClass)){
										// ok
									} else {
										errs.addEvent(errFact.logErrorMsg("object {0} with OID {1} referenced by {3} is not an instance of {2}.", targetObjClassStr,targetObjOid,targetClass.getScopedName(null),refAttrName ));
									}
									return;
								}
							}
						}
						errs.addEvent(errFact.logErrorMsg("attribute {1} references an inexistent object with OID {0}.", targetOid,refAttrName));
					}
				}
			}
		}
	}

	private void validateExistenceConstraint(IomObject iomObj, ExistenceConstraint existenceConstraint){
		if (iomObj.getattrcount() == 0){
			return;
		}
		String restrictedAttrName = existenceConstraint.getRestrictedAttribute().getLastPathEl().getName();
		if(iomObj.getattrvaluecount(restrictedAttrName)==0){
			return;
		}
		Type type = existenceConstraint.getRestrictedAttribute().getType();
		// if type of alias, cast type to TypeAlias
		if (type instanceof TypeAlias){
			TypeAlias aliasType = (TypeAlias) type;
			Domain domainAliasing = (Domain) aliasType.getAliasing();
			type = (Type) domainAliasing.getType();
		}
		Iterator<ObjectPath> requiredInIterator = existenceConstraint.iteratorRequiredIn();
		boolean valueExists = false;
		Table classA = null;
		Table otherClass = null;
		while (requiredInIterator.hasNext()) {
			classA = null;
			ObjectPath attrName = (ObjectPath)requiredInIterator.next();
			otherClass = (Table) attrName.getRoot();
			String otherAttrName = attrName.toString();
			for (IomObject otherIomObj:allObjects ){
				if (otherIomObj.getattrcount() == 0){
					// do not validate.
				} else {
					Object modelElement=tag2class.get(otherIomObj.getobjecttag());
					classA= (Table) modelElement;
					// otherAttr defined?
					if(otherIomObj.getattrvaluecount(otherAttrName)>0){
						// validate if otherClass is extending by classA
						if (classA.isExtending(otherClass)){
							// if type is type of alias, validate instance of
							if(type instanceof ReferenceType){
								ReferenceType referenceType = (ReferenceType) type;
								valueExists = equalsReferenceValue(iomObj, referenceType, otherIomObj, otherAttrName, restrictedAttrName);
							} else if (type instanceof CoordType){
								CoordType coordType = (CoordType) type;
								valueExists = equalsCoordValue(iomObj, coordType, otherIomObj, otherAttrName, restrictedAttrName);
							} else if (type instanceof PolylineType){
								PolylineType polylineType = (PolylineType) type;
								valueExists = equalsPolylineValue(iomObj, polylineType, otherIomObj, otherAttrName, restrictedAttrName);
							} else if (type instanceof SurfaceOrAreaType){
								SurfaceOrAreaType surfaceOrAreaType = (SurfaceOrAreaType) type;
								valueExists = equalsSurfaceOrAreaValue(iomObj, surfaceOrAreaType, otherIomObj, otherAttrName, restrictedAttrName);
							} else if (type instanceof CompositionType){
								CompositionType compositionType = (CompositionType) type;
								valueExists = equalsCompositionValue(iomObj, compositionType, otherIomObj, otherAttrName, restrictedAttrName);
							} else {
								// if type is not type of alias, validate attribute names
								if(otherIomObj.getattrvalue(otherAttrName).equals(iomObj.getattrvalue(restrictedAttrName))){
									valueExists = true;
								}
							}
						}
					}
				}
			}
		}
		if (!valueExists){
			errs.addEvent(errFact.logErrorMsg("The value of the attribute {0} of {1} was not found in the condition class.", restrictedAttrName.toString(), iomObj.getobjecttag().toString()));
		}
	}
	
	private boolean equalsCompositionValue(IomObject iomObjectA, CompositionType compositionType, IomObject otherIomObj, String otherAttrName, String restrictedAttrName) {
		IomObject compositionValueRestricted=iomObjectA.getattrobj(restrictedAttrName, 0);
		IomObject compositionValueOther = otherIomObj.getattrobj(otherAttrName, 0);
		return true;
	}

	private boolean equalsSurfaceOrAreaValue(IomObject iomObj, SurfaceOrAreaType surfaceType, IomObject otherIomObj, String otherAttrName, String restrictedAttrName) {
		IomObject objA=iomObj.getattrobj(restrictedAttrName, 0);
		IomObject objB = otherIomObj.getattrobj(otherAttrName, 0);
		if (objA != null && objB != null){
			// validate if count of surfaces or areas of the objects are equal and get current surface or area
			for (int i=0;i<objA.getattrvaluecount("surface");i++){
				IomObject surfaceA = objA.getattrobj("surface",i);
				IomObject surfaceB = objB.getattrobj("surface",i);
				if (objA.getattrvaluecount("surface") == objB.getattrvaluecount("surface")){
					// ok
				} else {
					return false;
				}
				// validate if count of boundaries of the current surface is equal and get current boundary
				for (int j=0;j<surfaceA.getattrvaluecount("boundary");j++){
					IomObject boundaryA=surfaceA.getattrobj("boundary",j);
					IomObject boundaryB=surfaceB.getattrobj("boundary",j);
					if (boundaryA.getattrvaluecount("boundary") == boundaryB.getattrvaluecount("boundary")){
						// ok
					} else {
						return false;
					}
					// validate if count of polylines of the current boundary is equal and get current polyline
					for (int k=0;k<boundaryA.getattrvaluecount("polyline");k++){
						IomObject polylineA=boundaryA.getattrobj("polyline",k);
						IomObject polylineB=boundaryB.getattrobj("polyline",k);
						if (polylineA.getattrvaluecount("polyline") == polylineB.getattrvaluecount("polyline")){
							// ok
						} else {
							return false;
						}
						// validate if count of sequences of the objects is equal and get current sequence
						for(int l=0;l<polylineA.getattrvaluecount("sequence");l++){
							IomObject sequenceA=polylineA.getattrobj("sequence",l);
							IomObject sequenceB=polylineB.getattrobj("sequence",l);
							if (objA.getattrvaluecount("sequence") == objB.getattrvaluecount("sequence")){
								// ok
							} else {
								return false;
							}
							// validate if count of segments of the current sequence is equal and get current current segment
							for(int m=0;m<sequenceA.getattrvaluecount("segment");m++){
								IomObject segmentA=sequenceA.getattrobj("segment",m);
								IomObject segmentB=sequenceB.getattrobj("segment", m);
								if (segmentA.getattrvaluecount("segment") == segmentB.getattrvaluecount("segment")){
									// ok
								} else {
									return false;
								}
								for (int n=0;n<segmentA.getattrcount();n++){
									if (segmentA.getobjecttag().equals("COORD")){
										// equalation of values of dimensions(C1-3) where C is not null
										if (segmentA.getattrvalue("C1") != null && segmentB.getattrvalue("C1") != null){
											if (!segmentA.getattrvalue("C1").equals(segmentB.getattrvalue("C1"))){
												return false;
											}
										} else {
											return false;
										}
										if (segmentA.getattrvalue("C2") != null && segmentB.getattrvalue("C2") != null){
											if (!segmentA.getattrvalue("C2").equals(segmentB.getattrvalue("C2"))){
												return false;
											}
										}
										if (segmentA.getattrvalue("C3") != null && segmentB.getattrvalue("C3") != null){
											if (!segmentA.getattrvalue("C3").equals(segmentB.getattrvalue("C3"))){
												return false;
											}
										}
									} else if (segmentA.getobjecttag().equals("ARC")) {
										// equalation of values of dimensions(C1-3) where C or A is not null
										if (segmentA.getattrvalue("A1") != null && segmentB.getattrvalue("A1") != null){
											if (!segmentA.getattrvalue("A1").equals(segmentB.getattrvalue("A1"))){
												return false;
											}
										} else {
											return false;
										}
										if (segmentA.getattrvalue("A2") != null && segmentB.getattrvalue("A2") != null){
											if (!segmentA.getattrvalue("A2").equals(segmentB.getattrvalue("A2"))){
												return false;
											}
										} else {
											return false;
										}
										if (segmentA.getattrvalue("C1") != null && segmentB.getattrvalue("C1") != null){
											if (!segmentA.getattrvalue("C1").equals(segmentB.getattrvalue("C1"))){
												return false;
											}
										} else {
											return false;
										}
										if (segmentA.getattrvalue("C2") != null && segmentB.getattrvalue("C2") != null){
											if (!segmentA.getattrvalue("C2").equals(segmentB.getattrvalue("C2"))){
												return false;
											}
										}
										if (segmentA.getattrvalue("C3") != null && segmentB.getattrvalue("C3") != null){
											if (!segmentA.getattrvalue("C3").equals(segmentB.getattrvalue("C3"))){
												return false;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return true;
	}
	
	private boolean equalsPolylineValue(IomObject iomObj, PolylineType polylineType, IomObject otherIomObj, String otherAttrName, String restrictedAttrName) {
		IomObject objA = iomObj.getattrobj(restrictedAttrName, 0);
		IomObject objB = otherIomObj.getattrobj(otherAttrName, 0);
		// validate if one object is null
		if (objA != null && objB != null){
			// validate if count of sequences of the objects is equal and get current sequence
			for(int i=0;i<objA.getattrvaluecount("sequence");i++){
				IomObject sequenceA=objA.getattrobj("sequence",i);
				IomObject sequenceB=objB.getattrobj("sequence",i);
				if (objA.getattrvaluecount("sequence") == objB.getattrvaluecount("sequence")){
					// ok
				} else {
					return false;
				}
				// validate if count of segments of the current sequence is equal and get current current segment
				for(int j=0;j<sequenceA.getattrvaluecount("segment");j++){
					IomObject segmentA=sequenceA.getattrobj("segment",j);
					IomObject segmentB=sequenceB.getattrobj("segment", j);
					if (segmentA.getattrvaluecount("segment") == segmentB.getattrvaluecount("segment")){
						// ok
					} else {
						return false;
					}
					for (int k=0;k<segmentA.getattrcount();k++){
						if (segmentA.getobjecttag().equals("COORD")){
							// equalation of values of dimensions(C1-3) where C is not null
							if (segmentA.getattrvalue("C1") != null && segmentB.getattrvalue("C1") != null){
								if (!segmentA.getattrvalue("C1").equals(segmentB.getattrvalue("C1"))){
									return false;
								}
							} else {
								return false;
							}
							if (segmentA.getattrvalue("C2") != null && segmentB.getattrvalue("C2") != null){
								if (!segmentA.getattrvalue("C2").equals(segmentB.getattrvalue("C2"))){
									return false;
								}
							}
							if (segmentA.getattrvalue("C3") != null && segmentB.getattrvalue("C3") != null){
								if (!segmentA.getattrvalue("C3").equals(segmentB.getattrvalue("C3"))){
									return false;
								}
							}
						} else if (segmentA.getobjecttag().equals("ARC")) {
							// equalation of values of dimensions(C1-3) where C or A is not null
							if (segmentA.getattrvalue("A1") != null && segmentB.getattrvalue("A1") != null){
								if (!segmentA.getattrvalue("A1").equals(segmentB.getattrvalue("A1"))){
									return false;
								}
							} else {
								return false;
							}
							if (segmentA.getattrvalue("A2") != null && segmentB.getattrvalue("A2") != null){
								if (!segmentA.getattrvalue("A2").equals(segmentB.getattrvalue("A2"))){
									return false;
								}
							} else {
								return false;
							}
							if (segmentA.getattrvalue("C1") != null && segmentB.getattrvalue("C1") != null){
								if (!segmentA.getattrvalue("C1").equals(segmentB.getattrvalue("C1"))){
									return false;
								}
							} else {
								return false;
							}
							if (segmentA.getattrvalue("C2") != null && segmentB.getattrvalue("C2") != null){
								if (!segmentA.getattrvalue("C2").equals(segmentB.getattrvalue("C2"))){
									return false;
								}
							}
							if (segmentA.getattrvalue("C3") != null && segmentB.getattrvalue("C3") != null){
								if (!segmentA.getattrvalue("C3").equals(segmentB.getattrvalue("C3"))){
									return false;
								}
							}
						}
					}
				}
			}
		}
		return true;
	}

	private boolean equalsReferenceValue(IomObject iomObj, ReferenceType referenceType, IomObject otherIomObj, String otherAttrName, String restrictedAttrName) {
		IomObject referenceValueRestricted=iomObj.getattrobj(restrictedAttrName, 0);
		IomObject referenceValueOther = otherIomObj.getattrobj(otherAttrName, 0);
		return true;
	}

	private boolean equalsCoordValue(IomObject iomObj, CoordType coordType, IomObject otherIomObj, String otherAttrName, String restrictedAttrName){
		IomObject coordValueRestricted=iomObj.getattrobj(restrictedAttrName, 0);
		IomObject coordValueOther = otherIomObj.getattrobj(otherAttrName, 0);
		// depends on dimension of coord. validate if coords are not null and equals the values of coord of the objects
		if (coordType.getDimensions().length >= 1){
			if (coordValueRestricted.getattrvalue("C1") != null ){
				if (!coordValueRestricted.getattrvalue("C1").equals(coordValueOther.getattrvalue("C1"))){
					return false;
				}
			}else if(coordValueOther.getattrvalue("C1") != null){
				return false;
			}
		}
		if (coordType.getDimensions().length >= 2){
			if (coordValueRestricted.getattrvalue("C2") != null ){
				if (!coordValueRestricted.getattrvalue("C2").equals(coordValueOther.getattrvalue("C2"))){
					return false;
				}
			}else if(coordValueOther.getattrvalue("C2") != null){
				return false;
			}
		}
		if (coordType.getDimensions().length >= 3){
			if (coordValueRestricted.getattrvalue("C3") != null){
				if (!coordValueRestricted.getattrvalue("C3").equals(coordValueOther.getattrvalue("C3"))){
					return false;
				}
			}else if(coordValueOther.getattrvalue("C3") != null){
				return false;
			}
		}
		return true;
	}

	// Declaration of the uniqueness of Oid's.
	String uniquenessOfOid = null;
	// HashMap of unique constraints.
	HashMap<ArrayList<String>, HashSet<AttributeArray>> seenValues = new HashMap<ArrayList<String>, HashSet<AttributeArray>>();
	// List of all arrayLists of unique attributes and classes.
	ArrayList<ArrayList<String>> listOfUniqueObj = new ArrayList<ArrayList<String>>();
	// List of all object Oid's and associated classPath's of uniqueness validate of Oid's.
	Map<String , String> uniqueObjectIDs = new HashMap<String, String>();
	
	private void validateObject(IomObject iomObj,String attrPath) {
		// validate if object is null
		boolean isObject = attrPath==null;
		if(isObject){
			errFact.setDataObj(iomObj);
		}
		// validate uniqueness of existing objects on OID and className
		if(isObject){
			Object modelElementToCompare = tag2class.get(iomObj.getobjecttag());
			Viewable classValueToCompare= (Viewable) modelElementToCompare;
			String objOidToCompare = iomObj.getobjectoid();
			if (allObjects != null){
				Iterator<IomObject> objectIterator = allObjects.iterator();
				while (objectIterator.hasNext()){
					IomObject objValue = objectIterator.next();
					Object modelElementOfObject=tag2class.get(objValue.getobjecttag());
					Viewable classValueOfModelElement= (Viewable) modelElementOfObject;
					String objOidOfObjValue = objValue.getobjectoid();
					// OID has to be unique in each table (ili 1.0)
					if (doItfOidPerTable){
						if (classValueToCompare != null && classValueOfModelElement != null){
							compareOidAndClassOfObjects(classValueToCompare, objOidToCompare, classValueOfModelElement, objOidOfObjValue, iomObj);
						}
					// OID has to be unique in the whole file (ili 2.3)
					} else {
						if (classValueOfModelElement != null){
							compareOidOfObject(objOidToCompare, classValueOfModelElement, objOidOfObjValue, iomObj);
						}
					}
				}
			}
		}
		// validate class, structure and association on existence of OID
		if (isObject){
			Object modelElementOidValidate = tag2class.get(iomObj.getobjecttag());
			Viewable classValueOidValidate = (Viewable) modelElementOidValidate;
			// association
			if (modelElementOidValidate instanceof AssociationDef){
				AssociationDef modelAssociationDef = (AssociationDef) modelElementOidValidate;
				Domain oidType=((AbstractClassDef) modelAssociationDef).getOid();
				if (modelAssociationDef.isIdentifiable() || oidType!=null){
					if (iomObj.getobjectoid() == null){
						errs.addEvent(errFact.logErrorMsg("Association {0} has to have an OID", iomObj.getobjecttag()));
					}
				} 
			} else if (classValueOidValidate instanceof Table){
				Table classValueTable = (Table) classValueOidValidate;
				// class
				if (classValueTable.isIdentifiable()){
					if (iomObj.getobjectoid() == null){
						errs.addEvent(errFact.logErrorMsg("Class {0} has to have an OID", iomObj.getobjecttag()));
					}
				// structure	
				} else {
					if (iomObj.getobjectoid() != null){
						errs.addEvent(errFact.logErrorMsg("Structure {0} has not to have an OID", iomObj.getobjecttag()));
					}
				}
			}
		}
		
		String tag=iomObj.getobjecttag();
		//EhiLogger.debug("tag "+tag);
		Object modelele=tag2class.get(tag);
		if(modelele==null){
			if(!unknownTypev.contains(tag)){
				errs.addEvent(errFact.logErrorMsg("unknown class <{0}>",tag));
			}
			return;
		}
		if(isObject){
			// is it a SURFACE or AREA line table?
			if(doItfLineTables && modelele instanceof AttributeDef){
				validateItfLineTableObject(iomObj,(AttributeDef)modelele);
				return;
			}
		}
		// ASSERT: an ordinary class/table
		Viewable aclass1=(Viewable)modelele;
		
		if(isObject){
			errFact.setDefaultCoord(getDefaultCoord(iomObj));
			// validate that object is instance of a concrete class
			if(aclass1.isAbstract()){
				errs.addEvent(errFact.logErrorMsg("Object must be a non-abstract class"));
			}
			if(aclass1 instanceof AbstractClassDef){
				Domain oidType=((AbstractClassDef) aclass1).getOid();
				
				if(oidType!=null && oidType==td.INTERLIS.UUIDOID){
					String oid=iomObj.getobjectoid();
					if(oid != null && !isValidUuid(oid)){
						errs.addEvent(errFact.logErrorMsg("TID <{0}> is not a valid UUID", oid));
					}
				} else if(oidType!=null && oidType==td.INTERLIS.I32OID){
					// valid i32OID.
				} else if(oidType!=null && oidType==td.INTERLIS.STANDARDOID){
					// valid Standardoid.
				}
			}
		}
		
		// Uniqueness
		if(isObject){
			Iterator attrI=aclass1.iterator();
			while (attrI.hasNext()) {
				Object obj1 = attrI.next();
				if(obj1 instanceof UniquenessConstraint){
					UniquenessConstraint uniqueConstraint=(UniquenessConstraint) obj1;
					StringBuilder contentUniqueAttrs = new StringBuilder();
					ArrayList<String> uniqueConstraintAttrs=new ArrayList<String>();
					// gets all constraint attribute-names.
					Iterator iter = uniqueConstraint.getElements().iteratorAttribute();
					uniqueConstraintAttrs.add(aclass1.toString());
					while (iter.hasNext()){
						ObjectPath object = (ObjectPath)iter.next();
						uniqueConstraintAttrs.add(object.getLastPathEl().getName());
						contentUniqueAttrs.append(object.getLastPathEl().getName());
					}
					if (!listOfUniqueObj.contains(uniqueConstraintAttrs)){
						listOfUniqueObj.add(uniqueConstraintAttrs);
					}
					AttributeArray returnValue = validateUnique(iomObj,uniqueConstraintAttrs);
					if (returnValue == null){
						// ok
					} else {
						errs.addEvent(errFact.logErrorMsg("Unique is violated! Values {0} already exist in Object: {1}", returnValue.valuesAsString(), returnValue.getOid()));
					}
				}
			}
		}
		
		// alle attrs aus allen Klassen abspeichern.
		if(isObject){
			allObjects.add(iomObj);
		}
		
		HashSet<String> propNames=new HashSet<String>();
		Iterator iter = aclass1.getAttributesAndRoles2();
		while (iter.hasNext()) {
			ViewableTransferElement obj = (ViewableTransferElement)iter.next();
			if (obj.obj instanceof AttributeDef) {
				AttributeDef attr = (AttributeDef) obj.obj;
				if(!attr.isTransient()){
					Type proxyType=attr.getDomain();
					if(proxyType!=null && (proxyType instanceof ObjectType)){
						// skip implicit particles (base-viewables) of views
					}else{
						propNames.add(attr.getName());
						validateAttrValue(iomObj,attr,null);
					}
				}
			}
			if (isObject && obj.obj instanceof RoleDef) {
				RoleDef role = (RoleDef) obj.obj;
				if (role.getExtending() == null) {
						String refoid = null;
						String roleName = role.getName();
						// a role of an embedded association?
						if (obj.embedded) {
							AssociationDef roleOwner = (AssociationDef) role
									.getContainer();
							if (roleOwner.getDerivedFrom() == null) {
								// not just a link?
								IomObject structvalue = iomObj.getattrobj(
										roleName, 0);
								propNames.add(roleName);
								if (roleOwner.getAttributes().hasNext()
										|| roleOwner
												.getLightweightAssociations()
												.iterator().hasNext()) {
									// TODO handle attributes of link
								}
								if (structvalue != null) {
									refoid = structvalue.getobjectrefoid();
									long orderPos = structvalue
											.getobjectreforderpos();
									if (orderPos != 0) {
										// refoid,orderPos
										// ret.setStringAttribute(roleName,
										// refoid);
										// ret.setStringAttribute(roleName+".orderPos",
										// Long.toString(orderPos));
									} else {
										// refoid
										// ret.setStringAttribute(roleName,
										// refoid);
									}
								} else {
									refoid = null;
								}
							}
						} else {
							IomObject structvalue = iomObj.getattrobj(
									roleName, 0);
							propNames.add(roleName);
							refoid = structvalue.getobjectrefoid();
							long orderPos = structvalue
									.getobjectreforderpos();
							if (orderPos != 0) {
								// refoid,orderPos
								// ret.setStringAttribute(roleName, refoid);
								// ret.setStringAttribute(roleName+".orderPos",
								// Long.toString(orderPos));
							} else {
								// refoid
								// ret.setStringAttribute(roleName, refoid);
							}
						}
						String targetClass = null;
						if (refoid != null) {
							// TODO validate target opbject
							linkPool.addLink(iomObj,role,refoid);
						}
				}
			 }
		}
		// validate if no superfluous properties
		int propc=iomObj.getattrcount();
		for(int propi=0;propi<propc;propi++){
			String propName=iomObj.getattrname(propi);
			if(propName.startsWith("_")){
				// ok, software internal properties start with a '_'
			}else{
				if(!propNames.contains(propName)){
					errs.addEvent(errFact.logErrorMsg("unknown property <{0}>",propName));
				}
			}
		}
	}
	
	private void compareOidOfObject(String objOidToCompare, Viewable classValueOfModelElement, String objOidOfObjValue, IomObject iomObj) {
		if (objOidOfObjValue != null && objOidToCompare != null){
			if (objOidOfObjValue.equals(objOidToCompare)){
				errs.addEvent(errFact.logErrorMsg("The OID {0} of object '{1}' already exists in {2}.", objOidToCompare, iomObj.toString(), classValueOfModelElement.toString()));
				return;
			}
		}
	}

	private void compareOidAndClassOfObjects(Viewable classValueToCompare, String objOidToCompare, Viewable classValueOfModelElement, String objOidOfObjValue, IomObject iomObj) {
		// compare consistency of classes
		if (classValueOfModelElement.equals(classValueToCompare)){
			// compare consistency of OID's
			if (objOidOfObjValue != null && objOidToCompare != null){
				if (objOidOfObjValue.equals(objOidToCompare)){
					errs.addEvent(errFact.logErrorMsg("The OID {0} of object '{1}' already exists in {2}.", objOidToCompare, iomObj.toString(), classValueOfModelElement.toString()));
					return;
				}
			}
		}
	}

	private IomObject getDefaultCoord(IomObject iomObj) {
		String tag=iomObj.getobjecttag();
		Object modelele=tag2class.get(tag);
		if(modelele==null){
			return null;
		}
		Viewable aclass1=(Viewable)modelele;		
		Iterator iter = aclass1.getAttributesAndRoles2();
		while (iter.hasNext()) {
			ViewableTransferElement obj = (ViewableTransferElement)iter.next();
			if (obj.obj instanceof AttributeDef) {
				AttributeDef attr = (AttributeDef) obj.obj;
				if(!attr.isTransient()){
					Type proxyType=attr.getDomain();
					if(proxyType!=null && (proxyType instanceof ObjectType)){
						// skip implicit particles (base-viewables) of views
					}else{
						Type type=attr.getDomainResolvingAliases();
						String attrName=attr.getName();
						if (type instanceof CompositionType){
							 int structc=iomObj.getattrvaluecount(attrName);
							 for(int structi=0;structi<structc;structi++){
								 IomObject structEle=iomObj.getattrobj(attrName, structi);
									IomObject coord=getDefaultCoord(structEle);
									if (coord!=null){
										return coord;
									}
							 }
						}else if (type instanceof PolylineType){
							PolylineType polylineType=(PolylineType)type;
							IomObject polylineValue=iomObj.getattrobj(attrName, 0);
							if (polylineValue != null){
								IomObject coord=getFirstCoordFromPolyline(polylineValue);
								if (coord!=null){
									return coord;
								}
							}
						}else if(type instanceof SurfaceOrAreaType){
							 if(doItfLineTables){
								 if(type instanceof SurfaceType){
									 // SURFACE; no attributeValue in mainTable
								 }else{
									// AREA
									// coord
									IomObject coord=iomObj.getattrobj(attrName, 0);
									if (coord!=null){
										return coord;
									}
								 }
							 }else{
								 // polygon
								SurfaceOrAreaType surfaceOrAreaType=(SurfaceOrAreaType)type;
								IomObject surfaceValue=iomObj.getattrobj(attrName,0);
								if (surfaceValue != null){
									for(int surfacei=0;surfacei< surfaceValue.getattrvaluecount("surface");surfacei++){
										IomObject surface= surfaceValue.getattrobj("surface",surfacei);
										int boundaryc=surface.getattrvaluecount("boundary");
										for(int boundaryi=0;boundaryi<boundaryc;boundaryi++){
											IomObject boundary=surface.getattrobj("boundary",boundaryi);
											for(int polylinei=0;polylinei<boundary.getattrvaluecount("polyline");polylinei++){
												IomObject polyline=boundary.getattrobj("polyline",polylinei);
												IomObject coord=getFirstCoordFromPolyline(polyline);
												if (coord!=null){
													return coord;
												}
											}
										}
									}
								}
							 }
						}else if(type instanceof CoordType){
							IomObject coord=iomObj.getattrobj(attrName, 0);
							if (coord!=null){
								return coord;
							}
						}
						
					}
				}
			}
		}
		return null;
	}
	
	private IomObject getFirstCoordFromPolyline(IomObject polylineValue) {
		for(int sequencei=0;sequencei<polylineValue.getattrvaluecount("sequence");sequencei++){
			IomObject sequence=polylineValue.getattrobj("sequence",sequencei);
			for(int segmenti=0;segmenti<sequence.getattrvaluecount("segment");segmenti++){
				IomObject segment=sequence.getattrobj("segment",segmenti);
				return segment;
			}
		}
		return null;
	}

	// viewable aClass
	private AttributeArray validateUnique(IomObject currentObject,ArrayList<String>uniqueAttrs) {
		int sizeOfUniqueAttribute = uniqueAttrs.size();
		ArrayList<String> accu = new ArrayList<String>();
		for (int i=1;i<sizeOfUniqueAttribute;i++){
			String attrValue=currentObject.getattrvalue(uniqueAttrs.get(i));
			if(attrValue==null){
				IomObject refObj=currentObject.getattrobj(uniqueAttrs.get(i),0);
				if(refObj!=null){
					attrValue=refObj.getobjectrefoid();
				}
			}
			if(attrValue==null){
				return null;
			}
			accu.add(attrValue);
		}
		AttributeArray values=new AttributeArray(currentObject.getobjectoid(), accu);
		HashSet<AttributeArray> allValues = new HashSet<AttributeArray>();
		allValues.add(values);
		if (seenValues.containsKey(uniqueAttrs)){
			HashSet<AttributeArray> valuesOfKey = seenValues.get(uniqueAttrs);
			if (valuesOfKey.equals(allValues)){
				return values;
			}
		} else {
			seenValues.put(uniqueAttrs, allValues);
		}
		return null;
	}

	private void validateAttrValue(IomObject iomObj, AttributeDef attr,String attrPath) {
		 String attrName=attr.getName();
		 String attrQName=attr.getContainer().getScopedName(null)+"."+attrName;
		 if(attrPath==null){
			 attrPath=attrName;
		 }else{
			 attrPath=attrPath+"/"+attrName;
		 }
		 String validateMultiplicity=validationConfig.getConfigValue(attrQName, ValidationConfig.MULTIPLICITY);
		 String validateType=validationConfig.getConfigValue(attrQName, ValidationConfig.TYPE);
		 
			Type type0 = attr.getDomain();
			Type type = attr.getDomainResolvingAliases();
			if (type instanceof CompositionType){
				 int structc=iomObj.getattrvaluecount(attrName);
					if(ValidationConfig.OFF.equals(validateMultiplicity)){
						// skip it
					}else{
						 Cardinality card = ((CompositionType)type).getCardinality();
						 if(structc<card.getMinimum() || structc>card.getMaximum()){
							 logMsg(validateMultiplicity,"Attribute {0} has wrong number of values", attrPath);
						 }
					}
				 for(int structi=0;structi<structc;structi++){
					 IomObject structEle=iomObj.getattrobj(attrName, structi);
						if(ValidationConfig.OFF.equals(validateType)){
							// skip it
						}else{
							String tag=structEle.getobjecttag();
							Object modelele=tag2class.get(tag);
							if(modelele==null){
								if(!unknownTypev.contains(tag)){
									errs.addEvent(errFact.logErrorMsg("unknown class <{0}>",tag));
								}
							}else{
								Viewable structEleClass=(Viewable) modelele;
								Table requiredClass=((CompositionType)type).getComponentType();
								if(!structEleClass.isExtending(requiredClass)){
									 logMsg(validateType,"Attribute {0} requires a structure {1}", attrPath,requiredClass.getScopedName(null));
								}
								if(structEleClass.isAbstract()){
									// validate that object is instance of concrete class
									 logMsg(validateType,"Attribute {0} requires a non-abstract structure", attrPath);
								}
							}
						}
					 validateObject(structEle, attrPath+"["+structi+"]");
				 }
			}else{
				if(ValidationConfig.OFF.equals(validateMultiplicity)){
					// skip it
				}else{
					 int structc=iomObj.getattrvaluecount(attrName);
					 if(attr.getDomain().isMandatoryConsideringAliases() && structc==0){
						 if(doItfLineTables && type instanceof SurfaceType){
							 // SURFACE; no attrValue in maintable
						 }else{
							 logMsg(validateMultiplicity,"Attribute {0} requires a value", attrPath);
						 }
					 }
				}
				if(ValidationConfig.OFF.equals(validateType)){
					// skip it
				}else{
					if (attr.isDomainIli1Date()) {
						String valueStr = iomObj.getattrvalue(attrName);
						// Value has to be not null and exactly 8 numbers long
						if (valueStr == null){
							// no value, skip test
						}else if(valueStr.length() == 8) {
							// Value has to be a number
							try {
								int year = Integer.parseInt(valueStr.substring(0, 4));
								int month = Integer.parseInt(valueStr.substring(4, 6));
								int day = Integer.parseInt(valueStr.substring(6, 8));
								// Substring value: year, month and day has to be in valid range
								if (year >= 1582 && year <= 2999 && month >= 01 && month <= 12
										&& day >= 01 && day <= 31) {
								} else {
									logMsg(validateType, "value <{0}> is not in range", valueStr);
								}
							} catch (NumberFormatException numberformatexception) {
								logMsg(validateType, "value <{0}> is not a valid Date", valueStr);
							}
						} else {
							logMsg(validateType, "value <{0}> is not a valid Date", valueStr);
						}
					} else if (attr.isDomainBoolean()) {
						// Value has to be not null and ("true" or "false")
						String valueStr = iomObj.getattrvalue(attrName);
						if (valueStr == null || valueStr.matches("^(true|false)$")) {
							// Value okay, skip it
						} else {
							logMsg(validateType, "value <{0}> is not a BOOLEAN", valueStr);
						}
					} else if (attr.isDomainIliUuid()) {
						// Value is exactly 36 chars long and matches the regex
						String valueStr = iomObj.getattrvalue(attrName);
						if (valueStr == null || isValidUuid(valueStr)) {
								// Value ok, Skip it
						} else {
							logMsg(validateType, "value <{0}> is not a valid UUID", valueStr);
						}
					} else if (attr.isDomainIli2Date()) {
						// Value matches regex and is not null and is in range of type.
						String valueStr = iomObj.getattrvalue(attrName);
						FormattedType subType = (FormattedType) type;
						if (valueStr == null || valueStr.matches(subType.getRegExp()) && subType.isValueInRange(valueStr)) {
							// Value okay, skip it
						} else {
							logMsg(validateType, "value <{0}> is not a valid Date", valueStr);
						}
					} else if (attr.isDomainIli2Time()) {
						// Value is not null and matches 0:0:0.000-23:59:59.999
						String valueStr = iomObj.getattrvalue(attrName);
						FormattedType subType = (FormattedType) type;
						// Min length and max length is added, because of the defined regular expression which does not test the length of the value.
						if (valueStr == null || valueStr.matches(subType.getRegExp()) && subType.isValueInRange(valueStr) && valueStr.length() >= 9 && valueStr.length() <= 12) {
							// Value okay, skip it
						} else {
							logMsg(validateType, "value <{0}> is not a valid Time", valueStr);
						}
					} else if (attr.isDomainIli2DateTime()) {
						// Value is not null
						String valueStr = iomObj.getattrvalue(attrName);
						FormattedType subType = (FormattedType) type;
						// Min length and max length is added, because of the defined regular expression which does not test the length of the value.
						if (valueStr == null || valueStr.matches(subType.getRegExp()) && subType.isValueInRange(valueStr) && valueStr.length() >= 18 && valueStr.length() <= 23) {
							// Value okay, skip it
						} else {
							logMsg(validateType, "value <{0}> is not a valid DateTime", valueStr);
						}
					}else if (type instanceof PolylineType){
						PolylineType polylineType=(PolylineType)type;
						IomObject polylineValue=iomObj.getattrobj(attrName, 0);
						if (polylineValue != null){
							validatePolyline(validateType, polylineType, polylineValue);
						}
					}else if(type instanceof SurfaceOrAreaType){
						 if(doItfLineTables){
							 if(type instanceof SurfaceType){
								 // SURFACE; no attributeValue in mainTable
							 }else{
								 // AREA
								 // validate coord
							 }
						 }else{
							 // validate polygon
							SurfaceOrAreaType surfaceOrAreaType=(SurfaceOrAreaType)type;
							IomObject surfaceValue=iomObj.getattrobj(attrName,0);
							if (surfaceValue != null){
								if (surfaceValue.getobjecttag().equals("MULTISURFACE")){
									boolean clipped = surfaceValue.getobjectconsistency()==IomConstants.IOM_INCOMPLETE;
									for(int surfacei=0;surfacei< surfaceValue.getattrvaluecount("surface");surfacei++){
										if(!clipped && surfacei>0){
											// unclipped surface with multi 'surface' elements
											logMsg(validateType,"invalid number of surfaces in COMPLETE basket");
										}
										IomObject surface= surfaceValue.getattrobj("surface",surfacei);
										int boundaryc=surface.getattrvaluecount("boundary");
										for(int boundaryi=0;boundaryi<boundaryc;boundaryi++){
											IomObject boundary=surface.getattrobj("boundary",boundaryi);
											if(boundaryi==0){
												// shell
											}else{
											    // hole
											}    
											for(int polylinei=0;polylinei<boundary.getattrvaluecount("polyline");polylinei++){
												IomObject polyline=boundary.getattrobj("polyline",polylinei);
												validatePolyline(validateType, surfaceOrAreaType, polyline);
												// add line to shell or hole
											}
										    // add shell or hole to surface
										}
									}
								} else {
									logMsg(validateType, "unexpected Type "+surfaceValue.getobjecttag()+"; MULTISURFACE expected");
								}
							}
						 }
					}else if(type instanceof CoordType){
						IomObject coord=iomObj.getattrobj(attrName, 0);
						if (coord!=null){
							validateCoordType(validateType, (CoordType)type, coord);
						}
					}else if(type instanceof NumericType){
						String valueStr=iomObj.getattrvalue(attrName);
						if(valueStr!=null){
							validateNumericType(validateType, (NumericType)type, valueStr);							
						}else{
							IomObject structValue=iomObj.getattrobj(attrName, 0);
							if(structValue!=null){
								logMsg(validateType, "Attribute {0} has an unexpected type {1}",attrPath,structValue.getobjecttag());
							}
						}
					}else if(type instanceof EnumerationType){
						String value=iomObj.getattrvalue(attrName);
						if(value!=null){
							if(!((EnumerationType) type).getValues().contains(value)){
								 logMsg(validateType,"value {0} is not a member of the enumeration", value);
							}
						}else{
							IomObject structValue=iomObj.getattrobj(attrName, 0);
							if(structValue!=null){
								logMsg(validateType, "Attribute {0} has an unexpected type {1}",attrPath,structValue.getobjecttag());
							}
						}
					}else if(type instanceof EnumTreeValueType){
						EnumTreeValueType enumTreeValueType = (EnumTreeValueType) type;
						String attri = iomObj.getattrname(0).toString();
						String attrv = iomObj.getattrvalue(attri);
						String value=iomObj.getattrvalue(attrName);
					}else if(type instanceof ReferenceType){
					}else if(type instanceof TextType){
						String value=iomObj.getattrvalue(attrName);
						if(value!=null){
							int maxLength=((TextType) type).getMaxLength();
							TextType textType = (TextType) type;
							if(maxLength!=-1){
								if(value.length()>maxLength){
									 logMsg(validateType,"Attribute {0} is length restricted to {1}", attrPath,Integer.toString(maxLength));
								}
							}
							if(((TextType) type).isNormalized()){
								if(value.indexOf('\n')>=0 || value.indexOf('\r')>=0 || value.indexOf('\t')>=0){
									 logMsg(validateType,"Attribute {0} must not contain control characters", attrPath);
								}
							}
						}else{
							IomObject structValue=iomObj.getattrobj(attrName, 0);
							if(structValue!=null){
								logMsg(validateType, "Attribute {0} has an unexpected type {1}",attrPath,structValue.getobjecttag());
							}
						}
					}
				}
			}
	}

	private void validatePolyline(String validateType, LineType polylineType, IomObject polylineValue) {
		if (polylineValue.getobjecttag().equals("POLYLINE")){
			boolean clipped = polylineValue.getobjectconsistency()==IomConstants.IOM_INCOMPLETE;
			for(int sequencei=0;sequencei<polylineValue.getattrvaluecount("sequence");sequencei++){
				if(!clipped && sequencei>0){
					// an unclipped polyline should have only one sequence element
					logMsg(validateType,"invalid number of sequences in COMPLETE basket");
				}
				IomObject sequence=polylineValue.getattrobj("sequence",sequencei);
				LineForm[] lineforms = polylineType.getLineForms();
				HashSet<String> lineformNames=new HashSet<String>();
				for(LineForm lf:lineforms){
					lineformNames.add(lf.getName());
				}
				if(sequence.getobjecttag().equals("SEGMENTS")){
					for(int segmenti=0;segmenti<sequence.getattrvaluecount("segment");segmenti++){
						// segment = all segments which are in the actual sequence.
						IomObject segment=sequence.getattrobj("segment",segmenti);
						if(segment.getobjecttag().equals("COORD")){
							if(lineformNames.contains("STRAIGHTS") || segmenti==0){
								validateCoordType(validateType, (CoordType) polylineType.getControlPointDomain().getType(), segment);
							}else{
								logMsg(validateType, "unexpected COORD");
							}
						} else if (segment.getobjecttag().equals("ARC")){
							if(lineformNames.contains("ARCS") && segmenti>0){
								validateARCSType(validateType, (CoordType) polylineType.getControlPointDomain().getType(), segment);
							}else{
								logMsg(validateType, "unexpected ARC");
							}
						} else {
							logMsg(validateType, "unexpected Type "+segment.getobjecttag());
						}
					}
				} else {
					logMsg(validateType, "unexpected Type "+sequence.getobjecttag());
				}
			}
		} else {
			logMsg(validateType, "unexpected Type "+polylineValue.getobjecttag()+"; POLYLINE expected");
		}
	}

	private void validateCoordType(String validateType, CoordType coordType, IomObject coordValue) {
		if (coordType.getDimensions().length >= 1){
			if (coordValue.getattrvalue("C1") != null){
				validateNumericType(validateType, (NumericType)coordType.getDimensions()[0], coordValue.getattrvalue("C1"));
			} else if (coordValue.getattrvalue("A1") != null) {
				logMsg(validateType, "Not a type of COORD");
			} else {
				logMsg(validateType, "Wrong COORD structure, C1 expected");
			}
		}
		if (coordType.getDimensions().length == 2){
			if (coordValue.getattrvalue("C3") != null){
				logMsg(validateType, "Wrong COORD structure, C3 not expected");
			}
		}
		if (coordType.getDimensions().length >= 2){
			if (coordValue.getattrvalue("C2") != null){
				validateNumericType(validateType, (NumericType)coordType.getDimensions()[1], coordValue.getattrvalue("C2"));
			} else if (coordValue.getattrvalue("A2") != null) {
				logMsg(validateType, "Not a type of COORD");
			} else {
				logMsg(validateType, "Wrong COORD structure, C2 expected");
			}
		}
		if (coordType.getDimensions().length == 3){
			if (coordValue.getattrvalue("C3") != null){
				validateNumericType(validateType, (NumericType)coordType.getDimensions()[2], coordValue.getattrvalue("C3"));
			} else {
				logMsg(validateType, "Wrong COORD structure, C3 expected");
			}
		}
		// validate if no superfluous properties
		int propc=coordValue.getattrcount();
		for(int propi=0;propi<propc;propi++){
			String propName=coordValue.getattrname(propi);
			if(propName.startsWith("_")){
				// ok, software internal properties start with a '_'
			}else{
				if(!propName.equals("C1") && !propName.equals("C2") && !propName.equals("C3")){
					errs.addEvent(errFact.logErrorMsg("Wrong COORD structure, unknown property <{0}>",propName));
				}
			}
		}
	}

	private void validateARCSType(String validateType, CoordType coordType, IomObject coordValue) {
		if (coordType.getDimensions().length == 2){
			if (coordValue.getattrvalue("C3") != null){
				logMsg(validateType, "Wrong ARC structure, C3 not expected");
			}
		} else if (coordType.getDimensions().length == 3){
			if (coordValue.getattrvalue("C3") == null){
				logMsg(validateType, "Wrong ARC structure, C3 expected");
			}
		}
		if (coordType.getDimensions().length >= 2){
			if (coordValue.getattrvalue("A1") != null && coordValue.getattrvalue("A2") != null && coordValue.getattrvalue("C1") != null && coordValue.getattrvalue("C2") != null && coordValue.getattrvalue("C3") == null){
				// if in ili, 2 coords are defined, then in coordValue.getDimensions()[0,1] are only 0 or 1 valid.
				validateNumericType(validateType, (NumericType)coordType.getDimensions()[0], coordValue.getattrvalue("A1"));
				validateNumericType(validateType, (NumericType)coordType.getDimensions()[1], coordValue.getattrvalue("A2"));
				validateNumericType(validateType, (NumericType)coordType.getDimensions()[0], coordValue.getattrvalue("C1"));
				validateNumericType(validateType, (NumericType)coordType.getDimensions()[1], coordValue.getattrvalue("C2"));
			} else if (coordValue.getattrvalue("A1") != null && coordValue.getattrvalue("A2") != null && coordValue.getattrvalue("C1") != null && coordValue.getattrvalue("C2") != null && coordValue.getattrvalue("C3") != null){
				//  if in ili, 3 coords are defined, then in coordValue.getDimensions()[0,1,2] are only 0 or 1 or 2 valid.
				validateNumericType(validateType, (NumericType)coordType.getDimensions()[0], coordValue.getattrvalue("A1"));
				validateNumericType(validateType, (NumericType)coordType.getDimensions()[1], coordValue.getattrvalue("A2"));
				validateNumericType(validateType, (NumericType)coordType.getDimensions()[0], coordValue.getattrvalue("C1"));
				validateNumericType(validateType, (NumericType)coordType.getDimensions()[1], coordValue.getattrvalue("C2"));
				validateNumericType(validateType, (NumericType)coordType.getDimensions()[2], coordValue.getattrvalue("C3"));
			// a 2d Arc depends on: A1, A2, C1, C2. 
			} else if (coordValue.getattrvalue("A1") == null || coordValue.getattrvalue("A2") == null || coordValue.getattrvalue("C1") == null || coordValue.getattrvalue("C2") == null){
				logMsg(validateType, "A1, A2, C1, C2 expected! (C3 is expected if 3d)");
			} else {
				logMsg(validateType, "Wrong ARC structure");
			}
		}
		// validate if no superfluous properties
		int propc=coordValue.getattrcount();
		for(int propi=0;propi<propc;propi++){
			String propName=coordValue.getattrname(propi);
			if(propName.startsWith("_")){
				// ok, software internal properties start with a '_'
			}else{
				if(!propName.equals("A1") || !propName.equals("A2") || !propName.equals("C1") || !propName.equals("C2") || !propName.equals("C3")){
					// ok.
				} else {
					errs.addEvent(errFact.logErrorMsg("Wrong ARC structure, unknown property <{0}>",propName));
				}
			}
		}
	}

	private void validateNumericType(String validateType, NumericType type, String valueStr) {
		PrecisionDecimal value=null;
		try {
			value=new PrecisionDecimal(valueStr);
		} catch (NumberFormatException e) {
			 logMsg(validateType,"value <{0}> is not a number", valueStr);
		}
		if(value!=null){
			PrecisionDecimal minimum=((NumericType) type).getMinimum();
			PrecisionDecimal maximum=((NumericType) type).getMaximum();
		BigDecimal rounded = new BigDecimal(
				value.toString()).setScale(
				value.getExponent(),
				BigDecimal.ROUND_HALF_UP);
		BigDecimal min_general = new BigDecimal(
				minimum.toString()).setScale(
				minimum.getExponent(),
				BigDecimal.ROUND_HALF_UP);
		BigDecimal max_general = new BigDecimal(
				maximum.toString()).setScale(
				maximum.getExponent(),
				BigDecimal.ROUND_HALF_UP);
		  if (rounded.compareTo (min_general) == -1
			  || rounded.compareTo (max_general) == +1){
				 logMsg(validateType,"value {0} is out of range", valueStr);
		  }
		}
	}

	public boolean isValidUuid(String valueStr) {
		return valueStr.length() == 36 && valueStr.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}?");
	}
	
	private void logMsg(String validateKind,String msg,String... args){
		 if(ValidationConfig.WARNING.equals(validateKind)){
			 errs.addEvent(errFact.logWarningMsg(msg, args));
		 }else{
			 errs.addEvent(errFact.logErrorMsg(msg, args));
		 }
	}

	private void validateItfLineTableObject(IomObject iomObj, AttributeDef modelele) {
		// validate if line table object
	}
}
