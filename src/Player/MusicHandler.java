package Player;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.LinkedBlockingQueue;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.*;
import PlayerCommands.PlayCommand;
import PlayerCommands.PlayerCommand;
import PlayerCommands.StopCommand;

class MusicHandler implements Runnable {
	private static AdvancedPlayer player;
	static public LinkedBlockingQueue<PlayerCommand> commands = new LinkedBlockingQueue<PlayerCommand>();

	Thread playingThread;

	private Playlist playlist;

	public void playSong(Mp3 song) throws FileNotFoundException, JavaLayerException {
		if (player != null) {
			player.close();
		}

		FileInputStream fis     = new FileInputStream(song.getFile());
		BufferedInputStream bis = new BufferedInputStream(fis);

		player = new AdvancedPlayer(bis);
		player.play();
	}

	public void stopSong() {
		System.out.println("stopSong hit");
		playingThread.stop();
		
		if (player != null) {
			player.close();
		}

		player = null;
	}

	public void run() {
		while (true) {
			//System.out.println(commands.size());
			if (commands.size() > 0) {
				Object command = commands.poll();

				// TODO: pause functionality
				if (command instanceof PlayCommand) {
					try {
						final PlayCommand playCommand = (PlayCommand) command;

						playingThread = new Thread() {
							public void run() {
								try {
									playSong(playCommand.getFileToPlay());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						};

						playingThread.start();
						
					} catch(Exception e) {
						// file not found or otherwise unable to play? handle me here.
						stopSong();
						e.printStackTrace();
					}
				} else if (command instanceof StopCommand) {
					System.out.println("stopping playing");
					stopSong();
				} 
			}

			//try{ Thread.sleep(50); } catch (Exception e) {}
		}
	}

	public static boolean isPlaying() {
		System.out.println("playing: " + player!=null);
		return player != null;
	}
}