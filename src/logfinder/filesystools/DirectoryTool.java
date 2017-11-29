package logfinder.filesystools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import logfinder.MessageHandler;

/**
 * 
 * Reads in a list of directories (supplied in a file called either
 * /etc/logfinder/default_directories, a command line argument, or if both of
 * those options fail it will default to /var/log) to search. The list of
 * directories is retrieved through the getSearchDirectories() method.
 * 
 * @author tajahem
 *
 */
public class DirectoryTool {

	private List<Path> directories = new ArrayList<>();
	private String dirFile = "/etc/logfinder/directories";
	private final MessageHandler messageHandler;

	public DirectoryTool(MessageHandler messageHandler) {
		this.messageHandler = messageHandler;
		setupSearchDirectories();
	}

	public DirectoryTool(String directory, MessageHandler messageHandler) {
		this.messageHandler = messageHandler;
		directories.add(Paths.get(directory));
	}

	/**
	 * Returns a list of paths containing the root directories to search
	 * 
	 * @return
	 */
	public List<Path> getSearchDirectories() {
		return directories;
	}

	private void setupSearchDirectories() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(dirFile)));
			String line = br.readLine();
			while (line != null) {
				if (isLocation(line)) {
					directories.add(Paths.get(line));
				}
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			String msg = "Error retireving configuration file: defaulting to /var/log";
			messageHandler.log(msg);
			directories.add(Paths.get("/var/log"));
		}
	}

	private boolean isLocation(String line) {
		boolean valid = false;
		if (!line.startsWith("#")) {
			File file = new File(line);
			return (file.exists() && file.isDirectory());
		}
		return valid;
	}

}
