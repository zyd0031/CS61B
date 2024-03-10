
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayRingBuffer<T>  extends AbstractBoundedQueue<T>{
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    private class ArrayRingBufferIterator implements Iterator<T>{
        private int current = first;
        private int count = 0;

        @Override
        public boolean hasNext(){
            return count < fillCount;
        }

        @Override
        public T next(){
            if(!hasNext()){
                throw new NoSuchElementException("No moew items to return.");
            }
            T item = rb[current];
            current = (current + 1) % capacity;
            count++;
            return item;
        }

    }

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        this.capacity = capacity;
        this.first = 0;
        this.last = 0;
        this.fillCount = 0;
        this.rb = (T[]) new Object[capacity];
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    @Override
    public void enqueue(T x) {
        if (isFull()){
            throw new RuntimeException("Ring buffer overflow");
        }
        rb[last] = x;
        last = (last + 1) % capacity;
        fillCount++;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    @Override
    public T dequeue() {
        if (isEmpty()){
            throw new RuntimeException("Ring buffer underflow");
        }
        T item = rb[first];
        rb[first] = null;
        first = (first + 1) % capacity;
        fillCount--;
        return item;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    @Override
    public T peek() {
        if (isEmpty()){
            return null;
        }
        return rb[first];
    }

    @Override
    public Iterator<T> iterator(){
        return new ArrayRingBufferIterator();
    }
}
