package ch.ehi.iox.objpool.impl;

import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

public class SortedSetIntegerSerializer implements Serializer<SortedSet<Integer>> {
	@Override
	public byte[] getBytes(SortedSet<Integer> value) throws IOException {
		byte[] b=new byte[4+4*value.size()];
		int offset=0;
		LongSerializer.integerToBytes(value.size(), b,offset);
		offset+=4;
		for(int v:value){
			LongSerializer.integerToBytes(v, b,offset);
			offset+=4;
		}
		return b;
	}

	@Override
	public SortedSet<Integer> getObject(byte[] bytes) throws IOException, ClassNotFoundException {
		int offset=0;
		int size=LongSerializer.bytesToInteger(bytes,offset);
		offset+=4;
		java.util.SortedSet ret=new TreeSet<Integer>();
		for(int i=0;i<size;i++){
			int value=LongSerializer.bytesToInteger(bytes,offset);
			ret.add(value);
			offset+=4;
		}
		return ret;
	}


}
