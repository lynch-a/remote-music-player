package Player;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;

/**
 * An example of subclassing NanoHTTPD to make a custom HTTP server.
 */
public class WebServer extends NanoHTTPD {

	private int UPVOTE_DELAY_SECONDS = 7;
	
	public HashMap<String, Long> lastActionTime = new HashMap<String, Long>();

	public WebServer() throws IOException {
		super(8080, new File("."));
	}

	public Response serve( String uri, String method, Properties header, Properties parms, Properties files, Socket source ) {
		System.out.println("URI: " + uri);
		if (uri.length() > 1) {
			if (uri.charAt(0) == '/') {
				uri = uri.substring(1);
			}
		} else {
			return handleIndex();
		}

		String[] params = uri.split("/");
		System.out.println("params: " + Arrays.toString(params));

		if (params[0].equals("upvote")) {
			
			// DEBUG - FOR TESTING
			System.out.println("------------------");
			for (String ip : lastActionTime.keySet()) {
				System.out.println("ip in action map: " + ip);
			}
			System.out.println("------------------");
			
			InetAddress addr = source.getInetAddress();
			String sourceHost = addr.getHostName().toString();

			// do not allow a user to upvote too often
			if (!lastActionTime.containsKey(sourceHost))  {
				lastActionTime.put(sourceHost, new Long(System.currentTimeMillis()));
			} else {

				long lastAction = lastActionTime.get(sourceHost);
				long currentTime = System.currentTimeMillis();

				// this client has sent an upvote within 30 seconds
				if (currentTime - lastAction < (UPVOTE_DELAY_SECONDS * 1000)) {
					return new NanoHTTPD.Response(HTTP_OK, MIME_HTML, "TMA");
				} else {
					lastActionTime.put(sourceHost, new Long(System.currentTimeMillis()));
				}
			}
			return handleUpvote(params);

		} else if (params[0].equals("search")) {
			if (params.length > 1) {
				return handleSearch(params[1].trim());
			}
			else {
				// Cheat for now for when no search results entered, I'll fix later...probably
				return new NanoHTTPD.Response(HTTP_OK, MIME_HTML, getMainPageData());
			}
			
		} else if (params[0].equals("reload")) {  // Returns basic page data when refresh is called
			return new NanoHTTPD.Response(HTTP_OK, MIME_HTML, getMainPageData());
		}
		return handleIndex();

	}

	public Response handleIndex() {

		// Fetch pre-formatted index file.
		String response = "";

		try {
			response = new Scanner(new File("web/index.html")).useDelimiter("\\Z").next();


			String songlist = getMainPageData();

			response = response.replace("$SONG_TABLE", songlist);

		} catch (IOException e) {
			e.printStackTrace();
			return new NanoHTTPD.Response( HTTP_OK, MIME_HTML, "Error - Unable to find index file.");
		}
		
		return new NanoHTTPD.Response(HTTP_OK, MIME_HTML, response);
	}

	public String getMainPageData() {
		MusicLibrary library = MusicPlayerFrame.getLibrary();
		ArrayList<Mp3> mp3List = library.getMp3List();
		
		Mp3 playing = MusicPlayerFrame.getCurrentlyPlaying();

		// Replace placeholder values with dynamic data from the application
		String songlist = "<h1>Currently Playing: " + MusicPlayerFrame.getCurrentlyPlayingTitle() + "</h1>" 
				+ "<table class='gridtable'>" +
				"<tr>" +
				"<th>Title</th>" +
				"<th>Artist</th>" +
				"<th>Votes</th>" +
				"<th></th>" +
				"</tr>";
		for (Mp3 song : mp3List) {
			if(song != playing){
				String[] data = song.getWebData();
				songlist += "<tr>";
				for(int i = 0; i < data.length; i++) {
					if (data[i].length() > 20) {
						data[i] = data[i].substring(0, 17) + "...";
					}
					// Hardcoded upvotecount index for now
					if(i == 2) {
						songlist += "<td>" + String.format("<span class=upvotecount%d>", song.getSongId()) + data[2] + "</span></td>";
					} else {
						songlist += "<td>" + data[i] + "</td>";
					}
				}
				songlist += "<td>" + String.format("<button class='upvote' value=%d>Upvote</button>", song.getSongId()) + "</td>"
						+ "</tr>";
			}
		}
		songlist += "</table>";
		
		return songlist;
	}

	public Response handleSearch(String searchParam) {
		MusicLibrary library = MusicPlayerFrame.getLibrary();
		ArrayList<Mp3> mp3List = library.getMp3List();

		// Fetch pre-formatted index file.
		String response = "";

		Mp3 playing = MusicPlayerFrame.getCurrentlyPlaying();

		// Replace placeholder values with dynamic data from the application
		response = "<h1>Search Results:</h1>" 
				+ "<table class='gridtable'>" +
				"<tr>" +
				"<th>Title</th>" +
				"<th>Artist</th>" +
				"<th>Votes</th>" +
				"<th></th>" +
				"</tr>";
		for (Mp3 song : mp3List) {
			
			if(song != playing){
				String[] data = song.getWebData();
				// only search based on title for now
				System.out.println("Searching on: " + data[0] + "|" +searchParam + "|");
				if(data[0].toLowerCase().contains(searchParam.toLowerCase())) {
					response += "<tr>";
					for(int i = 0; i < data.length; i++) {
						if (data[i].length() > 20) {
							data[i] = data[i].substring(0, 17) + "...";
						}
						// Hardcoded upvotecount index for now
						if(i == 2) {
							response += "<td>" + String.format("<span class=upvotecount%d>", song.getSongId())  + data[2] + "</span></td>";
						} else {
							response += "<td>" + data[i] + "</td>";
						}

					}
					response += "<td>" + String.format("<button class='upvote' value=%d>Upvote</button>", song.getSongId()) + "</td>"
							+ "</tr>";
				}
			}
		}
		response += "</table>";
		
		return new NanoHTTPD.Response(HTTP_OK, MIME_HTML, response);
	}
	
	public Response handleUpvote(String[] params) {
		int upvoteCount;
		try {
			int songId = Integer.parseInt(params[1]);
			System.out.println("Attempting upvote of song id: " + songId);
			upvoteCount = MusicPlayerFrame.doUpvote(songId);
		} catch (Exception e) {
			e.printStackTrace();
			return new NanoHTTPD.Response( HTTP_OK, MIME_HTML, "Error - invalid song given");
		}

		// For now I'm just returning a simple integer value representing the current number of upvotes.  This will be changed to return a xml formatted string.

		return new NanoHTTPD.Response( HTTP_OK, MIME_HTML, getMainPageData());
	}
}
