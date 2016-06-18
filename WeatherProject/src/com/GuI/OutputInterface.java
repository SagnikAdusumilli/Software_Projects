package com.GuI;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.PrintStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import com.NonGuI.Output;

public class OutputInterface  extends JFrame{

	private JTextArea consolePrint;
	private PrintStream printStream, customPrintStream;
	private JScrollPane scrollPane;
	
	public OutputInterface(){
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(new Dimension(400, 200));
		setTitle("Console");
		setIconImage(new ImageIcon("Images/Icon.png").getImage());
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation((screenSize.width/2)-200, (screenSize.height/2));
		
		consolePrint = new JTextArea(50, 10);
		consolePrint.setEditable(false);
		customPrintStream = new PrintStream(new Output(consolePrint));
		
		printStream = System.out;
		System.setOut(customPrintStream);
		System.setErr(customPrintStream);

		scrollPane = new JScrollPane(consolePrint);
		add(scrollPane);  // Make a JScrollPane outputting all console text
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		setVisible(true);
		new ShowProgress().execute();
		
		/*try {
		    Thread.sleep(5000);
		} catch(InterruptedException e){
		    Thread.currentThread().interrupt();
		}*/
		
	}
	
	public void removeFrame(){
		setVisible(false);
		dispose();
	}
	
	class ShowProgress extends SwingWorker<Void, Void>{

		public Void doInBackground() throws Exception{
			try {
			    Thread.sleep(100000);
			} catch(InterruptedException e){
			    Thread.currentThread().interrupt();
			}
			return null;
		}

		public void done(){
			setVisible(false);  // Terminate when finished patching
			dispose();
		}

	}
	
}
