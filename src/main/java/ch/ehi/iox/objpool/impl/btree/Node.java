package ch.ehi.iox.objpool.impl.btree;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import ch.ehi.iox.objpool.impl.LongSerializer;


// helper B-tree node data type
final class Node<Key,Value> {
    private int m;                             // number of entries
    private Entry<Key,Value>[] children = new Entry[BTree.M];   // the array of entries
    private NodeId nodeId;
    private boolean dirty=true;
    private BTree<Key,Value> tree=null;
    private ArrayList<Long> overflowPages=new ArrayList<Long>();
    // create a node with k children
    Node(BTree<Key,Value> tree,NodeId nodeId,int k) {
    	this.nodeId=nodeId;
        m=k;
        this.tree=tree;
        this.dirty=true;
        tree.cachePut(nodeId,this);
    }

	public NodeId getNodeId() {
		return nodeId;
	}

	public int getEntryCount() {
		tree.touchNode(nodeId);
		return m;
	}

	public void setEntryCount(int m) {
		tree.touchNode(nodeId);
		this.m = m;
		dirty=true;
	}

	public Entry<Key,Value> getEntry(int i) {
		tree.touchNode(nodeId);
		return children[i];
	}

	public void setEntry(int i,Entry<Key,Value> child) {
		tree.touchNode(nodeId);
		children[i] = child;
		dirty=true;
	}

	public boolean isDirty() {
		return dirty;
	}

	public ArrayList<Long> getOverflowPages() {
		return overflowPages;
	}

	public void setOverflowPages(ArrayList<Long> overflowPages) {
		this.overflowPages=overflowPages;		
	}

	public static <Key,Value> Node<Key,Value> read(BTree<Key,Value> tree,NodeId nodeId, byte[] in) throws IOException {
		ByteArrayInputStream bytes=new ByteArrayInputStream(in);
		if(doMagic){
			byte[] b=new byte[4];
			bytes.read(b);
			if(MAGIC!=LongSerializer.bytesToInteger(b,0)){
				throw new IllegalStateException();
			}
		}
		// read m
		int m;
		{
			byte[] b=new byte[4];
			bytes.read(b);
			m=LongSerializer.bytesToInteger(b,0);
		}
		Node<Key,Value> ret=new Node<Key,Value>(tree,nodeId,m);
		// write entries
		for(int i=0;i<m;i++){
			long next;
			{
				byte[] b=new byte[8];
				bytes.read(b);
				next=LongSerializer.bytesToLong( b);
			}
			Key key=tree.deserialzeKey(bytes);
			if(next==-1){
				Object val=tree.deserialzeValue(bytes);
				ret.children[i]=new Entry(key,val,null);
			}else{
				ret.children[i]=new Entry(key,null,new NodeId(next));
			}
		}
		if(doMagic){
			byte[] b=new byte[4];
			bytes.read(b);
			if(MAGIC!=LongSerializer.bytesToInteger(b,0)){
				throw new IllegalStateException();
			}
		}
		
		return ret;
	}

	private final static boolean doMagic=true;
	private final static int MAGIC=165;
	public byte[] getBytes() throws IOException {
		ByteArrayOutputStream bytes=new ByteArrayOutputStream();
		if(doMagic){
			byte[] b=new byte[4];
			LongSerializer.integerToBytes(MAGIC, b,0);
			bytes.write(b);
		}
		// write m
		{
			byte[] b=new byte[4];
			LongSerializer.integerToBytes(m, b,0);
			bytes.write(b);
		}
		// write entries
		for(int i=0;i<m;i++){
			Entry<Key,Value> e=children[i];
			{
				byte[] b=new byte[8];
				if(e.getChildNode()==null){
					LongSerializer.longToBytes(-1, b);
				}else{
					LongSerializer.longToBytes(e.getChildNode().getPageId(), b);
				}
				bytes.write(b);
			}
			tree.serialzeKey(bytes,e.getKey());
			if(e.getChildNode()==null){
				tree.serialzeValue(bytes,e.getVal());
			}
		}
		if(doMagic){
			byte[] b=new byte[4];
			LongSerializer.integerToBytes(MAGIC, b,0);
			bytes.write(b);
		}
		bytes.flush();
		return bytes.toByteArray();
	}
}