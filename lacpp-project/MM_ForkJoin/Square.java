import java.util.concurrent.RecursiveAction;


public class Square extends RecursiveAction {
	int workload;
	Matrix a;
	Matrix b;
	Matrix c;
	int size;
	int i;
	int j;
	
	public Square(Matrix a, Matrix b, Matrix c, int workload,int size) {
		this.workload = workload;
		this.a = a;
		this.b = b;
		this.c = c;
		this.size = size;
		this.i =  0;
		this.j = 0;
	}
	
	public Square(Matrix a, Matrix b, Matrix c, int workload,int size,int i,int j) {
		this.workload = workload;
		this.a = a;
		this.b = b;
		this.c = c;
		this.size = size;
		this.i =  i;
		this.j = j;
	}
	
	
	public int multiply(int [] row,int []column){
		int i = 0;
		int sum = 0;
		while(i<row.length){
			sum = sum + row[i] * column[i];
			i++;
		}
		return sum;
	}
	
	private void doWorkload(){
		int treshold = (int) Math.sqrt(size);
		int tmpj = j;
		int tmpi = i;
		
		for(int x = 0; x < treshold;x++) {
			for(int y = 0;y< treshold;y++) {
				c.addElement(i+x, j+y, multiply(a.getRow(i+x),b.getColumn(j+y)));
				
			}
		}
	}
	
	protected void compute() {
		if(size == workload){
			doWorkload();
		} else {
			int newSize = size/4;
			int newi =i + ((int) Math.sqrt(size))/2;
			int newj =j + ((int) Math.sqrt(size))/2; 
			Square task1 = new Square(a,b,c,workload,newSize,i,newj);
			Square task2 = new Square(a,b,c,workload,newSize,newi,j);
			Square task3 = new Square(a,b,c,workload,newSize,newi,newj);
			task1.fork();
			task2.fork();
			task3.fork();
			this.size = newSize;
			compute();
			task1.join();
			task2.join();
			task3.join();
		}
		
	}
	

}
