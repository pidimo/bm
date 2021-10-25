package com.piramide.elwis.utils;

/**
 * Jatun Team
 * This class is a container for a previos and next elements
 *
 * @author Alvaro
 * @version $Id: PagingPair.java,v 1.0 2008/06/22 21:46:12
 */
public class PagingPair<T> {
    private T previous;
    private T next;

    private int index;

    public PagingPair(T previous, T next) {
        this.previous = previous;
        this.next = next;
    }

    public PagingPair() {
    }

    public T getPrevious() {
        return previous;
    }

    public void setPrevious(T previous) {
        this.previous = previous;
    }

    public T getNext() {
        return next;
    }

    public void setNext(T next) {
        this.next = next;
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
