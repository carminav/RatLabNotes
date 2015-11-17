import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallback;
import uk.co.caprica.vlcj.player.direct.RenderCallbackAdapter;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;


public class VideoPanel extends JPanel {
	
	final private BufferedImage image;
	final private Dimension videoSize;
	final private DirectMediaPlayerComponent mediaPlayerComponent;
	private DirectMediaPlayer mediaPlayer;

	public VideoPanel(Dimension panelSize, Dimension videoSize) {
        setBackground(Color.black);
        setOpaque(true);
        setSize(panelSize);
        this.videoSize = videoSize;

        image = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .createCompatibleImage(videoSize.width, videoSize.height);
        
        BufferFormatCallback bufferFormatCallback = new BufferFormatCallback() {
            @Override
            public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
                return new RV32BufferFormat(videoSize.width, videoSize.height);
            }
        };
        
        mediaPlayerComponent = new DirectMediaPlayerComponent(bufferFormatCallback) {
            @Override
            protected RenderCallback onGetRenderCallback() {
                return new RCAdapter();
            }
        };
        
        mediaPlayer = mediaPlayerComponent.getMediaPlayer();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.drawImage(image, null, 0, 0);
    }
    
    public DirectMediaPlayerComponent getMediaPlayerComponent() {
    	return mediaPlayerComponent;
    }
    
    public DirectMediaPlayer getMediaPlayer() {
    	return mediaPlayer;
    }

    private class RCAdapter extends RenderCallbackAdapter {
        private RCAdapter() {
            super(new int[videoSize.width * videoSize.height]);
        }

        @Override
        protected void onDisplay(DirectMediaPlayer mediaPlayer, int[] rgbBuffer) {
            image.setRGB(0, 0, videoSize.width, videoSize.height, rgbBuffer, 0, videoSize.width);
            repaint();
        }
    }
}


