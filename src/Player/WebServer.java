package Player;
import java.io.*;
import java.util.*;

/**
 * An example of subclassing NanoHTTPD to make a custom HTTP server.
 */
public class WebServer extends NanoHTTPD
{
	public WebServer() throws IOException
	{
		super(8080, new File("."));
	}

	public Response serve( String uri, String method, Properties header, Properties parms, Properties files )
	{
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
		//ArrayList<Mp3> songList = 
		return new NanoHTTPD.Response(HTTP_OK, MIME_HTML, "index page");
	}
	
	public Response handleUpvote(String[] params) {

		return new NanoHTTPD.Response( HTTP_OK, MIME_HTML, "handle upvote!");
	}

	public Response handleDownvote() {
		return new NanoHTTPD.Response( HTTP_OK, MIME_HTML, "handle downvote!");
	}


	public static void main( String[] args )
	{
		try
		{
			new WebServer();
		}
		catch( IOException ioe )
		{
			System.err.println( "Couldn't start server:\n" + ioe );
			System.exit( -1 );
		}
		System.out.println( "Listening on port 8080. Hit Enter to stop.\n" );
		try { System.in.read(); } catch( Throwable t ) {};
	}
}