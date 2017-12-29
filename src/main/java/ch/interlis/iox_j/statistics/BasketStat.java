package ch.interlis.iox_j.statistics;

import java.util.HashMap;

import ch.interlis.iom.IomObject;


public class BasketStat{
	private IoxStatistics statistics=null;
	private HashMap<String, ClassStat> objStat=new HashMap<String,ClassStat>();
	private String dataset=null;
	private String file=null;
	private String topic=null;
	private String basketId=null;
	public BasketStat(IoxStatistics statistics, String dataset,String file,
			String topic, String iliBasketId) {
		this.statistics=statistics;
		this.dataset = dataset;
		this.file = file;
		this.topic = topic;
		this.basketId = iliBasketId;
	}
	public HashMap<String, ClassStat> getObjStat() {
		return objStat;
	}
	public String getFile() {
		return file;
	}
	public String getTopic() {
		return topic;
	}
	public String getBasketId() {
		return basketId;
	}
	public void addObject(IomObject iomObj) {
		String tag=iomObj.getobjecttag();
		if(objStat.containsKey(tag)){
			ClassStat stat=objStat.get(tag);
			stat.addObject(iomObj);
		}else{
			ClassStat stat=statistics.createClassStat(tag);
			stat.addObject(iomObj);
			objStat.put(tag,stat);
		}
	}
	public String getDataset() {
		return dataset;
	}
	
}