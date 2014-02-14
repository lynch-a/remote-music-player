package Player;
import java.util.ArrayList;


public class MusicLibrary {
	private ArrayList<Mp3> mp3List;
	
	MusicLibrary() {
		mp3List = new ArrayList<Mp3>();
	}
	
	private void addSong(Mp3 song) {
		mp3List.add(song);
	}
}
