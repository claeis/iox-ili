package ch.interlis.iox_j.validator;

import java.io.File;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.vividsolutions.jts.geom.Coordinate;
import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.settings.Settings;
import ch.ehi.basics.types.OutParam;
import ch.ehi.iox.objpool.ObjectPoolManager;
import ch.interlis.ili2c.Ili2cException;
import ch.interlis.ili2c.gui.UserSettings;
import ch.interlis.ili2c.metamodel.AbstractClassDef;
import ch.interlis.ili2c.metamodel.AreaType;
import ch.interlis.ili2c.metamodel.AssociationDef;
import ch.interlis.ili2c.metamodel.AssociationPath;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.AttributeRef;
import ch.interlis.ili2c.metamodel.Cardinality;
import ch.interlis.ili2c.metamodel.CompositionType;
import ch.interlis.ili2c.metamodel.Constant;
import ch.interlis.ili2c.metamodel.Constant.Enumeration;
import ch.interlis.ili2c.metamodel.Constraint;
import ch.interlis.ili2c.metamodel.CoordType;
import ch.interlis.ili2c.metamodel.DataModel;
import ch.interlis.ili2c.metamodel.Domain;
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
import ch.interlis.ili2c.metamodel.PathElAssocRole;
import ch.interlis.ili2c.metamodel.PathElRefAttr;
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
import ch.interlis.ilirepository.Dataset;
import ch.interlis.ilirepository.impl.RepositoryAccessException;
import ch.interlis.iom.IomConstants;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.itf.impl.ItfAreaPolygon2Linetable;
import ch.interlis.iom_j.itf.impl.ItfSurfaceLinetable2Polygon;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.noding.CompoundCurveNoder;
import ch.interlis.iom_j.itf.impl.jtsext.noding.Intersection;
import ch.interlis.iom_j.xtf.XtfReader;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxLogging;
import ch.interlis.iox.IoxReader;
import ch.interlis.iox.IoxValidationConfig;
import ch.interlis.iox.StartBasketEvent;
import ch.interlis.iox_j.IoxIntersectionException;
import ch.interlis.iox_j.IoxInvalidDataException;
import ch.interlis.iox_j.PipelinePool;
import ch.interlis.iox_j.jts.Iox2jtsext;
import ch.interlis.iox_j.logging.LogEventFactory;

public class Validator implements ch.interlis.iox.IoxValidator {
	public static final String ALL_OBJECTS_ACCESSIBLE="allObjectsAccessible";
	public static final String REGEX_FOR_ID_VALIDATION = "^[0-9a-zA-Z_][0-9a-zA-Z\\_\\.\\-]*";
	public static final String CONFIG_DO_ITF_LINETABLES="ch.interlis.iox_j.validator.doItfLinetables";
	public static final String CONFIG_DO_ITF_LINETABLES_DO="doItfLinetables";
	public static final String CONFIG_DO_ITF_OIDPERTABLE="ch.interlis.iox_j.validator.doItfOidPerTable";
	public static final String CONFIG_DO_ITF_OIDPERTABLE_DO="doItfOidPerTable";
	public static final String CONFIG_CUSTOM_FUNCTIONS="ch.interlis.iox_j.validator.customFunctions";
	public static final String CONFIG_OBJECT_RESOLVERS="ch.interlis.iox_j.validator.objectResolvers";
	// the object count result as value in map with the appropriate function as key.
	private Map<Function, Value> functions=new HashMap<Function, Value>();
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
	private String constraintValidation=null;
	private String defaultGeometryTypeValidation=null;
	Pattern patternForIdValidation = null;
	private boolean enforceTypeValidation=false;
	private boolean enforceConstraintValidation=false;
	private boolean enforceTargetValidation=false;
	private String currentBasketId = null;
	private String currentMainOid=null;
	private boolean autoSecondPass=true;
	private boolean allObjectsAccessible=false;
	private Map<AttributeDef,ItfAreaPolygon2Linetable> areaAttrs=new HashMap<AttributeDef,ItfAreaPolygon2Linetable>();
	private Map<String,Class> customFunctions=new HashMap<String,Class>(); // qualified Interlis function name -> java class that implements that function
	private List<ExternalObjectResolver> extObjResolvers=null; // java class that implements ExternalObjectResolver
	private HashMap<Constraint,Viewable> additionalConstraints=new HashMap<Constraint,Viewable>();
	private Map<PlausibilityConstraint, PlausibilityPoolValue> plausibilityConstraints=new LinkedHashMap<PlausibilityConstraint, PlausibilityPoolValue>();
	private HashSet<String> configOffOufputReduction =new HashSet<String>();
	private HashSet<String> setConstraintOufputReduction =new HashSet<String>();
	private HashSet<String> constraintOutputReduction=new HashSet<String>();
	private HashSet<String> datatypesOutputReduction=new HashSet<String>();
	private Map<String, String> uniquenessOfBid = new HashMap<String, String>();
	private String globalMultiplicity=null;
	private ch.interlis.ilirepository.ReposManager repositoryManager = null;
	
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
		if(errFact.getValidationConfig()==null){
			errFact.setValidationConfig(validationConfig);
		}
		
		this.config=config;
		this.patternForIdValidation = Pattern.compile(REGEX_FOR_ID_VALIDATION);
		this.config.setTransientObject(InterlisFunction.IOX_DATA_POOL,pipelinePool);
		this.pipelinePool=pipelinePool;
		objPoolManager=new ObjectPoolManager();
		Map<String,Class> cf=(Map<String, Class>) config.getTransientObject(CONFIG_CUSTOM_FUNCTIONS);
		if(cf!=null){
			customFunctions=cf;
		}
		List<Class> resolverClasses=(List<Class>) config.getTransientObject(CONFIG_OBJECT_RESOLVERS);
		if(resolverClasses!=null){
			extObjResolvers=new ArrayList<ExternalObjectResolver>();
			for(Class resolverClass:resolverClasses){
				ExternalObjectResolver resolver=null;
				try {
					resolver = (ExternalObjectResolver) resolverClass.newInstance();
				} catch (InstantiationException e) {
					throw new IllegalStateException(e);
				} catch (IllegalAccessException e) {
					throw new IllegalStateException(e);
				}
				resolver.init(td,config,validationConfig, objectPool, errFact);
				extObjResolvers.add(resolver);
			}
		}
		
        // get/create repository manager
        repositoryManager = (ch.interlis.ilirepository.ReposManager) config
                .getTransientObject(UserSettings.CUSTOM_ILI_MANAGER);
		
		this.doItfLineTables = CONFIG_DO_ITF_LINETABLES_DO.equals(config.getValue(CONFIG_DO_ITF_LINETABLES));
		this.doItfOidPerTable = CONFIG_DO_ITF_OIDPERTABLE_DO.equals(config.getValue(CONFIG_DO_ITF_OIDPERTABLE));
		allObjectsAccessible=ValidationConfig.TRUE.equals(validationConfig.getConfigValue(ValidationConfig.PARAMETER, ValidationConfig.ALL_OBJECTS_ACCESSIBLE));
		if(!allObjectsAccessible){
			errs.addEvent(errFact.logInfoMsg("assume unknown/external objects"));
		}
        disableRounding=ValidationConfig.TRUE.equals(validationConfig.getConfigValue(ValidationConfig.PARAMETER, ValidationConfig.DISABLE_ROUNDING));
        if(disableRounding){
            errs.addEvent(errFact.logInfoMsg("disable rounding"));
        }
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
		globalMultiplicity=validationConfig.getConfigValue(ValidationConfig.PARAMETER, ValidationConfig.MULTIPLICITY);
		if(globalMultiplicity!=null && ValidationConfig.ON.equals(globalMultiplicity)){
			globalMultiplicity=null;
		}
		areaOverlapValidation=this.validationConfig.getConfigValue(ValidationConfig.PARAMETER, ValidationConfig.AREA_OVERLAP_VALIDATION);
		constraintValidation=this.validationConfig.getConfigValue(ValidationConfig.PARAMETER, ValidationConfig.CONSTRAINT_VALIDATION);
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
			uniquenessOfBid.clear();
		} else if (event instanceof ch.interlis.iox.StartBasketEvent){
			StartBasketEvent startBasketEvent = ((ch.interlis.iox.StartBasketEvent) event);
			currentBasketId = ((ch.interlis.iox.StartBasketEvent) event).getBid();
			if (isValidId(currentBasketId)) {
			    validateUniqueBasketId(startBasketEvent);
			} else {
                errs.addEvent(errFact.logErrorMsg("value <{0}> is not a valid BID", currentBasketId==null?"":currentBasketId));
			}
		}else if(event instanceof ch.interlis.iox.ObjectEvent){
			IomObject iomObj=new ch.interlis.iom_j.Iom_jObject(((ch.interlis.iox.ObjectEvent)event).getIomObject());
			try {
                validateObject(iomObj,null,null);
			} catch (IoxException e) {
				errs.addEvent(errFact.logInfoMsg("failed to validate object {0}", iomObj.toString()));
			}catch(RuntimeException e) {
				EhiLogger.traceState("failing object: "+iomObj.toString());
				throw e;
			}
		} else if (event instanceof ch.interlis.iox.EndBasketEvent){
		}else if (event instanceof ch.interlis.iox.EndTransferEvent){
			if(autoSecondPass){
				doSecondPass();
			}
		}
	}
	private boolean isValidId(String valueStr) {
	    if(valueStr==null) {
	        return false;
	    }
	    
	    Matcher matcher = patternForIdValidation.matcher(valueStr);
	    if (matcher.matches()) {
	        return true;
	    } else {
	        return false;
	    }
    }

    public void doSecondPass() {
		errs.addEvent(errFact.logInfoMsg("second validation pass..."));
		iterateThroughAllObjects();
		validateAllAreas();
		validatePlausibilityConstraints();
	}
	
	private void validateUniqueBasketId(StartBasketEvent startBasketEvent) {
		// check if basket id is unique in transfer file
		if(currentBasketId != null){
			if(uniquenessOfBid.containsKey(currentBasketId)){
				errs.addEvent(errFact.logErrorMsg("BID {0} of {1} already exists in {2}", currentBasketId, startBasketEvent.getType().toString(), uniquenessOfBid.get(currentBasketId)));
			} else {
				uniquenessOfBid.put(currentBasketId, startBasketEvent.getType());
			}
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
				logMsg(additionalModel, "required additional model {0} not found", additionalModel);
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
					String constraintName = getScopedName(constraint);
					String checkConstraint=null;
					if(!enforceConstraintValidation){
						checkConstraint=validationConfig.getConfigValue(constraintName, ValidationConfig.CHECK);
					}
					if(ValidationConfig.OFF.equals(checkConstraint)){
						if(!configOffOufputReduction.contains(ValidationConfig.CHECK+":"+constraintName)){
							configOffOufputReduction.add(ValidationConfig.CHECK+":"+constraintName);
							errs.addEvent(errFact.logInfoMsg("{0} not validated, validation configuration check= off", constraintName));
						}
					}else{
						additionalConstraints.put(constraint, classValue);
					}
				}
			}
		}
	}
	
	private void validateAllAreas() {
		setCurrentMainObj(null);
		for(AttributeDef attr:areaAttrs.keySet()){
			errs.addEvent(errFact.logInfoMsg("validate AREA {0}...", getScopedName(attr)));
			ItfAreaPolygon2Linetable allLines=areaAttrs.get(attr);
			List<IoxInvalidDataException> intersections=allLines.validate();
			if(intersections!=null && intersections.size()>0){
				for(IoxInvalidDataException ex:intersections){ // iterate through non-overlay intersections
					String tid1=ex.getTid();
					String iliqname=ex.getIliqname();
					errFact.setTid(tid1);
					errFact.setIliqname(iliqname);
					//logMsg(areaOverlapValidation,"intersection tid1 "+is.getCurve1().getUserData()+", tid2 "+is.getCurve2().getUserData()+", coord "+is.getPt()[0].toString()+(is.getPt().length==2?(", coord2 "+is.getPt()[1].toString()):""));
					if(ex instanceof IoxIntersectionException) {
						logMsg(areaOverlapValidation, ((IoxIntersectionException) ex).getIntersection().toShortString());
						EhiLogger.traceState(ex.toString());
					}else {
						logMsg(areaOverlapValidation, ex.getMessage());
					}
				}
				setCurrentMainObj(null);
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
	
	private boolean isBasketSame(String bidOfTargetObject, IomObject iomObj){
		if(iomObj==null){
			return false;
		}
		Object modelElement=tag2class.get(iomObj.getobjecttag());
		Viewable classOfCurrentObj = (Viewable) modelElement;
		String oid = iomObj.getobjectoid();
		// instanceof association
		if(oid==null){
			oid=ObjectPool.getAssociationId(iomObj, (AssociationDef) modelElement);
		}
		String objectBid = objectPool.getBidOfObject(oid, classOfCurrentObj);		
		if(objectBid==null){
			return false;
		} else {
			if(objectBid.equals(bidOfTargetObject)){
				// bid's are same
				return true;
			}
		}
		// bid's are different
		return false;
	}

	private void iterateThroughAllObjects(){
		HashSet<Type> types=new HashSet<Type>();
		for (String basketId : objectPool.getBasketIds()){
			// iterate through iomObjects
			Iterator<IomObject> objectIterator = (objectPool.getObjectsOfBasketId(basketId)).valueIterator();
			while (objectIterator.hasNext()){
				IomObject iomObj = objectIterator.next();
				if(iomObj!=null){
					setCurrentMainObj(iomObj);
					errFact.setDefaultCoord(getDefaultCoord(iomObj));
					Object modelElement=tag2class.get(iomObj.getobjecttag());
					Viewable classOfCurrentObj= (Viewable) modelElement;
					if(!ValidationConfig.OFF.equals(constraintValidation)){
						// additional constraint
						for (Map.Entry<Constraint,Viewable> additionalConstraintsEntry : additionalConstraints.entrySet()) {
							Constraint additionalConstraint = additionalConstraintsEntry.getKey();
							Viewable classOfAdditionalConstraint = additionalConstraintsEntry.getValue();
							if(classOfCurrentObj.isExtending(classOfAdditionalConstraint)) {
								if(additionalConstraint instanceof ExistenceConstraint) {
									ExistenceConstraint existenceConstraint = (ExistenceConstraint) additionalConstraint;
									validateExistenceConstraint(iomObj, existenceConstraint);
								} else if(additionalConstraint instanceof MandatoryConstraint){
									MandatoryConstraint mandatoryConstraint = (MandatoryConstraint) additionalConstraint;
									validateMandatoryConstraint(iomObj, mandatoryConstraint);
								} else if(additionalConstraint instanceof SetConstraint){
									SetConstraint setConstraint = (SetConstraint) additionalConstraint;
									String constraintName = getScopedName(setConstraint);
									String checkAdditionalConstraint=null;
									if(!enforceConstraintValidation){
										checkAdditionalConstraint=validationConfig.getConfigValue(constraintName, ValidationConfig.CHECK);
									}
									if(ValidationConfig.OFF.equals(checkAdditionalConstraint)){
										if(!configOffOufputReduction.contains(ValidationConfig.CHECK+":"+constraintName)){
											configOffOufputReduction.add(ValidationConfig.CHECK+":"+constraintName);
											errs.addEvent(errFact.logInfoMsg("{0} not validated, validation configuration check=off", constraintName, iomObj.getobjectoid()));
										}
									} else {
										collectSetConstraintObjs(checkAdditionalConstraint, constraintName, iomObj, setConstraint);
									}
								} else if(additionalConstraint instanceof UniquenessConstraint){
									UniquenessConstraint uniquenessConstraint = (UniquenessConstraint) additionalConstraint; // uniquenessConstraint not null.
					                String iomObjOid=iomObj.getobjectoid();
					                if(iomObjOid==null && classOfCurrentObj instanceof AssociationDef) {
					                    iomObjOid=ObjectPool.getAssociationId(iomObj, (AssociationDef) classOfCurrentObj);
					                }
									validateUniquenessConstraint(iomObj, iomObjOid,uniquenessConstraint, null);
								} else if(additionalConstraint instanceof PlausibilityConstraint){
									PlausibilityConstraint plausibilityConstraint = (PlausibilityConstraint) additionalConstraint;
									String constraintName = getScopedName(plausibilityConstraint);
									String checkPlausibilityConstraint=null;
									if(!enforceConstraintValidation){
										checkPlausibilityConstraint=validationConfig.getConfigValue(constraintName, ValidationConfig.CHECK);
									}
									if(ValidationConfig.OFF.equals(checkPlausibilityConstraint)){
										if(!configOffOufputReduction.contains(ValidationConfig.CHECK+":"+constraintName)){
											configOffOufputReduction.add(ValidationConfig.CHECK+":"+constraintName);
											errs.addEvent(errFact.logInfoMsg("{0} not validated, validation configuration check=off", constraintName, iomObj.getobjectoid()));
										}
									} else {
										fillOfPlausibilityConstraintMap(checkPlausibilityConstraint, constraintName, plausibilityConstraint, iomObj);
									}
								}
							}
						}
					}
					// validate constraints
					validateConstraints(iomObj, classOfCurrentObj);
					Iterator<ViewableTransferElement> attrIterator=classOfCurrentObj.getAttributesAndRoles2();
					while (attrIterator.hasNext()) {
						ViewableTransferElement objA = attrIterator.next();
						if (objA.obj instanceof LocalAttribute){
							LocalAttribute attr = (LocalAttribute)objA.obj;
							String attrName=attr.getName();
							Type type = attr.getDomain();
							// composition
							if (type instanceof CompositionType){
								CompositionType compositionType = (CompositionType) type;
								Table structure = compositionType.getComponentType();
								// bid of iomObject
								String objectBid = objectPool.getBidOfObject(iomObj.getobjectoid(), classOfCurrentObj);
								int structc=iomObj.getattrvaluecount(attrName);
								 for(int structi=0;structi<structc;structi++){
									IomObject structValue=iomObj.getattrobj(attrName, structi);
									validateReferenceAttrs(attr.getScopedName(),structValue, structure, objectBid);
								}
							}
						}else if(objA.obj instanceof RoleDef){
							RoleDef roleDef = (RoleDef) objA.obj;
							validateRoleReference(roleDef, iomObj);
						}
					}
					if(classOfCurrentObj instanceof AbstractClassDef){
						AbstractClassDef abstractClassDef = (AbstractClassDef) classOfCurrentObj;
						Iterator<RoleDef> targetRoleIterator=abstractClassDef.getOpposideRoles();
						while(targetRoleIterator.hasNext()){
							RoleDef role=targetRoleIterator.next();
							validateRoleCardinality(role, iomObj);
						}
						targetRoleIterator=abstractClassDef.getOpposideForNonNavigableRoles();
						while(targetRoleIterator.hasNext()){
							RoleDef role=targetRoleIterator.next();
							validateRoleCardinality(role, iomObj);
						}
					}
				}
			}
		}
		for(SetConstraint setConstraint:setConstraints.keySet()){
			validateSetConstraint(setConstraint);
		}
	}
	private void validateConstraints(IomObject iomObj, Viewable classOfIomObj) {
		if(!ValidationConfig.OFF.equals(constraintValidation)){
			Viewable classOfCurrentObj=classOfIomObj;
			while(classOfCurrentObj!=null) {
				Iterator constraintIterator=classOfCurrentObj.iterator();
				while (constraintIterator.hasNext()) {
					Object constraintObj = constraintIterator.next();
					// existence constraint
					if(constraintObj instanceof ExistenceConstraint){
						ExistenceConstraint existenceConstraint=(ExistenceConstraint) constraintObj;
						validateExistenceConstraint(iomObj, existenceConstraint);
					}
					// mandatory constraint
					if(constraintObj instanceof MandatoryConstraint){
						MandatoryConstraint mandatoryConstraint=(MandatoryConstraint) constraintObj;
						validateMandatoryConstraint(iomObj, mandatoryConstraint);
					}
					// set constraint
					if(constraintObj instanceof SetConstraint){
						SetConstraint setConstraint=(SetConstraint) constraintObj;
						String constraintName = getScopedName(setConstraint);
						String checkSetConstraint=null;
						if(!enforceConstraintValidation){
							checkSetConstraint=validationConfig.getConfigValue(constraintName, ValidationConfig.CHECK);
						}
						if(ValidationConfig.OFF.equals(checkSetConstraint)){
							if(!configOffOufputReduction.contains(ValidationConfig.CHECK+":"+constraintName)){
								configOffOufputReduction.add(ValidationConfig.CHECK+":"+constraintName);
								errs.addEvent(errFact.logInfoMsg("{0} not validated, validation configuration check=off", constraintName, iomObj.getobjectoid()));
							}
						} else {
							collectSetConstraintObjs(checkSetConstraint, constraintName, iomObj, setConstraint);
						}
					}
					// plausibility constraint
					if(constraintObj instanceof PlausibilityConstraint){
						PlausibilityConstraint plausibilityConstraint=(PlausibilityConstraint) constraintObj;
						String constraintName = getScopedName(plausibilityConstraint);
						String checkPlausibilityConstraint=null;
						if(!enforceConstraintValidation){
							checkPlausibilityConstraint=validationConfig.getConfigValue(constraintName, ValidationConfig.CHECK);
						}
						if(ValidationConfig.OFF.equals(checkPlausibilityConstraint)){
							if(!configOffOufputReduction.contains(ValidationConfig.CHECK+":"+constraintName)){
								configOffOufputReduction.add(ValidationConfig.CHECK+":"+constraintName);
								errs.addEvent(errFact.logInfoMsg("{0} not validated, validation configuration check=off", constraintName, iomObj.getobjectoid()));
							}
						} else {
							fillOfPlausibilityConstraintMap(checkPlausibilityConstraint, constraintName, plausibilityConstraint, iomObj);
						}
					}
				}
				classOfCurrentObj=(Viewable) classOfCurrentObj.getExtending();
			}
			Iterator iter = classOfIomObj.getAttributesAndRoles2();
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
								Viewable structType=((CompositionType) type).getComponentType();
								 int structc=iomObj.getattrvaluecount(attrName);
								 for(int structi=0;structi<structc;structi++){
									 IomObject structEle=iomObj.getattrobj(attrName, structi);
									 if(structEle!=null) {
										 validateConstraints(structEle, structType);
									 }
								 }
							}
						}
					}
				}
			}
		}
	}

	private void fillOfPlausibilityConstraintMap(String validationKind, String usageScope, PlausibilityConstraint constraint, IomObject iomObj){
		Evaluable condition = (Evaluable) constraint.getCondition();
		Value conditionValue = evaluateExpression(validationKind, usageScope, iomObj, condition);
		if(plausibilityConstraints.containsKey(constraint)){
			PlausibilityPoolValue poolConstraintValues = plausibilityConstraints.get(constraint);
			double successfulResults = poolConstraintValues.getSuccessfulResults();
			double totalSumOfConstraints = poolConstraintValues.getTotalSumOfConstraints();
			if (conditionValue.isTrue()){
				plausibilityConstraints.remove(constraint);
				plausibilityConstraints.put(constraint, new PlausibilityPoolValue(successfulResults+1.0, totalSumOfConstraints+1.0));
			} else {
				// error, undefined, false
				plausibilityConstraints.remove(constraint);
				plausibilityConstraints.put(constraint, new PlausibilityPoolValue(successfulResults, totalSumOfConstraints+1.0));
			}
		} else {
			if (conditionValue.isTrue()){
				plausibilityConstraints.put(constraint, new PlausibilityPoolValue(1.0, 1.0));
			} else {
				// error, undefined, false
				plausibilityConstraints.put(constraint, new PlausibilityPoolValue(0.0, 1.0));
			}
		}
	}
	
	private void validatePlausibilityConstraints(){
		if(!ValidationConfig.OFF.equals(constraintValidation)){
			for (Entry<PlausibilityConstraint, PlausibilityPoolValue> constraintEntry : plausibilityConstraints.entrySet()){
				PlausibilityConstraint constraint = constraintEntry.getKey();
				String constraintName = getScopedName(constraintEntry.getKey());
				String checkConstraint=null;
				if(!enforceConstraintValidation){
					checkConstraint=validationConfig.getConfigValue(constraintName, ValidationConfig.CHECK);
				}
				if(ValidationConfig.OFF.equals(checkConstraint)){
					if(!configOffOufputReduction.contains(ValidationConfig.CHECK+":"+constraintName)){
						configOffOufputReduction.add(ValidationConfig.CHECK+":"+constraintName);
						errs.addEvent(errFact.logInfoMsg("{0} not validated, validation configuration check=off", constraintName));
					}
				}else{
					if(!constraintOutputReduction.contains(constraint+":"+constraintName)){
						constraintOutputReduction.add(constraint+":"+constraintName);
						errs.addEvent(errFact.logInfoMsg("validate plausibility constraint {0}...",getScopedName(constraintEntry.getKey())));
					}
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
	}
	
	private void validateUniquenessConstraint(IomObject iomObj, String iomObjOid,UniquenessConstraint uniquenessConstraint, RoleDef role) {
		if(!ValidationConfig.OFF.equals(constraintValidation)){
			String constraintName = getScopedName(uniquenessConstraint);
			String checkUniqueConstraint=null;
			if(!enforceConstraintValidation){
				checkUniqueConstraint=validationConfig.getConfigValue(constraintName, ValidationConfig.CHECK);
			}
			if(ValidationConfig.OFF.equals(checkUniqueConstraint)){
				if(!configOffOufputReduction.contains(ValidationConfig.CHECK+":"+constraintName)){
					configOffOufputReduction.add(ValidationConfig.CHECK+":"+constraintName);
					errs.addEvent(errFact.logInfoMsg("{0} not validated, validation configuration check=off", constraintName, iomObj.getobjectoid()));
				}
			}else{
				// preCondition (UNIQUE WHERE)
				if(uniquenessConstraint.getPreCondition()!=null){
					Value preConditionValue = evaluateExpression(checkUniqueConstraint, constraintName, iomObj, uniquenessConstraint.getPreCondition());
					if (preConditionValue.isNotYetImplemented()){
						errs.addEvent(errFact.logWarningMsg("Function {0} in uniqueness constraint is not yet implemented.", constraintName));
						return;
					}
					if (preConditionValue.skipEvaluation()){
						return;
					}
					if (!preConditionValue.isTrue()){
						// ignore this object in uniqueness constraint
						return;
					}
				}
				Object modelElement=tag2class.get(iomObj.getobjecttag());
				Viewable aClass1= (Viewable) modelElement;
				if(!loggedObjects.contains(uniquenessConstraint)){
					loggedObjects.add(uniquenessConstraint);
					errs.addEvent(errFact.logInfoMsg("validate unique constraint {0}...",getScopedName(uniquenessConstraint)));
				}
			    HashMap<UniquenessConstraint, HashMap<AttributeArray, String>> seenValues = null;
			    if(uniquenessConstraint.getLocal()) {
		            seenValues= new HashMap<UniquenessConstraint, HashMap<AttributeArray, String>>();
			    }else {
		            seenValues=seenUniqueConstraintValues;
			    }
		        if(uniquenessConstraint.getPrefix()!=null){
		            PathEl[] attrPath = uniquenessConstraint.getPrefix().getPathElements();
		            visitStructEle(checkUniqueConstraint,uniquenessConstraint,seenValues,iomObjOid,attrPath,0,iomObj, role);
		        }else {
	                visitStructEle(checkUniqueConstraint,uniquenessConstraint,seenValues,iomObjOid,null,0,iomObj, role);
		        }
			}
		}
	}
	
	private void visitStructEle(String checkUniqueConstraint,UniquenessConstraint uniquenessConstraint, HashMap<UniquenessConstraint, HashMap<AttributeArray, String>> seenValues, String iomObjOid, PathEl[] attrPath, int i, IomObject iomObj, RoleDef role) {
	    if(attrPath==null || i>=attrPath.length) {
	        OutParam<AttributeArray> values = new OutParam<AttributeArray>();
            String returnValue = validateUnique(seenValues,iomObjOid,iomObj,uniquenessConstraint, values, role);
            if (returnValue == null){
                // ok
            } else {
                String msg=validationConfig.getConfigValue(getScopedName(uniquenessConstraint), ValidationConfig.MSG);
                if(msg!=null && msg.length()>0){
                    logMsg(checkUniqueConstraint,msg);
                } else {
                    logMsg(checkUniqueConstraint,"Unique is violated! Values {0} already exist in Object: {1}", values.value.valuesAsString(), returnValue);
                }
            }
	        return;
	    }
        String attrName = attrPath[i].getName();
        int structElec=iomObj.getattrvaluecount(attrName);
        for(int structElei=0;structElei<structElec;structElei++) {
            IomObject structEle=iomObj.getattrobj(attrName, structElei);
            visitStructEle(checkUniqueConstraint,uniquenessConstraint, seenValues, iomObjOid, attrPath,i+1,structEle, role);
        }
    }
    private HashMap<SetConstraint,Collection<String>> setConstraints=new HashMap<SetConstraint,Collection<String>>();
	private Iterator<String> allObjIterator=null;
	
	private void collectSetConstraintObjs(String validationKind, String constraintName, IomObject iomObj, SetConstraint setConstraintObj) {
		Evaluable preCondition = (Evaluable) setConstraintObj.getPreCondition();
		if(preCondition != null){
			Value preConditionValue = evaluateExpression(validationKind, constraintName, iomObj, preCondition);
			if (preConditionValue.isNotYetImplemented()){
				errs.addEvent(errFact.logWarningMsg("Function in set constraint {0} is not yet implemented.", getScopedName(setConstraintObj)));
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
		if(!ValidationConfig.OFF.equals(constraintValidation)){
			Collection<String> objs=setConstraints.get(setConstraintObj);
			String constraintName = getScopedName(setConstraintObj);
			String checkConstraint = null;
			if(!enforceConstraintValidation){
				checkConstraint=validationConfig.getConfigValue(constraintName, ValidationConfig.CHECK);
			}
			if(ValidationConfig.OFF.equals(checkConstraint)){
				if(!configOffOufputReduction.contains(ValidationConfig.CHECK+":"+getScopedName(setConstraintObj))){
					configOffOufputReduction.add(ValidationConfig.CHECK+":"+getScopedName(setConstraintObj));
					errs.addEvent(errFact.logInfoMsg("{0} not validated, validation configuration check=off", getScopedName(setConstraintObj)));
				}
			}else{
				if(!constraintOutputReduction.contains(setConstraintObj+":"+constraintName)){
					constraintOutputReduction.add(setConstraintObj+":"+constraintName);
					errs.addEvent(errFact.logInfoMsg("validate set constraint {0}...",getScopedName(setConstraintObj)));
				}
				if(objs!=null && objs.size()>0){
					for(String oid:objs){
						allObjIterator=objs.iterator();
						IomObject iomObj=objectPool.getObject(oid, null, null);
						setCurrentMainObj(iomObj);
						errFact.setDefaultCoord(getDefaultCoord(iomObj));
						Evaluable condition = (Evaluable) setConstraintObj.getCondition();
						Value constraintValue = evaluateExpression(checkConstraint, constraintName, iomObj, condition);
						if (constraintValue.isNotYetImplemented()){
							errs.addEvent(errFact.logWarningMsg("Function in set constraint {0} is not yet implemented.", getScopedName(setConstraintObj)));
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
								if(!setConstraintOufputReduction.contains(setConstraintObj+":"+constraintName)){
									setConstraintOufputReduction.add(setConstraintObj+":"+constraintName);
									logMsg(checkConstraint,"Set Constraint {0} is not true.", constraintName);
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void validateMandatoryConstraint(IomObject iomObj, MandatoryConstraint mandatoryConstraintObj) {
		if(!ValidationConfig.OFF.equals(constraintValidation)){
			String constraintName = getScopedName(mandatoryConstraintObj);
			String checkConstraint = null;
			if(!enforceConstraintValidation){
				checkConstraint=validationConfig.getConfigValue(constraintName, ValidationConfig.CHECK);
			}
			if(ValidationConfig.OFF.equals(checkConstraint)){
				if(!configOffOufputReduction.contains(ValidationConfig.CHECK+":"+constraintName)){
					configOffOufputReduction.add(ValidationConfig.CHECK+":"+constraintName);
					errs.addEvent(errFact.logInfoMsg("{0} not validated, validation configuration check=off", constraintName));
				}
			} else {
				if(!constraintOutputReduction.contains(mandatoryConstraintObj+":"+constraintName)){
					constraintOutputReduction.add(mandatoryConstraintObj+":"+constraintName);
					errs.addEvent(errFact.logInfoMsg("validate mandatory constraint {0}...",getScopedName(mandatoryConstraintObj)));
				}
				Evaluable condition = (Evaluable) mandatoryConstraintObj.getCondition();
				Value conditionValue = evaluateExpression(checkConstraint, constraintName, iomObj, condition);
				if (!conditionValue.isNotYetImplemented()){
					if (!conditionValue.skipEvaluation()){
						if (conditionValue.isTrue()){
							// ok
						} else {
							String msg=validationConfig.getConfigValue(constraintName, ValidationConfig.MSG);
							if(msg!=null && msg.length()>0){
								logMsg(checkConstraint,msg);
							} else {
								logMsg(checkConstraint,"Mandatory Constraint {0} is not true.", constraintName);
							}
						}
					}
				} else {
					if(condition instanceof FunctionCall){
						FunctionCall functionCallObj = (FunctionCall) condition;
						Function function = functionCallObj.getFunction();
						errs.addEvent(errFact.logWarningMsg("Function {0} is not yet implemented.", function.getScopedName(null)));
						Value.createNotYetImplemented();
					} else {
						errs.addEvent(errFact.logWarningMsg("MandatoryConstraint {0} of {1} is not yet implemented.", mandatoryConstraintObj.getScopedName(null), iomObj.getobjecttag()));
						Value.createNotYetImplemented();
					}
				}
			}
		}
	}

	private Value evaluateExpression(String validationKind, String usageScope, IomObject iomObj, Evaluable expression) {
		TextType texttype = new TextType();
		if(expression instanceof Equality){
			// ==
			Equality equality = (Equality) expression;
			Evaluable leftExpression = (Evaluable) equality.getLeft();
			Evaluable rightExpression = (Evaluable) equality.getRight();
			Value leftValue=evaluateExpression(validationKind, usageScope, iomObj,leftExpression);
			// if isError, return error.
			if (leftValue.skipEvaluation()){
				return leftValue;
			}
			if (leftValue.isUndefined()){
				return Value.createSkipEvaluation();
			}
			
			Value rightValue=evaluateExpression(validationKind, usageScope, iomObj,rightExpression);
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
			Value leftValue=evaluateExpression(validationKind, usageScope, iomObj,leftExpression);
			// if isError, return error.
			if (leftValue.skipEvaluation()){
				return leftValue;
			}
			if (leftValue.isUndefined()){
				return Value.createSkipEvaluation();
			}
			Value rightValue=evaluateExpression(validationKind, usageScope, iomObj,rightExpression);
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
			Value leftValue=evaluateExpression(validationKind, usageScope, iomObj,leftExpression);
			// if isError, return error.
			if (leftValue.skipEvaluation()){
				return leftValue;
			}
			if (leftValue.isUndefined()){
				return Value.createSkipEvaluation();
			}
			Value rightValue=evaluateExpression(validationKind, usageScope, iomObj,rightExpression);
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
			Value leftValue=evaluateExpression(validationKind, usageScope, iomObj,leftExpression);
			// if isError, return error.
			if (leftValue.skipEvaluation()){
				return leftValue;
			}
			if (leftValue.isUndefined()){
				return Value.createSkipEvaluation();
			}
			Value rightValue=evaluateExpression(validationKind, usageScope, iomObj,rightExpression);
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
			Value leftValue=evaluateExpression(validationKind, usageScope, iomObj,leftExpression);
			// if isError, return error.
			if (leftValue.skipEvaluation()){
				return leftValue;
			}
			if (leftValue.isUndefined()){
				return Value.createSkipEvaluation();
			}
			Value rightValue=evaluateExpression(validationKind, usageScope, iomObj,rightExpression);
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
			Value leftValue=evaluateExpression(validationKind, usageScope, iomObj,leftExpression);
			// if isError, return error.
			if (leftValue.skipEvaluation()){
				return leftValue;
			}
			if (leftValue.isUndefined()){
				return Value.createSkipEvaluation();
			}
			Value rightValue=evaluateExpression(validationKind, usageScope, iomObj,rightExpression);
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
			Value arg=evaluateExpression(validationKind, usageScope, iomObj,negation.getNegated());
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
				Value arg=evaluateExpression(validationKind, usageScope, iomObj,conjunctionArray[i]);
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
			Value arg=evaluateExpression(validationKind, usageScope, iomObj,defined.getArgument());
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
				Value arg=evaluateExpression(validationKind, usageScope, iomObj,disjunctionArray[i]);
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
					return new Value(texttype, numericConstant.getValue().toString());
				}
			} else if (constantObj instanceof Constant.Class){
				Constant.Class classConstant = (Constant.Class) constantObj;
				if(classConstant!=null){
					if(classConstant.getValue() instanceof Viewable){
						Viewable classValue = (Viewable) classConstant.getValue();
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
			Function currentFunction = functionCallObj.getFunction();
			if(currentFunction.getScopedName(null).equals("INTERLIS.len") || currentFunction.getScopedName(null).equals("INTERLIS.lenM")){
				Evaluable[] arguments = functionCallObj.getArguments();
				for(Evaluable anArgument : arguments){
					Value arg=evaluateExpression(validationKind, usageScope, iomObj,anArgument);
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
			} else if(currentFunction.getScopedName(null).equals("INTERLIS.trim") || currentFunction.getScopedName(null).equals("INTERLIS.trimM")){
				Evaluable[] arguments = functionCallObj.getArguments();
				for(Evaluable anArgument : arguments){
					Value arg=evaluateExpression(validationKind, usageScope, iomObj,anArgument);
					if (arg.skipEvaluation()){
						return arg;
					}
					if (arg.isUndefined()){
						return Value.createSkipEvaluation();
				}
					return new Value(texttype, arg.getValue().trim());
				}
			} else if (currentFunction.getScopedName(null).equals("INTERLIS.isEnumSubVal")){
				Evaluable[] arguments = functionCallObj.getArguments();
				Value subEnum=evaluateExpression(validationKind, usageScope, iomObj,arguments[0]);
				if (subEnum.skipEvaluation()){
					return subEnum;
				}
				if (subEnum.isUndefined()){
					return Value.createSkipEvaluation();
				}
				Value nodeEnum=evaluateExpression(validationKind, usageScope, iomObj,arguments[1]);
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
			} else if (currentFunction.getScopedName(null).equals("INTERLIS.inEnumRange")){
				Evaluable[] arguments = functionCallObj.getArguments();
				Value enumToCompare=evaluateExpression(validationKind, usageScope, iomObj,arguments[0]);
				if (enumToCompare.skipEvaluation()){
					return enumToCompare;
				}
				if (enumToCompare.isUndefined()){
					return Value.createSkipEvaluation();
				}
				Value minEnum=evaluateExpression(validationKind, usageScope, iomObj,arguments[1]);
				if (minEnum.skipEvaluation()){
					return minEnum;
				}
				if (minEnum.isUndefined()){
					return Value.createSkipEvaluation();
				}
				Value maxEnum=evaluateExpression(validationKind, usageScope, iomObj,arguments[2]);
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
			} else if (currentFunction.getScopedName(null).equals("INTERLIS.objectCount")){
				FunctionCall functionCall = (FunctionCall) expression;
				Evaluable[] arguments = functionCall.getArguments();
				Value value=evaluateExpression(validationKind, usageScope, iomObj,arguments[0]);
				if (value.skipEvaluation()){
					return value;
				}
				if (value.isUndefined()){
					return Value.createSkipEvaluation();
				}
				if(value.getComplexObjects()!=null){
					return new Value(value.getComplexObjects().size());
				}else if(value.getViewable()!=null) {
					for(Function aFunction:functions.keySet()) {
						// contains/equal would not work here, because it is an object compare.
						if(aFunction==currentFunction) {
							Value objCount=functions.get(currentFunction);
							return objCount;
						}
					}
					Value objectCount=null;
					objectCount = evaluateObjectCount(value);
					// put the result of object count as value to the current function.
					functions.put(currentFunction, objectCount);
					return objectCount;
				}
			} else if (currentFunction.getScopedName(null).equals("INTERLIS.elementCount")){
				FunctionCall functionCall = (FunctionCall) expression;
				Evaluable[] arguments = (Evaluable[]) functionCall.getArguments();
				Evaluable anArgument = (Evaluable) arguments[0];
				Value value=evaluateExpression(validationKind, usageScope, iomObj, anArgument);
				if (value.skipEvaluation()){
					return value;
				}
				if (value.isUndefined()){
					return Value.createSkipEvaluation();
				}
				return new Value(value.getComplexObjects().size());
			} else if (currentFunction.getScopedName(null).equals("INTERLIS.isOfClass")){
				FunctionCall functionCall = (FunctionCall) expression;
				Evaluable[] arguments = functionCall.getArguments();
				Value paramObject=evaluateExpression(validationKind, usageScope, iomObj,arguments[0]);
				if (paramObject.skipEvaluation()){
					return paramObject;
				}
				if (paramObject.isUndefined()){
					return Value.createSkipEvaluation();
				}
				Value paramClass=evaluateExpression(validationKind, usageScope, iomObj,arguments[1]);
				if (paramClass.skipEvaluation()){
					return paramClass;
				}
				if (paramClass.isUndefined()){
					return Value.createSkipEvaluation();
				}
				Viewable paramObjectViewable = paramObject.getViewable();
				if (paramObject.getComplexObjects() != null) {
				    paramObjectViewable=(Viewable)td.getElement(paramObject.getComplexObjects().iterator().next().getobjecttag());				    
				}
				if(paramObjectViewable.equals(paramClass.getViewable())){
					return new Value(true);
				}
				if(paramObjectViewable.isExtending(paramClass.getViewable())){
					return new Value(true);					
				}
				return new Value(false);
			} else if (currentFunction.getScopedName(null).equals("INTERLIS.isSubClass")){
				FunctionCall functionCall = (FunctionCall) expression;
				Evaluable[] arguments = functionCall.getArguments();
				Value subViewable=evaluateExpression(validationKind, usageScope, iomObj,arguments[0]);
				if (subViewable.skipEvaluation()){
					return subViewable;
				}
				if (subViewable.isUndefined()){
					return Value.createSkipEvaluation();
				}
				Value superViewable=evaluateExpression(validationKind, usageScope, iomObj,arguments[1]);
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
			} else if (currentFunction.getScopedName(null).equals("INTERLIS.myClass")){
				FunctionCall functionCall = (FunctionCall) expression;
				Evaluable[] arguments = functionCall.getArguments();
				Value targetViewable=evaluateExpression(validationKind, usageScope, iomObj,arguments[0]);
				if (targetViewable.skipEvaluation()){
					return targetViewable;
				}
				if (targetViewable.isUndefined()){
					return Value.createSkipEvaluation();
				}
				return new Value((Viewable)td.getElement(targetViewable.getComplexObjects().iterator().next().getobjecttag()));
			} else if (currentFunction.getScopedName(null).equals("INTERLIS.areAreas")){
				FunctionCall functionCall = (FunctionCall) expression;
				Evaluable[] arguments = functionCall.getArguments();
				// founded objects (list<IomObjects)
				Value value=evaluateExpression(validationKind, usageScope, iomObj,arguments[0]);
				if (value.skipEvaluation()){
					return value;
				}
				if (value.isUndefined()){
					return Value.createSkipEvaluation();
				}
				// count of objects condition returns attrName of BAG / undefined=(numericIsDefined=false)
				Value surfaceBag=evaluateExpression(validationKind, usageScope, iomObj,arguments[1]);
				if (surfaceBag.skipEvaluation()){
					return surfaceBag;
				}
				// name of surface (textType)
				Value surfaceAttr=evaluateExpression(validationKind, usageScope, iomObj,arguments[2]);
				if (surfaceAttr.skipEvaluation()){
					return surfaceAttr;
				}
				if (surfaceAttr.isUndefined()){
					return Value.createSkipEvaluation();
				}
				for(Function aFunction:functions.keySet()) {
					if(aFunction==currentFunction) {
						Value isArea=functions.get(currentFunction);
						return isArea;
					}
				}
				Value isArea = evaluateAreArea(iomObj, value, surfaceBag, surfaceAttr);
				functions.put(currentFunction, isArea);
				return isArea;
			} else {
				String functionQname=currentFunction.getScopedName(null);
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
					Value anObject=evaluateExpression(validationKind, usageScope, iomObj,arguments[i]);
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
				return functionTarget.evaluate(validationKind, usageScope, iomObj, actualArguments);
			}
			//TODO INTERLIS.convertUnit
			//TODO instance of InspectionFactor
			//TODO instance of LengthOfReferencedText
		} else if(expression instanceof ObjectPath) {
			// object path	
			ObjectPath objectPathObj = (ObjectPath) expression;
			PathEl[] pathElements = objectPathObj.getPathElements();
			final int lastPathIndex = pathElements.length - 1;

			return getValueFromObjectPath(iomObj, pathElements, lastPathIndex,null);
		} else if(expression instanceof Objects) {
			// objects
			Iterator<String> objectIterator = allObjIterator;
			List<IomObject> listOfIomObjects = new ArrayList<IomObject>();
			if(allObjIterator==null){
				 throw new IllegalStateException("argument ALL requires a SET CONSTRAINT.");
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
			errs.addEvent(errFact.logWarningMsg("expression {0} is not yet implemented.", expression.toString()));
		}
		return Value.createSkipEvaluation(); // skip further evaluation
		//TODO instance of ParameterValue
		//TODO instance of ViewableAggregate
		//TODO instance of ViewableAlias
	}
    private Value getValueFromObjectPath(IomObject iomObjStart, PathEl[] pathElements, final int lastPathIndex,RoleDef firstRole) {
        ArrayList<IomObject> currentObjects=new ArrayList<IomObject>();
        ArrayList<IomObject> nextCurrentObjects=new ArrayList<IomObject>();
        RoleDef role = null;
        currentObjects.add(iomObjStart);
        for (int k = 0; k < pathElements.length; k++) {
            for (IomObject iomObj:currentObjects) {
                PathEl currentPathEl = pathElements[k];
                if (currentPathEl instanceof PathElAbstractClassRole || currentPathEl instanceof PathElAssocRole || currentPathEl instanceof AssociationPath) {
                    if (currentPathEl instanceof PathElAbstractClassRole) {
                        PathElAbstractClassRole abstractClassRole = (PathElAbstractClassRole) currentPathEl;
                        role = (RoleDef) abstractClassRole.getRole();
                    } else if (currentPathEl instanceof PathElAssocRole) {
                        role = ((PathElAssocRole) currentPathEl).getRole();
                    } else if (currentPathEl instanceof AssociationPath) {
                        role = ((AssociationPath) currentPathEl).getTargetRole();
                    }
                    
                    if (role == null) {
                        throw new IllegalStateException("Role is not be empty!");
                    } else {
                        if(k==0 && role==firstRole) {
                            // special case for embedded association
                            if (k != lastPathIndex) {
                                ; // the current object is already the first path element
                            }else {
                                List<IomObject> objects = new ArrayList<IomObject>();
                                objects.add(iomObj);
                                nextCurrentObjects.addAll(objects);
                            }
                        }else {
                            String targetOid = null;
                            IomObject roleDefValue = iomObj.getattrobj(role.getName(), 0);
                            if (roleDefValue != null) {
                                targetOid = roleDefValue.getobjectrefoid();
                            }
                            Viewable srcObjClass=(Viewable) tag2class.get(iomObj.getobjecttag());
                            if (isBackward(srcObjClass,role)) {
                                //has a linkObj?
                                List<IomObject> objects=null;
                                if (((AssociationDef) role.getContainer()).isLightweight()) {
                                    objects = getTargetObjectsOfReverseRole(role, iomObj.getobjectoid());
                                    if (objects != null) {
                                        nextCurrentObjects.addAll(objects);
                                    } 
                                    continue;
                                    
                                }else {
                                    objects = getLinkObjects(role, iomObj.getobjectoid());
                                    if (objects != null) {
                                        if(currentPathEl instanceof PathElAssocRole || currentPathEl instanceof PathElAbstractClassRole) {
                                            for (IomObject obj : objects) {
                                                IomObject attrobj = obj.getattrobj(role.getName(), 0);
                                                IomObject targetObj = getReferencedObject(role, attrobj.getobjectrefoid());
                                                if (targetObj != null) {
                                                    List<IomObject> objct = new ArrayList<IomObject>();
                                                    objct.add(targetObj);
                                                    if (k != lastPathIndex) {
                                                        nextCurrentObjects.add(targetObj);
                                                    } else {
                                                        nextCurrentObjects.addAll(objct);
                                                    }                                                    
                                                }
                                            }
                                      } else if (currentPathEl instanceof AssociationPath) {
                                          nextCurrentObjects.addAll(objects);
                                      }
                                    } 
                                    continue;
                                }
                            }
                            if (k != lastPathIndex) {
                                IomObject targetObj = getReferencedObject(role, targetOid);
                                if (targetObj != null) {
                                    nextCurrentObjects.add(targetObj);
                                }
                            }else {
                                List<IomObject> objects = new ArrayList<IomObject>();
                                if (roleDefValue != null) {
                                    objects.add(roleDefValue);
                                    nextCurrentObjects.addAll(objects);                                    
                                }

                            }
                        }
                    }
                } else if (currentPathEl instanceof StructAttributeRef) {
                    StructAttributeRef structAttributeRefValue = (StructAttributeRef) currentPathEl;
                    if(structAttributeRefValue.getAttr() instanceof LocalAttribute) {
                        LocalAttribute localAttributeValue = (LocalAttribute) structAttributeRefValue.getAttr();
                        if (!(localAttributeValue.getDomain() instanceof CompositionType)) {
                            throw new IllegalStateException();
                        } 
                        
                        CompositionType type = (CompositionType) localAttributeValue.getDomain();
                        String currentAttrName = localAttributeValue.getName();
                        if (iomObj.getattrvaluecount(currentAttrName) != 0) {
                            IomObject targetObj = iomObj = getIomObjWithIndex(iomObj, structAttributeRefValue, currentAttrName);
                            if (k!=lastPathIndex) {
                                nextCurrentObjects.add(targetObj);
                            }else {
                                String attrValue = iomObj.getattrvalue(currentAttrName);
                                if (attrValue != null) {
                                    if (attrValue.equals("true")) {
                                        return new Value(true);
                                    } else if (attrValue.equals("false")) {
                                        return new Value(false);
                                    } else {
                                        return new Value(type, attrValue);
                                    }
                                } else {
                                    List<IomObject> objects = new ArrayList<IomObject>();
                                    objects.add(targetObj);
                                    nextCurrentObjects.addAll(objects);
                                }
                            }                           
                        }
                    }
                } else if (currentPathEl instanceof AttributeRef) {
                    AttributeRef attrRef = (AttributeRef) currentPathEl;
                    Type type = attrRef.getAttr().getDomain();
                    String currentAttrName = currentPathEl.getName();
                    if (iomObj.getattrvaluecount(currentAttrName) == 0) {
                        return Value.createUndefined(); 
                    } else {
                        // not the last pathEl?
                        if (k!=lastPathIndex) {
                            iomObj = iomObj.getattrobj(currentAttrName,0); 
                            nextCurrentObjects.add(iomObj);
                        }else {
                            String attrValue = iomObj.getattrvalue(currentAttrName);
                            if (attrValue != null) {
                                if (attrValue.equals("true")) {
                                    return new Value(true);
                                } else if (attrValue.equals("false")) {
                                    return new Value(false);
                                // if null, then complex value.
                                } else {
                                    if (type instanceof TypeAlias) {
                                        Type aliasedType = ((TypeAlias) type).getAliasing().getType();
                                        if (aliasedType instanceof EnumerationType) {
                                            String refTypeName = ((TypeAlias) type)
                                                    .getAliasing().getName();
                                            return new Value(aliasedType, attrValue,
                                                    refTypeName);
                                        }
                                        return new Value(aliasedType, attrValue);
                                    }
                                    if (type instanceof EnumerationType) {
                                        return new Value(type, attrValue);
                                    }
                                }
                                return new Value(type, attrValue);
                            } else {
                                List<IomObject> objects = new ArrayList<IomObject>();
                                int attrValueCount = iomObj.getattrvaluecount(currentAttrName);
                                // iterate, because it's a list of attrObjects.
                                for (int i = 0; i < attrValueCount; i++) {
                                    objects.add(iomObj.getattrobj(currentAttrName, i));
                                }
                                nextCurrentObjects.addAll(objects);
                            }
                        }
                    }
                } else if (currentPathEl instanceof PathElRefAttr) {
                    PathElRefAttr pathElRefAttr = (PathElRefAttr) currentPathEl;
                    
                    if (!(pathElRefAttr.getAttr() instanceof LocalAttribute)) {
                        throw new IllegalStateException();
                    }
                    
                    LocalAttribute localAttributeValue = (LocalAttribute) pathElRefAttr.getAttr();
                    if (!(localAttributeValue.getDomain() instanceof ReferenceType)) {
                        throw new IllegalStateException();
                    }
                    
                    ReferenceType referenceType = (ReferenceType) localAttributeValue.getDomain();
                    
                    String targetOid=null;
                    IomObject tmpIomObject = iomObj.getattrobj(currentPathEl.getName(), 0);
                    if (tmpIomObject != null) {
                        targetOid = tmpIomObject.getobjectrefoid();
                    }

                    if (targetOid != null) {
                        IomObject targetObj = getIomObjectFromObjectPool(referenceType, targetOid);
                        if (targetObj != null) {
                            if (k != lastPathIndex) {
                                nextCurrentObjects.add(targetObj);
                            }else {
                                List<IomObject> objects = new ArrayList<IomObject>();
                                objects.add(targetObj);
                                nextCurrentObjects.addAll(objects);
                            }                        
                        }                        
                    }
                }
                
            }
            if (nextCurrentObjects.isEmpty()) {
                return Value.createUndefined();
            }
            if (k != lastPathIndex) {
                currentObjects = nextCurrentObjects;
                nextCurrentObjects = new ArrayList<IomObject>();
            } else {
                return new Value(nextCurrentObjects);
            }
 
        }
        return Value.createUndefined();
    }
    private boolean isBackward(Viewable srcObjClass, RoleDef role) {
        AssociationDef assoc=(AssociationDef) role.getContainer();
        if(assoc.isLightweight()) {
            // if reference is embedded in srcObj then is it a forward
            if (isRoleEmbedded(role) && role.getOppEnd().getDestination()==srcObjClass) {
                return true;
            } else {
                return false;
            }
        }else {
            // assoc has a link obj, is srcObj the link obj
            if(srcObjClass==assoc) {
                // role is forward; srcObj contains a reference
                return false;
            }
            // role is backward; srcObj doesn't contain a reference
            return true;
        }
    }
    private boolean isRoleEmbedded(RoleDef role) {
        Viewable destination = role.getDestination();
        Iterator iterator = destination.getAttributesAndRoles2();
        while (iterator.hasNext()) {
            ViewableTransferElement obj = (ViewableTransferElement)iterator.next();
            if (obj.obj instanceof RoleDef) {
                RoleDef objRole = ((RoleDef) obj.obj).getOppEnd();
                if (objRole.equals(role)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }
    private Map<RoleDef,Map<String,List<IomObject>>> targetObjects=new HashMap<RoleDef,Map<String,List<IomObject>>>();
    private List<IomObject> getTargetObjectsOfReverseRole(RoleDef role, String srcObjOid) {
        if(!targetObjects.containsKey(role)) {
            buildTargetObjectsMap(role);
        }
        return targetObjects.get(role).get(srcObjOid);
    }
    private void buildTargetObjectsMap(RoleDef role) {     
        Map<String, List<IomObject>> values = new HashMap<String, List<IomObject>>();
        targetObjects.put(role, values);
        for (String basketId : objectPool.getBasketIds()) {
            Iterator<IomObject> objectIterator = (objectPool.getObjectsOfBasketId(basketId)).valueIterator();
            while (objectIterator.hasNext()) { 
                IomObject targetObj = objectIterator.next();
                if (((Viewable)tag2class.get(targetObj.getobjecttag())).isExtending(role.getDestination())) {
                    IomObject refStruct = targetObj.getattrobj(role.getOppEnd().getName(), 0);
                    if (refStruct!=null) {
                        List<IomObject> objects = values.get(refStruct.getobjectrefoid());
                        if (objects == null) {
                            objects = new ArrayList<IomObject>();
                            values.put(refStruct.getobjectrefoid(), objects);                            
                        }
                        objects.add(targetObj);
                    }
                }
            }
        }
    }
    private IomObject getReferencedObject(RoleDef role, String oid) {
        Iterator<AbstractClassDef> targetClassIterator = role.iteratorDestination();
        ArrayList<Viewable> destinationClasses = new ArrayList<Viewable>();
        // find target classes.
        while(targetClassIterator.hasNext()) {
            Viewable roleDestinationClass = (Viewable) targetClassIterator.next();
            destinationClasses.add(roleDestinationClass);
        }
        OutParam<String> bidOfTargetObj = new OutParam<String>();
        IomObject targetObj = (IomObject) objectPool.getObject(oid, destinationClasses, bidOfTargetObj);
        return targetObj;
    }

    private Map<RoleDef,Map<String,List<IomObject>>> linkObjects=new HashMap<RoleDef,Map<String,List<IomObject>>>();
    private List<IomObject> getLinkObjects(RoleDef role, String srcObjOid) {        
        if(!linkObjects.containsKey(role)) {
            buildLinkObjMap(role);
        }
        return linkObjects.get(role).get(srcObjOid);
    }
    private void buildLinkObjMap(RoleDef role) {     
        Map<String, List<IomObject>> values = new HashMap<String, List<IomObject>>();
        linkObjects.put(role, values);
        for (String basketId : objectPool.getBasketIds()) {
            Iterator<IomObject> objectIterator = (objectPool.getObjectsOfBasketId(basketId)).valueIterator();
            while (objectIterator.hasNext()) { 
                IomObject linkObj = objectIterator.next();
                if (((Viewable)tag2class.get(linkObj.getobjecttag())).isExtending(role.getContainer())) {
                    IomObject refStruct = linkObj.getattrobj(role.getOppEnd().getName(), 0);
                    if (refStruct!=null) {
                        List<IomObject> objects = values.get(refStruct.getobjectrefoid());
                        if (objects == null) {
                            objects = new ArrayList<IomObject>();
                            values.put(refStruct.getobjectrefoid(), objects);                            
                        }
                        objects.add(linkObj);
                    }
                }
            }
        }
    }
    
    private IomObject getIomObjWithIndex(IomObject iomObj, StructAttributeRef structAttributeRefValue, String currentAttrName) {
        int expectedIndex = (int) (long) structAttributeRefValue.getIndex();
        int attrValueCount = iomObj.getattrvaluecount(currentAttrName);
        if (structAttributeRefValue.getIndex() == structAttributeRefValue.eFIRST) {
            iomObj = iomObj.getattrobj(currentAttrName, 0);
        } else if (structAttributeRefValue.getIndex() == structAttributeRefValue.eLAST) {
            iomObj = iomObj.getattrobj(currentAttrName, attrValueCount - 1);
        } else {
            if (expectedIndex <= attrValueCount && expectedIndex > 0) {
                iomObj = iomObj.getattrobj(currentAttrName, expectedIndex - 1);
                if (iomObj == null) {
                    throw new IllegalStateException("There is no record found for this index!");
                }
            } else {
                throw new IllegalStateException("There is no record found for this index!");
            }
        }
        return iomObj;
    }
    private IomObject getIomObjectFromObjectPool(ReferenceType referenceType, String targetOid) {
        OutParam<String> bidOfTargetObj = new OutParam<String>();
        ArrayList<Viewable> destinationClasses=new ArrayList<Viewable>();
        destinationClasses.add(referenceType.getReferred());
        return (IomObject) objectPool.getObject(targetOid, destinationClasses, bidOfTargetObj);
    }
    private Value evaluateObjectCount(Value value) {
		Iterator objectIterator = objectPool.getObjectsOfBasketId(currentBasketId).valueIterator();
		int counter = 0;
		while(objectIterator.hasNext()){
			IomObject anObj = (IomObject) objectIterator.next();
			if(anObj!=null){
				Object modelElement=tag2class.get(anObj.getobjecttag());
				Viewable viewable = (Viewable) modelElement;
				if(value.getViewable().equals(viewable)){
					counter+=1;
				}
			}
		}
		return new Value(counter);
	}
	
	private Value evaluateAreArea(IomObject iomObj, Value value, Value surfaceBag, Value surfaceAttr) {
		// if surfaceBag is undefined
		String objTag=null;
		if(iomObj!=null){
			objTag=iomObj.getobjecttag();
		}
		Object aModelele=tag2class.get(objTag);
		Viewable eleClass=(Viewable) aModelele;
		String iliClassQName=getScopedName(eleClass);
		if(surfaceBag.isUndefined()){
			ItfAreaPolygon2Linetable polygonPool = new ItfAreaPolygon2Linetable(iliClassQName, objPoolManager); // create new pool of polygons
			if(value.getViewable()!=null){
				Iterator objectIterator = objectPool.getObjectsOfBasketId(currentBasketId).valueIterator();
				while(objectIterator.hasNext()){
					IomObject aIomObj = (IomObject) objectIterator.next();
					if(aIomObj!=null){
						Object modelElement=tag2class.get(aIomObj.getobjecttag());
						Viewable anObjectClass = (Viewable) modelElement;
						if(value.getViewable().equals(anObjectClass)){
							IomObject polygon = aIomObj.getattrobj(surfaceAttr.getValue(), 0); // get polygon of current object
							if(polygon!=null){ // if value of Argument[2] equals object attribute
								try { // add polylines to polygonPool.
									polygonPool.addLines(aIomObj.getobjectoid(), null, polygonPool.getLinesFromPolygon(polygon));
								} catch (IoxException e) {
									throw new IllegalStateException(e);
								}
							}
						}
					}
				}
				// if objects.equals(anObjectClass) never equal, handling.
			} else {
				Iterator iterIomObjects = value.getComplexObjects().iterator(); // iterate through all objects of Argument[0] 
				while(iterIomObjects.hasNext()){
					IomObject anObject = (IomObject) iterIomObjects.next();
					IomObject polygon = anObject.getattrobj(surfaceAttr.getValue(), 0); // get polygon of current object
					if(polygon!=null){ // if value of Argument[2] equals object attribute
						try { // add polylines to polygonPool.
							polygonPool.addLines(anObject.getobjectoid(), null, polygonPool.getLinesFromPolygon(polygon));
						} catch (IoxException e) {
							throw new IllegalStateException(e);
						}
					}
				}
			}
			List<IoxInvalidDataException> intersections=polygonPool.validate();
			if(intersections!=null){
				return new Value(false); // not a valid area topology
			}
			return new Value(true); // valid areas
		} else {
			// if surfaceBag is defined
			ItfAreaPolygon2Linetable polygonPool = new ItfAreaPolygon2Linetable(iliClassQName, objPoolManager);
			if(value.getViewable()!=null){
				Iterator objectIterator = objectPool.getObjectsOfBasketId(currentBasketId).valueIterator();
				while(objectIterator.hasNext()){
					IomObject aIomObj = (IomObject) objectIterator.next();
					if(aIomObj!=null){
						Object modelElement=tag2class.get(aIomObj.getobjecttag());
						Viewable anObjectClass = (Viewable) modelElement;
						if(value.getViewable().equals(anObjectClass)){
							int countOfSurfaceBagValues = aIomObj.getattrvaluecount(surfaceBag.getValue());
							for(int i=0; i<countOfSurfaceBagValues; i++){
								IomObject surfaceBagObj = aIomObj.getattrobj(surfaceBag.getValue(), i);
								IomObject polygon = surfaceBagObj.getattrobj(surfaceAttr.getValue(), 0);
								if(polygon!=null){ // if value of Argument[2] equals object attribute
									try { // add polylines to polygonPool.
										polygonPool.addLines(aIomObj.getobjectoid(), null, polygonPool.getLinesFromPolygon(polygon));
									} catch (IoxException e) {
										throw new IllegalStateException(e);
									}
								}
							}
						}
					}
				}
			} else {
				Iterator iterIomObjects = value.getComplexObjects().iterator();
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
								throw new IllegalStateException(e);
							}
						} else {
							// there is no area to compare. --> area not false and not true.
						}
					}
				}
			}
			List<IoxInvalidDataException> intersections=polygonPool.validate();
			if(intersections!=null) {
				return new Value(false);
			}
			return new Value(true);
		}
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
		 if(multiplicity==null){
			 multiplicity=globalMultiplicity;
		 }
		if(multiplicity != null && ValidationConfig.OFF.equals(multiplicity)){
			if(!configOffOufputReduction.contains(ValidationConfig.MULTIPLICITY+":"+roleQName)){
				configOffOufputReduction.add(ValidationConfig.MULTIPLICITY+":"+roleQName);
				errs.addEvent(errFact.logInfoMsg("{0} not validated, validation configuration multiplicity=off", roleQName));
			}
		}else{
			if(!configOffOufputReduction.contains(ValidationConfig.MULTIPLICITY+":"+roleQName)){
				configOffOufputReduction.add(ValidationConfig.MULTIPLICITY+":"+roleQName);
				errs.addEvent(errFact.logInfoMsg("validate multiplicity of role {0}...",roleQName));
			}
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

	private void validateRoleReference(RoleDef role, IomObject iomObj){
		Object modelElement=tag2class.get(iomObj.getobjecttag());
		Viewable classObj = (Viewable) modelElement;
		String roleQName = getScopedName(role);
		String validateTarget=null;
		if(!enforceTargetValidation){
			validateTarget=validationConfig.getConfigValue(roleQName, ValidationConfig.TARGET);
		}
		if(ValidationConfig.OFF.equals(validateTarget)){
			if(!configOffOufputReduction.contains(ValidationConfig.TARGET+":"+roleQName)){
				configOffOufputReduction.add(ValidationConfig.TARGET+":"+roleQName);
				errs.addEvent(errFact.logInfoMsg("{0} not validated, validation configuration target=off", roleQName));
			}
		}else{
			if(!datatypesOutputReduction.contains(roleQName)){
				datatypesOutputReduction.add(roleQName);
				errs.addEvent(errFact.logInfoMsg("validate target of role {0}...",roleQName));
			}
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
			// find target classes.
			while(targetClassIterator.hasNext()){
				Viewable roleDestinationClass = (Viewable) targetClassIterator.next();
				destinationClasses.add(roleDestinationClass);
				possibleTargetClasses.append(sep);
				sep=",";
				possibleTargetClasses.append(roleDestinationClass.getScopedName(null));
			}
			OutParam<String> bidOfTargetObj = new OutParam<String>();
			IomObject targetObj = (IomObject) objectPool.getObject(targetOid, destinationClasses, bidOfTargetObj);
			if(targetObj == null) {
		        String bid = validationConfig.getConfigValue(roleQName, ValidationConfig.REQUIRED_IN);
		        if (bid != null) {
		            targetObj = findExternalObject(bid, targetOid, destinationClasses, bidOfTargetObj);
		        }
			    
			}
			String oid = iomObj.getobjectoid();
			if(oid==null){
				oid=ObjectPool.getAssociationId(iomObj, (AssociationDef) modelElement);
			}
			String bidOfObj = objectPool.getBidOfObject(oid, classObj);
			if(!role.isExternal()){
				if(targetOid!=null){
					if(isBasketSame(bidOfObj, targetObj)){
						if(targetObj!=null){
							// target object exists.
						} else {
							// no object with this oid found
							logMsg(validateTarget,"No object found with OID {0}.", targetOid);
							return;
						}
					} else {
						// no object with this oid found
						logMsg(validateTarget,"No object found with OID {0} in basket {1}.", targetOid,bidOfObj);
						return;
					}
				}
			}else{
				// EXTERNAL
				// not found in internal pool?
				if(targetObj==null){
					boolean extObjFound=false;
					// use external object resolver to find external objects.
					if(extObjResolvers!=null){
						// call custom function to verify in external data pools
						for(ExternalObjectResolver extObjResolver:extObjResolvers){
							if(extObjResolver.objectExists(targetOid, destinationClasses)){
								extObjFound=true;
								break;
							}
						}
					}
					if(allObjectsAccessible && !extObjFound){
						logMsg(validateTarget,"No object found with OID {0}.", targetOid);
					}
				}
			}
			if(targetObj != null){
				Object modelEle=tag2class.get(targetObj.getobjecttag());
				Viewable targetObjClass = (Viewable) modelEle;
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
				//wrong class of target object (typ false)
				logMsg(validateTarget,"wrong class {0} of target object {1} for role {2}.", getScopedName(targetObjClass),targetOid, getScopedName(role));
				return;
			}
		}
	}
	
	private IomObject findExternalObject(String targetBid,String targetOid, ArrayList<Viewable> destinationClasses, OutParam<String> bidOfTargetObj) {
	        if(!objectPool.getBasketIds().contains(targetBid)) {
	            List<Dataset> dataset = null;
	            File[] datasetFiles = null;
	            IoxReader reader=null;
	            try {
	                dataset = repositoryManager.getDatasetIndex(targetBid, null);
	                if (dataset!=null && !dataset.isEmpty()) {
	                    datasetFiles = repositoryManager.getLocalFileOfRemoteDataset(dataset.get(0), TransferDescription.MIMETYPE_XTF);                            
	                }
	                if (datasetFiles != null) {
	                    File[] files = datasetFiles.clone();
	                    for (File file : files) {
	                        reader = new XtfReader(file);
	                        String currentBid=null;
	                        while(true) {
	                            IoxEvent currentIoxEvent = reader.read();
	                            
	                            if (currentIoxEvent instanceof ch.interlis.iox.ObjectEvent) {
	                               IomObject objectValue = objectPool.addObject(((ch.interlis.iox.ObjectEvent) currentIoxEvent).getIomObject(), currentBid);
	                            }else if (currentIoxEvent instanceof ch.interlis.iox.StartBasketEvent) {
	                                currentBid=((ch.interlis.iox.StartBasketEvent) currentIoxEvent).getBid();
	                            } else if (currentIoxEvent instanceof ch.interlis.iox.EndTransferEvent) {
	                                break;
	                            }
	                        }
	                    }
	                }
	            } catch (RepositoryAccessException e) {
	                EhiLogger.logError(e); 
	            } catch (Ili2cException e) {
	                EhiLogger.logError(e);   
	            } catch (IoxException e) {
	                EhiLogger.logError(e);
	            } finally {
	                if (reader != null) {
	                    try {
	                        reader.close();
	                    } catch (IoxException e) {
	                        EhiLogger.logError(e);
	                    }
	                }
	            }
	        }
            return (IomObject) objectPool.getObject(targetOid, destinationClasses, bidOfTargetObj);
    }
    private void validateReferenceAttrs(String structAttrQName,IomObject iomStruct, Table structure, String bidOfObj){
		Iterator attrIter=structure.getAttributesAndRoles();
		while (attrIter.hasNext()){
			Object refAttrO = attrIter.next();
			if (refAttrO instanceof LocalAttribute){
				LocalAttribute refAttr = (LocalAttribute)refAttrO;
				Type type = refAttr.getDomain();
				if (type instanceof ReferenceType){
					ReferenceType refAttrType = (ReferenceType) type;
					String validateTarget=null;
					String attrQName=getScopedName(refAttr);
					if(!enforceTargetValidation){
						validateTarget=validationConfig.getConfigValue(attrQName, ValidationConfig.TARGET);
					}
					if(ValidationConfig.OFF.equals(validateTarget)){
						if(!configOffOufputReduction.contains(ValidationConfig.TARGET+":"+attrQName)){
							configOffOufputReduction.add(ValidationConfig.TARGET+":"+attrQName);
							errs.addEvent(errFact.logInfoMsg("{0} not validated, validation configuration target=off", attrQName));
						}
					}else{
						if(!datatypesOutputReduction.contains(attrQName)){
							datatypesOutputReduction.add(attrQName);
							errs.addEvent(errFact.logInfoMsg("validate reference attr {0}...",attrQName));
						}

						AbstractClassDef targetClass = refAttrType.getReferred();
						ArrayList<Viewable> destinationClasses = new ArrayList<Viewable>();
						destinationClasses.add(targetClass);
						IomObject refAttrStruct = iomStruct.getattrobj(refAttr.getName(), 0);
						String targetOid = null;
						if(refAttrStruct!=null){
							targetOid=refAttrStruct.getobjectrefoid();
						}
						String targetObjClassStr = null;
						OutParam<String> bidOfTargetObj = new OutParam<String>();
						IomObject targetObject = null;
						if(targetOid!=null){
							targetObject=(IomObject) objectPool.getObject(targetOid, destinationClasses, bidOfTargetObj);
						}
						if (targetObject == null) {
                            // Has an Extenal Obj?
                            // get restriction from reference attribute
                            String bid = validationConfig.getConfigValue(attrQName, ValidationConfig.REQUIRED_IN);
                            if (bid == null) {
                                // get restriction from structure attribute
                                bid = validationConfig.getConfigValue(structAttrQName, ValidationConfig.REQUIRED_IN);
                            }
                            if (bid != null) {
                                targetObject = findExternalObject(bid, targetOid, destinationClasses, bidOfTargetObj);
                            }
						}
						// EXTERNAL
						if(!refAttrType.isExternal()){
							if(targetOid!=null){
								if(targetObject!=null){
									if(isBasketSame(bidOfObj, targetObject)){
										// target object exists in correct basket
									} else {
										// no object with this oid found
										logMsg(validateTarget,"No object found with OID {0} in basket {1}.", targetOid,bidOfObj);
										return;
									}
								}else{
									// no object with this oid found
									logMsg(validateTarget,"No object found with OID {0}.", targetOid);
									return;
								}
							}
						} else {
							// EXTERNAL
							// not found in internal pool?
							if(targetObject==null){
								boolean extObjFound=false;
								// use external object resolver to find external objects.
								if(extObjResolvers!=null){
									// call custom function to verify in external data pools
									for(ExternalObjectResolver extObjResolver:extObjResolvers){
										if(extObjResolver.objectExists(targetOid, destinationClasses)){
												extObjFound=true;
												break;
										}
									}
								}
								if(allObjectsAccessible && !extObjFound){
									logMsg(validateTarget,"No object found with OID {0}.", targetOid);
								}
							}
						}
						if(targetObject != null){
							// target object exist
							Object modelEle=tag2class.get(targetObject.getobjecttag());
							Table targetObjClass = (Table) modelEle;
							targetObjClassStr = targetObject.getobjecttag();
							// if refAttrIter restricted to class
							StringBuffer classNames=new StringBuffer();
							String sep="";
							Iterator refAttrRestrictionIter = refAttrType.iteratorRestrictedTo();
							while (refAttrRestrictionIter.hasNext()){
								Object refAttrRestriction = refAttrRestrictionIter.next();
								targetClass = (Table) refAttrRestriction;
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
							// compare class
							if (targetObjClass.isExtending(targetClass) || targetObjClass.equals(targetClass)){
								// ok
							} else {
								logMsg(validateTarget,"wrong class {0} of target object {1} for reference attr {2}.", getScopedName(targetObjClass),targetOid, getScopedName(refAttr));
							}
						}
					}
				}
			}
		}
	}
	
	private void validateExistenceConstraint(IomObject iomObj, ExistenceConstraint existenceConstraint) {
		if(!ValidationConfig.OFF.equals(constraintValidation)){
			String constraintName = getScopedName(existenceConstraint);
			String checkConstraint = null;
			if(!enforceConstraintValidation){
				checkConstraint=validationConfig.getConfigValue(constraintName, ValidationConfig.CHECK);
			}
			if(ValidationConfig.OFF.equals(checkConstraint)){
				if(!configOffOufputReduction.contains(ValidationConfig.CHECK+":"+constraintName)){
					configOffOufputReduction.add(ValidationConfig.CHECK+":"+constraintName);
					errs.addEvent(errFact.logInfoMsg("{0} not validated, validation configuration check=off", constraintName));
				}
			} else {
				if (iomObj.getattrcount() == 0){
					return;
				}
				if(!constraintOutputReduction.contains(existenceConstraint+":"+constraintName)){
					constraintOutputReduction.add(existenceConstraint+":"+constraintName);
					errs.addEvent(errFact.logInfoMsg("validate existence constraint {0}...",getScopedName(existenceConstraint)));
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
				while (!valueExists && requiredInIterator.hasNext()) {
					classA = null;
					ObjectPath otherAttrPath = (ObjectPath)requiredInIterator.next();
					String otherAttrName = otherAttrPath.toString();
					otherClass = (Table) otherAttrPath.getRoot();
					String attrValueThisObj = iomObj.getattrvalue(restrictedAttrName);
					Iterator<String> basketIdIterator=objectPool.getBasketIds().iterator();
					while( !valueExists &&  basketIdIterator.hasNext()){
						String basketId=basketIdIterator.next();
						// iterate through iomObjects
						Iterator<IomObject> objectIterator = (objectPool.getObjectsOfBasketId(basketId)).valueIterator();
						while (!valueExists &&  objectIterator.hasNext()){
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
											if(iomObj.getattrvaluecount(restrictedAttrName)==otherIomObj.getattrvaluecount(restrictedAttrName)) {
												 for(int structi=0;structi<iomObj.getattrvaluecount(restrictedAttrName);structi++){
													 IomObject structEle=iomObj.getattrobj(restrictedAttrName, structi);
													 IomObject otherStructEle=otherIomObj.getattrobj(restrictedAttrName, structi);
													 if(structEle!=null && otherStructEle!=null) {
														 valueExists = equalsStructEle(((CompositionType) type).getComponentType(),structEle, otherStructEle);
														 if(!valueExists) {
															 // werte nicht gleich; weiterfahren mit naechstem Hauptobject
															 break;
														 }
													 }else {
														 break;
													 }
												 }
											}
										} else {
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
					String msg=validationConfig.getConfigValue(constraintName, ValidationConfig.MSG);
					if(msg!=null && msg.length()>0){
						logMsg(checkConstraint,msg);
					} else {
						logMsg(checkConstraint,"The value of the attribute {0} of {1} was not found in the condition class.", restrictedAttrName.toString(), iomObj.getobjecttag().toString());
					}
				}
			}
		}
	}
	
	private boolean equalsStructEle(Viewable aclass,IomObject iomObj, IomObject otherIomObj) {
		Iterator iter = aclass.getAttributesAndRoles2();
		while (iter.hasNext()) {
			ViewableTransferElement obj = (ViewableTransferElement)iter.next();
			if (obj.obj instanceof AttributeDef) {
				AttributeDef attr = (AttributeDef) obj.obj;
				if(!attr.isTransient()){
					Type proxyType=attr.getDomain();
					if(proxyType!=null && (proxyType instanceof ObjectType)){
						// skip implicit particles (base-viewables) of views
					}else{
						String targetAttrName=attr.getName();
						Type type=attr.getDomainResolvingAliases();
						if(iomObj.getattrvaluecount(targetAttrName)!=otherIomObj.getattrvaluecount(targetAttrName)) {
							return false;
						}
						if(iomObj.getattrvaluecount(targetAttrName)>0) {
							if(type instanceof ReferenceType){
								ReferenceType referenceType = (ReferenceType) type;
								if(!equalsReferenceValue(iomObj, referenceType, otherIomObj, targetAttrName,targetAttrName)) {
									return false;
								}
							} else if (type instanceof CoordType){
								CoordType coordType = (CoordType) type;
								if(!equalsCoordValue(iomObj, coordType, otherIomObj, targetAttrName,targetAttrName)) {
									return false;
								}
							} else if (type instanceof PolylineType){
								PolylineType polylineType = (PolylineType) type;
								if(!equalsPolylineValue(iomObj, polylineType, otherIomObj, targetAttrName,targetAttrName)){
									return false;
								}
							} else if (type instanceof SurfaceOrAreaType){
								SurfaceOrAreaType surfaceOrAreaType = (SurfaceOrAreaType) type;
								if(!equalsSurfaceOrAreaValue(iomObj, surfaceOrAreaType, otherIomObj, targetAttrName,targetAttrName)) {
									return false;
								}
							} else if (type instanceof CompositionType){
								 for(int structi=0;structi<iomObj.getattrvaluecount(targetAttrName);structi++){
									 IomObject structEle=iomObj.getattrobj(targetAttrName, structi);
									 IomObject otherStructEle=otherIomObj.getattrobj(targetAttrName, structi);
									 if(structEle!=null && otherStructEle!=null) {
										 if(!equalsStructEle(((CompositionType) type).getComponentType(),structEle, otherStructEle)) {
											 // werte nicht gleich; weiterfahren mit naechstem Hauptobject
											 return false;
										 }
									 }else {
										 return false;
									 }
								 }
							} else {
								if(!otherIomObj.getattrvalue(targetAttrName).equals(iomObj.getattrvalue(targetAttrName))){
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
	// HashMap of global unique constraints.
	HashMap<UniquenessConstraint, HashMap<AttributeArray, String>> seenUniqueConstraintValues = new HashMap<UniquenessConstraint, HashMap<AttributeArray, String>>();
	// List of all object Oid's and associated classPath's of uniqueness validate of Oid's.
	Map<String , String> uniqueObjectIDs = new HashMap<String, String>();
	HashSet<Object> loggedObjects=new HashSet<Object>();
    private boolean disableRounding=false;
	
	private void validateObject(IomObject iomObj,String attrPath,Viewable assocClass) throws IoxException {
		// validate if object is null
		boolean isObject = attrPath==null;
		if(isObject){
			setCurrentMainObj(iomObj);
		}
		
		// validate that OID is not a BID
		String objectoid = iomObj.getobjectoid();
		if (objectoid != null && !objectoid.equals("")) {
	        if (uniquenessOfBid.containsKey(objectoid)) {
	            errs.addEvent(errFact.logErrorMsg("OID <{0}> is equal to a BID", objectoid));
	        }		    
		}

		String tag=iomObj.getobjecttag();
        Object modelele=null;
		if(assocClass!=null && "REF".equals(tag)) {
		    modelele=assocClass;
		}else {
	        modelele=tag2class.get(tag);
		}
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
		
		boolean addToPool = true;
		if(isObject){
			errFact.setDefaultCoord(getDefaultCoord(iomObj));
			// validate that object is instance of a concrete class
			if(aclass1.isAbstract()){
				errs.addEvent(errFact.logErrorMsg("Object must be a non-abstract class"));
			}
			
			// association
			if (aclass1 instanceof AssociationDef){
				AssociationDef modelAssociationDef = (AssociationDef) aclass1;
				Domain oidType=((AbstractClassDef) modelAssociationDef).getOid();
				if (modelAssociationDef.isIdentifiable() || oidType!=null){
                    String oid = iomObj.getobjectoid();
					if (oid == null){
						errs.addEvent(errFact.logErrorMsg("Association {0} has to have an OID", iomObj.getobjecttag()));
						addToPool = false;
					}else if (!isValidId(oid)) {
	                    errs.addEvent(errFact.logErrorMsg("value <{0}> is not a valid OID", oid));                 
	                }
				}
			} else if (aclass1 instanceof Table){
				Table classValueTable = (Table) aclass1;
				// class
				if (classValueTable.isIdentifiable()){
                    String oid = iomObj.getobjectoid();
					if (oid == null){
						errs.addEvent(errFact.logErrorMsg("Class {0} has to have an OID", iomObj.getobjecttag()));
						addToPool = false;
					}else if (!isValidId(oid)) {
                        errs.addEvent(errFact.logErrorMsg("value <{0}> is not a valid OID", oid));                 
                    }
				// structure	
				} else {
					addToPool = false;
					if (iomObj.getobjectoid() != null){
						errs.addEvent(errFact.logErrorMsg("Structure {0} has not to have an OID", iomObj.getobjecttag()));
					}
				}
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
						validateAttrValue(aclass1,iomObj,attr,null);
					}
				}
			}
			if (isObject && obj.obj instanceof RoleDef) {
				RoleDef role = (RoleDef) obj.obj;
				{ // if (role.getExtending() == null)
					String refoid = null;
					String roleName = role.getName();
					// a role of an embedded association?
                    if (obj.embedded) {
                        int propc=iomObj.getattrvaluecount(roleName);
                        if(propc>=1) {
                            IomObject embeddedLinkObj = iomObj.getattrobj(roleName, 0);
                            AssociationDef roleOwner = (AssociationDef) role.getContainer();
                            
                            Viewable classOfCurrentObj = roleOwner;
                            if(classOfCurrentObj!=null) {
                                String iomObjOid=iomObj.getobjectoid();
                                if(iomObjOid==null && aclass1 instanceof AssociationDef) {
                                    iomObjOid=ObjectPool.getAssociationId(iomObj, (AssociationDef) aclass1);
                                }
                                Iterator constraintIterator=classOfCurrentObj.iterator();
                                while (constraintIterator.hasNext()) {
                                    Object constraintObj = constraintIterator.next();
                                    // role is unique?
                                    if(constraintObj instanceof UniquenessConstraint){
                                        UniquenessConstraint uniquenessConstraint=(UniquenessConstraint) constraintObj;
                                        validateUniquenessConstraint(embeddedLinkObj, iomObjOid,uniquenessConstraint, role);
                                    }
                                }
                            }
                            
                            if (roleOwner.getDerivedFrom() == null) {
                                // not just a link?
                                propNames.add(roleName);
                                
                                //Validate if no superfluous properties
                                if(propc>0) {
                                    validateObject(embeddedLinkObj,roleName,roleOwner);
                                }
                                
                                if (embeddedLinkObj != null) {
                                    refoid = embeddedLinkObj.getobjectrefoid();
                                    long orderPos = embeddedLinkObj
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
                            if(propc>1) {
                                errs.addEvent(errFact.logErrorMsg("Role {0} requires only one reference property",role.getScopedName()));
                            }
                            if(refoid==null) {
                                errs.addEvent(errFact.logErrorMsg("Role {0} requires a reference to another object",role.getScopedName()));
                            }
                        }
                    } else {
                        int propc=iomObj.getattrvaluecount(roleName);
                        if(propc==1) {
                            IomObject structvalue = iomObj.getattrobj(roleName, 0);
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
                        if(refoid==null) {
                            addToPool = false;
                            errs.addEvent(errFact.logErrorMsg("Role {0} requires a reference to another object",role.getScopedName()));
                        }
                    }
                    if (refoid != null) {
                        linkPool.addLink(iomObj,role,refoid,doItfOidPerTable);
                    }
				}
			 }
		}
		
		// Uniqueness
		if(isObject){
		    if(addToPool) {
	            Viewable aclass2=aclass1;
	            String iomObjOid=iomObj.getobjectoid();
	            if(iomObjOid==null && aclass1 instanceof AssociationDef) {
	                iomObjOid=ObjectPool.getAssociationId(iomObj, (AssociationDef) aclass1);
	            }
	            while(aclass2!=null) {
	                Iterator attrI=aclass2.iterator();
	                while (attrI.hasNext()) {
	                    Object obj1 = attrI.next();
	                    if(obj1 instanceof UniquenessConstraint){
	                        UniquenessConstraint uniquenessConstraint=(UniquenessConstraint) obj1; // uniquenessConstraint not null.
	                        validateUniquenessConstraint(iomObj, iomObjOid,uniquenessConstraint, null);
	                    }
	                }
	                aclass2=(Viewable) aclass2.getExtending();
	            }
		    }
		}
		
		if(isObject){
			if(addToPool){
				{
					// check if object id is unique in transferfile
					IomObject objectValue = objectPool.addObject(iomObj,currentBasketId);
					if(objectValue!=null){
						Object modelElement=tag2class.get(objectValue.getobjecttag());
						Viewable classValueOfKey= (Viewable) modelElement;
						errs.addEvent(errFact.logErrorMsg("OID {0} of object {1} already exists in {2}.", objectValue.getobjectoid(), iomObj.getobjecttag(), classValueOfKey.toString()));
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
		if(iomObj!=null) {
			currentMainOid=iomObj.getobjectoid();
		}else {
			currentMainOid=null;
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
								 if(structEle!=null) {
										IomObject coord=getDefaultCoord(structEle);
										if (coord!=null){
											return coord;
										}
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
	
	private String validateUnique(HashMap<UniquenessConstraint, HashMap<AttributeArray, String>> seenValues,String originObjOid,IomObject currentObject,UniquenessConstraint constraint, OutParam<AttributeArray> valuesRet, RoleDef role) {
        ArrayList<Object> values = new ArrayList<Object>();
		Iterator constraintIter = constraint.getElements().iteratorAttribute();
		while(constraintIter.hasNext()){
		    Object next = constraintIter.next();
		    ObjectPath objectPathObj = (ObjectPath) next;
		    PathEl[] pathElements = objectPathObj.getPathElements();
		    int lastPathIndex = pathElements.length - 1;
		    
		    Value value = getValueFromObjectPath(currentObject, pathElements, lastPathIndex,role);
		    if(value.isUndefined()) {
		        return null;
		    }else if(value.skipEvaluation()) {
		        return null;
		    }else if(value.getValue() != null) {
			    values.add(value.getValue());
			} else if (value.getComplexObjects() != null) {
			    IomObject complexValue = value.getComplexObjects().iterator().next();
			    if(complexValue.getobjectrefoid() != null) {
			        values.add(complexValue.getobjectrefoid());
			    } else {
			        values.add(complexValue);
			    }
			}else {
			    throw new IllegalStateException("unexpected value in unique");
			}
		}
		valuesRet.value=new AttributeArray(values);
		HashMap<AttributeArray, String> alreadySeenValues = null;
		if (seenValues.containsKey(constraint)){
			alreadySeenValues = seenValues.get(constraint);
			String oidOfNonUniqueObj = alreadySeenValues.get(valuesRet.value);

			// If exist a duplicate record
			if(oidOfNonUniqueObj!=null){
				return oidOfNonUniqueObj;
			}
			alreadySeenValues.put(valuesRet.value, originObjOid);
		} else {
			alreadySeenValues = new HashMap<AttributeArray, String>();
			alreadySeenValues.put(valuesRet.value, originObjOid);
			seenValues.put(constraint, alreadySeenValues);
		}
		return null;
	}

	private void validateAttrValue(Viewable eleClass,IomObject iomObj, AttributeDef attr,String attrPath) throws IoxException {
		 String attrName = attr.getName();
		 String attrQName = getScopedName(attr);
		 String iliClassQName=getScopedName(eleClass);
		 if(attrPath==null){
			 attrPath=attrName;
		 }else{
			 attrPath=attrPath+"/"+attrName;
		 }
		 String validateMultiplicity=validationConfig.getConfigValue(attrQName, ValidationConfig.MULTIPLICITY);
		 if(validateMultiplicity==null){
			 validateMultiplicity=globalMultiplicity;
		 }
		 String validateType=null;
		 String validateTarget=null;
		 String validateGeometryType=null;
		 if(!enforceTypeValidation){
			 validateType=validationConfig.getConfigValue(attrQName, ValidationConfig.TYPE);
			 if(validateType==null){
				 validateGeometryType=defaultGeometryTypeValidation;
			 }
		 }
		
		Type type0 = attr.getDomain();
		Type type = attr.getDomainResolvingAll();
		if (type instanceof CompositionType){
			 int structc=iomObj.getattrvaluecount(attrName);
				if(ValidationConfig.OFF.equals(validateMultiplicity)){
					if(!configOffOufputReduction.contains(ValidationConfig.MULTIPLICITY+":"+attrQName)){
						configOffOufputReduction.add(ValidationConfig.MULTIPLICITY+":"+attrQName);
						errs.addEvent(errFact.logInfoMsg("{0} not validated, validation configuration multiplicity=off in attribute {1}", attrQName, attrName, iomObj.getobjecttag(), iomObj.getobjectoid()));
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
						if(!configOffOufputReduction.contains(ValidationConfig.TYPE+":"+attrQName)){
							configOffOufputReduction.add(ValidationConfig.TYPE+":"+attrQName);
							errs.addEvent(errFact.logInfoMsg("{0} not validated, validation configuration type=off in attribute {1}", attrQName, attrName, iomObj.getobjecttag(), iomObj.getobjectoid()));
						}
					}else if(structEle==null) {
							 logMsg(validateType,"Attribute {0} requires a structure {1}", attrPath,((CompositionType)type).getComponentType().getScopedName(null));
					}else {
						String tag=structEle.getobjecttag();
						Object modelele=tag2class.get(tag);
						if(modelele==null){
							if(!unknownTypev.contains(tag)){
								errs.addEvent(errFact.logErrorMsg("unknown class <{0}> in attribute {1}",tag, attrName));
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
						validateObject(structEle, attrPath+"["+structi+"]",null);
					}
			 }
		}else{
			if(ValidationConfig.OFF.equals(validateMultiplicity)){
				if(!configOffOufputReduction.contains(ValidationConfig.MULTIPLICITY+":"+attrQName)){
					configOffOufputReduction.add(ValidationConfig.MULTIPLICITY+":"+attrQName);
					errs.addEvent(errFact.logInfoMsg("{0} not validated, validation configuration multiplicity=off", attrQName, iomObj.getobjecttag(), iomObj.getobjectoid()));
				}
			}else{
				Object topologyDone=pipelinePool.getIntermediateValue(attr, ValidationConfig.TOPOLOGY);
				if(type0==null) {
					 type0=attr.getDomainResolvingAll();
				}
				if(topologyDone==null){
					 int structc=iomObj.getattrvaluecount(attrName);
					 if(structc==0 && isAttributeMandatory(attr)) {
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
						 if(structc==0 && isAttributeMandatory(attr)) {
							 logMsg(validateMultiplicity,"Attribute {0} requires a value", attrPath);
						 }
					}else{
						// topology validation failed
						// ignore missing values
					}
				}
			}
			if(ValidationConfig.OFF.equals(validateType)){
				if(!configOffOufputReduction.contains(ValidationConfig.TYPE+":"+attrQName)){
					configOffOufputReduction.add(ValidationConfig.TYPE+":"+attrQName);
					errs.addEvent(errFact.logInfoMsg("{0} not validated, validation configuration type=off in attribute {1}", attrQName, attrName, iomObj.getobjecttag(), iomObj.getobjectoid()));
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
								logMsg(validateType, "value <{0}> is not in range in attribute {1}", valueStr, attrName);
							}
						} catch (NumberFormatException numberformatexception) {
							logMsg(validateType, "value <{0}> is not a valid Date in attribute {1}", valueStr, attrName);
						}
					} else {
						logMsg(validateType, "value <{0}> is not a valid Date in attribute {1}", valueStr, attrName);
					}
				} else if (attr.isDomainBoolean()) {
					// Value has to be not null and ("true" or "false")
					String valueStr = iomObj.getattrvalue(attrName);
					if (valueStr == null || valueStr.equals("true") || valueStr.equals("false")){
						// Value okay, skip it
					} else {
						logMsg(validateType, "value <{0}> is not a BOOLEAN in attribute {1}", valueStr, attrName);
					}
				} else if (attr.isDomainIliUuid()) {
					// Value is exactly 36 chars long and matches the regex
					String valueStr = iomObj.getattrvalue(attrName);
					if (valueStr == null || isValidUuid(valueStr)) {
							// Value ok, Skip it
					} else {
						logMsg(validateType, "value <{0}> is not a valid UUID in attribute {1}", valueStr, attrName);
					}
				} else if (attr.isDomainIli2Date()) {
					// Value matches regex and is not null and is in range of type.
					String valueStr = iomObj.getattrvalue(attrName);
					FormattedType subType = (FormattedType) type;
					if (valueStr != null){
						if (!valueStr.matches(subType.getRegExp())) {
							logMsg(validateType, "invalid format of date value <{0}> in attribue {1}", valueStr, attrName);
						} else if(!subType.isValueInRange(valueStr)){
							logMsg(validateType, "date value <{0}> is not in range in attribute {1}", valueStr, attrName);
						}
					}
				} else if (attr.isDomainIli2Time()) {
					// Value is not null and matches 0:0:0.000-23:59:59.999
					String valueStr = iomObj.getattrvalue(attrName);
					FormattedType subType = (FormattedType) type;
					// Min length and max length is added, because of the defined regular expression which does not test the length of the value.
					if (valueStr != null){
						if (!valueStr.matches(subType.getRegExp()) || valueStr.length() < 9 || valueStr.length() > 12){
							logMsg(validateType, "invalid format of time value <{0}> in attribute {1}", valueStr, attrName);
						} else if(!subType.isValueInRange(valueStr)){
							logMsg(validateType, "time value <{0}> is not in range in attribute {1}", valueStr, attrName);
						}
					}
				} else if (attr.isDomainIli2DateTime()) {
					// Value is not null
					String valueStr = iomObj.getattrvalue(attrName);
					FormattedType subType = (FormattedType) type;
					// Min length and max length is added, because of the defined regular expression which does not test the length of the value.
					if (valueStr != null){
						if (!valueStr.matches(subType.getRegExp()) || valueStr.length() < 18 || valueStr.length() > 23) {
							logMsg(validateType, "invalid format of datetime value <{0}> in attribute {1}", valueStr, attrName);
						} else if(!subType.isValueInRange(valueStr)){
							logMsg(validateType, "datetime value <{0}> is not in range in attribute {1}", valueStr, attrName);
						}
					}
				} else if(isDomainName(attr)) {
					// Value is not null
					String valueStr = iomObj.getattrvalue(attrName);
					if (valueStr!=null) {
						validateTextType(iomObj, attrPath, attrName, validateType, type, valueStr);
						if (isAKeyword(valueStr)) {
							logMsg(validateType,"value <{0}> is a keyword in attribute {1}", valueStr, attrName);
						}else{
							// value is not a keyword
						}
						Pattern pattern=Pattern.compile("[a-zA-Z]{1}([a-zA-Z0-9\\_]{1,})");
						Matcher matcher=pattern.matcher(valueStr);
						if(matcher!=null && matcher.matches()){
							// value matched pattern
						}else {
							logMsg(validateType,"invalid format of INTERLIS.NAME value <{0}> in attribute {1}", valueStr, attrName);
						}
					}
				}else if (isDomainUri(attr)) { 
                    // Value is not null
                    String valueStr = iomObj.getattrvalue(attrName);
                    if (valueStr!=null) {
                        validateTextType(iomObj, attrPath, attrName, validateType, type, valueStr);
                        
                        // see http://blog.dieweltistgarnichtso.net/constructing-a-regular-expression-that-matches-uris
                        Pattern pattern = Pattern.compile("((?<=\\()[A-Za-z][A-Za-z0-9\\+\\.\\-]*:([A-Za-z0-9\\.\\-_~:/\\?#\\[\\]@!\\$&'\\(\\)\\*\\+,;=]|%[A-Fa-f0-9]{2})+(?=\\)))|([A-Za-z][A-Za-z0-9\\+\\.\\-]*:([A-Za-z0-9\\.\\-_~:/\\?#\\[\\]@!\\$&'\\(\\)\\*\\+,;=]|%[A-Fa-f0-9]{2})+)");
                        Matcher matcher=pattern.matcher(valueStr);
                        if(matcher!=null && matcher.matches()){
                         // value matched pattern
                        }else {
                            logMsg(validateType,"invalid format of INTERLIS.URI value <{0}> in attribute {1}", valueStr, attrName);
                        }
                    }
				} else if (type instanceof PolylineType){
					PolylineType polylineType=(PolylineType)type;
					IomObject polylineValue=iomObj.getattrobj(attrName, 0);
					if (polylineValue != null){
						boolean isValid=validatePolyline(validateGeometryType, polylineType, polylineValue, attrName);
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
							boolean isValid = validatePolygon(validateGeometryType,surfaceOrAreaType, surfaceValue, iomObj, attrName);
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
										boolean surfaceTopologyValid=validateSurfaceTopology(validateGeometryType,attr,(AreaType)surfaceOrAreaType,currentMainOid, surfaceValue);
										if(!ValidationConfig.OFF.equals(areaOverlapValidation)){
											
											if(surfaceTopologyValid) {
											
												ItfAreaPolygon2Linetable allLines=areaAttrs.get(attr);
												if(allLines==null){
													allLines=new ItfAreaPolygon2Linetable(iliClassQName, objPoolManager); 
													areaAttrs.put(attr,allLines);
												}
												validateAreaTopology(validateGeometryType,allLines,(AreaType)surfaceOrAreaType, currentMainOid,null,surfaceValue);
											}else {
												// surface topology not valid
												errs.addEvent(errFact.logInfoMsg("AREA topology not validated, validation of SURFACE topology failed"));
											}
										}
									}
								}
							}
						}
					 }
				}else if(type instanceof CoordType){
					IomObject coord=iomObj.getattrobj(attrName, 0);
					if (coord!=null){
						validateCoordType(validateGeometryType, (CoordType)type, coord, attrName);
					}
				}else if(type instanceof NumericType){
					String valueStr=iomObj.getattrvalue(attrName);
					if(valueStr!=null){
						String newValueStr=validateNumericType(validateType, (NumericType)type, valueStr, attrName);
						if(newValueStr!=null) {
							iomObj.setattrvalue(attrName, newValueStr);
						}
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
							 logMsg(validateType,"value {0} is not a member of the enumeration in attribute {1}", value, attrName);
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
					validateTextType(iomObj, attrPath, attrName, validateType, type, value);
				}
			}
		}
	}

	private void validateTextType(IomObject iomObj, String attrPath, String attrName, String validateType, Type type,
			String value) {
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

	private static boolean isAttributeMandatory(AttributeDef attr) {
		Type attrType=attr.getDomain();
		if(attrType==null) {
			if(attr instanceof LocalAttribute){
				Evaluable[] ev = (((LocalAttribute)attr).getBasePaths());
				attrType=((ObjectPath)ev[0]).getType();
			}else {
				throw new IllegalArgumentException("unexpected attribute type "+attr.getScopedName());
			}
		}
		return attrType.isMandatoryConsideringAliases();
	}
	
	private void validateAreaTopology(String validateType, ItfAreaPolygon2Linetable allLines,AreaType type, String mainObjTid,String internalTid,IomObject iomPolygon) throws IoxException {
		// get lines
		allLines.addPolygon(mainObjTid,internalTid,iomPolygon,validateType,errFact);
	}

	private boolean validateSurfaceTopology(String validateType, AttributeDef attr,SurfaceOrAreaType type, String mainObjTid,IomObject iomValue) {
		boolean surfaceTopologyValid=true;
		try {
			surfaceTopologyValid=ItfSurfaceLinetable2Polygon.validatePolygon(mainObjTid, attr, iomValue, errFact,validateType);
		} catch (IoxException e) {
			surfaceTopologyValid=false;
			errs.addEvent(errFact.logErrorMsg(e,"failed to validate polygon"));
		}
		return surfaceTopologyValid;
	}
	
	private void validatePolylineTopology(String attrPath,String validateType, PolylineType type, IomObject iomValue) {
		CompoundCurve seg=null;
		try {
		    OutParam<Boolean> foundErrs=new OutParam<Boolean>();
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
	private boolean validatePolygon(String validateType, SurfaceOrAreaType surfaceOrAreaType, IomObject surfaceValue, IomObject currentIomObj, String attrName) {
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
				// a multisurface consists of at least one boundary.
				if(boundaryc==0){
					String objectIdentification = currentIomObj.getobjectoid();
					if(objectIdentification==null){
						objectIdentification = currentIomObj.getobjecttag();
					}
					logMsg(validateType,"missing outerboundary in {0} of object {1}.", attrName, objectIdentification);
					return false;
				} else {
					for(int boundaryi=0;boundaryi<boundaryc;boundaryi++){
						IomObject boundary=surface.getattrobj("boundary",boundaryi);
						if(boundaryi==0){
							// shell
						}else{
						    // hole
						}    
						for(int polylinei=0;polylinei<boundary.getattrvaluecount("polyline");polylinei++){
							IomObject polyline=boundary.getattrobj("polyline",polylinei);
							validatePolyline(validateType, surfaceOrAreaType, polyline, attrName);
							// add line to shell or hole
						}
					    // add shell or hole to surface
					}
				}
			}
		} else {
			logMsg(validateType, "unexpected Type "+surfaceValue.getobjecttag()+"; MULTISURFACE expected");
			return false;
		}
		return true;
	}

	// returns true if valid
	private boolean validatePolyline(String validateType, LineType polylineType, IomObject polylineValue, String attrName) {
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
								validateCoordType(validateType, (CoordType) polylineType.getControlPointDomain().getType(), segment, attrName);
							}else{
								logMsg(validateType, "unexpected COORD");
								foundErrs = foundErrs || true;
							}
						} else if (segment.getobjecttag().equals("ARC")){
							if(lineformNames.contains("ARCS") && segmenti>0){
								validateARCSType(validateType, (CoordType) polylineType.getControlPointDomain().getType(), segment, attrName);
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

	private void validateCoordType(String validateType, CoordType coordType, IomObject coordValue, String attrName) {
		if (coordType.getDimensions().length >= 1){
			if (coordValue.getattrvalue("C1") != null){
				coordValue.setattrvalue("C1", validateNumericType(validateType, (NumericType)coordType.getDimensions()[0], coordValue.getattrvalue("C1"), attrName));
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
				coordValue.setattrvalue("C2", validateNumericType(validateType, (NumericType)coordType.getDimensions()[1], coordValue.getattrvalue("C2"), attrName));
			} else if (coordValue.getattrvalue("A2") != null) {
				logMsg(validateType, "Not a type of COORD");
			} else {
				logMsg(validateType, "Wrong COORD structure, C2 expected");
			}
		}
		if (coordType.getDimensions().length == 3){
			if (coordValue.getattrvalue("C3") != null){
				coordValue.setattrvalue("C3", validateNumericType(validateType, (NumericType)coordType.getDimensions()[2], coordValue.getattrvalue("C3"), attrName));
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

	private void validateARCSType(String validateType, CoordType coordType, IomObject coordValue, String attrName) {
		int dimLength=coordType.getDimensions().length;
		String c1=coordValue.getattrvalue("C1");
		String c2=coordValue.getattrvalue("C2");
		String c3=coordValue.getattrvalue("C3");
		String a1=coordValue.getattrvalue("A1");
		String a2=coordValue.getattrvalue("A2");
		
		boolean wrongArcStructure=false;
		int c1Count=coordValue.getattrvaluecount("C1");
		if (dimLength>=2 && dimLength<=3){
			if(a1!=null && a2!=null && c1!=null && c2!=null){
				coordValue.setattrvalue("A1", validateNumericType(validateType, (NumericType)coordType.getDimensions()[0], a1, attrName));
				coordValue.setattrvalue("A2", validateNumericType(validateType, (NumericType)coordType.getDimensions()[1], a2, attrName));
				coordValue.setattrvalue("C1", validateNumericType(validateType, (NumericType)coordType.getDimensions()[0], c1, attrName));
				coordValue.setattrvalue("C2", validateNumericType(validateType, (NumericType)coordType.getDimensions()[1], c2, attrName));
				if(dimLength==2) {
					if(c3!=null) {
						logMsg(validateType, "Wrong ARC structure, C3 not expected");
					}
				}else if(dimLength==3) {
					if(c3!=null) {
						coordValue.setattrvalue("C3", validateNumericType(validateType, (NumericType)coordType.getDimensions()[2], c3, attrName));
					}else {
						logMsg(validateType, "Wrong ARC structure, C3 expected");
					}
				}
			}else {
				wrongArcStructure=true;
			}
		}else {
			wrongArcStructure=true;
		}
		if(wrongArcStructure) {
			logMsg(validateType, "Wrong ARC structure");
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

	private String validateNumericType(String validateType, NumericType type, String valueStr, String attrName) {
		PrecisionDecimal value=null;
		try {
			value=new PrecisionDecimal(valueStr);
		} catch (NumberFormatException e) {
			 logMsg(validateType,"value <{0}> is not a number", valueStr);
		}
		BigDecimal rounded=null;
		if(value!=null){
			PrecisionDecimal minimum=((NumericType) type).getMinimum();
			PrecisionDecimal maximum=((NumericType) type).getMaximum();
			BigDecimal min_general = new BigDecimal(minimum.toString());
			BigDecimal max_general = new BigDecimal(maximum.toString());
			BigDecimal valueBigDec = new BigDecimal(value.toString());
			int precision= minimum.getAccuracy();
			if(disableRounding) {
                rounded=valueBigDec;
			}else {
	            rounded=roundNumeric(precision,valueBigDec);
			}
			if (rounded!=null && (rounded.compareTo(min_general)==-1 || rounded.compareTo(max_general)==+1)){
				logMsg(validateType,"value {0} is out of range in attribute {1}", rounded.toString(), attrName);
			}
		}
		if(rounded==null) {
			return null;
		}
		return rounded.toPlainString();
	}
	
	private boolean isAKeyword(String valueStr) {
		HashSet<String> keyWords=Ili23KeyWords.getAllKeyWords();
		if(keyWords.contains(valueStr)){
			// value is a keyword
			return true;
		}
		// not a keyword
		return false;
	}
	
	public static BigDecimal roundNumeric(int precision, String valueStr) {
        if(valueStr==null) {
            return null;
        }
        return roundNumeric(precision, new BigDecimal(valueStr));
	}
	public static BigDecimal roundNumeric(int precision, BigDecimal value) {
        if(value==null) {
            return null;
        }
        boolean isNegative=value.signum()==-1;
		BigDecimal rounded=null;
		if(value!=null) {
			if(isNegative){
				rounded=value.setScale(precision, BigDecimal.ROUND_HALF_DOWN);
			}else {
				rounded=value.setScale(precision, BigDecimal.ROUND_HALF_UP);
			}
		}
		return rounded;
	}
	
	public boolean isValidUuid(String valueStr) {
		return valueStr.length() == 36 && valueStr.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}?");
	}
	
	private static boolean isDomainName(AttributeDef attr){
		TransferDescription td=(TransferDescription) attr.getContainer(TransferDescription.class);
		Type type=attr.getDomain();
		while(type instanceof TypeAlias) {
			if (((TypeAlias) type).getAliasing() == td.INTERLIS.NAME) {
				return true;
			}
			type=((TypeAlias) type).getAliasing().getType();
		}
		return false;
	}
	
    private static boolean isDomainUri(AttributeDef attr){
        TransferDescription td=(TransferDescription) attr.getContainer(TransferDescription.class);
        Type type=attr.getDomain();
        while(type instanceof TypeAlias) {
            if (((TypeAlias) type).getAliasing() == td.INTERLIS.URI) {
                return true;
            }
            type=((TypeAlias) type).getAliasing().getType();
        }
        return false;
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
	public boolean isAutoSecondPass() {
		return autoSecondPass;
	}
	public void setAutoSecondPass(boolean autoSecondPass) {
		this.autoSecondPass = autoSecondPass;
	}
}