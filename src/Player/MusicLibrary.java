package Player;
import java.util.ArrayList;


public class MusicLibrary {
	private ArrayList<Mp3> mp3List;
	
	MusicLibrary() {
		mp3List = new ArrayList<Mp3>();
	}
	
	public void addSong(String filePath) {
		
		mp3List.add(new Mp3(filePath));
	}
}
