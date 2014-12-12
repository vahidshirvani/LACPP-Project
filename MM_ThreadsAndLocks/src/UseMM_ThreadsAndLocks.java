import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class UseMM_ThreadsAndLocks {
		
	// It's simple to understand, it's mainly used for verification purposes 
	public static int[][] sequentialMultiply(int[][] a, int[][] b) {
		MatrixUtility.sanityCheck(a, b);
		
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
	
	public static int[][] parallelMultiply1(int[][] a, int[][] b, int numOfThreads) {
		MatrixUtility.sanityCheck(a, b);
		final int numOfRows = a.length;
		final int numOfColumns = b[0].length;
		int[][] c = new int[numOfRows][numOfColumns];
		final int rowLengthOfA = a[0].length;
		class Work implements Runnable {
			int i;
			int[][] a;
			int[][] b;
			int[][] c;
			Work(int id, int[][] a, int[][] b, int[][] c) {
				this.i = id;
				this.a = a;
				this.b = b;
				this.c = c;
			}
			@Override
			public void run() {							
				for(int j = 0; j <numOfColumns; j++)
					for(int k = 0; k < rowLengthOfA; k++)
						c[i][j] = c[i][j] + a[i][k] * b[k][j];
			}	
		}
		ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);
        Work[] works = new Work[numOfRows];
		for (int i = 0; i < numOfRows; i++) {
            works[i] = new Work(i, a, b, c);
            executor.execute(works[i]);
         }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
		return c;
	}
	public static void main(String[] args) {
		
		int[][] a = MatrixUtility.generateMatrix(3, 2); 
		int[][] b = MatrixUtility.generateMatrix(2, 2); 
		int[][] c1 = sequentialMultiply(a, b);
		int[][] c2 = parallelMultiply1(a, b, 4);
		
		System.out.println(MatrixUtility.matrixEqual(c1, c2));
		System.out.println(MatrixUtility.matrixToString(a));
		System.out.println(MatrixUtility.matrixToString(b));
		System.out.println(MatrixUtility.matrixToString(c1));
		System.out.println(MatrixUtility.matrixToString(c2));
	}

}
