package ch.ehi.iox.objpool.impl;

import java.lang.ref.ReferenceQueue;

public class ObjPoolEntry extends java.lang.ref.SoftReference{

	private byte[]buffer=null;
	private long pos=-1L;
	private int size=0;
	private boolean phantom=false;
	public ObjPoolEntry(byte[] referentSerialized,Object referent, ReferenceQueue q) {
		super(referent, q);
		buffer=referentSerialized;
		size=buffer.length;
	}
	public byte[] getByteBuffer() {
		return buffer;
	}
	public void setFilePos(long pos) {
		this.pos=pos;
		
	}
	public void freeByteBuffer() {
		buffer=null;
	}
	public int getSize() {
		return size;
	}
	public long getFilePos() {
		return pos;
	}
	public void refillBuffer(byte[] buffer) {
		if(size!=buffer.length){
			throw new IllegalArgumentException("different size");
		}
		buffer=buffer;
	}
	public void setPhantom() {
		phantom=true;
		
	}
	public boolean isPhantom() {
		return phantom;
		
	}

}
