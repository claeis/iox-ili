package ch.interlis.iox_j.validator;

import java.io.File;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ch.interlis.ili2c.generator.Interlis2Generator;
import ch.interlis.ili2c.metamodel.DomainConstraint;
import ch.interlis.ili2c.metamodel.Expression;
import ch.interlis.ili2c.metamodel.ValueRefThis;
import com.vividsolutions.jts.geom.Coordinate;
import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.settings.Settings;
import ch.ehi.basics.types.OutParam;
import ch.ehi.iox.objpool.ObjectPoolManager;
import ch.interlis.ili2c.Ili2cException;
import ch.interlis.ili2c.gui.UserSettings;
import ch.interlis.ili2c.metamodel.AbstractClassDef;
import ch.interlis.ili2c.metamodel.AbstractCoordType;
import ch.interlis.ili2c.metamodel.AbstractSurfaceOrAreaType;
import ch.interlis.ili2c.metamodel.AreaType;
import ch.interlis.ili2c.metamodel.AssociationDef;
import ch.interlis.ili2c.metamodel.AssociationPath;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.AttributeRef;
import ch.interlis.ili2c.metamodel.BlackboxType;
import ch.interlis.ili2c.metamodel.Cardinality;
import ch.interlis.ili2c.metamodel.CompositionType;
import ch.interlis.ili2c.metamodel.Constant;
import ch.interlis.ili2c.metamodel.Constant.Enumeration;
import ch.interlis.ili2c.metamodel.Constraint;
import ch.interlis.ili2c.metamodel.CoordType;
import ch.interlis.ili2c.metamodel.DataModel;
import ch.interlis.ili2c.metamodel.Domain;
import ch.interlis.ili2c.metamodel.Element;
import ch.interlis.ili2c.metamodel.EnumTreeValueType;
import ch.interlis.ili2c.metamodel.EnumerationType;
import ch.interlis.ili2c.metamodel.Evaluable;
import ch.interlis.ili2c.metamodel.ExistenceConstraint;
import ch.interlis.ili2c.metamodel.ExpressionSelection;
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
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.MultiAreaType;
import ch.interlis.ili2c.metamodel.MultiCoordType;
import ch.interlis.ili2c.metamodel.MultiPolylineType;
import ch.interlis.ili2c.metamodel.MultiSurfaceOrAreaType;
import ch.interlis.ili2c.metamodel.MultiSurfaceType;
import ch.interlis.ili2c.metamodel.NumericType;
import ch.interlis.ili2c.metamodel.NumericalType;
import ch.interlis.ili2c.metamodel.ObjectPath;
import ch.interlis.ili2c.metamodel.ObjectType;
import ch.interlis.ili2c.metamodel.Objects;
import ch.interlis.ili2c.metamodel.ParameterValue;
import ch.interlis.ili2c.metamodel.PathEl;
import ch.interlis.ili2c.metamodel.PathElAbstractClassRole;
import ch.interlis.ili2c.metamodel.PathElAssocRole;
import ch.interlis.ili2c.metamodel.PathElBase;
import ch.interlis.ili2c.metamodel.PathElRefAttr;
import ch.interlis.ili2c.metamodel.PathElThis;
import ch.interlis.ili2c.metamodel.PlausibilityConstraint;
import ch.interlis.ili2c.metamodel.PolylineType;
import ch.interlis.ili2c.metamodel.PrecisionDecimal;
import ch.interlis.ili2c.metamodel.PredefinedModel;
import ch.interlis.ili2c.metamodel.Projection;
import ch.interlis.ili2c.metamodel.ReferenceType;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.ili2c.metamodel.SetConstraint;
import ch.interlis.ili2c.metamodel.StructAttributeRef;
import ch.interlis.ili2c.metamodel.SurfaceOrAreaType;
import ch.interlis.ili2c.metamodel.SurfaceType;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.ili2c.metamodel.TextOIDType;
import ch.interlis.ili2c.metamodel.TextType;
import ch.interlis.ili2c.metamodel.Topic;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.Type;
import ch.interlis.ili2c.metamodel.TypeAlias;
import ch.interlis.ili2c.metamodel.UniquenessConstraint;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.ili2c.metamodel.ViewableAlias;
import ch.interlis.ili2c.metamodel.ViewableTransferElement;
import ch.interlis.ili2c.parser.Ili23Parser;
import ch.interlis.ilirepository.Dataset;
import ch.interlis.ilirepository.impl.RepositoryAccessException;
import ch.interlis.iom.IomConstants;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iom_j.itf.ModelUtilities;
import ch.interlis.iom_j.itf.impl.ItfAreaPolygon2Linetable;
import ch.interlis.iom_j.itf.impl.ItfSurfaceLinetable2Polygon;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.noding.CompoundCurveNoder;
import ch.interlis.iom_j.itf.impl.jtsext.noding.Intersection;
import ch.interlis.iom_j.xtf.XtfStartTransferEvent;
import ch.interlis.iom_j.xtf.XtfWriter;
import ch.interlis.iom_j.xtf.impl.MyHandler;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxLogging;
import ch.interlis.iox.IoxReader;
import ch.interlis.iox.IoxValidationConfig;
import ch.interlis.iox.IoxWriter;
import ch.interlis.iox.StartBasketEvent;
import ch.interlis.iox_j.IoxIntersectionException;
import ch.interlis.iox_j.IoxInvalidDataException;
import ch.interlis.iox_j.PipelinePool;
import ch.interlis.iox_j.jts.Iox2jtsext;
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.iox_j.utility.ReaderFactory;
import ch.interlis.iox_j.validator.functions.DmavtymTopologie;
import ch.interlis.iox_j.validator.functions.Text;
import ch.interlis.iox_j.validator.functions.Math;
import ch.interlis.iox_j.validator.functions.MinimalRuntimeSystem;
import ch.interlis.iox_j.validator.functions.Interlis;
import ch.interlis.iox_j.validator.functions.Interlis_ext;

public class Validator implements ch.interlis.iox.IoxValidator {
    private static final String ILI_LEGACYAREAREAS = "ILI_LEGACYAREAREAS";
    private static final String ENUM_TREE_VALUES = "ENUM_TREE_VALUES";
    private static final String VALUE_REF_THIS = "Value";
    public static final String ALL_OBJECTS_ACCESSIBLE="allObjectsAccessible";
	public static final String REGEX_FOR_ID_VALIDATION = "^[0-9a-zA-Z_][0-9a-zA-Z\\_\\.\\-]*";
	public static final String REGEX_FOR_TEXTOID_VALIDATION = "^[a-zA-Z_][0-9a-zA-Z\\_\\.\\-]*";
	public static final String REGEX_FOR_STANDARTOID_VALIDATION = "^[a-zA-Z][0-9a-zA-Z]*";
	public static final String REGEX_FOR_BASE64_VALIDATION = "[a-zA-Z0-9+/= \\t\\n\\r]+";

	public static final String CONFIG_DO_ITF_LINETABLES="ch.interlis.iox_j.validator.doItfLinetables";
	public static final String CONFIG_DO_ITF_LINETABLES_DO="doItfLinetables";
	public static final String CONFIG_DO_ITF_OIDPERTABLE="ch.interlis.iox_j.validator.doItfOidPerTable";
	public static final String CONFIG_DO_ITF_OIDPERTABLE_DO="doItfOidPerTable";
	public static final String CONFIG_DO_XTF_VERIFYMODEL="ch.interlis.iox_j.validator.doXtfVersionControl";
	public static final String CONFIG_DO_XTF_VERIFYMODEL_DO="doXtfVersionControl";
    public static final String CONFIG_DO_SINGLE_PASS="ch.interlis.iox_j.validator.doSinglePass";
    public static final String CONFIG_DO_SINGLE_PASS_DO="doSinglePass";
	public static final String CONFIG_CUSTOM_FUNCTIONS="ch.interlis.iox_j.validator.customFunctions";
	public static final String CONFIG_OBJECT_RESOLVERS="ch.interlis.iox_j.validator.objectResolvers";
    public static final String CONFIG_DEBUG_XTFOUT = "ch.interlis.iox_j.validator.debugXtfOutput";
	public static final String CONFIG_VERBOSE = "ch.interlis.iox_j.validator.verbose";
	// the object count result as value in map with the appropriate function as key.
	private Map<Evaluable, Value> functions=new HashMap<Evaluable, Value>();
	private ObjectPoolManager objPoolManager=null;
	private ObjectPool objectPool = null;
	private LinkPool linkPool=null;
	private ch.interlis.iox.IoxValidationConfig validationConfig=null;
	//private ch.interlis.iox.IoxDataPool pipelinePool=null;
	private PipelinePool pipelinePool=null;
	private IoxLogging errs=null;
	private LogEventFactory errFact=null;
	private TransferDescription td=null;
	private boolean doItfLineTables=false;
	private boolean doItfOidPerTable=false;
	private Settings settings=null;
	private boolean validationOff=false;
	private boolean singlePass=false;
	private String areaOverlapValidation=null;
	private String constraintValidation=null;
	private String defaultGeometryTypeValidation=null;
	Pattern patternForIdValidation = null;
	Pattern patternForTextOIdValidation = null;
	Pattern patternForStandartOidValidation = null;
    Pattern patternForBase64Validation = null;
	private boolean enforceTypeValidation=false;
	private boolean enforceConstraintValidation=false;
	private boolean enforceTargetValidation=false;
	private String currentBasketId = null;
	private String currentMainOid=null;
	private boolean autoSecondPass=true;
	private boolean allObjectsAccessible=false;
	private boolean isVerbose = false;
	private Map<AttributeDef,ItfAreaPolygon2Linetable> areaAttrs=new HashMap<AttributeDef,ItfAreaPolygon2Linetable>();
	private Map<AttributeDef, Boolean> areaAttrsAreSurfaceTopologiesValid = new HashMap<AttributeDef, Boolean>();
	private Map<String,Class> customFunctions=new HashMap<String,Class>(); // qualified Interlis function name -> java class that implements that function
	private List<ExternalObjectResolver> extObjResolvers=null; // java class that implements ExternalObjectResolver
	private HashMap<Constraint, Projection> additionalConstraints=new HashMap<Constraint, Projection>();
	private Map<PlausibilityConstraint, PlausibilityPoolValue> plausibilityConstraints=new LinkedHashMap<PlausibilityConstraint, PlausibilityPoolValue>();
	private HashSet<String> configOffOufputReduction =new HashSet<String>();
	private HashSet<String> setConstraintOufputReduction =new HashSet<String>();
	private HashSet<String> constraintOutputReduction=new HashSet<String>();
    private HashSet<String> seenModels=new HashSet<String>();
	private HashSet<String> datatypesOutputReduction=new HashSet<String>();
	private Map<String, String> uniquenessOfBid = new HashMap<String, String>();
    private Map<String, String> stableBids = new HashMap<String, String>();
	private String globalMultiplicity=null;
	private ch.interlis.ilirepository.ReposManager repositoryManager = null;
	private java.util.ResourceBundle rsrc=java.util.ResourceBundle.getBundle("ch.interlis.iox_j.validator.ValidatorMessages");
    private IoxWriter writer=null;
    private long objectCount=0l;
    private long structCount=0l;
	private final Map<String, Domain> genericDomains = new HashMap<String, Domain>();

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
	 * @param settings to validate ITF files you normally need to set config.setValue(Validator.CONFIG_DO_ITF_OIDPERTABLE, Validator.CONFIG_DO_ITF_OIDPERTABLE_DO);
	 */
	public Validator () { }

	public Validator(TransferDescription td, IoxValidationConfig validationConfig,
			IoxLogging errs, LogEventFactory errFact, PipelinePool pipelinePool,Settings settings) {
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
		
		this.legacyAreAreas=System.getenv(ILI_LEGACYAREAREAS)!=null;
		this.settings=settings;
		this.patternForIdValidation = Pattern.compile(REGEX_FOR_ID_VALIDATION);
		this.patternForTextOIdValidation = Pattern.compile(REGEX_FOR_TEXTOID_VALIDATION);
		this.patternForStandartOidValidation = Pattern.compile(REGEX_FOR_STANDARTOID_VALIDATION);
        this.patternForBase64Validation = Pattern.compile(REGEX_FOR_BASE64_VALIDATION);
		this.settings.setTransientObject(InterlisFunction.IOX_DATA_POOL,pipelinePool);
        this.settings.setTransientObject(InterlisFunction.IOX_VALIDATOR,this);
		this.pipelinePool=pipelinePool;
		objPoolManager=new ObjectPoolManager();
		Map<String,Class> cf=(Map<String, Class>) settings.getTransientObject(CONFIG_CUSTOM_FUNCTIONS);
		if(cf!=null){
			customFunctions=cf;
		}
		List<Class> resolverClasses=(List<Class>) settings.getTransientObject(CONFIG_OBJECT_RESOLVERS);
		
        // get/create repository manager
        repositoryManager = (ch.interlis.ilirepository.ReposManager) settings
                .getTransientObject(UserSettings.CUSTOM_ILI_MANAGER);
		
        this.singlePass = CONFIG_DO_SINGLE_PASS_DO.equals(settings.getValue(CONFIG_DO_SINGLE_PASS));
		this.doItfLineTables = CONFIG_DO_ITF_LINETABLES_DO.equals(settings.getValue(CONFIG_DO_ITF_LINETABLES));
		this.doItfOidPerTable = CONFIG_DO_ITF_OIDPERTABLE_DO.equals(settings.getValue(CONFIG_DO_ITF_OIDPERTABLE));
		this.isVerbose = ValidationConfig.TRUE.equals(settings.getTransientValue(CONFIG_VERBOSE));
		allObjectsAccessible=ValidationConfig.TRUE.equals(validationConfig.getConfigValue(ValidationConfig.PARAMETER, ValidationConfig.ALL_OBJECTS_ACCESSIBLE));
        if(singlePass){
            errs.addEvent(errFact.logInfoMsg("do single pass validation"));
        }
		if(!allObjectsAccessible){
			errs.addEvent(errFact.logInfoMsg("assume unknown external objects"));
		}
        disableRounding=ValidationConfig.TRUE.equals(validationConfig.getConfigValue(ValidationConfig.PARAMETER, ValidationConfig.DISABLE_ROUNDING));
        if(disableRounding){
            errs.addEvent(errFact.logInfoMsg("disable rounding"));
        }
        disableAreAreasMessages=ValidationConfig.TRUE.equals(validationConfig.getConfigValue(ValidationConfig.PARAMETER, ValidationConfig.DISABLE_AREAREAS_MESSAGES));
        if(disableAreAreasMessages){
            errs.addEvent(errFact.logInfoMsg("disable areAreas() messages"));
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
		if(!singlePass) {
	        objectPool=new ObjectPool(doItfOidPerTable, errs, errFact, tag2class,objPoolManager);
	        linkPool=new LinkPool(objPoolManager);
		}
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
                resolver.init(td,settings,validationConfig, objectPool, errFact);
                extObjResolvers.add(resolver);
            }
        }
		String filename=settings.getValue(CONFIG_DEBUG_XTFOUT);
		if(filename!=null) {
		    EhiLogger.traceState("xtfout <"+filename+">");
		    try {
                writer=new XtfWriter(new File(filename),td);
            } catch (IoxException e) {
                throw new IllegalArgumentException(e);
            }
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
		if(writer!=null) {
		    try {
                writer.close();
            } catch (IoxException e) {
                throw new IllegalArgumentException(e);
            }
		    writer=null;
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
			errs.addEvent(errFact.logInfoMsg(rsrc.getString("validate.firstValidationPass")));
			validateInconsistentIliAndXMLVersion(event);
			uniquenessOfBid.clear();
			uniquenessOfBid.putAll(stableBids);
			if(!singlePass) {
	            objectPool.startNewTransfer();
			}
		} else if (event instanceof ch.interlis.iox.StartBasketEvent){
			StartBasketEvent startBasketEvent = ((ch.interlis.iox.StartBasketEvent) event);
			currentBasketId = startBasketEvent.getBid();
			validateBasketEvent(startBasketEvent);
		}else if(event instanceof ch.interlis.iox.ObjectEvent){
			IomObject iomObj=new ch.interlis.iom_j.Iom_jObject(((ch.interlis.iox.ObjectEvent)event).getIomObject());
			try {
                validateObject(iomObj,null,null);
			} catch (IoxException e) {
				errs.addEvent(errFact.logInfoMsg(rsrc.getString("validate.failedToValidateObject"), iomObj.toString()));
			}catch(RuntimeException e) {
				EhiLogger.traceState(rsrc.getString("validate.failingObject") + iomObj.toString());
				throw e;
			}
		} else if (event instanceof ch.interlis.iox.EndBasketEvent){
		    cleanupCurrentBasket();
		}else if (event instanceof ch.interlis.iox.EndTransferEvent){
			if(autoSecondPass){
				doSecondPass();
			}
		}
        if(writer!=null) {
		    try {
                writer.write(event);
            } catch (IoxException e) {
                throw new IllegalArgumentException(e);
            }
		}
	}
    private void validateInconsistentIliAndXMLVersion(ch.interlis.iox.IoxEvent event) {
        String versionControl = settings.getValue(CONFIG_DO_XTF_VERIFYMODEL);
        if (versionControl != null && versionControl.equals(CONFIG_DO_XTF_VERIFYMODEL_DO) && event instanceof XtfStartTransferEvent) {            
            XtfStartTransferEvent startTransferEvent = (XtfStartTransferEvent) event;
            Collection<IomObject> headerObjValues = startTransferEvent.getHeaderObjects().values();
            List<IomObject> headerObjects = new ArrayList<IomObject>(headerObjValues);

            for(IomObject currentObj : headerObjects) {
                if (currentObj.getobjecttag().equals(MyHandler.HEADER_OBJECT_MODELENTRY) && 
                        currentObj.getattrvaluecount(MyHandler.HEADER_OBJECT_MODELENTRY_VERSION) > 0) {
                    String currentVersion = currentObj.getattrvalue(MyHandler.HEADER_OBJECT_MODELENTRY_VERSION);
                    String currentModelName = currentObj.getattrvalue("model");
                    Model model = getModelFromTransferDesc(currentModelName);
                    String iliVersion = model.getModelVersion();
                    if (!(currentVersion.equals(iliVersion))) {
                        String versionInfoMessage = "The VERSION in model (" + currentModelName + ") and transferfile do not match (" 
                                    + iliVersion + "!=" + currentVersion + ")";
                        errs.addEvent(errFact.logInfoMsg(versionInfoMessage));
                    }                    
                }
            }
        }
    }
    
    private Model getModelFromTransferDesc(String modelName) {
        Iterator<Model> modeli = td.iterator();
        
        List<Model> list = new ArrayList<Model>();
        while (modeli.hasNext()) {
            list.add(0, modeli.next());
        }
        modeli = list.iterator();
        while (modeli.hasNext()) {
            Model model = modeli.next();
            if (model.getName().equals(modelName)) {
                return model;
            }
        }
        return null;
    }
    
    private void cleanupCurrentBasket() {
        currentMainOid = null;
        errFact.setDataObj(null);
        genericDomains.clear();
    }
    private void validateBasketEvent(ch.interlis.iox.StartBasketEvent event) {
        boolean isValid = true;
        Topic topic = null;
        Model model = null;
        errFact.setTid(event.getBid());
        errFact.setIliqname(event.getType());
	    if (!isValidTopicName(event.getType())) {
	        isValid = false;
            errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateBasketEvent.invalidBasketElementName"), event.getType()));
        }
        if (!isValidId(event.getBid())) {
            isValid = false;
            LogEventFactory factory = new LogEventFactory();
            errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateBasketEvent.valueIsNotAValidBID"), event.getBid()==null?"":event.getBid()));
        }
        Domain bidDomain=null;
        if(isValid) {
            topic = (Topic)td.getElement(event.getType());
            model = (Model) topic.getContainer();
            seenModels.add(model.getName());
            collectSetConstraints(topic);
            bidDomain=topic.getBasketOid();
            if (bidDomain!=null && !isAValidBasketOID(bidDomain, event.getBid())) {
                isValid = false;
                errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateBasketEvent.valueIsNotAValidBID"), event.getBid()==null?"":event.getBid()));
            }
        }
        if(isValid) {
            String bid=event.getBid();
            // check if basket id is unique in transfer file
            if(bid != null){
                if(bidDomain==td.INTERLIS.UUIDOID) {
                    bid=normalizeUUID(bid);
                }
            	if(uniquenessOfBid.containsKey(bid)){
            		errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateUniqueBasketId.bidOfAlreadyExistIn"), bid, event.getType(), uniquenessOfBid.get(bid)));
            	} else {
            		uniquenessOfBid.put(bid, event.getType());
                    if (bidDomain!=null) {
                        stableBids.put(bid, event.getType());
                    }
            	}
            }
        }

        genericDomains.clear();
        if (isValid) {
            for (Entry<String, String> genericDomainAssignment : ((ch.interlis.iox_j.StartBasketEvent) event).getDomains().entrySet()) {
                String genericDomainName = genericDomainAssignment.getKey();
                Element genericDomain = td.getElement(genericDomainName);
                if (!(genericDomain instanceof Domain)) {
                    errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateBasketEvent.genericDomainNotFound"), genericDomainName));
                    continue;
                }

                String concreteDomainName = genericDomainAssignment.getValue();
                Element concreteDomain = td.getElement(concreteDomainName);
                if (concreteDomain instanceof Domain) {
                    if (validateDeferredGeneric(model, topic, (Domain) genericDomain, (Domain) concreteDomain)) {
                        genericDomains.put(genericDomainName, (Domain) concreteDomain);
                    }
                } else {
                    errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateBasketEvent.concreteDomainNotFound"), concreteDomainName, genericDomainName));
                }
            }
        }
    }

    private static <T> boolean arrayContains(T[] array, T value) {
        for (T object : array) {
            if (object == value) {
                return true;
            }
        }
        return false;
    }

    private boolean validateDeferredGeneric(Model model, Topic topic, Domain genericDomain, Domain concreteDomain) {
        Type genericType = genericDomain.getType();
        if (!(genericType instanceof AbstractCoordType) || !((AbstractCoordType)genericType).isGeneric()) {
            errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateBasketEvent.domainNotGeneric"), genericDomain.toString()));
            return false;
        }
        if (!arrayContains(topic.getDefferedGenerics(), genericDomain)) {
            errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateBasketEvent.genericNotDeferred"), genericDomain.toString()));
            return false;
        }

        // resolve generic domain from contexts in model
        Domain[] resolved = model.resolveGenericDomain(genericDomain);
        if (resolved == null) {
            errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateCoordType.missingContext"), genericDomain.toString()));
            return false;
        }
        if (!arrayContains(resolved, concreteDomain)) {
            errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateCoordType.invalidDomain"), concreteDomain.toString(), genericDomain.toString()));
            return false;
        }
        return true;
    }

    private boolean isAValidBasketOID(Domain domain, String bid) {

        Type type = domain.getType();
        if (domain == td.INTERLIS.UUIDOID) {
            if (!isValidUuid(bid)) {
                return false;
            }
        } else if (domain == td.INTERLIS.STANDARDOID) {
            if (!isValidAnnexOid(bid)) {
                return false;
            }
        } else if (type instanceof TextOIDType) {
            if (!isValidTextOid(bid,((TextType)((TextOIDType)type).getOIDType()).getMaxLength())) {
                return false;
            }
        }
        return true;
    }
    private boolean isValidTopicName(String topicName) {
	    Element element = td.getElement(topicName);
        return element != null && element instanceof Topic ?  true : false;
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
    
    private boolean isValidAnnexOid(String valueStr) {
        if(valueStr==null) {
            return false;
        }
        
        if (valueStr.length() != 16) {
            return false;
        }
        
        Matcher matcher = patternForStandartOidValidation.matcher(valueStr);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }
    
    private boolean isValidTextOid(String valueStr,int maxLength) {
        if(valueStr==null) {
            return false;
        }
        int len=valueStr.length();
        if(len==0) {
            return false;
        }
        if(maxLength!=-1) {
            if(len>maxLength) {
                return false;
            }
        }
        Matcher matcher = patternForTextOIdValidation.matcher(valueStr);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public void doSecondPass() {
        if(!singlePass) {
            errs.addEvent(errFact.logInfoMsg(rsrc.getString("doSecondPass.secondValidationPass")));
            iterateThroughReferencedModels();
            String additionalModelsConfig = validationConfig.getConfigValue(ValidationConfig.PARAMETER, ValidationConfig.ADDITIONAL_MODELS);
            if (additionalModelsConfig != null) {
                String[] additionalModels = additionalModelsConfig.split(";");
                iterateThroughAdditionalModels(additionalModels);
            }
            iterateThroughAllObjects();
            validateAllAreas();
            validatePlausibilityConstraints();
        }
    }

    private void iterateThroughReferencedModels() {
        HashSet<String> iteratedModels = new HashSet<String>();
        for (String modelName : seenModels) {
            Element modelElement = td.getElement(modelName);
            if (modelElement instanceof DataModel) {
                DataModel model = (DataModel) modelElement;
                iterateThroughReferencedModel(model, iteratedModels);
            }
        }
    }

    private void iterateThroughReferencedModel(DataModel model, HashSet<String> iteratedModels) {
        if (iteratedModels.contains(model.getName())) {
            return;
        }
        iteratedModels.add(model.getName());

        Iterator<Element> modelIterator = model.iterator();
        while (modelIterator.hasNext()) {
            Element modelEntry = modelIterator.next();
            if (!(modelEntry instanceof Topic)) {
                continue;
            }
            Topic topic = (Topic) modelEntry;

            Iterator<Element> topicIterator = topic.iterator();
            while (topicIterator.hasNext()) {
                Element topicEntry = topicIterator.next();
                if (topicEntry instanceof Projection) {
                    Projection view = (Projection) topicEntry;
                    collectProjectionConstraints(view);
                }
            }
        }

        for (Model importedModel : model.getImporting()) {
            if (importedModel instanceof DataModel) {
                iterateThroughReferencedModel((DataModel) importedModel, iteratedModels);
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
			if(seenModels.contains(additionalModel)) {
			    continue;
			}
			errs.addEvent(errFact.logInfoMsg(rsrc.getString("iterateThroughAdditionalModels.additionalModel"), additionalModel));
			seenModels.add(additionalModel);
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
				logMsg(additionalModel, rsrc.getString("iterateThroughAdditionalModels.requiredAdditionalModelNotFound"), additionalModel);
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
				collectProjectionConstraints(view);
			}
		}
	}

	private void collectProjectionConstraints(Projection view) {
		if(view.getSelected().getAliasing()==null){
			return;
		}
		// constraint
		Iterator iteratorOfViewConstraints=view.iterator();
		while (iteratorOfViewConstraints.hasNext()){
			Object constraintObj = iteratorOfViewConstraints.next();
			if(!(constraintObj instanceof Constraint)){
				continue;
			}
			Constraint constraint = (Constraint) constraintObj;
			String constraintName = getScopedName(constraint);
			String checkConstraint=null;
			if(!enforceConstraintValidation){
				checkConstraint=validationConfig.getConfigValue(constraintName, ValidationConfig.CHECK);
			}
			if(ValidationConfig.OFF.equals(checkConstraint)){
				if(!configOffOufputReduction.contains(ValidationConfig.CHECK+":"+constraintName)){
					configOffOufputReduction.add(ValidationConfig.CHECK+":"+constraintName);
					errs.addEvent(errFact.logInfoMsg(rsrc.getString("collectAdditionalConstraints.validationConfigurationCheckOff"), constraintName));
				}
			}else{
				additionalConstraints.put(constraint, view);
			}
		}
	}

	private void validateAllAreas() {
		setCurrentMainObj(null);
		for(AttributeDef attr:areaAttrs.keySet()){
			ItfAreaPolygon2Linetable allLines=areaAttrs.get(attr);
			Boolean surfaceTopologiesValid = areaAttrsAreSurfaceTopologiesValid.get(attr);
			if (surfaceTopologiesValid == null || surfaceTopologiesValid) {
				errs.addEvent(errFact.logInfoMsg(rsrc.getString("validateAllAreas.validateAREA"), getScopedName(attr)));

				AbstractSurfaceOrAreaType type = (AbstractSurfaceOrAreaType)attr.getDomainResolvingAliases();
				double maxOverlap = type.getMaxOverlap() == null ? 0.0 : type.getMaxOverlap().doubleValue();

                List<IoxInvalidDataException> intersections=null;
		        if(legacyAreAreas) {
	                intersections=allLines.validate0(maxOverlap);
		        }else {
	                intersections=allLines.validate1(maxOverlap);
		        }
				if(intersections!=null && !intersections.isEmpty()){
					for(IoxInvalidDataException ex:intersections){ // iterate through non-overlay intersections
						String tid1=ex.getTid();
						String iliqname=ex.getIliqname();
						errFact.setTid(tid1);
						errFact.setIliqname(iliqname);
						//logMsg(areaOverlapValidation,"intersection tid1 "+is.getCurve1().getUserData()+", tid2 "+is.getCurve2().getUserData()+", coord "+is.getPt()[0].toString()+(is.getPt().length==2?(", coord2 "+is.getPt()[1].toString()):""));
						if(ex instanceof IoxIntersectionException) {
							IoxIntersectionException intersectionEx = ((IoxIntersectionException) ex);
							logMsg(areaOverlapValidation, intersectionEx);
							EhiLogger.traceState(intersectionEx.toString());
						}else {
							logMsg(areaOverlapValidation, ex.getMessage());
						}
					}
					setCurrentMainObj(null);
					logMsg(areaOverlapValidation,rsrc.getString("validateAllAreas.failedToValidateAREA"), getScopedName(attr));
				}
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

	/**
	 * Get the string that represents/identifies the specified constraint in log messages.
	 */
	private String getDisplayName(Constraint cnstr) {
		String scopedContainerName = cnstr.getContainer().getScopedName(null);
		String constraintName = cnstr.getName();

		if (isVerbose) {
			return String.format("%s.%s (%s)", scopedContainerName, constraintName, getConditionString(cnstr));
		} else {
			return String.format("%s.%s", scopedContainerName, constraintName);
		}
	}

	private String getConditionString(Constraint constraint) {
		StringWriter stringWriter = new StringWriter();
		Interlis2Generator generator = Interlis2Generator.generateElements(stringWriter, td);
		generator.printConstraint(constraint, true);
		String constraintDefinition = stringWriter.toString();

		return constraintDefinition.replaceAll("\\s+", " ").trim();
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
			updateCurrentBasket(basketId);
			while (objectIterator.hasNext()){
				IomObject iomObj = objectIterator.next();
				if(iomObj!=null){
					setCurrentMainObj(iomObj);
					errFact.setDefaultCoord(getDefaultCoord(iomObj));
					Object modelElement=tag2class.get(iomObj.getobjecttag());
					Viewable classOfCurrentObj= (Viewable) modelElement;
					if(!ValidationConfig.OFF.equals(constraintValidation)){
						// additional constraint
						for (Map.Entry<Constraint, Projection> additionalConstraintsEntry : additionalConstraints.entrySet()) {
							Constraint additionalConstraint = additionalConstraintsEntry.getKey();
							Projection view = additionalConstraintsEntry.getValue();
							Viewable<?> classOfAdditionalConstraint = view.getSelected().getAliasing();
							if (classOfCurrentObj.isExtending(classOfAdditionalConstraint) && viewIncludesObject(view, iomObj)) {
								if(additionalConstraint instanceof ExistenceConstraint) {
									ExistenceConstraint existenceConstraint = (ExistenceConstraint) additionalConstraint;
									validateExistenceConstraint(iomObj, existenceConstraint);
								} else if(additionalConstraint instanceof MandatoryConstraint){
									MandatoryConstraint mandatoryConstraint = (MandatoryConstraint) additionalConstraint;
									validateMandatoryConstraint(null, iomObj, mandatoryConstraint, null);
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
											errs.addEvent(errFact.logInfoMsg(rsrc.getString("iterateThroughAllObjects.validationConfigurationCheckOff"), constraintName, iomObj.getobjectoid()));
										}
									} else {
										collectSetConstraintObj(checkAdditionalConstraint, constraintName, iomObj, setConstraint);
									}
								} else if(additionalConstraint instanceof UniquenessConstraint){
									UniquenessConstraint uniquenessConstraint = (UniquenessConstraint) additionalConstraint; // uniquenessConstraint not null.
					                String iomObjOid=iomObj.getobjectoid();
					                if(iomObjOid==null && classOfCurrentObj instanceof AssociationDef) {
					                    iomObjOid=ObjectPool.getAssociationId(iomObj, (AssociationDef) classOfCurrentObj);
					                }
									validateUniquenessConstraint(null,iomObj, iomObjOid,uniquenessConstraint, null);
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
											errs.addEvent(errFact.logInfoMsg(rsrc.getString("iterateThroughAllObjects.validationConfigurationCheckOff"), constraintName, iomObj.getobjectoid()));
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
									if(structValue==null) {
									    // invalid: structAttributeName element without a nested structure element
									    // but already reported in validateAttrValue()
									}else {
	                                    validateReferenceAttrs(attr.getScopedName(),structValue, structure, objectBid);
									}
								}
							}
						}else if(objA.obj instanceof RoleDef){
							RoleDef roleDef = (RoleDef) objA.obj;
							validateRoleReference(basketId,roleDef, iomObj);
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
			for(SetConstraint setConstraint:setConstraints.keySet()){
				if(setConstraint.perBasket()){
					validateSetConstraint(setConstraint);
				}
			}
		}
		for(SetConstraint setConstraint:setConstraints.keySet()){
			if(!setConstraint.perBasket()) {
				validateSetConstraint(setConstraint);
			}
		}
	}

	private boolean viewIncludesObject(Projection view, IomObject iomObj) {
		String viewName = getScopedName(view);
		Iterator<?> viewIterator = view.iterator();
		while (viewIterator.hasNext()) {
			Object viewEntry = viewIterator.next();
			if (viewEntry instanceof ExpressionSelection) {
				ExpressionSelection selection = (ExpressionSelection) viewEntry;
				Evaluable whereExpression = selection.getCondition();
				Value result = evaluateExpression(null, null, viewName, iomObj, whereExpression, null);
				if (!result.skipEvaluation() && !result.isTrue()) {
					return false;
				}
			}
		}
		return true;
	}

	private void updateCurrentBasket(String basketId) {
		currentBasketId = basketId;
		for (UniquenessConstraint uniquenessConstraint: seenUniqueConstraintValues.keySet()) {
			if (uniquenessConstraint.perBasket()){
				seenUniqueConstraintValues.get(uniquenessConstraint).clear();
			}
		}

		for (SetConstraint setConstraint: setConstraints.keySet()){
			if(setConstraint.perBasket() && setConstraints.get(setConstraint) != null){
				setConstraints.get(setConstraint).clear();
			}
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
						validateMandatoryConstraint(null, iomObj, mandatoryConstraint, null);
					}
                    // Uniqueness constraint
                    if (constraintObj instanceof UniquenessConstraint) {
                        String iomObjOid = iomObj.getobjectoid();
                        if (iomObjOid == null && classOfIomObj instanceof AssociationDef) {
                            iomObjOid = ObjectPool.getAssociationId(iomObj, (AssociationDef) classOfIomObj);
                        }
                        UniquenessConstraint uniquenessConstraint = (UniquenessConstraint) constraintObj;
                        validateUniquenessConstraint(null, iomObj, iomObjOid, uniquenessConstraint, null);
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
								errs.addEvent(errFact.logInfoMsg(rsrc.getString("validateConstraints.validationConfigurationCheckOff"), constraintName, iomObj.getobjectoid()));
							}
						} else {
							collectSetConstraintObj(checkSetConstraint, constraintName, iomObj, setConstraint);
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
								errs.addEvent(errFact.logInfoMsg(rsrc.getString("validateConstraints.validationConfigurationCheckOff"), constraintName, iomObj.getobjectoid()));
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
				// unique contraints
				} else if (obj.obj instanceof RoleDef) {
                    if (obj.embedded) {
                        validateConstraintsOfEmbeddedAssociation((RoleDef) obj.obj, iomObj);
                    }				    
				}
			}
		}
	}
	
    private void validateConstraintsOfEmbeddedAssociation(RoleDef role, IomObject iomObj) {
        int propc = iomObj.getattrvaluecount(role.getName());
        if (propc >= 1) {
            IomObject embeddedLinkObj = iomObj.getattrobj(role.getName(), 0);
            AssociationDef roleOwner = (AssociationDef) role.getContainer();
            Viewable classOfCurrentObj = roleOwner;
            if (classOfCurrentObj != null) {
                String iomObjOid = iomObj.getobjectoid();

                Iterator constraintIterator = classOfCurrentObj.iterator();
                while (constraintIterator.hasNext()) {
                    Object constraintObj = constraintIterator.next();
                    // role is unique?
                    if (constraintObj instanceof UniquenessConstraint) {
                        UniquenessConstraint uniquenessConstraint = (UniquenessConstraint) constraintObj;
                        validateUniquenessConstraint(iomObj, embeddedLinkObj, iomObjOid, uniquenessConstraint, role);
                    } else if (constraintObj instanceof MandatoryConstraint) {
                        MandatoryConstraint mandatoryConstraint=(MandatoryConstraint) constraintObj;
                        validateMandatoryConstraint(/*ParentObject ->*/iomObj, /*iomObj*/embeddedLinkObj, mandatoryConstraint, role);
                    }
                }
            }
        } 
    }
    private void fillOfPlausibilityConstraintMap(String validationKind, String usageScope, PlausibilityConstraint constraint, IomObject iomObj){
		Evaluable condition = (Evaluable) constraint.getCondition();
		Value conditionValue = evaluateExpression(null, validationKind, usageScope, iomObj, condition,null);
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
						errs.addEvent(errFact.logInfoMsg(rsrc.getString("validatePlausibilityConstraints.validationConfigurationCheckOff"), constraintName));
					}
				}else{
					if(!constraintOutputReduction.contains(constraint+":"+constraintName)){
						constraintOutputReduction.add(constraint+":"+constraintName);
						errs.addEvent(errFact.logInfoMsg(rsrc.getString("validatePlausibilityConstraints.validatePlausibilityConstraint"),getScopedName(constraintEntry.getKey())));
					}
                    String actualLanguage = Locale.getDefault().getLanguage();
                    String msg = validationConfig.getConfigValue(getScopedName(constraintEntry.getKey()), ValidationConfig.MSG+"_"+actualLanguage);
                    if (msg == null) {
                        msg=validationConfig.getConfigValue(getScopedName(constraintEntry.getKey()), ValidationConfig.MSG);
                    }

					if (msg != null && isVerbose) {
						msg = String.format("%s %s", msg, getDisplayName(constraint));
					}

					if(constraintEntry.getKey().getDirection()==PlausibilityConstraint.DIRECTION_AT_LEAST){ // >=
                        errs.addEvent(errFact.logInfoMsg(rsrc.getString("validatePlausibilityConstraints.calculatedValue"),">=",Double.toString(((constraintEntry.getValue().getSuccessfulResults()/constraintEntry.getValue().getTotalSumOfConstraints())*100)),Double.toString(constraintEntry.getKey().getPercentage())));                        
						if(((constraintEntry.getValue().getSuccessfulResults()/constraintEntry.getValue().getTotalSumOfConstraints())*100) >= constraintEntry.getKey().getPercentage()){
							// ok
						} else {
							if(msg!=null && msg.length()>0){
								logMsg(checkConstraint,msg);
							} else {
								logMsg(checkConstraint, rsrc.getString("validatePlausibilityConstraints.plausibilityConstraintIsNotTrue"), getDisplayName(constraintEntry.getKey()));
							}
						}
					} else if(constraintEntry.getKey().getDirection()==PlausibilityConstraint.DIRECTION_AT_MOST){ // <=
                        errs.addEvent(errFact.logInfoMsg(rsrc.getString("validatePlausibilityConstraints.calculatedValue"),"<=",Double.toString(((constraintEntry.getValue().getSuccessfulResults()/constraintEntry.getValue().getTotalSumOfConstraints())*100)),Double.toString(constraintEntry.getKey().getPercentage())));                        
						if(((constraintEntry.getValue().getSuccessfulResults()/constraintEntry.getValue().getTotalSumOfConstraints())*100) <= constraintEntry.getKey().getPercentage()){
							// ok
						} else {
							if(msg!=null && msg.length()>0){
								logMsg(checkConstraint,msg);
							} else {
								logMsg(checkConstraint,rsrc.getString("validatePlausibilityConstraints.plausibilityConstraintIsNotTrue"), getDisplayName(constraintEntry.getKey()));
							}
						}
					}
				}
			}
		}
	}
	
	private void validateUniquenessConstraint(IomObject parentObject,IomObject iomObj, String iomObjOid,UniquenessConstraint uniquenessConstraint, RoleDef role) {
		if(!ValidationConfig.OFF.equals(constraintValidation)){
			String constraintName = getScopedName(uniquenessConstraint);
			String checkUniqueConstraint=null;
			if(!enforceConstraintValidation) {
				checkUniqueConstraint = validationConfig.getConfigValue(constraintName, ValidationConfig.CHECK);
			}
			if(ValidationConfig.OFF.equals(checkUniqueConstraint)){
				if(!configOffOufputReduction.contains(ValidationConfig.CHECK+":"+constraintName)){
					configOffOufputReduction.add(ValidationConfig.CHECK+":"+constraintName);
					errs.addEvent(errFact.logInfoMsg(rsrc.getString("validateUniquenessConstraint.validationConfigurationCheckOff"), constraintName, iomObj.getobjectoid()));
				}
			}else{
				// preCondition (UNIQUE WHERE)
				if(uniquenessConstraint.getPreCondition()!=null){
					Value preConditionValue = evaluateExpression(parentObject, checkUniqueConstraint, constraintName, iomObj, uniquenessConstraint.getPreCondition(),role);
					if (preConditionValue.isNotYetImplemented()){
						errs.addEvent(errFact.logWarningMsg(rsrc.getString("validateUniquenessConstraint.functionInUniquenessConstraintIsNotYetImplemented"), constraintName));
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
					errs.addEvent(errFact.logInfoMsg(rsrc.getString("validateUniquenessConstraint.validateUniqeConstraint"),getScopedName(uniquenessConstraint)));
				}
			    HashMap<UniquenessConstraint, HashMap<AttributeArray, String>> seenValues = null;
			    if(uniquenessConstraint.getLocal()) {
		            seenValues= new HashMap<UniquenessConstraint, HashMap<AttributeArray, String>>();
			    }else {
		            seenValues=seenUniqueConstraintValues;
			    }

		        if(uniquenessConstraint.getPrefix()!=null){
		            PathEl[] attrPath = uniquenessConstraint.getPrefix().getPathElements();
		            visitStructEle(checkUniqueConstraint,uniquenessConstraint,seenValues,iomObjOid, aClass1,attrPath,0,parentObject,iomObj, role);
		        }else {
	                visitStructEle(checkUniqueConstraint,uniquenessConstraint,seenValues,iomObjOid, aClass1,null,0,parentObject,iomObj,role);
		        }
			}
		}
	}
	
	private void visitStructEle(String checkUniqueConstraint,UniquenessConstraint uniquenessConstraint, HashMap<UniquenessConstraint, HashMap<AttributeArray, String>> seenValues, String iomObjOid, Viewable iomObjClass, PathEl[] attrPath, int i, IomObject parentObject, IomObject iomObj,RoleDef role) {
	    if(attrPath==null || i>=attrPath.length) {
	        OutParam<AttributeArray> values = new OutParam<AttributeArray>();
            String oidOfObjectWithDuplicateValue = validateUnique(seenValues,iomObjOid,parentObject,iomObj,uniquenessConstraint, values, role);
            if (oidOfObjectWithDuplicateValue == null){
                // ok
            } else {
                String actualLanguage = Locale.getDefault().getLanguage();
                String msg = validationConfig.getConfigValue(getScopedName(uniquenessConstraint), ValidationConfig.MSG+"_"+actualLanguage);
                if (msg == null) {
                    msg=validationConfig.getConfigValue(getScopedName(uniquenessConstraint), ValidationConfig.MSG);
                }
                if(msg!=null && msg.length()>0){
                    if (isVerbose) {
                        msg = String.format("%s %s", msg, getDisplayName(uniquenessConstraint));
                    }
                    logMsg(checkUniqueConstraint,msg);
                } else {
                    logMsg(checkUniqueConstraint,rsrc.getString("visitStructEle.uniqueIsViolatedValuesAlreadyExistInObject"), getDisplayName(uniquenessConstraint), values.value.valuesAsString(), formatObjectId(oidOfObjectWithDuplicateValue,iomObjClass));
                }
            }
	        return;
	    }
        String attrName = attrPath[i].getName();
        int structElec=iomObj.getattrvaluecount(attrName);
        for(int structElei=0;structElei<structElec;structElei++) {
            IomObject structEle=iomObj.getattrobj(attrName, structElei);
            visitStructEle(checkUniqueConstraint,uniquenessConstraint, seenValues, iomObjOid, iomObjClass,attrPath,i+1,parentObject,structEle, role);
        }
    }
    private String formatObjectId(String oid,Viewable classOfOid) {
        String actualLanguage = Locale.getDefault().getLanguage();
        ArrayList<Viewable> classes=new ArrayList<Viewable>();
        classes.add(classOfOid);
        IomObject iomObj=objectPool.getObject(oid, classes, null);
        String keymsg = validationConfig.getConfigValue(iomObj.getobjecttag(), ValidationConfig.KEYMSG+"_"+actualLanguage);
        if (keymsg != null) {
            return LogEventFactory.formatMessage(keymsg, iomObj);
        } else {
            keymsg = validationConfig.getConfigValue(iomObj.getobjecttag(), ValidationConfig.KEYMSG);
            return keymsg != null ? LogEventFactory.formatMessage(keymsg, iomObj) : oid;
        }
    }

    private HashMap<SetConstraint,Collection<String>> setConstraints=new HashMap<SetConstraint,Collection<String>>();
	private Iterator<String> allObjIterator=null;
	
	private void collectSetConstraintObj(String validationKind, String constraintName, IomObject iomObj, SetConstraint setConstraint) {
		Evaluable preCondition = (Evaluable) setConstraint.getPreCondition();
        Collection<String> objs=setConstraints.get(setConstraint);
        if(objs==null){
            // mark set constraint as evaluable / might have objects
            objs=new HashSet<String>();
            setConstraints.put(setConstraint,objs);
        }
		if(preCondition != null){
			Value preConditionValue = evaluateExpression(null, validationKind, constraintName, iomObj, preCondition,null);
			if (preConditionValue.isNotYetImplemented()){
				errs.addEvent(errFact.logWarningMsg(rsrc.getString("collectSetConstraintObjs.functionInSetConstraintIsNotYetImplemented"), getScopedName(setConstraint)));
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
		String oid=iomObj.getobjectoid();
		if(oid==null) {
	        oid=ObjectPool.getAssociationId(iomObj, (AssociationDef)td.getElement(iomObj.getobjecttag()));
		}
		objs.add(oid);
	}
    private void collectSetConstraints(Topic topic) {
        while(topic!=null) {
            Iterator<Element> eleIt = topic.iterator();
            while(eleIt.hasNext()) {
                Element ele=eleIt.next();
                if(ele instanceof Viewable) {
                    Viewable view=(Viewable)ele;
                    Iterator cIt = view.iterator();
                    while(cIt.hasNext()) {
                        Object cObj = cIt.next();
                        if(cObj instanceof SetConstraint) {
                            SetConstraint setConstraint=(SetConstraint)cObj;
                            if(!setConstraints.containsKey(setConstraint)){
                                // mark set constraint as seen
                                setConstraints.put(setConstraint,null);
                            }
                        }
                    }
                }
            }
            topic=(Topic)topic.getExtending();
        }
    }
	
	private void validateSetConstraint(SetConstraint setConstraint) {
		if(!ValidationConfig.OFF.equals(constraintValidation)){
			Collection<String> objs=setConstraints.get(setConstraint);
			String constraintName = getScopedName(setConstraint);
			String checkConstraint = null;
			if(!enforceConstraintValidation){
				checkConstraint=validationConfig.getConfigValue(constraintName, ValidationConfig.CHECK);
			}
			if(ValidationConfig.OFF.equals(checkConstraint)){
				if(!configOffOufputReduction.contains(ValidationConfig.CHECK+":"+getScopedName(setConstraint))){
					configOffOufputReduction.add(ValidationConfig.CHECK+":"+getScopedName(setConstraint));
					errs.addEvent(errFact.logInfoMsg(rsrc.getString("validateSetConstraint.validationConfigurationCheckOff"), getScopedName(setConstraint)));
				}
			}else{
				if(!constraintOutputReduction.contains(setConstraint+":"+constraintName)){
					constraintOutputReduction.add(setConstraint+":"+constraintName);
					errs.addEvent(errFact.logInfoMsg(rsrc.getString("validateSetConstraint.validateSetConstraint"),getScopedName(setConstraint)));
				}
                Evaluable preCondition = (Evaluable) setConstraint.getPreCondition();
                if(preCondition!=null) {
                }else {
                    if(objs==null) {
                        objs=new HashSet<String>();
                    }
                }
                Iterator<String> objIt=null;
                if(objs==null){
                    objIt=new HashSet<String>().iterator();
                }else {
                    objIt=objs.iterator();
                }
                String oid=null;
                while(true){
                    if(objIt.hasNext()) {
                        oid=objIt.next();
                    }
                    if(objs==null){
                        allObjIterator=null;
                    }else {
                        allObjIterator=objs.iterator();
                    }
                    IomObject iomObj=null;
                    if(oid!=null) {
                        iomObj=objectPool.getObject(oid, null, null);
                        setCurrentMainObj(iomObj);
                        errFact.setDefaultCoord(getDefaultCoord(iomObj));
                    }
                    Evaluable condition = (Evaluable) setConstraint.getCondition();
                    Value constraintValue = evaluateExpression(null, checkConstraint, constraintName, iomObj, condition,null);
                    if (constraintValue.isNotYetImplemented()){
                        errs.addEvent(errFact.logWarningMsg(rsrc.getString("validateSetConstraint.functionInSetConstraintIsNotYetImplemented"), getScopedName(setConstraint)));
                        return;
                    }
                    if (constraintValue.skipEvaluation()){
                        return;
                    }
                    if (constraintValue.isTrue()){
                        // ok
                    } else {
                        String actualLanguage = Locale.getDefault().getLanguage();
                        String msg = validationConfig.getConfigValue(getScopedName(setConstraint), ValidationConfig.MSG+"_"+actualLanguage);
                        if (msg == null) {
                            msg=validationConfig.getConfigValue(getScopedName(setConstraint), ValidationConfig.MSG);
                        }
                        if(msg!=null && msg.length()>0){
                            if (isVerbose) {
                                msg = String.format("%s %s", msg, getDisplayName(setConstraint));
                            }
                            logMsg(checkConstraint,msg);
                        } else {
                            String constraintIdentifier = setConstraint+":"+constraintName+(setConstraint.perBasket() ? ":Basket("+currentBasketId+")" : "");
                            if(!setConstraintOufputReduction.contains(constraintIdentifier)){
                                setConstraintOufputReduction.add(constraintIdentifier);
                                logMsg(checkConstraint,rsrc.getString("validateSetConstraint.setConstraintIsNotTrue"), getDisplayName(setConstraint));
                            }
                        }
                    }
                    if(!objIt.hasNext()) {
                        break;
                    }
                }
			}
		}
	}
	
	private void validateMandatoryConstraint(IomObject parentObject, IomObject iomObj, MandatoryConstraint mandatoryConstraintObj,RoleDef firstRole) {
		if(!ValidationConfig.OFF.equals(constraintValidation)){
			String constraintName = getScopedName(mandatoryConstraintObj);
			String checkConstraint = null;
			if(!enforceConstraintValidation){
				checkConstraint=validationConfig.getConfigValue(constraintName, ValidationConfig.CHECK);
			}
			if(ValidationConfig.OFF.equals(checkConstraint)){
				if(!configOffOufputReduction.contains(ValidationConfig.CHECK+":"+constraintName)){
					configOffOufputReduction.add(ValidationConfig.CHECK+":"+constraintName);
					errs.addEvent(errFact.logInfoMsg(rsrc.getString("validateMandatoryConstraint.validationConfigurationCheckOff"), constraintName));
				}
			} else {
				if(!constraintOutputReduction.contains(mandatoryConstraintObj+":"+constraintName)){
					constraintOutputReduction.add(mandatoryConstraintObj+":"+constraintName);
					errs.addEvent(errFact.logInfoMsg(rsrc.getString("validateMandatoryConstraint.validateMandatoryConstraint"),getScopedName(mandatoryConstraintObj)));
				}
				Evaluable condition = (Evaluable) mandatoryConstraintObj.getCondition();
				Value conditionValue = evaluateExpression(parentObject, checkConstraint, constraintName, iomObj, condition,firstRole);
				if (!conditionValue.isNotYetImplemented()){
					if (!conditionValue.skipEvaluation()){
						if (conditionValue.isTrue()){
							// ok
						} else {
						    String actualLanguage = Locale.getDefault().getLanguage();
						    String msg = validationConfig.getConfigValue(constraintName, ValidationConfig.MSG+"_"+actualLanguage);
						    if (msg == null) {
						        msg=validationConfig.getConfigValue(constraintName, ValidationConfig.MSG);
						    }
							if(msg!=null && msg.length()>0){
								if (isVerbose) {
									msg = String.format("%s %s", msg, getDisplayName(mandatoryConstraintObj));
								}
								logMsg(checkConstraint,msg);
							} else {
								logMsg(checkConstraint,rsrc.getString("validateMandatoryConstraint.mandatoryConstraintIsNotTrue"), getDisplayName(mandatoryConstraintObj));
							}
						}
					}
				} else {
					if(condition instanceof FunctionCall){
						FunctionCall functionCallObj = (FunctionCall) condition;
						Function function = functionCallObj.getFunction();
						errs.addEvent(errFact.logWarningMsg(rsrc.getString("validateMandatoryConstraint.functionIsNotYetImplemented"), function.getScopedName(null)));
						Value.createNotYetImplemented();
					} else {
						errs.addEvent(errFact.logWarningMsg(rsrc.getString("validateMandatoryConstraint.mandatoryConstraintIsNotYetImplemented"), mandatoryConstraintObj.getScopedName(null), iomObj.getobjecttag()));
						Value.createNotYetImplemented();
					}
				}
			}
		}
	}

    private Value evaluateExpressionToSingleValue(IomObject parentObject, String validationKind, String usageScope, IomObject iomObj, Evaluable expression,RoleDef firstRole) {
        Value value=evaluateExpression(parentObject, validationKind, usageScope, iomObj, expression,firstRole);
        if(!value.skipEvaluation()) {
            if( value.getValues()!=null && value.getValues().length>1) {
                errs.addEvent(errFact.logErrorMsg(rsrc.getString("evaluateExpressionToSingleValue.expressionMustEvaluateToASingleValue"), expression.toString()));
                return Value.createSkipEvaluation();
            }
        }
        return value;
    }
	public Value evaluateExpression(IomObject parentObject, String validationKind, String usageScope, IomObject iomObj, Evaluable expression,RoleDef firstRole) {
		TextType texttype = new TextType();
		if(expression instanceof Equality){
			// ==
			Equality equality = (Equality) expression;
			Evaluable leftExpression = (Evaluable) equality.getLeft();
			Evaluable rightExpression = (Evaluable) equality.getRight();
			Value leftValue=evaluateExpressionToSingleValue(parentObject, validationKind, usageScope, iomObj,leftExpression,firstRole);
			// if isError, return error.
			if (leftValue.skipEvaluation()){
				return leftValue;
			}
			if (leftValue.isUndefined()){
				return Value.createSkipEvaluation();
			}
			
			Value rightValue=evaluateExpressionToSingleValue(parentObject, validationKind, usageScope, iomObj,rightExpression,firstRole);
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
			Value leftValue=evaluateExpression(parentObject, validationKind, usageScope, iomObj,leftExpression, firstRole);
			// if isError, return error.
			if (leftValue.skipEvaluation()){
				return leftValue;
			}
			if (leftValue.isUndefined()){
				return Value.createSkipEvaluation();
			}
			Value rightValue=evaluateExpression(parentObject, validationKind, usageScope, iomObj,rightExpression, firstRole);
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
			Value leftValue=evaluateExpression(parentObject, validationKind, usageScope, iomObj,leftExpression, firstRole);
			// if isError, return error.
			if (leftValue.skipEvaluation()){
				return leftValue;
			}
			if (leftValue.isUndefined()){
				return Value.createSkipEvaluation();
			}
			Value rightValue=evaluateExpression(parentObject, validationKind, usageScope, iomObj,rightExpression, firstRole);
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
			Value leftValue=evaluateExpression(parentObject, validationKind, usageScope, iomObj,leftExpression, firstRole);
			// if isError, return error.
			if (leftValue.skipEvaluation()){
				return leftValue;
			}
			if (leftValue.isUndefined()){
				return Value.createSkipEvaluation();
			}
			Value rightValue=evaluateExpression(parentObject, validationKind, usageScope, iomObj,rightExpression, firstRole);
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
			Value leftValue=evaluateExpression(parentObject, validationKind, usageScope, iomObj,leftExpression, firstRole);
			// if isError, return error.
			if (leftValue.skipEvaluation()){
				return leftValue;
			}
			if (leftValue.isUndefined()){
				return Value.createSkipEvaluation();
			}
			Value rightValue=evaluateExpression(parentObject, validationKind, usageScope, iomObj,rightExpression, firstRole);
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
			Value leftValue=evaluateExpression(parentObject, validationKind, usageScope, iomObj,leftExpression, firstRole);
			// if isError, return error.
			if (leftValue.skipEvaluation()){
				return leftValue;
			}
			if (leftValue.isUndefined()){
				return Value.createSkipEvaluation();
			}
			Value rightValue=evaluateExpression(parentObject, validationKind, usageScope, iomObj,rightExpression, firstRole);
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
			Value arg=evaluateExpression(parentObject, validationKind, usageScope, iomObj,negation.getNegated(), firstRole);
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
        } else if(expression instanceof Expression.Subexpression){
            // ()
            Expression.Subexpression negation = (Expression.Subexpression) expression;              
            Value arg=evaluateExpression(parentObject, validationKind, usageScope, iomObj,negation.getSubexpression(), firstRole);
            if (arg.skipEvaluation()){
                return arg;
            }
            if (arg.isUndefined()){
                return Value.createSkipEvaluation();
            }
            return arg;
		} else if(expression instanceof Conjunction){
			// AND
			Conjunction conjunction = (Conjunction) expression;
			Evaluable[] conjunctionArray = (Evaluable[]) conjunction.getConjoined();
			for (int i=0;i<conjunctionArray.length;i++){
				Value arg=evaluateExpression(parentObject, validationKind, usageScope, iomObj,conjunctionArray[i], firstRole);
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
			Value arg=evaluateExpression(parentObject, validationKind, usageScope, iomObj,defined.getArgument(), firstRole);
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
				Value arg=evaluateExpression(parentObject, validationKind, usageScope, iomObj,disjunctionArray[i], firstRole);
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
		} else if(expression instanceof Expression.Implication) {
			Expression.Implication implication = (Expression.Implication) expression;
			Value leftValue = evaluateExpression(parentObject, validationKind, usageScope, iomObj, implication.getLeft(), firstRole);
			Value rightValue = Value.createSkipEvaluation();
			if (!leftValue.skipEvaluation() && !leftValue.isUndefined() && leftValue.isTrue()) {
				rightValue = evaluateExpression(parentObject, validationKind, usageScope, iomObj, implication.getRight(), firstRole);
			}

			if (leftValue.skipEvaluation() || rightValue.skipEvaluation()) {
				return rightValue;
			}
			if (leftValue.isUndefined() || rightValue.isUndefined()) {
				return Value.createUndefined();
			}
			return new Value(!leftValue.isTrue() || (leftValue.isTrue() && rightValue.isTrue()));
		} else if (expression instanceof Expression.Addition) {
			Expression.Addition addition = (Expression.Addition) expression;
			Value leftValue = evaluateExpression(parentObject, validationKind, usageScope, iomObj, addition.getLeft(), firstRole);
			Value rightValue = evaluateExpression(parentObject, validationKind, usageScope, iomObj, addition.getRight(), firstRole);
			if (leftValue.skipEvaluation() || rightValue.skipEvaluation())
				return Value.createSkipEvaluation();
			if (leftValue.isUndefined() || rightValue.isUndefined())
				return Value.createUndefined();

			return new Value(leftValue.getNumeric() + rightValue.getNumeric());
		} else if (expression instanceof Expression.Subtraction) {
			Expression.Subtraction subtraction = (Expression.Subtraction) expression;
			Value leftValue = evaluateExpression(parentObject, validationKind, usageScope, iomObj, subtraction.getLeft(), firstRole);
			Value rightValue = evaluateExpression(parentObject, validationKind, usageScope, iomObj, subtraction.getRight(), firstRole);
			if (leftValue.skipEvaluation() || rightValue.skipEvaluation())
				return Value.createSkipEvaluation();
			if (leftValue.isUndefined() || rightValue.isUndefined())
				return Value.createUndefined();

			return new Value(leftValue.getNumeric() - rightValue.getNumeric());
		} else if (expression instanceof Expression.Multiplication) {
			Expression.Multiplication multiplication = (Expression.Multiplication) expression;
			Value leftValue = evaluateExpression(parentObject, validationKind, usageScope, iomObj, multiplication.getLeft(), firstRole);
			Value rightValue = evaluateExpression(parentObject, validationKind, usageScope, iomObj, multiplication.getRight(), firstRole);
			if (leftValue.skipEvaluation() || rightValue.skipEvaluation())
				return Value.createSkipEvaluation();
			if (leftValue.isUndefined() || rightValue.isUndefined())
				return Value.createUndefined();

			return new Value(leftValue.getNumeric() * rightValue.getNumeric());
		} else if (expression instanceof Expression.Division) {
			Expression.Division division = (Expression.Division) expression;
			Value leftValue = evaluateExpression(parentObject, validationKind, usageScope, iomObj, division.getLeft(), firstRole);
			Value rightValue = evaluateExpression(parentObject, validationKind, usageScope, iomObj, division.getRight(), firstRole);
			if (leftValue.skipEvaluation() || rightValue.skipEvaluation())
				return Value.createSkipEvaluation();
			if (leftValue.isUndefined() || rightValue.isUndefined())
				return Value.createUndefined();

			return new Value(leftValue.getNumeric() / rightValue.getNumeric());
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
			final String funcName = currentFunction.getScopedName(null);
            if (funcName.startsWith(Text.TEXT+".") || funcName.startsWith(Text.TEXT_V2+".")) {
			    if (textFunction == null) {
			        textFunction = new Text(this, td, validationConfig);    
			    }
			    
			    return textFunction.evaluateFunction(currentFunction, functionCallObj, parentObject, validationKind, usageScope, iomObj, texttype, firstRole);
			} else if (funcName.startsWith(Math.MATH+".") || funcName.startsWith(Math.MATH_V2+".")) {
			    if(mathFunction == null) {
	                mathFunction = new Math(this, td, validationConfig);
			    }
			    
			    return mathFunction.evaluateFunction(currentFunction, functionCallObj, parentObject,
			            validationKind, usageScope, iomObj, texttype, firstRole);
            } else if (funcName.startsWith("MinimalRuntimeSystem01.")) {
                if(rtsFunction == null) {
                    rtsFunction = new MinimalRuntimeSystem(this, td, validationConfig);
                }
                
                return rtsFunction.evaluateFunction(currentFunction, functionCallObj, parentObject,
                        validationKind, usageScope, iomObj, texttype, firstRole);
			} else if (!funcName.equals("INTERLIS.convertUnit") && 
			        funcName.startsWith("INTERLIS.")) {
			    if (interlisFunction == null) {
			        interlisFunction = new Interlis(this, td, validationConfig);
			    }
			    
			    return interlisFunction.evaluateFunction(currentFunction, functionCallObj, parentObject,
			            validationKind, usageScope, iomObj, texttype, expression, functions, td, firstRole);
			} else if (funcName.startsWith("INTERLIS_ext.")) {
			    if (interlis_ext == null) {
			        interlis_ext = new Interlis_ext(this, td, validationConfig);
			    }
			    
			    return interlis_ext.evaluateFunction(currentFunction, parentObject, validationKind, usageScope, iomObj, expression, functions, td, firstRole);
            } else if (funcName.startsWith("DMAVTYM_Topologie_V1_0.")) {
                if (dmavtymTopologie == null) {
                    dmavtymTopologie = new DmavtymTopologie(this, td, validationConfig, errFact);
                }

                return dmavtymTopologie.evaluateFunction(currentFunction, functionCallObj, parentObject,
                        validationKind, usageScope, iomObj, texttype, firstRole);
			} else {
				String functionQname=funcName;
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
					Value anObject=evaluateExpression(parentObject, validationKind, usageScope, iomObj,arguments[i], firstRole);
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
				functionTarget.init(td,settings,validationConfig, objectPool, errFact);
				return functionTarget.evaluate(validationKind, usageScope, iomObj, actualArguments);
			}
			//TODO INTERLIS.convertUnit
			//TODO instance of InspectionFactor
			//TODO instance of LengthOfReferencedText
		} else if(expression instanceof ObjectPath) {
			// object path	
			ObjectPath objectPathObj = (ObjectPath) expression;
			PathEl[] pathElements = objectPathObj.getPathElements();

			return getValueFromObjectPath(parentObject, iomObj, pathElements, firstRole);
		} else if(expression instanceof Objects) {
			// objects
            if(allObjIterator==null){
                //throw new IllegalStateException(rsrc.getString("evaluateExpression.argumentAllRequiresASetConstraint"));
                return Value.createSkipEvaluation();
           }

            // return cached value if available
            if (functions.containsKey(expression)) {
                return functions.get(expression);
            }

			Iterator<String> objectIterator = allObjIterator;
			List<IomObject> listOfIomObjects = new ArrayList<IomObject>();
			while(objectIterator.hasNext()){
				String oid=objectIterator.next();
				IomObject aIomObj = objectPool.getObject(oid, null, null);
				if(aIomObj!=null){
					listOfIomObjects.add(aIomObj);
				}
			}
            Value listOfIomObjectsValue = new Value(listOfIomObjects);
            functions.put(expression, listOfIomObjectsValue);
            return listOfIomObjectsValue;
        } else if (expression instanceof ValueRefThis) {
            Domain domain = (Domain) td.getElement(iomObj.getobjecttag());
            String value = iomObj.getattrvalue(VALUE_REF_THIS);
            return new Value(domain.getType(), value);
        } else if(expression instanceof ParameterValue) {
            ParameterValue paramValue=(ParameterValue)expression;
            String paramName=paramValue.getParameter().getScopedName();
            String value=(String)td.getActualRuntimeParameter(paramName);
            if(value!=null) {
                return new Value(texttype,value);
            }
            return Value.createUndefined();
		} else {
			errs.addEvent(errFact.logWarningMsg(rsrc.getString("evaluateExpression.ExpressionIsNotYetImplemented"), expression.toString()));
		}
		return Value.createSkipEvaluation(); // skip further evaluation
		//TODO instance of ViewableAggregate
		//TODO instance of ViewableAlias
	}
	
	public ObjectPath parseObjectOrAttributePath(Viewable viewable, String objectPath) throws Ili2cException {
        return Ili23Parser.parseObjectOrAttributePath(td,viewable, objectPath);
	}
	
    public Value getValueFromObjectPath(IomObject parentObject,IomObject iomObjStart, PathEl[] pathElements, RoleDef firstRole) {
        if(iomObjStart==null) {
            return Value.createSkipEvaluation();
        }
        ArrayList<IomObject> currentObjects=new ArrayList<IomObject>();
        ArrayList<IomObject> nextCurrentObjects=new ArrayList<IomObject>();
        RoleDef role = null;
        final int lastPathIndex = pathElements.length - 1;
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
                        throw new IllegalStateException(rsrc.getString("getValueFromObjectPath.roleIsNotBeEmpty"));
                    } else {
                        // IF embedded association and first PathEl of objectpath
                        if(parentObject!=null && k==0) {
                            // IF last PathEl of objectpath
                            if(k==lastPathIndex) {
                                // THEN return embedded reference object
                                if(role==firstRole) {
                                    nextCurrentObjects.add(iomObj);
                                }else {
                                    // create/return a reference object to parent object
                                    Iom_jObject ref = new Iom_jObject("REF", null);
                                    ref.setobjectrefoid(parentObject.getobjectoid());
                                    nextCurrentObjects.add(ref);
                                }
                            }else {
                                if(role==firstRole) {
                                    IomObject targetObj = getReferencedObject(role, iomObj.getobjectrefoid());
                                    nextCurrentObjects.add(targetObj);
                                }else {
                                    nextCurrentObjects.add(parentObject);
                                }
                            }
                        }else{
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
                                    objects = getTargetObjectsOfReverseRole(role, normalizeOid(srcObjClass,iomObj.getobjectoid()));
                                    if (objects != null) {
                                        nextCurrentObjects.addAll(objects);
                                    } 
                                    continue;
                                    
                                }else {
                                    objects = getLinkObjects(role, normalizeOid(srcObjClass,iomObj.getobjectoid()));
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
                                IomObject targetRefObj = getReferencedObject(role, targetOid);
                                if (targetRefObj != null) {
                                    if (role instanceof RoleDef) {
                                        return Value.createOidValue(targetRefObj.getobjectoid());
                                    } else {
                                        objects.add(targetRefObj);
                                        nextCurrentObjects.addAll(objects);                                        
                                    }
  
                                } else if (roleDefValue != null) {
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
                    if (iomObj == null) {
                        return Value.createUndefined();
                    }
                    int attrCount = iomObj.getattrvaluecount(currentAttrName);
                    if (attrCount == 0) {
                        return Value.createUndefined(); 
                    } else {
                        // not the last pathEl?
                        if (k!=lastPathIndex) {
                            for (int i = 0; i < attrCount; i++) {
                                IomObject iomObjTmp = iomObj.getattrobj(currentAttrName,i);
                                nextCurrentObjects.add(iomObjTmp);                                
                            }
                        }else {
                            String attrValue = iomObj.getattrvalue(currentAttrName);
                            if (attrValue != null) {
                                if(attrRef.getAttr().isDomainIliUuid()) {
                                    Type aliasedType = ((TypeAlias) type).getAliasing().getType();
                                    return new Value(aliasedType, normalizeUUID(attrValue));
                                }else if (attrValue.equals("true")) {
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
                                if (currentObjects.size() == 1) {
                                    return new Value(type, attrValue);
                                } else {
                                    String[] attrValues = new String[currentObjects.size()];
                                    int counter = 0;
                                    for (IomObject value : currentObjects) {
                                        attrValue = value.getattrvalue(currentAttrName);
                                        if (attrValue != null) {
                                            attrValues[counter] = attrValue;
                                            counter++;
                                        }
                                    }
                                    if (attrValues != null) {
                                        return new Value(type, attrValues);
                                    }
                                }
                                
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
                } else if (currentPathEl instanceof PathElThis) {
                    nextCurrentObjects.addAll(currentObjects);
                } else if (currentPathEl instanceof PathElBase) {
                    Viewable<?> viewable = ((PathElBase) currentPathEl).getCurrentViewable();
                    if (viewable instanceof Projection) {
                        ViewableAlias alias = ((Projection) viewable).getSelected();
                        if (alias.getName().equals(currentPathEl.getName())) {
                            nextCurrentObjects.addAll(currentObjects);
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
    private String normalizeOid(Viewable modelEle, String oid) {
        if(oid==null) {
            return null;
        }
        if(modelEle instanceof AbstractClassDef) {
            Domain oidType=((AbstractClassDef) modelEle).getOid();
            if(oidType==PredefinedModel.getInstance().UUIDOID) {
                oid=Validator.normalizeUUID(oid);
            }
        }
        return oid;
    }
    public static String normalizeUUID(String attrValue) {
        if(attrValue==null) {
            return null;
        }
        return attrValue.toLowerCase();
    }
    private boolean isBackward(Viewable srcObjClass, RoleDef role) {
        AssociationDef assoc=(AssociationDef) role.getContainer();
        if(assoc.isLightweight()) {
            // if reference is embedded in srcObj then is it a forward
            if (role.isAssociationEmbedded() && role.getOppEnd().getDestination()==srcObjClass) {
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
    private boolean legacyAreAreas=false;
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
                    throw new IllegalStateException(rsrc.getString("getIomObjWithIndex.thereIsNoRecordFoundForThisIndex"));
                }
            } else {
                throw new IllegalStateException(rsrc.getString("getIomObjWithIndex.thereIsNoRecordFoundForThisIndex"));
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
    public Value evaluateObjectCount(Value value) {
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

	/**
	 * @deprecated
	 * Use {@link #evaluateAreArea(IomObject, Value, PathEl[], PathEl[], Function, String)} instead.
	 */
	@Deprecated
	public Value evaluateAreArea(IomObject mainIomObj, Value value, PathEl[] pathToStructEle, PathEl[] pathToSurfaceAttr, Function currentFunction) {
		return evaluateAreArea(mainIomObj, value, pathToStructEle, pathToSurfaceAttr, currentFunction, null);
	}

    public Value evaluateAreArea(IomObject mainIomObj, Value value, PathEl[] pathToStructEle, PathEl[] pathToSurfaceAttr, Function currentFunction, String validationKind) {
        if(legacyAreAreas) {
            return evaluateAreArea0(mainIomObj, value, pathToStructEle, pathToSurfaceAttr, currentFunction, validationKind);
        }
        return evaluateAreArea1(mainIomObj, value, pathToStructEle, pathToSurfaceAttr, currentFunction, validationKind);
    }
    public Value evaluateAreArea1(IomObject mainIomObj, Value value, PathEl[] pathToStructEle, PathEl[] pathToSurfaceAttr, Function currentFunction, String validationKind) {
        PathEl[] fullPath;
        if (pathToStructEle == null) {
            fullPath = pathToSurfaceAttr;
        } else {
            fullPath = Arrays.copyOf(pathToStructEle, pathToStructEle.length + pathToSurfaceAttr.length);
            System.arraycopy(pathToSurfaceAttr, 0, fullPath, pathToStructEle.length, pathToSurfaceAttr.length);
        }

        Collection<IomObject> inputObjects = new ArrayList<IomObject>();
        if (value.getViewable() == null) {
            inputObjects = value.getComplexObjects();
        } else {
            Iterator<IomObject> iterator = objectPool.getObjectsOfBasketId(currentBasketId).valueIterator();
            while (iterator.hasNext()) {
                IomObject iomObj = iterator.next();
                Viewable iomObjClass = (Viewable) tag2class.get(iomObj.getobjecttag());
                if (value.getViewable().equals(iomObjClass)) {
                    inputObjects.add(iomObj);
                }
            }
        }

        List<IomObject> listOfPolygons = new ArrayList<IomObject>();
        for (IomObject obj : inputObjects) {
            getStructElesFromAttrPath(fullPath, obj.getobjectoid(), listOfPolygons, obj, 0);
        }

        String mainObjTag = mainIomObj.getobjecttag();
        ItfAreaPolygon2Linetable polygonPool = new ItfAreaPolygon2Linetable(mainObjTag, objPoolManager); // create new pool of polygons
        for (IomObject polygon : listOfPolygons) {
            try {
                polygonPool.addPolygon(null, polygon.getobjectoid(), polygon, validationKind, errFact);
            } catch (IoxException e) {
                EhiLogger.logError(e);
                return new Value(false); // when the input polygon causes an exception, areAreas is false
            } catch (IllegalArgumentException e) {
                EhiLogger.logError(e);
                return new Value(false); // when the input polygon causes an exception, areAreas is false
            }
        }

        List<IoxInvalidDataException> intersections = polygonPool.validate1(0.0);
        if (intersections != null && !intersections.isEmpty()) {
            if (!disableAreAreasMessages) {
                for (IoxInvalidDataException ex : intersections) {
                    String tid1 = ex.getTid();
                    String iliqname = ex.getIliqname();
                    errFact.setTid(tid1);
                    errFact.setIliqname(iliqname);
                    if (ex instanceof IoxIntersectionException) {
                        IoxIntersectionException intersectionEx = ((IoxIntersectionException) ex);
                        logMsg(areaOverlapValidation, intersectionEx);
                        EhiLogger.traceState(intersectionEx.toString());
                    } else {
                        logMsg(areaOverlapValidation, ex.getMessage());
                    }
                }
                setCurrentMainObj(null);
            }

            // short circuit; no need to further evaluate
            EhiLogger.traceState(mainObjTag + ":" + currentFunction.getScopedName(null) + " returned false");
            return new Value(false);
        }

        return new Value(true);
    }
    public Value evaluateAreArea0(IomObject mainIomObj, Value value, PathEl[] pathToStructEle, PathEl[] pathToSurfaceAttr, Function currentFunction, String validationKind) {
        String mainObjTag=mainIomObj.getobjecttag();
        if(pathToStructEle == null){
            ItfAreaPolygon2Linetable polygonPool = new ItfAreaPolygon2Linetable(mainObjTag, objPoolManager); // create new pool of polygons
            ArrayList<IomObject> listOfPolygons = new ArrayList<IomObject>();
            if(value.getViewable()!=null){
                Iterator objectIterator = objectPool.getObjectsOfBasketId(currentBasketId).valueIterator();
                while(objectIterator.hasNext()){
                    IomObject iomObj = (IomObject) objectIterator.next();
                    Viewable iomObjClass = (Viewable) tag2class.get(iomObj.getobjecttag());
                    if(value.getViewable().equals(iomObjClass)){
                        getStructElesFromAttrPath(pathToSurfaceAttr, iomObj.getobjectoid(),listOfPolygons, iomObj, 0);
                    }
                }
                // if objects.equals(anObjectClass) never equal, handling.
            } else {
                Iterator iterIomObjects = value.getComplexObjects().iterator(); 
                while(iterIomObjects.hasNext()){
                    IomObject iomObj = (IomObject) iterIomObjects.next();
                    getStructElesFromAttrPath(pathToSurfaceAttr, iomObj.getobjectoid(),listOfPolygons, iomObj, 0);
                }
            }
            for (IomObject polygon : listOfPolygons) {
                try {
                    polygonPool.addPolygon(null, polygon.getobjectoid(), polygon, validationKind, errFact);
                } catch (IoxException e) {
                    EhiLogger.logError(e);  
                }
            }
            List<IoxInvalidDataException> intersections=polygonPool.validate0(0.0);
            if(intersections!=null){
                if(!disableAreAreasMessages && intersections.size()>0){
                    for(IoxInvalidDataException ex:intersections){ // iterate through non-overlay intersections
                        String tid1=ex.getTid();
                        String iliqname=ex.getIliqname();
                        errFact.setTid(tid1);
                        errFact.setIliqname(iliqname);
                        if(ex instanceof IoxIntersectionException) {
                            IoxIntersectionException intersectionEx = ((IoxIntersectionException) ex);
                            logMsg(areaOverlapValidation, intersectionEx);
                            EhiLogger.traceState(intersectionEx.toString());
                        }else {
                            logMsg(areaOverlapValidation, ex.getMessage());
                        }
                    }
                    setCurrentMainObj(null);
                }
                EhiLogger.traceState(mainObjTag+ ":" + currentFunction.getScopedName(null) + " returned false"); 
                // not a valid area topology
                return new Value(false); 
            }
            // valid areas
            return new Value(true); 
        } else {
            // ASSERT: pathToStructEle is defined
            ItfAreaPolygon2Linetable polygonPool = new ItfAreaPolygon2Linetable(mainObjTag, objPoolManager);
            ArrayList<IomObject> listOfPolygons = new ArrayList<IomObject>();
            Iterator objectIterator=null;
            Viewable classCriteria=null;
            if(value.getViewable()!=null){
                classCriteria=value.getViewable();
                objectIterator = objectPool.getObjectsOfBasketId(currentBasketId).valueIterator();
            }else {
                objectIterator = value.getComplexObjects().iterator();
            }
            boolean returnValue=true;
            while(objectIterator.hasNext()){
                IomObject iomObj = (IomObject) objectIterator.next();
                Viewable iomObjClass = (Viewable)tag2class.get(iomObj.getobjecttag());
                if(classCriteria==null || classCriteria.equals(iomObjClass)){
                    ArrayList<IomObject> complexObjects = new ArrayList<IomObject>();
                    getStructElesFromAttrPath(pathToStructEle, iomObj.getobjectoid(),complexObjects, iomObj, 0);
                    //Value currentValue = getValueFromObjectPath(null, iomObj, pathToStructEle, null);
                    if (complexObjects.size()>0) {
                        //Collection<IomObject> complexObjects = currentValue.getComplexObjects();
                        for (IomObject currentObj : complexObjects) {
                            getStructElesFromAttrPath(pathToSurfaceAttr, currentObj.getobjectoid(), listOfPolygons, currentObj, 0);
                        }
                    }
                }
            }

            for (IomObject polygon : listOfPolygons) {
                try {
                    polygonPool.addPolygon(null, polygon.getobjectoid(), polygon, validationKind, errFact);
                } catch (IoxException e) {
                    EhiLogger.logError(e);
                }
            }

            List<IoxInvalidDataException> intersections=polygonPool.validate0(0.0);
            if(intersections!=null) {
                if(!disableAreAreasMessages && intersections.size()>0){
                    for(IoxInvalidDataException ex:intersections){ // iterate through non-overlay intersections
                        String tid1=ex.getTid();
                        String iliqname=ex.getIliqname();
                        errFact.setTid(tid1);
                        errFact.setIliqname(iliqname);
                        if(ex instanceof IoxIntersectionException) {
                            IoxIntersectionException intersectionEx = ((IoxIntersectionException) ex);
                            logMsg(areaOverlapValidation, intersectionEx);
                            EhiLogger.traceState(intersectionEx.toString());
                        }else {
                            logMsg(areaOverlapValidation, ex.getMessage());
                        }
                    }
                    setCurrentMainObj(null);
                }

                // short circuit; no need to further evaluate
                EhiLogger.traceState(mainObjTag+ ":" + currentFunction.getScopedName(null) + " returned false");
                return new Value(false);
            }

            return new Value(true);
        }
    }

    private void getStructElesFromAttrPath(PathEl[] attrPath, String oidPrefix,Collection<IomObject> listOfFoundStructEles, IomObject iomObj, int currentPathElIdx) {
        if (attrPath != null && currentPathElIdx < attrPath.length) {
            if(oidPrefix==null) {
                oidPrefix="";
            }else if(oidPrefix.length()>0) {
                oidPrefix=oidPrefix+"/";
            }
            int lastPathElIdx = attrPath.length - 1;
            PathEl currentPathEl = attrPath[currentPathElIdx];
            String attrName=currentPathEl.getName();
            int valueCount = iomObj.getattrvaluecount(attrName);
            for (int valuei = 0; valuei < valueCount; valuei++) {
                //  in einem Interlis ObjectPath hat das erste Element den Index 1
                String oid=oidPrefix+attrName+"["+(valuei+1)+"]";
                IomObject structEle = iomObj.getattrobj(attrName, valuei);
                if (currentPathElIdx == lastPathElIdx) {
                    // surface attr found, add polylines to polygonPool.
                    structEle.setobjectoid(oid);
                    listOfFoundStructEles.add(structEle);
                } else {
                    getStructElesFromAttrPath(attrPath, oid,listOfFoundStructEles, structEle, currentPathElIdx+1);
                }
            }
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
				errs.addEvent(errFact.logInfoMsg(rsrc.getString("validateRoleCardinality.validationConfigurationMultiplicityOff"), roleQName));
			}
		}else{
			if(!configOffOufputReduction.contains(ValidationConfig.MULTIPLICITY+":"+roleQName)){
				configOffOufputReduction.add(ValidationConfig.MULTIPLICITY+":"+roleQName);
				errs.addEvent(errFact.logInfoMsg(rsrc.getString("validateRoleCardinality.validateMultiplicityOfRole"),roleQName));
			}
			long nrOfTargetObjs=linkPool.getTargetObjectCount(iomObj,role,doItfOidPerTable);
			long cardMin=role.getCardinality().getMinimum();
			long cardMax=role.getCardinality().getMaximum();
			if((nrOfTargetObjs>=cardMin && nrOfTargetObjs<=cardMax)){
				// is valid
			} else {
				if(role.getCardinality().getMaximum() == Cardinality.UNBOUND){
					String cardMaxStr = "*";
					logMsg(multiplicity, rsrc.getString("validateRoleCardinality.shouldAssociateToTargetObject"), role.getName(), String.valueOf(cardMin), cardMaxStr, String.valueOf(nrOfTargetObjs));
				} else {
					logMsg(multiplicity, rsrc.getString("validateRoleCardinality.shouldAssociateToTargetObject"), role.getName(), String.valueOf(cardMin), String.valueOf(cardMax), String.valueOf(nrOfTargetObjs));
				}
			}
		}
	}

	private void validateRoleReference(String bidOfObj,RoleDef role, IomObject iomObj){
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
				errs.addEvent(errFact.logInfoMsg(rsrc.getString("validateRoleReference.validationConfigurationTargetOff"), roleQName));
			}
		}else{
			if(!datatypesOutputReduction.contains(roleQName)){
				datatypesOutputReduction.add(roleQName);
				errs.addEvent(errFact.logInfoMsg(rsrc.getString("validateRoleReference.validateTargetOfRole"),roleQName));
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
				for(Viewable roleDestinationClass : getTranslations((Viewable) targetClassIterator.next())) {
	                destinationClasses.add(roleDestinationClass);
	                possibleTargetClasses.append(sep);
	                possibleTargetClasses.append(roleDestinationClass.getScopedName(null));
	                sep=",";
				}
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
			if(!role.isExternal()){
				if(targetOid!=null){
					if(isBasketSame(bidOfObj, targetObj)){
						if(targetObj!=null){
							// target object exists.
						} else {
							// no object with this oid found
							logMsg(validateTarget, rsrc.getString("validateRoleReference.noObjectFoundWithOid"), targetOid);
							return;
						}
					} else {
						// no object with this oid found
						logMsg(validateTarget, rsrc.getString("validateRoleReference.noObjectFoundWithOidInBasket"), targetOid,bidOfObj);
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
						logMsg(validateTarget, rsrc.getString("validateRoleReference.noObjectFoundWithOid"), targetOid);
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
				logMsg(validateTarget, rsrc.getString("validateRoleReference.wrongClassOfTargetObjectForRole"), getScopedName(targetObjClass),targetOid, getScopedName(role));
				return;
			}
		}
	}
	
	private List<Viewable> getTranslations(Viewable aclass0) {
        Viewable rootClass=(Viewable)getRootTranslation(aclass0);
	    List<Viewable> ret=new ArrayList<Viewable>();
	    ret.add(rootClass);
	    Model rootModel=(Model)getRootTranslation(rootClass.getContainer(Model.class));
        Topic rootTopic=(Topic)getRootTranslation(rootClass.getContainer(Topic.class));
	    for(Iterator<Model> it=td.iterator();it.hasNext();) {
	        Model destModel=it.next();
	        if(isTranslatedBy(rootModel,destModel)) {
	            for(Iterator<Element> eleIt=destModel.iterator();eleIt.hasNext();) {
	                Element destEle=eleIt.next();
	                if(destEle instanceof Viewable && isTranslatedBy(rootClass,destEle)) {
	                    if(destEle!=rootClass)ret.add((Viewable)destEle); // one candidate found
	                }else if(destEle instanceof Topic && rootTopic!=null && isTranslatedBy(destEle,rootTopic)){
	                    for(Iterator<Element> classIt=((Topic)destEle).iterator();classIt.hasNext();) {
	                        Element destClass=classIt.next();
	                        if(destClass instanceof Viewable && isTranslatedBy(destClass,rootClass)) {
	                            if(destClass!=rootClass)ret.add((Viewable)destClass);
	                            break; // one candidate found
	                        }
	                    }
	                }
	            }
	        }
	    }
        return ret;
    }
	private boolean isTranslatedBy(Element ele1,Element ele2) {
	    
        Element root1=getRootTranslation(ele1);
        Element root2=getRootTranslation(ele2);
        return root1.equals(root2);
	}
    private Element getRootTranslation(Element ele0) {
        if(ele0==null) {
            return null;
        }
        while(true) {
            Element ele1=ele0.getTranslationOf();
            if(ele1==null) {
                return ele0;
            }
            ele0=ele1;
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
	                        reader = new ReaderFactory().createReader(file,errFact);
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
							errs.addEvent(errFact.logInfoMsg(rsrc.getString("validateReferenceAttrs.validationConfigurationTargetOff"), attrQName));
						}
					}else{
						if(!datatypesOutputReduction.contains(attrQName)){
							datatypesOutputReduction.add(attrQName);
							errs.addEvent(errFact.logInfoMsg(rsrc.getString("validateReferenceAttrs.validateReferenceAttr"),attrQName));
						}

                        IomObject refAttrStruct = iomStruct.getattrobj(refAttr.getName(), 0);
                        String targetOid = null;
                        if(refAttrStruct!=null){
                            targetOid=refAttrStruct.getobjectrefoid();
                        }

			            Iterator<AbstractClassDef> targetClassIterator = refAttrType.iteratorRestrictedTo();
			            if(!targetClassIterator.hasNext()) {
	                        AbstractClassDef targetClass = refAttrType.getReferred();
			                List<AbstractClassDef> refs=new ArrayList<AbstractClassDef>();
			                refs.add(targetClass);
			                targetClassIterator=refs.iterator();
			            }
			            ArrayList<Viewable> destinationClasses = new ArrayList<Viewable>();
			            StringBuffer possibleTargetClasses=new StringBuffer();
			            // find target classes.
			            while(targetClassIterator.hasNext()){
			                for(Viewable roleDestinationClass : getTranslations((Viewable) targetClassIterator.next())) {
			                    destinationClasses.add(roleDestinationClass);
			                }
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
						// not EXTERNAL?
						if(!refAttrType.isExternal()){
							if(targetOid!=null){
								if(targetObject!=null){
									if(isBasketSame(bidOfObj, targetObject)){
										// target object exists in correct basket
									} else {
										// no object with this oid found
										logMsg(validateTarget, rsrc.getString("validateReferenceAttrs.noObjectFoundWithOidInBasket"), targetOid,bidOfObj);
										return;
									}
								}else{
									// no object with this oid found
									logMsg(validateTarget, rsrc.getString("validateReferenceAttrs.noObjectFoundWithOid"), targetOid);
									return;
								}
							}
						} else {
							// EXTERNAL
							// not found in internal pool?
							if(targetObject==null){
							    if(targetOid!=null) {
	                                boolean extObjFound=false;
	                                // use external object resolver to find external objects.
	                                if(extObjResolvers!=null){
	                                    // call custom function to verify in external data pools
	                                    for(ExternalObjectResolver extObjResolver:extObjResolvers){
	                                        if(extObjResolver.objectExists(targetOid, destinationClasses)){
	                                            extObjFound = true;
	                                            break;
	                                        }
	                                    }
	                                }
	                                if(allObjectsAccessible && !extObjFound){
	                                    logMsg(validateTarget,rsrc.getString("validateReferenceAttrs.noObjectFoundWithOid"), targetOid);
	                                }
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
							Iterator<Viewable> refAttrRestrictionIter = destinationClasses.iterator();
							while (refAttrRestrictionIter.hasNext()){
								Viewable targetClass = refAttrRestrictionIter.next();;
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
                            logMsg(validateTarget, rsrc.getString("validateReferenceAttrs.wrongClassOfTargetObjectForReferenceAttr"), getScopedName(targetObjClass),targetOid, getScopedName(refAttr));
						}
					}
				}else if(type instanceof CompositionType) {
				    String attrName=refAttr.getName();
                    int structc=iomStruct.getattrvaluecount(attrName);
                    for(int structi=0;structi<structc;structi++){
                       IomObject structValue=iomStruct.getattrobj(attrName, structi);
                       if(structValue==null) {
                           // invalid: structAttributeName element without a nested structure element
                           // but already reported in validateAttrValue()
                       }else {
                           validateReferenceAttrs(refAttr.getScopedName(),structValue, ((CompositionType) type).getComponentType(), bidOfObj);
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
					errs.addEvent(errFact.logInfoMsg(rsrc.getString("validateExistenceConstraint.validationConfigurationCheckOff"), constraintName));
				}
			} else {
				if (iomObj.getattrcount() == 0){
					return;
				}
				if(!constraintOutputReduction.contains(existenceConstraint+":"+constraintName)){
					constraintOutputReduction.add(existenceConstraint+":"+constraintName);
					errs.addEvent(errFact.logInfoMsg(rsrc.getString("validateExistenceConstraint.validateExistenceConstraint"), getScopedName(existenceConstraint)));
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
                    String actualLanguage = Locale.getDefault().getLanguage();
                    String msg = validationConfig.getConfigValue(constraintName, ValidationConfig.MSG+"_"+actualLanguage);
                    if (msg == null) {
                        msg=validationConfig.getConfigValue(constraintName, ValidationConfig.MSG);
                    }
					if(msg!=null && msg.length()>0){
						if (isVerbose) {
							msg = String.format("%s %s", msg, getDisplayName(existenceConstraint));
						}
						logMsg(checkConstraint,msg);
					} else {
						logMsg(checkConstraint, rsrc.getString("validateExistenceConstraint.valueOfTheAttributeWasNotFoundInTheConditionClass"), getDisplayName(existenceConstraint), restrictedAttrName.toString(), iomObj.getobjecttag().toString());
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

	// HashMap of global unique constraints.
	HashMap<UniquenessConstraint, HashMap<AttributeArray, String>> seenUniqueConstraintValues = new HashMap<UniquenessConstraint, HashMap<AttributeArray, String>>();
	// List of all object Oid's and associated classPath's of uniqueness validate of Oid's.
	Map<String , String> uniqueObjectIDs = new HashMap<String, String>();
	HashSet<Object> loggedObjects=new HashSet<Object>();
    private boolean disableRounding=false;
    private boolean disableAreAreasMessages=false;
    private Interlis interlisFunction=null;
    private Interlis_ext interlis_ext=null;
    private Math mathFunction=null;
    private Text textFunction=null;
    private MinimalRuntimeSystem rtsFunction=null;
    private DmavtymTopologie dmavtymTopologie=null;
	
	private void validateObject(IomObject iomObj,String attrPath,Viewable assocClass) throws IoxException {
		// validate if object is null
		boolean isObject = attrPath==null;
		if(isObject){
			setCurrentMainObj(iomObj);
			objectCount++;
		}else {
		    structCount++;
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
				errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateObject.unknownClass"),tag));
			}
			return;
		}
		
        // validate that OID is not a BID
        if(isObject) {
            String objectoid=iomObj.getobjectoid();
            if (objectoid != null && !objectoid.equals("")) {
                if(modelele instanceof AbstractClassDef) {
                    Domain oidType=((AbstractClassDef) modelele).getOid();
                    if(oidType==td.INTERLIS.UUIDOID) {
                        objectoid=normalizeUUID(objectoid);
                    }
                }
                if (uniquenessOfBid.containsKey(objectoid)) {
                    errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateObject.oidIsEqualToABid"), objectoid));
                }           
            }
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
		}
        // validate that object is instance of a concrete class
        if(aclass1.isAbstract()){
            errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateObject.objectMustBeANonAbstractClass")));
        }

        // association
        if (aclass1 instanceof AssociationDef){
            AssociationDef modelAssociationDef = (AssociationDef) aclass1;
            Domain oidType=((AbstractClassDef) modelAssociationDef).getOid();
            String oid = iomObj.getobjectoid();
            if(isObject && modelAssociationDef.isLightweight()) {
                errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateObject.linkMustBeEmbedded"), iomObj.getobjecttag()));
                addToPool = false;
            }else if (modelAssociationDef.isIdentifiable() || oidType!=null){
                if (oid == null){
                    errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateObject.AssociationHasToHaveAnOid"), iomObj.getobjecttag()));
                    addToPool = false;
                }else if (!isValidId(oid)) {
                    errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateObject.valueIsNotAValidOid"), oid));                 
                }
            }else {
                if(oid!=null) {
                    errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateObject.associationHasNotToHaveAnOid"), iomObj.getobjecttag(),oid));
                }else {
                    if(isObject) {
                        // fix current oid
                        currentMainOid=ObjectPool.getAssociationId(iomObj, (AssociationDef)modelAssociationDef);
                    }
                }
            }
        } else if (aclass1 instanceof Table){
            Table classValueTable = (Table) aclass1;
            if (classValueTable.isIdentifiable()) {
                // class
                Domain oidType=((AbstractClassDef) aclass1).getOid();
                String oid = iomObj.getobjectoid();
                if (oid == null) {
                    errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateObject.classHasToHaveAnOid"), iomObj.getobjecttag()));
                    addToPool = false;
                } else if (!isValidId(oid) && oidType == null) {
                    errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateObject.valueIsNotAValidOid"), oid));
                }
            } else {
                // structure
                addToPool = false;
                String structId=iomObj.getobjectoid();
                if(structId!=null){
                    if(isObject) {
                        errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateObject.structureHasNotToHaveAnOid"), iomObj.getobjecttag()));
                    }else {
                        errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateObject.structAttrMustNotHaveAnOid"),attrPath,tag,structId));
                    }
                }
            }
        }
        
        
        if(isObject){
			
			if(aclass1 instanceof AbstractClassDef){
				Domain oidType=((AbstractClassDef) aclass1).getOid();
                String oid=iomObj.getobjectoid();
				if(oidType!=null && oidType==td.INTERLIS.UUIDOID){
					if(oid != null && !isValidUuid(oid)){
						errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateObject.tidIsNotAValidUuid"), oid));
					}
				} else if(oidType!=null && oidType==td.INTERLIS.I32OID){
					// valid i32OID.
				} else if(oidType!=null && oidType==td.INTERLIS.STANDARDOID) {
                    if (!isValidAnnexOid(oid)) {
                        errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateObject.valueIsNotAValidOid"), oid));
                    }
				} else if (oidType!=null && oidType.getType() instanceof TextOIDType) {
                    if (!isValidTextOid(oid,((TextType)((TextOIDType) oidType.getType()).getOIDType()).getMaxLength())) {
                        errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateObject.valueIsNotAValidOid"), oid));
                    }
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
						validateAttrValue(aclass1,iomObj,attr,attrPath);
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
                                errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateObject.roleRequiresOnlyOneReferenceProperty"),role.getScopedName()));
                            }
                            if(refoid==null) {
                                errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateObject.roleRequiresAReferenceToAnotherObject"),role.getScopedName()));
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
                            errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateObject.roleRequiresAReferenceToAnotherObject"),role.getScopedName()));
                        }
                    }
                    if (refoid != null) {
                        if(!singlePass) {
                            linkPool.addLink(iomObj,role,refoid,doItfOidPerTable);
                        }
                    }
                }
			}
		}
		
		if(isObject){
			if(addToPool){
				if(!singlePass){
					// check if object id is unique in transferfile
					IomObject duplicateObj = objectPool.addObject(iomObj,currentBasketId);
					if(duplicateObj!=null){
						Viewable aclass= (Viewable) tag2class.get(duplicateObj.getobjecttag());
						errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateObject.oidXOfObjectYAlreadyExistsInZ"), currentMainOid, iomObj.getobjecttag(), aclass.getScopedName()));
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
					errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateObject.unknownPropertyX"),propName));
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
	
	private String validateUnique(HashMap<UniquenessConstraint, HashMap<AttributeArray, String>> seenValues,String originObjOid,IomObject parentObject,IomObject currentObject,UniquenessConstraint constraint, OutParam<AttributeArray> valuesRet, RoleDef role) {
        ArrayList<Object> values = new ArrayList<Object>();
		Iterator constraintIter = constraint.getElements().iteratorAttribute();
		while(constraintIter.hasNext()){
		    Object next = constraintIter.next();
		    ObjectPath objectPathObj = (ObjectPath) next;
		    PathEl[] pathElements = objectPathObj.getPathElements();
		    
		    Value value = getValueFromObjectPath(parentObject, currentObject, pathElements, role);
		    if(value.isUndefined()) {
		        return null;
		    }else if(value.skipEvaluation()) {
		        return null;
		    }else if(value.getValue() != null) {
			    values.add(value.getValue());
			} else if (value.getOid() != null) { 
			    values.add(value.getOid());
			}else if (value.getComplexObjects() != null) {
			    IomObject complexValue = value.getComplexObjects().iterator().next();
			    if(complexValue.getobjectrefoid() != null) {
			        values.add(complexValue.getobjectrefoid());
			    } else {
			        values.add(complexValue);
			    }
			}else {
			    throw new IllegalStateException(rsrc.getString("validateUnique.unexpectedValueInUnique"));
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
		Type attrType = attr.getDomain();
		Domain domain = attrType instanceof TypeAlias ? ((TypeAlias)attrType).getAliasing() : null;
		Model model = (Model)attr.getContainer(Model.class);
		Type type = attr.getDomainResolvingAll();
		if (type instanceof CompositionType){
			 int structc=iomObj.getattrvaluecount(attrName);
				if(ValidationConfig.OFF.equals(validateMultiplicity)){
					if(!configOffOufputReduction.contains(ValidationConfig.MULTIPLICITY+":"+attrQName)){
						configOffOufputReduction.add(ValidationConfig.MULTIPLICITY+":"+attrQName);
						errs.addEvent(errFact.logInfoMsg(rsrc.getString("validateAttrValue.XNotValidatedValidationConfigurationMultiplicityOffInAttributeY"), attrQName, attrPath, iomObj.getobjecttag(), iomObj.getobjectoid()));
					}
				}else{
					 Cardinality card = ((CompositionType)type).getCardinality();
					 if(structc<card.getMinimum() || structc>card.getMaximum()){
						logMsg(validateMultiplicity, rsrc.getString("validateAttrValue.attributeXHasWrongNumberOfValues"), attrPath);
					 }
				}
			 for(int structi=0;structi<structc;structi++){
				 IomObject structEle=iomObj.getattrobj(attrName, structi);
					if(ValidationConfig.OFF.equals(validateType)){
						if(!configOffOufputReduction.contains(ValidationConfig.TYPE+":"+attrQName)){
							configOffOufputReduction.add(ValidationConfig.TYPE+":"+attrQName);
							errs.addEvent(errFact.logInfoMsg(rsrc.getString("validateAttrValue.XNotValidatedValidationConfigurationTypeOffInAttributeY"), attrQName, attrPath, iomObj.getobjecttag(), iomObj.getobjectoid()));
						}
					}else if(structEle==null) {
							 logMsg(validateType, rsrc.getString("validateAttrValue.attributeXRequiresAStructureY"), attrPath,((CompositionType)type).getComponentType().getScopedName(null));
					}else {
						String tag=structEle.getobjecttag();
						Object modelele=tag2class.get(tag);
						if(modelele==null){
							if(!unknownTypev.contains(tag)){
								errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateAttrValue.unknownClassXInAttributeY"),tag, attrPath));
							}
						}else{
							Viewable structEleClass=(Viewable) modelele;
							Table requiredClass=((CompositionType)type).getComponentType();
							if(!structEleClass.isExtending(requiredClass)){
								 logMsg(validateType, rsrc.getString("validateAttrValue.attributeXRequiresAStructureY"), attrPath,requiredClass.getScopedName(null));
							}else {
							    // subType in valid context
							    //same Model?
							    Model structEleModel=(Model)structEleClass.getContainer(Model.class);
							    Model requiredClassModel=(Model)requiredClass.getContainer(Model.class);
							    if(structEleModel==requiredClassModel) {
							        // ok, usable subtype
							    }else {
                                    Topic requiredClassTopic=(Topic)requiredClass.getContainer(Topic.class);
                                    if(requiredClassTopic==null) {
                                        // struct at model level, no base topics
                                        logMsg(validateType, rsrc.getString("validateAttrValue.attributeXRequiresAStructureY"), attrPath,requiredClass.getScopedName(null));
                                    }else {
                                        // if extended topic
                                        requiredClassTopic=(Topic)requiredClassTopic.getExtending();
                                        while(requiredClassTopic!=null) {
                                              // model of base topic
                                            requiredClassModel=(Model)requiredClassTopic.getContainer(Model.class);
                                            if(structEleModel==requiredClassModel) {
                                                // valid subtype found
                                                break;
                                            }
                                        }
                                        if(structEleModel!=requiredClassModel) {
                                            logMsg(validateType, rsrc.getString("validateAttrValue.attributeXRequiresAStructureY"), attrPath,requiredClass.getScopedName(null));
                                        }
                                    }
							    }
							}
							if(structEleClass.isAbstract()){
								// validate that object is instance of concrete class
								 logMsg(validateType, rsrc.getString("validateAttrValue.attributeXRequiresANonAbstractStructure"), attrPath);
							}
						}
						validateObject(structEle, attrPath+"["+structi+"]",null);
					}
			 }
		}else{
            int structc=iomObj.getattrvaluecount(attrName);
			if(ValidationConfig.OFF.equals(validateMultiplicity)){
				if(!configOffOufputReduction.contains(ValidationConfig.MULTIPLICITY+":"+attrQName)){
					configOffOufputReduction.add(ValidationConfig.MULTIPLICITY+":"+attrQName);
					errs.addEvent(errFact.logInfoMsg(rsrc.getString("validateAttrValue.XNotValidatedValidationConfigurationMultiplicityOffInAttributeY"), attrQName, attrPath, iomObj.getobjecttag(), iomObj.getobjectoid()));
				}
			}else{
				Object topologyDone=pipelinePool.getIntermediateValue(attr, ValidationConfig.TOPOLOGY);
				if(topologyDone==null){
					 if(structc==1 && type instanceof ReferenceType) {
					     IomObject refObj=iomObj.getattrobj(attrName, 0);
					     if(refObj==null || refObj.getobjectrefoid()==null) {
					         structc=0;
					     }
					 }
                     if(doItfLineTables && type instanceof SurfaceType){
                         // SURFACE; no attrValue in maintable
                     }else{
                         Cardinality card = getCardinality(attr);
                         if(structc<card.getMinimum() || structc>card.getMaximum()){
                             if(card.getMaximum()>1 || structc>1) {
                                 logMsg(validateMultiplicity, rsrc.getString("validateAttrValue.attributeXHasWrongNumberOfValues"), attrPath);
                             }else {
                                 logMsg(validateMultiplicity, rsrc.getString("validateAttrValue.attributeXRequiresAValue"), attrPath);
                             }
                         }
                     }
				}else{
					Boolean topologyValidationOk=(Boolean)pipelinePool.getIntermediateValue(attr, ValidationConfig.TOPOLOGY_VALIDATION_OK);
					if(topologyValidationOk==null || topologyValidationOk){
						 if(structc==0 && isAttributeMandatory(attr)) {
							 logMsg(validateMultiplicity,rsrc.getString("validateAttrValue.attributeXRequiresAValue"), attrPath);
						 }
					}else{
						// topology validation failed
						// ignore missing values
					}
				}
			}
            for(int structi=0;structi<structc;structi++){
                if(ValidationConfig.OFF.equals(validateType)){
                    if(!configOffOufputReduction.contains(ValidationConfig.TYPE+":"+attrQName)){
                        configOffOufputReduction.add(ValidationConfig.TYPE+":"+attrQName);
                        errs.addEvent(errFact.logInfoMsg(rsrc.getString("validateAttrValue.XNotValidatedValidationConfigurationTypeOffInAttributeY"), attrQName, attrPath, iomObj.getobjecttag(), iomObj.getobjectoid()));
                    }
                }else{
                    if (attr.isDomainIli1Date()) {
                        String valueStr=iomObj.getattrprim(attrName, structi);
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
                                    logMsg(validateType, rsrc.getString("validateAttrValue.valueXIsNotInRangeInAttributeY"), valueStr, attrPath);
                                }
                            } catch (NumberFormatException numberformatexception) {
                                logMsg(validateType, rsrc.getString("validateAttrValue.valueXIsNotAValidDateInAttributeY"), valueStr, attrPath);
                            }
                        } else {
                            logMsg(validateType, rsrc.getString("validateAttrValue.valueXIsNotAValidDateInAttributeY"), valueStr, attrPath);
                        }
                    } else if (attr.isDomainBoolean()) {
                        // Value has to be not null and ("true" or "false")
                        String valueStr=iomObj.getattrprim(attrName, structi);
                        if (valueStr == null || valueStr.equals("true") || valueStr.equals("false")){
                            // Value okay, skip it
                        } else {
                            logMsg(validateType, rsrc.getString("validateAttrValue.valueXIsNotABooleanInAttributeY"), valueStr, attrPath);
                        }
                    } else if (attr.isDomainIliUuid()) {
                        // Value is exactly 36 chars long and matches the regex
                        String valueStr=iomObj.getattrprim(attrName, structi);
                        if (valueStr == null || isValidUuid(valueStr)) {
                                // Value ok, Skip it
                        } else {
                            logMsg(validateType, rsrc.getString("validateAttrValue.valueXIsNotAValidUUIDInAttributeY"), valueStr, attrPath);
                        }
                    } else if (attr.isDomainIli2Date()) {
                        // Value matches regex and is not null and is in range of type.
                        String valueStr=iomObj.getattrprim(attrName, structi);
                        FormattedType subType = (FormattedType) type;
                        // The length is explicitly tested because the generated regular expression does not test the length of the value.
                        if (valueStr != null){
                            if (!valueStr.matches(subType.getRegExp()) || valueStr.length() != 10) {
                                logMsg(validateType, rsrc.getString("validateAttrValue.invalidFormatOfDateValueXInAttributeY"), valueStr, attrPath);
                            } else if(!subType.isValueInRange(valueStr)){
                                logMsg(validateType, rsrc.getString("validateAttrValue.dateValueXIsNotInRangeInAttributeY"), valueStr, attrPath);
                            }
                        }
                    } else if (attr.isDomainIli2Time()) {
                        // Value is not null and matches 0:0:0.000-23:59:59.999
                        String valueStr=iomObj.getattrprim(attrName, structi);
                        FormattedType subType = (FormattedType) type;
                        // Min length and max length is added, because of the defined regular expression which does not test the length of the value.
                        if (valueStr != null){
                            if (!valueStr.matches(subType.getRegExp()) || valueStr.length() < 9 || valueStr.length() > 12){
                                logMsg(validateType, rsrc.getString("validateAttrValue.invalidFormatOfTimeValueXInAttributeY"), valueStr, attrPath);
                            } else if(!subType.isValueInRange(valueStr)){
                                logMsg(validateType, rsrc.getString("validateAttrValue.timeValueXIsNotInRangeInAttributeY"), valueStr, attrPath);
                            }
                        }
                    } else if (attr.isDomainIli2DateTime()) {
                        // Value is not null
                        String valueStr=iomObj.getattrprim(attrName, structi);
                        FormattedType subType = (FormattedType) type;
                        // Min length and max length is added, because of the defined regular expression which does not test the length of the value.
                        if (valueStr != null){
                            if (!valueStr.matches(subType.getRegExp()) || valueStr.length() < 18 || valueStr.length() > 23) {
                                logMsg(validateType, rsrc.getString("validateAttrValue.invalidFormatOfDatetimeValueXInAttributeY"), valueStr, attrPath);
                            } else if(!subType.isValueInRange(valueStr)){
                                logMsg(validateType, rsrc.getString("validateAttrValue.datetimeValueXIsNotInRangeInAttributeY"), valueStr, attrPath);
                            }
                        }
                    } else if(isDomainName(attr)) {
                        // Value is not null
                        String valueStr=iomObj.getattrprim(attrName, structi);
                        if (valueStr!=null) {
                            validateTextType(iomObj, attrPath, attrName, validateType, type, valueStr);
                            if (isAKeyword(valueStr)) {
                                logMsg(validateType,rsrc.getString("validateAttrValue.valueXIsAKeywordInAttributeY"), valueStr, attrPath);
                            }else{
                                // value is not a keyword
                            }
                            Pattern pattern=Pattern.compile("[a-zA-Z]{1}([a-zA-Z0-9\\_]{1,})");
                            Matcher matcher=pattern.matcher(valueStr);
                            if(matcher!=null && matcher.matches()){
                                // value matched pattern
                            }else {
                                logMsg(validateType, rsrc.getString("validateAttrValue.invalidFormatOfInterlisNameValueXInAttributeY"), valueStr, attrPath);
                            }
                        }
                    }else if (isDomainUri(attr)) { 
                        // Value is not null
                        String valueStr=iomObj.getattrprim(attrName, structi);
                        if (valueStr!=null) {
                            validateTextType(iomObj, attrPath, attrName, validateType, type, valueStr);
                            
                            // see http://blog.dieweltistgarnichtso.net/constructing-a-regular-expression-that-matches-uris
                            Pattern pattern = Pattern.compile("((?<=\\()[A-Za-z][A-Za-z0-9\\+\\.\\-]*:([A-Za-z0-9\\.\\-_~:/\\?#\\[\\]@!\\$&'\\(\\)\\*\\+,;=]|%[A-Fa-f0-9]{2})+(?=\\)))|([A-Za-z][A-Za-z0-9\\+\\.\\-]*:([A-Za-z0-9\\.\\-_~:/\\?#\\[\\]@!\\$&'\\(\\)\\*\\+,;=]|%[A-Fa-f0-9]{2})+)");
                            Matcher matcher=pattern.matcher(valueStr);
                            if(matcher!=null && matcher.matches()){
                             // value matched pattern
                            }else {
                                logMsg(validateType, rsrc.getString("validateAttrValue.invalidFormatOfInterlisUriValueXInAttributeY"), valueStr, attrPath);
                            }
                        }
                    } else if (type instanceof PolylineType){
                        PolylineType polylineType=(PolylineType)type;
                        IomObject polylineValue=iomObj.getattrobj(attrName, structi);
                        if (polylineValue != null){
                            boolean isValid=validatePolyline(validateGeometryType, model, polylineType, polylineValue, attrName);
                            if(isValid){
                                validatePolylineTopology(attrPath,validateGeometryType, polylineType, polylineValue);
                            }
                        } else {
                            String attrValue = iomObj.getattrvalue(attrName);
                            if (attrValue != null) {
                                logMsg(validateType, rsrc.getString("validateAttrValue.theValueXIsNotAPolylineInAttributeY"), attrValue, attrPath);
                            }                       
                        }
                    } else if (type instanceof MultiPolylineType){
                        MultiPolylineType polylineType=(MultiPolylineType)type;
                        IomObject multipolylineValue=iomObj.getattrobj(attrName, structi);
                        if (multipolylineValue != null){
                            if(multipolylineValue.getobjecttag().equals("MULTIPOLYLINE")) {
                                int polylinec=multipolylineValue.getattrvaluecount("polyline");
                                if(polylinec>0) {
                                    for(int polylinei=0;polylinei<polylinec;polylinei++) {
                                        IomObject polylineValue=multipolylineValue.getattrobj("polyline", polylinei);
                                        boolean isValid=validatePolyline(validateGeometryType, model, polylineType, polylineValue, attrName);
                                        if(isValid){
                                            validatePolylineTopology(attrPath,validateGeometryType, polylineType, polylineValue);
                                        }
                                    }
                                }else {
                                    logMsg(validateType, rsrc.getString("validateMultiPolyline.invalidNumberOfPolylines"));
                                }
                            }else {
                                logMsg(validateType, "unexpected Type "+multipolylineValue.getobjecttag()+"; MULTIPOLYLINE expected");
                            }
                        } else {
                            String attrValue = iomObj.getattrvalue(attrName);
                            if (attrValue != null) {
                                logMsg(validateType, rsrc.getString("validateAttrValue.theValueXIsNotAPolylineInAttributeY"), attrValue, attrPath);
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
                            IomObject surfaceValue=iomObj.getattrobj(attrName,structi);
                            if (surfaceValue != null){
                                boolean isValid = validatePolygon(validateGeometryType, model, surfaceOrAreaType, surfaceValue, iomObj, attrName);
                                if(isValid){
                                    Object attrValidator=pipelinePool.getIntermediateValue(attr, ValidationConfig.TOPOLOGY);
                                    if(attrValidator==null){
                                        attrValidator=this;
                                        pipelinePool.setIntermediateValue(attr, ValidationConfig.TOPOLOGY,this);
                                    }
                                    if(attrValidator==this){
                                        boolean surfaceTopologyValid=validateSurfaceTopology(validateGeometryType,attr,surfaceOrAreaType,currentMainOid, surfaceValue);
                                        if(surfaceOrAreaType instanceof AreaType){
                                            if(!singlePass) {
                                                if(!ValidationConfig.OFF.equals(areaOverlapValidation)){

                                                    ItfAreaPolygon2Linetable allLines=areaAttrs.get(attr);
                                                    if(allLines==null){
                                                        allLines=new ItfAreaPolygon2Linetable(iliClassQName, objPoolManager);
                                                        areaAttrs.put(attr,allLines);
                                                    }

                                                    if(surfaceTopologyValid) {
                                                        allLines.addPolygon(currentMainOid, null, surfaceValue, validateGeometryType, errFact);
                                                    }else {
                                                        // surface topology not valid
                                                        areaAttrsAreSurfaceTopologiesValid.put(attr, false);
                                                        errs.addEvent(errFact.logInfoMsg(rsrc.getString("validateAttrValue.areaTopologyNoValidatedValidationOfSurfaceTopologyFailedInAttributeY"), attrPath));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                String attrValue=iomObj.getattrprim(attrName, structi);
                                if (attrValue != null) {
                                    logMsg(validateType,rsrc.getString("validateAttrValue.theValueXIsNotAPolygonInAttributeY"), attrValue, attrPath);
                                }
                            }
                         }
                    }else if(type instanceof MultiSurfaceOrAreaType){
                        // validate polygon
                        MultiSurfaceOrAreaType surfaceOrAreaType=(MultiSurfaceOrAreaType)type;
                       IomObject surfaceValue=iomObj.getattrobj(attrName,structi);
                       if (surfaceValue != null){
                           boolean isValid = validatePolygon(validateGeometryType, model, surfaceOrAreaType, surfaceValue, iomObj, attrName);
                           if(isValid){
                               Object attrValidator=pipelinePool.getIntermediateValue(attr, ValidationConfig.TOPOLOGY);
                               if(attrValidator==null){
                                   attrValidator=this;
                                   pipelinePool.setIntermediateValue(attr, ValidationConfig.TOPOLOGY,this);
                               }
                               if(attrValidator==this){
                                   boolean surfaceTopologyValid=validateMultiSurfaceTopology(validateGeometryType,attr, surfaceOrAreaType,currentMainOid, surfaceValue);
                                   if(surfaceOrAreaType instanceof MultiAreaType){
                                       if(!singlePass) {
                                           if(!ValidationConfig.OFF.equals(areaOverlapValidation)){

                                               ItfAreaPolygon2Linetable allLines=areaAttrs.get(attr);
                                               if(allLines==null){
                                                   allLines=new ItfAreaPolygon2Linetable(iliClassQName, objPoolManager);
                                                   areaAttrs.put(attr,allLines);
                                               }

                                               if(surfaceTopologyValid) {
                                                   allLines.addMultiPolygon(currentMainOid, null, surfaceValue, validateGeometryType, errFact);
                                               }else {
                                                   // surface topology not valid
                                                   areaAttrsAreSurfaceTopologiesValid.put(attr, false);
                                                   errs.addEvent(errFact.logInfoMsg(rsrc.getString("validateAttrValue.areaTopologyNoValidatedValidationOfSurfaceTopologyFailedInAttributeY"), attrPath));
                                               }
                                           }
                                       }
                                   }
                               }
                           }
                       } else {
                           String attrValue=iomObj.getattrprim(attrName, structi);
                           if (attrValue != null) {
                               logMsg(validateType,rsrc.getString("validateAttrValue.theValueXIsNotAPolygonInAttributeY"), attrValue, attrPath);
                           }
                       }
                    }else if(type instanceof CoordType){
                        IomObject coord=iomObj.getattrobj(attrName, structi);
                        if (coord!=null){
                            validateCoordType(validateGeometryType, model, domain, (CoordType)type, coord, attrName);
                        } else {
                            String attrValue=iomObj.getattrprim(attrName, structi);
                            if (attrValue != null) {
                                logMsg(validateType, rsrc.getString("validateAttrValue.theValueXIsNotCoordInAttributeY"), attrValue, attrPath);
                            }
                        }
                    }else if(type instanceof MultiCoordType){
                        IomObject multicoordValue=iomObj.getattrobj(attrName, structi);
                        if (multicoordValue!=null){
                            if(multicoordValue.getobjecttag().equals("MULTICOORD")) {
                                int coordc=multicoordValue.getattrvaluecount("coord");
                                if(coordc>0) {
                                    for(int coordi=0;coordi<coordc;coordi++) {
                                        IomObject coordValue=multicoordValue.getattrobj("coord", coordi);
                                        if(coordValue.getobjecttag().equals("COORD")) {
                                            validateCoordType(validateGeometryType, model, domain, (MultiCoordType)type, coordValue, attrName);
                                        }else {
                                            logMsg(validateType, "unexpected Type "+coordValue.getobjecttag()+"; COORD expected");
                                        }
                                    }
                                }else {
                                    logMsg(validateType, rsrc.getString("validateMultiCoord.invalidNumberOfCoords"));
                                }
                            }else {
                                logMsg(validateType, "unexpected Type "+multicoordValue.getobjecttag()+"; MULTICOORD expected");
                            }
                        } else {
                            String attrValue=iomObj.getattrprim(attrName, structi);
                            if (attrValue != null) {
                                logMsg(validateType, rsrc.getString("validateAttrValue.theValueXIsNotCoordInAttributeY"), attrValue, attrPath);
                            }
                        }
                    }else if(type instanceof NumericType){
                        String valueStr=iomObj.getattrprim(attrName, structi);
                        if(valueStr!=null){
                            OutParam<Boolean> isNumValid=new OutParam<Boolean>(true);
                            String newValueStr=validateNumericType(validateType, (NumericType)type, valueStr, attrName,isNumValid);
                            if(newValueStr!=null) {
                                ((Iom_jObject)iomObj).setattrvalue(attrName, structi,newValueStr);
                            }
                        }else{
                            IomObject structValue=iomObj.getattrobj(attrName, structi);
                            if(structValue!=null){
                                logMsg(validateType, rsrc.getString("validateAttrValue.attributeXHasAnUnexpectedTypeY"),attrPath,structValue.getobjecttag());
                            }
                        }
                    }else if(type instanceof EnumerationType){
                        EnumerationType enumType = (EnumerationType) type;
                        String value=iomObj.getattrprim(attrName, structi);
                        if(value!=null){
                            if(!((EnumerationType) type).getValues().contains(value)){
                                logMsg(validateType,rsrc.getString("validateAttrValue.valueXIsNotAMemberOfTheEnumerationInAttributeY"), value, attrPath);   
                            }
                        }else{
                            IomObject structValue=iomObj.getattrobj(attrName, structi);
                            if(structValue!=null){
                                logMsg(validateType, rsrc.getString("validateAttrValue.attributeXHasAnUnexpectedTypeY"),attrPath,structValue.getobjecttag());
                            }
                        }
                    }else if(type instanceof EnumTreeValueType){
                        String actualValue=iomObj.getattrprim(attrName, structi);
                        if(actualValue!=null) {
                            if (isValidEnumTreeValue(actualValue, attrPath,(EnumTreeValueType) type)) {
                                logMsg(validateType,rsrc.getString("validateAttrValue.valueXIsNotAMemberOfTheEnumerationInAttributeY"), actualValue, attrPath);
                            }
                        }
                    }else if(type instanceof ReferenceType){
                    }else if(type instanceof TextType){
                        String value=iomObj.getattrprim(attrName, structi);
                        validateTextType(iomObj, attrPath, attrName, validateType, type, value);
                    }else if(type instanceof TextOIDType){
                        String value=iomObj.getattrprim(attrName, structi);
                        if (value != null && isDomainAnnexOid(attr)) {
                            if (!isValidAnnexOid(value)) {
                                errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateAttrValue.valueXIsNotAValidOidInAttributeY"), value, attrPath));
                            }                        
                        } else if (value != null && isDomainUuid(attr)) {
                            if (!isValidUuid(value)) {
                                errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateAttrValue.valueXIsNotAValidOidInAttributeY"), value, attrPath));
                            }                        
                        } else if (value != null && isDomainTextOid(attr)) {
                            if (!isValidTextOid(value,((TextType)((TextOIDType)type).getOIDType()).getMaxLength())) {
                                errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateAttrValue.valueXIsNotAValidOidInAttributeY"), value, attrPath));
                            }                        
                        }
                    } else if (type instanceof FormattedType) {
                        String regExp = ((FormattedType) type).getRegExp();
                        String actualValue=iomObj.getattrprim(attrName, structi);
                        if (actualValue != null) {
                            if (!actualValue.matches(regExp)) {
                                errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateAttrValue.attributeXHasAInvalidValueY"), attrPath, actualValue));
                            } else {
                                boolean hasAValidValue = ((FormattedType) type).isValueInRange(actualValue);
                                if (!hasAValidValue) {
                                    errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateAttrValue.valueXIsAOutOfRangeInAttributeY"), actualValue, attrPath));
                                }
                            }
                        }                   
                    } else if (type instanceof BlackboxType) {
                        if(((BlackboxType) type).getKind()==BlackboxType.eBINARY) {
                            String actualValue=iomObj.getattrprim(attrName, structi);
                            if (actualValue != null) {
                                Matcher matcher = patternForBase64Validation.matcher(actualValue);
                                if (!matcher.matches()) {
                                    errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateAttrValue.attributeXHasAInvalidValueY"), attrPath, shortcutValue(actualValue)));
                                }
                            }                   
                        }
                    }
                }
                if (domain != null) {
                    String rootDomainName = domain.getScopedName(null);

                    while (domain != null) {
                        Iterator<DomainConstraint> iterator = domain.iterator();
                        while (iterator.hasNext()){
                            DomainConstraint constraint = iterator.next();
                            Iom_jObject iomValueObject = new Iom_jObject(rootDomainName, null);
                            iomValueObject.setattrvalue(VALUE_REF_THIS, iomObj.getattrprim(attrName, structi));

                            Value result = evaluateExpression(null, constraint.getScopedName(), rootDomainName, iomValueObject, constraint.getCondition(), null);

                            if (!result.isTrue()){
                                logMsg(validateType, rsrc.getString("validateAttrValue.attributeXDoesNotSatisfyTheDomainConstraintY"), attrPath, constraint.getScopedName());
                            }
                        }
                        domain = domain.getExtending();
                    }
                }
            }
        }
    }

    static private Cardinality getCardinality(AttributeDef attr) { // move to ili2c
        Type type=attr.getDomainOrDerivedDomain();
        Cardinality card=type.getCardinality();
        if(card.getMinimum()==0){
            if(false) { // attr.isDefinedMandatory()) { // fix in compiler
                card=new Cardinality(1,card.getMaximum());
            }else if(type instanceof TypeAlias) {
                if(((TypeAlias)type).getAliasing().isDefinedMandatory()){
                    card=new Cardinality(1,card.getMaximum());
                }
            }
        }
        return card;
    }
    private static String shortcutValue(String value) {
        int MAX_LEN=20;
        if(value==null || value.length()<=MAX_LEN) {
            return value;
        }
        return value.substring(0,MAX_LEN)+"...";
    }
    private boolean isValidEnumTreeValue(String actualValue, String attrPath, EnumTreeValueType enumTreeValueType) {
        HashSet<String> trueValueFormat=(HashSet<String>) enumTreeValueType.getTransientMetaValue(ENUM_TREE_VALUES);
        if(trueValueFormat==null) {
            List<String> values = new ArrayList<String>();
            ModelUtilities.buildEnumListAll(values, "", enumTreeValueType.getConsolidatedEnumeration());
            trueValueFormat = new HashSet<String>(values);
            enumTreeValueType.setTransientMetaValue(ENUM_TREE_VALUES,trueValueFormat);
        }
        if (!trueValueFormat.contains(actualValue)) {
            return true;
        }
        return false;
    }

    private void validateTextType(IomObject iomObj, String attrPath, String attrName, String validateType, Type type,
			String value) {
		if(value!=null){
			int maxLength=((TextType) type).getMaxLength();
			TextType textType = (TextType) type;
			if(maxLength!=-1){
				if(value.length()>maxLength){
					 logMsg(validateType,rsrc.getString("validateTextType.attributeXIsLengthRestrictedToY"), attrPath,Integer.toString(maxLength));
				}
			}
			if(((TextType) type).isNormalized()){
				if(value.indexOf('\n')>=0 || value.indexOf('\r')>=0 || value.indexOf('\t')>=0){
					 logMsg(validateType,rsrc.getString("validateTextType.attributeXMustNotContainControlCharacters"), attrPath);
				}
			}
		}else{
			IomObject structValue=iomObj.getattrobj(attrName, 0);
			if(structValue!=null){
				logMsg(validateType, rsrc.getString("validateTextType.attributeXHasAnUnexpectedTypeY"),attrPath,structValue.getobjecttag());
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
				throw new IllegalArgumentException("unexpected attribute type " + attr.getScopedName());
			}
		}
		return attrType.isMandatoryConsideringAliases();
	}

	/**
	 * Validate that the surface is valid according to INTERLIS. For example no self-intersection etc.
	 *
	 * @return <c>true</c> if the surface is valid, <c>false</c> otherwise.
	 */
	private boolean validateSurfaceTopology(String validateType, AttributeDef attr,SurfaceOrAreaType type, String mainObjTid,IomObject iomValue) {
		boolean surfaceTopologyValid=true;
		try {
			AbstractCoordType coordType = resolveGenericCoordTypeOfSurface(validateType, attr);
			if (coordType == null) {
				return false;
			}
			surfaceTopologyValid=ItfSurfaceLinetable2Polygon.validatePolygon(mainObjTid, attr, iomValue, errFact, validateType, coordType);
		} catch (IoxException e) {
			surfaceTopologyValid=false;
			errs.addEvent(errFact.logErrorMsg(e, rsrc.getString("validateSurfaceTopology.failedToValidatePolygon")));
		}
		return surfaceTopologyValid;
	}
    private boolean validateMultiSurfaceTopology(String validateType, AttributeDef attr,MultiSurfaceOrAreaType type, String mainObjTid,IomObject iomValue) {
        boolean surfaceTopologyValid=true;
        try {
            AbstractCoordType coordType = resolveGenericCoordTypeOfSurface(validateType, attr);
            if (coordType == null) {
                return false;
            }
            surfaceTopologyValid=ItfSurfaceLinetable2Polygon.validateMultiPolygon(mainObjTid, attr, iomValue, errFact, validateType, coordType);
        } catch (IoxException e) {
            surfaceTopologyValid=false;
            errs.addEvent(errFact.logErrorMsg(e, rsrc.getString("validateSurfaceTopology.failedToValidatePolygon")));
        }
        return surfaceTopologyValid;
    }

    private AbstractCoordType resolveGenericCoordTypeOfSurface(String validateType, AttributeDef attr) {
        AbstractSurfaceOrAreaType surfaceType = (AbstractSurfaceOrAreaType) attr.getDomainResolvingAliases();
        Domain coordDomain = surfaceType.getControlPointDomain();
        AbstractCoordType coordType = (AbstractCoordType) coordDomain.getType();
        if (coordType.isGeneric()) {
            coordType = resolveGenericCoordType(validateType, (Model) attr.getContainer(Model.class), coordDomain);
        }
        return coordType;
    }

	private void validatePolylineTopology(String attrPath,String validateType, LineType type, IomObject iomValue) {
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
					newVertexOffset=2*java.lang.Math.pow(10, -size);
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

	/**
	 * Validate if the polygon is syntactically correct.
	 *
	 * @return <c>true</c> if the polygon is valid.
	 */
	private boolean validatePolygon(String validateType, Model model, AbstractSurfaceOrAreaType surfaceOrAreaType, IomObject surfaceValue, IomObject currentIomObj, String attrName) {
        boolean foundErrs=false;
		if (surfaceValue.getobjecttag().equals("MULTISURFACE")){
		    int surfacec=surfaceValue.getattrvaluecount("surface");
            if(surfacec==0){
                logMsg(validateType, rsrc.getString("validateMultiPolygon.invalidNumberOfSurfaces"));
                return false;
            }
			boolean clipped = surfaceValue.getobjectconsistency()==IomConstants.IOM_INCOMPLETE;
            if((surfaceOrAreaType instanceof SurfaceOrAreaType) && !clipped && surfacec>1){
                // unclipped surface with multi 'surface' elements
                logMsg(validateType, rsrc.getString("validatePolygon.invalidNumberOfSurfaceInCompleteBasket"));
                return false;
            }
			for(int surfacei=0;surfacei< surfacec;surfacei++){
				IomObject surface= surfaceValue.getattrobj("surface",surfacei);
				int boundaryc=surface.getattrvaluecount("boundary");
				// a multisurface consists of at least one boundary.
				if(boundaryc==0){
					String objectIdentification = currentIomObj.getobjectoid();
					if(objectIdentification==null){
						objectIdentification = currentIomObj.getobjecttag();
					}
					logMsg(validateType, rsrc.getString("validatePolygon.missingOuterboundaryInXOfObjectY"), attrName, objectIdentification);
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
					        foundErrs = foundErrs || !validatePolyline(validateType, model, surfaceOrAreaType, polyline, attrName);
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
		return !foundErrs;
	}

	/**
	 * Validate if the polyline is syntactically correct.
	 *
	 * @return <c>true</c> if valid.
	 */
	private boolean validatePolyline(String validateType, Model model, LineType polylineType, IomObject polylineValue, String attrName) {
		boolean foundErrs=false;
		if (polylineValue.getobjecttag().equals("POLYLINE")){
			boolean clipped = polylineValue.getobjectconsistency()==IomConstants.IOM_INCOMPLETE;
            if(!clipped && polylineValue.getattrvaluecount("sequence")>1){
                // an unclipped polyline should have only one sequence element
                logMsg(validateType, rsrc.getString("validatePolyline.invalidNumberOfSequenceInCompleteBasket"));
                foundErrs = foundErrs || true;
            }
			for(int sequencei=0;sequencei<polylineValue.getattrvaluecount("sequence");sequencei++){
				IomObject sequence=polylineValue.getattrobj("sequence",sequencei);
				LineForm[] lineforms = polylineType.getLineForms();
				HashSet<String> lineformNames=new HashSet<String>();
				for(LineForm lf:lineforms){
					lineformNames.add(lf.getName());
				}
				if(sequence.getobjecttag().equals("SEGMENTS")){
		            if(sequence.getattrvaluecount("segment")<=1){
		                logMsg(validateType, rsrc.getString("validatePolyline.invalidNumberOfSegments"));
		                foundErrs = foundErrs || true;
		            }
					for(int segmenti=0;segmenti<sequence.getattrvaluecount("segment");segmenti++){
						// segment = all segments which are in the actual sequence.
						IomObject segment=sequence.getattrobj("segment",segmenti);
						if(segment.getobjecttag().equals("COORD")){
							if(lineformNames.contains("STRAIGHTS") || segmenti==0){
								Domain coordDomain = polylineType.getControlPointDomain();
							    foundErrs = foundErrs || !validateCoordType(validateType, model, coordDomain, (CoordType) coordDomain.getType(), segment, attrName);
							}else{
								logMsg(validateType, "unexpected COORD");
								foundErrs = foundErrs || true;
							}
						} else if (segment.getobjecttag().equals("ARC")){
							if(lineformNames.contains("ARCS") && segmenti>0){
							    foundErrs = foundErrs || !validateARCSType(validateType, (CoordType) polylineType.getControlPointDomain().getType(), segment, attrName);
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

	private AbstractCoordType resolveGenericCoordType(String validateType, Model model, Domain coordDomain) {
		// search deferred generic in transfer
		Domain concreteDomain = genericDomains.get(coordDomain.getScopedName());
		if (concreteDomain != null) {
			return (AbstractCoordType) concreteDomain.getType();
		}

		// resolve generic domain from contexts in model
		Domain[] resolved = model.resolveGenericDomain(coordDomain);
		if (resolved == null) {
			logMsg(validateType, rsrc.getString("validateCoordType.missingContext"), coordDomain.toString());
			return null;
		}

		if (resolved.length == 1) {
			return (AbstractCoordType) resolved[0].getType();
		}

		logMsg(validateType, rsrc.getString("validateCoordType.multipleDomainsInContext"), coordDomain.toString());
		return null;
    }

    // returns true if valid
	private boolean validateCoordType(String validateType, Model model, Domain coordDomain, AbstractCoordType coordType, IomObject coordValue, String attrName) {
        boolean foundErrs=false;
        OutParam<Boolean> isNumValid=new OutParam<Boolean>(true);

		if (coordType.isGeneric()) {
			coordType = resolveGenericCoordType(validateType, model, coordDomain);
			if (coordType == null) {
				return false;
			}
		}

		if (coordType.getDimensions().length >= 1){
			if (coordValue.getattrvalue("C1") != null){
				coordValue.setattrvalue("C1", validateNumericType(validateType, (NumericType)coordType.getDimensions()[0], coordValue.getattrvalue("C1"), attrName,isNumValid));
			} else if (coordValue.getattrvalue("A1") != null) {
				logMsg(validateType, rsrc.getString("validateCoordType.notATypeOfCoord"));
	            foundErrs = foundErrs || true;
			} else {
				logMsg(validateType, rsrc.getString("validateCoordType.wrongCoordStructureC1Expected"));
	            foundErrs = foundErrs || true;
			}
		}
		if (coordType.getDimensions().length == 2){
			if (coordValue.getattrvalue("C3") != null){
				logMsg(validateType, rsrc.getString("validateCoordType.wrongCoordStructureC3NotExpected"));
	            foundErrs = foundErrs || true;
			}
		}
		if (coordType.getDimensions().length >= 2){
			if (coordValue.getattrvalue("C2") != null){
				coordValue.setattrvalue("C2", validateNumericType(validateType, (NumericType)coordType.getDimensions()[1], coordValue.getattrvalue("C2"), attrName,isNumValid));
			} else if (coordValue.getattrvalue("A2") != null) {
				logMsg(validateType, rsrc.getString("validateCoordType.notATypeOfCoord"));
	            foundErrs = foundErrs || true;
			} else {
				logMsg(validateType, rsrc.getString("validateCoordType.wrongCoordStructureC2Expected"));
	            foundErrs = foundErrs || true;
			}
		}
		if (coordType.getDimensions().length == 3){
			if (coordValue.getattrvalue("C3") != null){
				coordValue.setattrvalue("C3", validateNumericType(validateType, (NumericType)coordType.getDimensions()[2], coordValue.getattrvalue("C3"), attrName,isNumValid));
			} else {
				logMsg(validateType, rsrc.getString("validateCoordType.wrongCoordStructureC3Expected"));
	            foundErrs = foundErrs || true;
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
					errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateCoordType.wrongCoordStructureUnknownPropertyX"),propName));
		            foundErrs = foundErrs || true;
				}
			}
		}
        return !foundErrs && isNumValid.value;
	}

    // returns true if valid
	private boolean validateARCSType(String validateType, CoordType coordType, IomObject coordValue, String attrName) {
        boolean foundErrs=false;
		int dimLength=coordType.getDimensions().length;
		String c1=coordValue.getattrvalue("C1");
		String c2=coordValue.getattrvalue("C2");
		String c3=coordValue.getattrvalue("C3");
		String a1=coordValue.getattrvalue("A1");
		String a2=coordValue.getattrvalue("A2");
		
		boolean wrongArcStructure=false;
		OutParam<Boolean> isNumValid=new OutParam<Boolean>(true);
		int c1Count=coordValue.getattrvaluecount("C1");
		if (dimLength>=2 && dimLength<=3){
			if(a1!=null && a2!=null && c1!=null && c2!=null){
				coordValue.setattrvalue("A1", validateNumericType(validateType, (NumericType)coordType.getDimensions()[0], a1, attrName,isNumValid));
				coordValue.setattrvalue("A2", validateNumericType(validateType, (NumericType)coordType.getDimensions()[1], a2, attrName,isNumValid));
				coordValue.setattrvalue("C1", validateNumericType(validateType, (NumericType)coordType.getDimensions()[0], c1, attrName,isNumValid));
				coordValue.setattrvalue("C2", validateNumericType(validateType, (NumericType)coordType.getDimensions()[1], c2, attrName,isNumValid));
				if(dimLength==2) {
					if(c3!=null) {
						logMsg(validateType, rsrc.getString("validateARCSType.wrongArcStructureC3NotExpected"));
	                    foundErrs = foundErrs || true;
					}
				}else if(dimLength==3) {
					if(c3!=null) {
						coordValue.setattrvalue("C3", validateNumericType(validateType, (NumericType)coordType.getDimensions()[2], c3, attrName,isNumValid));
					}else {
						logMsg(validateType, rsrc.getString("validateARCSType.wrongArcStructureC3Expected"));
	                    foundErrs = foundErrs || true;
					}
				}
			}else {
				wrongArcStructure=true;
			}
		}else {
			wrongArcStructure=true;
		}
		if(wrongArcStructure) {
			logMsg(validateType, rsrc.getString("validateARCSType.wrongArcStructure"));
            foundErrs = foundErrs || true;
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
					errs.addEvent(errFact.logErrorMsg(rsrc.getString("validateARCSType.wrongArcStructureUnknownPropertyX"),propName));
                    foundErrs = foundErrs || true;
				}
			}
		}
        return !foundErrs && isNumValid.value;
	}

	private String validateNumericType(String validateType, NumericType type, String valueStr, String attrName,OutParam<Boolean> isValid) {
		PrecisionDecimal value=null;
		try {
			value=new PrecisionDecimal(valueStr);
		} catch (NumberFormatException e) {
			 logMsg(validateType, rsrc.getString("validateNumericType.valueXIsNotANumber"), valueStr);
			 isValid.value=false;
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
				logMsg(validateType, rsrc.getString("validateNumericType.valueXIsOutOfRangeInAttributeY"), rounded.toString(), attrName);
	             isValid.value=false;
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
	    return attr.isDomainName();
	}
	
    private static boolean isDomainUri(AttributeDef attr){
        return attr.isDomainUri();
    }
    private static boolean isDomainAnnexOid(AttributeDef attr){
        Type type=attr.getDomain();
        while(type instanceof TypeAlias) {
            if (((TypeAlias) type).getAliasing() == PredefinedModel.getInstance().STANDARDOID) {
                return true;
            }
            type=((TypeAlias) type).getAliasing().getType();
        }
        return false;
    }
    private static boolean isDomainUuid(AttributeDef attr){
        return attr.isDomainIliUuid();
    }
    
    private static boolean isDomainTextOid(AttributeDef attr){
        Type type=attr.getDomain();
        while(type instanceof TextOIDType) {
            return true;
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

	private void logMsg(String validateKind, IoxIntersectionException intersectionException) {
		if(ValidationConfig.OFF.equals(validateKind)){
			// skip it
		}else if(ValidationConfig.WARNING.equals(validateKind)){
			errs.addEvent(errFact.logWarningMsg(intersectionException));
		}else{
			errs.addEvent(errFact.logErrorMsg(intersectionException));
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
	public static void initItfValidation(Settings settings)
	{
        settings.setValue(CONFIG_DO_ITF_OIDPERTABLE, CONFIG_DO_ITF_OIDPERTABLE_DO);
	}
	public long getObjectCount() {
	    return objectCount;
	}
    public long getStructCount() {
        return structCount;
    }
}