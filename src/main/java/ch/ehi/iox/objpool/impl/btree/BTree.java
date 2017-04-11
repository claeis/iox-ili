package ch.ehi.iox.objpool.impl.btree;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;

import ch.ehi.iox.objpool.impl.Serializer;


public class BTree<Key, Value>  {
    // max children per B-tree node = M-1
    // (must be even and greater than 2)
    static final int M = 16;

    private NodeId root;       // root of the B-tree
    private int height;      // height of the B-tree
    private int n;           // number of key-value pairs in the B-tree
    
    private Serializer<Key> keySerializer;
    private Serializer<Value> valueSerializer;
    java.util.Comparator<Key> keyComparator;
    private RandomAccessFile file;
    private java.io.File fileName=null;
    public final static int BLOCK_SIZE=8192;
    private int pageCount=0;
    private static int MAX_CACHE=32;
    private final LinkedHashMap<NodeId,Node> cache = new LinkedHashMap<NodeId,Node>(MAX_CACHE,0.75f,true){
    	@Override
    	protected boolean removeEldestEntry(Map.Entry<NodeId, Node> eldest) {
    		if(true){
        		Node node=eldest.getValue();
        		if(size()<MAX_CACHE){
        			return false;
        		}
        		if(node.isDirty()){
        			// save it
        			writeNode(node);
        		}
                return true; // remove eldest
    		}
    		return false;
        }

    };
	protected void touchNode(NodeId nodeId) {
		cache.get(nodeId);
	}
	private void writeNode(Node node) {
		try {
			ArrayList<Long> pages=node.getOverflowPages();
			byte[] bytes=node.getBytes();
			final int blockSize=BLOCK_SIZE-8;
			int pagec=(bytes.length-1)/blockSize+1; // nr of required pages to write this node
			if(pagec>pages.size()+1){
				// allocate (additional) overflow pages
				for(int i=pages.size()+1;i<pagec;i++){
					pages.add(allocPage().getPageId());
				}
			}
			long blockId=node.getNodeId().getPageId();
			for(int i=0;i<pagec;i++){
				long nextPage=-1;
				if(i<pages.size()){
					nextPage=pages.get(i);
				}
				if(i>0){
					blockId=pages.get(i-1);
				}
				file.seek(blockId*BLOCK_SIZE);
				file.writeLong(nextPage);
				if(bytes.length<(i+1)*blockSize){
					file.write(bytes, i*blockSize,bytes.length-i*blockSize);
				}else{
					file.write(bytes, i*blockSize, blockSize);
				}
			}
			node.setOverflowPages(pages);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}    	
    protected Node<Key, Value> getNode(NodeId nodeId)
    {
    	Node<Key, Value> ret=cache.get(nodeId);
    	if(ret!=null){
    		return ret;
    	}
    	try{
			ArrayList<Long> pages=new ArrayList<Long>();
			long blockId=nodeId.getPageId();
			file.seek(blockId*BLOCK_SIZE);
			long nextBlock=file.readLong();
			byte[] buffer=new byte[BLOCK_SIZE-8];
			file.read(buffer);
			ByteArrayOutputStream bytes=new ByteArrayOutputStream();
			bytes.write(buffer);
			while(nextBlock!=-1){
				pages.add(nextBlock);
				blockId=nextBlock;
				file.seek(blockId*BLOCK_SIZE);
				nextBlock=file.readLong();
				file.read(buffer);
				bytes.write(buffer);
			}
    		Node node=Node.read(this,nodeId,bytes.toByteArray());
    		node.setOverflowPages(pages);
        	return node;
		} catch (IOException e) {
			throw new IllegalStateException(e);
    	}
    }

    /**
     * Initializes an empty B-tree.
     */
    public BTree(java.io.File path,java.util.Comparator<Key> keyComparator) 
    		throws java.io.IOException 
    {
    	this.keyComparator=keyComparator;
    	this.keySerializer=keySerializer;
    	this.valueSerializer=valueSerializer;
    	this.fileName=path;
		this.file = new RandomAccessFile( path, "rw" );
		height=0;
		n=0;
        root = new Node(this,allocPage(),0).getNodeId();
    }
 
    private NodeId allocPage() {
		NodeId ret=new NodeId(pageCount);
		pageCount++;
		return ret;
	}

	/**
     * Returns true if this symbol table is empty.
     * @return {@code true} if this symbol table is empty; {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return n;
    }

    /**
     * Returns the height of this B-tree (for debugging).
     *
     * @return the height of this B-tree
     */
    public int height() {
        return height;
    }


    /**
     * Returns the value associated with the given key.
     *
     * @param  key the key
     * @return the value associated with the given key if the key is in the symbol table
     *         and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Value get(Key key) {
        if (key == null){
        	throw new IllegalArgumentException("argument to get() is null");
        }
        return search(getNode(root), key, height);
    }

    private Value search(Node<Key,Value> currentNode, Key key, int ht) {

        // external/leaf node
        if (ht == 0) {
            for (int j = 0; j < currentNode.getEntryCount(); j++) {
                if (eq(key, currentNode.getEntry(j).getKey())){
                	// key found
                	return (Value) currentNode.getEntry(j).getVal();
                }
            }
        } else {
        	// internal/non-leaf node
            for (int j = 0; j < currentNode.getEntryCount(); j++) {
                if (j+1 == currentNode.getEntryCount() || less(key, currentNode.getEntry(j+1).getKey())){
                    return search(getNode(currentNode.getEntry(j).getChildNode()), key, ht-1);
                }
            }
        }
        return null; // not found
    }


    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is {@code null}, this effectively deletes the key from the symbol table.
     *
     * @param  key the key
     * @param  val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("argument key to put() is null");
        Node<Key,Value> u = insert(getNode(root), key, val, height); 
        n++;
        if (u == null){
        	return;
        }

        // need to split root
        Node<Key,Value> t = new Node<Key,Value>(this,allocPage(),2);
        t.setEntry(0, new Entry(getNode(root).getEntry(0).getKey(), null, root));
        t.setEntry(1, new Entry(u.getEntry(0).getKey(), null, u.getNodeId()));
        root = t.getNodeId();
        height++;
    }

    private Node insert(Node<Key,Value> h, Key key, Value val, int ht) {
        int j;
        Entry t = new Entry(key, val, null);

        
        if (ht == 0) {
        	// external/leaf node
            for (j = 0; j < h.getEntryCount(); j++) {
                if (eq(key, h.getEntry(j).getKey())){
                	// found, replace value
                	h.setEntry(j, t);
                	return null;
                }
                if (less(key, h.getEntry(j).getKey())){
                	break;
                }
            }
        } else {         
        	// internal/non-leaf node
            for (j = 0; j < h.getEntryCount(); j++) {
                if ((j+1 == h.getEntryCount()) || less(key, h.getEntry(j+1).getKey())) {
                	NodeId childNode=h.getEntry(j).getChildNode();
                    Node u = insert(getNode(childNode), key, val, ht-1);
                	j++;
                    if (u == null){
                    	return null;
                    }
                    t = new Entry(u.getEntry(0).getKey(), null, u.getNodeId());
                    break;
                }
            }
        }

        h=getNode(h.getNodeId()); // refresh node
        // insert t into h at position j
        {
            for (int i = h.getEntryCount(); i > j; i--){
                h.setEntry(i,h.getEntry(i-1));
        	}
            h.setEntry(j, t);
            h.setEntryCount(h.getEntryCount() + 1);
        }
        
        // node not yet full?
        if (h.getEntryCount() < M){
        	return null;
        }else{
        	// node full
        	return split(h);
        }
    }

    // split node in half
    private Node split(Node fullNode) {
        Node newNode = new Node(this,allocPage(),M/2);
        fullNode.setEntryCount(M/2);
        for (int j = 0; j < M/2; j++){
            newNode.setEntry(j,fullNode.getEntry(M/2+j));
        }
        return newNode;    
    }

    /**
     * Returns a string representation of this B-tree (for debugging).
     *
     * @return a string representation of this B-tree.
     */
    public String toString() {
        return toString(getNode(root), height, "") + "\n";
    }

    private String toString(Node h, int ht, String indent) {
        StringBuilder s = new StringBuilder();

        if (ht == 0) {
            for (int j = 0; j < h.getEntryCount(); j++) {
                s.append(indent + h.getEntry(j).getKey() + " " + h.getEntry(j).getVal() + "\n");
            }
        }
        else {
            for (int j = 0; j < h.getEntryCount(); j++) {
                if (j > 0){
                	s.append(indent + h.getNodeId()+": (" + h.getEntry(j).getKey() + ")\n");
                }
                s.append(toString(getNode(h.getEntry(j).getChildNode()), ht-1, indent + "     "));
            }
        }
        return s.toString();
    }


    // comparison functions - make Comparable instead of Key to avoid casts
    private boolean less(Key k1, Key k2) {
        return keyComparator.compare(k1, k2) < 0;
    }

    private boolean eq(Key k1, Key k2) {
        return keyComparator.compare(k1, k2) == 0;
    }
	public void serialzeKey(ByteArrayOutputStream bytes, Key key) throws IOException {
		ObjectOutputStream out=new ObjectOutputStream(bytes);
		out.writeObject(key);
		out.flush();
	}
	public void serialzeValue(ByteArrayOutputStream bytes, Value val) throws IOException {
		ObjectOutputStream out=new ObjectOutputStream(bytes);
		out.writeObject(val);
		out.flush();
	}
	public Key deserialzeKey(ByteArrayInputStream bytes) throws IOException {
		ObjectInputStream in=new ObjectInputStream(bytes);
		try {
			return (Key) in.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException(e);
		}
	}
	public Object deserialzeValue(ByteArrayInputStream bytes) throws IOException {
		ObjectInputStream in=new ObjectInputStream(bytes);
		try {
			return in.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException(e);
		}
	}
	public void cachePut(NodeId nodeId, Node node) {
		cache.put(nodeId, node);
	}
	public void close() throws IOException {
		if(file!=null){
			file.close();
			file=null;
		}
		if(fileName!=null){
			fileName.delete();
			fileName=null;
		}
		
	}
	public NodeId getRoot() {
		return root;
	}

}
