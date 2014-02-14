package Player;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.LinkedBlockingQueue;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import PlayerCommands.PlayCommand;
import PlayerCommands.PlayerCommand;
import PlayerCommands.StopCommand;

class MusicHandler implements Runnable {
	private Player player;
	static public LinkedBlockingQueue<PlayerCommand> commands = new LinkedBlockingQueue<PlayerCommand>();

	private Playlist playlist;

	public void playSong(Mp3 song) throws FileNotFoundException, JavaLayerException {
		if (player != null) {
			player.close();
		}

		FileInputStream fis     = new FileInputStream(song.getFile());
		BufferedInputStream bis = new BufferedInputStream(fis);

		player = new Player(bis);
		player.play();
	}

	public void stopSong() {
		if (player != null) {
			player.close();
		}

		player = null;
	}

	public void run() {
		while (true) {
			if (commands.size() > 0) {
				Object command = commands.poll();

				// TODO: pause functionality
				if (command instanceof PlayCommand) {
					PlayCommand playCommand = (PlayCommand) command;
					try {
						playSong(playCommand.getFileToPlay());
					} catch(Exception e) {
						// file not found or otherwise unable to play? handle me here.
						stopSong();
						e.printStackTrace();
					}
				} else if (command instanceof StopCommand) {
					stopSong();
				} 
			}

			try{ Thread.sleep(50); } catch (Exception e) {}
		}
	}
}