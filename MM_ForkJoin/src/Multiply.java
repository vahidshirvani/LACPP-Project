import java.util.concurrent.RecursiveAction;



public class Multiply extends RecursiveAction{
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
			c.addElement(i,j,multiply(a.getRow(i),b.getColumn(j)));
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
		if(workload < c.columns&&(j+workload)>c.columns) {
			tmp = i+1;
			
		} else {
		tmp = i + (workload/c.rows);
		}
		return tmp;	
	}
	public int calcNewj(){
		int tmp;
		if(workload < c.columns){
			tmp =(j + workload) % c.columns;
		} else {
			
			tmp =(workload%c.columns);
			
		}
		return tmp;

	}

	@Override
	protected void compute() {
		if((size)-(i*c.columns + j) <= workload){
			this.workload = (size)-(i*c.columns + j); 
			doWorkload();
			/*} else if(i*c.columns + j >= (size-1)){
			this.workload = (size)-(i*c.rows + j); 
			doWorkload();*/
		}else {
			int newi = calcNewi();
			int newj = calcNewj();
			if(newi * c.columns +  newj >= size-1){
				this.workload = (size) - (i*c.columns+j);
				doWorkload();
			} else {
				Multiply task = new Multiply(a,b,c,newi,newj,workload,size);
				task.fork();
				doWorkload();
				task.join();
			}

		}

	}

}
