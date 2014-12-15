import java.util.concurrent.RecursiveTask;

public class BlockStripedC extends RecursiveTask <Integer> {
		Matrix a;
		Matrix b;
		Matrix c;
		int i;
		
		
	public BlockStripedC(Matrix a,Matrix b,Matrix c,int i){
		this.a = a;
		this.b = b;
		this.c = c;
		this.i = i;
		
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
	void doWork() 
	{
		for(int j = 0;j < c.columns;j++){
			c.addElement(j,i,multiply(a.getRow(i),b.getColumn(j)));
		}
	}
	@Override
	protected Integer compute() {
		if(i == c.rows-1){
			doWork();
		} else {
			BlockStripedC task = new BlockStripedC(a,b,c,i+1);
			task.fork();
			doWork();
			task.join();
		}
		return 0;
	}

}
