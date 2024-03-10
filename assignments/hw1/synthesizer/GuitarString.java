//Make sure this class is public
public class GuitarString {
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private BoundedQueue<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        int capacity = (int)Math.round(SR / frequency);
        buffer = new ArrayRingBuffer<>(capacity);
        for (int i = 0; i < capacity; i++) {
            buffer.enqueue(0.0);
        }
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        while(!buffer.isEmpty()){
            buffer.dequeue();
        }
        for (int i = 0; i < buffer.capacity(); i++) {
            double r = Math.random() - 0.5;
            buffer.enqueue(r);
        }

    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm. 
     */
    public void tic() {
        double first = buffer.dequeue();
        double second = buffer.peek();
        double insert = DECAY * 0.5 * (first + second);
        buffer.enqueue(insert);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.peek();
    }

}
