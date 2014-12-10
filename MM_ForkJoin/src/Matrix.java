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
			int j = 0;
			int n;
            while (i < rows) {
                while (j < columns)  {
                    n = in.read() - 48;
                       //System.out.println(i);
                        this.rowMatrix[i][j] = n;
                    j++;
                   }
                n = in.read();
                i++;
                j = 0;
            }
            in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}
	
	void printMatrix() {
		for(int i = 0;i<rows;i++){
			for(int j = 0;j<columns;j++) {
				System.out.print(rowMatrix[i][j]);
			}
			System.out.println();
		}
	}
	

}
