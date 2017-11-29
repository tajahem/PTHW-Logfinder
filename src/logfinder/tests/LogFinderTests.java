package logfinder.tests;

import java.util.ArrayList;

public class LogFinderTests {

	public static Test testSuite;
	
	class TestSuite extends Test {

		ArrayList<Test> tests = new ArrayList<>();
		
		@Override
		public void setup() {
		}
		
		@Override
		public void test() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	public static void main(String[] args) {
		System.out.println("Beginning Tests...");
		
		new HelpTest();
		new UsageExceptionTests();
		new VersionTest();
		System.out.println("");
	}

}
