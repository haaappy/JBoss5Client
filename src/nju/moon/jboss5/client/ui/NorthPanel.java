/**
 * NorthPanel is a Control Panel.
 * It includes several buttons, labels and textfields.
 * functions:
 * 	Start/Stop (Application Server)
 * 	Deploy/Update/Remove (EJB files)
 * 	Select EJB Files
 *  
 *  Use Process to exec shell  (Only for Linux now, Win for future)
 */

package nju.moon.jboss5.client.ui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class NorthPanel extends JPanel {
	
	private JFrame fatherFrame;
	
	private JLabel jbossLabel = new JLabel("JBoss Home:");
	private JLabel serverLabel = new JLabel("Server Name:");
	private JTextField jbossHome = new JTextField();
	private JTextField serverName = new JTextField();
	private JButton startButton = new JButton("启动");
	private JButton stopButton = new JButton("停止");
	
	private JLabel fileNameLabel = new JLabel("File Name:");
	private JTextField fileNameTextField = new JTextField();
	private JButton openButton = new JButton("选择文件");
	
	private JLabel controlLabel = new JLabel("控制台信息:");
	private JButton deployButton = new JButton("部署文件");
	private JButton updateButton = new JButton("更新文件");
	private JButton removeButton = new JButton("卸载文件");
		
	// for process to excute shell
	private Process process; 
	private boolean isRunning = false;
	private String jbossHomeString = "";
	private String serverNameString = "";
	private String fileNamePath = "";
			
	public NorthPanel(JFrame fatherFrame){
		this.fatherFrame = fatherFrame;
		initNorthPanel();
	}
	
	public void initNorthPanel(){
		// add comp, set format and set buttons handlers
		initCompFormat();   
		initButtons();		
		
		// these values is set for default
		jbossHome.setText("/home/happy/JBOSS/jboss-5.1.0.GA-O");
		serverName.setText("default");
	}
	
	public void initCompFormat(){
		// use GridBagLayout
		GridBagLayout gridbag = new GridBagLayout();
		this.setLayout(gridbag);
		
		// add comp for four lines
		this.add(jbossLabel);	this.add(jbossHome);	this.add(startButton);
		this.add(serverLabel);	this.add(serverName);	this.add(stopButton);	
		this.add(fileNameLabel);	this.add(fileNameTextField);	this.add(openButton);
		this.add(controlLabel);		this.add(deployButton);		this.add(updateButton);		this.add(removeButton);	
		
		// set format
		GridBagConstraints s = new GridBagConstraints();
		s.fill = GridBagConstraints.BOTH;					
		
		// first line
		s.gridwidth = 1;		s.weightx = 0;		s.weighty = 0;
		gridbag.setConstraints(jbossLabel, s);

		s.gridwidth = 4;		s.weightx = 1;		s.weighty = 0;
		gridbag.setConstraints(jbossHome, s);
		
		s.gridwidth = GridBagConstraints.REMAINDER;		s.weightx = 0;		s.weighty = 0;
		gridbag.setConstraints(startButton, s);
		
		// second line
		s.gridwidth = 1;		s.weightx = 0;		s.weighty = 0;
		gridbag.setConstraints(serverLabel, s);
		
		s.gridwidth = 4;		s.weightx = 1;		s.weighty = 0;
		gridbag.setConstraints(serverName, s);
	
		s.gridwidth = GridBagConstraints.REMAINDER;		s.weightx = 0;		s.weighty = 0;
		gridbag.setConstraints(stopButton, s);
		
		// third line
		s.gridwidth = 1;		s.weightx = 0;		s.weighty = 1;
		gridbag.setConstraints(fileNameLabel, s);
		
		s.gridwidth = 4;		s.weightx = 0;		s.weighty = 1;
		gridbag.setConstraints(fileNameTextField, s);
			
		s.gridwidth = GridBagConstraints.REMAINDER;		s.weightx = 0;		s.weighty = 0;
		gridbag.setConstraints(openButton, s);
		
		// fourth line
		s.gridwidth = 3;		s.weightx = 1;		s.weighty = 0;
		gridbag.setConstraints(controlLabel, s);
		
		s.gridwidth = 1;		s.weightx = 0;		s.weighty = 0;
		gridbag.setConstraints(deployButton, s);
		
		s.gridwidth = 1;		s.weightx = 0;		s.weighty = 0;
		gridbag.setConstraints(updateButton, s);

		s.gridwidth = GridBagConstraints.REMAINDER;		s.weightx = 0;		s.weighty = 0;
		gridbag.setConstraints(removeButton, s);	
	}

	public void initButtons(){
		// six buttons, six action listener and hander
		startButton.addActionListener(new startButtonHandler());
		stopButton.addActionListener(new stopButtonHandler());
		openButton.addActionListener(new openButtonHandler());
		deployButton.addActionListener(new deployButtonHandler());
		updateButton.addActionListener(new updateButtonHandler());
		removeButton.addActionListener(new removeButtonHandler());
	}
	
	/**************** four thread to excute the shell (begin)  *****************/
	private class StartThread extends Thread{
		@Override
		public void run(){
			try{
				String cmd = "java -jar " + jbossHome.getText() + "/bin/run.jar -c " + serverName.getText();
//				System.out.println(cmd);
				
				process = Runtime.getRuntime().exec(cmd);
							
				InputStream stderr = process.getErrorStream();
				InputStreamReader isrerr = new InputStreamReader(stderr);
				BufferedReader brerr = new BufferedReader(isrerr);
				
				String line = null;				
				InputStream std = process.getInputStream();
				InputStreamReader isr = new InputStreamReader(std);
				BufferedReader br = new BufferedReader(isr);
				((JBoss5Client)fatherFrame).setTextAreaValue("[JBoss will start...]\n");
				while ((line = br.readLine()) != null || (line = brerr.readLine()) != null){
					System.out.println(line);					
					((JBoss5Client)fatherFrame).addTextAreaValue(line + "\n");
				}
				br.close();
				brerr.close();
				System.out.println("[JBoss has been stopped!!!]");
				((JBoss5Client)fatherFrame).addTextAreaValue("[JBoss has been stopped!!!]\n");
				isRunning = false;
			}
			catch(Exception exception){
				if (exception.getMessage().equals("Stream closed")){   // enter "stop" (ctrl+C)
					System.out.println("[JBoss has been stopped!!!]");
					((JBoss5Client)fatherFrame).addTextAreaValue("[JBoss has been stopped!!!]\n");
					isRunning = false;
				}
				else{
					exception.printStackTrace();
				}		
			}
		}
	}
	
	private class DeployThread extends Thread{
		@Override
		public void run(){
			try{
				String cmd = "cp " + fileNamePath + " " + jbossHomeString + "/server/"+ serverNameString + "/deploy/";
				System.out.println(cmd);	
				Runtime.getRuntime().exec(cmd);
			}
			catch(Exception exception){
				exception.printStackTrace();		
			}
		}
	}
	
	private class UpdateThread extends Thread{
		@Override
		public void run(){
			try{
				String cmd = "cp " + fileNamePath + " " + jbossHomeString + "/server/"+ serverNameString + "/deploy/";
				System.out.println(cmd);	
				Runtime.getRuntime().exec(cmd);
			}
			catch(Exception exception){
				exception.printStackTrace();		
			}
		}
	}
	
	private class RemoveThread extends Thread{
		@Override
		public void run(){
			try{
				String cmd = "rm " + fileNamePath;
				System.out.println(cmd);	
				Runtime.getRuntime().exec(cmd);
			}
			catch(Exception exception){
				exception.printStackTrace();		
			}
		}
	}
	
	/**************** four thread to excute the shell (end)  *****************/
	
	/**************** six handlers for buttons (begin)  *****************/
	private class startButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if (!isRunning){
				jbossHomeString = jbossHome.getText();
				serverNameString = serverName.getText();
				new StartThread().start();
				isRunning = true;
			}
			else{
				JOptionPane.showMessageDialog(null, "已经有个JBoss在运行中！");
			}
		}
	}
	
	private class stopButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if (process != null){
				process.destroy();
				if (isRunning){
					isRunning = false;
				}		
			}
		}
	}
	
	private class openButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e){
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("EJB文件","jar");
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(new JPanel());
			if (returnVal == JFileChooser.APPROVE_OPTION){
				fileNameTextField.setText(chooser.getSelectedFile().getPath());
			}
		}
	}
	
	private class deployButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e){
			String filePath = fileNameTextField.getText();
			if (isRunning && filePath.length()!=0){
				String fileName = filePath.substring(filePath.lastIndexOf('/'));
				File file = new File(jbossHomeString + "/server/"+ serverNameString + "/deploy/"+fileName);
				if (!file.exists()){
					fileNamePath = filePath;
					new DeployThread().start();
				}
				else{
					JOptionPane.showMessageDialog(null, "选择的文件已经存在！");
				}		
			}
			else{
				JOptionPane.showMessageDialog(null, "先启动JBoss并选择文件！");
			}
		}
	}
	
	private class updateButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e){
			String filePath = fileNameTextField.getText();
			if (isRunning && filePath.length()!=0){
				String fileName = filePath.substring(filePath.lastIndexOf('/'));
				File file = new File(jbossHomeString + "/server/"+ serverNameString + "/deploy/"+fileName);
				if (!file.exists()){
					JOptionPane.showMessageDialog(null, "原版本文件不存在！");
				}
				else{
					fileNamePath = filePath;
					new UpdateThread().start();
				}		
			}
			else{
				JOptionPane.showMessageDialog(null, "先启动JBoss并选择文件！");
			}
		}
	}
	
	private class removeButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e){
			String filePath = fileNameTextField.getText();
			if (isRunning && filePath.length()!=0){
				File file = new File(filePath);
				if (!file.exists()){
					JOptionPane.showMessageDialog(null, "删除的文件不存在！");
				}
				else{
					fileNamePath = filePath;
					new RemoveThread().start();
				}		
			}
			else{
				JOptionPane.showMessageDialog(null, "先启动JBoss并选择文件！");
			}
		}
	}
	
	/**************** six handlers for buttons (end)  *****************/
	
}
