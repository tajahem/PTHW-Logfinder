package logfinder.tests;

import logfinder.LogFinder;

public class HelpTest extends Test{

	public final String[] args ={
		"-h"
	};
	
	

	@Override
	public void setup() {
	
	}

	@Override
	public void test(){
		LogFinder.main(args);
	}

	
}
