#### Deque.java

> 双向链表

```java
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
```

#### RandomizedQueue.java

> - 可变长度数组
> - 数组随机弹出值：交换随机位置与N-1位置的值，再弹出N-1

```java
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
```

#### Permutation.java

> 命令行参数 args[]

```java
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {

    public static void main(String[] args) {
        RandomizedQueue<String> s = new RandomizedQueue<String>();

        int k = Integer.valueOf(args[0]);

        while (!StdIn.isEmpty()) {
            String line = StdIn.readString();
            s.enqueue(line);
        }

        for (int i = 0; i < k; i++) {
            String item = s.dequeue();
            StdOut.println(item);
        }

    }

}

```

> Iterator的实现