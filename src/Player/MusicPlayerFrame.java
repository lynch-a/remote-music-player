package Player;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javazoom.jl.player.Player;
import PlayerCommands.PlayCommand;


public class MusicPlayerFrame extends JFrame {

	private JPanel contentPane;
	private Player player;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		new Thread(new MusicHandler()).start();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MusicPlayerFrame frame = new MusicPlayerFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MusicPlayerFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnPlay = new JButton("play");
		btnPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				playButtonPressed();
			}
		});
		btnPlay.setBounds(155, 168, 117, 29);
		contentPane.add(btnPlay);
	}

	public void playButtonPressed() {

		Mp3 testFile = new Mp3("/Users/alynch/Desktop/test.mp3");
		MusicHandler.commands.add(new PlayCommand(testFile));
	}

}