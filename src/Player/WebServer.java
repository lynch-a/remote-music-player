package Player;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

/**
 * An example of subclassing NanoHTTPD to make a custom HTTP server.
 */
public class WebServer extends NanoHTTPD {
	public WebServer() throws IOException {
		super(8080, new File("."));
	}

	public Response serve( String uri, String method, Properties header, Properties parms, Properties files ) {
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
			return handleUpvote(params);
		} else if (params[0].equals("downvote")) {
			return handleDownvote(params); 
		} else {
			return new NanoHTTPD.Response(HTTP_OK, MIME_HTML, "function not implemented");
		}

		/*
		System.out.println( method + " '" + uri + "' " );
		String msg = "<html><body><h1>Hello server</h1>\n";
		if ( parms.getProperty("username") == null )
			msg +=
			"<form action='?' method='get'>\n" +
					"  <p>Your name: <input type='text' name='username'></p>\n" +
					"</form>\n";
		else
			msg += "<p>Hello, " + parms.getProperty("username") + "!</p>";

		msg += "</body></html>\n";
		return new NanoHTTPD.Response( HTTP_OK, MIME_HTML, msg );
		 */
	}

	public Response handleIndex() {
		MusicLibrary library = MusicPlayerFrame.getCurrentList();
		ArrayList<Mp3> mp3List = library.getMp3List();

		String response = "";

		for (Mp3 song : mp3List) {
			response += song.getTitle() + String.format(" id:%d up:%d down:%d ", song.getSongId(), song.getUpvotes(), song.getDownvotes()) +
					String.format("<a href='/upvote/%d'>Upvote</a> ", song.getSongId()) + 
					String.format("<a href='/downvote/%d'>Downvote</a> ", song.getSongId()) + "<br/>";
		}

		return new NanoHTTPD.Response(HTTP_OK, MIME_HTML, "index page!<br/>" + response);
	}

	public Response handleUpvote(String[] params) {
		try {
			int songId = Integer.parseInt(params[1]);
			System.out.println("Attempting upvote of song id: " + songId);
			MusicPlayerFrame.doUpvote(songId);
		} catch (Exception e) {
			e.printStackTrace();
			return new NanoHTTPD.Response( HTTP_OK, MIME_HTML, "Error - invalid song given");
		}

		return new NanoHTTPD.Response( HTTP_OK, MIME_HTML, "Success");
	}

	public Response handleDownvote(String[] params) {
		try {
			int songId = Integer.parseInt(params[1]);
			System.out.println("Attempting downvote of song id: " + songId);
			MusicPlayerFrame.doDownvote(songId);
		} catch (Exception e) {
			e.printStackTrace();
			return new NanoHTTPD.Response( HTTP_OK, MIME_HTML, "Error - invalid song given");
		}

		return new NanoHTTPD.Response( HTTP_OK, MIME_HTML, "Success");
	}
}