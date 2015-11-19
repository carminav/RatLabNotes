import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;

import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;

public class ControlPanel extends JPanel {
	
	private static final int ROWS = 1;
	private static final int COLS = 4;
	
	private JButton pausePlayBtn;
	private JButton rewindBtn;
	private JButton forwardBtn;
	private JButton muteBtn;
	private JButton saveBtn;
	
	private JSlider timeline;
	private JSlider speed;
	
	private JPanel topPanel;
	private JPanel middlePanel;
	private JPanel bottomPanel;
	
	private JTextArea timePassedText;
	private JTextArea timeLeftText;
	
	private Dimension size;
	
	private DirectMediaPlayerComponent mediaPlayerComponent;
	
	public ControlPanel(DirectMediaPlayerComponent mpc, Dimension size) {
		this.size = size;
		
		setBackground(Color.black);
		setSize(size);
		setPreferredSize(size);
		setMaximumSize(size);
		
		setLayout(new GridLayout(5, 1));
		mediaPlayerComponent = mpc;
		
		
		addTopPanel();
		addMiddlePanel();
		addBottomPanel();
		
	}
	
	private void addTopPanel() {
		topPanel = new JPanel();
		topPanel.setBackground(Color.green);
		
		// add temporary time passed timestamp
		timePassedText = new JTextArea("15:03");
		timePassedText.setSize((int) (size.width * 0.10), size.height  / 5);
		topPanel.add(timePassedText);
			
		// add JSlider
		timeline = new JSlider(0, 100, 0);
	 	timeline.setPreferredSize(new Dimension((int) (size.width * 0.8), size.height / 5));
		
		topPanel.add(timeline);
		
		//add temporary time left timestamp
		timeLeftText = new JTextArea("02:03");
		timeLeftText.setSize((int) (size.width * 0.10), size.height / 5);
		topPanel.add(timeLeftText);
		
		add(topPanel);
		
	}
	
	private void addMiddlePanel() {
		middlePanel = new JPanel();
		middlePanel.setLayout(new GridLayout(1, 4));
		middlePanel.setBackground(Color.blue);
		
		addButtons();
		
		add(middlePanel);
	}
	
	private void addBottomPanel() {
		bottomPanel = new JPanel();
		
		speed = new JSlider(0, 100, 0);
		speed.setPreferredSize(new Dimension((int) (size.width * 0.7), size.height / 5));
		
		saveBtn = new JButton("Save");
		saveBtn.setPreferredSize(new Dimension((int) (size.width * 0.1), size.height / 5));
		
		bottomPanel.add(speed);
		bottomPanel.add(saveBtn);
		add(bottomPanel);
	}
	
	private void addButtons() {
		
		pausePlayBtn = new JButton("Play");
		pausePlayBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mediaPlayerComponent.getMediaPlayer().pause();
			}
		});
		
		rewindBtn = new JButton("Rewind");
		rewindBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mediaPlayerComponent.getMediaPlayer().skip(-1000);
			}
		});
		
		forwardBtn = new JButton("Forward");
		forwardBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mediaPlayerComponent.getMediaPlayer().skip(1000);
			}
		});
		
		muteBtn = new JButton("Mute");
		muteBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mediaPlayerComponent.getMediaPlayer().mute();
			}
		});
		
		middlePanel.add(pausePlayBtn);
		middlePanel.add(rewindBtn);
		middlePanel.add(forwardBtn);
		middlePanel.add(muteBtn);
	}
	
	public void keyPressPlayPause() {
		
	}
	
	public void keyPressRewind() {
		
	}
	
	public void keyPressForward() {
		
	}
	
	public void keyPressDone() {
		
	}

}
