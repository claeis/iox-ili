package ch.ehi.iox.objpool.impl;

import java.io.IOException;

public class StringSerializer implements Serializer<String> {

	public final static String UTF_8="UTF-8";
	@Override
	public byte[] getBytes(String value) throws IOException {
		return value.getBytes( UTF_8);
	}

	@Override
	public String getObject(byte[] bytes) throws IOException, ClassNotFoundException {
		return new String( bytes, UTF_8 );
	}

}
