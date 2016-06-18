package com.NonGuI;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

public class Output extends OutputStream{
	
	private JTextArea consoleStream;
	
	public Output(JTextArea stream){
		
		consoleStream = stream;
		
	}
	
	@Override
	public void write(int b) throws IOException{
		
		// Used to import console text into a new JTextArea
		consoleStream.append(String.valueOf((char)b));
		consoleStream.setCaretPosition(consoleStream.getDocument().getLength());
		
	}

}
