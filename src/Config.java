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
		System.out.println(String.format("Window size: %d x %d", width, height));
		return new Dimension(width, height);
	}
	
	public Dimension getVideoPanelSize() {
		return new Dimension((int) (width * VIDEO_PANEL_WIDTH_RATIO), height);
	}
	
	public Dimension getLiveFeedSize() {
		System.out.println(String.format("Live Feed Size: %d x %d", 
				(int) (width * LIVE_FEED_PANEL_WIDTH_RATIO), height));
		return new Dimension((int) (width * LIVE_FEED_PANEL_WIDTH_RATIO), height);
	}
	
	public Dimension getVideoSize() {
		return new Dimension((int) (width * VIDEO_PANEL_WIDTH_RATIO), 
							(int) (height * VIDEO_PANEL_WIDTH_RATIO));
	}
	
	public Dimension getControlPanelSize() {
		return new Dimension ((int) (width * VIDEO_PANEL_WIDTH_RATIO), 
								height - ((int) (height * VIDEO_PANEL_WIDTH_RATIO)));
	}
}
