package com.ts.common;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Stream;

/**
 * @author hum
 */
public class PeekIterator<T> implements Iterator<T> {

    private final Iterator<T> it;

    private final LinkedList<T> queueCache = new LinkedList<>();
    private final LinkedList<T> stackPutBacks = new LinkedList<>();
    private final static int CACHE_SIZE = 10;
    private T endToken = null;


    public PeekIterator(Stream<T> stream) {
        it = stream.iterator();
    }

    public PeekIterator(Iterator<T> it, T endToke) {
        this.it = it;
        this.endToken = endToke;
    }


    public PeekIterator(Stream<T> stream, T endToken) {
        it = stream.iterator();
        this.endToken = endToken;
    }

    public T peek() {
        if (this.stackPutBacks.size() > 0) {
            return this.stackPutBacks.getFirst();
        }
        if (!it.hasNext()) {
            return endToken;
        }
        T val = next();
        this.putBack();
        return val;
    }


    public void putBack() {
        if (this.queueCache.size() > 0) {
            this.stackPutBacks.push(this.queueCache.pollLast());
        }
    }


    @Override
    public boolean hasNext() {
        return endToken != null || this.stackPutBacks.size() > 0 || it.hasNext();
    }

    @Override
    public T next() {
        T val;
        if (this.stackPutBacks.size() > 0) {
            val = this.stackPutBacks.pop();
        } else {
            if (!this.it.hasNext()) {
                T tmp = endToken;
                endToken = null;
                return tmp;
            }
            val = it.next();
        }
        while (queueCache.size() > CACHE_SIZE - 1) {
            queueCache.poll();
        }
        queueCache.add(val);
        return val;
    }
}
