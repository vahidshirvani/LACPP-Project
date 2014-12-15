import java.util.concurrent.RecursiveAction;



public class BlockStriped extends RecursiveAction{
	static final int SEQUENTIAL_THRESHOLD = 100;
	Matrix a;
	Matrix b;
	Matrix c;
	int n;
		
	public BlockStriped(Matrix a,Matrix b,Matrix c){
		this.a = a;
		this.b = b;
		this.c =c;
		this.n = 0;

	}
	
	public BlockStriped(Matrix a,Matrix b,Matrix c,int n){
		this.a = a;
		this.b = b;
		this.c =c;
		this.n = n;
	}
	

	public void doWorkload(){
		int element = 0;
		int []aRow = a.getRow(n);
		int []bRow;
		int [] cRow = c.getRow(n);
		for(int nRow = 0;nRow<b.rows;nRow++){
			bRow = b.getRow(nRow);
			for(int counter = 0;counter < c.columns;counter++){
				element = cRow[counter];
				element = element + (aRow[nRow] * bRow[counter]);
				c.addElement(n, counter, element);
			}	
		}
	}
	

	@Override
	protected void compute() {
		if(n == c.rows-1) {
			doWorkload();
		} else  {
			BlockStriped task = new BlockStriped(a,b,c,n+1);
			task.fork();
			doWorkload();
			task.join();
		}
 	}

}

