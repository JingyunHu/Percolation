import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

// Models an N-by-N percolation system.
public class Percolation {
    private boolean[][] array; //creates array of boolean class level
    private int numOfSitesOpen = 0; // initializes count
    private int source = 0; //initialized source
    private int sink; //creates class level variable for sink
    private WeightedQuickUnionUF unionFind; // creates an instance of the class
    private int N; //creates class level of N
    private WeightedQuickUnionUF copyUnionFind; //creates a copy of the class
    // Create an N-by-N grid, with all sites blocked.
    public Percolation(int N) {
        this.N = N;
        this.array = new boolean[N][N];
        this.sink = N * N + 1; //tells that the sink is + 1
        this.unionFind = new WeightedQuickUnionUF(N * N + 2); 
        //+ 2 including sink
        this.copyUnionFind = new WeightedQuickUnionUF(N * N + 1);
        //not including sink
        if (N <= 0) { //exception
            throw new IllegalArgumentException("Illegal Argument");
        }
        for (int r = 0; r <= N; r++) { //unifies the top row with source 
            // and bottom row with the sink
          unionFind.union(encode(0, r), source);
          unionFind.union(encode(N - 1, r), sink);
          copyUnionFind.union(encode(0, r), source);
        } 
        /*for (int r = N * N; r > (N * N - N); r--) {
          unionFind.union(r, sink);
        }*/
    }

    // Open site (i, j) if it is not open already.
    public void open(int i, int j) {
        if (i < 0 || i > N - 1 || j < 0 || j > N - 1) {
            throw new IndexOutOfBoundsException("Illegal index.");
        }
    
        if (!isOpen(i, j)) {
           array[i][j] = true;
           //System.out.println("Percolation site "+i);
           numOfSitesOpen++;
           if ((i - 1) >= 0 && array[i-1][j]) { //top
               unionFind.union(encode(i, j), encode(i-1, j));
               copyUnionFind.union(encode(i, j), encode(i-1, j));
            } 
           if ((j + 1) < N && array[i][j + 1]) { //right
               unionFind.union(encode(i, j), encode(i, j + 1));
               copyUnionFind.union(encode(i, j), encode(i, j + 1));
            }
           if ((j - 1) >= 0 && array[i][j - 1]) { //left
               unionFind.union(encode(i, j), encode(i, j - 1));
               copyUnionFind.union(encode(i, j), encode(i, j - 1));
           }
           if ((i + 1) < N && array[i + 1][j]) { //bottom
               unionFind.union(encode(i, j), encode(i+1, j));
               copyUnionFind.union(encode(i, j), encode(i+1, j));
            } 
           //StdOut.println("i= "+i+" N=" + N);
           /*if (i == (N - 1) && unionFind.connected(encode(i, j), source)) {
               //StdOut.println("i= "+i);
               unionFind.union(encode(i, j), sink);
            }*/
        }  
        } 
    // Is site (i, j) open?
    public boolean isOpen(int i, int j) {
         if (i < 0 || i > N - 1 || j < 0 || j > N - 1) {
            throw new IndexOutOfBoundsException("Illegal index.");
        }
        if (array[i][j]) { //checks if it is open
            return true;
        }
        return false;
        }
        //return array[i][j];
       
    // Is site (i, j) full? check if its connected to the source
    public boolean isFull(int i, int j) {
         if (i < 0 || i > N - 1 || j < 0 || j > N - 1) {
            throw new IndexOutOfBoundsException("Illegal index.");
        }
        if (array[i][j] && copyUnionFind.connected(encode(i, j), source)) {
            return true; //checks if it is full aka connected to source
        }
        return false;
    }

    // Number of open sites.
    public int numberOfOpenSites() {
        return numOfSitesOpen; //returns the number of open sites
    }

    // Does the system percolate? 
    public boolean percolates() {
        if (unionFind.connected(source, sink)) {
            return true; //checks if source is connected to sink
        }
        return false;
    }

    // An integer ID (1...N) for site (i, j).
    private int encode(int i, int j) {
        return (i * N + j + 1); //transforms teh (i,j) into a number
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Percolation perc = new Percolation(N);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
        }
        StdOut.println(perc.numberOfOpenSites() + " open sites");
        if (perc.percolates()) {
            StdOut.println("percolates");
        }
        else {
            StdOut.println("does not percolate");
        }
        
        // Check if site (i, j) optionally specified on the command line
        // is full.
        if (args.length == 3) {
            int i = Integer.parseInt(args[1]);
            int j = Integer.parseInt(args[2]);
            StdOut.println(perc.isFull(i, j));
        }
    }
}
