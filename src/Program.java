import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
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

public class Program {

    private final Config config;
    private final JFrame frame;
    private final VideoSurface videoSurface;
    private final LiveFeedPanel liveFeedPanel;
    private final DirectMediaPlayerComponent mediaPlayerComponent;
    private DirectMediaPlayer mediaPlayer;
    private final JPanel leftPanel;
    
    private String mediaPath = null;

    /* Initialize window with video and live feed components */
    public Program(String[] args) {

    	config = new Config(Toolkit.getDefaultToolkit().getScreenSize().width,
    				Toolkit.getDefaultToolkit().getScreenSize().height);
    	
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
        
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setSize(config.getVideoPanelSize());
        
        videoSurface = new VideoSurface(config.getVideoSize());
        liveFeedPanel = new LiveFeedPanel(config.getLiveFeedSize());

        JPanel controlsPanel = new JPanel();
        controlsPanel.setSize(new Dimension(config.getVideoPanelSize().width, 
        								(int) (config.getVideoSize().height * 0.15)));
        controlsPanel.setMaximumSize(new Dimension(config.getVideoPanelSize().width, 
				(int) (config.getVideoSize().height * 0.15)));
        controlsPanel.setPreferredSize(new Dimension(config.getVideoPanelSize().width, 
				(int) (config.getVideoSize().height * 0.15)));
        
        controlsPanel.setBackground(Color.red);
        
        JButton pause = new JButton("Pause");
        pause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mediaPlayerComponent.getMediaPlayer().pause();
				
			}
        });
        controlsPanel.add(pause);
        
        
        leftPanel.add(videoSurface);
        leftPanel.add(controlsPanel);
        
        setupMenu();
        
        frame.add(leftPanel);
        frame.add(liveFeedPanel);
        frame.setVisible(true);
        
        /* play media */
        mediaPlayer = videoSurface.getMediaPlayer();
        mediaPlayerComponent = videoSurface.getMediaPlayerComponent();
        //mediaPlayer.playMedia(args[0]);
    }
    
    private void setupMenu() {
    	JMenuBar menuBar = new JMenuBar();
    	JMenu fileMenu = new JMenu("File");
    	
    	JMenuItem newI, openI, saveI, configI;
    	
    	newI = new JMenuItem("New");
    	openI = new JMenuItem("Open");
    	saveI = new JMenuItem("Save");
    	configI = new JMenuItem("Configure");
    	
    	newI.addActionListener(new MenuListener());
    	
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
	    	
		}
    	
    }
    
}
