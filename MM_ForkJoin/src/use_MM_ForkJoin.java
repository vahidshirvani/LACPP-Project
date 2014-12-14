import java.util.concurrent.ForkJoinPool;
import java.util.Arrays;

public class use_MM_ForkJoin {
	public static void doBlockStriped(){
		ForkJoinPool fjPool = new ForkJoinPool();
		Matrix a,b,c;
		a = new Matrix(5,5,"/Users/Eddie1/Programmering/lacpp/LACPP-Project/MM_ForkJoin/src/test.txt");
		b = new Matrix(5,5,"/Users/Eddie1/Programmering/lacpp/LACPP-Project/MM_ForkJoin/src/test2.txt");
		c = new Matrix(a.rows,b.columns);

		fjPool.invoke(new Multiply(a,b,c,0,0,5,(c.rows*c.columns)));
		c.printMatrix();
		
	}
	public static void doBlockStriped2 (){
		Matrix a,b,c;
		a = new Matrix(5,5,"/Users/Eddie1/Programmering/lacpp/LACPP-Project/MM_ForkJoin/src/test.txt");
		b = new Matrix(5,5,"/Users/Eddie1/Programmering/lacpp/LACPP-Project/MM_ForkJoin/src/test2.txt");
		c = new Matrix(a.rows,b.columns);
		ForkJoinPool fjPool = new ForkJoinPool();
		fjPool.invoke(new BlockStriped(a,b,c,0));
		c.printMatrix();
	}
	public static void main(String[] args) {
		//doBlockStriped();
		doBlockStriped2();
	}
}
