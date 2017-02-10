package ch.ehi.iox.objpool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import ch.ehi.iox.objpool.impl.BTreeImpl;
import ch.ehi.iox.objpool.impl.JavaSerializer;
import ch.ehi.iox.objpool.impl.ObjPoolImpl;
import ch.ehi.iox.objpool.impl.ObjPoolImpl2;
import ch.ehi.iox.objpool.impl.Serializer;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxException;

public class ObjectPoolManager {

	private ArrayList<ObjPoolImpl> maps=new ArrayList<ObjPoolImpl>(); 
	private ArrayList<BTreeImpl> maps2=new ArrayList<BTreeImpl>(); 
	private ArrayList<ObjPoolImpl2> maps3=new ArrayList<ObjPoolImpl2>(); 
	public ObjectPoolManager() {
	}

	public <K,V> java.util.Map<K, V> newObjectPool() {
		flushWriteQueues();
		ObjPoolImpl m=null;
		m = new ObjPoolImpl(this,new JavaSerializer());
		maps.add(m);
		return m;
	}
	public <K,V> java.util.Map<K, V> newObjectPool(Serializer<V> serializer) {
		flushWriteQueues();
		ObjPoolImpl m=null;
		m = new ObjPoolImpl(this,serializer);
		maps.add(m);
		return m;
	}
	public <K,V> java.util.Map<K, V> newObjectPoolImpl2(Serializer<V> serializer) {
		flushWriteQueues();
		ObjPoolImpl2 m=null;
		m = new ObjPoolImpl2(serializer);
		maps3.add(m);
		return m;
	}
	public <K,V> java.util.Map<K, V> newObjectPool2(Serializer keySerializer,Serializer valueSerializer) {
		flushWriteQueues();
		BTreeImpl<K,V> m=null;
		m = new BTreeImpl<K,V>(keySerializer,valueSerializer);
		maps2.add(m);
		return m;
	}

	public void flushWriteQueues() {
		for (ObjPoolImpl m: maps) {
			m.flushWriteQueue();
		}
	}

	public void close() {
		for (ObjPoolImpl m: maps) {
			m.clear();
		}
		maps.clear();
		for (BTreeImpl m: maps2) {
			m.close();
		}
		maps2.clear();
		for (ObjPoolImpl2 m: maps3) {
			m.close();
		}
		maps3.clear();
	}

	static public File getCacheTmpFilename() {
		String tmp=System.getProperty("java.io.tmpdir");
		return new File(tmp,getTmpName());
	}

	static public String getTmpName() {
		long n=new java.util.Random().nextLong();
		if (n == Long.MIN_VALUE) {
			n = 0;
		} else {
			n = Math.abs(n);
		}
		return "ioxtmp"+n;
		
	}
}
