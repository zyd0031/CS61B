/**
 * Portions of the following code snip are sorece form 《Java编程的逻辑》Section 9.3 ArrayDeque
 */

import java.util.NoSuchElementException;

/**
 * For arrays of length 16 or more, 
 * your usage factor should always be at least 25%. 
 * For smaller arrays, your usage factor can be arbitrarily low.
 */


public class ArrayDeque<T> {

    private T[] elements = (T[])new Object[8];
    private int head;
    /** points to the next empty position */
    private int tail;
    final double USAGEFACTORTHRESHOLD = 0.25;
    final int CAPACITYTHRESHOLD = 16;


    /**
     * Creates an empty array deque.
     */
    public ArrayDeque(){
    }

    /** Adds an item of type T to the front of the deque. */
    public void addFirst(T item){
        if (item == null){
            throw new NullPointerException();
        }
        /**
         * elements.length = 2^k, ensuring that (elements.length - 1) is a binary number with all bits set to 1.
         * if head is 0, decrementing by 1 would result in a negative number. 
         * & is used to confine the result to the range 0 to elements.length - 1.
         */
        elements[head = (head - 1) & (elements.length - 1)] = item; 
        if (head == tail){
            doubleCapacity();
        }
    }

    /** Adds an item of type T to the back of the deque. */
    public void addLast(T item){
        if (item == null){
            throw new NullPointerException();
        }
        elements[tail] = item;
        if( (tail = (tail + 1) & (elements.length - 1)) == head){
            doubleCapacity();
        }
    }

    /** Returns true if deque is empty, false otherwise. */
    public boolean isEmpty(){
        return head == tail;
    }

    /** Returns the number of items in the deque. */
    public int size(){
        return (tail - head) & (elements.length - 1);
    }

    /** Prints the items in the deque from first to last, separated by a space. */
    public void printDeque(){

    }

    /** Removes and returns the item at the front of the deque. If no such item exists, returns null. */
    public T removeFirst(){
        T x = pollFirst();
        if (x == null){
            throw new NoSuchElementException();
        }
        return x;
    }

    private T pollFirst(){
        int h = head;
        T result = elements[h]; // Element is null if deque empty;
        if (result == null){
            return null;
        }
        elements[h] = null;
        /** check if usage factor < 0.25. */
        head = (h + 1) & (elements.length - 1);
        int size = size();
        int capacity = elements.length;
        adjustCapacityIfNeeded(size, capacity);
        return result;
    }

    private void adjustCapacityIfNeeded(int size, int capacity){
        if(size > CAPACITYTHRESHOLD){
            if (!checkUsageFactor(size, capacity)){
                halveCapacity(size);  
            }
        }
    }

    /** Removes and returns the item at the back of the deque. If no such item exists, returns null. */
    public T removeLast(){
        T x = pollLast();
        if (x == null){
            throw new NoSuchElementException();
        }
        return x;
    }

    private T pollLast(){
        int t = tail;
        int lastindex = (t - 1) & (elements.length - 1);
        T result = elements[lastindex];
        if (result == null){
            return null;
        }
        elements[lastindex] = null;
        tail = lastindex;
        int size = size();
        int capacity = elements.length;
        adjustCapacityIfNeeded(size, capacity);
        return result;
    }

    /** Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth. If no such item exists, returns null. Must not alter the deque! */
    public T get(int index){
        if (index > 0 && index < elements.length - 1){
            return elements[index];
        }else{
            return null;
        }
    }

    private void doubleCapacity(){
        assert head == tail;
        int p = head;
        int n = elements.length;
        int r = n - p;
        int newCapacity = n << 1;
        if (newCapacity < 0){
            throw new IllegalStateException("Sorry, deque too big!");  // << a positive number, if it exceed the range of the data type, the result wiuld be negative.
        }
        Object[] a = new Object[newCapacity];
        System.arraycopy(elements, p, a, 0, r);
        System.arraycopy(elements, 0, a, r, p);
        elements = (T[])a;
        head = 0;
        tail = n;
    }

    private void halveCapacity(int size){
        int h = head;
        int t = tail;
        int n = elements.length;
        int r = n - h; // size of array[head:]
        int newCapacity = elements.length >> 1;
        Object[] a = new Object[newCapacity];
        if( (t > h) || (t == 0)){
            System.arraycopy(elements, h, a, 0, size);
        }else if (h > t){
            System.arraycopy(elements, h, a, 0, r);
            System.arraycopy(elements, 0, a, r, t);
        }
        elements = (T[])a;
        head = 0;
        tail = size - 1;
    }
    /**
     * return true if usagecapacity > USAGEFACTORTHRESHOLD else false.
     */
    private boolean checkUsageFactor(int size, int capacity){
        double usageFactor = size / capacity;
        return usageFactor > USAGEFACTORTHRESHOLD;
    }
}
