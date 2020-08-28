package ch.ehi.iox.objpool;

import java.io.File;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.iox.objpool.impl.BTreeImpl;
import ch.ehi.iox.objpool.impl.JavaSerializer;
import ch.ehi.iox.objpool.impl.ObjPoolImpl;
import ch.ehi.iox.objpool.impl.ObjPoolImpl2;
import ch.ehi.iox.objpool.impl.Serializer;

public class ObjectPoolManager {

	private ArrayList<ObjPoolImpl> maps=new ArrayList<ObjPoolImpl>(); 
	private ArrayList<BTreeImpl> maps2=new ArrayList<BTreeImpl>(); 
	private ArrayList<ObjPoolImpl2> maps3=new ArrayList<ObjPoolImpl2>(); 
	private long initGcTime=0;
	private boolean doCacheing=true;
	private int callCount=0;
	
	public ObjectPoolManager() {
		initGcTime=getTotalGcTime();
	}

	@Deprecated
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
		m = new ObjPoolImpl2(this,serializer);
		if(!doCacheing){
			m.disableCache();
		}
		maps3.add(m);
		return m;
	}
	public <K,V> java.util.Map<K, V> newObjectPool2(Serializer keySerializer,Serializer valueSerializer) {
		flushWriteQueues();
		BTreeImpl<K,V> m=null;
		m = new BTreeImpl<K,V>(this,keySerializer,valueSerializer);
		if(!doCacheing){
			m.disableCache();
		}
		maps2.add(m);
		return m;
	}

	public void flushWriteQueues() {
		for (ObjPoolImpl m: maps) {
			m.flushWriteQueue();
		}
		//System.out.println("maxMemory "+(java.lang.Runtime.getRuntime().maxMemory()-(java.lang.Runtime.getRuntime().totalMemory()-java.lang.Runtime.getRuntime().freeMemory()))/1024L+" KB");
		if(doCacheing){
			if(callCount==0){
				if(getTotalGcTime()-initGcTime>10000){
					// switch off caches
					EhiLogger.traceState("switch off object cache");
					for (BTreeImpl m: maps2) {
						m.disableCache();
					}
					for (ObjPoolImpl2 m: maps3) {
						m.disableCache();
					}
					doCacheing=false;
				}
				callCount++;
			}else if(callCount==1000){
				callCount=0;
			}else{
				callCount++;
			}
		} 
	}
	private long getTotalGcTime()
	{
		List<GarbageCollectorMXBean> memBeans = ManagementFactory.getGarbageCollectorMXBeans();
		long totalGcTime=0;
		for(GarbageCollectorMXBean memBean:memBeans){
			if(memBean.isValid()){
				//System.out.println(memBean.getName()+": count "+memBean.getCollectionCount()+", time "+memBean.getCollectionTime());
				totalGcTime+=memBean.getCollectionTime();
			}
		}
		return totalGcTime;
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
