import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class ConfigDialog extends JDialog {

	private JPanel mainPane;
	private JPanel keyInputPanel;
	private JButton addRowBtn;
	private JButton applyBtn;
	
	
	private final int INIT_ROWS = 3;
	
	
	public ConfigDialog(JFrame parent) {
		super(parent, "Configure Hotkeys");
		setLocation(500,500);
	//	setSize(800, 600);
		
		mainPane = new JPanel();
		mainPane.setBackground(Color.BLUE);
		
		keyInputPanel = new JPanel(new GridLayout(0, 3, 5, 5));
		JLabel keyLabel = new JLabel("Hot Key");
		keyLabel.setHorizontalAlignment(JLabel.CENTER);
		
		JLabel descLabel = new JLabel("Description");
		descLabel.setHorizontalAlignment(JLabel.CENTER);
		
		JLabel durLabel = new JLabel("Has Duration");
		durLabel.setHorizontalAlignment(JLabel.CENTER);
		
		keyInputPanel.add(keyLabel);
		keyInputPanel.add(descLabel);
		keyInputPanel.add(durLabel);
		
		for (int i = 0; i < INIT_ROWS; i++) {
			addRow();
		}
		
		addRowBtn = new JButton("Add New");
		addRowBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addRow();
				keyInputPanel.revalidate();
				
			}
		});
		
		applyBtn = new JButton("Apply");
		applyBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		
		JPanel btnPanel = new JPanel(new GridLayout(2,1));
		btnPanel.add(addRowBtn);
		btnPanel.add(applyBtn);
		btnPanel.setAlignmentX(0);
		
		mainPane.add(keyInputPanel);
		mainPane.add(btnPanel);
		
		getContentPane().add(mainPane);
		setVisible(true);
	}
	
	private void addRow() {
		
		JTextArea keyText = new JTextArea(1, 3);
		JTextArea descText = new JTextArea(1, 20);
		JCheckBox hasDuration = new JCheckBox();
		hasDuration.setHorizontalAlignment(JCheckBox.CENTER);
		
		keyInputPanel.add(keyText);
		keyInputPanel.add(descText);
		keyInputPanel.add(hasDuration);
	}
}
