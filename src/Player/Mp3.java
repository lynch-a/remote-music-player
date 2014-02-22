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

	private String title;
	private String whateverMetaData;
	private String fileLocation;

	int id; // id for playlists/queues

	public Mp3(String filePath) {
		fileLocation = filePath;
	}

	public String[] parseMetaData() throws IOException, TagException, UnsupportedAudioFileException {
		MP3File mp3file = new MP3File(fileLocation);
		if (mp3file!=null && mp3file.hasID3v1Tag()) {
			ID3v1 id3v1Tag = mp3file.getID3v1Tag();
			System.out.println("Title: " + id3v1Tag.getTitle());
			System.out.println("Artist: " + id3v1Tag.getArtist());
			System.out.println("Album: " + id3v1Tag.getAlbum());
			title = id3v1Tag.getTitle();
			String[] mp3Info = {id3v1Tag.getTitle(),id3v1Tag.getArtist(),"3:50", id3v1Tag.getAlbum()};
			return mp3Info;
		}
		return null;
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

	public String getFilePath() {
		return fileLocation;
	}
}
