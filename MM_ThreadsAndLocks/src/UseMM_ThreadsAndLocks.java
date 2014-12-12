import java.util.ArrayList;
import java.util.List;
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
	
	// one element in C is a dot product between row in A with column in B
	// we get cache misses when we traverse B vertically
	public static int[][] parallelMultiply1(int[][] a, int[][] b, int numOfThreads, int numOfWorks) {
		MatrixUtility.sanityCheck(a, b);
		final int numOfRows = a.length;
		final int numOfColumns = b[0].length;
		int[][] c = new int[numOfRows][numOfColumns];
		final int rowLengthOfA = a[0].length;
		class Work implements Runnable {
			List<Integer> rows;
			int[][] a;
			int[][] b;
			int[][] c;
			Work(List<Integer> rows, int[][] a, int[][] b, int[][] c) {
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
			int workSize = (numOfRows/numOfWorks) + 1;
			List<Integer> rows = new ArrayList<Integer>();
			for(int j = 0; j < workSize; j++) {
				int rowNumber = i*workSize + j;
				if(rowNumber < numOfRows)
					rows.add(rowNumber);
			}
			
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
		MatrixUtility.sanityCheck(a, b);
		final int numOfRows = a.length;
		final int numOfColumns = b[0].length;
		int[][] c = new int[numOfRows][numOfColumns];
		final int rowLengthOfA = a[0].length;
		final int rowLengthOfB = b[0].length;
		class Work implements Runnable {
			List<Integer> rows;
			int[][] a;
			int[][] b;
			int[][] c;
			Work(List<Integer> rows, int[][] a, int[][] b, int[][] c) {
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
			int workSize = (numOfRows/numOfWorks) + 1;
			List<Integer> rows = new ArrayList<Integer>();
			for(int j = 0; j < workSize; j++) {
				int rowNumber = i*workSize + j;
				if(rowNumber < numOfRows)
					rows.add(rowNumber);				
			}			
            works[i] = new Work(rows, a, b, c);
            executor.execute(works[i]);
         }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
		return c;
	}
	
	public static void main(String[] args) {
		
		int[][] a = MatrixUtility.generateMatrix(102, 10); 
		int[][] b = MatrixUtility.generateMatrix(10, 10); 
		int[][] c1 = sequentialMultiply(a, b);
		int[][] c2 = parallelMultiply1(a, b, 4, 4);
		int[][] c3 = parallelMultiply2(a, b, 4, 4);
		
		System.out.println(MatrixUtility.matrixEqual(c1, c2));
		System.out.println(MatrixUtility.matrixEqual(c1, c3));
//		System.out.println(MatrixUtility.matrixToString(a));
//		System.out.println(MatrixUtility.matrixToString(b));
//		System.out.println(MatrixUtility.matrixToString(c1));
//		System.out.println(MatrixUtility.matrixToString(c2));
	}

}
