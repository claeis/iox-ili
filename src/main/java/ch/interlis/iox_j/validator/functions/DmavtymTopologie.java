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
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class DmavtymTopologie {
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
        Collection<IomObject> surfaceObjects = actualArguments[0].getComplexObjects();
        String surfaceAttr = actualArguments[1].getValue();
        Collection<IomObject> multiLineObjects = actualArguments[2].getComplexObjects();
        String multiLineAttr = actualArguments[3].getValue();
        double tolerance = actualArguments[4].getNumeric();
        if (surfaceObjects == null || surfaceAttr == null
                || multiLineObjects == null || multiLineObjects.size() != 1 || multiLineAttr == null
                || tolerance < 0) {
            return Value.createUndefined();
        }

        // Resolve lines
        IomObject multiLineObject = multiLineObjects.iterator().next();
        if (multiLineObject.getattrvaluecount(multiLineAttr) != 1) {
            return Value.createUndefined();
        }

        // Convert the IOM objects to JTS objects
        Collection<CurvePolygon> surfaces = new ArrayList<CurvePolygon>(surfaceObjects.size());
        Collection<CompoundCurve> lines;
        try {
            for (IomObject surfaceObject : surfaceObjects) {
                if (surfaceObject.getattrvaluecount(surfaceAttr) != 1) {
                    return Value.createUndefined();
                }
                surfaces.add(getSurface(surfaceObject.getattrobj(surfaceAttr, 0), validationKind));
            }

            lines = getLines(multiLineObject.getattrobj(multiLineAttr, 0));
        } catch (IoxException e) {
            EhiLogger.logError(e);
            return Value.createUndefined();
        }

        Envelope linesEnvelope = new Envelope();
        for (CompoundCurve line : lines) {
            linesEnvelope.expandToInclude(line.getEnvelopeInternal());
        }
        linesEnvelope.expandBy(tolerance);

        for (CurvePolygon surface : surfaces) {
            if (surface.getEnvelopeInternal().intersects(linesEnvelope) && coversWithTolerance(surface, lines, tolerance)) {
                return new Value(true);
            }
        }

        return new Value(false);
    }

    private boolean coversWithTolerance(CurvePolygon surface, Collection<CompoundCurve> lines, double tolerance) {
        LineString surfaceOutline = surface.getExteriorRing();
        Geometry bufferedOutline = surfaceOutline.buffer(tolerance);
        if (coversAllLines(bufferedOutline, lines)) {
            return true;
        }

        for (int i = 0; i < surface.getNumInteriorRing(); i++) {
            LineString surfaceInterior = surface.getInteriorRingN(i);
            Geometry bufferedInterior = surfaceInterior.buffer(tolerance);
            if (coversAllLines(bufferedInterior, lines)) {
                return true;
            }
        }
        return false;
    }

    private boolean coversAllLines(Geometry geometry, Collection<CompoundCurve> lines) {
        for (CompoundCurve line : lines) {
            if (!geometry.covers(line)) {
                return false;
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
}
