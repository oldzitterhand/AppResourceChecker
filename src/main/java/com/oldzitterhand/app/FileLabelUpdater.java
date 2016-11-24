package com.oldzitterhand.app;

import javax.swing.JLabel;

/**
 * The FileLabelUpdater is used for providing the information of the currently inspected file.
 * Will set the text of a given JLabel.
 * 
 * @author Patrick Metz
 */
public class FileLabelUpdater implements Runnable {

	private JLabel fileLabel;
	private String fileName;
	
	public FileLabelUpdater(JLabel fileLabel, String fileName) {
		this.fileLabel = fileLabel;
		this.fileName = fileName;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		if (fileLabel != null) {
			fileLabel.setText("Looking at file: " +fileName);
		}
	}

}
