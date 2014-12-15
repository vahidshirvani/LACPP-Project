import java.util.concurrent.RecursiveAction;


public class Multiply extends RecursiveAction  {
	 static final int SEQUENTIAL_THRESHOLD = 100;
	 
	private Matrix a;
	private Matrix b;
	private Matrix c;
	private int i;
	private int j;
	private int workload;
	public Multiply(Matrix a, Matrix b, Matrix c){
		this.a = a;
		this.b = b;
		this.c = c;
		this.i = 0;
		this.j = 0;
		this.workload = c.rows*c.columns;
	}
	
	public Multiply(Matrix a, Matrix b, Matrix c,int i, int j,int workload){
		this.a = a;
		this.b = b;
		this.c = c;
		this.i = i;
		this.j = j;
		this.workload = workload;
	}
	
	private int multiply(int [] row,int []column){
		int i = 0;
		int sum = 0;
		while(i<row.length){
			sum = sum + row[i] * column[i];
			i++;
		}
		return sum;
	}
	private void doWorkload() {
		for(int counter = 0;counter < workload;counter++){
			int element = multiply(a.getRow(i),b.getColumn(j));
			c.addElement(i, j,element);
			if(j != (c.columns-1)) {
				j = j + 1;
			} else if (j == (c.columns-1)) {
				j = 0;
				if(i != (c.rows-1)) {
					i =i+1;
				}
			}
		}
	}
	
	protected void compute() {
		if(workload <= SEQUENTIAL_THRESHOLD){
			doWorkload();
		} else {
			int newWorkload = workload - (workload/2);
			int newi = i + (newWorkload/c.rows);
			int newj = j + (newWorkload % c.columns);
			
			Multiply task = new Multiply(a,b,c,newi,newj,newWorkload);
			task.fork();
			compute();
			task.join();
		}
	}


}
