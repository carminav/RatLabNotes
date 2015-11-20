import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.Timestamp;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class LiveFeedPanel extends JPanel {
	
	public LiveFeedPanel(Dimension size) {
		setBackground(Color.white);
		setSize(size);
		setLayout(new GridLayout(0, 5));
	}
	
	public void printLiveEvent(char key, Timestamp start, Timestamp end, String desc) {
		
		long dur = end.getTime() - start.getTime();
		
		JLabel keyLabel = new JLabel(String.format("(%s)", key));
		JLabel startLabel = new JLabel(String.format("[%s]", start));
		JLabel endLabel = new JLabel(String.format("[%s]", end));
		JLabel descLabel = new JLabel(String.format("%s", desc));
		JLabel durLabel = new JLabel(String.format("%s", new Timestamp(dur)));
		
		add(keyLabel);
		add(startLabel);
		add(endLabel);
		add(descLabel);
		add(durLabel);
		
	}
	

	
}
