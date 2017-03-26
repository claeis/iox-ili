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

public class CompoundCurveSerializer implements Serializer<CompoundCurve> {
	JtsextGeometryFactory factory=new JtsextGeometryFactory();
	@Override
	public byte[] getBytes(CompoundCurve value) throws IOException {
        ByteArrayOutputStream  byteStream = new ByteArrayOutputStream();
		int segc=value.getNumSegments();
		writeInt(byteStream,MAGIC);
		writeInt(byteStream,segc);
		for(CurveSegment seg:value.getSegments()){
			if(seg instanceof StraightSegment){
				byteStream.write(1);
				writeCoord(byteStream,seg.getStartPoint());
				writeCoord(byteStream,seg.getEndPoint());
				writeString(byteStream,(String) seg.getUserData());
			}else if(seg instanceof ArcSegment){
				byteStream.write(2);
				writeCoord(byteStream,seg.getStartPoint());
				writeCoord(byteStream,((ArcSegment) seg).getMidPoint());
				writeCoord(byteStream,seg.getEndPoint());
				writeString(byteStream,(String) seg.getUserData());
			}else{
				throw new IllegalStateException();
			}
		}
		writeInt(byteStream,MAGIC);
		return byteStream.toByteArray();
	}
	private static final int MAGIC=342;
	
	private void writeString(ByteArrayOutputStream byteStream, String userData) {
		if(userData == null || userData.length()==0){
			writeInt(byteStream,0);
		}else{
			writeInt(byteStream,userData.length());
			try {
				byteStream.write(userData.getBytes(StringSerializer.UTF_8));
			} catch (UnsupportedEncodingException e) {
				throw new IllegalStateException();
			} catch (IOException e) {
				throw new IllegalStateException();
			}
		}
	}
	private void writeInt(ByteArrayOutputStream byteStream, int value) {
		byte[] bytes=new byte[4];
		LongSerializer.integerToBytes(value, bytes, 0);
		try {
			byteStream.write(bytes);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException();
		} catch (IOException e) {
			throw new IllegalStateException();
		}
	}

	private void writeCoord(ByteArrayOutputStream byteStream,
			Coordinate c) {
		byte[] b=new byte[8];
		try {
			LongSerializer.doubleToBytes(c.x,b);
			byteStream.write(b);
			LongSerializer.doubleToBytes(c.y,b);
			byteStream.write(b);
			LongSerializer.doubleToBytes(c.z,b);
			byteStream.write(b);
		} catch (IOException e) {
			throw new IllegalStateException();
		}
		
	}

	@Override
	public CompoundCurve getObject(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in=new ByteArrayInputStream(bytes);
		if(readInt(in)!=MAGIC){
			throw new IllegalArgumentException();
		}
		int segc=readInt(in);
		ArrayList<CurveSegment> segs=new ArrayList<CurveSegment>(segc);
		for(int segi=0;segi<segc;segi++){
			int segType=in.read();
			CurveSegment seg=null;
			if(segType==1){
				Coordinate startPt=readCoord(in);
				Coordinate endPt=readCoord(in);
				String userData=readString(in);
				seg=new StraightSegment(userData,startPt,endPt);
			}else if(segType==2){
				Coordinate startPt=readCoord(in);
				Coordinate midPt=readCoord(in);
				Coordinate endPt=readCoord(in);
				String userData=readString(in);
				seg=new ArcSegment(userData,startPt,midPt,endPt);
			}else{
				throw new IllegalStateException();
			}
			segs.add(seg);
		}
		if(readInt(in)!=MAGIC){
			throw new IllegalArgumentException();
		}
		CompoundCurve ret=new CompoundCurve(segs, factory);
		return ret;
	}

	private String readString(ByteArrayInputStream in) {
		int s=readInt(in);
		if(s==0){
			return null;
		}
		byte[] b=new byte[s];
		try {
			if(in.read(b)!=s){
				throw new IllegalStateException();
			}
			return new String(b,StringSerializer.UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException();
		} catch (IOException e) {
			throw new IllegalStateException();
		}
	}
	private int readInt(ByteArrayInputStream byteStream) {
		byte[] bytes=new byte[4];
		try {
			if(byteStream.read(bytes)!=bytes.length){
				throw new IllegalStateException();
			}
			return LongSerializer.bytesToInteger(bytes, 0);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException();
		} catch (IOException e) {
			throw new IllegalStateException();
		}
	}

	private Coordinate readCoord(ByteArrayInputStream in) {
		byte[] b=new byte[8];
		try {
			if(in.read(b)!=b.length){
				throw new IllegalStateException();
			}
			double x=LongSerializer.bytesToDouble(b);
			if(in.read(b)!=b.length){
				throw new IllegalStateException();
			}
			double y=LongSerializer.bytesToDouble(b);
			if(in.read(b)!=b.length){
				throw new IllegalStateException();
			}
			double z=LongSerializer.bytesToDouble(b);
			return new Coordinate(x,y,z);
		} catch (IOException e) {
			throw new IllegalStateException();
		}
	}
}
