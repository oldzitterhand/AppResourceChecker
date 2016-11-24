package com.oldzitterhand.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 * @author Patrick Metz
 *
 */
public class RecursiveFileVisitor extends SimpleFileVisitor<Path> {

	 // set this to true to stop the thread
	volatile boolean shutdown = false;
	//private Pattern pattern = Pattern.compile("^(.*?)");
	private Pattern pattern = Pattern.compile("([^\\s]+((?i)(\\.xhtml|\\.java|-flow\\.xml))$)");
	private Path rootDir;
	private List<String> properties;
	private JLabel fileLabel;
	
	public RecursiveFileVisitor(Path path, List<String> properties, JLabel fileLabel) {
		this.rootDir = path;
		this.properties = properties;
		this.fileLabel = fileLabel;
	}
	
    @Override
    public FileVisitResult visitFile(final Path path, BasicFileAttributes mainAtts) throws IOException {
    	if (pattern.matcher(path.toString()).matches()) {
	    	SwingUtilities.invokeLater(new FileLabelUpdater(fileLabel, path.getFileName().toString()));
//    		SwingUtilities.invokeLater(new Runnable(){ 
//	    		public void run(){ 
//	    			fileLabel.setText("Looking at file: " + path.getFileName());
//			         	
//	    		}
//	    	});
    		try (BufferedReader reader = Files.newBufferedReader(path, Charset.defaultCharset())) {
    			String line = null;
    			while ((line = reader.readLine()) != null && !shutdown) {
    				Iterator<String> propertiesIterator = properties.iterator();
    				while (propertiesIterator.hasNext() && !shutdown) {
    					String property = propertiesIterator.next();
    					if (line.contains(property)) {
    						propertiesIterator.remove();
    					}
    					
    				}
    			}
    		} catch (IOException e1) {
    			e1.printStackTrace();
    		}
        }
        
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path path, IOException exc) throws IOException {
        exc.printStackTrace();
        // If the root directory has failed it makes no sense to continue
        return path.equals(rootDir)? FileVisitResult.TERMINATE:FileVisitResult.CONTINUE;
    }
    
    public void cancel() {
    	shutdown = true;
    }
	
}
