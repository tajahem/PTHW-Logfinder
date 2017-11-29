package logfinder.tests;

import logfinder.LogFinder;

public class UsageExceptionTests {

	public static final String[] args = {
	};
	
	public static final String[] args2 = {
		"-n"
	};
	
	public static final String[] args3 = {
		"-n2", "apple"
	};
	
	public static final String[] args4 = {
		"-o"
	};
	
	public UsageExceptionTests(){
		System.out.println("Usage Tests");
		LogFinder.main(args);
		LogFinder.main(args2);
		LogFinder.main(args3);
		LogFinder.main(args4);
	}
}
