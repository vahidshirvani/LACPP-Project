import java.util.Arrays;


public class MatrixUtility {
	
	public static int[][] generateMatrix(int rows, int columns) {
		int[][] matrix = new int[rows][columns];
		for(int i = 0; i < rows; i++)
			for(int j = 0; j < columns; j++)
				matrix[i][j] = (int) (Math.random() * 10);
		return matrix;
	}
	
	public static String matrixToString(int[][] matrix) {
		StringBuilder sb = new StringBuilder();
		for(int[] row: matrix) {
			for(int i: row) {
				sb.append(String.format("%04d", i));
				sb.append(" ");
			}
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}
	
	// checks if two matrices can be multiplied. number of columns in A 
	// must be equal to number of rows in B
	public static boolean sanityCheck1(int[][] a, int[][] b) {
		boolean isValid = true;
		int rowLengthOfA = a[0].length;
		int columnLengthOfB = b.length;
		if(rowLengthOfA != columnLengthOfB) {
			System.out.println("two matrices can not be multiplied");
			System.exit(1);
		}
		return isValid;
	}
	
	// checks the matrix can be divided into smaller grids
	// the number of columns in A and B must be divisible by block size
	public static boolean sanityCheck2(int[][] a, int[][] b, int blockSize) {
		boolean isValid = true;
		int rowLengthOfA = a[0].length;
		int rowLengthOfB = b[0].length;
		if((rowLengthOfA % blockSize != 0) || (rowLengthOfB % blockSize != 0)) {
			System.out.println("can not fit blocks inside matrices");
			System.exit(1);
		}
		return isValid;
	}
	
	// checks if the matrix can be divided into several work units
	// the number of rows in matrix A must be divisible by number of work
	public static boolean sanityCheck3(int[][] a, int numOfWorks) {
		boolean isValid = true;
		int numOfRowsInA = a.length;
		if((numOfRowsInA % numOfWorks) != 0) {
			System.out.println("could not divide into work");
			System.exit(1);
		}
		return isValid;
	}
	
	// checks if the matrix can be divided into several work units
	// the number of rows in matrix A must be divisible by number of work
	public static boolean sanityCheck4(int[][] a, int numOfWorks) {
		boolean isValid = true;
		int numOfColumnsInA = a[0].length;
		if((numOfColumnsInA % numOfWorks) != 0) {
			System.out.println("could not divide into work");
			System.exit(1);
		}
		return isValid;
	}
	
	public static boolean matrixEqual(int[][] a, int[][] b) {
		boolean isEqual = true;
		int numOfRowsInA = a.length; 
		int numOfRowsInB = b.length;
		int numOfColumnsInA = a[0].length;
		int numOfColumnsInB = b[0].length;
		if (numOfRowsInA == numOfRowsInB && numOfColumnsInA == numOfColumnsInB) {
			for(int i = 0; i < numOfRowsInA; i++)
				if (!Arrays.equals(a[i], b[i]))
					isEqual = false;
		} else {
			isEqual = false;
		}
		return isEqual;		
	}

}
