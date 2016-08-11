package ch.interlis.iom_j.itf.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import ch.ehi.iox.objpool.impl.ObjPoolImpl;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxException;

public class ObjectPoolManager {

	protected final long MIN_FREE_MEM=1024L*1024L;
	private String cacheFileBasename = null;
	private ArrayList<ObjPoolImpl> maps=new ArrayList<ObjPoolImpl>(); 
	public ObjectPoolManager() {
		cacheFileBasename = ObjectPoolManager.getCacheTmpFilename();
	}

	public <T> java.util.Map<String, T> newObjectPool() {
		ObjPoolImpl m=null;
		m = new ObjPoolImpl();
		maps.add(m);
		return m;

	}

	public void close() {
		for (ObjPoolImpl m: maps) {
			m.clear();
		}
		maps.clear();
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
