package Player;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.farng.mp3.TagException;


public class MusicLibrary {
	private String name;
	private ArrayList<Mp3> mp3List;
	private int nextSongId;
	public MusicLibrary(String listName) {
		name = listName;
		nextSongId = 1;
		mp3List = new ArrayList<Mp3>();
	}

	public void addSong(String filePath) {
		
		mp3List.add(new Mp3(filePath, nextSongId));
		nextSongId++;
	}
	
	public ArrayList<Mp3> getMp3List(){
		return mp3List;
		
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
}
