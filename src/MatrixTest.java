import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MatrixTest {

	static {
		System.loadLibrary("NativeFunctions");
	}
	
	@Test
	public void testMultiplication() {
		Matrix a = new Matrix(2, 4, 2, 4, 5, 1, 7, 1, 0, 20);
		Matrix b = new Matrix(4, 3, 2, 1, 2, 3, 4, 5, 6, 7, 8, 9, 20, 10);
		Matrix r = new Matrix(2, 3, 55, 73, 74, 197, 411, 219);
		System.out.println(a);
		System.out.println(b);
		System.out.println(a.multiply(b));
		System.out.println(r);
		assertTrue(a.multiply(b).equals(r));
	}
	
	@Test
	public void testMultiplicationNative() {
		Matrix a = new Matrix(2, 4, 2, 4, 5, 1, 7, 1, 0, 20);
		Matrix b = new Matrix(4, 3, 2, 1, 2, 3, 4, 5, 6, 7, 8, 9, 20, 10);
		Matrix r = new Matrix(2, 3, 55, 73, 74, 197, 411, 219);
		System.out.println(a);
		System.out.println(b);
		System.out.println(a.multiplyNative(b));
		System.out.println(r);
		assertTrue(a.multiplyNative(b).equals(r));
	}

	@Test
	public void testPower() {
		Matrix m = new Matrix(3,3,1,2,3,4,5,6,7,8,9);
		assertTrue(m.power(0).equals(new Matrix(3)));
		assertTrue(m.power(1).equals(m));
		m = new Matrix(5,5,1,2,3,5);
		assertTrue(m.power(1).equals(m));
		assertTrue(m.power(0).equals(new Matrix(5)));
		m = new Matrix(4);
		assertTrue(m.equals(m.power(33)));
		m = new Matrix(3, 3, 1,2,3,6,5,4,7,8,9);
		Matrix r = new Matrix (3, 3, 28956096, 30968352, 32980608, 55857276, 59738985, 63620694, 101653272, 108717498, 115781724);
		assertTrue(m.power(7).equals(r));
	}
	
	@Test
	public void testPowerNative() {
		Matrix m = new Matrix(3,3,1,2,3,4,5,6,7,8,9);
		assertTrue(m.powerNative(0).equals(new Matrix(3)));
		assertTrue(m.powerNative(1).equals(m));
		m = new Matrix(5,5,1,2,3,5);
		assertTrue(m.powerNative(1).equals(m));
		assertTrue(m.powerNative(0).equals(new Matrix(5)));
		m = new Matrix(4);
		assertTrue(m.equals(m.powerNative(33)));
		m = new Matrix(3, 3, 1,2,3,6,5,4,7,8,9);
		Matrix r = new Matrix (3, 3, 28956096, 30968352, 32980608, 55857276, 59738985, 63620694, 101653272, 108717498, 115781724);
		assertTrue(m.powerNative(7).equals(r));
	}
}
