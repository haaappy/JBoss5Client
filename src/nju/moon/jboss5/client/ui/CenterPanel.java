/**
 *  CenterPanel is a display Panel
 *  It includes a textArea to display information
 */

package nju.moon.jboss5.client.ui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class CenterPanel extends JPanel {

	private JTextArea textArea = new JTextArea();
	
	public CenterPanel(){
		textArea.setEditable(false);
		this.setLayout(new GridLayout(1, 1, 10, 10));	
		this.add(new JScrollPane(textArea));
	}
	
	// function to set Value for textArea
	public void setTextAreaValue(String str){
		textArea.setText(str);
	}
	
	// funtion to add Value for textArea
	public void addTextAreaValue(String str){
		textArea.setText(textArea.getText() + str);
	}

}
