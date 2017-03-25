package ch.interlis.iox_j.validator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.ws.Holder;

import com.vividsolutions.jts.geom.Coordinate;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.settings.Settings;
import ch.ehi.iox.objpool.ObjectPoolManager;
import ch.interlis.ili2c.metamodel.AbstractClassDef;
import ch.interlis.ili2c.metamodel.AbstractLeafElement;
import ch.interlis.ili2c.metamodel.AreaType;
import ch.interlis.ili2c.metamodel.AssociationDef;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.AttributeRef;
import ch.interlis.ili2c.metamodel.Cardinality;
import ch.interlis.ili2c.metamodel.CompositionType;
import ch.interlis.ili2c.metamodel.Constant;
import ch.interlis.ili2c.metamodel.Constant.Enumeration;
import ch.interlis.ili2c.metamodel.Constraint;
import ch.interlis.ili2c.metamodel.Container;
import ch.interlis.ili2c.metamodel.CoordType;
import ch.interlis.ili2c.metamodel.DataModel;
import ch.interlis.ili2c.metamodel.Domain;
import ch.interlis.ili2c.metamodel.Element;
import ch.interlis.ili2c.metamodel.EnumTreeValueType;
import ch.interlis.ili2c.metamodel.EnumerationType;
import ch.interlis.ili2c.metamodel.Evaluable;
import ch.interlis.ili2c.metamodel.ExistenceConstraint;
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
import ch.interlis.ili2c.metamodel.Function;
import ch.interlis.ili2c.metamodel.FunctionCall;
import ch.interlis.ili2c.metamodel.LineForm;
import ch.interlis.ili2c.metamodel.LineType;
import ch.interlis.ili2c.metamodel.LocalAttribute;
import ch.interlis.ili2c.metamodel.MandatoryConstraint;
import ch.interlis.ili2c.metamodel.NumericType;
import ch.interlis.ili2c.metamodel.NumericalType;
import ch.interlis.ili2c.metamodel.ObjectPath;
import ch.interlis.ili2c.metamodel.ObjectType;
import ch.interlis.ili2c.metamodel.Objects;
import ch.interlis.ili2c.metamodel.PathEl;
import ch.interlis.ili2c.metamodel.PathElAbstractClassRole;
import ch.interlis.ili2c.metamodel.PlausibilityConstraint;
import ch.interlis.ili2c.metamodel.PolylineType;
import ch.interlis.ili2c.metamodel.PrecisionDecimal;
import ch.interlis.ili2c.metamodel.Projection;
import ch.interlis.ili2c.metamodel.ReferenceType;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.ili2c.metamodel.SetConstraint;
import ch.interlis.ili2c.metamodel.StructAttributeRef;
import ch.interlis.ili2c.metamodel.SurfaceOrAreaType;
import ch.interlis.ili2c.metamodel.SurfaceType;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.ili2c.metamodel.TextType;
import ch.interlis.ili2c.metamodel.Topic;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.Type;
import ch.interlis.ili2c.metamodel.TypeAlias;
import ch.interlis.ili2c.metamodel.UniquenessConstraint;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.ili2c.metamodel.ViewableTransferElement;
import ch.interlis.iom.IomConstants;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.itf.impl.ItfAreaPolygon2Linetable;
import ch.interlis.iom_j.itf.impl.ItfSurfaceLinetable2Polygon;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.noding.CompoundCurveNoder;
import ch.interlis.iom_j.itf.impl.jtsext.noding.Intersection;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxLogging;
import ch.interlis.iox.IoxValidationConfig;
import ch.interlis.iox_j.IoxInvalidDataException;
import ch.interlis.iox_j.PipelinePool;
import ch.interlis.iox_j.jts.Iox2jtsext;
import ch.interlis.iox_j.logging.LogEventFactory;

public class Validator implements ch.interlis.iox.IoxValidator {
	public static final String CONFIG_DO_ITF_LINETABLES="ch.interlis.iox_j.validator.doItfLinetables";
	public static final String CONFIG_DO_ITF_LINETABLES_DO="doItfLinetables";
	public static final String CONFIG_DO_ITF_OIDPERTABLE="ch.interlis.iox_j.validator.doItfOidPerTable";
	public static final String CONFIG_DO_ITF_OIDPERTABLE_DO="doItfOidPerTable";
	public static final String CONFIG_CUSTOM_FUNCTIONS="ch.interlis.iox_j.validator.customFunctions";
	private ObjectPoolManager objPoolManager=null;
	private ObjectPool objectPool = null;
	private LinkPool linkPool;
	private ch.interlis.iox.IoxValidationConfig validationConfig=null;
	//private ch.interlis.iox.IoxDataPool pipelinePool=null;
	private PipelinePool pipelinePool=null;
	private IoxLogging errs=null;
	private LogEventFactory errFact=null;
	private TransferDescription td=null;
	private boolean doItfLineTables=false;
	private boolean doItfOidPerTable=false;
	private Settings config=null;
	private boolean validationOff=false;
	private String areaOverlapValidation=null;
	private String defaultGeometryTypeValidation=null;
	private boolean enforceTypeValidation=false;
	private boolean enforceConstraintValidation=false;
	private boolean enforceTargetValidation=false;
	private String currentBasketId = null;
	private String currentMainOid=null;
	private Map<AttributeDef,ItfAreaPolygon2Linetable> areaAttrs=new HashMap<AttributeDef,ItfAreaPolygon2Linetable>();
	private String checkConstraint=null;
	private String validateType=null;
	private Map<String,Class> customFunctions=new HashMap<String,Class>(); // qualified Interlis function name -> java class that implements that function
	private HashMap<Constraint,Viewable> additionalConstraints=new HashMap<Constraint,Viewable>();
	private Map<PlausibilityConstraint, PlausibilityPoolValue> plausibilityConstraints=new LinkedHashMap<PlausibilityConstraint, PlausibilityPoolValue>();
	private HashSet<String> validationConfigOff =new HashSet<String>();
	
	@Deprecated
	public Validator(TransferDescription td, IoxValidationConfig validationConfig,
			IoxLogging errs, LogEventFactory errFact, Settings config) {
		this(td, validationConfig,errs, errFact, new PipelinePool(),config);
	}
	/** 
	 * @param td
	 * @param validationConfig
	 * @param errs
	 * @param errFact
	 * @param pipelinePool
	 * @param config to validate ITF files you normally need to set config.setValue(Validator.CONFIG_DO_ITF_OIDPERTABLE, Validator.CONFIG_DO_ITF_OIDPERTABLE_DO);
	 */

	public Validator(TransferDescription td, IoxValidationConfig validationConfig,
			IoxLogging errs, LogEventFactory errFact, PipelinePool pipelinePool,Settings config) {
		super();
		this.td = td;
		this.validationConfig = validationConfig;
		this.errs = errs;
		this.errFact = errFact;
		if(errFact.getLogger()==null){
			errFact.setLogger(errs);
		}
		this.config=config;
		this.pipelinePool=pipelinePool;
		objPoolManager=new ObjectPoolManager();
		Map<String,Class> cf=(Map<String, Class>) config.getTransientObject(CONFIG_CUSTOM_FUNCTIONS);
		if(cf!=null){
			customFunctions=cf;
		}
		this.doItfLineTables = CONFIG_DO_ITF_LINETABLES_DO.equals(config.getValue(CONFIG_DO_ITF_LINETABLES));
		this.doItfOidPerTable = CONFIG_DO_ITF_OIDPERTABLE_DO.equals(config.getValue(CONFIG_DO_ITF_OIDPERTABLE));
		boolean allowOnlyRelaxedMultiplicity=ValidationConfig.ON.equals(validationConfig.getConfigValue(ValidationConfig.PARAMETER,ValidationConfig.ALLOW_ONLY_MULTIPLICITY_REDUCTION));
		if(allowOnlyRelaxedMultiplicity){
			errs.addEvent(errFact.logInfoMsg("only multiplicity validation relaxable"));
		}
		enforceConstraintValidation=allowOnlyRelaxedMultiplicity;
		enforceTypeValidation=allowOnlyRelaxedMultiplicity;
		enforceTargetValidation=allowOnlyRelaxedMultiplicity;
		if(doItfLineTables){
			tag2class=ch.interlis.iom_j.itf.ModelUtilities.getTagMap(td);
		}else{
			tag2class=ch.interlis.ili2c.generator.XSDGenerator.getTagMap(td);
		}
		unknownTypev=new HashSet<String>();
		validationOff=ValidationConfig.OFF.equals(this.validationConfig.getConfigValue(ValidationConfig.PARAMETER, ValidationConfig.VALIDATION));
		areaOverlapValidation=this.validationConfig.getConfigValue(ValidationConfig.PARAMETER, ValidationConfig.AREA_OVERLAP_VALIDATION);
		defaultGeometryTypeValidation=this.validationConfig.getConfigValue(ValidationConfig.PARAMETER, ValidationConfig.DEFAULT_GEOMETRY_TYPE_VALIDATION);
		objectPool=new ObjectPool(doItfOidPerTable, errs, errFact, tag2class,objPoolManager);
		linkPool=new LinkPool();
		String additionalModels=this.validationConfig.getConfigValue(ValidationConfig.PARAMETER, ValidationConfig.ADDITIONAL_MODELS);
		if(additionalModels!=null){
			String[] additionalModelv = additionalModels.split(";");
			iterateThroughAdditionalModels(additionalModelv);
	}
	}
	/** mappings from xml-tags to Viewable|AttributeDef
	 */
	private HashMap<String,Object> tag2class=null;
	/** list of seen but unknown types; maintained to prevent duplicate error messages
	 */
	private HashSet<String> unknownTypev=null;
	
	@Override
	public void close() {
		if(objPoolManager!=null){
			objPoolManager.close();
			objPoolManager=null;
		}
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
		if (event instanceof ch.interlis.iox.StartTransferEvent){
			errs.addEvent(errFact.logInfoMsg("first validation pass..."));
		} else if (event instanceof ch.interlis.iox.StartBasketEvent){
			currentBasketId = ((ch.interlis.iox.StartBasketEvent) event).getBid();
		}else if(event instanceof ch.interlis.iox.ObjectEvent){
			IomObject iomObj=((ch.interlis.iox.ObjectEvent)event).getIomObject();
			try {
				validateObject(iomObj,null);
			} catch (IoxException e) {
				errs.addEvent(errFact.logInfoMsg("failed to validate object {0}", iomObj.toString()));
			}
		} else if (event instanceof ch.interlis.iox.EndBasketEvent){
		}else if (event instanceof ch.interlis.iox.EndTransferEvent){
			iterateThroughAllObjects();
			validateAllAreas();
			validatePlausibilityConstraints();
		}
	}
	
	private void iterateThroughAdditionalModels(String[] additionalModels){
		if(additionalModels==null){
			return;
		}
		for(int modelIndex=0;modelIndex<additionalModels.length;modelIndex++){
			String additionalModel = additionalModels[modelIndex];
			if(additionalModel==null){
				continue;
			}
			errs.addEvent(errFact.logInfoMsg("additional model {0}", additionalModel));
			// models
			Iterator tdIterator = td.iterator();
			boolean modelExists=false;
			while(tdIterator.hasNext()){
				Object modelObj = tdIterator.next();
				if(!(modelObj instanceof DataModel)){
					continue;
				}
				// model
				DataModel model = (DataModel) modelObj;
				if(model.getName().equals(additionalModel)){
					modelExists=true;
					collectAdditionalConstraints(model);
				}
			}
			if(!modelExists){
				logMsg(checkConstraint,"required additional model {0} not found", additionalModel);
			}
		}
	}
	
	private void collectAdditionalConstraints(DataModel model){
		Iterator modelIterator = model.iterator();
		while(modelIterator.hasNext()){
			Object modelObj = modelIterator.next();
			// view topic
			if(!(modelObj instanceof Topic)){
				continue;
			}
			// topic
			Topic topic = (Topic) modelObj;
			if(!topic.isViewTopic()){
				continue;
			}
			Iterator topicIterator = topic.iterator();
			while(topicIterator.hasNext()){
				Object topicObj = topicIterator.next();
				if(!(topicObj instanceof Projection)){
					continue;
				}
				// view
				Projection view = (Projection) topicObj;
				Viewable classValue=null;
				if(view.getSelected().getAliasing()==null){
					continue;
				}
				// class
				classValue = view.getSelected().getAliasing();
				// constraint								
				Iterator iteratorOfViewConstraints=view.iterator();
				while (iteratorOfViewConstraints.hasNext()){
					Object constraintObj = iteratorOfViewConstraints.next();
					if(!(constraintObj instanceof Constraint)){
						continue;
					}
					// constraint off
					Constraint constraint = (Constraint) constraintObj;
					String check = getScopedName(constraint);
					checkConstraint=null;
					if(!enforceConstraintValidation){
						checkConstraint=validationConfig.getConfigValue(check, ValidationConfig.CHECK);
					}
					if(ValidationConfig.OFF.equals(checkConstraint)){
						if(!validationConfigOff.contains(ValidationConfig.CHECK)){
							validationConfigOff.add(ValidationConfig.CHECK);
							errs.addEvent(errFact.logInfoMsg("{0} not validated, validation configuration check= off", check));
						}
					}else{
						additionalConstraints.put(constraint, classValue);
					}
				}
			}
		}
	}
	
	private void validateAllAreas() {
		for(AttributeDef attr:areaAttrs.keySet()){
			errs.addEvent(errFact.logInfoMsg("validate AREA {0}...", getScopedName(attr)));
			ItfAreaPolygon2Linetable allLines=areaAttrs.get(attr);
			List<Intersection> intersections=allLines.validate();
			if(intersections!=null){
				for(Intersection is:intersections){
					//logMsg(areaOverlapValidation,"intersection tid1 "+is.getCurve1().getUserData()+", tid2 "+is.getCurve2().getUserData()+", coord "+is.getPt()[0].toString()+(is.getPt().length==2?(", coord2 "+is.getPt()[1].toString()):""));
					EhiLogger.logError("intersection tid1 "+is.getCurve1().getUserData()+", tid2 "+is.getCurve2().getUserData()+", coord "+is.getPt()[0].toString()+(is.getPt().length==2?(", coord2 "+is.getPt()[1].toString()):""));
					EhiLogger.traceState("overlap "+is.getOverlap()+", seg1 "+is.getSegment1()+", seg2 "+is.getSegment2());
				}
				logMsg(areaOverlapValidation,"failed to validate AREA {0}", getScopedName(attr));
			}
		}
	}
	private String getScopedName(AttributeDef attr) {
		return attr.getContainer().getScopedName(null)+"."+attr.getName();
	}
	private String getScopedName(RoleDef roleDef) {
		return roleDef.getContainer().getScopedName(null)+"."+roleDef.getName();
	}
	private String getScopedName(Constraint cnstr) {
		return cnstr.getContainer().getScopedName(null)+"."+cnstr.getName();
	}
	private String getScopedName(Viewable viewable) {
		return viewable.getContainer().getScopedName(null)+"."+viewable.getName();
	}
	private boolean isBasketSame(RoleDef role, ReferenceType ref,String bidOfTargetObject){
		if(!currentBasketId.equals(bidOfTargetObject)){
			if(role != null){
				if(role.isExternal()){
					return false;
				}
			} else if(ref != null){
				if(ref.isExternal()){
					return false;
				}
			}
		}
		return true;
	}

	private void iterateThroughAllObjects(){
		errs.addEvent(errFact.logInfoMsg("second validation pass..."));
		HashSet<Constraint> constraints=new HashSet<Constraint>();
		HashSet<Type> types=new HashSet<Type>();
		HashSet<Viewable> viewables=new HashSet<Viewable>();
		HashSet<AbstractLeafElement> abstractLeafElement=new HashSet<AbstractLeafElement>();
		for (String basketId : objectPool.getBasketIds()){
			// iterate through iomObjects
			Iterator<IomObject> objectIterator = (objectPool.getObjectsOfBasketId(basketId)).valueIterator();
			while (objectIterator.hasNext()){
				IomObject iomObj = objectIterator.next();
				setCurrentMainObj(iomObj);
				Object modelElement=tag2class.get(iomObj.getobjecttag());
				Viewable currentClass= (Viewable) modelElement;
				String check = getScopedName(currentClass);
				checkConstraint=null;
				if(!enforceConstraintValidation){
					checkConstraint=validationConfig.getConfigValue(check, ValidationConfig.CHECK);
				}
				if(ValidationConfig.OFF.equals(checkConstraint)){
					if(!validationConfigOff.contains(ValidationConfig.CHECK)){
						validationConfigOff.add(ValidationConfig.CHECK);
						errs.addEvent(errFact.logInfoMsg("{0} not validated, validation configuration check=off", check, iomObj.getobjectoid()));
					}
					}else{
					// additional constraint
					Viewable classValue=null;
					for (Map.Entry<Constraint,Viewable> constraintValue : additionalConstraints.entrySet()) {
						Constraint constraintObj = constraintValue.getKey();
						classValue = constraintValue.getValue();
						if(currentClass.isExtending(classValue)) {
							if(constraintObj instanceof ExistenceConstraint) {
								ExistenceConstraint existenceConstraint = (ExistenceConstraint) constraintObj;
								validateExistenceConstraint(iomObj, existenceConstraint);
							} else if(constraintObj instanceof MandatoryConstraint){
								MandatoryConstraint mandatoryConstraint = (MandatoryConstraint) constraintObj;
								validateMandatoryConstraint(iomObj, mandatoryConstraint);
							} else if(constraintObj instanceof SetConstraint){
								SetConstraint setConstraint = (SetConstraint) constraintObj;
								collectSetConstraintObjs(iomObj, setConstraint);
							} else if(constraintObj instanceof UniquenessConstraint){
								UniquenessConstraint uniquenessConstraint = (UniquenessConstraint) constraintObj;
								validateUniquenessConstraint(iomObj, uniquenessConstraint);
							} else if(constraintObj instanceof PlausibilityConstraint){
								PlausibilityConstraint plausibilityConstraint = (PlausibilityConstraint) constraintObj;
								fillOfPlausibilityConstraintMap(plausibilityConstraint, iomObj);
							}
						}
					}
					Iterator constraintIterator=currentClass.iterator();
					while (constraintIterator.hasNext()) {
						Object constraintObj = constraintIterator.next();
						// existence constraint
						if(constraintObj instanceof ExistenceConstraint){
							ExistenceConstraint existenceConstraint=(ExistenceConstraint) constraintObj;
							if(!constraints.contains(existenceConstraint)){
								constraints.add(existenceConstraint);
								errs.addEvent(errFact.logInfoMsg("validate existence constraint {0}...",getScopedName(existenceConstraint)));
							}
							validateExistenceConstraint(iomObj, existenceConstraint);
						}
						// mandatory constraint
						if(constraintObj instanceof MandatoryConstraint){
							MandatoryConstraint mandatoryConstraint=(MandatoryConstraint) constraintObj;
							if(!constraints.contains(mandatoryConstraint)){
								constraints.add(mandatoryConstraint);
								errs.addEvent(errFact.logInfoMsg("validate mandatory constraint {0}...",getScopedName(mandatoryConstraint)));
							}
							validateMandatoryConstraint(iomObj, mandatoryConstraint);
						}
						// set constraint
						if(constraintObj instanceof SetConstraint){
							SetConstraint setConstraint=(SetConstraint) constraintObj;
							collectSetConstraintObjs(iomObj, setConstraint);
						}
						// plausibility constraint
						if(constraintObj instanceof PlausibilityConstraint){
							PlausibilityConstraint plausibilityConstraint=(PlausibilityConstraint) constraintObj;
							if(!constraints.contains(plausibilityConstraint)){
								constraints.add(plausibilityConstraint);
								errs.addEvent(errFact.logInfoMsg("validate plausibility constraint {0}...",getScopedName(plausibilityConstraint)));
							}
							fillOfPlausibilityConstraintMap(plausibilityConstraint, iomObj);
						}
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
							CompositionType compositionType = (CompositionType) type;
							Table structure = compositionType.getComponentType();
							validateReferenceAttrs(structValue, structure);
						}
					}else if(objA.obj instanceof RoleDef){
						RoleDef roleDef = (RoleDef) objA.obj;
						if(!abstractLeafElement.contains(roleDef)){
							abstractLeafElement.add(roleDef);
							errs.addEvent(errFact.logInfoMsg("validate role reference {0}...",getScopedName(roleDef)));
						}
						validateRoleReference(roleDef, iomObj);
					}
				}
				if(currentClass instanceof AbstractClassDef){
					AbstractClassDef abstractClassDef = (AbstractClassDef) currentClass;
					if(!viewables.contains(abstractClassDef)){
						viewables.add(abstractClassDef);
						errs.addEvent(errFact.logInfoMsg("validate role references of {0}...",abstractClassDef.getScopedName(null)));
					}
					Iterator<RoleDef> targetRoleIterator=abstractClassDef.getOpposideRoles();
					while(targetRoleIterator.hasNext()){
						RoleDef role=targetRoleIterator.next();
						validateRoleCardinality(role, iomObj);
					}
				}
			}
		}
		for(SetConstraint setConstraint:setConstraints.keySet()){
			if(!loggedObjects.contains(setConstraint)){
				loggedObjects.add(setConstraint);
				errs.addEvent(errFact.logInfoMsg("validate set constraint {0}...",getScopedName(setConstraint)));
			}
			validateSetConstraint(setConstraint);
		}
	}

	private void fillOfPlausibilityConstraintMap(PlausibilityConstraint currentConstraint, IomObject iomObj){
		Evaluable condition = (Evaluable) currentConstraint.getCondition();
		Value conditionValue = evaluateExpression(iomObj, condition);
		if(plausibilityConstraints.containsKey(currentConstraint)){
			PlausibilityPoolValue poolConstraintValues = plausibilityConstraints.get(currentConstraint);
			double successfulResults = poolConstraintValues.getSuccessfulResults();
			double totalSumOfConstraints = poolConstraintValues.getTotalSumOfConstraints();
			if (conditionValue.isTrue()){
				plausibilityConstraints.remove(currentConstraint);
				plausibilityConstraints.put(currentConstraint, new PlausibilityPoolValue(successfulResults+1.0, totalSumOfConstraints+1.0));
			} else {
				// error, undefined, false
				plausibilityConstraints.remove(currentConstraint);
				plausibilityConstraints.put(currentConstraint, new PlausibilityPoolValue(successfulResults, totalSumOfConstraints+1.0));
			}
		} else {
			if (conditionValue.isTrue()){
				plausibilityConstraints.put(currentConstraint, new PlausibilityPoolValue(1.0, 1.0));
			} else {
				// error, undefined, false
				plausibilityConstraints.put(currentConstraint, new PlausibilityPoolValue(0.0, 1.0));
			}
		}
	}
	
	private void validatePlausibilityConstraints(){
		for (Entry<PlausibilityConstraint, PlausibilityPoolValue> constraintEntry : plausibilityConstraints.entrySet()){
			String check = getScopedName(constraintEntry.getKey());
			checkConstraint=null;
			if(!enforceConstraintValidation){
				checkConstraint=validationConfig.getConfigValue(check, ValidationConfig.CHECK);
			}
			if(ValidationConfig.OFF.equals(checkConstraint)){
				if(!validationConfigOff.contains(ValidationConfig.CHECK)){
					validationConfigOff.add(ValidationConfig.CHECK);
					errs.addEvent(errFact.logInfoMsg("{0} not validated, validation configuration check=off", check));
				}
			}else{
				String msg=validationConfig.getConfigValue(getScopedName(constraintEntry.getKey()), ValidationConfig.MSG);
				if(constraintEntry.getKey().getDirection()==0){ // >=
					if(((constraintEntry.getValue().getSuccessfulResults()/constraintEntry.getValue().getTotalSumOfConstraints())*100) >= constraintEntry.getKey().getPercentage()){
						// ok
					} else {
						if(msg!=null && msg.length()>0){
							logMsg(checkConstraint,msg);
						} else {
							logMsg(checkConstraint,"Plausibility Constraint {0} is not true.", getScopedName(constraintEntry.getKey()));
						}
					}
				} else if(constraintEntry.getKey().getDirection()==1){ // <=
					if(((constraintEntry.getValue().getSuccessfulResults()/constraintEntry.getValue().getTotalSumOfConstraints())*100) <= constraintEntry.getKey().getPercentage()){
						// ok
					} else {
						if(msg!=null && msg.length()>0){
							logMsg(checkConstraint,msg);
						} else {
							logMsg(checkConstraint,"Plausibility Constraint {0} is not true.", getScopedName(constraintEntry.getKey()));
						}
					}
				}
			}
		}
	}
	
	private void validateUniquenessConstraint(IomObject iomObj, UniquenessConstraint uniquenessConstraint) {
		String check = getScopedName(uniquenessConstraint);
		String checkUniqueConstraint=validationConfig.getConfigValue(check, ValidationConfig.CHECK);
		if(ValidationConfig.OFF.equals(checkUniqueConstraint)){
			if(!validationConfigOff.contains(ValidationConfig.CHECK)){
				validationConfigOff.add(ValidationConfig.CHECK);
				errs.addEvent(errFact.logInfoMsg("{0} not validated, validation configuration check=off", check, iomObj.getobjectoid()));
			}
		}else{
			Object modelElement=tag2class.get(iomObj.getobjecttag());
			Viewable aClass1= (Viewable) modelElement;
			if(!loggedObjects.contains(uniquenessConstraint)){
				loggedObjects.add(uniquenessConstraint);
				errs.addEvent(errFact.logInfoMsg("validate unique constraint {0}...",getScopedName(uniquenessConstraint)));
			}
			StringBuilder contentUniqueAttrs = new StringBuilder();
			ArrayList<String> uniqueConstraintAttrs=new ArrayList<String>();
			// gets all constraint attribute-names.
			Iterator iter = uniquenessConstraint.getElements().iteratorAttribute();
			uniqueConstraintAttrs.add(aClass1.toString());
			while (iter.hasNext()){
				ObjectPath object = (ObjectPath)iter.next();
				uniqueConstraintAttrs.add(object.getLastPathEl().getName());
				contentUniqueAttrs.append(object.getLastPathEl().getName());
			}
			if (!listOfUniqueObj.contains(uniqueConstraintAttrs)){
				listOfUniqueObj.add(uniqueConstraintAttrs);
			}
			Holder<AttributeArray> values = new Holder<AttributeArray>();
			String returnValue = validateUnique(iomObj,uniqueConstraintAttrs, values);
			if (returnValue == null){
				// ok
			} else {
				if(ValidationConfig.WARNING.equals(checkUniqueConstraint)){
					logMsg(checkUniqueConstraint,"Unique is violated! Values {0} already exist in Object: {1}", values.value.valuesAsString(), returnValue);
				} else {
					String msg=validationConfig.getConfigValue(getScopedName(uniquenessConstraint), ValidationConfig.MSG);
					if(msg!=null && msg.length()>0){
						logMsg(checkConstraint,msg);
					} else {
						logMsg(checkConstraint,"Unique is violated! Values {0} already exist in Object: {1}", values.value.valuesAsString(), returnValue);
					}
				}
			}
		}
	}
	
	private HashMap<SetConstraint,Collection<String>> setConstraints=new HashMap<SetConstraint,Collection<String>>();
	private Iterator<String> allObjIterator=null;
	
	private void collectSetConstraintObjs(IomObject iomObj, SetConstraint setConstraintObj) {
		Evaluable preCondition = (Evaluable) setConstraintObj.getPreCondition();
		if(preCondition != null){
			Value preConditionValue = evaluateExpression(iomObj, preCondition);
			if (preConditionValue.isNotYetImplemented()){
				logMsg(checkConstraint,"Function in set constraint {0} is not yet implemented.",getScopedName(setConstraintObj));
				return;
			}
			if (preConditionValue.skipEvaluation()){
				return;
			}
			if (!preConditionValue.isTrue()){
				// ignore this object in set constraint
				return;
			}
			// consider object in set constraint
		}
		// save object
		Collection<String> objs=setConstraints.get(setConstraintObj);
		if(objs==null){
			objs=new HashSet<String>();
			setConstraints.put(setConstraintObj,objs);
		}
		objs.add(iomObj.getobjectoid());
	}
	private void validateSetConstraint(SetConstraint setConstraintObj) {
		Collection<String> objs=setConstraints.get(setConstraintObj);
		if(objs!=null && objs.size()>0){
			for(String oid:objs){
				allObjIterator=objs.iterator();
				IomObject iomObj=objectPool.getObject(oid, null, null);
				Evaluable condition = (Evaluable) setConstraintObj.getCondition();
				Value constraintValue = evaluateExpression(iomObj, condition);
				if (constraintValue.isNotYetImplemented()){
					logMsg(checkConstraint,"Function in set constraint {0} is not yet implemented.",getScopedName(setConstraintObj));
					return;
				}
				if (constraintValue.skipEvaluation()){
					return;
				}
				if (constraintValue.isTrue()){
					// ok
				} else {
					String msg=validationConfig.getConfigValue(getScopedName(setConstraintObj), ValidationConfig.MSG);
					if(msg!=null && msg.length()>0){
						logMsg(checkConstraint,msg);
					} else {
						logMsg(checkConstraint,"Set Constraint {0} is not true.", getScopedName(setConstraintObj));
					}
				}
			}
		}
	}
	
	private void validateMandatoryConstraint(IomObject iomObj, MandatoryConstraint mandatoryConstraintObj) {
		Evaluable condition = (Evaluable) mandatoryConstraintObj.getCondition();
		Value conditionValue = evaluateExpression(iomObj, condition);
		if (!conditionValue.isNotYetImplemented()){
			if (!conditionValue.skipEvaluation()){
				if (conditionValue.isTrue()){
					// ok
				} else {
					String msg=validationConfig.getConfigValue(getScopedName(mandatoryConstraintObj), ValidationConfig.MSG);
					if(msg!=null && msg.length()>0){
						logMsg(checkConstraint,msg);
					} else {
						logMsg(checkConstraint,"Mandatory Constraint {0} is not true.", getScopedName(mandatoryConstraintObj));
					}
				}
			}
		} else {
			if(condition instanceof FunctionCall){
				FunctionCall functionCallObj = (FunctionCall) condition;
				Function function = functionCallObj.getFunction();
				logMsg(checkConstraint,"Function {0} is not yet implemented.", function.getScopedName(null));
				Value.createNotYetImplemented();
			} else {
				logMsg(checkConstraint,"MandatoryConstraint {0} of {1} is not yet implemented.", mandatoryConstraintObj.getScopedName(null), iomObj.getobjecttag());
				Value.createNotYetImplemented();
			}
		}
	}

	private Value evaluateExpression(IomObject iomObj, Evaluable expression) {
		TextType texttype = new TextType();
		if(expression instanceof Equality){
			// ==
			Equality equality = (Equality) expression;
			Evaluable leftExpression = (Evaluable) equality.getLeft();
			Evaluable rightExpression = (Evaluable) equality.getRight();
			Value leftValue=evaluateExpression(iomObj,leftExpression);
			// if isError, return error.
			if (leftValue.skipEvaluation()){
				return leftValue;
			}
			if (leftValue.isUndefined()){
				return Value.createSkipEvaluation();
			}
			
			Value rightValue=evaluateExpression(iomObj,rightExpression);
			// if isError, return error.
			if (rightValue.skipEvaluation()){
				return rightValue;
			}
			if (rightValue.isUndefined()){
				return Value.createSkipEvaluation();
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
			if (leftValue.skipEvaluation()){
				return leftValue;
			}
			if (leftValue.isUndefined()){
				return Value.createSkipEvaluation();
			}
			Value rightValue=evaluateExpression(iomObj,rightExpression);
			// if isError, return error.
			if (rightValue.skipEvaluation()){
				return rightValue;
			}
			if (rightValue.isUndefined()){
				return Value.createSkipEvaluation();
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
			if (leftValue.skipEvaluation()){
				return leftValue;
			}
			if (leftValue.isUndefined()){
				return Value.createSkipEvaluation();
			}
			Value rightValue=evaluateExpression(iomObj,rightExpression);
			// if isError, return error.
			if (rightValue.skipEvaluation()){
				return rightValue;
			}
			if (rightValue.isUndefined()){
				return Value.createSkipEvaluation();
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
			if (leftValue.skipEvaluation()){
				return leftValue;
			}
			if (leftValue.isUndefined()){
				return Value.createSkipEvaluation();
			}
			Value rightValue=evaluateExpression(iomObj,rightExpression);
			// if isError, return error.
			if (rightValue.skipEvaluation()){
				return rightValue;
			}
			if (rightValue.isUndefined()){
				return Value.createSkipEvaluation();
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
			if (leftValue.skipEvaluation()){
				return leftValue;
			}
			if (leftValue.isUndefined()){
				return Value.createSkipEvaluation();
			}
			Value rightValue=evaluateExpression(iomObj,rightExpression);
			// if isError, return error.
			if (rightValue.skipEvaluation()){
				return rightValue;
			}
			if (rightValue.isUndefined()){
				return Value.createSkipEvaluation();
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
			if (leftValue.skipEvaluation()){
				return leftValue;
			}
			if (leftValue.isUndefined()){
				return Value.createSkipEvaluation();
			}
			Value rightValue=evaluateExpression(iomObj,rightExpression);
			// if isError, return error.
			if (rightValue.skipEvaluation()){
				return rightValue;
			}
			if (rightValue.isUndefined()){
				return Value.createSkipEvaluation();
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
			if (arg.skipEvaluation()){
				return arg;
			}
			if (arg.isUndefined()){
				return Value.createSkipEvaluation();
			}
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
				if (arg.skipEvaluation()){
					return arg;
				}
				if (arg.isUndefined()){
					return Value.createSkipEvaluation();
				}
				if(!arg.isTrue()){
					return new Value(false);
				}
			}
			return new Value(true);
		} else if(expression instanceof DefinedCheck){
			// DEFINED
			DefinedCheck defined = (DefinedCheck) expression;
			Value arg=evaluateExpression(iomObj,defined.getArgument());
			if(arg.skipEvaluation()){
				return arg;
			}
			if(arg.isUndefined()){
				return new Value(false);
			} else {
				return new Value(true);
			}
		} else if(expression instanceof Disjunction){
			// OR
			Disjunction disjunction = (Disjunction) expression;
			Evaluable[] disjunctionArray = (Evaluable[]) disjunction.getDisjoined();
			for (int i=0;i<disjunctionArray.length;i++){
				Value arg=evaluateExpression(iomObj,disjunctionArray[i]);
				if (arg.skipEvaluation()){
					return arg;
				}
				if (arg.isUndefined()){
					return Value.createSkipEvaluation();
				}
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
					String value = getEnumerationConstantXtfValue(enumObj);
					if (value.equals("true")){
						return new Value(true);
					} else if (value.equals("false")){
						return new Value(false);
					}
					return new Value(texttype, value);
				}
			// TODO instance of EnumerationRange
			} else if (constantObj instanceof Constant.Text){
				Constant.Text textConstant = (Constant.Text) constantObj;
				if(textConstant!=null){
					return new Value(texttype, textConstant.getValue());
			}
			} else if (constantObj instanceof Constant.Numeric){
				Constant.Numeric numericConstant = (Constant.Numeric) constantObj;
				if(numericConstant!=null){
					return new Value(Integer.valueOf(numericConstant.getValue().toString()));
				}
			} else if (constantObj instanceof Constant.Class){
				Constant.Class classConstant = (Constant.Class) constantObj;
				if(classConstant!=null){
					if(classConstant.getValue() instanceof Viewable){
						Viewable classValue = (Viewable) classConstant.getValue();
						while(classValue.getExtending()!=null){
							classValue = (Viewable) classValue.getExtending();
						}
						return new Value(classValue);
					}
				}
			} else if (constantObj instanceof Constant.Undefined){
				return Value.createUndefined();
			} else if (constantObj instanceof Constant.AttributePath){
				Constant.AttributePath attrPath = (Constant.AttributePath) constantObj;
				if(attrPath.getValue() instanceof LocalAttribute){
					LocalAttribute attrLocal = (LocalAttribute) attrPath.getValue();
					String attrName = attrLocal.getName();
					return new Value(texttype, attrName);
				}
			}
		//TODO instance of ConditionalExpression
		} else if(expression instanceof FunctionCall){
			FunctionCall functionCallObj = (FunctionCall) expression;
			Function function = functionCallObj.getFunction();
			if(function.getScopedName(null).equals("INTERLIS.len") || function.getScopedName(null).equals("INTERLIS.lenM")){
				Evaluable[] arguments = functionCallObj.getArguments();
				for(Evaluable anArgument : arguments){
					Value arg=evaluateExpression(iomObj,anArgument);
					if (arg.skipEvaluation()){
						return arg;
						}
					if (arg.isUndefined()){
						return Value.createSkipEvaluation();
					}
					if(arg.getValue()!=null){
						int lengthOfArgument = arg.getValue().length();
						return new Value(lengthOfArgument);
					}
				}
				return new Value(false);
			} else if(function.getScopedName(null).equals("INTERLIS.trim") || function.getScopedName(null).equals("INTERLIS.trimM")){
				Evaluable[] arguments = functionCallObj.getArguments();
				for(Evaluable anArgument : arguments){
					Value arg=evaluateExpression(iomObj,anArgument);
					if (arg.skipEvaluation()){
						return arg;
					}
					if (arg.isUndefined()){
						return Value.createSkipEvaluation();
				}
					return new Value(texttype, arg.getValue().trim());
				}
			} else if (function.getScopedName(null).equals("INTERLIS.isEnumSubVal")){
				Evaluable[] arguments = functionCallObj.getArguments();
				Value subEnum=evaluateExpression(iomObj,arguments[0]);
				if (subEnum.skipEvaluation()){
					return subEnum;
				}
				if (subEnum.isUndefined()){
					return Value.createSkipEvaluation();
				}
				Value nodeEnum=evaluateExpression(iomObj,arguments[1]);
				if (nodeEnum.skipEvaluation()){
					return nodeEnum;
				}
				if (nodeEnum.isUndefined()){
					return Value.createSkipEvaluation();
				}
				if(subEnum.getValue().toString().startsWith(nodeEnum.getValue().toString())){
					return new Value(true);
				} else {
					return new Value(false);
				}
			} else if (function.getScopedName(null).equals("INTERLIS.inEnumRange")){
				Evaluable[] arguments = functionCallObj.getArguments();
				Value enumToCompare=evaluateExpression(iomObj,arguments[0]);
				if (enumToCompare.skipEvaluation()){
					return enumToCompare;
				}
				if (enumToCompare.isUndefined()){
					return Value.createSkipEvaluation();
				}
				Value minEnum=evaluateExpression(iomObj,arguments[1]);
				if (minEnum.skipEvaluation()){
					return minEnum;
				}
				if (minEnum.isUndefined()){
					return Value.createSkipEvaluation();
				}
				Value maxEnum=evaluateExpression(iomObj,arguments[2]);
				if (maxEnum.skipEvaluation()){
					return maxEnum;
				}
				if (maxEnum.isUndefined()){
					return Value.createSkipEvaluation();
				}
				if (enumToCompare.getType() instanceof EnumerationType){
					EnumerationType enumerationType = (EnumerationType) enumToCompare.getType();
						// enumeration has to be ordered
						if(enumerationType.isOrdered()){
						// enumerations from same enumeration
						if(enumToCompare.getRefTypeName().equals(minEnum.getRefTypeName()) && (enumToCompare.getRefTypeName().equals(maxEnum.getRefTypeName()))){
							int indexOfEnumToCompare = enumerationType.getValues().indexOf(enumToCompare.getValue());
							int indexOfMinEnumValue = enumerationType.getValues().indexOf(minEnum.getValue());
							int indexOfMaxEnumValue = enumerationType.getValues().indexOf(maxEnum.getValue());
							// enum is between min and max
							if(indexOfEnumToCompare > indexOfMinEnumValue && indexOfEnumToCompare < indexOfMaxEnumValue){
								return new Value(true);
							}
						}
					}
				}
				return new Value(false);
			} else if (function.getScopedName(null).equals("INTERLIS.objectCount")){
				FunctionCall functionCall = (FunctionCall) expression;
				Evaluable[] arguments = (Evaluable[]) functionCall.getArguments();
				Evaluable anArgument = (Evaluable) arguments[0];
				Value value=evaluateExpression(iomObj, anArgument);
				if (value.skipEvaluation()){
					return value;
				}
				if (value.isUndefined()){
					return Value.createSkipEvaluation();
				}
				if(value.getComplexObjects()!=null){
					return new Value(value.getComplexObjects().size());
				} else if(value.getViewable()!=null) {
					Iterator objectIterator = objectPool.getObjectsOfBasketId(currentBasketId).valueIterator();
					int counter = 0;
					while(objectIterator.hasNext()){
						IomObject aIomObj = (IomObject) objectIterator.next();
						if(aIomObj!=null){
							Object modelElement=tag2class.get(aIomObj.getobjecttag());
							Viewable anObjectClass = (Viewable) modelElement;
							if(value.getViewable().equals(anObjectClass)){
								counter+=1;
							}
						}
					}
					return new Value(counter);
				}
			} else if (function.getScopedName(null).equals("INTERLIS.elementCount")){
				FunctionCall functionCall = (FunctionCall) expression;
				Evaluable[] arguments = (Evaluable[]) functionCall.getArguments();
				Evaluable anArgument = (Evaluable) arguments[0];
				Value value=evaluateExpression(iomObj, anArgument);
				if (value.skipEvaluation()){
					return value;
				}
				if (value.isUndefined()){
					return Value.createSkipEvaluation();
				}
				return new Value(value.getComplexObjects().size());
			} else if (function.getScopedName(null).equals("INTERLIS.isOfClass")){
				FunctionCall functionCall = (FunctionCall) expression;
				Evaluable[] arguments = functionCall.getArguments();
				Value childViewable=evaluateExpression(iomObj,arguments[0]);
				if (childViewable.skipEvaluation()){
					return childViewable;
				}
				if (childViewable.isUndefined()){
					return Value.createSkipEvaluation();
				}
				Value parentViewable=evaluateExpression(iomObj,arguments[1]);
				if (parentViewable.skipEvaluation()){
					return parentViewable;
				}
				if (parentViewable.isUndefined()){
					return Value.createSkipEvaluation();
				}
				if(childViewable.getViewable().equals(parentViewable.getViewable())){
					return new Value(true);
				}
				if(parentViewable.getViewable().isExtending(childViewable.getViewable())){
					return new Value(true);					
				}
				return new Value(false);
			} else if (function.getScopedName(null).equals("INTERLIS.isSubClass")){
				FunctionCall functionCall = (FunctionCall) expression;
				Evaluable[] arguments = functionCall.getArguments();
				Value subViewable=evaluateExpression(iomObj,arguments[0]);
				if (subViewable.skipEvaluation()){
					return subViewable;
				}
				if (subViewable.isUndefined()){
					return Value.createSkipEvaluation();
				}
				Value superViewable=evaluateExpression(iomObj,arguments[1]);
				if (superViewable.skipEvaluation()){
					return superViewable;
				}
				if (superViewable.isUndefined()){
					return Value.createSkipEvaluation();
				}
				if(subViewable.getViewable().equals(superViewable.getViewable())){
					return new Value(true);
				}
				if(superViewable.getViewable().isExtending(subViewable.getViewable())){
					return new Value(true);					
				}
				return new Value(false);
			} else if (function.getScopedName(null).equals("INTERLIS.myClass")){
				FunctionCall functionCall = (FunctionCall) expression;
				Evaluable[] arguments = functionCall.getArguments();
				Value targetViewable=evaluateExpression(iomObj,arguments[0]);
				if (targetViewable.skipEvaluation()){
					return targetViewable;
				}
				if (targetViewable.isUndefined()){
					return Value.createSkipEvaluation();
				}
				return new Value(targetViewable.getViewable());
			} else if (function.getScopedName(null).equals("INTERLIS.areAreas")){
				FunctionCall functionCall = (FunctionCall) expression;
				Evaluable[] arguments = functionCall.getArguments();
				// founded objects (list<IomObjects)
				Value objects=evaluateExpression(iomObj,arguments[0]);
				if (objects.skipEvaluation()){
					return objects;
				}
				if (objects.isUndefined()){
					return Value.createSkipEvaluation();
				}
				// count of objects condition returns attrName of BAG / undefined=(numericIsDefined=false)
				Value surfaceBag=evaluateExpression(iomObj,arguments[1]);
				if (surfaceBag.skipEvaluation()){
					return surfaceBag;
				}
				// name of surface (textType)
				Value surfaceAttr=evaluateExpression(iomObj,arguments[2]);
				if (surfaceAttr.skipEvaluation()){
					return surfaceAttr;
				}
				if (surfaceAttr.isUndefined()){
					return Value.createSkipEvaluation();
				} // if surfaceBag is undefined
				if(surfaceBag.isUndefined()){
					ItfAreaPolygon2Linetable polygonPool = new ItfAreaPolygon2Linetable(objPoolManager); // create new pool of polygons
					if(objects.getViewable()!=null){
						Iterator objectIterator = objectPool.getObjectsOfBasketId(currentBasketId).valueIterator();
						while(objectIterator.hasNext()){
							IomObject aIomObj = (IomObject) objectIterator.next();
							if(aIomObj!=null){
								Object modelElement=tag2class.get(aIomObj.getobjecttag());
								Viewable anObjectClass = (Viewable) modelElement;
								if(objects.getViewable().equals(anObjectClass)){
									IomObject polygon = aIomObj.getattrobj(surfaceAttr.getValue(), 0); // get polygon of current object
									if(polygon!=null){ // if value of Argument[2] equals object attribute
										try { // add polylines to polygonPool.
											polygonPool.addLines(aIomObj.getobjectoid(), null, polygonPool.getLinesFromPolygon(polygon));
										} catch (IoxException e) {
											e.getStackTrace();
										}
									}
								}
							}
						}
						// if objects.equals(anObjectClass) never equal, handling.
					} else {
						Iterator iterIomObjects = objects.getComplexObjects().iterator(); // iterate through all objects of Argument[0] 
						while(iterIomObjects.hasNext()){
							IomObject anObject = (IomObject) iterIomObjects.next();
							IomObject polygon = anObject.getattrobj(surfaceAttr.getValue(), 0); // get polygon of current object
							if(polygon!=null){ // if value of Argument[2] equals object attribute
								try { // add polylines to polygonPool.
									polygonPool.addLines(anObject.getobjectoid(), null, polygonPool.getLinesFromPolygon(polygon));
								} catch (IoxException e) {
									e.getStackTrace();
								}
							}
						}
					}
					List<Intersection> intersections=polygonPool.validate();
					if(intersections!=null){
						return new Value(false); // not a valid area topology
					}
					return new Value(true); // valid areas
				} else {
					// if surfaceBag is defined
					ItfAreaPolygon2Linetable polygonPool = new ItfAreaPolygon2Linetable(objPoolManager);
					if(objects.getViewable()!=null){
						Iterator objectIterator = objectPool.getObjectsOfBasketId(currentBasketId).valueIterator();
						while(objectIterator.hasNext()){
							IomObject aIomObj = (IomObject) objectIterator.next();
							if(aIomObj!=null){
								Object modelElement=tag2class.get(aIomObj.getobjecttag());
								Viewable anObjectClass = (Viewable) modelElement;
								if(objects.getViewable().equals(anObjectClass)){
									int countOfSurfaceBagValues = aIomObj.getattrvaluecount(surfaceBag.getValue());
									for(int i=0; i<countOfSurfaceBagValues; i++){
										IomObject surfaceBagObj = aIomObj.getattrobj(surfaceBag.getValue(), i);
										IomObject polygon = surfaceBagObj.getattrobj(surfaceAttr.getValue(), 0);
										if(polygon!=null){ // if value of Argument[2] equals object attribute
											try { // add polylines to polygonPool.
												polygonPool.addLines(aIomObj.getobjectoid(), null, polygonPool.getLinesFromPolygon(polygon));
											} catch (IllegalStateException e) {
												throw new IllegalStateException(e);
											} catch (IoxException e) {
												e.getStackTrace();
											}
										}
									}
								}
							}
						}
					} else {
						Iterator iterIomObjects = objects.getComplexObjects().iterator();
						while(iterIomObjects.hasNext()){
							IomObject anObject = (IomObject) iterIomObjects.next();
							int countOfSurfaceBagValues = anObject.getattrvaluecount(surfaceBag.getValue());
							for(int i=0; i<countOfSurfaceBagValues; i++){
								IomObject surfaceBagObj = anObject.getattrobj(surfaceBag.getValue(), i);
								IomObject polygon = surfaceBagObj.getattrobj(surfaceAttr.getValue(), 0);
								if(polygon!=null){
									try {
										polygonPool.addLines(anObject.getobjectoid(), null, polygonPool.getLinesFromPolygon(polygon));
									} catch (IoxException e) {
										e.getStackTrace();
									}
								} else {
									// there is no area to compare. --> area not false and not true.
								}
							}
						}
					}
					List<Intersection> intersections=polygonPool.validate();
					if(intersections!=null) {
						return new Value(false);
					}
					return new Value(true);
				}
			} else {
				String functionQname=function.getScopedName(null);
				Class functionTargetClass=customFunctions.get(functionQname);
				if(functionTargetClass==null){
					return Value.createNotYetImplemented();
				}
				// get values for all actual arguments
				FunctionCall functionCall = (FunctionCall) expression;
				Evaluable[] arguments = functionCall.getArguments();
				int argumentCount = functionCall.getArguments().length;
				Value[] actualArguments=new Value[argumentCount];
				for(int i=0; i<argumentCount;i++){
					Value anObject=evaluateExpression(iomObj,arguments[i]);
					if (anObject.skipEvaluation()){
						return anObject;
					}
					actualArguments[i]=anObject;
				}
				// init function
				InterlisFunction functionTarget=null;
				try {
					functionTarget = (InterlisFunction) functionTargetClass.newInstance();
				} catch (InstantiationException e) {
					throw new IllegalStateException(e);
				} catch (IllegalAccessException e) {
					throw new IllegalStateException(e);
				}
				functionTarget.init(td,config,validationConfig, objectPool, errFact);
				return functionTarget.evaluate(iomObj, actualArguments);
			}
			//TODO INTERLIS.convertUnit
			//TODO instance of InspectionFactor
			//TODO instance of LengthOfReferencedText
		} else if(expression instanceof ObjectPath){
			// object path	
			ObjectPath objectPathObj = (ObjectPath) expression;
			PathEl pathEl = (PathEl) objectPathObj.getLastPathEl();
			if(pathEl instanceof PathElAbstractClassRole){
				PathElAbstractClassRole abstractClassRole = (PathElAbstractClassRole) pathEl;
				if (abstractClassRole.getRole()!=null){
					RoleDef role = (RoleDef) abstractClassRole.getRole();
					AbstractClassDef destinationClass = role.getDestination();
					while(destinationClass.getExtending()!=null){
						destinationClass.getExtending();
					}
					Viewable pathElementOfClassRole = (Viewable) destinationClass;
					return new Value(pathElementOfClassRole);
				}
			} else if(pathEl instanceof StructAttributeRef){
				StructAttributeRef structAttributeRefValue = (StructAttributeRef) pathEl;
				if(structAttributeRefValue.getAttr() instanceof LocalAttribute){
					LocalAttribute localAttributeValue = (LocalAttribute) structAttributeRefValue.getAttr();
					if(localAttributeValue.getDomain() instanceof CompositionType){
						CompositionType compositionValue = (CompositionType) localAttributeValue.getDomain();
						if(compositionValue.getComponentType() instanceof Viewable){
							Viewable referredStructOfAttr = (Viewable) compositionValue.getComponentType();
							return new Value(referredStructOfAttr);
						}
					}
				}
			} else if(pathEl instanceof AttributeRef){
			AttributeRef attrRef = (AttributeRef) pathEl;
			Type type = attrRef.getAttr().getDomain();
			String attrName = objectPathObj.getLastPathEl().getName();
			if(iomObj.getattrvaluecount(attrName)==0){
				return Value.createUndefined();
			}else{
				String objValue = iomObj.getattrvalue(attrName);
				if(objValue != null){
					if (objValue.equals("true")){
						return new Value(true);
					} else if (objValue.equals("false")){
						return new Value(false);
						// if null, then complex value.
					} else {
							Type aliasType=null;
						if (type instanceof TypeAlias){
							TypeAlias typeAlias = (TypeAlias) type;
								aliasType = typeAlias.getAliasing().getType();
								if (aliasType instanceof EnumerationType){
									String refTypeName = typeAlias.getAliasing().getName();
									return new Value(aliasType, objValue, refTypeName);
								}
							}
							if (type instanceof EnumerationType){
								return new Value(type, objValue);
							}
						}
						return new Value(type, objValue);
					} else {
						List<IomObject> objects = new ArrayList<IomObject>();
						int attrValueCount = iomObj.getattrvaluecount(attrName);
						// iterate, because it's a list of attrObjects.
						for(int i=0;i<attrValueCount;i++){
							objects.add(iomObj.getattrobj(attrName, i));
						}
						return new Value(objects);
					}
				}
			}
		} else if(expression instanceof Objects){
			// objects
			Iterator<String> objectIterator = allObjIterator;
			List<IomObject> listOfIomObjects = new ArrayList<IomObject>();
			if(allObjIterator==null){
				 throw new IllegalStateException("allObjIterator==null");
			}
			while(objectIterator.hasNext()){
				String oid=objectIterator.next();
				IomObject aIomObj = objectPool.getObject(oid, null, null);
				if(aIomObj!=null){
					listOfIomObjects.add(aIomObj);
				}
			}
			return new Value(listOfIomObjects);
		} else {
			logMsg(checkConstraint,"expression {0} is not yet implemented.",expression.toString());
		}
		return Value.createSkipEvaluation(); // skip further evaluation
		//TODO instance of ParameterValue
		//TODO instance of ViewableAggregate
		//TODO instance of ViewableAlias
	}
	private String getEnumerationConstantXtfValue(Constant.Enumeration enumValue)
	{
		String value[] = enumValue.getValue();
		StringBuilder buf = new StringBuilder(100);
		for (int i = 0; i < value.length; i++) {
			if (i > 0) {
				buf.append('.');
			}
			buf.append(value[i]);
		}
		return buf.toString();
	}
	private void validateRoleCardinality(RoleDef role, IomObject iomObj) {
		String roleQName = null;
		roleQName = getScopedName(role);
		String multiplicity=validationConfig.getConfigValue(roleQName, ValidationConfig.MULTIPLICITY);
		if(multiplicity != null && ValidationConfig.OFF.equals(multiplicity)){
			if(!validationConfigOff.contains(ValidationConfig.MULTIPLICITY)){
				validationConfigOff.add(ValidationConfig.MULTIPLICITY);
				errs.addEvent(errFact.logInfoMsg("{0} not validated, validation configuration multiplicity=off", roleQName, iomObj.getobjecttag(), iomObj.getobjectoid()));
			}
		}else{
			int nrOfTargetObjs=linkPool.getTargetObjectCount(iomObj,role,doItfOidPerTable);
			long cardMin=role.getCardinality().getMinimum();
			long cardMax=role.getCardinality().getMaximum();
			if((nrOfTargetObjs>=cardMin && nrOfTargetObjs<=cardMax)){
				// is valid
			} else {
				if(role.getCardinality().getMaximum() == Cardinality.UNBOUND){
					String cardMaxStr = "*";
					logMsg(multiplicity,"{0} should associate {1} to {2} target objects (instead of {3})", role.getName(), String.valueOf(cardMin), cardMaxStr, String.valueOf(nrOfTargetObjs));
				} else {
					logMsg(multiplicity,"{0} should associate {1} to {2} target objects (instead of {3})", role.getName(), String.valueOf(cardMin), String.valueOf(cardMax), String.valueOf(nrOfTargetObjs));
				}
			}
		}
	}

	private void validateRoleReference(RoleDef role, IomObject iomObj) {
		String roleQName = getScopedName(role);
		String validateTarget=null;
		if(!enforceTargetValidation){
			validateTarget=validationConfig.getConfigValue(roleQName, ValidationConfig.TARGET);
		}
		if(ValidationConfig.OFF.equals(validateTarget)){
			if(!validationConfigOff.contains(ValidationConfig.TARGET)){
				validationConfigOff.add(ValidationConfig.TARGET);
				errs.addEvent(errFact.logInfoMsg("{0} not validated, validation configuration target=off", roleQName, iomObj.getobjectoid()));
			}
		}else{
			String targetOid = null;
			// role of iomObj
			IomObject roleDefValue = iomObj.getattrobj(role.getName(), 0);
			if (roleDefValue != null){
				targetOid = roleDefValue.getobjectrefoid();
			}
			if(targetOid==null){
				return;
			}
		 	// OID has to be unique in each table (ili 1.0)
			// OID has to be unique in the whole file (ili 2.3)	
			Iterator<AbstractClassDef> targetClassIterator = role.iteratorDestination();
			ArrayList<Viewable> destinationClasses = new ArrayList<Viewable>();
			StringBuffer possibleTargetClasses=new StringBuffer();
			String sep="";
			while(targetClassIterator.hasNext()){
				Viewable roleDestinationClass = (Viewable) targetClassIterator.next();
				destinationClasses.add(roleDestinationClass);
				possibleTargetClasses.append(sep);
				sep=",";
				possibleTargetClasses.append(roleDestinationClass.getScopedName(null));
			}
			Holder<String> bid = new Holder<String>();
			IomObject targetObj = (IomObject) objectPool.getObject(targetOid, destinationClasses, bid);
			if(targetObj != null){
				Object modelElement=tag2class.get(targetObj.getobjecttag());
				Viewable targetObjClass = (Viewable) modelElement;
				for(Viewable destinationClass : destinationClasses){
					if(targetObjClass.equals(destinationClass)){
						// target is ok
						return;
					} else if(targetObjClass.getExtending() != null){
						if(targetObjClass.isExtending(destinationClass)){
							// target is ok
							return;
						}
					}
				}
				if(isBasketSame(role, null,bid.value)){
					// object found, but wrong class
					logMsg(validateTarget,"Object {1} with OID {0} must be of {2}", targetOid,targetObj.getobjecttag(),possibleTargetClasses.toString());
					return;
				}
			}
			if(isBasketSame(role, null,bid.value)){
				// no object with this oid found
				logMsg(validateTarget,"No object found with OID {0}.", targetOid);
			}
		}
	}
	
	private void validateReferenceAttrs(IomObject structValue, Table structure) {
		Iterator attrIter=structure.getAttributesAndRoles();
		while (attrIter.hasNext()){
			Object refAttrO = attrIter.next();
			if (refAttrO instanceof LocalAttribute){
				Type type = ((LocalAttribute)refAttrO).getDomain();
				// ReferenceType validation
				if (type instanceof ReferenceType){
					// targettype
					ReferenceType refAttrType = (ReferenceType) type;
					AbstractClassDef targetClass = refAttrType.getReferred();
					Viewable targetViewable = (Viewable) targetClass;
					ArrayList<Viewable> destinationClasses = new ArrayList<Viewable>();
					destinationClasses.add(targetViewable);
					String refAttrName = ((Element) refAttrO).getName();
					IomObject refAttrValue = structValue.getattrobj(refAttrName, 0);
					if (refAttrValue != null){
						String targetOid = refAttrValue.getobjectrefoid();
						String targetObjOid = null;
						String targetObjClassStr = null;
						Holder<String> bid = new Holder<String>();
						IomObject iomObj = (IomObject) objectPool.getObject(targetOid, destinationClasses, bid);
						if(iomObj != null){
							Object modelElement=tag2class.get(iomObj.getobjecttag());
							Table targetObjClass = (Table) modelElement;
							targetObjClassStr = iomObj.getobjecttag();
							targetObjOid = iomObj.getobjectoid().toString();
							Iterator refAttrIter = refAttrType.iteratorRestrictedTo();
							// if refAttrIter restricted to class
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
								if(isBasketSame(null, refAttrType,bid.value)){
									errs.addEvent(errFact.logErrorMsg("object {0} with OID {1} referenced by {3} is not an instance of {2}.", targetObjClassStr,targetObjOid,classNames.toString(),refAttrName));
									return;
								}
							}else{
								// compare class
								if (targetObjClass.isExtending(targetClass)){
									// ok
								} else {
									if(isBasketSame(null, refAttrType,bid.value)){
										errs.addEvent(errFact.logErrorMsg("object {0} with OID {1} referenced by {3} is not an instance of {2}.", targetObjClassStr,targetObjOid,targetClass.getScopedName(null),refAttrName));
									}
								}
								return;
							}
						} else {
							if(isBasketSame(null, refAttrType,bid.value)){
								errs.addEvent(errFact.logErrorMsg("attribute {1} references an inexistent object with OID {0}.", targetOid,refAttrName));
							}
						}
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
				String attrValueThisObj = iomObj.getattrvalue(otherAttrName);
				for (String basketId : objectPool.getBasketIds()){
					// iterate through iomObjects
					Iterator<IomObject> objectIterator = (objectPool.getObjectsOfBasketId(basketId)).valueIterator();
					while (objectIterator.hasNext()){
						IomObject otherIomObj = objectIterator.next();
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
			}
			if (!valueExists){
				String msg=validationConfig.getConfigValue(getScopedName(existenceConstraint), ValidationConfig.MSG);
				if(msg!=null && msg.length()>0){
					logMsg(checkConstraint,msg);
				} else {
					logMsg(checkConstraint,"The value of the attribute {0} of {1} was not found in the condition class.", restrictedAttrName.toString(), iomObj.getobjecttag().toString());
				}
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
//		if(referenceValueRestricted.equals(referenceValueOther)){
//			return true;
//		}
//		return false;
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
	HashMap<ArrayList<String>, HashMap<AttributeArray, String>> seenUniqueConstraintValues = new HashMap<ArrayList<String>, HashMap<AttributeArray, String>>();
	// List of all arrayLists of unique attributes and classes.
	ArrayList<ArrayList<String>> listOfUniqueObj = new ArrayList<ArrayList<String>>();
	// List of all object Oid's and associated classPath's of uniqueness validate of Oid's.
	Map<String , String> uniqueObjectIDs = new HashMap<String, String>();
	HashSet<Object> loggedObjects=new HashSet<Object>();
	
	private void validateObject(IomObject iomObj,String attrPath) throws IoxException {
		// validate if object is null
		boolean isObject = attrPath==null;
		if(isObject){
			setCurrentMainObj(iomObj);
		}
		// validate class, structure and association on existence of OID
		boolean addToPool = true;
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
						addToPool = false;
					}
				} 
			} else if (classValueOidValidate instanceof Table){
				Table classValueTable = (Table) classValueOidValidate;
				// class
				if (classValueTable.isIdentifiable()){
					if (iomObj.getobjectoid() == null){
						errs.addEvent(errFact.logErrorMsg("Class {0} has to have an OID", iomObj.getobjecttag()));
						addToPool = false;
					}
				// structure	
				} else {
					addToPool = false;
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
					UniquenessConstraint uniquenessConstraint=(UniquenessConstraint) obj1;
					validateUniquenessConstraint(iomObj, uniquenessConstraint);
				}
			}
		}
		
		if(isObject){
			if(addToPool){
				IomObject objectValue = objectPool.addObject(iomObj,currentBasketId);
				if (objectValue != null){
					Object modelElement=tag2class.get(objectValue.getobjecttag());
					Viewable classValueOfKey= (Viewable) modelElement;
					errs.addEvent(errFact.logErrorMsg("The OID {0} of object '{1}' already exists in {2}.", objectValue.getobjectoid(), iomObj.toString(), classValueOfKey.toString()));
			}
		}
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
					int propc=iomObj.getattrvaluecount(roleName);
					for(int propi=0;propi<propc;propi++){
						if (obj.embedded) {
							AssociationDef roleOwner = (AssociationDef) role
									.getContainer();
							if (roleOwner.getDerivedFrom() == null) {
								// not just a link?
								IomObject structvalue = iomObj.getattrobj(roleName, propi);
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
							IomObject structvalue = iomObj.getattrobj(roleName, propi);
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
						if (refoid != null) {
							linkPool.addLink(iomObj,role,refoid,doItfOidPerTable);
						}
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
	private void setCurrentMainObj(IomObject iomObj) {
		errFact.setDataObj(iomObj);
		currentMainOid=iomObj.getobjectoid();
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
									 SurfaceType surfaceType = (SurfaceType) type;
									 // SURFACE; no attributeValue in mainTable
								 }else{
									 AreaType areaType = (AreaType) type;
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
							CoordType coordType = (CoordType) type;
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
	private String validateUnique(IomObject currentObject,ArrayList<String>uniqueAttrs, Holder<AttributeArray> values) {
		
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
		values.value=new AttributeArray(currentObject.getobjectoid(), accu);
		HashMap<AttributeArray, String> allValues = null;
		if (seenUniqueConstraintValues.containsKey(uniqueAttrs)){
			allValues = seenUniqueConstraintValues.get(uniqueAttrs);
			if (allValues.containsKey(values.value)){
				return allValues.get(values.value);
			} else {
				allValues.put(values.value, currentObject.getobjectoid());
			}
		} else {
			allValues = new HashMap<AttributeArray, String>();
			allValues.put(values.value, currentObject.getobjectoid());
			seenUniqueConstraintValues.put(uniqueAttrs, allValues);
		}
		return null;
	}

	private void validateAttrValue(IomObject iomObj, AttributeDef attr,String attrPath) throws IoxException {
		 String attrName = attr.getName();
		 String attrQName = getScopedName(attr);
		 if(attrPath==null){
			 attrPath=attrName;
		 }else{
			 attrPath=attrPath+"/"+attrName;
		 }
		 String validateMultiplicity=validationConfig.getConfigValue(attrQName, ValidationConfig.MULTIPLICITY);
		 validateType=null;
		 String validateGeometryType=null;
		 if(!enforceTypeValidation){
			 validateType=validationConfig.getConfigValue(attrQName, ValidationConfig.TYPE);
			 if(validateType==null){
				 validateGeometryType=defaultGeometryTypeValidation;
			 }
		 }
		 
		Type type0 = attr.getDomain();
		Type type = attr.getDomainResolvingAliases();
		if (type instanceof CompositionType){
			 int structc=iomObj.getattrvaluecount(attrName);
				if(ValidationConfig.OFF.equals(validateMultiplicity)){
					if(!validationConfigOff.contains(ValidationConfig.MULTIPLICITY)){
						validationConfigOff.add(ValidationConfig.MULTIPLICITY);
						errs.addEvent(errFact.logInfoMsg("{0} not validated, validation configuration multiplicity=off", attrQName, iomObj.getobjecttag(), iomObj.getobjectoid()));
					}
				}else{
					 Cardinality card = ((CompositionType)type).getCardinality();
					 if(structc<card.getMinimum() || structc>card.getMaximum()){
						logMsg(validateMultiplicity,"Attribute {0} has wrong number of values", attrPath);
					 }
				}
			 for(int structi=0;structi<structc;structi++){
				 IomObject structEle=iomObj.getattrobj(attrName, structi);
					if(ValidationConfig.OFF.equals(validateType)){
						if(!validationConfigOff.contains(ValidationConfig.TYPE)){
							validationConfigOff.add(ValidationConfig.TYPE);
							errs.addEvent(errFact.logInfoMsg("{0} not validated, validation configuration type=off", attrQName, iomObj.getobjecttag(), iomObj.getobjectoid()));
						}
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
				if(!validationConfigOff.contains(ValidationConfig.MULTIPLICITY)){
					validationConfigOff.add(ValidationConfig.MULTIPLICITY);
					errs.addEvent(errFact.logInfoMsg("{0} not validated, validation configuration multiplicity=off", attrQName, iomObj.getobjecttag(), iomObj.getobjectoid()));
				}
			}else{
				Object topologyDone=pipelinePool.getIntermediateValue(attr, ValidationConfig.TOPOLOGY);
				if(topologyDone==null){
					 int structc=iomObj.getattrvaluecount(attrName);
					 if(attr.getDomain().isMandatoryConsideringAliases() && structc==0){
						 if(doItfLineTables && type instanceof SurfaceType){
							 // SURFACE; no attrValue in maintable
						 }else{
							 logMsg(validateMultiplicity,"Attribute {0} requires a value", attrPath);
						 }
					 }
				}else{
					Boolean topologyValidationOk=(Boolean)pipelinePool.getIntermediateValue(attr, ValidationConfig.TOPOLOGY_VALIDATION_OK);
					if(topologyValidationOk==null || topologyValidationOk){
						 int structc=iomObj.getattrvaluecount(attrName);
						 if(attr.getDomain().isMandatoryConsideringAliases() && structc==0){
							 logMsg(validateMultiplicity,"Attribute {0} requires a value", attrPath);
						 }
					}else{
						// topology validation failed
						// ignore missing values
					}
				}
			}
			if(ValidationConfig.OFF.equals(validateType)){
				if(!validationConfigOff.contains(ValidationConfig.TYPE)){
					validationConfigOff.add(ValidationConfig.TYPE);
					errs.addEvent(errFact.logInfoMsg("{0} not validated, validation configuration type=off", attrQName, iomObj.getobjecttag(), iomObj.getobjectoid()));
				}
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
						boolean isValid=validatePolyline(validateGeometryType, polylineType, polylineValue);
						if(isValid){
							validatePolylineTopology(attrPath,validateGeometryType, polylineType, polylineValue);
						}
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
							boolean isValid = validatePolygon(validateGeometryType,surfaceOrAreaType, surfaceValue);
							if(isValid){
								Object attrValidator=pipelinePool.getIntermediateValue(attr, ValidationConfig.TOPOLOGY);
								if(attrValidator==null){
									attrValidator=this;
									pipelinePool.setIntermediateValue(attr, ValidationConfig.TOPOLOGY,this);
								}
								if(attrValidator==this){
									if(surfaceOrAreaType instanceof SurfaceType){
										validateSurfaceTopology(validateGeometryType,attr,(SurfaceType)surfaceOrAreaType,currentMainOid, surfaceValue);
									}else{
										validateSurfaceTopology(validateGeometryType,attr,(AreaType)surfaceOrAreaType,currentMainOid, surfaceValue);
										if(!ValidationConfig.OFF.equals(areaOverlapValidation)){
											ItfAreaPolygon2Linetable allLines=areaAttrs.get(attr);
											if(allLines==null){
												allLines=new ItfAreaPolygon2Linetable(objPoolManager); 
												areaAttrs.put(attr,allLines);
											}
											validateAreaTopology(validateGeometryType,allLines,(AreaType)surfaceOrAreaType, currentMainOid,null,surfaceValue);
										}
									}
								}
							}
						}
					 }
				}else if(type instanceof CoordType){
					IomObject coord=iomObj.getattrobj(attrName, 0);
					if (coord!=null){
						validateCoordType(validateGeometryType, (CoordType)type, coord);
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

	private void validateAreaTopology(String validateType, ItfAreaPolygon2Linetable allLines,AreaType type, String mainObjTid,String internalTid,IomObject iomPolygon) throws IoxException {
		// get lines
		ArrayList<IomObject> lines=ItfAreaPolygon2Linetable.getLinesFromPolygon(iomPolygon);
		allLines.addLines(mainObjTid,internalTid,lines,validateType,errFact);
	}

	private void validateSurfaceTopology(String validateType, AttributeDef attr,SurfaceOrAreaType type, String mainObjTid,IomObject iomValue) {
		try {
			ItfSurfaceLinetable2Polygon.validatePolygon(mainObjTid, attr, iomValue, errFact,validateType);
		} catch (IoxException e) {
			errs.addEvent(errFact.logErrorMsg(e,"failed to validate polygon"));
		}
	}
	
	private void validatePolylineTopology(String attrPath,String validateType, PolylineType type, IomObject iomValue) {
		CompoundCurve seg=null;
		try {
			Holder<Boolean> foundErrs=new Holder<Boolean>();
			seg = Iox2jtsext.polyline2JTS(iomValue, false, 0.0,foundErrs,errFact,0.0,validateType,ValidationConfig.WARNING);
			if(seg==null || foundErrs.value){
				return;
			}
		} catch (IoxException e) {
			throw new IllegalStateException(e);
		}
		PrecisionDecimal overlapDef=type.getMaxOverlap();
		if(overlapDef!=null){
			double newVertexOffset=0.0;
			double maxOverlaps=overlapDef.doubleValue();
			if(maxOverlaps>0){
			    NumericalType[] dimensions = ((CoordType)type.getControlPointDomain().getType()).getDimensions();
				double size=((NumericType)dimensions[0]).getMinimum().getAccuracy();
				if(size>0){
					newVertexOffset=2*Math.pow(10, -size);
				}
			}
			ItfSurfaceLinetable2Polygon.removeValidSelfIntersections(seg,maxOverlaps,newVertexOffset);
			ArrayList<CompoundCurve> segv=new ArrayList<CompoundCurve>();
			segv.add(seg);
			CompoundCurveNoder validator=new CompoundCurveNoder(segv,false);
			if(!validator.isValid()){
				boolean hasIntersections=false;
				for(Intersection is:validator.getIntersections()){
					Coordinate[] pt=is.getPt();
					logMsg(validateType, "Attribute {0} has an invalid self-intersection at {1}",attrPath,"" + coordToString(pt[0])+(pt.length==2?(", coord2 "+coordToString(pt[1])):""));
				}
			}
		}
	}

	private String coordToString(Coordinate coord) {
		return "(" + coord.x + ", " + coord.y  + ")";
	}

	/* returns true, if polygon is valid
	 * 
	 */
	private boolean validatePolygon(String validateType, SurfaceOrAreaType surfaceOrAreaType, IomObject surfaceValue) {
		if (surfaceValue.getobjecttag().equals("MULTISURFACE")){
			boolean clipped = surfaceValue.getobjectconsistency()==IomConstants.IOM_INCOMPLETE;
			for(int surfacei=0;surfacei< surfaceValue.getattrvaluecount("surface");surfacei++){
				if(!clipped && surfacei>0){
					// unclipped surface with multi 'surface' elements
					logMsg(validateType,"invalid number of surfaces in COMPLETE basket");
					return false;
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
			return false;
		}
		return true;
	}

	// returns true if valid
	private boolean validatePolyline(String validateType, LineType polylineType, IomObject polylineValue) {
		boolean foundErrs=false;
		if (polylineValue.getobjecttag().equals("POLYLINE")){
			boolean clipped = polylineValue.getobjectconsistency()==IomConstants.IOM_INCOMPLETE;
			for(int sequencei=0;sequencei<polylineValue.getattrvaluecount("sequence");sequencei++){
				if(!clipped && sequencei>0){
					// an unclipped polyline should have only one sequence element
					logMsg(validateType,"invalid number of sequences in COMPLETE basket");
					foundErrs = foundErrs || true;
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
								foundErrs = foundErrs || true;
							}
						} else if (segment.getobjecttag().equals("ARC")){
							if(lineformNames.contains("ARCS") && segmenti>0){
								validateARCSType(validateType, (CoordType) polylineType.getControlPointDomain().getType(), segment);
							}else{
								logMsg(validateType, "unexpected ARC");
								foundErrs = foundErrs || true;
							}
						} else {
							logMsg(validateType, "unexpected Type "+segment.getobjecttag());
							foundErrs = foundErrs || true;
						}
					}
				} else {
					logMsg(validateType, "unexpected Type "+sequence.getobjecttag());
					foundErrs = foundErrs || true;
				}
			}
		} else {
			logMsg(validateType, "unexpected Type "+polylineValue.getobjecttag()+"; POLYLINE expected");
			foundErrs = foundErrs || true;
		}
		return !foundErrs;
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
		 if(ValidationConfig.OFF.equals(validateKind)){
			 // skip it
		 }else if(ValidationConfig.WARNING.equals(validateKind)){
			 errs.addEvent(errFact.logWarningMsg(msg, args));
		 }else{
			 errs.addEvent(errFact.logErrorMsg(msg, args));
		 }
	}
		
	private void validateItfLineTableObject(IomObject iomObj, AttributeDef modelele) {
		// validate if line table object
	}
}