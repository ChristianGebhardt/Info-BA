package de.lmu.ifi.mfa_gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.lmu.ifi.mfa.FlowNetwork;
import de.lmu.ifi.mfa.IFlowNetwork;

public class MFAView extends JFrame implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private IFlowNetwork myFlowNet = null;

	private     JSplitPane  splitPaneH;
	private     JSplitPane  splitPaneV;
	private     JPanel      panel1a;
	private     JPanel      panel1b;
	private     JPanel      panel2;

	//Vertex
	private JTextField txtAddVertexId;
	private JTextField txtRemoveVertexId;
	private JButton cmdAddV;
	private JButton cmdRemoveV;
	//Edge
	private JTextField txtAddEdgeId1;
	private JTextField txtAddEdgeId2;
	private JTextField txtAddEdgeCap;
	private JTextField txtRemoveEdgeId1;
	private JTextField txtRemoveEdgeId2;
	private JButton cmdAddE;
	private JButton cmdRemoveE;
	
	//Source/Sink
	private JTextField txtSourceId;
	private JTextField txtSinkId;
	private JButton cmdSetSource;
	private JButton cmdSetSink;
	
	//Evaluate
	private JButton cmdDinic;
	private JButton cmdGoldberg;
	
	//Evaluate
	private JButton cmdReset;
	private JButton cmdSave;
	private JButton cmdLoad;
		
	private JTextArea txtPrompt;
	private JTextArea txtDisplay;
	
    public MFAView(IFlowNetwork flowNet) {
    	//Model
    	this.myFlowNet = flowNet;
    	
    	//GUI
    	setTitle( "Maximum Flow Algorithm" );
        setBackground(Color.gray);
        //setSize(1200, 800);
        setLocation(300, 200);
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout( new BorderLayout() );
        getContentPane().add( topPanel );

        // Create the panels
        createPanel1a();
        createPanel1b();
        createPanel2();

        // Create a splitter pane
        splitPaneH = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
        //splitPaneH.setOneTouchExpandable(true); 	  //allows to minimize one side of the panel
        //splitPaneH.setDividerLocation(400);			//specify the location of the split pane (maximization removes setting)
        //splitPaneH.setDividerLocation(0.5);			//specify the location of the split pane (only works when pane is packed)
        topPanel.add( splitPaneH, BorderLayout.CENTER );

        splitPaneV = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
        splitPaneV.setLeftComponent(panel1a);
        splitPaneV.setRightComponent(panel1b);

        splitPaneH.setLeftComponent(splitPaneV);
        splitPaneH.setRightComponent(panel2);
    }

    //Input Field
    public void createPanel1a(){
        panel1a = new JPanel();
        panel1a.setLayout(new BorderLayout());
        panel1a.setPreferredSize(new Dimension( 500, 500));

        // Create Sub-Panels
        JPanel inputMask = new JPanel();
        inputMask.setLayout(new BoxLayout(inputMask, BoxLayout.PAGE_AXIS));
        inputMask.setOpaque(true);
        inputMask.setBackground(Color.WHITE);
        inputMask.add(Box.createRigidArea(new Dimension(0,25)));
        //Vertex
        JPanel vertexPanel = new JPanel();
        vertexPanel.setLayout(new FlowLayout());
        vertexPanel.setOpaque(true);
        vertexPanel.setBackground(Color.WHITE);
        vertexPanel.setBorder(BorderFactory.createTitledBorder("Vertex"));
        txtAddVertexId = new JTextField(3);
        vertexPanel.add(txtAddVertexId);
        cmdAddV = new JButton("Add Vertex");
        vertexPanel.add(cmdAddV);
        //vertexPanel.add(Box.createHorizontalGlue());
        vertexPanel.add(Box.createRigidArea(new Dimension(10,0)));
        txtRemoveVertexId = new JTextField(3);
        vertexPanel.add(txtRemoveVertexId);
        cmdRemoveV = new JButton("Remove Vertex");
        vertexPanel.add(cmdRemoveV);
        inputMask.add(vertexPanel);
        inputMask.add(Box.createRigidArea(new Dimension(0,10)));
        //Edge
        JPanel edgePanel = new JPanel();
        edgePanel.setLayout(new FlowLayout());
        edgePanel.setOpaque(true);
        edgePanel.setBackground(Color.WHITE);
        edgePanel.setBorder(BorderFactory.createTitledBorder("Edge"));
        txtAddEdgeId1 = new JTextField(3);
        edgePanel.add(txtAddEdgeId1);
        txtAddEdgeId2 = new JTextField(3);
        edgePanel.add(txtAddEdgeId2);
        txtAddEdgeCap = new JTextField(3);
        edgePanel.add(txtAddEdgeCap);
        cmdAddE = new JButton("Add Edge");
        edgePanel.add(cmdAddE);
        //vertexPanel.add(Box.createHorizontalGlue());
        edgePanel.add(Box.createRigidArea(new Dimension(10,0)));
        txtRemoveEdgeId1 = new JTextField(3);
        edgePanel.add(txtRemoveEdgeId1);
        txtRemoveEdgeId2 = new JTextField(3);
        edgePanel.add(txtRemoveEdgeId2);
        cmdRemoveE = new JButton("Remove Edge");
        edgePanel.add(cmdRemoveE);
        inputMask.add(edgePanel);
        inputMask.add(Box.createRigidArea(new Dimension(0,10)));
        //Source/Sink
        JPanel source_sink = new JPanel();
        source_sink.setLayout(new FlowLayout());
        source_sink.setOpaque(true);
        source_sink.setBackground(Color.WHITE);
        source_sink.setBorder(BorderFactory.createTitledBorder("Source / Sink"));
        txtSourceId = new JTextField(3);
        source_sink.add(txtSourceId);
        cmdSetSource = new JButton("Set Source");
        source_sink.add(cmdSetSource);
        //vertexPanel.add(Box.createHorizontalGlue());
        source_sink.add(Box.createRigidArea(new Dimension(10,0)));
        txtSinkId = new JTextField(3);
        source_sink.add(txtSinkId);
        cmdSetSink = new JButton("Set Sink");
        source_sink.add(cmdSetSink);
        inputMask.add(source_sink);
        inputMask.add(Box.createRigidArea(new Dimension(0,25)));
        //Evaluate
        JPanel evaluate = new JPanel();
        evaluate.setLayout(new FlowLayout());
        evaluate.setOpaque(true);
        evaluate.setBackground(Color.WHITE);
        evaluate.setBorder(BorderFactory.createTitledBorder("Evaluate"));
        cmdDinic = new JButton("Dinic");
        evaluate.add(cmdDinic);
        //vertexPanel.add(Box.createHorizontalGlue());
        evaluate.add(Box.createRigidArea(new Dimension(10,0)));
        cmdGoldberg = new JButton("Goldberg-Tarjan");
        evaluate.add(cmdGoldberg);
        inputMask.add(evaluate);
        inputMask.add(Box.createRigidArea(new Dimension(0,25)));
        //Control
        JPanel control = new JPanel();
        control.setLayout(new FlowLayout());
        control.setOpaque(true);
        control.setBackground(Color.WHITE);
        control.setBorder(BorderFactory.createTitledBorder("Control"));
        cmdReset = new JButton("Reset Flow Network");
        control.add(cmdReset);
        //vertexPanel.add(Box.createHorizontalGlue());
        control.add(Box.createRigidArea(new Dimension(10,0)));
        cmdSave = new JButton("Save Flow Network");
        control.add(cmdSave);
        //vertexPanel.add(Box.createHorizontalGlue());
        control.add(Box.createRigidArea(new Dimension(10,0)));
        cmdLoad = new JButton("Load Flow Network");
        control.add(cmdLoad);
        inputMask.add(control);
        inputMask.add(Box.createRigidArea(new Dimension(0,25)));
        
//        inputMask.add( new JButton( "South" ), BorderLayout.SOUTH );
//        inputMask.add( new JButton( "East" ), BorderLayout.EAST );
//        inputMask.add( new JButton( "West" ), BorderLayout.WEST );
//        inputMask.add( new JButton( "Center" ), BorderLayout.CENTER );
        
        // Add everything
        panel1a.add(new JLabel( "Input:"), BorderLayout.NORTH);
        panel1a.add(inputMask,BorderLayout.CENTER);
    }

    //Output field
    public void createPanel1b(){
        panel1b = new JPanel();
        panel1b.setLayout(new BorderLayout());
        panel1b.setPreferredSize( new Dimension( 500, 100 ) );
        
        panel1b.add(new JLabel( "Output:"), BorderLayout.NORTH);
        
        
        //panel1b.add(cmdAddV);
        txtPrompt  = new JTextArea(5,40);
        panel1b.add(txtPrompt, BorderLayout.CENTER);
    }

    //Display field
    public void createPanel2(){
        panel2 = new JPanel();
        panel2.setLayout( new BorderLayout() );
        panel2.setPreferredSize( new Dimension( 500, 600 ) );
        //panel2.setMinimumSize( new Dimension( 100, 50 ) );

        panel2.add( new JLabel( "Display:" ), BorderLayout.NORTH );
        txtDisplay = new JTextArea();
        panel2.add(txtDisplay, BorderLayout.CENTER );
    }

   
   //Implement update reaction
   public void update(Observable obs, Object obj) {
      if (obs == myFlowNet)
      {
         System.out.println(myFlowNet.getPrompt());
         this.txtPrompt.setText(myFlowNet.getPrompt());
         this.txtDisplay.setText(myFlowNet.displayFlowNetwork());
      }
   }
   
   //add action listener
   public void setAddVertexListener(ActionListener l){
       this.cmdAddV.addActionListener(l);
   }
   public void setRemoveVertexListener(ActionListener l){
       this.cmdRemoveV.addActionListener(l);
   }
   public void setAddEdgeListener(ActionListener l){
       this.cmdAddE.addActionListener(l);
   }
   public void setRemoveEdgeListener(ActionListener l){
       this.cmdRemoveE.addActionListener(l);
   }
   public void setSourceListener(ActionListener l){
       this.cmdSetSource.addActionListener(l);
   }
   public void setSinkListener(ActionListener l){
       this.cmdSetSink.addActionListener(l);
   }
   public void setDinicListener(ActionListener l){
       this.cmdDinic.addActionListener(l);
   }
   public void setGoldbergListener(ActionListener l){
       this.cmdGoldberg.addActionListener(l);
   }
   public void setResetListener(ActionListener l){
       this.cmdReset.addActionListener(l);
   }
   public void setSaveListener(ActionListener l){
       this.cmdSave.addActionListener(l);
   }
   public void setLoadListener(ActionListener l){
       this.cmdLoad.addActionListener(l);
   }
   
   
   //return input values
   public int getAddVertexId() {
	   try {
		   return Integer.parseInt(this.txtAddVertexId.getText());
	   } catch (Exception evt) {
		   return -1;
	   }
   }
   public int getRemoveVertexId() {
	   try {
		   return Integer.parseInt(this.txtRemoveVertexId.getText());
	   } catch (Exception evt) {
		   return -1;
	   }
   }
   public int getAddEdgeId1() {
	   try {
		   return Integer.parseInt(this.txtAddEdgeId1.getText());
	   } catch (Exception evt) {
		   return -1;
	   }
   }
   public int getAddEdgeId2() {
	   try {
		   return Integer.parseInt(this.txtAddEdgeId2.getText());
	   } catch (Exception evt) {
		   return -1;
	   }
   }
   public int getAddEdgeCap() {
	   try {
		   return Integer.parseInt(this.txtAddEdgeCap.getText());
	   } catch (Exception evt) {
		   return -1;
	   }
   }
   public int getRemoveEdgeId1() {
	   try {
		   return Integer.parseInt(this.txtRemoveEdgeId1.getText());
	   } catch (Exception evt) {
		   return -1;
	   }
   }
   public int getRemoveEdgeId2() {
	   try {
		   return Integer.parseInt(this.txtRemoveEdgeId2.getText());
	   } catch (Exception evt) {
		   return -1;
	   }
   }
   public int getSourceId() {
	   try {
		   return Integer.parseInt(this.txtSourceId.getText());
	   } catch (Exception evt) {
		   return -1;
	   }
   }
   public int getSinkId() {
	   try {
		   return Integer.parseInt(this.txtSinkId.getText());
	   } catch (Exception evt) {
		   return -1;
	   }
   }
   

}
