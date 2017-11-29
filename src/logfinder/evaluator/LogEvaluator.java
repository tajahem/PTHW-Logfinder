package logfinder.evaluator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.GZIPInputStream;


/**
 * The LogEvaluators do most of the heavy lifting when it comes to
 * constructing patterns and evaluating files for matches.
 * 
 * @author tajahem
 *
 */
public abstract class LogEvaluator {

	/*
	 * 	I can guarantee that my code has at least one anti-pattern...
	 *  Seriously if you're reading this and you've got a better name
	 *  for a pattern that negates matches let me know.
	 */
	protected String antiPattern;
	
	public abstract void addPatterns(List<String> patterns);
	public abstract boolean evaluate(Path path) throws IOException;

	public void addExcludePatterns(List<String> exclude) {
		antiPattern = constructRegex(exclude);
	}

	protected String constructRegex(List<String> trms) {
		StringBuilder regexBuilder = new StringBuilder("(");
		regexBuilder.append("(" + trms.get(0) + ")");
		for (int n = 1; n < trms.size(); n++) {
			regexBuilder.append("|(" + trms + ")");
		}
		regexBuilder.append(")");
		return regexBuilder.toString();
	}

	protected BufferedReader getReader(Path path) throws IOException {
		BufferedReader reader;
		if (path.toString().endsWith(".gz")) {
			reader = new BufferedReader(new InputStreamReader(
					new GZIPInputStream(new FileInputStream(path.toString())),
					"UTF-8"));
		} else {
			reader = Files.newBufferedReader(path);
		}
		return reader;
	}
}
