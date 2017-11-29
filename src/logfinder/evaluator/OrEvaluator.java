package logfinder.evaluator;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The OrEvaluator evaluates to true if any of the supplied patterns are true,
 * and the antiPattern is not
 * 
 * @author tajahem
 *
 */
public class OrEvaluator extends LogEvaluator {

	private String pattern;
	
	@Override
	public void addPatterns(List<String> patterns) {
		pattern = constructRegex(patterns);
	}
	
	@Override
	public boolean evaluate(Path path) throws IOException {
		boolean found = false;
		BufferedReader reader = getReader(path);
		String line = reader.readLine();
		while (line != null && !found) {
			if (Pattern.matches(antiPattern, line)) {
				break;
			}
			if (Pattern.matches(pattern, line)) {
				found = true;
			}
			reader.readLine();
		}
		reader.close();
		return found;
	}
}
