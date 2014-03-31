package Player;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.filechooser.FileNameExtensionFilter;

import PlayerCommands.PauseCommand;
import PlayerCommands.PlayCommand;
import PlayerCommands.PlayerCommand;
import PlayerCommands.ResumeCommand;
import PlayerCommands.StopCommand;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.Player;


public class MusicHandler implements Runnable {

	    private final static int NOTSTARTED = 0;
	    private final static int PLAYING = 1;
	    private final static int PAUSED = 2;
	    private final static int FINISHED = 3;
		static public LinkedBlockingQueue<PlayerCommand> commands = new LinkedBlockingQueue<PlayerCommand>();

	    // the player actually doing all the work
	    private Player player;

	    // locking object used to communicate with player thread
	    private final Object playerLock = new Object();

	    // status variable what player thread is doing/supposed to do
	    private int playerStatus;

	    public MusicHandler(){
	    	playerStatus = NOTSTARTED;
	    }
	    
	    public MusicHandler(final InputStream inputStream) throws JavaLayerException {
	        this.player = new Player(inputStream);
	    }
	    /*
	    public AudioHandler(final InputStream inputStream, final AudioDevice audioDevice) throws JavaLayerException {
	        this.player = new Player(inputStream, audioDevice);
	    }*/

	    /**
	     * Starts playback (resumes if paused)
	     */
	    public void play() throws JavaLayerException {
	        synchronized (playerLock) {
	            switch (playerStatus) {
	                case NOTSTARTED:
	                    final Runnable r = new Runnable() {
	                        public void run() {
	                            playInternal();
	                            MusicPlayerFrame.playNextSong();
	                        }
	                    };
	                    final Thread t = new Thread(r);
	                    t.setDaemon(true);
	                    t.setPriority(Thread.MAX_PRIORITY);
	                    playerStatus = PLAYING;
	                    t.start();
	                    break;
	                case PAUSED:
	                    resume();
	                    break;
	                default:
	                    break;
	            }
	        }
	    }

	    /**
	     * Pauses playback. Returns true if new state is PAUSED.
	     */
	    public boolean pause() {
	        synchronized (playerLock) {
	            if (playerStatus == PLAYING) {
	                playerStatus = PAUSED;
	            }
	            return playerStatus == PAUSED;
	        }
	    }

	    /**
	     * Resumes playback. Returns true if the new state is PLAYING.
	     */
	    public boolean resume() {
	        synchronized (playerLock) {
	            if (playerStatus == PAUSED) {
	                playerStatus = PLAYING;
	                playerLock.notifyAll();
	            }
	            return playerStatus == PLAYING;
	        }
	    }

	    /**
	     * Stops playback. If not playing, does nothing
	     */
	    public void stop() {
	        synchronized (playerLock) {
	            playerStatus = FINISHED;
	            playerLock.notifyAll();
	        }
	    }

	    private void playInternal() {
	        while (playerStatus != FINISHED) {
	            try {
	                if (!player.play(1)) {
	                    break;
	                }
	            } catch (final JavaLayerException e) {
	                break;
	            }
	            // check if paused or terminated
	            synchronized (playerLock) {
	                while (playerStatus == PAUSED) {
	                    try {
	                        playerLock.wait();
	                    } catch (final InterruptedException e) {
	                        // terminate player
	                        break;
	                    }
	                }
	            }
	        }
	        close();
	    }

	    /**
	     * Closes the player, regardless of current state.
	     */
	    public void close() {
	        synchronized (playerLock) {
	            playerStatus = FINISHED;
	        }
	        //try {
	            //player.close();
	        //} catch (final Exception e) {
	            // ignore, we are terminating anyway
	       // }
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
							this.playSong(playCommand.getFileToPlay());
							System.out.println(playerStatus);
						} catch(Exception e) {
							// file not found or otherwise unable to play? handle me here.
							e.printStackTrace();
						}
					}
					else if (command instanceof PauseCommand)
						this.pause();
					else if(command instanceof ResumeCommand)
						this.resume();
					else if(command instanceof StopCommand){
						this.stop();
					}
					
				}
				
			}
		}
	    // demo how to use
	    public void playSong(Mp3 song) {
	        try {
	            FileInputStream input = new FileInputStream(song.getFilePath()); 
	            this.player = new Player(input);
	            playerStatus = NOTSTARTED;

	            // start playing
	            this.play();
	        } catch (final Exception e) {
	            throw new RuntimeException(e);
	        }
	    }

		public int getPlayerStatus() {
			return this.playerStatus;
		}


}
