package ch.ehi.iox.objpool.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import ch.ehi.iox.objpool.ObjectPoolManager;
import ch.ehi.iox.objpool.impl.btree.BTree;
import ch.ehi.iox.objpool.impl.btree.BTreeCursor;

public class ObjPoolImpl2<K,V> implements Map<K, V> {
	
	private HashMap<K, Long> tree= null;
	private RandomAccessFile outFile=null;
	private Serializer valueSerializer=null;
	public ObjPoolImpl2( Serializer valueSerializer1)
	{
		try{
			valueSerializer=valueSerializer1;
			tree= new HashMap<K, Long>();
			String outFilename=ObjectPoolManager.getCacheTmpFilename();
			outFile=new RandomAccessFile(outFilename, "rw");
		}catch(IOException e){
			throw new IllegalStateException(e);
		}
	}
	public void close()
	{
		if(tree!=null){
			tree=null;
		}
		if(outFile!=null){
			try {
				outFile.close();
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
			outFile=null;
		}
	}

	@Override
	public void clear() {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public boolean containsKey(Object key) {
		return tree.get((K)key)!=null;
	}

	@Override
	public boolean containsValue(Object arg0) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public V get(Object key) {
		try {
			Long pos= tree.get((K)key);
			return readValue(pos);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	private V readValue(Long pos) throws IOException {
		if(pos!=null){
			outFile.seek(pos);
			int size=outFile.readInt();
			byte[] bytes=new byte[size];
			outFile.read(bytes);
			V value;
		    try {
				value=(V)valueSerializer.getObject(bytes);
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException(e);
			}
			return value;
		}
		return null;
	}

	@Override
	public boolean isEmpty() {
	 return size()==0;
	}

	@Override
	public Set<K> keySet() {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public V put(K key, V value) {
		try {
			
			long pos = outFile.length();
			outFile.seek(pos);
			byte[] bytes=valueSerializer.getBytes(value);
			outFile.writeInt(bytes.length);
			outFile.write(bytes);
			Long retPos=tree.get((K)key);
			tree.put(key, pos);
			return null;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> arg0) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public V remove(Object key) {
		Long retPos=tree.get((K)key);
		tree.put((K)key,null);
		return null;
	}

	@Override
	public int size() {
		return tree.size();
	}

	@Override
	public Collection<V> values() {
		throw new java.lang.UnsupportedOperationException();
	}
	public java.util.Iterator<K> keyIterator(){
		return tree.keySet().iterator();
	}
	public java.util.Iterator<V> valueIterator(){
		java.util.Iterator<V> ret= new Iterator<V>() {
			Iterator<K> cursor=tree.keySet().iterator();
			@Override
			public boolean hasNext() {
				return cursor.hasNext();
			}

			@Override
			public V next() {
				Long pos= tree.get(cursor.next());
				try {
					return readValue(pos);
				} catch (IOException e) {
					throw new IllegalStateException(e);
				}
			}

			@Override
			public void remove() {
				throw new java.lang.UnsupportedOperationException();
			}
			
		};
		return ret;
	}

}
