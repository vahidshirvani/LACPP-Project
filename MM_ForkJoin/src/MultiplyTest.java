import static org.junit.Assert.*;

import org.junit.Test;


public class MultiplyTest {

	@Test
	public void testMultiply() {
		int[] hej = {1,1,1,1,1,1,1,1,1,1};
		int[] tja = {1,1,1,1,1,1,1,1,1,1};
		int[] x = {1,2,3,4};
		int [] y = {1,2,3,4};
		Matrix a;
		Matrix b;
		Matrix c;
		a = new Matrix(10,10,"/Users/Eddie1/Programmering/lacpp/LACPP-Project/MM_ForkJoin/src/test.txt");
		b = new Matrix(10,10,"/Users/Eddie1/Programmering/lacpp/LACPP-Project/MM_ForkJoin/src/test.txt");
		c = new Matrix(10,10);
		Multiply m = new Multiply(a,b,c,0,0,10,(c.rows*c.columns));
		int bye = m.multiply(hej,tja);
		int z = m.multiply(x,y);
		assertTrue(bye == 10);
		assertTrue(z == 30);
	}

	@Test
	public void testDoWorkload() {
		fail("Not yet implemented");
	}

	@Test
	public void testCalcNewi() {
		Matrix a;
		Matrix b;
		Matrix c;
		a = new Matrix(10,10,"/Users/Eddie1/Programmering/lacpp/LACPP-Project/MM_ForkJoin/src/test.txt");
		b = new Matrix(10,10,"/Users/Eddie1/Programmering/lacpp/LACPP-Project/MM_ForkJoin/src/test.txt");
		c = new Matrix(10,10);
		Multiply m = new Multiply(a,b,c,0,0,10,(c.rows*c.columns));
		assertTrue(m.calcNewi()==1);
	}

	@Test
	public void testCalcNewj() {
		fail("Not yet implemented");
	}

}
