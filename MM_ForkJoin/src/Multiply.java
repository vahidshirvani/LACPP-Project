import java.util.concurrent.RecursiveTask;


public class Multiply extends RecursiveTask<Integer>  {
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
	
	public int multiply(int [] row,int []column){
		int i = 0;
		int sum = 0;
		while(i<row.length){
			sum = sum + row[i] * column[i];
			i++;
		}
		return sum;
	}
	public void doWorkload(){
		for(int x = 0;x<workload;x++){
			c.addElement(j,i,multiply(a.getRow(i),b.getColumn(i)));
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
	
	public int calcNewi(){
		int tmp;
		tmp = i + (workload/c.rows);
		return tmp;
	}
	public int calcNewj(){
		int tmp;
		tmp = j + (workload % c.columns);
		return tmp;

	}
	
	@Override
	protected Integer compute() {
		if((size)-(i*c.columns + j) <= workload){
			this.workload = (size)-(i*c.rows + j); 
			doWorkload();
		} else if(i*c.columns + j >= (size-1)){
			this.workload = (size)-(i*c.rows + j); 
			doWorkload();
		}else {
			Multiply task = new Multiply(a,b,c,calcNewi(),calcNewj(),workload,size);
			task.fork();
			doWorkload();
			task.join();
			
		}
		return 0;
		
	}

}
