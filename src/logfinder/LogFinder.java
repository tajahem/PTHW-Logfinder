package logfinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import logfinder.evaluator.AndEvaluator;
import logfinder.evaluator.LogEvaluator;
import logfinder.evaluator.OrEvaluator;
import logfinder.filesystools.DirectoryTool;
import logfinder.filesystools.LogTypeTool;
import logfinder.filesystools.LogVisitor;

/**
 * Most of the main class is dedicated to argument processing
 * 
 * @author tajahem
 *
 */
public class LogFinder {

	public static final String VERSION = "1.1.0";

	public static final String HELP_FILE = "help";

	public static final String USAGE_MESSAGE = "LogFinder Usage:  [options]... [patterns]...\n"
			+ "Try  'LogFinder -h or --help' for more information";
	public static final String HELP_ERROR = "Error: Help file missing or corrupt: \n\t"
			+ "check github.com/tajahem/LogFinder for an updated version";

	private LogTypeTool logTypes;
	private DirectoryTool directoryTool;
	private MessageHandler messageHandler = new MessageHandler(false);

	private LogEvaluator evaluator;
	private List<String> exclude = new ArrayList<>();
	private LogVisitor visitor;

	public LogFinder(String[] args) {
		try {
			processArgs(args);
			finishInitilization();
			processLogs();
			displayResults();
		} catch (UsageException e) {
			e.getMessage();
		}
	}

	private void processArgs(String[] args) throws UsageException {
		// easier to work with a queue than try to keep a counter
		Queue<String> arghs = new LinkedList<>(); // pirate queue
		arghs.addAll(Arrays.asList(args));

		while (!arghs.isEmpty()) {
			String argument = arghs.poll();
			if (argument.startsWith("-")) {
				processOption(argument, arghs);
			} else {
				processPatterns(argument, arghs);
			}
		}
	}

	private void processOption(String argument, Queue<String> arghs) throws UsageException {
		switch (argument) {
		case "-d":
		case "--directory":
			directoryTool = new DirectoryTool(arghs.poll(), messageHandler);
			break;
		case "-l":
		case "--log-file":
			logTypes = new LogTypeTool(arghs.poll());
			break;
		case "-n":
			processExcludePatterns(argument, arghs);
			break;
		default:
			processSingleArgumentOption(argument);
		}
	}

	private void processExcludePatterns(String argument, Queue<String> arghs) throws UsageException {
		try {

			int nTerms = Integer.parseInt(argument.substring(2, argument.length()));

			for (int i = 0; i < nTerms; i++) {
				String term = arghs.poll();
				if (term != null) {
					exclude.add(term);
				} else {
					throw new UsageException("too few arguments expecting " + nTerms);
				}
			}
		} catch (NumberFormatException e) {
			String msg = "The -n option must be followed with a number. ex: -n2";
			throw new UsageException(msg);
		}
	}

	private void processSingleArgumentOption(String argument) throws UsageException {
		for (int i = 1; i < argument.length(); i++) {
			switch (argument.charAt(i)) {
			case 'o':
				evaluator = new OrEvaluator();
				break;
			case 'q':
				messageHandler.suppressMessages();
				break;
			default:
				throw new UsageException("Invalid option -" + argument.charAt(i));
			}
		}
	}

	// assumes any further arguments are search terms
	private void processPatterns(String argument, Queue<String> arghs) {
		if (evaluator == null) {
			evaluator = new AndEvaluator();
		}
		List<String> patterns = new ArrayList<>();
		patterns.add(argument);
		patterns.addAll(arghs);
		evaluator.addPatterns(patterns);
	}

	// null cleanup and LogVisitor construction
	private void finishInitilization() {
		if (directoryTool == null) {
			directoryTool = new DirectoryTool(messageHandler);
		}
		if (logTypes == null) {
			logTypes = new LogTypeTool();
		}
		visitor = new LogVisitor(logTypes.getLogTypes(), evaluator, messageHandler);
	}

	private void processLogs() {
		for (Path p : directoryTool.getSearchDirectories()) {
			try {
				Files.walkFileTree(p, visitor);
			} catch (IOException e) {
				// Should be unlikely
				System.out.println("Critical error while searching " + p.toString());
			}
		}
	}

	private void displayResults() {
		List<String> results = visitor.getResults();
		if (!results.isEmpty()) {
			for (String s : results) {
				System.out.println(s);
			}
		} else {
			messageHandler.writeMessage("no results");
		}
	}

	public static void displayHelpFile() {
		try {
			BufferedReader reader = Files.newBufferedReader(Paths.get("help"));
			String line = reader.readLine();
			while (line != null) {
				System.out.println(line);
				reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			System.out.println(HELP_ERROR);
		}
	}

	// see README or the help file for more information on arguments
	public static void main(String[] args) {
		// Handle options that break normal usage here
		if (args.length == 0) {
			System.out.println(USAGE_MESSAGE);
		} else {
			switch (args[0]) {
			case "-h":
			case "--help":
				displayHelpFile();
				break;
			case "-v":
			case "--version":
				System.out.println("LogFinder version " + VERSION);
				break;
			default:
				new LogFinder(args);
			}
		}
	}
}
