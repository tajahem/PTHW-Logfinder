package logfinder.filesystools;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import logfinder.MessageHandler;
import logfinder.evaluator.LogEvaluator;

/**
 * Walks the file tree for log files then evaluates them against the search
 * patterns.
 * 
 * @author tajahem
 *
 */
public class LogVisitor extends SimpleFileVisitor<Path> {

	private List<String> results = new ArrayList<>();

	private List<PathMatcher> logTypes = new ArrayList<>();
	private LogEvaluator evaluator;
	private MessageHandler messager;

	public LogVisitor(List<String> patterns, LogEvaluator evaluator, MessageHandler messager) {
		this.messager = messager;
		for (String pattern : patterns) {
			logTypes.add(FileSystems.getDefault().getPathMatcher(pattern));
		}
		this.evaluator = evaluator;
	}

	public List<String> getResults() {
		return results;
	}

	private boolean hasPattern(Path p) {
		for (PathMatcher match : logTypes) {
			if (match.matches(p)) {
				return true;
			}
		}
		return false;
	}

	private void evalFile(Path file) throws IOException {
		if (evaluator.evaluate(file)) {
			results.add(file.toString());
		}
	}

	@Override
	public FileVisitResult preVisitDirectory(Path file, BasicFileAttributes atr) {
		if (!file.toFile().canRead()) {
			messager.log("Skipping directory:" + file.toString());
			return FileVisitResult.SKIP_SUBTREE;
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
		Path p = file.getFileName();
		if (p != null && hasPattern(p)) {
			try {
				evalFile(file);
			} catch (IOException e) {
				messager.log(e.getMessage());
				return FileVisitResult.CONTINUE;
			}
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) {
		messager.log("Failed to visit:" + file.toString());
		return FileVisitResult.CONTINUE;
	}
}
