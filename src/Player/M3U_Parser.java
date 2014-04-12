//package com.odesanmi.m3ufileparser;
package Player;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


/**
 * M3U Parser class.  This is NOT OUR ORIGINAL CODE, but code freely available
 * under the <a href='http://www.eclipse.org/legal/epl-v10.html'>Eclipse Public
 * License 1.0</a> from <a href='https://code.google.com/p/java-m3u-file-parser/'>
 * this project</a>.  (There were no available .jar files and I figured that, so
 * long as we include this information, everything should be O.K.  Also, there
 * was absolutely no javadoc included in this thing, so all that you see, so far
 * as those type of comments, is stuff I've added.)
 * 
 * This class only handles the initial import of m3u playlists, and not export.
 * If we're going to handle that too, we're going to need to extend this code,
 * but, for now, we've got something to work with.  (I looked into other
 * options, but most of them supported far too much and required adding upwards
 * of 15-million extra dependencies to our code.)
 * @author <a href='https://code.google.com/u/roomtek/'>roomtek</a>
 */
public class M3U_Parser {

	/**
	 * Basic constructor, pretty much empty.
	 * @throws Exception
	 */
	public M3U_Parser() throws Exception {

	}

	/**
	 * Converts input stream to string.
	 * @param is Input stream.
	 * @return String version of input stream.
	 */
	public String convertStreamToString(java.io.InputStream is) {
		try {
			return new java.util.Scanner(is).useDelimiter("\\A").next();
		} catch (java.util.NoSuchElementException e) {
			return "";
		}
	}

	/**
	 * Parses input m3u file into an M3UHolder object.
	 * @param f Input file.
	 * @return M3UHolder object.
	 * @throws FileNotFoundException
	 */
	public M3UHolder parseFile(File f) throws FileNotFoundException {
		if (f.exists()) {
			String stream = convertStreamToString(new FileInputStream(f));
			stream = stream.replaceAll("#EXTM3U", "").trim();
			String[] arr = stream.split("#EXTINF.*,");
			String urls = "", data = "";
			// clean
			{
				for (int n = 0; n < arr.length; n++) {
					if (arr[n].contains("http")) {
						String nu = arr[n].substring(arr[n].indexOf("http://"),
								arr[n].indexOf(".mp3") + 4);

						urls = urls.concat(nu);
						data = data.concat(arr[n].replaceAll(nu, "").trim())
								.concat("&&&&");
						urls = urls.concat("####");
					}
				}
			}
			return new M3UHolder(data.split("&&&&"), urls.split("####"));
		}
		return null;
	}

	/**
	 * M3UHolder class, contains all the data related to the m3u playlist being
	 * imported.
	 */
	public class M3UHolder {
		private String[] data, url;

		public M3UHolder(String[] names, String[] urls) {
			this.data = names;
			this.url = urls;
		}

		int getSize() {
			if (url != null)
				return url.length;
			return 0;
		}

		String getName(int n) {
			return data[n];
		}

		String getUrl(int n) {
			return url[n];
		}
	}
}
