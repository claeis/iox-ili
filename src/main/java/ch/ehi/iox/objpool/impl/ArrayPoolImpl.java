package ch.ehi.iox.objpool.impl;

import java.util.Iterator;
import java.util.Map;

import ch.ehi.iox.objpool.ObjectPoolManager;

public class ArrayPoolImpl<V> implements Iterable<V> {
    
    private Map<Object, Object> impl=null;
    private long key=0;
    public ArrayPoolImpl(ObjectPoolManager objectPoolManager,Serializer<V> valueSerializer)
    {
        impl=objectPoolManager.newObjectPool2(new LongSerializer(), valueSerializer);
    }
    public void add(V value) {
        impl.put(key, value);
        key++;
    }
    public Iterator<V> valueIterator(){
        return ((BTreeImpl)impl).valueIterator();
    }
    public void close() {
        ((BTreeImpl)impl).close();
    }
    @Override
    public Iterator<V> iterator() {
        return valueIterator();
    }
}
