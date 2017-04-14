package ch.ehi.iox.objpool.impl.btree;

// internal/non-leaf nodes: only use key and next
// external/leaf nodes: only use key and value
class Entry<Key,Value> {
    private Key key;
    private final Value val;
    private NodeId next;     // helper field to iterate over array entries
    public Entry(Key key, Value val, NodeId childNode) {
    	if(val!=null && childNode!=null){
    		throw new IllegalArgumentException();
    	}
        this.key=key;
        this.val  = val;
        this.next=childNode;
    }
	public NodeId getChildNode() {
		return next;
	}
	public Value getVal() {
		return val;
	}
	public Key getKey() {
		return key;
	}
}