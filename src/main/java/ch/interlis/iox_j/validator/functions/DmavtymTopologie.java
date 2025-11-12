package ch.interlis.iox_j.validator.functions;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.types.OutParam;
import ch.interlis.ili2c.metamodel.Evaluable;
import ch.interlis.ili2c.metamodel.Function;
import ch.interlis.ili2c.metamodel.FunctionCall;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.ili2c.metamodel.TextType;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurveRing;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurvePolygon;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurveSegment;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxValidationConfig;
import ch.interlis.iox_j.jts.Iox2jtsext;
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.iox_j.validator.ValidationConfig;
import ch.interlis.iox_j.validator.Validator;
import ch.interlis.iox_j.validator.Value;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.ItemVisitor;
import com.vividsolutions.jts.index.strtree.STRtree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DmavtymTopologie {
    public static final String DMAVTYM_Topologie_V1_0 = "DMAVTYM_Topologie_V1_0";
    public static final String DMAVTYM_Topologie_V1_1 = "DMAVTYM_Topologie_V1_1";

    private final TransferDescription td;
    private final IoxValidationConfig validationConfig;
    private final Validator validator;
    private final LogEventFactory logger;

    public DmavtymTopologie(Validator validator, TransferDescription td, IoxValidationConfig validationConfig, LogEventFactory logger) {
        this.validator = validator;
        this.td = td;
        this.validationConfig = validationConfig;

        this.logger = logger;
        logger.setValidationConfig(validationConfig);
    }

    public Value evaluateFunction(Function currentFunction, FunctionCall functionCallObj, IomObject parentObject,
                                  String validationKind, String usageScope, IomObject iomObj, TextType texttype, RoleDef firstRole) {

        Evaluable[] arguments = functionCallObj.getArguments();
        Value[] actualArguments = new Value[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            Value result = validator.evaluateExpression(parentObject, validationKind, usageScope, iomObj, arguments[i], firstRole);
            if (result.skipEvaluation()) {
                return result;
            }

            actualArguments[i] = result;
        }

        if (currentFunction.getName().equals("covers")) {
            return evaluateCovers(validationKind, usageScope, iomObj, actualArguments);
        } else if (currentFunction.getName().equals("coversWithTolerance")) {
            return evaluateCoversWithTolerance(validationKind, usageScope, iomObj, actualArguments);
        } else if (currentFunction.getName().equals("pointInPoints")) {
            return evaluatePointInPoints(validationKind, usageScope, iomObj, actualArguments);
        } else {
            return Value.createNotYetImplemented();
        }
    }

    private Value evaluateCovers(String validationKind, String usageScope, IomObject mainObj, Value[] actualArguments) {
        // All arguments must be defined
        for (Value arg : actualArguments) {
            if (arg.isUndefined()) {
                return Value.createSkipEvaluation();
            }
        }

        // Check the type of the arguments
        Collection<IomObject> surfaceObjects = actualArguments[0].getComplexObjects();
        String surfaceAttr = actualArguments[1].getValue();
        Collection<IomObject> multiLineObjects = actualArguments[2].getComplexObjects();
        String multiLineAttr = actualArguments[3].getValue();
        if (surfaceObjects == null || surfaceObjects.size() != 1 || surfaceAttr == null
                || multiLineObjects == null || multiLineObjects.size() != 1 || multiLineAttr == null) {
            return Value.createUndefined();
        }

        // Resolve attributes
        IomObject surfaceObject = surfaceObjects.iterator().next();
        IomObject multiLineObject = multiLineObjects.iterator().next();
        if (surfaceObject.getattrvaluecount(surfaceAttr) != 1 || multiLineObject.getattrvaluecount(multiLineAttr) != 1) {
            return Value.createUndefined();
        }

        // Convert the IOM objects to JTS objects
        CurvePolygon surface;
        Collection<CompoundCurve> lines;
        try {
            surface = getSurface(surfaceObject.getattrobj(surfaceAttr, 0), validationKind);
            lines = getLines(multiLineObject.getattrobj(multiLineAttr, 0));
        } catch (Exception e) {
            EhiLogger.logError(e);
            return Value.createUndefined();
        }

        // Add all surface segments to a hashset
        HashMap<CurveSegment, Boolean> surfaceSegments = new HashMap<CurveSegment, Boolean>();
        for (CompoundCurve line : ((CompoundCurveRing) surface.getExteriorRing()).getLines()) {
            for (CurveSegment segment : line.getSegments()) {
                surfaceSegments.put(segment, false);
            }
        }
        for (int i = 0; i < surface.getNumInteriorRing(); i++) {
            for (CompoundCurve line : ((CompoundCurveRing) surface.getInteriorRingN(i)).getLines()) {
                for (CurveSegment segment : line.getSegments()) {
                    surfaceSegments.put(segment, false);
                }
            }
        }

        // Check if all multiline lines are contained in the surfaceSegments hashset
        boolean result = true;
        for (CompoundCurve line : lines) {
            for (CurveSegment segment : line.getSegments()) {
                Boolean isSegmentVisited = surfaceSegments.get(segment);
                if (isSegmentVisited == null) {
                    // No matching segment found
                    Coordinate point = segment.getStartPoint();
                    logger.addEvent(logger.logErrorMsg("MultiLineAttr contains unmatched line segment: {0}.", point.x, point.y, point.z, segment.toString()));
                    result = false;
                } else if (isSegmentVisited) {
                    // Segment already visited
                    Coordinate point = segment.getStartPoint();
                    logger.addEvent(logger.logWarningMsg("MultiLineAttr contains duplicate line segment: {0}.", point.x, point.y, point.z, segment.toString()));
                } else {
                    surfaceSegments.put(segment, true);
                }
            }
        }

        return new Value(result);
    }

    private Value evaluateCoversWithTolerance(String validationKind, String usageScope, IomObject mainObj, Value[] actualArguments) {
        // All arguments must be defined
        for (Value arg : actualArguments) {
            if (arg.isUndefined()) {
                return Value.createSkipEvaluation();
            }
        }

        // Check the type of the arguments
        Collection<IomObject> referenceObjects = actualArguments[0].getComplexObjects();
        String referenceAttr = actualArguments[1].getValue();
        Collection<IomObject> multiLineObjects = actualArguments[2].getComplexObjects();
        String multiLineAttr = actualArguments[3].getValue();
        double tolerance = actualArguments[4].getNumeric();
        if (referenceObjects == null || referenceAttr == null
                || multiLineObjects == null || multiLineObjects.size() != 1 || multiLineAttr == null
                || tolerance < 0) {
            return Value.createUndefined();
        }

        // Resolve multiline
        IomObject multiLineObject = multiLineObjects.iterator().next();
        if (multiLineObject.getattrvaluecount(multiLineAttr) != 1) {
            return Value.createUndefined();
        }

        // Convert the IOM objects to JTS objects
        Collection<Collection<CompoundCurve>> referenceMultiLines = new ArrayList<Collection<CompoundCurve>>(referenceObjects.size());
        Collection<CompoundCurve> multiLine;
        try {
            for (IomObject referenceObject : referenceObjects) {
                if (referenceObject.getattrvaluecount(referenceAttr) != 1) {
                    return Value.createUndefined();
                }
                IomObject attrValue = referenceObject.getattrobj(referenceAttr, 0);
                String objectTag = attrValue.getobjecttag();
                if (objectTag.equals(Iom_jObject.MULTISURFACE)) {
                    CurvePolygon surface = getSurface(attrValue, validationKind);
                    referenceMultiLines.add(((CompoundCurveRing) surface.getExteriorRing()).getLines());
                    for (int i = 0; i < surface.getNumInteriorRing(); i++) {
                        referenceMultiLines.add(((CompoundCurveRing) surface.getInteriorRingN(i)).getLines());
                    }
                } else if (objectTag.equals(Iom_jObject.MULTIPOLYLINE) || objectTag.equals(Iom_jObject.POLYLINE)) {
                    referenceMultiLines.add(getLines(attrValue));
                } else {
                    return Value.createUndefined();
                }
            }

            multiLine = getLines(multiLineObject.getattrobj(multiLineAttr, 0));
        } catch (IoxException e) {
            EhiLogger.logError(e);
            return Value.createUndefined();
        }

        Envelope linesEnvelope = new Envelope();
        for (CompoundCurve line : multiLine) {
            linesEnvelope.expandToInclude(line.getEnvelopeInternal());
        }
        linesEnvelope.expandBy(tolerance);

        for (Collection<CompoundCurve> referenceMultiLine : referenceMultiLines) {
            boolean envelopesIntersect = false;
            for (CompoundCurve line : referenceMultiLine) {
                if (linesEnvelope.intersects(line.getEnvelopeInternal())) {
                    envelopesIntersect = true;
                    break;
                }
            }
            if (envelopesIntersect && coversWithTolerance(referenceMultiLine, multiLine, tolerance)) {
                return new Value(true);
            }
        }

        return new Value(false);
    }

    private boolean coversWithTolerance(Collection<CompoundCurve> referenceMultiLine, Collection<CompoundCurve> multiLine, final double tolerance) {
        STRtree tree = new STRtree();
        for (CompoundCurve referenceLine : referenceMultiLine) {
            for (CurveSegment segment : referenceLine.getSegments()) {
                tree.insert(segment.computeEnvelopeInternal(), segment);
            }
        }

        // Check if all line segments have a matching entry in the tree
        for (CompoundCurve line : multiLine) {
            for (final CurveSegment segment : line.getSegments()) {
                final boolean[] found = new boolean[]{false};
                tree.query(segment.computeEnvelopeInternal(), new ItemVisitor() {
                    @Override
                    public void visitItem(Object item) {
                        if (!found[0] && item instanceof CurveSegment && segment.equals2D((CurveSegment) item, tolerance)) {
                            found[0] = true;
                        }
                    }
                });

                if (!found[0]) {
                    return false;
                }
            }
        }

        return true;
    }

    private Collection<CompoundCurve> getLines(IomObject multiLine) throws IoxException {
        Collection<CompoundCurve> lines;
        if (multiLine.getobjecttag().equals(Iom_jObject.POLYLINE)) {
            lines = new ArrayList<CompoundCurve>(1);
            lines.add(getLine(multiLine));
            return lines;
        }

        lines = new ArrayList<CompoundCurve>(multiLine.getattrvaluecount(Iom_jObject.MULTIPOLYLINE_POLYLINE));
        for (int i = 0; i < multiLine.getattrvaluecount(Iom_jObject.MULTIPOLYLINE_POLYLINE); i++) {
            lines.add(getLine(multiLine.getattrobj(Iom_jObject.MULTIPOLYLINE_POLYLINE, i)));
        }
        return lines;
    }

    private CompoundCurve getLine(IomObject line) throws IoxException {
        return Iox2jtsext.polyline2JTS(line, false, 0.0, new OutParam<Boolean>(), logger, 0.0, ValidationConfig.WARNING, ValidationConfig.WARNING);
    }

    private CurvePolygon getSurface(IomObject surface, String validationKind) throws IoxException {
        return (CurvePolygon) Iox2jtsext.surface2JTS(surface, 0.0, new OutParam<Boolean>(), logger, 0.0, validationKind);
    }

    private Value evaluatePointInPoints(String validationKind, String usageScope, IomObject mainObj, Value[] actualArguments) {
        Value argPointObjects = actualArguments[0];
        Value argPointAttr = actualArguments[1];
        Value argReferencePointObjects = actualArguments[2];
        Value argReferencePointAttr = actualArguments[3];
        if (argPointObjects.isUndefined() && argReferencePointObjects.isUndefined()) {
            return Value.createUndefined();
        }
        // No points are always contained in the reference points
        if (argPointObjects.isUndefined()) {
            return new Value(true);
        }
        // If no reference points are given, the points cannot be contained in it
        if (argReferencePointObjects.isUndefined()) {
            return new Value(false);
        }

        // Check the type of the arguments
        Collection<IomObject> pointObjects = getAttribute(argPointObjects, argPointAttr);
        Collection<IomObject> referencePoints = getAttribute(argReferencePointObjects, argReferencePointAttr);
        if (pointObjects == null || referencePoints == null) {
            return Value.createUndefined();
        }

        try {
            // Prepare reference point set
            Set<Coordinate> referencePointSet = new HashSet<Coordinate>();
            for (IomObject referencePoint : referencePoints) {
                String objectTag = referencePoint.getobjecttag();
                if (objectTag.equals(Iom_jObject.COORD)) {
                    referencePointSet.add(Iox2jtsext.coord2JTS(referencePoint));
                } else {
                    logger.addEvent(logger.logErrorMsg("Reference Point with unexpected type: {0}.", objectTag));
                    return Value.createUndefined();
                }
            }

            // Check point objects
            for (IomObject point : pointObjects) {
                String objectTag = point.getobjecttag();
                if (objectTag.equals(Iom_jObject.COORD)) { // MULTICOORD ?? Surface, Polyline
                    Coordinate coord = Iox2jtsext.coord2JTS(point);
                    if (!referencePointSet.contains(coord)) {
                        return new Value(false);
                    }
                } else if (objectTag.equals(Iom_jObject.MULTISURFACE)) {
                    CurvePolygon surface = getSurface(point, validationKind);
                    // Check every start and end point of the surface segments if they are contained inside the referencePointSet
                    CompoundCurveRing exteriorRing = (CompoundCurveRing) surface.getExteriorRing();
                    for (CompoundCurve line : exteriorRing.getLines()) {
                        for (CurveSegment segment : line.getSegments()) {
                            if (!referencePointSet.contains(segment.getStartPoint()) || !referencePointSet.contains(segment.getEndPoint())) {
                                return new Value(false);
                            }
                        }
                    }
                    for (int ringIndex = 0; ringIndex < surface.getNumInteriorRing(); ringIndex++) {
                        CompoundCurveRing interiorRing = (CompoundCurveRing) surface.getInteriorRingN(ringIndex);
                        for (CompoundCurve line : interiorRing.getLines()) {
                            for (CurveSegment segment : line.getSegments()) {
                                if (!referencePointSet.contains(segment.getStartPoint()) || !referencePointSet.contains(segment.getEndPoint())) {
                                    return new Value(false);
                                }
                            }
                        }
                    }
                } else if (objectTag.equals(Iom_jObject.MULTIPOLYLINE) || objectTag.equals(Iom_jObject.POLYLINE)) {
                    Collection<CompoundCurve> lines = getLines(point);
                    for (CompoundCurve line : lines) {;
                        for (CurveSegment segment : line.getSegments()) {
                            if (!referencePointSet.contains(segment.getStartPoint()) || !referencePointSet.contains(segment.getEndPoint())) {
                                return new Value(false);
                            }
                        }
                    }
                } else {
                    logger.addEvent(logger.logErrorMsg("Point with unexpected type: {0}.", objectTag));
                    return Value.createUndefined();
                }
            }
        } catch (IoxException e) {
            EhiLogger.logError(e);
            return Value.createUndefined();
        }

        return new Value(true);
    }

    /**
     * Extract the IomObjects with the given attribute from the objects.
     * If attrValue is UNDEFINED, the objects themselves are returned.
     */
    private Collection<IomObject> getAttribute(Value objectsValue, Value attrValue) {
        Collection<IomObject> objects = objectsValue.getComplexObjects();
        if (objects == null) {
            return null;
        }

        if (attrValue.isUndefined()) {
            return objects;
        }

        String attribute = attrValue.getValue();
        if (attribute == null) {
            return null;
        }

        Collection<IomObject> result = new ArrayList<IomObject>();
        for (IomObject object : objects) {
            for (int i = 0; i < object.getattrvaluecount(attribute); i++) {
                result.add(object.getattrobj(attribute, i));
            }
        }

        return result;
    }
}
