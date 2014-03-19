package Player;
import java.util.ArrayList;
import java.util.Collections;


public class MusicLibrary {
	private String name;
	private ArrayList<Mp3> mp3List;
	private int nextSongId;
	public MusicLibrary(String listName) {
		name = listName;
		nextSongId = 1;
		mp3List = new ArrayList<Mp3>();
	}

	public Mp3 addSong(String filePath) {
		Mp3 mp3 = new Mp3(filePath, nextSongId);
		mp3List.add(mp3);
		nextSongId++;
		return mp3;
	}
	
	public ArrayList<Mp3> getMp3List(){
		return mp3List;
		
	}
	
	// -1 if not found
	public int getMp3Row(Mp3 needle) {
		int i = 0;
		for (Mp3 haystack : mp3List) {
			if (haystack == needle) {
				return i;
			}
			i++;
		}
		return -1;
	}
	
	public Mp3 getMp3ByPlaylistId(int id) {
		return mp3List.get(id-1);
	}
	
	public Object[][] getSongListInfo() {
		if(mp3List.size() == 0)
			return null;
		
		Object[][] data = new Object[mp3List.size()][];
		for (int i = 0; i < mp3List.size(); i++){
			data[i] = mp3List.get(i).parseMetaData();
		}
		
		return data;
	}
	
	public String getName(){
		return name;
	}
	
	public void sortPlaylist() {
		Collections.sort(mp3List);
		
	}
}
