import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlPanel extends JPanel {
	
	private static final int ROWS = 1;
	private static final int COLS = 4;
	
	private JButton pausePlayBtn;
	private JButton rewindBtn;
	private JButton forwardBtn;
	private JButton doneBtn;
	
	public ControlPanel(Dimension size) {
		setBackground(Color.red);
		setSize(size);
		setPreferredSize(size);
		setMaximumSize(size);
		
		setLayout(new GridLayout(1,4));
		
		
		
		addButtons();
	}
	
	private void addButtons() {
		
		pausePlayBtn = new JButton("Play");
		rewindBtn = new JButton("Rewind");
		forwardBtn = new JButton("Forward");
		doneBtn = new JButton("Done");
		
		add(pausePlayBtn);
		add(rewindBtn);
		add(forwardBtn);
		add(doneBtn);
	}

}
