import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF grid;
    private boolean[] open;
    private final int size;

    /**
     * create n-by-n grid, with all sites blocked
     * @param n
     * @throws java.lang.IllegalArgumentException
     */
    public Percolation(int n) {
        if (n <= 0)
            throw new java.lang.IllegalArgumentException();

        size = n;
        grid = new WeightedQuickUnionUF(n*n+2);

        open = new boolean[n*n+2];
        for (int i = 0;i < n*n+2;i++)
            open[i] = false;
        open[0] = open[n*n+1] = true;
    }

    /**
     * open site (row, col) if it is not open already
     * @param row
     * @param col
     * @throws java.lang.IllegalArgumentException
     */
    public void open(int row, int col) {
        if (!inBoundary(row,col))
            throw new java.lang.IllegalArgumentException();

        int coord = (row-1)*size+col;

        if (!open[coord]) {

            open[coord] = true;

            for (int i = -1;i <= 1;i = i+2) {

                if (inBoundary(row+i,col) && open[coord+i*size])
                    grid.union(coord, coord+i*size);
                if (inBoundary(row,col+i) && open[coord+i])
                    grid.union(coord, coord+i);

            }

            if (row == 1)grid.union(0, coord);
            if (row == size)grid.union(coord, size*size+1);

        }

    }

    /**
     * is site (row, col) open?
     * @param row
     * @param col
     * @return
     * @throws java.lang.IllegalArgumentException
     */
    public boolean isOpen(int row, int col) {
        if (!inBoundary(row,col))
            throw new java.lang.IllegalArgumentException();

        if (open[(row-1)*size+col] == true)
            return true;
        else
            return false;
    }

    /**
     * is site (row, col) full?
     * @param row
     * @param col
     * @return
     * @throws java.lang.IllegalArgumentException
     */
    public boolean isFull(int row, int col) {
        if (!inBoundary(row,col))
            throw new java.lang.IllegalArgumentException();

        if (grid.connected(0, (row-1)*size+col))
            return true;
        else
            return false;
    }

    /**
     * number of open sites
     * @return
     */
    public int numberOfOpenSites() {
        int count = 0;

        for (int i = 1;i < size*size+1;i++) {
            if (open[i])
                count++;
        }

        return count;
    }

    /**
     * does the system percolate?
     * @return
     */
    public boolean percolates() {
        if (grid.connected(0, size*size+1))
            return true;
        else
            return false;
    }

    /**
     * test client (optional)
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        Percolation p = new Percolation(4);
        if (!p.percolates())System.out.println("not percolate");
        p.open(1, 1);
        if (p.isOpen(1, 1))System.out.println("1,1 is open");
        if (p.isFull(1, 1))System.out.println("1,1 is full.");
        p.open(2,1);p.open(3,1);p.open(4,1);
        if (p.percolates())System.out.println("percolate");
    }

    private boolean inBoundary(int row, int col) {
        if (row<1 || row>size || col<1 || col>size)
            return false;
        else
            return true;
    }

}