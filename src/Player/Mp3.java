package Player;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.ID3v1;


public class Mp3 {
	private File file;
	private int upvotes;
	private String title;
	private String artist;
	private String album;
	private String fileLocation;
	private int songId;
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
		upvotes = 0;
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
		String[] mp3Info = {Integer.toString(songId), title, artist, "3:50", album};
		return mp3Info;

	}

	public String getIdString(){
		return Integer.toString(songId);
	}
	
	public File getFile() {
		return new File(fileLocation);
	}
	
	private String getTime() throws UnsupportedAudioFileException, IOException{
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getFile());
		AudioFormat format = audioInputStream.getFormat();
		long frames = audioInputStream.getFrameLength();
		double seconds = (frames+0.0) / format.getFrameRate();
		int minutes = (int)(seconds/60);
		seconds %= 60;
		return new String(minutes + ":" + seconds);
	}
	
	public int getSongId(){
		return songId;
	}

	public String getFilePath() {
		return fileLocation;
	}
}
