package logfinder.evaluator;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The AndEvaluator requires that all of the included patterns be present for it
 * to evaluate as a positive match.
 * 
 * @author tajahem
 *
 */
public class AndEvaluator extends LogEvaluator {

	List<String> patterns = new ArrayList<>();
	
	@Override
	public void addPatterns(List<String> patterns) {
		this.patterns = patterns;
	}
	
	@Override
	public boolean evaluate(Path path) throws IOException {

		/*
		 * There is likely a clever way of doing this that doesn't have the memory and
		 * performance hit of a boolean array. However, you would have to be searching
		 * for a lot of terms for it to be noticeable and this is easier to understand
		 * for the moment.
		 */
		boolean[] found = new boolean[patterns.size()];

		BufferedReader reader = getReader(path);
		String line = reader.readLine();
		while (line != null && !areAllFound(found)) {
			if (Pattern.matches(antiPattern, line)) {
				return false;
			}
			for (int n = 0; n < found.length; n++) {
				if (Pattern.matches(patterns.get(n), line)) {
					found[n] = true;
				}
			}
			reader.readLine();
		}
		reader.close();
		return areAllFound(found);
	}

	private boolean areAllFound(boolean[] found) {
		boolean allFound = true;
		int count = 0;
		while (allFound && count < found.length) {
			allFound = found[count];
			++count;
		}
		return allFound;
	}

}
