package ch.interlis.iox_j.statistics;

import java.util.ArrayList;
import java.util.HashMap;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;

public class IoxStatistics {
	private String dataset=null;
	private String filename=null;
	private BasketStat basket=null;
	private ArrayList<BasketStat> baskets=new ArrayList<BasketStat>();
	private boolean hideBid=false;
	public IoxStatistics(TransferDescription td, Settings settings) {
	}
	public void add(ch.interlis.iox.IoxEvent event) {
		if(event instanceof ch.interlis.iox.StartBasketEvent) {
			basket=createBasketStat(event);
			baskets.add(basket);
		}else if(event instanceof ch.interlis.iox.EndBasketEvent) {
			basket=null;
		}else if(event instanceof ch.interlis.iox.ObjectEvent) {
			if(basket!=null) {
				IomObject iomObj = ((ch.interlis.iox.ObjectEvent) event).getIomObject();
				basket.addObject(iomObj);
			}
		}
	}
	protected BasketStat createBasketStat(ch.interlis.iox.IoxEvent event) {
		return new BasketStat(this,dataset,filename,((ch.interlis.iox.StartBasketEvent) event).getType(),((ch.interlis.iox.StartBasketEvent) event).getBid());
	}
	public void write2logger() {
		ArrayList<BasketStat> statv=baskets;
		java.util.Collections.sort(statv,new java.util.Comparator<BasketStat>(){
			@Override
			public int compare(BasketStat b0, BasketStat b1) {
				int ret=compareStringOrNull(b0.getDataset(),b1.getDataset());
				if(ret==0) {
					ret=compareStringOrNull(b0.getFile(),b1.getFile());
				}
				if(ret==0){
					ret=b0.getTopic().compareTo(b1.getTopic());
					if(ret==0){
						ret=b0.getBasketId().compareTo(b1.getBasketId());
					}
				}
				return ret;
			}
			
		});
		for(BasketStat basketStat:statv){
			String file=basketStat.getFile();
			String dataset=basketStat.getDataset();
			StringBuffer prefix=new StringBuffer();
			String sep="";
			if(dataset!=null) {
				prefix.append(sep);
				prefix.append(dataset);
				sep=": ";
			}
			if(file!=null) {
				prefix.append(sep);
				prefix.append(file);
				sep=": ";
			}
			if(hideBid){
				EhiLogger.logState(prefix+sep+basketStat.getTopic());
			}else{
				EhiLogger.logState(prefix+sep+basketStat.getTopic()+" BID="+basketStat.getBasketId());
			}
			HashMap<String, ClassStat> objStat=basketStat.getObjStat();
			ArrayList<String> classv=new ArrayList<String>(objStat.keySet());
			java.util.Collections.sort(classv,new java.util.Comparator<String>(){
				@Override
				public int compare(String b0, String b1) {
					int ret=b0.compareTo(b1);
					return ret;
				}
			});
			String nbsp=Character.toString('\u00A0');
			for(String className : classv){
				ClassStat classStat=objStat.get(className);
				String objCount=Long.toString(classStat.getObjcount());
				if(objCount.length()<6){
					objCount=ch.ehi.basics.tools.StringUtility.STRING(6-objCount.length(), ' ')+objCount;
				}
				EhiLogger.logState(nbsp+objCount+" objects in CLASS "+className);
			}
		}
		
	}
	protected static int compareStringOrNull(String value1, String value2) {
		if(value1==null) {
			if(value2==null) {
				return 0;
			}
			return -1;
		}else if(value2==null) {
			return 1;
		}
		return value1.compareTo(value2);
	}
	protected ClassStat createClassStat(String tag) {
		return new ClassStat(tag);
	}
	public boolean isHideBid() {
		return hideBid;
	}
	public void setHideBid(boolean hideBid) {
		this.hideBid = hideBid;
	}
	public String getDataset() {
		return dataset;
	}
	public void setDataset(String dataset) {
		this.dataset = dataset;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
}
