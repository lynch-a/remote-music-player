package Player;
import java.io.File;
import java.io.IOException;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.ID3v1;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;


/***
 * Mp3 container class, this fetches and makes easily accessible all the
 * metadata contained in the Mp3 or, if not present, sets default properties.
 */
public class Mp3 implements Comparable<Mp3> {
	/**
	 * Track title, read from the mp3's metadata.
	 */
	private String title;
	/**
	 * Track artist, read from the mp3's metadata.
	 */
	private String artist;
	/**
	 * Track album, read from the mp3's metadata.
	 */
	private String album;
	/**
	 * String representation of the filepath to the mp3 on disk.
	 */
	private String fileLocation;
	/**
	 * Unique integer identifier to be assigned on creation.
	 */
	private int songId;
	/**
	 * Track duration, in seconds.
	 */
	private double duration;
	/**
	 * String representation of this Mp3's duration, for stringing.
	 */
	private String durStr;
	/**
	 * This track's current upvote count.
	 */
	private int upvoteCount = 0;
	/**
	 * This track's current downvote count.
	 * TODO Remove downvote functionality, not wanted/needed.
	 */
	private int downvoteCount = 0;
	
	
	/**
	 * Constructor for the Mp3 class.  Initializes all metadata and creates a
	 * file pointer to the mp3 who's filepath is passed in arguments.
	 * @param filePath A string representation of the file location.
	 * @param id Unique integer identifier for this Mp3.
	 */
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
		return;
	}
	
	
	/**
	 * Retrieves metadata from the Mp3 referenced by the fileLocation variable,
	 * sets to default values if there's nothing there.
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
				title = clipFileName();
			}
			artist = id3v1Tag.getArtist();
			album = id3v1Tag.getAlbum();
		}
		getTime();
		return;
	}
	
	
	/**
	 * Clips the filename from the fileLocation.  Praise Allah for
	 * stackoverflow.
	 */
	private String clipFileName() {
		java.util.regex.Pattern p       = java.util.regex.Pattern.compile("^[/\\\\]?(?:.+[/\\\\]+?)?(.+?)[/\\\\]?$");
	    java.util.regex.Matcher matcher = p.matcher(fileLocation);

	    if ( matcher.find() ) {
	        return matcher.group(1);
	    }
	    return null;
	}
	
	
	/**
	 * Retrieves the song duration from the file and assigns the duration and
	 * durStr variables accordingly.
	 */
	private void getTime(){
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
			durStr = new String( Integer.toString(minutes) + ":0" + Integer.toString(seconds));
		else
			durStr = new String( Integer.toString(minutes) + ":" + Integer.toString(seconds));
		return;
	}
	
	
	/**
	 * Add an upvote to the upvote count.
	 */
	public void addUpvote() {
		upvoteCount++;
		return;
	}
	
	
	/**
	 * Add a downvote to the downvote count.
	 */
	public void addDownvote() {
		downvoteCount++;
		return;
	}

	
	/**
	 * Returns the current upvote count.
	 * @return Current upvote count.
	 */
	public int getUpvotes() {
		return upvoteCount;
	}
	
	
	/**
	 * Returns the current downvote count.
	 * @return Current downvote count.
	 */
	public int getDownvotes() {
		return downvoteCount;
	}
	
	
	/**
	 * Returns this instance's track title.
	 * @return This instance's track title.
	 */
	public String getTitle() {
		return title;
	}

	
	/**
	 * Parses the Mp3's metadata for use by the rest of the program into a 
	 * string list.
	 * @return A string list consisting of the songId, title, artist, song
	 * length, and album title.
	 */
	public String[] parseMetaData(){
		String[] mp3Info = {Integer.toString(songId), title, artist, durStr, album, ""+getUpvotes()};
		return mp3Info;

	}

	
	/**
	 * Returns this instance's track title.
	 * @return This instance's track title.
	 */
	public String[] getWebData(){
		String[] mp3Info = {title, artist,""+getUpvotes()};
		return mp3Info;
	}
	
	
	/**
	 * Returns this instance's identifier integer, in string form.
	 * @return This instance's songId, in string form.
	 */
	public String getIdString(){
		return Integer.toString(songId);
	}

	
	/**
	 * Returns a file pointer referenced by this Mp3's fileLocation.
	 * @return A file pointer as directed by this Mp3's fileLocation.
	 */
	public File getFile() {
		return new File(fileLocation);
	}

	
	/**
	 * Returns the songId variable.
	 * @return This instance's songId value.
	 */
	public int getSongId(){
		return songId;
	}

	
	/**
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
