package Player;
import java.io.File;


public class Mp3 {
	private File file;
	
	private String title;
	private String whateverMetaData;
	private String fileLocation;
	
	int id; // id for playlists/queues
	
	public Mp3(String filePath) {
		fileLocation = filePath;
	}
	
	public void parseMetaData() {
		title = "temptest";
	}
	
	public File getFile() {
		return new File(fileLocation);
	}
}
