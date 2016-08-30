package de.lmu.ifi.mfa_gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.lmu.ifi.mfa.IFlowNetwork;

public class MFAController {

    private MFAView ctrlView;
    private IFlowNetwork ctrlModel;

    public MFAController(MFAView ctrlView, IFlowNetwork ctrlModel){
        this.ctrlModel = ctrlModel;
        this.ctrlView = ctrlView;

        addListeners();
    }
    
    private void addListeners(){
        this.ctrlView.setAddVertexListener(new AddVertexListener());
        this.ctrlView.setRemoveVertexListener(new RemoveVertexListener());
        this.ctrlView.setAddEdgeListener(new AddEdgeListener());
        this.ctrlView.setRemoveEdgeListener(new RemoveEdgeListener());
        this.ctrlView.setSourceListener(new SourceListener());
        this.ctrlView.setSinkListener(new SinkListener());
        this.ctrlView.setDinicListener(new DinicListener());
        this.ctrlView.setGoldbergListener(new GoldbergListener());
        this.ctrlView.setResetListener(new ResetListener());
        this.ctrlView.setSaveListener(new SaveListener());
        this.ctrlView.setLoadListener(new LoadListener());
    }
    
    /**
     * Inner listener classes implementing the interface ActionListener
     *
     */
    class AddVertexListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	int vertexId = ctrlView.getAddVertexId();
        	ctrlModel.addVertex(vertexId);
        }
    }
    class RemoveVertexListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	int vertexId = ctrlView.getRemoveVertexId();
        	ctrlModel.removeVertex(vertexId);
        }
    }
    class AddEdgeListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int startEdgeId = ctrlView.getAddEdgeId1();
            int endEdgeId = ctrlView.getAddEdgeId2();
            int capEdge = ctrlView.getAddEdgeCap();
        	ctrlModel.addEdge(startEdgeId,endEdgeId,capEdge);
        }
    }
    class RemoveEdgeListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	int startEdgeId = ctrlView.getRemoveEdgeId1();
            int endEdgeId = ctrlView.getRemoveEdgeId2();
        	ctrlModel.removeEdge(startEdgeId,endEdgeId);
        }
    }
    class SourceListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	int sourceId = ctrlView.getSourceId();
        	ctrlModel.setSource(sourceId);
        }
    }
    class SinkListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	int sinkId = ctrlView.getSinkId();
        	ctrlModel.setSink(sinkId);
        }
    }
    class DinicListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	ctrlModel.dinic();
        }
    }
    class GoldbergListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	ctrlModel.goldbergTarjan();
        }
    }
    class ResetListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	ctrlModel.resetNetwork();
        }
    }
    class SaveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	JFileChooser fileChooser = new JFileChooser();
        	// add filter
        	FileNameExtensionFilter mfaFilter = new FileNameExtensionFilter("mfa files (*.mfa)", "mfa");
        	fileChooser.addChoosableFileFilter(mfaFilter);
        	fileChooser.setFileFilter(mfaFilter);
        	fileChooser.setSelectedFile(new File("*.mfa"));
        	if (fileChooser.showSaveDialog(ctrlView) == JFileChooser.APPROVE_OPTION) {
        	  File file = fileChooser.getSelectedFile();
        	  if (!file.toString().endsWith(".mfa")) {
        	        String filename = file.toString()+".mfa";
        	        file = new File(filename);
        	  }
        	  ctrlModel.saveNetwork(file);
        	}
        }
    }
    class LoadListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	int example = loadExample();
        	if (example == 0) {
        		try {
//	        		URL url = MFAView.class.getResource("/resources/example.mfa");
	        		
//	        		File file = new File(url.toURI());
	        		File file = getFile();
	        		ctrlModel.loadNetwork(file);
        		} catch (Exception ex) {}
        	} else if (example == 1) {
	        	JFileChooser fileChooser = new JFileChooser();
	        	// add filter
	        	FileNameExtensionFilter mfaFilter = new FileNameExtensionFilter("mfa files (*.mfa)", "mfa");
	        	fileChooser.addChoosableFileFilter(mfaFilter);
	        	fileChooser.setFileFilter(mfaFilter);
	        	if (fileChooser.showOpenDialog(ctrlView) == JFileChooser.APPROVE_OPTION) {
	        	  File file = fileChooser.getSelectedFile();
	        	  ctrlModel.loadNetwork(file);
	        	}
        	} else {}
        }
    }

    //Option dialog to load demo example or file from file system
    private int loadExample() {
    	Object[] options = {"Load example", "Load file"};
    	int response = JOptionPane.showOptionDialog(ctrlView,
    	    "Would you like to load a standard example or an external file?",
    	    "Choose Source",
    	    JOptionPane.YES_NO_OPTION,
    	    JOptionPane.PLAIN_MESSAGE,
    	    null,
    	    options,
    	    options[1]);
    	return response;
    }
    
    
    private File getFile() {
    	File file = null;
        String resource = "/resources/example.mfa";
        URL res = getClass().getResource(resource);
        if (res.toString().startsWith("jar:")) {
            try {	//read file from jar package (see: http://stackoverflow.com/questions/941754/how-to-get-a-path-to-a-resource-in-a-java-jar-file)
                InputStream input = getClass().getResourceAsStream(resource);
                file = File.createTempFile("example", ".tmp");
                OutputStream out = new FileOutputStream(file);
                int read;
                byte[] bytes = new byte[1024];
                while ((read = input.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                out.close();
                file.deleteOnExit();
            } catch (IOException ex) {
            	ex.printStackTrace();
            }
        } else {
            //this will probably work in your IDE, but not from a JAR
            file = new File(res.getFile());
        }
        
        if (file != null && !file.exists()) {
            throw new RuntimeException("Error: File " + file + " not found!");
        }
        return file;
    }
}
