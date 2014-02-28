package Player;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;

import javax.media.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.ID3v1;
import org.jaudiotagger.audio.*;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;


public class Mp3 {
	private File file;
	private String title;
	private String artist;
	private String album;
	private String fileLocation;
	private int songId;
	double duration;
	int id; // id for playlists/queues

	public Mp3(String filePath, int id) {
		fileLocation = filePath;
		try {
			setMetaData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		songId = id;
	}

	private void setMetaData() throws IOException, TagException {
		title = null;
		artist = null;
		album = null;
		MP3File mp3file = new MP3File(fileLocation);
		if (mp3file!=null && mp3file.hasID3v1Tag()) {
			ID3v1 id3v1Tag = mp3file.getID3v1Tag();
			title = id3v1Tag.getTitle();
			artist = id3v1Tag.getArtist();
			album = id3v1Tag.getAlbum();
		}
	}

	public String[] parseMetaData(){
		String[] mp3Info = {Integer.toString(songId), title, artist, getTime(), album};
		return mp3Info;

	}

	public String getIdString(){
		return Integer.toString(songId);
	}

	public File getFile() {
		return new File(fileLocation);
	}

	private String getTime(){
		duration = 0;
		try {
			
			MP3AudioHeader au= (MP3AudioHeader) AudioFileIO.read(new File(fileLocation)).getAudioHeader();
			
			duration = au.getPreciseTrackLength();
			
		} catch (Exception e) {
			//e.printStackTrace();

		}
		
		int minutes = (int) Math.floor(duration/60);
		int seconds = (int) Math.floor(duration%60);
		
		if(seconds < 10)
			return new String( Integer.toString(minutes) + ":0" + Integer.toString(seconds));
		else
			return new String( Integer.toString(minutes) + ":" + Integer.toString(seconds));
	}

	public int getSongId(){
		return songId;
	}

	public String getFilePath() {
		return fileLocation;
	}
}
