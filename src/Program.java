import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;

public class Program implements KeyEventDispatcher {

    private final Config config;
    private final JFrame frame;
    private final VideoSurface videoSurface;
    private final LiveFeedPanel liveFeedPanel;
    private static DirectMediaPlayerComponent mediaPlayerComponent;
    private DirectMediaPlayer mediaPlayer;
    private final JPanel leftPanel;
    
    private Behaviors behaviors;
    
    private ArrayList<BehaviorEvent> events;
    
    private ConfigDialog configDialog;
    
    private ControlPanel controlPanel;
    
    private boolean ignoreKeys;
    
    private HashMap<Integer, BehaviorEvent> openEvents;
    
    private String mediaPath = null;

    /* Initialize window with video and live feed components */
    public Program(String[] args) {

    	config = new Config(Toolkit.getDefaultToolkit().getScreenSize().width,
    				 (int) (0.95 * Toolkit.getDefaultToolkit().getScreenSize().height));
    	
        frame = new JFrame("Rat Lab Notes");
        frame.setSize(config.getWindowSize());
        Dimension dim = config.getWindowSize();
        System.out.println("window: " + dim.width + " x " + dim.height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
        ignoreKeys = true;
        
        openEvents = new HashMap<Integer, BehaviorEvent>();
        
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));
        
        behaviors = new Behaviors();
        
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setSize(config.getVideoPanelSize());
        
        videoSurface = new VideoSurface(config.getVideoSize());
        liveFeedPanel = new LiveFeedPanel(config.getLiveFeedSize());
           
        mediaPlayer = videoSurface.getMediaPlayer();
        mediaPlayerComponent = videoSurface.getMediaPlayerComponent();
        
        controlPanel = new ControlPanel(mediaPlayerComponent, config.getControlPanelSize());
        
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);
        
        leftPanel.add(videoSurface);
        leftPanel.add(controlPanel);
        
        setupMenu();
        
        configDialog = new ConfigDialog(frame, this);
        
        frame.add(leftPanel);
        frame.add(liveFeedPanel);
        frame.setVisible(true);
        
        System.out.println("live feed size: " + liveFeedPanel.getWidth());
        
        events = new ArrayList<BehaviorEvent>();
    }
    
    /* setup header menu items */
    private void setupMenu() {
    	JMenuBar menuBar = new JMenuBar();
    	JMenu fileMenu = new JMenu("File");
    	
    	JMenuItem newI, openI, saveI, configI;
    	
    	newI = new JMenuItem("New");
    	openI = new JMenuItem("Open");
    	saveI = new JMenuItem("Save");
    	configI = new JMenuItem("Configure");
    	
    	newI.addActionListener(new MenuListener());
    	configI.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ignoreKeys = true;
				configDialog.setVisible(true);
			}
    	});
    	
    	fileMenu.add(newI);
    	fileMenu.add(openI);
    	fileMenu.add(saveI);
    	fileMenu.add(configI);
    	menuBar.add(fileMenu);
    	
    	frame.setJMenuBar(menuBar);
    }
    
    
    public static void main(final String[] args) {
        new NativeDiscovery().discover();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Program(args);
                
            }
        });
    }
    
    protected void updateBehaviors(Behaviors b) {
    	behaviors = b;
    	System.out.println("size of b: " + b.size());
    	ignoreKeys = false;
    }
    
	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		
		if (ignoreKeys) return false;
		
		if (e.getID() == KeyEvent.KEY_PRESSED) {
			
			// Built in video controls
			if  (e.getKeyCode() == KeyEvent.VK_SPACE) {
				controlPanel.keyPressPlayPause();
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				controlPanel.keyPressRewind();
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				controlPanel.keyPressForward();
			} else {
				processConfigKeys(e);
			}
			
		}
			
		return false;
	}
	
	private void processConfigKeys(KeyEvent e) {
		System.out.println("code: " + e.getKeyCode());
		behaviors.print();
		
		if (e.getID() == KeyEvent.KEY_PRESSED && behaviors.hasBehavior(e.getKeyCode())) {
			String desc = behaviors.getDescription(e.getKeyCode());
			boolean hasDur = behaviors.getHasDuration(e.getKeyCode());
			long vidTime = mediaPlayerComponent.getMediaPlayer().getTime();
			
			// add to event
			
			// check if still open and then close
			if (hasDur) {
				// close event
				if (openEvents.containsKey(e.getKeyCode())) {
					BehaviorEvent b = openEvents.get(e.getKeyCode());
					b.setEnd(vidTime);
					openEvents.remove(e.getKeyCode());
					events.add(b);
					liveFeedPanel.printLiveEvent(e.getKeyChar(), time(b.start), time(b.end), desc, time(b.dur));
				} else {
					// open event (wait for ending key press)
					BehaviorEvent b = new BehaviorEvent(e.getKeyChar(), desc);
					b.setStart(vidTime);
					openEvents.put(e.getKeyCode(), b);
				}
			} else {
				BehaviorEvent b = new BehaviorEvent(e.getKeyChar(), desc);
				b.setStart(vidTime);
				b.setEnd(vidTime);
				events.add(b);
				liveFeedPanel.printLiveEvent(e.getKeyChar(), time(b.start), time(b.end), desc, time(b.dur));
			}
			
		}
	} 
	
	private String time(long millis) {
		return String.format("%02d:%02d:%02d",
				TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), 
				TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
				);
	}
    
	class BehaviorEvent {
		char key;
		String description;
		long start, end, dur;
		boolean expectNext;
		
		public BehaviorEvent(char key, String description) {
			this.key = key;
			this.description = description;
		}
		
		void setStart(long start) {
			this.start = start;
		}
		
		void setEnd(long end) {
			this.end = end;
			dur = end - start;
		}
	}
	
    class MenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean tmp = ignoreKeys;
			ignoreKeys = true;
			JFileChooser fileChooser = new JFileChooser();
	    	fileChooser.setCurrentDirectory(new File("C:\\Users\\Carmina\\Videos"));
	    	int result = fileChooser.showOpenDialog(frame);
	    	if (result == JFileChooser.APPROVE_OPTION) {
	    	    File selectedFile = fileChooser.getSelectedFile();
	    	    mediaPath = selectedFile.getAbsolutePath();
	    	}
	    	
	    	mediaPlayer.playMedia(mediaPath);
	    	
	    	frame.setTitle(String.format(
	                "Rat Lab Notes - %s",
	                mediaPlayerComponent.getMediaPlayer().getMediaMeta().getTitle()
	        ));
	    	ignoreKeys = tmp;
		}
    	
    }


    
}
