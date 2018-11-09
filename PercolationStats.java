import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final double[] threshold;
    private final int times;
    private static final double z = 1.96;

    /**
     * perform trials independent experiments on an n-by-n grid
     * @param n
     * @param trials
     * @throws java.lang.IllegalArgumentException
     */
    public PercolationStats(int n, int trials) {

        if (n <= 0 || trials <= 0)
            throw new java.lang.IllegalArgumentException();

        times = trials;
        threshold = new double[trials];

        int row, col;
        Percolation percolation;

        for (int i = 0;i < trials;i++){

            percolation = new Percolation(n);

            while (!percolation.percolates()){
                row = StdRandom.uniform(n) + 1;
                col = StdRandom.uniform(n) + 1;

                percolation.open(row,col);
            }

            threshold[i] = percolation.numberOfOpenSites()/(n*n*1.0);
        }
    }

    /**
     * sample mean of percolation threshold
     * @return
     */
    public double mean() {

        return StdStats.mean(threshold);

    }

    /**
     * sample standard deviation of percolation threshold
     * @return
     */
    public double stddev() {

        return StdStats.stddev(threshold);

    }

    /**
     * low  endpoint of 95% confidence interval
     * @return
     */
    public double confidenceLo() {

        return StdStats.mean(threshold) - z * stddev() / Math.sqrt(times);

    }

    /**
     * high endpoint of 95% confidence interval
     * @return
     */
    public double confidenceHi() {

        return StdStats.mean(threshold) + z * stddev() / Math.sqrt(times);

    }

    /**
     * test client (described below)
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        PercolationStats percolationStats = new PercolationStats(1,10000);

        System.out.println(percolationStats.mean());

        System.out.println(percolationStats.stddev());

        System.out.println(percolationStats.confidenceLo());

        System.out.println(percolationStats.confidenceHi());

    }

}