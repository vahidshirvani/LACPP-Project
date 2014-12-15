import java.util.concurrent.ForkJoinPool;


public class use_MM_ForkJoin {

	public static void main(String[] args) {
		int size = 100000;
		int[] array = new int[size];
		for(int i = 0; i < size; i++)
			array[i] = (int) (Math.random()*100); 
		
		ForkJoinPool fjPool = new ForkJoinPool();
		int sum = fjPool.invoke(new Sum(array,0,array.length));
		System.out.println("The sum is " + sum);
		
		Matrix a =new Matrix(10,10);
		a.generateMatrix();
		Matrix b =new Matrix(10,10);
		b.generateMatrix();
		Matrix c =new Matrix(10,10);
		
		fjPool.invoke(new Multiply(a,b,c));
		c.printMatrix();
	}
}
