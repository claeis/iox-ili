package ch.ehi.iox.objpool.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.Iom_jObject;

public class IomObjectArraySerializer extends AbstractIomObjectSerializer implements Serializer<List<IomObject>>  {
    
	@Override
	public byte[] getBytes(List<IomObject> iomObjs) throws IOException {
        ByteArrayOutputStream  byteStream = new ByteArrayOutputStream();
		writeInt(byteStream,MAGIC);
		int objc=iomObjs.size();
		writeInt(byteStream,objc);
		for(int i=0;i<objc;i++) {
		    IomObject iomObj=iomObjs.get(i);
	        writeIomObject(byteStream, iomObj);
		}
		writeInt(byteStream,MAGIC);
		return byteStream.toByteArray();
	}

	@Override
	public List<IomObject> getObject(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in=new ByteArrayInputStream(bytes);
		if(readInt(in)!=MAGIC){
			throw new IllegalArgumentException();
		}
		ArrayList<IomObject> ret=new ArrayList<IomObject>();
		int objc=readInt(in);
        for(int i=0;i<objc;i++) {
            IomObject obj = readIomObject(in);
            ret.add(obj);
        }
		if(readInt(in)!=MAGIC){
			throw new IllegalArgumentException();
		}
		return ret;
	}
}
