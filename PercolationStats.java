import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


// Estimates percolation threshold for an N-by-N percolation system.
public class PercolationStats {
    private boolean[][] array;
    private double[] p;
    private Percolation percolation;
    private int T;
    private double mn = 0;
    private double sd = 0;

    // Perform T independent experiments (Monte Carlo simulations) on an 
    // N-by-N grid.
    public PercolationStats(int N, int T) {
        p = new double[T];
        int count = 0;
        this.T = T;
        if (N <= 0 || T <= 0) {
            throw new java.lang.IllegalArgumentException(
            " Value must be greater than 0.");
        }
        //array = new boolean[N][N]; 
        
        for (int i = 0; i < T; i++) {
            //System.out.println("repeating exp "+i);
            // open the system until it perculates
            // create an instance of percolation
            count = 0;
            percolation = new Percolation(N);
            int row = 0;
            int col = 0;
            while (!percolation.percolates()) {
                row = StdRandom.uniform(0, N);
                col = StdRandom.uniform(0, N);
                //System.out.println("opening "+row+" "+col);
                if (!percolation.isOpen(row, col)) {
                   count++;
                   percolation.open(row, col);
                }
            }
            p[i] = count / (double) (N * N);
        }
    }
    
    // Sample mean of percolation threshold.
    public double mean() { //gets the mean
       double sum = 0;
       for (int i = 0; i < p.length; i++) {
           sum = sum + p[i];
       } 
       return sum/T;
    }

    // Sample standard deviation of percolation threshold.
    public double stddev() { //gets the Standard deviation
        /*if (mn == 0) {
           mn = mean();
        }
        double sum = 0;
        for (int i = 0; i < p.length; i++) {
           sum = sum + Math.pow((p[i] - mn), 2);
        }
        return sum / (T - 1);*/
        return StdStats.stddev(p);
    }

    // Low endpoint of the 95% confidence interval.
    public double confidenceLow() {
        if (sd == 0) { //checks the basecase
            sd = stddev();
        }
        //return (mn - ((1.96 * Math.sqrt(sd))/Math.sqrt(T)));
        return (mean() - ((1.96 *stddev())/Math.sqrt(T)));
    }

    // High endpoint of the 95% confidence interval.
    public double confidenceHigh() {
       if (sd == 0) {
            sd = stddev();
        }
       //return (mn + ((1.96 * Math.sqrt(sd))/Math.sqrt(T)));
       return (mean() + ((1.96 * stddev())/Math.sqrt(T)));
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(N, T);
        StdOut.printf("mean           = %f\n", stats.mean());
        StdOut.printf("stddev         = %f\n", stats.stddev());
        StdOut.printf("confidenceLow  = %f\n", stats.confidenceLow());
        StdOut.printf("confidenceHigh = %f\n", stats.confidenceHigh());
    }
}
