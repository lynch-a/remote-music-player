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

import javax.swing.BorderFactory;
import javax.swing.JMenuBar;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.border.EtchedBorder;

import java.awt.GridLayout;

import javax.swing.SwingConstants;

import java.awt.FlowLayout;

import javax.swing.JSplitPane;
import javax.swing.BoxLayout;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JTable;

import java.awt.Font;

import javax.swing.table.DefaultTableModel;


public class MusicPlayerFrame extends JFrame {

	private JPanel contentPane;
	private Player player;
	private JTable currentPlaylistTable;

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
		setBounds(100, 100, 727, 481);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel bottomContentPanel = new JPanel();
				bottomContentPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
				
				JPanel sideContentPanel = new JPanel();
				sideContentPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
				
				JPanel mainContentPanel = new JPanel();
				mainContentPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
				GroupLayout gl_contentPane = new GroupLayout(contentPane);
				gl_contentPane.setHorizontalGroup(
					gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(bottomContentPanel, GroupLayout.DEFAULT_SIZE, 701, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
							.addComponent(sideContentPanel, GroupLayout.PREFERRED_SIZE, 161, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(mainContentPanel, GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE))
				);
				gl_contentPane.setVerticalGroup(
					gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(mainContentPanel, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
								.addComponent(sideContentPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(bottomContentPanel, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
				);
				JLabel lblSelectedPlaylistName = new JLabel("Selected Playlist Name");
				lblSelectedPlaylistName.setFont(new Font("Tahoma", Font.BOLD, 18));
				
				JScrollPane scrollPane_1 = new JScrollPane();
				GroupLayout gl_mainContentPanel = new GroupLayout(mainContentPanel);
				gl_mainContentPanel.setHorizontalGroup(
					gl_mainContentPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_mainContentPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblSelectedPlaylistName)
							.addContainerGap(316, Short.MAX_VALUE))
						.addComponent(scrollPane_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
				);
				gl_mainContentPanel.setVerticalGroup(
					gl_mainContentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_mainContentPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblSelectedPlaylistName)
							.addGap(8)
							.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE))
				);
				
				currentPlaylistTable = new JTable();
				scrollPane_1.setViewportView(currentPlaylistTable);
				
				currentPlaylistTable.setModel(new DefaultTableModel(
					new Object[][] {
						{"Never gonna give you up", "Rick Astley", "3:32", "Whenever you Need Somebody"},
					},
					new String[] {
						"Track", "Artist", "Time", "Abum"
					}
				));
				currentPlaylistTable.getColumnModel().getColumn(0).setPreferredWidth(146);
				currentPlaylistTable.getColumnModel().getColumn(1).setPreferredWidth(118);
				currentPlaylistTable.getColumnModel().getColumn(2).setPreferredWidth(43);
				currentPlaylistTable.getColumnModel().getColumn(3).setPreferredWidth(162);
				currentPlaylistTable.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
				mainContentPanel.setLayout(gl_mainContentPanel);
				
				JPanel cdArtPanel = new JPanel();
				
				JList playlistList = new JList();
				playlistList.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
				playlistList.setModel(new AbstractListModel() {
					String[] values = new String[] {"Library", "Queued Songs", "Playlist 1", "Playlist 2", "Playlist n", "Playlist n+1"};
					public int getSize() {
						return values.length;
					}
					public Object getElementAt(int index) {
						return values[index];
					}
				});
				GroupLayout gl_sideContentPanel = new GroupLayout(sideContentPanel);
				gl_sideContentPanel.setHorizontalGroup(
					gl_sideContentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(cdArtPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
						.addComponent(playlistList, GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
				);
				gl_sideContentPanel.setVerticalGroup(
					gl_sideContentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_sideContentPanel.createSequentialGroup()
							.addComponent(playlistList, GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(cdArtPanel, GroupLayout.PREFERRED_SIZE, 139, GroupLayout.PREFERRED_SIZE))
				);
				sideContentPanel.setLayout(gl_sideContentPanel);
				
				JPanel playbackControlPanel = new JPanel();
				
				JPanel seekPanel = new JPanel();
				GroupLayout gl_bottomContentPanel = new GroupLayout(bottomContentPanel);
				gl_bottomContentPanel.setHorizontalGroup(
					gl_bottomContentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_bottomContentPanel.createSequentialGroup()
							.addComponent(playbackControlPanel, GroupLayout.PREFERRED_SIZE, 241, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(seekPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(79, Short.MAX_VALUE))
				);
				gl_bottomContentPanel.setVerticalGroup(
					gl_bottomContentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(playbackControlPanel, GroupLayout.PREFERRED_SIZE, 32, Short.MAX_VALUE)
						.addComponent(seekPanel, GroupLayout.PREFERRED_SIZE, 32, Short.MAX_VALUE)
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
							.addGap(16)
							.addComponent(seekCurrentLabel)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(seekSlider, GroupLayout.PREFERRED_SIZE, 296, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(seekDurationLabel)
							.addContainerGap(11, Short.MAX_VALUE))
				);
				gl_seekPanel.setVerticalGroup(
					gl_seekPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_seekPanel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_seekPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(seekSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(seekDurationLabel)
								.addComponent(seekCurrentLabel))
							.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);
				seekPanel.setLayout(gl_seekPanel);
				
				JButton btnPrevious = new JButton("<<");
				
						JButton btnPlay = new JButton(">");
						btnPlay.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent arg0) {
								playButtonPressed();
							}
						});
				
				JButton btnNext = new JButton(">>");
				
				JSlider volumeSlider = new JSlider();
				GroupLayout gl_playbackControlPanel = new GroupLayout(playbackControlPanel);
				gl_playbackControlPanel.setHorizontalGroup(
					gl_playbackControlPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_playbackControlPanel.createSequentialGroup()
							.addGap(2)
							.addComponent(btnPrevious)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnPlay)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNext)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(volumeSlider, GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
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

	public void playButtonPressed() {

		Mp3 testFile = new Mp3("/Users/alynch/Desktop/test.mp3");
		MusicHandler.commands.add(new PlayCommand(testFile));
	}
}