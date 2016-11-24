package com.oldzitterhand.app;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Main class of the application resource checker.
 * 
 * @author Patrick Metz
 */
public class AppResourceChecker extends JFrame {
	
	public AppResourceChecker() {
		setTitle("Application Resource Checker");
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		//panel.setPreferredSize(new Dimension(580, 660));
		
		JLabel lblResourceFile = new JLabel("Resource file:");
		lblResourceFile.setBounds(10, 11, 84, 14);
		panel.add(lblResourceFile);
		
		JLabel lblSearchFolder = new JLabel("Search folder:");
		lblSearchFolder.setBounds(10, 36, 84, 14);
		panel.add(lblSearchFolder);
		
		txtSelectedFile = new JTextField();
		txtSelectedFile.setEditable(false);
		txtSelectedFile.setBounds(224, 8, 246, 20);
		panel.add(txtSelectedFile);
		txtSelectedFile.setColumns(10);
		
		FolderChooserListener folderChooserListener = new FolderChooserListener(this);
		btnSelectFolder = new JButton("Select folder...");
		btnSelectFolder.setHorizontalAlignment(SwingConstants.LEFT);
		btnSelectFolder.addActionListener(folderChooserListener);
		btnSelectFolder.setBounds(104, 32, 110, 23);
		panel.add(btnSelectFolder);
		
		txtSelectedFolder = new JTextField();
		txtSelectedFolder.setEditable(false);
		txtSelectedFolder.setBounds(224, 33, 246, 20);
		panel.add(txtSelectedFolder);
		txtSelectedFolder.setColumns(10);
		
		StartButtonListener startButtonListener = new StartButtonListener();
		btnStartSearch = new JButton("Start search");
		btnStartSearch.addActionListener(startButtonListener);
		btnStartSearch.setBounds(104, 66, 110, 23);
		btnStartSearch.setEnabled(false);
		panel.add(btnStartSearch);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 100, 544, 2);
		panel.add(separator);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 113, 544, 438);
		panel.add(scrollPane_1);
		
		textArea = new JTextArea();
		scrollPane_1.setViewportView(textArea);
		
		FileChooserListener fileChooserListener = new FileChooserListener(this);
		btnSelectFile = new JButton("Select file...");
		btnSelectFile.setHorizontalAlignment(SwingConstants.LEFT);
		btnSelectFile.addActionListener(fileChooserListener);
		btnSelectFile.setBounds(104, 7, 110, 23);
		panel.add(btnSelectFile);
		
		btnStopSearch = new JButton("Stop search");
		btnStopSearch.addActionListener(startButtonListener);
		btnStopSearch.setBounds(224, 66, 110, 23);
		panel.add(btnStopSearch);
		
		btnClearSearch = new JButton("Clear search");
		btnClearSearch.addActionListener(startButtonListener);
		btnClearSearch.setBounds(340, 66, 110, 23);
		panel.add(btnClearSearch);
		
		statusLabel = new JLabel("");
		statusLabel.setBounds(10, 562, 544, 14);
		panel.add(statusLabel);
		
		fileLabel = new JLabel("");
		fileLabel.setBounds(10, 587, 544, 14);
		panel.add(fileLabel);

        try {
            URL urlBig = this.getClass().getResource("/icon/skull_32x32.png");
            URL urlSmall = this.getClass().getResource("/icon/skull_16x16.png");
            ArrayList<Image> images = new ArrayList<Image>();
            images.add( ImageIO.read(urlBig) );
            images.add( ImageIO.read(urlSmall) );
            setIconImages(images);
        } catch(Exception weTried) {}
		
		setPreferredSize(new Dimension(570, 660));
		setResizable(false);
		pack();
	}

	
	private static final long serialVersionUID = 2355294445820297149L;
	
	private JTextField txtSelectedFile;
	private JTextField txtSelectedFolder;
	private JFileChooser folderChooser;
	private JFileChooser fileChooser;
	private JButton btnSelectFolder;
	private JButton btnStartSearch;
	private JButton btnSelectFile;
	private JTextArea textArea;
	private CheckerThread checkerThread;
	private JButton btnStopSearch;
	private JButton btnClearSearch;
	private File propertiesFile;
	private File folder;
	private JLabel statusLabel;
	private JLabel fileLabel;

	public static void main(String[] args) {
		AppResourceChecker checker = new AppResourceChecker();
		checker.setVisible(true);
	}

    private static class FolderFilter extends javax.swing.filechooser.FileFilter {
        @Override
        public boolean accept( File file ) {
          return file.isDirectory();
        }

        @Override
        public String getDescription() {
          return "We only take directories";
        }
      }
    
    private static class PropertiesFileFilter extends javax.swing.filechooser.FileFilter {
        @Override
        public boolean accept( File file ) {
          return file.isDirectory() || file.getName().endsWith(".properties");
        }

        @Override
        public String getDescription() {
          return ".properties";
        }
      }
    
    private class FolderChooserListener implements ActionListener {
    	
    	Component parent;
    	
    	FolderChooserListener(Component parent) {
    		this.parent = parent;
    	}
    	
    	public void actionPerformed(ActionEvent e) {
    		if( e.getSource() == btnSelectFolder) {

    			folderChooser = new JFileChooser(".");
    			folderChooser.setControlButtonsAreShown(true);
    			folderChooser.setFileFilter( new FolderFilter() );
    			folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    			
    			int returnVal = folderChooser.showOpenDialog(parent);
	 
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	            	folder = folderChooser.getSelectedFile();
	                try {
						txtSelectedFolder.setText(folder.getCanonicalPath());
						if (txtSelectedFile.getText() != null && !txtSelectedFile.getText().isEmpty()) {
							btnStartSearch.setEnabled(true);
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	            } else {
	            	System.err.println("Open command cancelled by user.");
	            }
    		}
    	}
    }
    
    private class FileChooserListener implements ActionListener {
    	
    	Component parent;
    	
    	FileChooserListener(Component parent) {
    		this.parent = parent;
    	}
    	
    	public void actionPerformed(ActionEvent e) {
    		if( e.getSource() == btnSelectFile) {
    			fileChooser = new JFileChooser(".");
    			fileChooser.setControlButtonsAreShown(true);
    			fileChooser.setFileFilter( new PropertiesFileFilter() );
    			fileChooser.setAcceptAllFileFilterUsed(false);
    			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    			
    			int returnVal = fileChooser.showOpenDialog(parent);
	 
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	            	propertiesFile = fileChooser.getSelectedFile();
	                try {
						txtSelectedFile.setText(propertiesFile.getCanonicalPath());
						if (txtSelectedFolder.getText() != null && !txtSelectedFolder.getText().isEmpty()) {
							btnStartSearch.setEnabled(true);
						}

					} catch (IOException e1) {
						e1.printStackTrace();
					}
	            } else {
	            	System.err.println("Open command cancelled by user.");
	            }
    		}
    	}
    }
    
	private class StartButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnStartSearch){
				btnStartSearch.setEnabled(false);
				textArea.setText("");
				checkerThread = new CheckerThread(
										textArea, statusLabel, fileLabel, btnStartSearch, propertiesFile, folder);
				statusLabel.setText("Search started...");
				checkerThread.start();
			} else if (e.getSource() == btnStopSearch) {
				if (checkerThread != null) {
					checkerThread.cancel();
					statusLabel.setText("Search stopped");
				}
			} else if (e.getSource() == btnClearSearch) {
				textArea.setText("");
			}
		}
	}
	
}
