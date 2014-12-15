import java.util.concurrent.ForkJoinPool;

public class use_MM_ForkJoin {

    public static void multiply(int workload) {
	Matrix a = new Matrix(1024,1024);
	a.generateMatrix();
	Matrix b = new Matrix(1024,1024);
	b.generateMatrix();
	Matrix c = new Matrix(1024,1024);
	long start = System.currentTimeMillis();
	ForkJoinPool fjPool = new ForkJoinPool();

	fjPool.invoke(new Multiply(a,b,c,0,0,workload,(c.rows*c.columns)));
	long end = System.currentTimeMillis();

	System.out.println(end-start);

	
    }
    public static void doBlockStriped(){
	ForkJoinPool fjPool = new ForkJoinPool();
	Matrix a = new Matrix(1024,1024);
	a.generateMatrix();
	Matrix b = new Matrix(1024,1024);
	b.generateMatrix();
	Matrix c = new Matrix(1024,1024);
	long start = System.currentTimeMillis();
	fjPool.invoke(new Multiply(a,b,c,0,0,8192,(c.rows*c.columns)));
	long end = System.currentTimeMillis();

	System.out.println(end-start);


		
    }
    public static void doBlockStriped2 (){



	Matrix a = new Matrix(1024,1024);
	a.generateMatrix();
	Matrix b = new Matrix(1024,1024);
	b.generateMatrix();
	Matrix c = new Matrix(1024,1024);
	ForkJoinPool fjPool = new ForkJoinPool();
	long start = System.currentTimeMillis();
	fjPool.invoke(new BlockStriped(a,b,c,0));
	long end = System.currentTimeMillis();

	System.out.println(end-start);
    }
	
    public static void doSquare(int workload) {
		

	Matrix a = new Matrix(1024,1024);
	a.generateMatrix();
	Matrix b = new Matrix(1024,1024);
	b.generateMatrix();
	Matrix c = new Matrix(1024,1024);
	ForkJoinPool fjPool = new ForkJoinPool();
	long start = System.currentTimeMillis();
	fjPool.invoke(new Square(a,b,c,workload,c.rows*c.columns));
	long end = System.currentTimeMillis();
	System.out.println(end-start);

	

    }
	
    public static void doSequential() {

	Matrix a = new Matrix(1024,1024);
	a.generateMatrix();
	Matrix b = new Matrix(1024,1024);
	b.generateMatrix();
	Matrix c = new Matrix(1024,1024);
	long start = System.currentTimeMillis();
	int elem = 0;
	for(int i = 0;i < c.rows;i++){
	    for(int j = 0;j < c.columns;j++) {
		int []row = a.getRow(i);
		int []column = b.getColumn(j);
		for(int k = 0;k< a.columns;k++) {
		    elem = elem + row[k]*column[k];
		}
		c.addElement(i, j, elem);
		elem = 0;
	    }
	}
	long end = System.currentTimeMillis();
	System.out.println(end-start);


    }
	
    public static void main(String[] args) {
	String alg = args[0];
	int workload = 0;
	if(args.length == 2) {
	    workload = Integer.parseInt(args[1]);
	}



	switch(alg) {
	case "seq":
	    doSequential();
	    break;
	case "bsRow":
	    doBlockStriped2();
	    break;
	case "bsCol":
	    doBlockStriped();
	    break;
	case "square":
	    if (workload == 0)
		{
		    System.out.println("You also need a workload");
		    break;
		}
	    doSquare(workload);
	    break;
	case "mult":
	    if (workload == 0)
		{
		    System.out.println("You also need a workload");
		    break;
		}
	    multiply(workload);
	    break;
	default: 
	    System.out.println("No option was selected");
	    break;

	}
    }
}
