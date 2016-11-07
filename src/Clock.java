
public class Clock {

	private long start, stop;
	
	public Clock(){
		start();
	}

	public void start() {
		this.start = System.nanoTime();
	}
	
	public void stop() {
		this.stop = System.nanoTime();
	}
	
	private long getNanoSecs() {
		return stop - start;
	}

	public int getMilliSecs() {
		return (int) (getNanoSecs()/1000000);
	}
	
	@Override
	public String toString(){
		return getMilliSecs() + " ms";
	}

}
