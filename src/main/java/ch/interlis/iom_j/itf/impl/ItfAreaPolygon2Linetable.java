package ch.interlis.iom_j.itf.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.ws.Holder;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.iox.objpool.ObjectPoolManager;
import ch.ehi.iox.objpool.impl.CompoundCurveSerializer;
import ch.ehi.iox.objpool.impl.FileBasedCollection;
import ch.ehi.iox.objpool.impl.JavaSerializer;
import ch.interlis.iom.IomConstants;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.noding.CompoundCurveDissolver;
import ch.interlis.iom_j.itf.impl.jtsext.noding.CompoundCurveNoder;
import ch.interlis.iom_j.itf.impl.jtsext.noding.Intersection;
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.jts.Iox2jtsException;
import ch.interlis.iox_j.jts.Iox2jtsext;
import ch.interlis.iox_j.jts.Jtsext2iox;
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.iox_j.validator.ValidationConfig;

public class ItfAreaPolygon2Linetable {

	private Collection<? extends CompoundCurve> lines=null;
	private Collection<IomObject> ioxlines=null;
	private ObjectPoolManager recman=null;
	public ItfAreaPolygon2Linetable(ObjectPoolManager recman1){
		lines=new FileBasedCollection<CompoundCurve>(recman1,new CompoundCurveSerializer());
		recman=recman1;
	}
	public void addLines(String mainObjTid,String internalTid,ArrayList<IomObject> ioxlines) throws IoxException {
		for(IomObject ioxline:ioxlines){
			CompoundCurve line=Iox2jtsext.polyline2JTS(ioxline, false, 0.0);
			if(internalTid!=null){
				line.setUserData(internalTid);
			}else{
				line.setUserData(mainObjTid);
			}
			((Collection)lines).add(line);
		}
	}
	public void addLines(String mainObjTid,String internalTid,ArrayList<IomObject> ioxlines,String validationType,LogEventFactory errs) throws IoxException {
		for(IomObject ioxline:ioxlines){
			Holder<Boolean> foundErrs=new Holder<Boolean>();
			CompoundCurve line=Iox2jtsext.polyline2JTS(ioxline, false, 0.0,foundErrs,errs,0.0,validationType,ValidationConfig.WARNING);
			if(line!=null){
				if(internalTid!=null){
					line.setUserData(internalTid);
				}else{
					line.setUserData(mainObjTid);
				}
				((Collection)lines).add(line);
			}
		}
	}

	public List<Intersection> validate()  {
		CompoundCurveNoder noder=new CompoundCurveNoder(recman,(java.util.List)lines,false);
		if(!noder.isValid()){
			return noder.getIntersections();
		}
		return null;
	}
	public java.util.List<IomObject> getLines() throws IoxException {
		if(ioxlines==null){
			{
				CompoundCurveNoder noder=new CompoundCurveNoder(recman,(java.util.List)lines,false);
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
			//CompoundCurveDissolver dissolver=new CompoundCurveDissolver();
			//dissolver.dissolve(lines);
			//lines=dissolver.getDissolved();
			//ioxlines=new ArrayList<IomObject>();
			ioxlines=new FileBasedCollection<IomObject>(recman,new JavaSerializer());
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
		return (List<IomObject>) ioxlines;
	}

	public static ArrayList<IomObject> getLinesFromPolygon(IomObject polygon)
	{
		ArrayList<IomObject> ret=new ArrayList<IomObject>();
		boolean clipped=polygon.getobjectconsistency()==IomConstants.IOM_INCOMPLETE;
		for(int surfacei=0;surfacei<polygon.getattrvaluecount("surface");surfacei++){
			if(clipped){
				throw new IllegalArgumentException("clipped surface not supported");
			}else{
				// an unclipped surface should have only one surface element
				if(surfacei>0){
					throw new IllegalArgumentException("unclipped surface with multi 'surface' elements");
				}
			}
			IomObject surface=polygon.getattrobj("surface",surfacei);
			for(int boundaryi=0;boundaryi<surface.getattrvaluecount("boundary");boundaryi++){
				IomObject boundary=surface.getattrobj("boundary",boundaryi);
				for(int polylinei=0;polylinei<boundary.getattrvaluecount("polyline");polylinei++){
					IomObject polyline=boundary.getattrobj("polyline",polylinei);
					ret.add(polyline);
				}
			}
			if(clipped){
			}
		}
		return ret;
	}

}
