import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;


public class LiveFeedPanel extends JPanel {
	
	public LiveFeedPanel(Dimension dim) {
		setBackground(Color.white);
		setSize(dim);
		System.out.println("live feed panel: " + dim.width + " x " + dim.height);
	}
	
}
