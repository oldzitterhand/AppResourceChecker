package com.oldzitterhand.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 * The checker thread contains the logic to actually to search the files for the resources.
 * 
 * @author Patrick Metz
 */
public class CheckerThread extends Thread {

	private JTextArea textArea;
	private File propertiesFile;
	private File searchFolder;
	private JLabel statusLabel;
	private JLabel fileLabel;
	private RecursiveFileVisitor fileVisitor;
	private JButton btnStartSearch;
	
	 // set this to true to stop the thread
	volatile boolean shutdown = false;
	
	public CheckerThread(JTextArea textArea, JLabel statusLabel, JLabel fileLabel, JButton btnStartSearch,
			File propertiesFile, File searchFolder) {
		this.textArea = textArea;
		this.propertiesFile = propertiesFile;
		this.searchFolder = searchFolder;
		this.statusLabel = statusLabel;
		this.fileLabel = fileLabel;
		this.btnStartSearch = btnStartSearch;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
    public void run() {

		List<String> properties = new ArrayList<>();
		
		try (BufferedReader reader = Files.newBufferedReader(propertiesFile.toPath(), Charset.defaultCharset())) {
		    String line = null;
		    while ((line = reader.readLine()) != null && !shutdown) {
		    	int pos = line.indexOf('=');
		    	if(pos > 0) {
		    		System.err.println("extract: " + line.substring(0, pos));
		    		properties.add(line.substring(0, pos));
		    	}
		    }
			fileVisitor = new RecursiveFileVisitor(searchFolder.toPath(), properties, fileLabel);
			
			if (!shutdown) {
				try {
					Files.walkFileTree(searchFolder.toPath(), fileVisitor);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				properties = new ArrayList<>();
			}
		} catch (IOException e1) {
			updateStatusFileReadingError();
		}

		statusLabel.setText("Search finished");
		fileLabel.setText("");
		for(String property : properties) {
			textArea.append(property + "\n");			
		}
    }

	/**
	 * 
	 */
	private void updateStatusFileReadingError() {
		try {
			statusLabel.setText("Failed to read file: " + propertiesFile.getCanonicalPath());
		} catch (IOException e) {
			statusLabel.setText("Failed to read file: " + propertiesFile.getName());
		}
	}
	
	public void cancel() {
		shutdown = true;
		if (fileVisitor != null) {
			fileVisitor.cancel();
		}
		btnStartSearch.setEnabled(true);
	}

}
