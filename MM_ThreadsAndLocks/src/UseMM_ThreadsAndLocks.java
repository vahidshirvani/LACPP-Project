
public class UseMM_ThreadsAndLocks {
	
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
	
	public static int[][] SequentialMultiply(int[][] a, int[][] b) {
		int rowLengthOfA = a[0].length;
		int columnLengthOfB = b.length;
		if(rowLengthOfA != columnLengthOfB) {
			System.out.println("two matrices can not be multiplied");
			System.exit(1);
		}
		
		int numOfRows = a.length;
		int numOfColumns = b[0].length;
		int[][] c = new int[numOfRows][numOfColumns];
		
		for(int i = 0; i < numOfRows; i++)
			for(int j = 0; j <numOfColumns; j++)
				for(int k = 0; k < rowLengthOfA; k++)
					c[i][j] = c[i][j] + a[i][k] * b[k][j];
		return c;
	}
	
	public static void main(String[] args) {
		
		int[][] a = generateMatrix(3, 2); 
		int[][] b = generateMatrix(2, 2); 
		int[][] c = SequentialMultiply(a, b);
		
		System.out.println(matrixToString(a));
		System.out.println(matrixToString(b));
		System.out.println(matrixToString(c));
	}

}
