package logfinder.tests;

public abstract class Test {

	boolean result;
	
	public abstract void setup();
	public abstract void test();
	
	public boolean getResult() {
		return result;
	}
	
}
