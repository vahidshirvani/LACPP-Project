import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class UseMM_ThreadsAndLocks {
		
	// It's simple to understand, it's mainly used for verification purposes 
	public static int[][] sequentialMultiply1(int[][] a, int[][] b) {
		MatrixUtility.sanityCheck1(a, b);
		
		int numOfRows = a.length;
		int numOfColumns = b[0].length;
		int[][] c = new int[numOfRows][numOfColumns];
		int rowLengthOfA = a[0].length;
		
		for(int i = 0; i < numOfRows; i++)
			for(int j = 0; j <numOfColumns; j++)
				for(int k = 0; k < rowLengthOfA; k++)
					c[i][j] = c[i][j] + a[i][k] * b[k][j];
		return c;
	}
	
	public static int[][] sequentialMultiply2(int[][] a, int[][] b) {
		MatrixUtility.sanityCheck1(a, b);
		int numOfRowsInA = a.length;
		int numOfColumnsInB = b[0].length;
		int[][] c = new int[numOfRowsInA][numOfColumnsInB];
		int rowLengthOfA = a[0].length;
		int rowLengthOfB = b[0].length;
		
		for(int i = 0; i < numOfRowsInA; i++)						
			for(int j = 0; j < rowLengthOfA; j++)
				for(int k = 0; k < rowLengthOfB; k++)
					c[i][k] = c[i][k] + a[i][j] * b[j][k];
		return c;
	}
	// sequential but fast because of cache hits 
	public static int[][] sequentialMultiply3(int[][] a, int[][] b, int blockSize) {
		MatrixUtility.sanityCheck1(a, b);
		MatrixUtility.sanityCheck2(a, b, blockSize);
		
		int numOfRowsInA = a.length;
		int numOfColumnsInB = b[0].length;
		int[][] c = new int[numOfRowsInA][numOfColumnsInB];
		int rowLengthOfA = a[0].length;
		int rowLengthOfB = b[0].length;
		
		for(int jj = 0; jj < rowLengthOfB; jj = jj+blockSize)
			for (int kk = 0; kk < rowLengthOfA; kk = kk+blockSize)
				for (int i = 0; i < numOfRowsInA; i = i+1)
					for (int j = jj; j < Math.min(jj+blockSize,rowLengthOfB); j = j+1) {
						int r = 0;
						for (int k = kk; k < Math.min(kk+blockSize,rowLengthOfA); k = k+1)
							r = r + a[i][k]*b[k][j];
						c[i][j] = c[i][j] + r;
					}
		return c;
	}
	
	// one element in C is a dot product between row in A with column in B
	// we get cache misses when we traverse B vertically
	public static int[][] parallelMultiply1(int[][] a, int[][] b, int numOfThreads, int numOfWorks) {
		MatrixUtility.sanityCheck1(a, b);
		MatrixUtility.sanityCheck3(a, numOfWorks);
		final int numOfRows = a.length;
		final int numOfColumns = b[0].length;
		int[][] c = new int[numOfRows][numOfColumns];
		final int rowLengthOfA = a[0].length;
		class Work implements Runnable {
			int[] rows;
			int[][] a;
			int[][] b;
			int[][] c;
			Work(int[] rows, int[][] a, int[][] b, int[][] c) {
				this.rows = rows;
				this.a = a;
				this.b = b;
				this.c = c;
			}
			@Override
			public void run() {	
				for(int i: rows)
					for(int j = 0; j <numOfColumns; j++)
						for(int k = 0; k < rowLengthOfA; k++)
							c[i][j] = c[i][j] + a[i][k] * b[k][j];
			}	
		}
		ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);
        Work[] works = new Work[numOfWorks];
		for (int i = 0; i < numOfWorks; i++) {
			int workSize = numOfRows/numOfWorks;
			int[] rows = new int[workSize];
			for(int j = 0; j < workSize; j++)
				rows[j] = i*workSize + j;
			
            works[i] = new Work(rows, a, b, c);
            executor.execute(works[i]);
         }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
		return c;
	}
	
	// we get don't get cache misses because we traverse B horizontally
	// we can traverse like that because we are multiplying one element 
	// in A with a whole row in B before continuing to the next row in B
	public static int[][] parallelMultiply2(int[][] a, int[][] b, int numOfThreads, int numOfWorks) {
		MatrixUtility.sanityCheck1(a, b);
		MatrixUtility.sanityCheck3(a, numOfWorks);
		final int numOfRows = a.length;
		final int numOfColumns = b[0].length;
		int[][] c = new int[numOfRows][numOfColumns];
		final int rowLengthOfA = a[0].length;
		final int rowLengthOfB = b[0].length;
		class Work implements Runnable {
			int[] rows;
			int[][] a;
			int[][] b;
			int[][] c;
			Work(int[] rows, int[][] a, int[][] b, int[][] c) {
				this.rows = rows;
				this.a = a;
				this.b = b;
				this.c = c;
			}
			@Override
			public void run() {	
				for(int i: rows)						
					for(int j = 0; j <rowLengthOfA; j++)
						for(int k = 0; k < rowLengthOfB; k++)
							c[i][k] = c[i][k] + a[i][j] * b[j][k];
			}	
		}
		ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);
        Work[] works = new Work[numOfWorks];
		for (int i = 0; i < numOfWorks; i++) {
			int workSize = numOfRows/numOfWorks;
			int[] rows = new int[workSize];
			for(int j = 0; j < workSize; j++)
				rows[j] = i*workSize + j;		
            works[i] = new Work(rows, a, b, c);
            executor.execute(works[i]);
         }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
		return c;
	}

	// a multi-threaded solution which also has the benefit of cache hits
	public static int[][] parallelMultiply3(int[][] a, int[][] b, int numOfThreads, int numOfWorks, int bs) {
		MatrixUtility.sanityCheck1(a, b);
		MatrixUtility.sanityCheck2(a, b, bs);
		MatrixUtility.sanityCheck3(a, numOfWorks);
		final int numOfRowsInA = a.length;
		final int numOfColumnsInB = b[0].length;
		int[][] c = new int[numOfRowsInA][numOfColumnsInB];
		final int rowLengthOfA = a[0].length;
		final int rowLengthOfB = b[0].length;
		final int blockSize = bs;
		class Work implements Runnable {
			int[] rows;
			int[][] a;
			int[][] b;
			int[][] c;
			Work(int[] rows, int[][] a, int[][] b, int[][] c) {
				this.rows = rows;
				this.a = a;
				this.b = b;
				this.c = c;
			}
			@Override
			public void run() {		
				for(int jj = 0; jj < rowLengthOfB; jj = jj+blockSize)
					for (int kk = 0; kk < rowLengthOfA; kk = kk+blockSize)
						for(int i: rows)
							for (int j = jj; j < Math.min(jj+blockSize,rowLengthOfB); j = j+1) {
								int r = 0;
								for (int k = kk; k < Math.min(kk+blockSize,rowLengthOfA); k = k+1)
									r = r + a[i][k]*b[k][j];
								c[i][j] = c[i][j] + r;
							}				
			}	
		}
		ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);
        Work[] works = new Work[numOfWorks];
		for (int i = 0; i < numOfWorks; i++) {
			int workSize = numOfRowsInA/numOfWorks;
			int[] rows = new int[workSize];
			for(int j = 0; j < workSize; j++)
				rows[j] = i*workSize + j;		
            works[i] = new Work(rows, a, b, c);
            executor.execute(works[i]);
         }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
		return c;
	}
	
	public static void main(String[] args) {
		
		int[][] a = MatrixUtility.generateMatrix(100, 128); 
		int[][] b = MatrixUtility.generateMatrix(128, 512); 
		int[][] c1 = sequentialMultiply1(a, b);
		int[][] c2 = sequentialMultiply2(a, b);
		int[][] c3 = sequentialMultiply3(a, b, 16);
		int[][] c4 = parallelMultiply1(a, b, 4, 4);
		int[][] c5 = parallelMultiply2(a, b, 4, 4);
		int[][] c6 = parallelMultiply3(a, b, 4, 4, 16);
		
		System.out.println(MatrixUtility.matrixEqual(c1, c2));
		System.out.println(MatrixUtility.matrixEqual(c1, c3));
		System.out.println(MatrixUtility.matrixEqual(c1, c4));
		System.out.println(MatrixUtility.matrixEqual(c1, c5));
		System.out.println(MatrixUtility.matrixEqual(c1, c6));
//		System.out.println(MatrixUtility.matrixToString(a));
//		System.out.println(MatrixUtility.matrixToString(b));
//		System.out.println(MatrixUtility.matrixToString(c1));
	}

}
