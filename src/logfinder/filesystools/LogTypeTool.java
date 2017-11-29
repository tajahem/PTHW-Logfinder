package logfinder.filesystools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Reads in a list of log types (provided in /etc/logfinder/logfiles or a
 * command line argument) to search through. The list is retrievable through the
 * getLogTypes() method.
 * 
 * @author tajahem
 *
 */
public class LogTypeTool {

	private final String DEFAULT_FILE = "/etc/logfinder/logfile";

	private List<String> logTypes;

	public LogTypeTool() {
		setupTypes(DEFAULT_FILE);
	}

	public LogTypeTool(String typesFile) {
		setupTypes(typesFile);
	}

	/**
	 * Returns the list of log types to search
	 * 
	 * @return
	 */
	public List<String> getLogTypes() {
		return logTypes;
	}

	private void setupTypes(String typesFile) {
		logTypes = new ArrayList<>();
		try {
			BufferedReader reader = new BufferedReader(
					new FileReader(typesFile));
			String line = reader.readLine();
			while (line != null) {
				if (!line.contains("glob:") && !line.contains("regex:")) {
					logTypes.add("regex:" + line);
				} else {
					logTypes.add(line);
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			System.out.println("Error retireving " + typesFile);
			System.exit(0);
		}
	}
}
