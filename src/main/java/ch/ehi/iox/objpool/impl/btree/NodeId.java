package ch.ehi.iox.objpool.impl.btree;

public class NodeId {
	long pageId;
	public NodeId(long pageId){
		this.pageId=pageId;
	}
	public long getPageId()
	{
		return pageId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (pageId ^ (pageId >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeId other = (NodeId) obj;
		if (pageId != other.pageId)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "NodeId [pageId=" + pageId + "]";
	}
}
