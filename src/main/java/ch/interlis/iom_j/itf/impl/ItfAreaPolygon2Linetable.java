package ch.interlis.iom_j.itf.impl;

import java.util.ArrayList;
import java.util.Collection;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.noding.CompoundCurveDissolver;
import ch.interlis.iom_j.itf.impl.jtsext.noding.CompoundCurveNoder;
import ch.interlis.iom_j.itf.impl.jtsext.noding.Intersection;
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.jts.Iox2jtsException;
import ch.interlis.iox_j.jts.Iox2jtsext;
import ch.interlis.iox_j.jts.Jtsext2iox;

public class ItfAreaPolygon2Linetable {

	private Collection<? extends CompoundCurve> lines=new ArrayList<CompoundCurve>();
	private ArrayList<IomObject> ioxlines=null;
	public void addLines(String mainObjTid,String internalTid,ArrayList<IomObject> ioxlines) throws IoxException {
		for(IomObject ioxline:ioxlines){
			CompoundCurve line=Iox2jtsext.polyline2JTS(ioxline, false, 0.0);
			if(internalTid!=null){
				line.setUserData(internalTid);
			}else{
				line.setUserData(mainObjTid);
			}
			((ArrayList<CompoundCurve>)lines).add(line);
		}
	}

	public ArrayList<IomObject> getLines() throws IoxException {
		if(ioxlines==null){
			{
				CompoundCurveNoder noder=new CompoundCurveNoder(lines,false);
				if(!noder.isValid()){
					for(Intersection is:noder.getIntersections()){
						EhiLogger.logError("intersection tid1 "+is.getCurve1().getUserData()+", tid2 "+is.getCurve2().getUserData()+", coord "+is.getPt()[0].toString()+(is.getPt().length==2?(", coord2 "+is.getPt()[1].toString()):""));
						EhiLogger.traceState("overlap "+is.getOverlap()+", seg1 "+is.getSegment1()+", seg2 "+is.getSegment2());
					}
					throw new IoxException("intersections");
				}
				lines=noder.getNodedSubstrings();
				noder=null;
			}
			CompoundCurveDissolver dissolver=new CompoundCurveDissolver();
			dissolver.dissolve(lines);
			lines=dissolver.getDissolved();
			ioxlines=new ArrayList<IomObject>();
			for(CompoundCurve line:lines){
				IomObject ioxline;
				try {
					ioxline = Jtsext2iox.JTS2polyline(line);
				} catch (Iox2jtsException e) {
					throw new IoxException(e);
				}
				ioxlines.add(ioxline);
			}
		}
		return ioxlines;
	}

}
