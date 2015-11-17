import java.awt.Dimension;

/* Contains size configurations based on window dimensions */
public class Config {
	
	private int width;
	private int height;
	private final double VIDEO_PANEL_WIDTH_RATIO = 0.66;
	private final double LIVE_FEED_PANEL_WIDTH_RATIO = 0.33;
	
	public Config(int width, int height) {
		this.width = width; 
		this.height = height;
	}
	
	public Dimension getWindowSize() {
		return new Dimension(width, height);
	}
	
	public Dimension getVideoPanelSize() {
		return new Dimension((int) (width * VIDEO_PANEL_WIDTH_RATIO), height);
	}
	
	public Dimension getLiveFeedSize() {
		return new Dimension((int) (width * LIVE_FEED_PANEL_WIDTH_RATIO), height);
	}
}
