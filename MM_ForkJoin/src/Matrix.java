import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class Matrix {
	int[][] rowMatrix;
	int [][] columnMatrix;
	int rows;
	int columns;
	public Matrix(int rows,int columns,String filename) {
		this.rowMatrix = new int[rows][columns];
		this.columnMatrix = new int[columns][rows];
		this.rows = rows;
		this.columns = columns;
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			int i = 0;
			
			String line = in.readLine();
			
			String [] strArr = line.split(" ");
            while (i < rows) {
            	for(int j= 0;j<strArr.length;j++) {
            		rowMatrix[i][j] = Integer.parseInt(strArr[j]);	
            	}
            	line = in.readLine();
        		if(line != null) {
        			strArr = line.split(" ");
        		}
            	i++;
            }
            in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.transpose();
		
	}
	
	int [] getColumn(int col) {
		return columnMatrix[col];
	}
	
	int [] getRow(int row) {
		return rowMatrix[row];
	}
	
	private void transpose() {
		
		for(int i = 0;i<rows;i++){
			for (int j = 0;j<columns;j++) {
				columnMatrix[j][i] = rowMatrix[i][j];
			}
		}
		
	}
	void printMatrix() {
		for(int i = 0;i<rows;i++){
			for(int j = 0;j<columns;j++) {
				System.out.print(rowMatrix[i][j]+" ");
			}
			System.out.println();
		}
	}
	

}
