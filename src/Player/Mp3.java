package Player;
import java.io.File;
import java.io.IOException;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.ID3v1;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
//import javax.media.*;


/*
 * Mp3 container class, this fetches and makes easily accessible all the
 *  metadata contained in the Mp3 or, if not present, sets default properties.
 * 
 */
public class Mp3 implements Comparable<Mp3> {
	private File file;
	private String title;
	private String artist;
	private String album;
	private String fileLocation;
	private int songId;
	double duration;
	
	private int upvoteCount = 0;
	private int downvoteCount = 0;
	
	
	/*
	 * Constructor for the Mp3 class.
	 * 
	 * INPUT: a filePath string and a unique integer identifier.
	 * 
	 * OUTPUT: it's a constructor, so nope.
	 * 
	 */
	public Mp3(String filePath, int id) {
		file = new File(filePath);
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
	
	public void addUpvote() {
		upvoteCount++;
	}
	
	public void addDownvote() {
		downvoteCount++;
	}

	public int getUpvotes() {
		return upvoteCount;
	}
	
	public int getDownvotes() {
		return downvoteCount;
	}
	
	public String getTitle() {
		return title;
	}
	/*
	 * Retrieves metadata from the Mp3 referenced by the fileLocation variable,
	 *  sets to default values if there's nothing there.
	 * 
	 * INPUT: none.
	 * 
	 * OUTPUT: none.
	 */
	private void setMetaData() throws IOException, TagException {
		title = null;
		artist = null;
		album = null;
		MP3File mp3file = new MP3File(fileLocation);
		if (mp3file!=null && mp3file.hasID3v1Tag()) {
			ID3v1 id3v1Tag = mp3file.getID3v1Tag();
			title = id3v1Tag.getTitle();
			if (title.equals("")) {
				title = file.getName().replace(".mp3", "");
			}
			artist = id3v1Tag.getArtist();
			album = id3v1Tag.getAlbum();
		}
	}

	
	/*
	 * Parses the Mp3's metadata for use by the rest of the program into a
	 *  string list.
	 * 
	 * INPUT: none.
	 * 
	 * OUTPUT: a string list consisting of the songId, title, artist, song
	 *  length, and album title.
	 * 
	 */
	public String[] parseMetaData(){
		String[] mp3Info = {Integer.toString(songId), title, artist, getTime(), album, ""+getUpvotes()};
		return mp3Info;

	}

	public String[] getWebData(){
		String[] mp3Info = {title, artist,""+getUpvotes()};
		return mp3Info;
	}
	
	/*
	 * Returns the songId in string form.
	 * 
	 * INPUT: none.
	 * 
	 * OUTPUT: this Mp3's songId, as a string.
	 * 
	 */
	public String getIdString(){
		return Integer.toString(songId);
	}

	
	/*
	 * Returns a file pointer referenced by this Mp3's fileLocation.  If not
	 *  present, returns a NULL pointer.
	 * 
	 * INPUT: none.
	 * 
	 * OUTPUT: a new file pointer to the Mp3 (or a NULL pointer).
	 * 
	 */
	public File getFile() {
		return new File(fileLocation);
	}

	
	/*
	 * Retrieves the song duration from the file and returns a string
	 *  representation of it while setting the Mp3.duration variable.  If the
	 *  file fails to open, sets duration to 0.
	 * 
	 * INPUT: none.
	 * 
	 * OUTPUT: string representation of song duration in the format of MM:SS.
	 * 
	 */
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
	
	
	/*
	 * Returns the songId variable.
	 * 
	 * INPUT: none.
	 * 
	 * OUTPUT: none.
	 * 
	 */
	public int getSongId(){
		return songId;
	}

	
	/*
	 * Returns the fileLocation, in string format.
	 * 
	 * INPUT: none.
	 * 
	 * OUTPUT: this Mp3's fileLocation.
	 */
	public String getFilePath() {
		return fileLocation;
	}

	@Override
	public int compareTo(Mp3 other) {
		// arbitrarily complicated scoring algorithm
		int thisDelta = this.getUpvotes() - this.getDownvotes();
		int otherDelta = other.getUpvotes() - other.getDownvotes();
		if (thisDelta > otherDelta) {
			return 1;
		} else {
			return -1;
		}
	}

	public void resetUpvoteCount() {
		upvoteCount = 0;
		
	}
}
