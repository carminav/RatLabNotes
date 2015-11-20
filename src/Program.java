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
        
        behaviors = new Behaviors();
        
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setSize(config.getVideoPanelSize());
        
        videoSurface = new VideoSurface(config.getVideoSize());
        liveFeedPanel = new LiveFeedPanel(config.getLiveFeedSize());
           
        /* play media */
        mediaPlayer = videoSurface.getMediaPlayer();
        mediaPlayerComponent = videoSurface.getMediaPlayerComponent();
        
        controlPanel = new ControlPanel(mediaPlayerComponent, config.getControlPanelSize());
        
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);
        
        leftPanel.add(videoSurface);
        leftPanel.add(controlPanel);
        
        setupMenu();
        
        configDialog = new ConfigDialog(frame);
        
        frame.add(leftPanel);
        frame.add(liveFeedPanel);
        frame.setVisible(true);
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
				configDialog.setVisible(true);
				Behaviors b = configDialog.getBehaviorConfig();
				if (b != null) {
					behaviors = b;
				}
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
    
	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		
		if (e.getID() == KeyEvent.KEY_PRESSED) {
			if  (e.getKeyCode() == KeyEvent.VK_SPACE) {
				controlPanel.keyPressPlayPause();
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				controlPanel.keyPressRewind();
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				controlPanel.keyPressForward();
			}
		}
			
		
		return false;
	}
    
    class MenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
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
	    	
		}
    	
    }


    
}
