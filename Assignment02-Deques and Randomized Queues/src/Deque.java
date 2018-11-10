import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

    private Node first, last;
    private int size;

    private class Node
    {
        Item item;
        Node next;
        Node previous;
    }

    /**
     * is the deque empty?
     * @return
     */
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * return the number of items on the deque
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * add the item to the front
     * @param item
     */
    public void addFirst(Item item) {
        if (item == null)
            throw new java.lang.IllegalArgumentException();

        ++size;
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        // special cases for empty queue
        if (oldfirst == null) last = first;
        else {
            first.next = oldfirst;
            oldfirst.previous = first;
        }
    }

    /**
     * add the item to the end
     * @param item
     */
    public void addLast(Item item) {
        if (item == null)
            throw new java.lang.IllegalArgumentException();

        ++size;
        Node oldlast = last;
        last = new Node();
        last.item = item;
        // special cases for empty queue
        if (isEmpty()) first = last;
        else {
            oldlast.next = last;
            last.previous = oldlast;
        }
    }

    /**
     * remove and return the item from the front
     * @return
     */
    public Item removeFirst() {
        if (isEmpty())
            throw new java.util.NoSuchElementException();

        --size;
        Item item = first.item;
        first = first.next;
        // special cases for empty queue
        if (isEmpty()) last = null;
        return item;
    }

    /**
     * remove and return the item from the end
     * @return
     */
    public Item removeLast() {
        if (isEmpty())
            throw new java.util.NoSuchElementException();

        --size;
        Item item = last.item;
        last = last.previous;
        // special cases for empty queue
        if (last == null) first = null;
        return item;
    }

    /**
     * return an iterator over items in order from front to end
     * @return
     */
    public Iterator<Item> iterator() { return new ListIterator(); }

    private class ListIterator implements Iterator<Item>
    {
        private Node current = first;

        public boolean hasNext() { return current != null; }

        public void remove() {
            /* not supported */
            throw new java.lang.UnsupportedOperationException();
        }

        public Item next()
        {
            if (!hasNext())
                throw new java.util.NoSuchElementException();

            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    /**
     * unit testing (optional)
     * @param args
     */
    public static void main(String[] args) {
        Deque<Integer> d = new Deque<Integer>();

        for (int i = 0; i < 5; i++) {
            d.addFirst(i);
        }
        for (int i = 0; i < 5; i++) {
            System.out.println(d.removeLast());
        }
    }
}