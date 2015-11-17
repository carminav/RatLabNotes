import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;

public class Program {

    private final Config config;
    private final JFrame frame;
    private final VideoPanel videoPanel;
    private final LiveFeedPanel liveFeedPanel;
    private final DirectMediaPlayerComponent mediaPlayerComponent;
    private DirectMediaPlayer mediaPlayer;

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
        
        videoPanel = new VideoPanel(config.getVideoPanelSize(), config.getVideoSize());
        liveFeedPanel = new LiveFeedPanel(config.getLiveFeedSize());

        frame.add(videoPanel);
        frame.add(liveFeedPanel);
        frame.setVisible(true);
        
        /* play media */
        mediaPlayer = videoPanel.getMediaPlayer();
        mediaPlayerComponent = videoPanel.getMediaPlayerComponent();
        mediaPlayer.playMedia(args[0]);
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
    
}
