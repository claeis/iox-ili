package ch.ehi.iox.objpool.impl;

import java.util.Collection;
import java.util.List;
import java.util.Iterator;
import java.util.ListIterator;

import ch.ehi.iox.objpool.ObjectPoolManager;

public class FileBasedCollection<E> implements List<E> {
	private int size=0;
	private ObjectPoolManager recman=null;
	private ObjPoolImpl2 pool=null;
	public FileBasedCollection(ObjectPoolManager objectPoolManager,Serializer<E> serializer) {
		recman=objectPoolManager;
		pool=(ObjPoolImpl2) recman.newObjectPoolImpl2(serializer);
	}

	@Override
	public boolean add(E value) {
		pool.put(size, value);
		size++;
		return true;
	}

	@Override
	public void clear() {
		pool.clear();
	}

	@Override
	public boolean contains(Object arg0) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public boolean isEmpty() {
		return size==0;
	}

	@Override
	public Iterator<E> iterator() {
		Iterator<E> ret=new Iterator<E>() {
			int i=0;
			@Override
			public boolean hasNext() {
				return i<size;
			}

			@Override
			public E next() {
				return (E)pool.get(i++);
			}

			@Override
			public void remove() {
				throw new java.lang.UnsupportedOperationException();
			}
		};
		return ret;
	}

	@Override
	public boolean remove(Object arg0) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Object[] toArray() {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void add(int index, E element) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public E get(int index) {
		if(index<0 || index>=size){
			throw new IndexOutOfBoundsException();
		}
		return (E) pool.get(index);
	}

	@Override
	public int indexOf(Object o) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public int lastIndexOf(Object o) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public ListIterator<E> listIterator() {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public E remove(int index) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public E set(int index, E element) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		throw new java.lang.UnsupportedOperationException();
	}

}
