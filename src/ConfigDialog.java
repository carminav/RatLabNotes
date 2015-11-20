import java.awt.AWTKeyStroke;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
	
	private ArrayList<ConfigRow> rows;
	
	
	private final int INIT_ROWS = 3;
	
	private boolean updated;
	
	
	public ConfigDialog(JFrame parent) {
		super(parent, "Configure Hotkeys");
		setLocation(500,500);
		setSize(800, 600);
		
		mainPane = new JPanel();
		
		updated = false;
		
		keyInputPanel = new JPanel(new GridLayout(0, 3, 5, 5));
		JLabel keyLabel = new JLabel("Hot Key");
		keyLabel.setHorizontalAlignment(JLabel.CENTER);
		
		JLabel descLabel = new JLabel("Description");
		descLabel.setHorizontalAlignment(JLabel.CENTER);
		
		JLabel durLabel = new JLabel("Has Duration");
		durLabel.setHorizontalAlignment(JLabel.CENTER);
		
		rows = new ArrayList<ConfigRow>();
		
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
				updated = true;
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
	
	public Behaviors getBehaviorConfig() {
		if (updated) {
			
			Behaviors b = new Behaviors();
			
			for (int i = 0; i < rows.size(); i++) {
				ConfigRow row = rows.get(i);
				String key = row.keyText.getText();
				String desc = row.descText.getText();
				boolean hasDur = row.hasDuration.isSelected();
				
				if (!key.isEmpty() && !desc.isEmpty()) {
					int keyStroke = AWTKeyStroke.getAWTKeyStroke(key.charAt(0)).getKeyCode();
					b.addBehavior(keyStroke, desc, hasDur);
				}	
			}
			
			return b;
			
		} else return null;
	}
	
	private void addRow() {
		
		JTextArea keyText = new JTextArea(1, 3);
		JTextArea descText = new JTextArea(1, 20);
		
		JCheckBox hasDuration = new JCheckBox();
		
		hasDuration.setHorizontalAlignment(JCheckBox.CENTER);
		keyInputPanel.add(keyText);
		keyInputPanel.add(descText);
		keyInputPanel.add(hasDuration);
		
		rows.add(new ConfigRow(keyText, descText, hasDuration));
	}
	
	class ConfigRow {
		
		JTextArea keyText;
		JTextArea descText;
		JCheckBox hasDuration;
		
		public ConfigRow(JTextArea keyText, JTextArea descText, JCheckBox hasDuration) {
			this.keyText = keyText;
			this.descText = descText;
			this.hasDuration = hasDuration;
		}
	}
}
