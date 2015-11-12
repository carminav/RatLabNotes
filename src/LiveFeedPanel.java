import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;


public class LiveFeedPanel extends JPanel {
	
	public LiveFeedPanel(int width, int height) {
		setBackground(Color.white);
		setPreferredSize(new Dimension(width, height));
		setMinimumSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
	}
	
}
