import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] s;
    private int N;

    /**
     * construct an empty randomized queue
     */
    public RandomizedQueue() {
        s = (Item[]) new Object[1];
    }

    /**
     * is the randomized queue empty?
     * @return
     */
    public boolean isEmpty() {
        return N == 0;
    }

    /**
     * return the number of items on the randomized queue
     * @return
     */
    public int size() {
        return N;
    }

    private void resize(int capacity)
    {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++)
            copy[i] = s[i];
        s = copy;
    }

    /**
     * add the item
     * @param item
     */
    public void enqueue(Item item) {
        if (item == null)
            throw new java.lang.IllegalArgumentException();

        if (N + 1 == s.length) resize(2 * s.length);
        s[N++] = item;
    }

    /**
     * remove and return a random item
     * @return
     */
    public Item dequeue() {
        if (isEmpty())
            throw new java.util.NoSuchElementException();

        int ri = StdRandom.uniform(N);
        Item item = s[ri];
        s[ri] = s[--N];
        s[N] = null;
        if (N > 0 && (N + 1) == s.length/4) resize(s.length/2);
        return item;
    }

    /**
     * return a random item (but do not remove it)
     * @return
     */
    public Item sample() {
        if (isEmpty())
            throw new java.util.NoSuchElementException();

        int ri = StdRandom.uniform(N);
        return s[ri];
    }

    /**
     * return an independent iterator over items in random order
     * @return
     */
    public Iterator<Item> iterator()
    { return new ArrayIterator(); }

    private class ArrayIterator implements Iterator<Item>
    {
        private int i = N;
        private int[] order;

        public ArrayIterator() {
            order = new int[N];
            for (int j = 0; j < N; j++) {
                order[j] = j;
            }
            StdRandom.shuffle(order);
        }

        public boolean hasNext() { return i > 0; }
        public void remove() {
            /* not supported */
            throw new java.lang.UnsupportedOperationException();
        }
        public Item next() {
            if (!hasNext())
                throw new java.util.NoSuchElementException();

            return s[order[--i]];
        }
    }

    /**
     * unit testing (optional)
     * @param args
     */
    public static void main(String[] args) {
        RandomizedQueue<Integer> d = new RandomizedQueue<Integer>();

        for (int i = 0; i < 5; i++) {
            d.enqueue(i);
        }
        for (int i = 0; i < 5; i++) {
            System.out.println(d.dequeue());
        }
    }
}