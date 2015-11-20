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
import java.sql.Timestamp;

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
    
    private ConfigDialog configDialog;
    
    private ControlPanel controlPanel;
    
    private boolean ignoreKeys;
    
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
			long start = mediaPlayerComponent.getMediaPlayer().getTime();
			long end = mediaPlayerComponent.getMediaPlayer().getTime();
			
			liveFeedPanel.printLiveEvent(e.getKeyChar(), new Timestamp(start), 
											new Timestamp(end), desc);
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
