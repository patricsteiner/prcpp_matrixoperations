import java.util.Arrays;

public class Matrix {
	double[] values;
	final int h, w;
	
	public Matrix(Matrix m) {
		h = m.h;
		w = m.w;
		values = Arrays.copyOf(m.values, m.values.length);
	}
	
	/**
	 * Create new matrix with given initial values.
	 */
	public Matrix(int h, int w, double... init) {
		this.h = h;
		this.w = w;
		values = new double[h*w];
		for (int i = 0; i < init.length && i < values.length; i++) {
			values[i] = init[i];
		}
	}
	
	/**
	 * Create new matrix filled with random values between 0 inclusive and 1 exclusive.
	 */
	public Matrix(int h, int w) {
		this.h = h;
		this.w = w;
		values = new double[h*w];
		for (int i = 0; i < values.length; i++) {
			values[i] = Math.random();
		}
	}
	
	/**
	 * Create new matrix with given initial value.
	 */
	public Matrix(int h, int w, double init) {
		this.h = h;
		this.w = w;
		values = new double[h*w];
		if (init != 0) { // default value is 0, no need to initialize
			for (int i = 0; i < values.length; i++) {
				values[i] = init;
			}
		}
	}
	
	/**
	 * create new identity matrix with size s.
	 * @param s
	 */
	public Matrix(int s) {
		this.h = s;
		this.w = s;
		values = new double[s*s];
		for (int i = 0; i < values.length; i += s+1) {
			values[i] = 1;
		}
	}
	
	public void fill(double val) {
		for (int i = 0; i < values.length; i++) {
			values[i] = val;
		}
	}
	
	private void swapValues(Matrix m) {
		double[] tmp = values;
		values = m.values;
		m.values = tmp;
	}
	
	public Matrix multiply(Matrix m) {
		assert w == m.h;
		Matrix r = new Matrix(h, m.w, 0);
		multiply(values, m.values, r.values, h, w, m.w);
		return r;
	}
	
	public Matrix multiplyNative(Matrix m) {
		assert w == m.h;
		Matrix r = new Matrix(h, m.w, 0);
		multiplyC(values, m.values, r.values, h, w, m.w);
		return r;
	}
	
	/**
	 * Calculates the k-th power of a matrix using square and multiply algorithm.
	 * @param k power
	 * @return resulting matrix
	 */
	public Matrix power(int k) {
		assert k >= 0 && h == w;
		Matrix res = new Matrix(h); // identity matrix
		if (k == 0) return res;
		Matrix m = new Matrix(this); // the matrix to be powered
		if (k == 1) return m;
		Matrix r = new Matrix(h, w, 0); // 0 matrix
		while (k > 0) {
			if ((k & 1) == 1) {
				r.fill(0);
				multiply(res.values, m.values, r.values, h, w, w);
				res.swapValues(r);
			}
			if (k > 1) { // make sure not to square when the result is already set
				r.fill(0);
				multiply(m.values, m.values, r.values, h, w, w);
				m.swapValues(r);
			}
			k >>= 1;
		}
		return res;
	}
	
	/**
	 * Calculates the k-th power of a matrix using a C++ native function.
	 */
	public Matrix powerNative(int k) {
		assert k >= 0 && h == w;
		if (k == 0) return new Matrix(h);
		if (k == 1) return new Matrix(this);
		Matrix res = new Matrix(h, w, 0);
		powerC(this.values, res.values, h, k);
		return res;
	}
	
	/**
	 * 
	 * @param a values from matrix a (not changed)
	 * @param b values from matrix b (not changed)
	 * @param r empty array (all values 0), will be filled with multiplication result
	 * @param ah height of matrix a
	 * @param aw width of matrix a
	 * @param bw width of matrix b
	 */
	private void multiply(double[] a, double[] b, double[] r, int ah, int aw, int bw) {
		int ia = 0, ir = 0; // index on a and r
		for (int i = 0; i < ah; i++) {
			int ib = 0; // index on b
			for (int j = 0; j < aw; j++) {
				double tmp = a[ia++]; // save result in tmp to reduce array access (every value is only accessed once)
				for (int k = 0; k < bw; k++) {
					r[ir++] += tmp * b[ib++]; // add intermediate result to right place in result matrix
				}
				ir -= bw; // jump back to the start of the row (we're not done adding the intermediate results)
			}
			ir += bw; // we're done with the row, go to next row
		}
	}
	
	private native void multiplyC(double[] a, double[] b, double[] r, int ah, int aw, int bw);
	
	private native void powerC(double m[], double[] r, int s, int k);
	
	public boolean equals(Matrix m) {
		if (this == m) return true;
		if (h != m.h || w != m.w || values.length != m.values.length) return false;
		for (int i = 0; i < values.length; i++) {
			if (values[i] != m.values[i]) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < values.length; i++) {
			sb.append(values[i]);
			if ((i + 1) % w == 0) {
				sb.append("\n");
			} else {
				sb.append("   ");
			}
		}
		return sb.toString();
	}
}
