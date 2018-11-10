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
