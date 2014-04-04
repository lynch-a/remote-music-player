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
			InetAddress addr = source.getInetAddress();
			String sourceHost = addr.getHostName().toString();

			// do not allow a user to upvote too often
			if (!lastActionTime.containsKey(sourceHost))  {
				lastActionTime.put(sourceHost, new Long(System.currentTimeMillis()));
			} else {

				long lastAction = lastActionTime.get(sourceHost);
				long currentTime = System.currentTimeMillis();

				// this client has sent an upvote within 30 seconds
				if (currentTime - lastAction < (1 * 1000 * 30)) {
					return new NanoHTTPD.Response(HTTP_OK, MIME_HTML, "TMA");
				} else {
					lastActionTime.put(sourceHost, new Long(System.currentTimeMillis()));
				}
			}
			handleUpvote(params);
			
			return handleIndex();
		} else {
			return new NanoHTTPD.Response(HTTP_OK, MIME_HTML, "function not implemented");
		}
	}

	public Response handleIndex() {
		MusicLibrary library = MusicPlayerFrame.getLibrary();
		ArrayList<Mp3> mp3List = library.getMp3List();

		// Fetch pre-formatted index file.
		String response = "";

		try {
			response = new Scanner(new File("web/index.html")).useDelimiter("\\Z").next();
			Mp3 playing = MusicPlayerFrame.getCurrentlyPlaying();

			// Replace placeholder values with dynamic data from the application
			String songlist = "<h1>Currently Playing:<br/><br/>" + MusicPlayerFrame.getCurrentlyPlayingTitle() + "</h1>" 
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
						songlist += "<td>" + data[i] + "</td>";
					}
					songlist += "<td>" + String.format("<button class='upvote' value=%d>Upvote</button>", song.getSongId()) + "</td>"
							+ "</tr>";
				}
			}
			songlist += "</table>";

			response = response.replace("$SONG_TABLE", songlist);

		} catch (IOException e) {
			e.printStackTrace();
			return new NanoHTTPD.Response( HTTP_OK, MIME_HTML, "Error - Unable to find index file.");
		}
		
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

		// For now I'm just returning a simple integer value represnntig the current number of upvotes.  This will be changed to return a xml formatted string.

		return new NanoHTTPD.Response( HTTP_OK, MIME_HTML, Integer.toString(upvoteCount));
	}
}