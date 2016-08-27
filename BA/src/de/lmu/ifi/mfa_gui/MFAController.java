package de.lmu.ifi.mfa_gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import de.lmu.ifi.mfa.FlowNetwork;
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
     * ACHTUNG: Fehlerprüfung muss noch implementeirt werden
     */
    class AddVertexListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	int vertexId = ctrlView.getAddVertexId();
        	ctrlModel.addVertex(vertexId);
            //_view.setErgebnis(String.valueOf(_model.getWurzel()));
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
        	if (fileChooser.showSaveDialog(ctrlView) == JFileChooser.APPROVE_OPTION) {
        	  File file = fileChooser.getSelectedFile();
        	  ctrlModel.saveNetwork(file);
        	}
        }
    }
    class LoadListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	JFileChooser fileChooser = new JFileChooser();
        	if (fileChooser.showOpenDialog(ctrlView) == JFileChooser.APPROVE_OPTION) {
        	  File file = fileChooser.getSelectedFile();
        	  ctrlModel.loadNetwork(file);
        	}
        	
        }
    }
}
