package ch.interlis.iox_j.validator.functions;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.types.OutParam;
import ch.interlis.ili2c.metamodel.CoordType;
import ch.interlis.ili2c.metamodel.Element;
import ch.interlis.ili2c.metamodel.Evaluable;
import ch.interlis.ili2c.metamodel.Function;
import ch.interlis.ili2c.metamodel.FunctionCall;
import ch.interlis.ili2c.metamodel.LocalAttribute;
import ch.interlis.ili2c.metamodel.NumericType;
import ch.interlis.ili2c.metamodel.NumericalType;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.ili2c.metamodel.TextType;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.Type;
import ch.interlis.ili2c.metamodel.Viewable;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurveRing;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurvePolygon;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurveSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.JtsextGeometryFactory;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxValidationConfig;
import ch.interlis.iox_j.jts.Iox2jtsext;
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.iox_j.validator.ValidationConfig;
import ch.interlis.iox_j.validator.Validator;
import ch.interlis.iox_j.validator.Value;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.index.ItemVisitor;
import com.vividsolutions.jts.index.strtree.STRtree;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class DmavtymTopologie {
    public static final String DMAVTYM_Topologie_V1_0 = "DMAVTYM_Topologie_V1_0";
    public static final String DMAVTYM_Topologie_V1_1 = "DMAVTYM_Topologie_V1_1";

    private interface GeometryEqualityFunction {
        boolean areEqual(Geometry geom1, Geometry geom2, double tolerance);
    }

    private final TransferDescription td;
    private final IoxValidationConfig validationConfig;
    private final Validator validator;
    private final LogEventFactory logger;
    private final JtsextGeometryFactory geometryFactory = new JtsextGeometryFactory();
    private final HashMap<String, Double> pointToleranceCache = new HashMap<String, Double>();

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
        } else if (currentFunction.getName().equals("hasGeometrySameControlPoints")) {
            return evaluateHasGeometrySameControlPoints(validationKind, usageScope, iomObj, actualArguments);
        } else if (currentFunction.getName().equals("isGeometrySpatiallyEqual")) {
            return evaluateIsGeometrySpatiallyEqual(validationKind, usageScope, iomObj, actualArguments);
        } else if (currentFunction.getName().equals("isInside")) {
            return evaluateIsInside(validationKind, usageScope, iomObj, actualArguments);
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

    private Value evaluateHasGeometrySameControlPoints(String validationKind, String usageScope, IomObject mainObj, Value[] actualArguments) {
        return evaluateGeometryEquality(validationKind, usageScope, mainObj, actualArguments, new GeometryEqualityFunction() {
            @Override
            public boolean areEqual(Geometry geom1, Geometry geom2, double tolerance) {
                geom1.normalize();
                geom2.normalize();
                return geom1.equalsExact(geom2, tolerance);
            }
        });
    }

    private Value evaluateIsGeometrySpatiallyEqual(String validationKind, String usageScope, IomObject mainObj, Value[] actualArguments) {
        return evaluateGeometryEquality(validationKind, usageScope, mainObj, actualArguments, new GeometryEqualityFunction() {
            @Override
            public boolean areEqual(Geometry geom1, Geometry geom2, double tolerance) {
                return geom1.buffer(tolerance).covers(geom2) && geom2.buffer(tolerance).covers(geom1);
            }
        });
    }

    private Value evaluateGeometryEquality(String validationKind, String usageScope, IomObject mainObj, Value[] actualArguments, GeometryEqualityFunction equality) {
        // All arguments must be defined
        for (Value arg : actualArguments) {
            if (arg.isUndefined()) {
                return Value.createSkipEvaluation();
            }
        }

        // Check the type of the arguments
        Collection<IomObject> surface1Objects = actualArguments[0].getComplexObjects();
        String surface1Attr = actualArguments[1].getValue();
        Collection<IomObject> surface2Objects = actualArguments[2].getComplexObjects();
        String surface2Attr = actualArguments[3].getValue();
        double tolerance = actualArguments[4].getNumeric();
        if (surface1Objects == null || surface1Attr == null || surface2Objects == null || surface2Attr == null || tolerance < 0) {
            return Value.createUndefined();
        }

        // Convert the IOM objects to JTS objects
        Geometry surface1;
        Geometry surface2;
        try {
            surface1 = unionSurfaces(surface1Objects, surface1Attr, validationKind);
            surface2 = unionSurfaces(surface2Objects, surface2Attr, validationKind);
            if (surface1 == null || surface2 == null) {
                return Value.createUndefined();
            }
        } catch (IoxException e) {
            EhiLogger.logError(e);
            return Value.createUndefined();
        }

        Envelope envelope1 = surface1.getEnvelopeInternal();
        Envelope envelope2 = surface2.getEnvelopeInternal();
        Envelope expandedEnvelope1 = new Envelope(envelope1);
        expandedEnvelope1.expandBy(tolerance);
        Envelope expandedEnvelope2 = new Envelope(envelope2);
        expandedEnvelope2.expandBy(tolerance);
        if (!expandedEnvelope1.covers(envelope2) || !expandedEnvelope2.covers(envelope1)) {
            return new Value(false);
        }

        return new Value(equality.areEqual(surface1, surface2, tolerance));
    }

    private Geometry unionSurfaces(Collection<IomObject> objects, String attribute, String validationKind) throws IoxException {
        Geometry[] surfaces = new Geometry[objects.size()];
        int i = 0;
        for (IomObject surfaceObject : objects) {
            if (surfaceObject.getattrvaluecount(attribute) != 1) {
                return null;
            }
            surfaces[i] = getSurface(surfaceObject.getattrobj(attribute, 0), validationKind);
            i++;
        }
        GeometryCollection collection = new GeometryCollection(surfaces, geometryFactory);
        return collection.buffer(0);
    }

    private Value evaluateIsInside(String validationKind, String usageScope, IomObject mainObj, Value[] actualArguments) {
        // All arguments must be defined
        for (Value arg : actualArguments) {
            if (arg.isUndefined()) {
                return Value.createSkipEvaluation();
            }
        }

        // Check the type of the arguments
        Collection<IomObject> pointObjects = actualArguments[0].getComplexObjects();
        String pointAttr = actualArguments[1].getValue();
        Collection<IomObject> surfaceObjects = actualArguments[2].getComplexObjects();
        String surfaceAttr = actualArguments[3].getValue();
        if (pointObjects == null || pointObjects.size() != 1 || pointAttr == null
                || surfaceObjects == null || surfaceAttr == null) {
            return Value.createUndefined();
        }

        IomObject pointObject = pointObjects.iterator().next();
        if (pointObject.getattrvaluecount(pointAttr) != 1) {
            return Value.createUndefined();
        }

        // Resolve attributes
        Collection<CurvePolygon> surfaces = new ArrayList<CurvePolygon>(surfaceObjects.size());
        Point point;
        try {
            for (IomObject surfaceObject : surfaceObjects) {
                if (surfaceObject.getattrvaluecount(surfaceAttr) != 1) {
                    return Value.createUndefined();
                }
                surfaces.add(getSurface(surfaceObject.getattrobj(surfaceAttr, 0), validationKind));
            }

            point = getPoint(pointObject.getattrobj(pointAttr, 0));
        } catch (IoxException e) {
            EhiLogger.logError(e);
            return Value.createUndefined();
        }

        double tolerance = getPointTolerance(td, pointObject, pointAttr);

        for (CurvePolygon surface : surfaces) {
            Envelope envelope = surface.getEnvelopeInternal();
            envelope.expandBy(tolerance);
            if (envelope.contains(point.getCoordinate()) && surface.buffer(tolerance).covers(point)) {
                return new Value(true);
            }
        }

        return new Value(false);
    }

    private double getPointTolerance(TransferDescription td, IomObject object, String pointAttribute) {
        String className = object.getobjecttag();
        String qualifiedAttributeName = className + "." + pointAttribute;
        Double cached = pointToleranceCache.get(qualifiedAttributeName);
        if (cached != null) {
            return cached;
        }

        int accuracy = getPointAccuracy(td, className, pointAttribute);
        double precision = Math.pow(10, -accuracy);
        double tolerance = precision * Math.sqrt(2) / 2.0;
        pointToleranceCache.put(qualifiedAttributeName, tolerance);
        return tolerance;
    }

    private int getPointAccuracy(TransferDescription td, String className, String pointAttribute) {
        Element classElement = td.getElement(className);
        if (classElement instanceof Viewable) {
            Viewable<?> viewable = (Viewable<?>) classElement;
            LocalAttribute attrElement = (LocalAttribute) viewable.getElement(LocalAttribute.class, pointAttribute);
            if (attrElement != null) {
                Type attrType = attrElement.getDomainResolvingAliases();
                if (attrType instanceof CoordType) {
                    NumericalType firstDimension = ((CoordType) attrType).getDimensions()[0];
                    if (firstDimension instanceof NumericType) {
                        NumericType numericType = (NumericType) firstDimension;
                        if (numericType.getMinimum() != null) {
                            return numericType.getMinimum().getAccuracy();
                        }
                    }
                }
            }
        }

        logger.addEvent(logger.logWarningMsg("Cannot determine accuracy for point attribute '{0}' in class '{1}'.", pointAttribute, className));
        return 0;
    }

    private Point getPoint(IomObject point) throws IoxException {
        Coordinate coordinate = Iox2jtsext.coord2JTS(point);
        return geometryFactory.createPoint(coordinate);
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
}
