package ch.ehi.iox.objpool.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

import ch.interlis.iom_j.itf.impl.jtsext.geom.ArcSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurveRing;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurvePolygon;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurveSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.JtsextGeometryFactory;
import ch.interlis.iom_j.itf.impl.jtsext.geom.StraightSegment;

public class PolygonSerializer extends AbstractIomObjectSerializer implements Serializer<Polygon> {
	@Override
	public byte[] getBytes(Polygon value) throws IOException {
        ByteArrayOutputStream  byteStream = new ByteArrayOutputStream();
        writeInt(byteStream,MAGIC);
         LineString shell = value.getExteriorRing();
         writeLineString(byteStream, shell);
         int holec=value.getNumInteriorRing();
         writeInt(byteStream,holec);
         for(int i=0;i<holec;i++) {
             LineString hole=value.getInteriorRingN(i);
             writeLineString(byteStream, hole);             
         }
		writeInt(byteStream,MAGIC);
		return byteStream.toByteArray();
	}

	@Override
	public Polygon getObject(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in=new ByteArrayInputStream(bytes);
		if(readInt(in)!=MAGIC){
			throw new IllegalArgumentException();
		}
		LinearRing shell=(LinearRing) readLineString(in);
		int holec=readInt(in);
		LinearRing holes[]=new LinearRing[holec];
        for(int i=0;i<holec;i++) {
            holes[i]=(LinearRing) readLineString(in);             
        }
        Polygon ret=null;
        if(shell instanceof CompoundCurveRing) {
            ret=factory.createCurvePolygon(shell, holes);
        }else {
            ret=factory.createPolygon(shell, holes);
        }
		if(readInt(in)!=MAGIC){
			throw new IllegalArgumentException();
		}
		return ret;
	}
}
