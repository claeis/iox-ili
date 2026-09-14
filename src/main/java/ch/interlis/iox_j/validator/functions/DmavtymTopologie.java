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
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.index.ItemVisitor;
import com.vividsolutions.jts.index.strtree.STRtree;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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
    private final HashMap<UnionKey, Geometry> unionCache = new HashMap<UnionKey, Geometry>();

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
        } else if (currentFunction.getName().equals("pointInPoints")) {
            return evaluatePointInPoints(validationKind, usageScope, iomObj, actualArguments);
        } else if (currentFunction.getName().equals("geometricFilter")) {
            return evaluateGeometricFilter(validationKind, usageScope, iomObj, actualArguments);
        } else {
            return Value.createNotYetImplemented();
        }
    }

    private Value evaluateCovers(String validationKind, String usageScope, IomObject mainObj, Value[] actualArguments) {
        // Extract argument values
        Collection<IomObject> referenceObjects = actualArguments[0].isUndefined() ? null : actualArguments[0].getComplexObjects();
        String referenceAttr = actualArguments[1].isUndefined() ? null : actualArguments[1].getValue();
        Collection<IomObject> multiLineObjects = actualArguments[2].isUndefined() ? null : actualArguments[2].getComplexObjects();
        String multiLineAttr = actualArguments[3].isUndefined() ? null : actualArguments[3].getValue();

        // No reference geometry: the lines cannot be covered
        if (referenceObjects == null || referenceObjects.isEmpty()) {
            return new Value(false);
        }

        // No lines: there cannot be an unmatched line so return true
        if (multiLineObjects == null || multiLineObjects.isEmpty()) {
            return new Value(true);
        }

        // Convert the IOM objects to JTS objects
        Collection<CompoundCurve> referenceLines = new ArrayList<CompoundCurve>();
        Collection<CompoundCurve> lines = new ArrayList<CompoundCurve>();
        try {
            for (IomObject referenceObject : referenceObjects) {
                IomObject attrValue;
                if (referenceAttr == null) {
                    attrValue = referenceObject;
                } else {
                    if (referenceObject.getattrvaluecount(referenceAttr) != 1) {
                        return Value.createUndefined();
                    }
                    attrValue = referenceObject.getattrobj(referenceAttr, 0);
                }
                String objectTag = attrValue.getobjecttag();
                if (objectTag.equals(Iom_jObject.MULTISURFACE)) {
                    for (CurvePolygon surface : getSurfaces(attrValue, validationKind)) {
                        for (CompoundCurveRing ring : getRings(surface)) {
                            referenceLines.addAll(ring.getLines());
                        }
                    }
                } else if (objectTag.equals(Iom_jObject.MULTIPOLYLINE) || objectTag.equals(Iom_jObject.POLYLINE)) {
                    referenceLines.addAll(getLines(attrValue));
                } else {
                    return Value.createUndefined();
                }
            }
            for (IomObject multiLineObject : multiLineObjects) {
                if (multiLineAttr == null) {
                    lines.addAll(getLines(multiLineObject));
                } else {
                    if (multiLineObject.getattrvaluecount(multiLineAttr) != 1) {
                        return Value.createUndefined();
                    }
                    lines.addAll(getLines(multiLineObject.getattrobj(multiLineAttr, 0)));
                }
            }
        } catch (Exception e) {
            EhiLogger.logError(e);
            return Value.createUndefined();
        }

        // Add all reference segments to a hashset
        HashMap<CurveSegment, Boolean> referenceSegments = new HashMap<CurveSegment, Boolean>();
        for (CompoundCurve referenceLine : referenceLines) {
            for (CurveSegment segment : referenceLine.getSegments()) {
                referenceSegments.put(segment, false);
            }
        }

        // Check if all multiline lines are contained in the referenceSegments hashset
        boolean result = true;
        for (CompoundCurve line : lines) {
            for (CurveSegment segment : line.getSegments()) {
                Boolean isSegmentVisited = referenceSegments.get(segment);
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
                    referenceSegments.put(segment, true);
                }
            }
        }

        return new Value(result);
    }

    private Value evaluateCoversWithTolerance(String validationKind, String usageScope, IomObject mainObj, Value[] actualArguments) {
        // Required argument tolerance
        if (actualArguments[4].isUndefined()) {
            return Value.createSkipEvaluation();
        }

        // Extract argument values
        Collection<IomObject> referenceObjects = actualArguments[0].isUndefined() ? null : actualArguments[0].getComplexObjects();
        String referenceAttr = actualArguments[1].isUndefined() ? null : actualArguments[1].getValue();
        Collection<IomObject> multiLineObjects = actualArguments[2].isUndefined() ? null : actualArguments[2].getComplexObjects();
        String multiLineAttr = actualArguments[3].isUndefined() ? null : actualArguments[3].getValue();
        double tolerance = actualArguments[4].getNumeric();
        if (tolerance < 0) {
            return Value.createUndefined();
        }

        // No reference geometry: the lines cannot be covered
        if (referenceObjects == null || referenceObjects.isEmpty()) {
            return new Value(false);
        }

        // No lines: there cannot be an unmatched line so return true
        if (multiLineObjects == null || multiLineObjects.isEmpty()) {
            return new Value(true);
        }

        // Convert the IOM objects to JTS objects; each ring of a surface and each (multi)polyline
        // is a separate reference geometry
        Collection<Collection<CompoundCurve>> referenceMultiLines = new ArrayList<Collection<CompoundCurve>>();
        Collection<Collection<CompoundCurve>> multiLines = new ArrayList<Collection<CompoundCurve>>(multiLineObjects.size());
        try {
            for (IomObject referenceObject : referenceObjects) {
                IomObject attrValue;
                if (referenceAttr == null) {
                    attrValue = referenceObject;
                } else {
                    if (referenceObject.getattrvaluecount(referenceAttr) != 1) {
                        return Value.createUndefined();
                    }
                    attrValue = referenceObject.getattrobj(referenceAttr, 0);
                }
                String objectTag = attrValue.getobjecttag();
                if (objectTag.equals(Iom_jObject.MULTISURFACE)) {
                    for (CurvePolygon surface : getSurfaces(attrValue, validationKind)) {
                        for (CompoundCurveRing ring : getRings(surface)) {
                            referenceMultiLines.add(ring.getLines());
                        }
                    }
                } else if (objectTag.equals(Iom_jObject.MULTIPOLYLINE) || objectTag.equals(Iom_jObject.POLYLINE)) {
                    referenceMultiLines.add(getLines(attrValue));
                } else {
                    return Value.createUndefined();
                }
            }

            for (IomObject multiLineObject : multiLineObjects) {
                if (multiLineAttr == null) {
                    multiLines.add(getLines(multiLineObject));
                } else {
                    if (multiLineObject.getattrvaluecount(multiLineAttr) != 1) {
                        return Value.createUndefined();
                    }
                    multiLines.add(getLines(multiLineObject.getattrobj(multiLineAttr, 0)));
                }
            }
        } catch (IoxException e) {
            EhiLogger.logError(e);
            return Value.createUndefined();
        }

        // Each multiline must lie on a single reference geometry
        for (Collection<CompoundCurve> multiLine : multiLines) {
            if (!isCoveredWithTolerance(referenceMultiLines, multiLine, tolerance)) {
                return new Value(false);
            }
        }
        return new Value(true);
    }

    private boolean isCoveredWithTolerance(Collection<Collection<CompoundCurve>> referenceMultiLines, Collection<CompoundCurve> multiLine, double tolerance) {
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
                return true;
            }
        }
        return false;
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
        Collection<String> objectIds = new ArrayList<String>(objects.size());
        for (IomObject obj : objects) {
            objectIds.add(obj.getobjectoid());
        }
        UnionKey key = new UnionKey(objectIds, attribute);
        Geometry cached = unionCache.get(key);
        if (cached != null) {
            return cached;
        }

        Geometry union = calculateUnion(objects, attribute, validationKind);
        unionCache.put(key, union);
        return union;
    }

    private Geometry calculateUnion(Collection<IomObject> objects, String attribute, String validationKind) throws IoxException {
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

    private MultiPolygon getMultiSurface(IomObject multiSurface, String validationKind) throws IoxException {
        return Iox2jtsext.multisurface2JTS(multiSurface, 0.0, new OutParam<Boolean>(), logger, 0.0, validationKind);
    }

    /**
     * Converts a SURFACE or MULTISURFACE value to its polygons.
     */
    private Collection<CurvePolygon> getSurfaces(IomObject multiSurface, String validationKind) throws IoxException {
        MultiPolygon multiPolygon = getMultiSurface(multiSurface, validationKind);
        Collection<CurvePolygon> surfaces = new ArrayList<CurvePolygon>(multiPolygon.getNumGeometries());
        for (int i = 0; i < multiPolygon.getNumGeometries(); i++) {
            surfaces.add((CurvePolygon) multiPolygon.getGeometryN(i));
        }
        return surfaces;
    }

    /**
     * Returns the outer and the inner rings of a surface.
     */
    private Collection<CompoundCurveRing> getRings(CurvePolygon surface) {
        Collection<CompoundCurveRing> rings = new ArrayList<CompoundCurveRing>(surface.getNumInteriorRing() + 1);
        rings.add((CompoundCurveRing) surface.getExteriorRing());
        for (int i = 0; i < surface.getNumInteriorRing(); i++) {
            rings.add((CompoundCurveRing) surface.getInteriorRingN(i));
        }
        return rings;
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
                    for (CompoundCurveRing ring : getRings(surface)) {
                        for (CompoundCurve line : ring.getLines()) {
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

    private Value evaluateGeometricFilter(String validationKind, String usageScope, IomObject mainObj, Value[] actualArguments) {
        // All arguments must be defined
        for (Value arg : actualArguments) {
            if (arg.isUndefined()) {
                return Value.createSkipEvaluation();
            }
        }

        // Check the type of the arguments
        Collection<IomObject> pointObjects = actualArguments[0].getComplexObjects();
        String pointAttr = actualArguments[1].getValue();
        Collection<IomObject> objects = actualArguments[2].getComplexObjects();
        String surfaceAttr = actualArguments[3].getValue();
        if (pointObjects == null || pointObjects.size() != 1 || pointAttr == null
                || objects == null || surfaceAttr == null) {
            return Value.createUndefined();
        }

        IomObject pointObject = pointObjects.iterator().next();
        if (pointObject.getattrvaluecount(pointAttr) != 1) {
            return Value.createUndefined();
        }

        // Extract point geometry
        Point point;
        try {
            point = getPoint(pointObject.getattrobj(pointAttr, 0));
        } catch (IoxException e) {
            EhiLogger.logError(e);
            return Value.createUndefined();
        }

        double tolerance = getPointTolerance(td, pointObject, pointAttr);

        // Filter objects
        ArrayList<IomObject> filteredObjects = new ArrayList<IomObject>();
        for (IomObject object : objects) {
            if (object.getattrvaluecount(surfaceAttr) != 1) {
                continue;
            }

            try {
                MultiPolygon surface = getMultiSurface(object.getattrobj(surfaceAttr, 0), validationKind);
                Envelope envelope = surface.getEnvelopeInternal();
                envelope.expandBy(tolerance);
                if (envelope.contains(point.getCoordinate()) && surface.buffer(tolerance).covers(point)) {
                    filteredObjects.add(object);
                }
            } catch (IoxException e) {
                EhiLogger.logError(e);
                // Continue with next object
            }
        }

        return new Value(filteredObjects);
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

    private static final class UnionKey {
        private final Collection<String> objectIds;
        private final String attribute;

        public UnionKey(Collection<String> objectIds, String attribute) {
            this.objectIds = objectIds;
            this.attribute = attribute;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            UnionKey other = (UnionKey) obj;
            return attribute.equals(other.attribute) && objectIds.equals(other.objectIds);
        }

        @Override
        public int hashCode() {
            int result = objectIds.hashCode();
            result = 31 * result + attribute.hashCode();
            return result;
        }
    }
}
