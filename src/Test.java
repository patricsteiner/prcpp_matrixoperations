
public class Test {
	
	static {
		System.loadLibrary("NativeFunctions");
	}
	
	public static void main(String[] args) {
		Clock clock = new Clock();
		Matrix a = new Matrix(500, 6000);
		Matrix b = new Matrix(6000, 400);
		clock.start();
		Matrix r1 = a.multiply(b);
		clock.stop();
		System.out.println("Multiplication in Java took " + clock);
		clock.start();
		Matrix r2 = a.multiplyNative(b);
		clock.stop();
		System.out.println("Multiplication in C++ took  " + clock);
		assert r1.equals(r2);
		
		Matrix m = new Matrix(250, 250);
		clock.start();
		Matrix r3 = m.power(91);
		clock.stop();
		System.out.println("Power in Java took " + clock);
		clock.start();
		Matrix r4 = m.powerNative(91);
		clock.stop();
		System.out.println("Power in C++ took " + clock);
		assert r3.equals(r4);
	}
	
	/* Typical Output (on Intel Core i7-3630QM CPU @ 2.4GHz):
		Multiplication in Java took 966 ms
		Multiplication in C++ took  662 ms
		Power in Java took 119 ms
		Power in C++ took 75 ms
		
		After refactoring:
		Multiplication in Java took 969 ms
		Multiplication in C++ took  667 ms
		Power in Java took 120 ms
		Power in C++ took 69 ms --> ~5ms faster on average
	*/

}
