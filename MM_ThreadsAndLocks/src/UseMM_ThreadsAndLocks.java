import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


public class UseMM_ThreadsAndLocks {
		
	// It's simple to understand, it's mainly used for verification purposes 
	public static int[][] sequentialMultiply10(int[][] a, int[][] b) {
		MatrixUtility.sanityCheck1(a, b);
		
		int numOfRowsInA = a.length;
		int numOfColumnsInB = b[0].length;
		int[][] c = new int[numOfRowsInA][numOfColumnsInB];
		int rowLengthOfA = a[0].length;
		
		for(int i = 0; i < numOfRowsInA; i++)
			for(int j = 0; j < numOfColumnsInB; j++)
				for(int k = 0; k < rowLengthOfA; k++)
					c[i][j] = c[i][j] + a[i][k] * b[k][j];
		return c;
	}
    
    // one element in C is a dot product between row in A with column in B
	// we get cache misses when we traverse B vertically
	public static int[][] parallelMultiply11(int[][] a, int[][] b, int numOfThreads, int numOfWorks) {
		MatrixUtility.sanityCheck1(a, b);
		MatrixUtility.sanityCheck3(a, numOfWorks);
		final int numOfRowsInA = a.length;
		final int numOfColumnsInB = b[0].length;
		int[][] c = new int[numOfRowsInA][numOfColumnsInB];
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
					for(int j = 0; j < numOfColumnsInB; j++)
						for(int k = 0; k < rowLengthOfA; k++)
							c[i][j] = c[i][j] + a[i][k] * b[k][j];
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
    
    // one element in C is a dot product between row in A with column in B
	// we get cache misses when we traverse B vertically
	public static int[][] parallelMultiply12(int[][] a, int[][] b, int numOfThreads, int numOfWorks) {
		MatrixUtility.sanityCheck1(a, b);
		MatrixUtility.sanityCheck4(a, numOfWorks);
		final int numOfRowsInA = a.length;
		final int numOfColumnsInB = b[0].length;
		AtomicInteger[][] c = new AtomicInteger[numOfRowsInA][numOfColumnsInB];
        for(int i = 0; i < numOfRowsInA; i++)
        	for(int j = 0; j < numOfColumnsInB; j++)
        		c[i][j] = new AtomicInteger();
		final int numOfColumnsInA = a[0].length;
		class Work implements Runnable {
			int[] columns;
			int[][] a;
			int[][] b;
			AtomicInteger[][] c;
			Work(int[] columns, int[][] a, int[][] b, AtomicInteger[][] c) {
				this.columns = columns;
				this.a = a;
				this.b = b;
				this.c = c;
			}
			@Override
			public void run() {	
				for(int i = 0; i < numOfRowsInA; i++)
					for(int j = 0; j < numOfColumnsInB; j++) {
						int r = 0;
						for(int k: columns)
							r += a[i][k] * b[k][j];		
						c[i][j].addAndGet(r);
					}
			}	
		}
		ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);
        Work[] works = new Work[numOfWorks];
		for (int i = 0; i < numOfWorks; i++) {
			int workSize = numOfColumnsInA/numOfWorks;
			int[] columns = new int[workSize];
			for(int j = 0; j < workSize; j++)
				columns[j] = i*workSize + j;
			
            works[i] = new Work(columns, a, b, c);
            executor.execute(works[i]);
         }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        
        int[][] ret = new int[numOfRowsInA][numOfColumnsInB];
        for(int i = 0; i < numOfRowsInA; i++)
        	for(int j = 0; j < numOfColumnsInB; j++)
        		ret[i][j] = c[i][j].get();        	
		return ret;
	}
	
	public static int[][] sequentialMultiply20(int[][] a, int[][] b) {
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
    
	// we get don't get cache misses because we traverse B horizontally
	// we can traverse like that because we are multiplying one element 
	// in A with a whole row in B before continuing to the next row in B
	public static int[][] parallelMultiply21(int[][] a, int[][] b, int numOfThreads, int numOfWorks) {
		MatrixUtility.sanityCheck1(a, b);
		MatrixUtility.sanityCheck3(a, numOfWorks);
		final int numOfRowsInA = a.length;
		final int numOfColumnsInB = b[0].length;
		int[][] c = new int[numOfRowsInA][numOfColumnsInB];
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
    
	// sequential but fast because of cache hits 
	public static int[][] sequentialMultiply30(int[][] a, int[][] b, int blockSize) {
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

	// a multi-threaded solution which also has the benefit of cache hits
	public static int[][] parallelMultiply31(int[][] a, int[][] b, int numOfThreads, int numOfWorks, int bs) {
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
		
		int algotithm = Integer.parseInt(args[0]);
		int numOfThreads = Integer.parseInt(args[1]);
		int numOfWorks = Integer.parseInt(args[2]);
		int blockSize = Integer.parseInt(args[3]);
		
		int numOfIterations = 10;
		long[] time = new long[numOfIterations];		
		for(int i = 0; i < numOfIterations; i++) {
			int[][] a = MatrixUtility.generateMatrix(128, 2048); 
			int[][] b = MatrixUtility.generateMatrix(2048, 8192); 
			int[][] c;
			
			long start = System.currentTimeMillis();
			switch(algotithm) {
				case 10:			
					c = sequentialMultiply10(a, b);
					break;
				case 11:				
					c = parallelMultiply11(a, b, numOfThreads, numOfWorks);
					break;
				case 12:					
					c = parallelMultiply12(a, b, numOfThreads, numOfWorks);
					break;
				case 20:					
					c = sequentialMultiply20(a, b);
					break;
				case 21:
					c = parallelMultiply21(a, b, numOfThreads, numOfWorks);
					break;
				case 30:				
					c = sequentialMultiply30(a, b, blockSize);
					break;
				case 31:
					c = parallelMultiply31(a, b, numOfThreads, numOfWorks, blockSize);
					break;
				default:
					System.out.println("chose one of these 10, 11, 12, 20, 21, 30, 31");
					break;
			}
			long end = System.currentTimeMillis();
			long delta = end - start;
			time[i] = delta;
//			int[][] c10 = sequentialMultiply10(a, b);
//			System.out.println(MatrixUtility.matrixEqual(c10, c));			
//			System.out.println(MatrixUtility.matrixToString(a));
//			System.out.println(MatrixUtility.matrixToString(b));
//			System.out.println(MatrixUtility.matrixToString(c));
		}
		
		Arrays.sort(time);
		long sum = 0L;
		for(int i = 1; i < numOfIterations - 1; i++)
			sum += time[i];
		double average = ((double)sum)/((double)(numOfIterations-2));
		System.out.println("average time was: " + average + "ms");
	}

}
