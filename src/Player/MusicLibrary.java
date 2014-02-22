package Player;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.farng.mp3.TagException;


public class MusicLibrary {
	private String name;
	private ArrayList<Mp3> mp3List;
	
	public MusicLibrary(String listName) {
		name = listName;
		mp3List = new ArrayList<Mp3>();
	}

	public void addSong(String filePath) {
		
		mp3List.add(new Mp3(filePath));
	}
	public Object[][] getSongListInfo() throws IOException, TagException, UnsupportedAudioFileException{
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
