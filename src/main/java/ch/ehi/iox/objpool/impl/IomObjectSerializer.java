package ch.ehi.iox.objpool.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import ch.interlis.iom.IomObject;

public class IomObjectSerializer extends AbstractIomObjectSerializer implements Serializer<IomObject> {
	@Override
	public byte[] getBytes(IomObject iomObj) throws IOException {
        ByteArrayOutputStream  byteStream = new ByteArrayOutputStream();
		writeInt(byteStream,MAGIC);
		writeIomObject(byteStream, iomObj);
		writeInt(byteStream,MAGIC);
		return byteStream.toByteArray();
	}


	@Override
	public IomObject getObject(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in=new ByteArrayInputStream(bytes);
		if(readInt(in)!=MAGIC){
			throw new IllegalArgumentException();
		}
		
        IomObject ret = readIomObject(in);
        
		if(readInt(in)!=MAGIC){
			throw new IllegalArgumentException();
		}
		return ret;
	}
}
