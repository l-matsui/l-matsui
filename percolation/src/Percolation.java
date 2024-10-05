import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    WeightedQuickUnionUF stream;
    WeightedQuickUnionUF streamFull;
    int[][] board;
    int N;
    int numOpen;
    int topStreamLoc;
    int bottomStreamLoc;

    // 1 is full, 0 is empty
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N has to be greater than 0");
        }
        this.N = N;
        this.board = new int[N][N];
        stream = new WeightedQuickUnionUF((N * N) + 2);
        streamFull = new WeightedQuickUnionUF((N * N) + 1);

        //create board
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                board[i][j] = 0;
            }
        }

        //notate locations
        topStreamLoc = N * N;
        bottomStreamLoc = (N * N) + 1;

        if (N == 1) {
            stream.union(0, topStreamLoc);
            streamFull.union(0, topStreamLoc);
            //stream.union(0, bottomStreamLoc);
        } else {
            // connect w top
            for (int i = 0; i < N; i++) {
                stream.union(i, topStreamLoc);
                streamFull.union(i, topStreamLoc);
            }

            // connect w bottom
            for (int i = 0; i < N; i++) {
                stream.union(((N * (N - 1)) + i), bottomStreamLoc);
            }
        }
    }

    public void open(int row, int col) {
        if (row >= N || row < 0 || col >= N || col < 0) {
            throw new IndexOutOfBoundsException("out of bounds");
        }

        if (board[row][col] == 1) {
            return;
        } else {
            board[row][col] = 1;
            numOpen++;
        }

        connectAroundHelper(row, col);
    }

    private void connectAroundHelper(int row, int col) {
        // above
        if (row > 0 && board[row - 1][col] == 1) {
            stream.union(((row - 1) * N) + col, (row * N) + col);
            streamFull.union(((row - 1) * N) + col, (row * N) + col);
        }

        // below
        if (row < N - 1 && board[row + 1][col] == 1) {
            stream.union(((row + 1) * N) + col, (row * N) + col);
            streamFull.union(((row + 1) * N) + col, (row * N) + col);
        }

        // left
        if (col > 0 && board[row][col - 1] == 1) {
            stream.union((row * N) + (col - 1), (row * N) + col);
            streamFull.union((row * N) + (col - 1), (row * N) + col);
        }

        // right
        if (col < N - 1 && board[row][col + 1] == 1) {
            stream.union((row * N) + (col + 1), (row * N) + col);
            streamFull.union((row * N) + (col + 1), (row * N) + col);
        }
    }

    public boolean isOpen(int row, int col) {
        if (row >= N || row < 0 || col >= N || col < 0) {
            throw new IndexOutOfBoundsException("out of bounds");
        }
        if (board[row][col] == 1) {
            return true;
        }
        return false;
    }

    public boolean isFull(int row, int col) {
        if (row >= N || row < 0 || col >= N || col < 0) {
            throw new IndexOutOfBoundsException("out of bounds");
        }
        if (!isOpen(row, col)) {
            return false;
        }
        return streamFull.connected((row * N) + col, topStreamLoc);
    }

    public int numberOfOpenSites() {
        return numOpen;
    }

    public boolean percolates() {
        if (N == 1) {
            return isOpen(0, 0);
        }
        return stream.connected(topStreamLoc, bottomStreamLoc);
    }
}
