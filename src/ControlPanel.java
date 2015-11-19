import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;

public class ControlPanel extends JPanel {
	
	private static final int ROWS = 1;
	private static final int COLS = 4;
	
	private JToggleButton pausePlayBtn;
	private JButton rewindBtn;
	private JButton forwardBtn;
	private JToggleButton muteBtn;
	private JButton saveBtn;
	
	private JSlider timeline;
	private JSlider speed;
	
	private JPanel topPanel;
	private JPanel middlePanel;
	private JPanel bottomPanel;
	
	private JLabel timePassedText;
	private JLabel timeLeftText;
	
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
		timePassedText = new JLabel("15:03");
		timePassedText.setFont(new Font("Courier New", Font.BOLD, 30));
		topPanel.add(timePassedText);
			
		// add JSlider
		timeline = new JSlider(0, 100, 0);
	 	timeline.setPreferredSize(new Dimension((int) (size.width * 0.85), size.height / 5));
	 	timeline.setMajorTickSpacing(10);
        timeline.setMajorTickSpacing(5);
        timeline.setPaintTicks(true);
        
		
		topPanel.add(timeline);
		
		//add temporary time left timestamp
		timeLeftText = new JLabel("02:03");
		timeLeftText.setFont(new Font("Courier New", Font.BOLD, 30));
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
		
		speed = new JSlider(-200, 200, 0);
		speed.setPreferredSize(new Dimension((int) (size.width * 0.7), size.height / 5));
		speed.setMajorTickSpacing(100);
        speed.setPaintTicks(true);
		Hashtable<Integer, JLabel> labelTable = new Hashtable ();
        labelTable.put( new Integer( 0 ), new JLabel("1x") );
        labelTable.put( new Integer( -200 ), new JLabel("-2x") );
        labelTable.put( new Integer( 200 ), new JLabel("2x") );
        speed.setLabelTable( labelTable );
        speed.setPaintLabels(true);

        speed.addChangeListener(new ChangeListener() {

        	public void stateChanged(ChangeEvent arg0) {
        		if(!speed.getValueIsAdjusting() && 
        				(mediaPlayerComponent.getMediaPlayer().isPlaying()))
        		{
        			int perc = speed.getValue();
        			float ratio= (float) (perc/400f*1.75);
        			ratio = ratio + (9/8);
        			mediaPlayerComponent.getMediaPlayer().setRate(ratio);
        		}

        	}
        	
        });
		
		saveBtn = new JButton("Save");
		saveBtn.setPreferredSize(new Dimension((int) (size.width * 0.1), size.height / 5));
		
		bottomPanel.add(speed);
		bottomPanel.add(saveBtn);
		add(bottomPanel);
	}
	
	private void addButtons() {
		
		pausePlayBtn = new JToggleButton("Play");
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
		
		muteBtn = new JToggleButton("mute");
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
