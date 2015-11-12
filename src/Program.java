import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;

public class Program {

    private static int width;

    private static int height;

    private final JFrame frame;

    private final VideoPanel videoSurface;

    private final DirectMediaPlayerComponent mediaPlayerComponent;
    private final LiveFeedPanel liveFeed;
    private final JPanel leftPanel;
    private final JPanel optionsPanel;
    
    private DirectMediaPlayer mediaPlayer;

    public static void main(final String[] args) {
        new NativeDiscovery().discover();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Program(args);
            }
        });
    }

    public Program(String[] args) {
    	width = Toolkit.getDefaultToolkit().getScreenSize().width;
    	height = Toolkit.getDefaultToolkit().getScreenSize().height;
    	
        frame = new JFrame("Direct Media Player");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
        frame.setLayout(layout);
        
        leftPanel = new JPanel();
        leftPanel.setLayout(layout);
        leftPanel.setSize(400, height);
        leftPanel.setBorder(BorderFactory.createLineBorder(Color.green));
        
        
        optionsPanel = new JPanel();
        optionsPanel.setLayout(layout);
        optionsPanel.setSize((int)(width * 0.33), (int)(height * 0.20));
        optionsPanel.setBackground(Color.red);
        optionsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
      
        
        videoSurface = new VideoPanel((int)(width * 0.60), (int)(height * 0.659));
        videoSurface.setBorder(BorderFactory.createLineBorder(Color.blue));
        liveFeed = new LiveFeedPanel((int)(width * 0.325), height);
     //   liveFeed.setBorder(BorderFactory.createLineBorder(Color.black));
        
        
        System.out.println("videoPanel: " + videoSurface.WIDTH + " x " + videoSurface.HEIGHT);
        System.out.println("optionsPanel: " + optionsPanel.WIDTH + " x " + optionsPanel.HEIGHT);
        // HELLOW
        
        leftPanel.add(videoSurface);
        leftPanel.add(optionsPanel);
        
        frame.add(leftPanel);
        frame.add(liveFeed);
        frame.setVisible(true);
        mediaPlayer = videoSurface.getMediaPlayer();
        mediaPlayerComponent = videoSurface.getMediaPlayerComponent();
        mediaPlayer.playMedia(args[0]);
    }
    
}
