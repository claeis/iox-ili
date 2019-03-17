package ch.ehi.iox.objpool.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;

import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iom_j.itf.impl.jtsext.geom.ArcSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurve;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CompoundCurveRing;
import ch.interlis.iom_j.itf.impl.jtsext.geom.CurveSegment;
import ch.interlis.iom_j.itf.impl.jtsext.geom.JtsextGeometryFactory;
import ch.interlis.iom_j.itf.impl.jtsext.geom.StraightSegment;

public abstract class AbstractIomObjectSerializer {
    protected static final int MAGIC=342;
    private static final int PRIMITIVE = 1;
    private static final int COMPLEX = 2;
    private static final int SINGLE_COMPLEX = 3;
    private static final int LINE_STRING = 4;
    private static final int COMPOUND_CURVE = 5;
    private static final int COMPOUND_CURVE_RING = 6;
    private static final byte META_OID = 1;
    private static final byte META_COL = 2;
    private static final byte META_LINE = 4;
    private static final byte META_CONSISTENCY = 8;
    private static final byte META_OPERATION = 16;
    private static final byte META_REFORDERPOS = 32;
    private static final byte META_REFBID = 64;
    private static final byte META_REFOID = -128;
    private HashMap<String,Integer> name2idx=new HashMap<String,Integer>();
    private HashMap<Integer,String> idx2name=new HashMap<Integer,String>();
    private int nameIdx=1;
    protected JtsextGeometryFactory factory=new JtsextGeometryFactory();

    protected void writeIomObject(ByteArrayOutputStream byteStream, IomObject iomObj) {
        writeInt(byteStream,mapName2Idx(iomObj.getobjecttag()));
        byte metaAttrs=0;
		String oid=iomObj.getobjectoid();
		if(oid!=null) {
		    metaAttrs |= META_OID;
		}
        int col = iomObj.getobjectcol();
        if(col!=0) {
            metaAttrs |= META_COL;
        }
        int line = iomObj.getobjectline();
        if(line!=0) {
            metaAttrs |= META_LINE;
        }
        int consistency = iomObj.getobjectconsistency();
        if(consistency!=0) {
            metaAttrs |= META_CONSISTENCY;
        }
        int operation = iomObj.getobjectoperation();
        if(operation!=0) {
            metaAttrs |= META_OPERATION;
        }
        long reforderpos = iomObj.getobjectreforderpos();
        if(reforderpos!=0) {
            metaAttrs |= META_REFORDERPOS;
        }
        String refbid = iomObj.getobjectrefbid();
        if(refbid!=null) {
            metaAttrs |= META_REFBID;
        }
        String refoid = iomObj.getobjectrefoid();
        if(refoid!=null) {
            metaAttrs |= META_REFOID;
        }

        writeByte(byteStream,metaAttrs);
        if(oid!=null) {
            writeString(byteStream,oid);
        }
        if(col!=0) {
            writeInt(byteStream,col);
        }
        if(line!=0) {
            writeInt(byteStream,line);
        }
        if(consistency!=0) {
            writeInt(byteStream,consistency);
        }
        if(operation!=0) {
            writeInt(byteStream,operation);
        }
        if(reforderpos!=0) {
            writeLong(byteStream,reforderpos);
        }
        if(refbid!=null) {
            writeString(byteStream,refbid);
        }
        if(refoid!=null) {
            writeString(byteStream,refoid);
        }
        
        int attrc = iomObj.getattrcount();
        writeInt(byteStream,attrc);
        
        String propNames[]=new String[attrc];
        for(int i=0;i<attrc;i++){
               propNames[i]=iomObj.getattrname(i);
        }
        java.util.Arrays.sort(propNames);
        for(int i=0;i<attrc;i++){
            String propName=propNames[i];
            String value=iomObj.getattrprim(propName,0);
            if(value!=null){
                writeByte(byteStream,PRIMITIVE);
                writeInt(byteStream,mapName2Idx(propName));
                writeString(byteStream,value);
            }else {
                int propc=iomObj.getattrvaluecount(propName);
                if(propc==1) {
                    writeByte(byteStream,SINGLE_COMPLEX);
                    writeInt(byteStream,mapName2Idx(propName));
                    IomObject structvalue=iomObj.getattrobj(propName,0);
                    writeIomObject(byteStream,structvalue);
                }else {
                    writeByte(byteStream,COMPLEX);
                    writeInt(byteStream,mapName2Idx(propName));
                    writeInt(byteStream,propc);
                    for(int propi=0;propi<propc;propi++){
                        IomObject structvalue=iomObj.getattrobj(propName,propi);
                        writeIomObject(byteStream,structvalue);
                    }
                }
                
            }
         }
    }
	
    protected int mapName2Idx(String name) {
        Integer idx=name2idx.get(name);
        if(idx==null) {
            idx=nameIdx++;
            name2idx.put(name, idx);
            idx2name.put(idx, name);
        }
        return idx;
    }
	protected String mapIdx2Name(int idx) {
        String name=idx2name.get(idx);
        if(name==null) {
            throw new IllegalStateException();
        }
        return name;
    }

    protected void writeString(ByteArrayOutputStream byteStream, String userData) {
		if(userData == null || userData.length()==0){
			writeInt(byteStream,0);
		}else{
			try {
				byte[] stringBytes = userData.getBytes(StringSerializer.UTF_8);
	            writeInt(byteStream,stringBytes.length);
                byteStream.write(stringBytes);
			} catch (UnsupportedEncodingException e) {
				throw new IllegalStateException();
			} catch (IOException e) {
				throw new IllegalStateException();
			}
		}
	}
    protected void writeByte(ByteArrayOutputStream byteStream, int value) {
        byteStream.write(value);
    }
    protected void writeInt(ByteArrayOutputStream byteStream, int value) {
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
	protected void writeLong(ByteArrayOutputStream byteStream, long value) {
        byte[] bytes=new byte[8];
        LongSerializer.longToBytes(value, bytes);
        try {
            byteStream.write(bytes);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException();
        } catch (IOException e) {
            throw new IllegalStateException();
        }
    }

    protected IomObject readIomObject(ByteArrayInputStream in) {
        String tag=mapIdx2Name(readInt(in));

        int metaAttrs=readByte(in);
        String oid=null;
        if((metaAttrs&META_OID) != 0) {
            oid=readString(in);
        }
        int col = 0;
        if((metaAttrs&META_COL) != 0) {
            col=readInt(in);
        }
        int line = 0;
        if((metaAttrs&META_LINE) != 0) {
            line=readInt(in);
        }
        int consistency =0;
        if((metaAttrs&META_CONSISTENCY) != 0) {
            consistency=readInt(in);
        }
        int operation = 0;
        if((metaAttrs&META_OPERATION) != 0) {
            operation=readInt(in);
        }
        long reforderpos = 0;
        if((metaAttrs&META_REFORDERPOS) != 0) {
            reforderpos=readLong(in);
        }
        String refbid = null;
        if((metaAttrs&META_REFBID) != 0) {
            refbid=readString(in);
        }
        String refoid = null;
        if((metaAttrs&META_REFOID) != 0) {
            refoid=readString(in);
        }
        
        
        IomObject ret=new Iom_jObject(tag, oid);
        if(col!=0) {
            ret.setobjectcol(col);
        }
        if(line!=0) {
            ret.setobjectline(line);
        }
        if(consistency!=0) {
            ret.setobjectconsistency(consistency);
        }
        if(operation!=0) {
            ret.setobjectoperation(operation);
        }
        if(reforderpos!=0) {
            ret.setobjectreforderpos(reforderpos);
        }
        if(refbid!=null) {
            ret.setobjectrefbid(refbid);
        }
        if(refoid!=null) {
            ret.setobjectrefoid(refoid);
        }
        
		int attrc=readInt(in);
		
        String propNames[]=new String[attrc];
        for(int i=0;i<attrc;i++){
            int propType=readByte(in);
            if(propType==PRIMITIVE){
                String propName=propNames[i]=mapIdx2Name(readInt(in));
                String value=readString(in);
                ret.setattrvalue(propName,value);
            }else if(propType==SINGLE_COMPLEX){
                String propName=propNames[i]=mapIdx2Name(readInt(in));
                IomObject structValue=readIomObject(in);
                ret.addattrobj(propName, structValue);
            }else if(propType==COMPLEX){
                String propName=propNames[i]=mapIdx2Name(readInt(in));
                int propc=readInt(in);
                for(int propi=0;propi<propc;propi++){
                    IomObject structValue=readIomObject(in);
                    ret.addattrobj(propName, structValue);
                }
            }else {
                throw new IllegalStateException();
            }
         }
        return ret;
    }

    protected String readString(ByteArrayInputStream in) {
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
    protected int readByte(ByteArrayInputStream byteStream) {
        int ret=byteStream.read();
        if(ret==-1){
            throw new IllegalStateException();
        }
        return ret;
	}
	protected int readInt(ByteArrayInputStream byteStream) {
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
    protected long readLong(ByteArrayInputStream byteStream) {
        byte[] bytes=new byte[8];
        try {
            if(byteStream.read(bytes)!=bytes.length){
                throw new IllegalStateException();
            }
            return LongSerializer.bytesToLong(bytes);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException();
        } catch (IOException e) {
            throw new IllegalStateException();
        }
    }
    protected void writeLineString(ByteArrayOutputStream byteStream, LineString shell) {
        if(shell instanceof CompoundCurveRing) {
            writeByte(byteStream,COMPOUND_CURVE_RING);
             CompoundCurveRing ring=(CompoundCurveRing)shell;
             ArrayList<CompoundCurve> lines = ring.getLines();
             int linec = lines.size();
             writeInt(byteStream,linec);
             for(int i=0;i<linec;i++) {
                 writeCompoundCurve(byteStream, lines.get(i));
             }
         }else if(shell instanceof CompoundCurve) {
             writeByte(byteStream,COMPOUND_CURVE);
             writeCompoundCurve(byteStream, (CompoundCurve)shell);
         }else {
             writeByte(byteStream,LINE_STRING);
             Coordinate[] coords = shell.getCoordinates();
             writeInt(byteStream,coords.length);
             for(int i=0;i<coords.length;i++) {
                 writeCoord(byteStream, coords[i]);
             }
         }
    }
    protected LineString readLineString(ByteArrayInputStream in) {
        LineString ret=null;
        int type=readByte(in);
        if(type==COMPOUND_CURVE_RING) {
             int linec=readInt(in);
             ArrayList<CompoundCurve> lines = new ArrayList<CompoundCurve>();
             for(int i=0;i<linec;i++) {
                 CompoundCurve curve=readCompoundCurve(in);
                 lines.add(curve);
             }
             ret=new CompoundCurveRing(lines,factory);
         }else if(type==COMPOUND_CURVE) {
             ret=readCompoundCurve(in);
         }else if(type==LINE_STRING){
             int coordc=readInt(in);
             Coordinate[] coords = new Coordinate[coordc];
             for(int i=0;i<coords.length;i++) {
                 coords[i]=readCoord(in);
             }
             ret=factory.createLineString(coords);
         }else {
             throw new IllegalStateException();
         }
        return ret;
    }
    protected void writeCompoundCurve(ByteArrayOutputStream byteStream, CompoundCurve value) {
        int segc=value.getNumSegments();
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
        writeString(byteStream,(String) value.getUserData());
    }
    protected CompoundCurve readCompoundCurve(ByteArrayInputStream in) {
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
        String userData=readString(in);
        CompoundCurve ret=new CompoundCurve(segs, factory);
        ret.setUserData(userData);
        return ret;
    }
    protected void writeCoord(ByteArrayOutputStream byteStream,
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
    protected Coordinate readCoord(ByteArrayInputStream in) {
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
