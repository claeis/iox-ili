package ch.ehi.iox.objpool.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxFactoryCollection;

public class IomObjectSerializer extends AbstractIomObjectSerializer implements Serializer<IomObject> {
    public IomObjectSerializer() {
    }
	public IomObjectSerializer(IoxFactoryCollection ioxFactory) {
        this.ioxFactory=ioxFactory;
    }


    @Override
	public byte[] getBytes(IomObject iomObj) throws IOException {
        ByteArrayOutputStream  byteStream = new ByteArrayOutputStream();
        startObject();
		writeInt(byteStream,MAGIC);
		writeIomObject(byteStream, iomObj);
		writeInt(byteStream,MAGIC);
		endObject();
		return byteStream.toByteArray();
	}


	@Override
	public IomObject getObject(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in=new ByteArrayInputStream(bytes);
        startObject();
		if(readInt(in)!=MAGIC){
			throw new IllegalArgumentException();
		}
		
        IomObject ret = readIomObject(in);
        
		if(readInt(in)!=MAGIC){
			throw new IllegalArgumentException();
		}
        endObject();
		return ret;
	}
}
