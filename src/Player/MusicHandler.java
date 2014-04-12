package Player;
//import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.LinkedBlockingQueue;

/*import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.filechooser.FileNameExtensionFilter;
*/
import PlayerCommands.PauseCommand;
import PlayerCommands.PlayCommand;
import PlayerCommands.PlayerCommand;
import PlayerCommands.ResumeCommand;
import PlayerCommands.StopCommand;
import javazoom.jl.decoder.JavaLayerException;
//import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.Player;


/**
 * MusicHandler class, this launches and controls a thread that acts as the main
 * music player.
 */
public class MusicHandler implements Runnable {
	
	/**
	 * 
	 */
	enum STATE {
		NOT_STARTED,
		PLAYING,
		PAUSED,
		FINISHED
	}
    /**
     *
     */
	static public LinkedBlockingQueue<PlayerCommand> commands = new LinkedBlockingQueue<PlayerCommand>();
	/**
	 * The actual music player.
	 */
    private Player player;
    /**
     * Locking object used to communicate with player thread.
     */
    private final Object playerLock = new Object();
    /**
     * Status variable describing what the player thread is doing/supposed to
     * do.
    */
    private STATE playerState;

    /**
    * Constructor, sets the playerStatus to NOTSTARTED.
    */
    public MusicHandler(){
    	playerState = STATE.NOT_STARTED;
    }

    /**
    * Constructor, starts the player playing the passed InputStream.
    * @param inputStream InputStream of playable shit.
    * @throws JavaLayerException Oh no!
    */
    public MusicHandler(final InputStream inputStream) throws JavaLayerException {
        this.player = new Player(inputStream);
    }

    /**
    * Starts playback (resumes if paused).
    * @throws JavaLayerException FUUUUUUUUUUUUUU
    */
    public void play() throws JavaLayerException {
        synchronized (playerLock) {
            switch (playerState) {
                case NOT_STARTED:
                    final Runnable r = new Runnable() {
                        public void run() {
                            playInternal();
                            MusicPlayerFrame.playNextSong();
                        }
                    };
                    final Thread t = new Thread(r);
                    t.setDaemon(true);
                    t.setPriority(Thread.MAX_PRIORITY);
                    playerState = STATE.PLAYING;
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
    * @return True if the new state is PAUSED, False if something got boned up.
    */
    public boolean pause() {
        synchronized (playerLock) {
            if (playerState == STATE.PLAYING) {
                playerState = STATE.PAUSED;
            }
            return playerState == STATE.PAUSED;
        }
    }

    /**
     * Resumes playback. Returns true if the new state is PLAYING.
     */
    public boolean resume() {
        synchronized (playerLock) {
            if (playerState == STATE.PAUSED) {
                playerState = STATE.PLAYING;
                playerLock.notifyAll();
            }
            return playerState == STATE.PLAYING;
        }
    }

    /**
     * Stops playback. If not playing, does nothing
     */
    public void stop() {
        synchronized (playerLock) {
            playerState = STATE.FINISHED;
            playerLock.notifyAll();
        }
    }

    private void playInternal() {
        while (playerState != STATE.FINISHED) {
            try {
                if (!player.play(1)) {
                    break;
                }
            } catch (final JavaLayerException e) {
                break;
            }
            // check if paused or terminated
            synchronized (playerLock) {
                while (playerState == STATE.PAUSED) {
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
            playerState = STATE.FINISHED;
        }
        //try {
            //player.close();
        //} catch (final Exception e) {
            // ignore, we are terminating anyway
       // }
    }

    /**
    * Run method for the Thread.
    */
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
						System.out.println(playerState);
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
            playerState = STATE.NOT_STARTED;

            // start playing
            this.play();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

	public STATE getPlayerState() {
		return this.playerState;
	}


}
