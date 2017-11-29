package logfinder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * The purpose of the MessageHandler is to allow for simultaneous stndout and
 * logging of messages
 * 
 * @author tajahem
 *
 */
public class MessageHandler {

	public static final String LOG_PATH = "/var/log/logfinder";

	private boolean suppress;
	private List<String> messages = new ArrayList<>();

	public MessageHandler(boolean suppress) {
		this.suppress = suppress;
	}

	/**
	 * Sets messages to log only
	 */
	public void suppressMessages() {
		suppress = false;
	}

	// likely not needed
	public void toggleSuppressMessages() {
		suppress = !suppress;
	}

	/**
	 * If the suppress toggle is **not** set then log and write the message to
	 * stndout otherwise just log it
	 * 
	 * @param message
	 */
	public void writeMessage(String message) {
		if (!suppress) {
			System.out.println(message);
		}
		messages.add(message);
	}

	/**
	 * Log the specified message without sending it to stndout regardless of the
	 * suppress status
	 * 
	 * @param message
	 */
	public void log(String message) {
		messages.add(message);
	}

	/**
	 * Write all remaining messages to the log file located in the path provided in
	 * the LOG_PATH variable. Should be called before program exits.
	 */
	public void close() {
		if (!messages.isEmpty()) {
			Path logPath = Paths.get(LOG_PATH);
			try {
				BufferedWriter writer = Files.newBufferedWriter(logPath);
				for (String s : messages) {
					writer.write(s);
				}
				writer.flush();
				writer.close();
			} catch (IOException e) {
				System.err.println("ERROR: log failed to write");
			}
		}
	}
}
