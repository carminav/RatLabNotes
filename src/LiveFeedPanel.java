import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.Timestamp;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class LiveFeedPanel extends JPanel {
	
	private JTextArea keyText;
	private JTextArea startText;
	private JTextArea endText;
	private JTextArea descText;
	private JTextArea durText;
	
	public LiveFeedPanel(Dimension size) {
		setBackground(Color.blue);
		setSize(size);
		setPreferredSize(size);
		setMaximumSize(size);
		
		System.out.println("size.width = " + size.width);
		setLayout(new GridLayout(1, 5));
		setUpTextAreas();
	}
	
	private void setUpTextAreas() {
		keyText = new JTextArea();
		keyText.setEditable(false);
		add(keyText);
		
		startText = new JTextArea();
		startText.setEditable(false);
		add(startText);
		
		endText = new JTextArea();
		endText.setEditable(false);
		add(endText);
		
		descText = new JTextArea();
		descText.setEditable(false);
		add(descText);
		
		durText = new JTextArea();
		durText.setEditable(false);
		add(durText);
	}
	
	public void printLiveEvent(char key, Timestamp start, Timestamp end, String desc) {
		
		long dur = end.getTime() - start.getTime();

		keyText.append(String.format("(%s)\n", key));
		startText.append(String.format("[%s]\n", start));
		endText.append(String.format("[%s]\n", end));
		descText.append(String.format("%s\n", desc));
		durText.append(String.format("%s\n", new Timestamp(dur)));
		
	}
	

	
}
