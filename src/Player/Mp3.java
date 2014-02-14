package Player;
import java.io.File;


public class Mp3 {
	private File file;
	
	private String title;
	private String whateverMetaData;
	
	int id; // id for playlists/queues
	
	public Mp3(String fileLocation) {
		file = new File(fileLocation);
	}
	
	public void parseMetaData() {
		title = "temptest";
	}
	
	public File getFile() {
		return file;
	}
}
