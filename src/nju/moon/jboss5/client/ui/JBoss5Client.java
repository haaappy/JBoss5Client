/**
 * 
 * @author happy
 * JBoss5.1.0 Dymatic Update System
 * Conup transplant to JBoss5.1.0
 * It is a UI Client with 5 functions
 * Start/Stop (Application Server)
 * Deploy/Update/Remove (EJB files)
 */

package nju.moon.jboss5.client.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class JBoss5Client extends JFrame{

	// control panel
	private NorthPanel northPanel = new NorthPanel(this);
	
	// display panel
	private CenterPanel centerPanel = new CenterPanel();
	
	public JBoss5Client(){
		this.setTitle("JBoss5Client");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600,800);
		this.setResizable(false);
		this.setVisible(true);
		this.setLayout(new BorderLayout());
		this.add(northPanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);	
	}
	
	// function to set Value for centerPanel's textAreaValue
	public void setTextAreaValue(String str){
		centerPanel.setTextAreaValue(str);
	}
	
	// function to add Value for centerPanel's textAreaValue
	public void addTextAreaValue(String str){
		centerPanel.addTextAreaValue(str);
	}
		
	// main method to create JBoss5Client Frame
	public static void main(String[] args) {
		JBoss5Client client = new JBoss5Client();

	}

}
