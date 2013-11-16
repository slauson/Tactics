package com.slauson.tactics.utils;

import java.util.Iterator;

/**
 * Iterator that can iterate over multiple iterables.
 * @author josh
 *
 * @param <E>
 */
public class MultiIterator<E> implements Iterator<E> {
	
	private Iterable<E> iterables[];
	private Iterator<E> iterator;
	private boolean[] iterate;
	private int index;
	
	public MultiIterator(Iterable<E>... iterables) {
		this.iterables = iterables;
		
		// make sure we have at least one iterable
		if (iterables == null || iterables.length == 0) {
			throw new UnsupportedOperationException();
		}
		
		index = 0;
		iterator = iterables[index].iterator();
		iterate = new boolean[iterables.length];
		
		for (int i = 0; i < iterate.length; i++) {
			iterate[i] = true;
		}
	}
	
	/**
	 * Reset the iterator instead of instantiating each time.
	 * @return
	 */
	public MultiIterator<E> reset(boolean... iterate) {
		
		for (int i = 0; i < this.iterate.length; i++) {
			this.iterate[i] = iterate[i];
		}
		
		index = 0;
		iterator = iterables[index].iterator();
		
		return this;
	}

	@Override
	public boolean hasNext() {
		
		while (index >= 0 && index < iterables.length) {
			
			if (iterate[index] && iterator.hasNext()) {
				return true;
			} else {
				index++;
				iterator = index < iterables.length ? iterables[index].iterator() : iterator;
			}
		}
		
		return false;
	}

	@Override
	public E next() {
		
		while (index >= 0 && index < iterables.length) {
			E next = iterate[index] ? iterator.next() : null;
			
			if (next != null) {
				return next;
			} else {
				index++;
				iterator = index < iterables.length ? iterables[index].iterator() : iterator;
			}
		}
		
		return null;
	}

	@Override
	public void remove() {
		// possible, but not needed
		throw new UnsupportedOperationException();
	}	
}