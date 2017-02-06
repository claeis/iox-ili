package ch.ehi.iox.objpool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import ch.ehi.iox.objpool.impl.BTreeImpl;
import ch.ehi.iox.objpool.impl.ObjPoolImpl;
import ch.ehi.iox.objpool.impl.Serializer;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxException;

public class ObjectPoolManager {

	private ArrayList<ObjPoolImpl> maps=new ArrayList<ObjPoolImpl>(); 
	private ArrayList<BTreeImpl> maps2=new ArrayList<BTreeImpl>(); 
	public ObjectPoolManager() {
	}

	public <K,V> java.util.Map<K, V> newObjectPool() {
		flushWriteQueues();
		ObjPoolImpl m=null;
		m = new ObjPoolImpl(this);
		maps.add(m);
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
	}

	static public String getCacheTmpFilename() {
		String tmp=System.getProperty("java.io.tmpdir");
		return new File(tmp,getTmpName()).getPath();
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
