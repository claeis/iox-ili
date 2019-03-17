package ch.interlis.iox_j.statistics;

import ch.interlis.iom.IomObject;

public class ClassStat{
	private String tag=null;
	private long objcount=0;
	public ClassStat(String tag) {
		this.tag = tag;
		this.objcount = 0;
	}
	public long getObjcount() {
		return objcount;
	}
	public void addObject(IomObject iomObj) {
		this.objcount++;
	}
	public String getTag() {
		return tag;
	}
}