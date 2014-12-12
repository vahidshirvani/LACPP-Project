import java.util.concurrent.RecursiveTask;


public class Multiply extends RecursiveTask<Matrix>  {
	//static final int SEQUENTIAL_THRESHOLD = 100;
	Matrix a;
	Matrix b;
	Matrix c;
	int i;
	int j;
	int workload;
	int size;
	
	public Multiply(Matrix a,Matrix b,Matrix c,int i,int j,int workload,int size){
		this.a = a;
		this.b = b;
		this.c =c;
		this.i=i;
		this.j=j;
		this.workload = workload;
		this.size=size;
	}
	
	private int multiply(int [] row,int []column){
		int i = 0;
		int sum = 0;
		while(i<row.length){
			sum = sum + row[i] * column[i];
		}
		return sum;
	}
	private void doWorkload(){
		for(int x = 0;x<workload;x++){
			c.addElement(i,j,multiply(a.getRow(i),b.getColumn(i))); 
		}
	}
	@Override
	protected Matrix compute() {
		
		return null;
	}

}
