package ch.ehi.iox.objpool.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.vividsolutions.jts.geom.Coordinate;

import ch.interlis.iom_j.itf.impl.jtsext.geom.ArcSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurveSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.JtsextGeometryFactory;
import ch.interlis.iom_j.itf.impl.jtsext.geom.StraightSegment;

public class CompoundCurveSerializer extends AbstractIomObjectSerializer implements Serializer<CompoundCurve> {
	@Override
	public byte[] getBytes(CompoundCurve value) throws IOException {
        ByteArrayOutputStream  byteStream = new ByteArrayOutputStream();
		writeInt(byteStream,MAGIC);
        writeCompoundCurve(byteStream, value);
		writeInt(byteStream,MAGIC);
		return byteStream.toByteArray();
	}
	

	@Override
	public CompoundCurve getObject(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in=new ByteArrayInputStream(bytes);
		if(readInt(in)!=MAGIC){
			throw new IllegalArgumentException();
		}
		CompoundCurve ret = readCompoundCurve(in);
		if(readInt(in)!=MAGIC){
			throw new IllegalArgumentException();
		}
		return ret;
	}



}
