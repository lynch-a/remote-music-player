package Player;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import javazoom.jl.player.Player;
import Player.MusicHandler.STATE;
import PlayerCommands.PauseCommand;
import PlayerCommands.PlayCommand;
import PlayerCommands.ResumeCommand;
import PlayerCommands.StopCommand;


public class MusicPlayerFrame extends JFrame {
	private static JPanel contentPane;
	private static Player player;
	private static MusicLibrary library;
	private static JTable currentPlaylistTable;
	private JTextField lblWebAddress;
	private static JLabel lblSongTitle;
	private String[] PlayListColumnNames = new String[] {"ID", "Track", "Artist", "Time", "Album", "Votes"};
	private static MusicHandler handler = new MusicHandler();
	private static Mp3 currentlyPlaying;
	private static JButton btnPlay;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		// spin up web server
		new Thread() {
			public void run() {
				try {
					new WebServer();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
		new Thread(handler).start(); // start thread that handles commands and playing the songs

		// start UI thread
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
		setResizable(false);
		library = new MusicLibrary("Library");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 550);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("File");
		fileMenu.setIcon(null);
		menuBar.add(fileMenu);

		JMenuItem addSongMenuItem = new JMenuItem("Add Songs to Library");
		addSongMenuItem.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JFileChooser filesChosen = new JFileChooser();
				filesChosen.setMultiSelectionEnabled(true);
				FileNameExtensionFilter mp3filter = new FileNameExtensionFilter("MP3", "mp3");
				filesChosen.setFileFilter(mp3filter);
				int returnVal = filesChosen.showOpenDialog(getParent());
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File[] mp3s = filesChosen.getSelectedFiles();
					//System.out.println("You chose to open this file: " + filesChosen.getSelectedFile().getAbsolutePath());
					for (File i : mp3s){
						Mp3 mp3 = library.addSong(i.getAbsolutePath());
						((DefaultTableModel) currentPlaylistTable.getModel()).addRow(mp3.parseMetaData()); // is this the right way to do this? Yes
					}
				}
			}
		});
		fileMenu.add(addSongMenuItem);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JPanel bottomContentPanel = new JPanel();
		bottomContentPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

		JPanel mainContentPanel = new JPanel();
		mainContentPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(bottomContentPanel, GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addComponent(mainContentPanel, GroupLayout.PREFERRED_SIZE, 771, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(204, Short.MAX_VALUE))
				);
		gl_contentPane.setVerticalGroup(
				gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addComponent(mainContentPanel, GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(bottomContentPanel, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE))
				);

		JScrollPane scrollPane_1 = new JScrollPane();

		JLabel lblCurrentlyPlaying = new JLabel("Currently Playing:");
		lblCurrentlyPlaying.setHorizontalTextPosition(SwingConstants.CENTER);
		lblCurrentlyPlaying.setFont(new Font("Tahoma", Font.BOLD, 20));
		try {
			lblWebAddress = new JTextField("Web Address: http://" + InetAddress.getLocalHost().getHostAddress().toString() + ":8080");
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		lblWebAddress.setBorder(null);
		lblWebAddress.setEditable(false);

		//lblSelectedPlaylistName = new JLabel("Selected Playlist Name");
		lblWebAddress.setFont(new Font("Tahoma", Font.BOLD, 16));

		lblSongTitle = new JLabel("");
		lblSongTitle.setHorizontalTextPosition(SwingConstants.CENTER);
		lblSongTitle.setFont(new Font("Tahoma", Font.BOLD, 30));

		GroupLayout gl_mainContentPanel = new GroupLayout(mainContentPanel);
		gl_mainContentPanel.setHorizontalGroup(
				gl_mainContentPanel.createParallelGroup(Alignment.TRAILING)
				.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 767, Short.MAX_VALUE)
				.addGroup(gl_mainContentPanel.createSequentialGroup()
						.addContainerGap()
						.addComponent(lblCurrentlyPlaying)
						.addPreferredGap(ComponentPlacement.RELATED, 248, Short.MAX_VALUE)
						.addComponent(lblWebAddress, GroupLayout.PREFERRED_SIZE, 289, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.LEADING, gl_mainContentPanel.createSequentialGroup()
								.addContainerGap()
								.addComponent(lblSongTitle, GroupLayout.DEFAULT_SIZE, 737, Short.MAX_VALUE)
								.addContainerGap())
				);
		gl_mainContentPanel.setVerticalGroup(
				gl_mainContentPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_mainContentPanel.createSequentialGroup()
						.addGroup(gl_mainContentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblWebAddress, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_mainContentPanel.createSequentialGroup()
										.addGap(8)
										.addComponent(lblCurrentlyPlaying)))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(lblSongTitle, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 318, GroupLayout.PREFERRED_SIZE))
				);

		currentPlaylistTable = new JTable();
		scrollPane_1.setViewportView(currentPlaylistTable);

		currentPlaylistTable.setModel(new DefaultTableModel(
				library.getSongListInfo(),
				PlayListColumnNames
				)
				);
		currentPlaylistTable.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );


		currentPlaylistTable.getColumnModel().getColumn(1).setPreferredWidth(146);
		currentPlaylistTable.getColumnModel().getColumn(2).setPreferredWidth(118);
		currentPlaylistTable.getColumnModel().getColumn(3).setPreferredWidth(43);
		currentPlaylistTable.getColumnModel().getColumn(4).setPreferredWidth(162);

		currentPlaylistTable.getColumnModel().getColumn(1).setResizable(false);
		currentPlaylistTable.getColumnModel().getColumn(2).setResizable(false);
		currentPlaylistTable.getColumnModel().getColumn(3).setResizable(false);
		currentPlaylistTable.getColumnModel().getColumn(4).setResizable(false);


		currentPlaylistTable.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
		currentPlaylistTable.getColumnModel().removeColumn(currentPlaylistTable.getColumnModel().getColumn(0));
		//currentPlaylistTable.getColumnModel().getColumn(0).setMaxWidth(0);
		mainContentPanel.setLayout(gl_mainContentPanel);

		JPanel playbackControlPanel = new JPanel();

		JPanel seekPanel = new JPanel();
		GroupLayout gl_bottomContentPanel = new GroupLayout(bottomContentPanel);
		gl_bottomContentPanel.setHorizontalGroup(
				gl_bottomContentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_bottomContentPanel.createSequentialGroup()
						.addComponent(playbackControlPanel, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(seekPanel, GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE))
				);
		gl_bottomContentPanel.setVerticalGroup(
				gl_bottomContentPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(playbackControlPanel, GroupLayout.PREFERRED_SIZE, 41, Short.MAX_VALUE)
				.addComponent(seekPanel, GroupLayout.PREFERRED_SIZE, 41, Short.MAX_VALUE)
				);

		JSlider seekSlider = new JSlider();
		seekSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				// Update current seek time label to reflect slider position

			}
		});
		seekSlider.setValue(0);

		JLabel seekCurrentLabel = new JLabel("0:00");

		JLabel seekDurationLabel = new JLabel("0:00");
		GroupLayout gl_seekPanel = new GroupLayout(seekPanel);
		gl_seekPanel.setHorizontalGroup(
				gl_seekPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_seekPanel.createSequentialGroup()
						.addGap(25)
						.addComponent(seekCurrentLabel)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(seekSlider, GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(seekDurationLabel)
						.addContainerGap())
				);
		gl_seekPanel.setVerticalGroup(
				gl_seekPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_seekPanel.createSequentialGroup()
						.addGap(11)
						.addGroup(gl_seekPanel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(seekCurrentLabel, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
								.addComponent(seekSlider, GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
								.addComponent(seekDurationLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);
		seekPanel.setLayout(gl_seekPanel);

		JButton btnPrevious = new JButton("<<");

		btnPlay = new JButton(">");
		btnPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				playButtonPressed();
			}
		});

		JButton btnNext = new JButton(">>");

		btnNext.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(handler.getPlayerState() == STATE.PLAYING){
					handler.commands.add(new StopCommand());
				}
			}
		});


		JSlider volumeSlider = new JSlider();
		GroupLayout gl_playbackControlPanel = new GroupLayout(playbackControlPanel);
		gl_playbackControlPanel.setHorizontalGroup(
				gl_playbackControlPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_playbackControlPanel.createSequentialGroup()
						.addGap(11)
						.addComponent(btnPrevious)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnPlay)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnNext)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(volumeSlider, GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
						.addContainerGap())
				);
		gl_playbackControlPanel.setVerticalGroup(
				gl_playbackControlPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_playbackControlPanel.createSequentialGroup()
						.addGap(5)
						.addGroup(gl_playbackControlPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnPrevious)
								.addComponent(btnPlay)
								.addComponent(btnNext))
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addGroup(Alignment.TRAILING, gl_playbackControlPanel.createSequentialGroup()
										.addContainerGap()
										.addComponent(volumeSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addGap(0, 0, Short.MAX_VALUE))
				);
		playbackControlPanel.setLayout(gl_playbackControlPanel);
		bottomContentPanel.setLayout(gl_bottomContentPanel);
		contentPane.setLayout(gl_contentPane);
	}

	public static void playButtonPressed() {
		MusicLibrary list = library;
		if (currentPlaylistTable.getModel().getRowCount() == 0) {
			JOptionPane.showMessageDialog(contentPane, "You do not have any songs in your library.\nPlease add a song to the library.", "Empty Library", JOptionPane.ERROR_MESSAGE);
			return;
		}



		if (handler.getPlayerState() == STATE.PLAYING) {
			System.out.println("sending pause command");
			handler.commands.add(new PauseCommand());
			btnPlay.setText(">");
			//MusicHandler.commands.add(new PlayCommand(list.getMp3ByPlaylistId(selectSongId)));

		} else {
			if(handler.getPlayerState() == STATE.PAUSED){
				handler.commands.add(new ResumeCommand());
			}else if(handler.getPlayerState() == STATE.NOT_STARTED || handler.getPlayerState() == STATE.FINISHED){
				int selectSongId = Integer.parseInt(""+currentPlaylistTable.getModel().getValueAt(0, 0));
				System.out.println("selectSongId: " + selectSongId);
				Mp3 selectSong = list.getMp3ById(selectSongId);
				if(!selectSong.getFile().exists()){
					String message = selectSong.getTitle() + " could not be found.\n" +
							"This song will now be removed from the library.";
					JOptionPane.showMessageDialog(contentPane, message, "Could not locate mp3 file", JOptionPane.ERROR_MESSAGE);
					((DefaultTableModel) currentPlaylistTable.getModel()).removeRow(0);
					list.getMp3List().remove(selectSong);
					//list.printMp3List();
				}
				else{
					currentlyPlaying = list.getMp3ById(selectSongId);
					handler.commands.add(new PlayCommand(list.getMp3ById(selectSongId)));
					lblSongTitle.setText(currentlyPlaying.getTitle());
					redrawTable();
				}
			}
			btnPlay.setText("| |");
		}
	}

	public void pauseButtonPressed() {
		// TODO Auto-generated method stub

	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}


	// return the currently selected playlist?
	public static MusicLibrary getCurrentList() {
		return library;
	}

	public static Mp3 getCurrentlyPlaying(){
		return currentlyPlaying;
	}

	public static int doUpvote(int songId) {
		Mp3 mp3 = library.getMp3ById(songId);
		if(mp3 != currentlyPlaying){
			mp3.addUpvote();
			redrawTable();
		}
		return mp3.getUpvotes();
	}


	public static void redrawTable() {
		System.out.println("redrawing");
		// clear the table
		int rowCount = currentPlaylistTable.getRowCount();
		for (int i = 0; i < rowCount; i++)  {
			((DefaultTableModel) currentPlaylistTable.getModel()).removeRow(0);
		}

		library.sortPlaylist();

		// recreate the table based on newly sorted playlist
		for (Mp3 mp3 : library.getMp3List()) {
			if(mp3 != currentlyPlaying){
				((DefaultTableModel) currentPlaylistTable.getModel()).addRow(mp3.parseMetaData());
			}
		}

		currentPlaylistTable.getColumnModel().getColumn(1).setPreferredWidth(146);
		currentPlaylistTable.getColumnModel().getColumn(2).setPreferredWidth(118);
		currentPlaylistTable.getColumnModel().getColumn(3).setPreferredWidth(43);
		currentPlaylistTable.getColumnModel().getColumn(4).setPreferredWidth(162);

		currentPlaylistTable.getColumnModel().getColumn(1).setResizable(false);
		currentPlaylistTable.getColumnModel().getColumn(2).setResizable(false);
		currentPlaylistTable.getColumnModel().getColumn(3).setResizable(false);
		currentPlaylistTable.getColumnModel().getColumn(4).setResizable(false);


	}
	/*
	public static int doDownvote(int songId) {
		Mp3 mp3 = library.getMp3ById(songId);
		mp3.addDownvote();
		redrawTable();
		return mp3.getDownvotes(); 
	}*/

	public static MusicLibrary getLibrary() {
		return library;
	}

	public static void playNextSong() {
		currentlyPlaying.resetUpvoteCount();
		redrawTable();
		System.out.println("selectSongId: " + currentlyPlaying);
		playButtonPressed();
	}

	public static String getCurrentlyPlayingTitle() {
		if (handler.getPlayerState() == STATE.PLAYING){
			return currentlyPlaying.getTitle();
		}
		else
			return " ";
	}
}