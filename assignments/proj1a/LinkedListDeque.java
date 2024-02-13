/**
 * Portions of the following code snip are sorece form java.util.LinkedList Source code
 */


import java.util.NoSuchElementException;

public class LinkedListDeque<T> {
  
    private static class Node<T>{
        T item;
        Node<T> next;
        Node<T> prev;
        Node(Node<T> prev, T element, Node<T> next){
            this.prev = prev;
            this.item = element;
            this.next = next;
        }
    }

    private int size = 0;
    private Node<T> first;
    private Node<T> last;

    /** Creates an empty linked list deque */
    public LinkedListDeque(){
    }

    /** Create an linnked list deque */
    public LinkedListDeque(int size, Node<T> first, Node<T> last){
        this.size = size;
        this.first = first;
        this.last = last;
    }

    /** Adds an item of type T to the front of the deque. */
    public void addFirst(T item){
        linkFirst(item);
    }

    /** Adds an item of type T to the back of the deque. */
    public void addLast(T item){
        linkLast(item);
    }

    /** Returns true if deque is empty, false otherwise. */
    public boolean isEmpty(){
        return size == 0;
    }

    /** Returns the number of items in the deque. */
    public int size(){
        return size;
    }

    /** Prints the items in the deque from first to last, separated by a space. */
    public void printDeque(){
        String dequeString = "";
        for(Node<T> curr = first; curr!= null; curr = curr.next){
            dequeString += curr.item;
        }
        System.out.println(dequeString);
    }

    /** Removes and returns the item at the front of the deque. If no such item exists, returns null. */
    public T removeFirst(){
        final Node<T> f = first;
        if (f == null){
            throw new NoSuchElementException();
        }
        return unlinkFirst(f);
    }

    /** Removes and returns the item at the back of the deque. If no such item exists, returns null. */
    public T removeLast(){
        final Node<T> l = last;
        if (l == null){
            throw new NoSuchElementException();
        }
        return unlinkLast(l);
    }

    /**
     * Returns the (non-null) Node at the specified element iindex. 
     */
    Node<T> node(int index){
        // assert isElementIndex(index);
        if (index < (size >> 1)){
            Node<T> x = first;
            for (int i = 0; i < index; i++) {
                x = x.next;
            }
            return x;
        }else{
            Node<T> x = last;
            for (int i = size - 1; i > index; i--) {
                x = x.prev;
            }
            return x;
        }
        
    }

    /** Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth. If no such item exists, returns null. Must not alter the deque! */
    public T get(int index){
        checkElementIndex(index);
        return node(index).item;
    }

    /** Same as get, but uses recursion */
    public T getRecursive(int index){
        checkElementIndex(index);
        if(index == 0){
            return first.item;
        }
        LinkedListDeque<T> withoutFirst = new LinkedListDeque<>(size - 1, this.first.next, last);
        return withoutFirst.getRecursive(index - 1);
    }

    /**
     * Links item as first element.
     */
    private void linkFirst(T item){
        final Node<T> f = first;
        final Node<T> newNode = new Node<>(null, item, f);
        first = newNode;
        if(f == null){
            last = newNode;
        }else{
            f.prev = newNode;
        }
        size++;
    }

    /**
     * Links item as last element.
     */
    private void linkLast(T item){
        final Node<T> l = last;
        final Node<T> newNode = new Node<>(l, item, null);
        last = newNode;
        if (l == null){
            first = newNode;
        }else{
            l.next = newNode;
        }
        size++;
    }

    /**
     * Unlinks non-null first node f.
     */
    private T unlinkFirst(Node<T> f){
        // assert f == first && f != null;
        final T element = f.item;
        final Node<T> next = f.next;
        f.item = null;
        f.next = null; // help GC
        if(next == null){
            last = null;
        }else{
            next.prev = null;
        }
        size--;
        return element;
    }

    /**
     * Unlinks non-null last node l.
     */
    private T unlinkLast(Node<T> l){
        final T element = l.item;
        final Node<T> prev = l.prev;
        l.item = null;
        l.prev = null; //help GC
        last = prev;
        if(prev == null){
            first = null;
        }else{
            prev.next = null;
        }
        size--;
        return element;
    }

    /**
     * Tells if the argument is the index of an existing element.
     */
    private boolean isElementIndex(int index){
        return index >= 0 && index < size;
    }

    /**
     * Constructs an IndexOutOfBoundsException detail message.
     */
    private String outOfBoundsMsg(int index){
        return "Index: " + index + ", Size: " + size;
    }

    private void checkElementIndex(int index){
        if(!isElementIndex(index)){
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }

    
}
