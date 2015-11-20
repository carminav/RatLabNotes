import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;

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
	private JProgressBar progressBar;
	
	private JPanel topPanel;
	private JPanel middlePanel;
	private JPanel bottomPanel;
	
	private JLabel videoTime;
	
	private Dimension size;
	
	private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	
	private DirectMediaPlayerComponent mediaPlayerComponent;
	private boolean setPositionValue;
	
	private boolean pausePlaySelected = false;
	
	private Program prog;
	
	
	public ControlPanel(DirectMediaPlayerComponent mpc, Dimension size, Program prog) {
		this.size = size;
		this.prog = prog;
		
		setBackground(Color.black);
		setSize(size);
		setPreferredSize(size);
		setMaximumSize(size);
		
		setLayout(new GridLayout(5, 1));
		mediaPlayerComponent = mpc;
		
		addTopPanel();
		addMiddlePanel();
		addBottomPanel();
		
		executorService.scheduleAtFixedRate(new UpdateRunnable(mpc.getMediaPlayer()), 0L, 1L, TimeUnit.SECONDS);
		
	}
	
	private void addTopPanel() {
		topPanel = new JPanel();
		topPanel.setBackground(Color.green);
		
		videoTime = new JLabel("hh:mm:ss");
		
		timeline = new JSlider(0,100,0);
		timeline.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(!setPositionValue) {
					float positionValue = (float)timeline.getValue() / 100.0f;
					mediaPlayerComponent.getMediaPlayer().setPosition(positionValue);
				}
			}
		});
		
		
		progressBar = new JProgressBar();
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		progressBar.setValue(0);
		
		JPanel positionPanel = new JPanel();
	    positionPanel.setLayout(new GridLayout(2, 1));
	    positionPanel.add(progressBar);
	    positionPanel.add(timeline);
	    
	    topPanel.setLayout(new BorderLayout(8, 0));
	    
	    topPanel.add(videoTime, BorderLayout.WEST);
	    topPanel.add(positionPanel, BorderLayout.CENTER);
	    
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
		saveBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				prog.exportToCSV();
			}
			
		});
		
		
		bottomPanel.add(speed);
		bottomPanel.add(saveBtn);
		add(bottomPanel);
	}
	
	private void updatePosition(int value) {
		progressBar.setValue(value);

		// Set the guard to stop the update from firing a change event
		setPositionValue = true;
		timeline.setValue(value);
		setPositionValue = false;
	}
	
	private void updateTime(long millis) {
		String s = String.format("%02d:%02d:%02d",
				TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), 
				TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
				);
		videoTime.setText(s);
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
		pausePlayBtn.doClick();
	}
	
	public void keyPressRewind() {
		rewindBtn.doClick();
	}
	
	public void keyPressForward() {
		forwardBtn.doClick();
	}
	
	
	private final class UpdateRunnable implements Runnable {

	    private final MediaPlayer mediaPlayer;
	    
	    private UpdateRunnable(MediaPlayer mediaPlayer) {
	      this.mediaPlayer = mediaPlayer;
	    }
	    
	    @Override
	    public void run() {
	      final long time = mediaPlayer.getTime();
	      
	      final long duration = mediaPlayer.getLength();
	      final int position = duration > 0 ? (int)Math.round(100.0 * (double)time / (double)duration) : 0;
	      
	      // Updates to user interface components must be executed on the Event
	      // Dispatch Thread
	      SwingUtilities.invokeLater(new Runnable() {
	        @Override
	        public void run() {
	          updateTime(time);
	          updatePosition(position);
	        }
	      });
	    }
	  }

}
