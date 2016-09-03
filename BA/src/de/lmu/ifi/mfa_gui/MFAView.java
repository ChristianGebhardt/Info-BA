package de.lmu.ifi.mfa_gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import de.lmu.ifi.mfa.IFlowNetwork;

/**
 *  The <tt>MFAView</tt> class is the view of a maximum flow algorithm program. 
 *  It is connected to a controller {@link MFAController}, that evaluates and forwards
 *  the user input to the flow network model {@link IFlowNetwork}. 
 *  The view and the controller build an exchangeable controller-view unit in the package <tt>mfa_gui</tt>.
 *  <p>
 *  The <tt>MFAView</tt> class is an implementation of {@link Observer}, which has to be attached to the model.
 *  This allows the retrieve update information and query the current status of the flow network from the model
 *  <p>
 *  For additional information about the program, see <a href="https://github.com/ChristianGebhardt/mfa">MFA</a>
 *  by Christian Gebhardt on Github.
 *  
 *
 * @author  Christian Gebhardt
 * @version 1.0.1
 * @since   2016-09-03
 */
public class MFAView extends JFrame implements Observer, ActionListener {

	private static final long serialVersionUID = 1L;
	private static final String NEWLINE = System.getProperty("line.separator");

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
	
	//Information
	private JTextArea txtPrompt;
	private JTextArea txtDisplay;
	
	//mxGraph
	private mxGraph graph;
	private mxGraphComponent graphComponent;
	private Map<Integer,Object> vertices;
	
	//Menu
	JMenuBar menuBar;
	JMenu info;
	JMenuItem help;
    JMenuItem about;
	
	//Screen
	private static final int MIN_WIDTH = 1600;
	private static final int MIN_HEIGHT = 800;
	
    /**
     * The constructor uses the flow network {@link IFlowNetwork} as observable to create a
     * view for this model. Therefore, the constructor packs all necessary swing
     * object on the a frame displays it as view of the program.
     *  
     * @param flowNet the model for the flow network of the program.
     */
    public MFAView(IFlowNetwork flowNet) {
    	//Model
    	this.myFlowNet = flowNet;
    	
    	//GUI
    	setTitle( "Maximum Flow Algorithm" );
        setBackground(Color.gray);
        
        //MenuBar
        menuBar = new JMenuBar();
        info = new JMenu("Help");
        menuBar.add(info);
        URL urlHelp = MFAView.class.getResource("/resources/questionmark16.png");
		ImageIcon helpIcon = new ImageIcon(urlHelp);
//        UIManager.getIcon(	"OptionPane.questionIcon")
        help = new JMenuItem("Help Contents", helpIcon);
        help.addActionListener(this);
        URL urlAbout = MFAView.class.getResource("/resources/lmu16.gif");
		ImageIcon aboutIcon = new ImageIcon(urlAbout);
        about = new JMenuItem("About MFA",aboutIcon);
        about.addActionListener(this);
        info.add(help);
        info.add(about);
        add(menuBar, BorderLayout.NORTH);
        
        //Scrennsize
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        if (dim.getWidth()<MIN_WIDTH || dim.getHeight()<MIN_HEIGHT) {
        	setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        	setMinimumSize(dim);
        } else {
        	setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        }
        System.out.println(dim);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setSize(1200, 800);
        
//        setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
//        setLocation(200, 200);
        System.out.println(this.getClass());//getResource("resources/lmu.gif")
//        ImageIcon img = new ImageIcon(this.getClass().getResource("resources/lmu.gif"));
        URL url = MFAView.class.getResource("/resources/lmu.gif");

        System.out.println(url);
        ImageIcon icon = new ImageIcon(url);
		add(new JLabel(icon));
		
        setIconImage(icon.getImage());
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
        
        //finally show view
        pack();
	    setLocationRelativeTo(null);	//set view in the middle of the screen
	    setVisible(true);
    }

    //Input Field
    private void createPanel1a(){
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
        txtAddVertexId.setToolTipText("vertex-ID (add)");
        vertexPanel.add(txtAddVertexId);
        cmdAddV = new JButton("Add Vertex");
        cmdAddV.setToolTipText("add vertex-ID to flow network");
        vertexPanel.add(cmdAddV);
        vertexPanel.add(Box.createRigidArea(new Dimension(10,0)));
        txtRemoveVertexId = new JTextField(3);
        txtRemoveVertexId.setToolTipText("vertex ID (remove)");
        vertexPanel.add(txtRemoveVertexId);
        cmdRemoveV = new JButton("Remove Vertex");
        cmdRemoveV.setToolTipText("remove vertex-ID from flow network");
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
        txtAddEdgeId1.setToolTipText("start-vertex ID (add edge)");
        edgePanel.add(txtAddEdgeId1);
        txtAddEdgeId2 = new JTextField(3);
        txtAddEdgeId2.setToolTipText("end-vertex ID (add edge)");
        edgePanel.add(txtAddEdgeId2);
        txtAddEdgeCap = new JTextField(3);
        txtAddEdgeCap.setToolTipText("capacity (add edge)");
        edgePanel.add(txtAddEdgeCap);
        cmdAddE = new JButton("Add Edge");
        cmdAddE.setToolTipText("add edge to flow network");
        edgePanel.add(cmdAddE);
        edgePanel.add(Box.createRigidArea(new Dimension(10,0)));
        txtRemoveEdgeId1 = new JTextField(3);
        txtRemoveEdgeId1.setToolTipText("start-vertex ID (remove edge)");
        edgePanel.add(txtRemoveEdgeId1);
        txtRemoveEdgeId2 = new JTextField(3);
        txtRemoveEdgeId2.setToolTipText("end-vertex ID (remove edge)");
        edgePanel.add(txtRemoveEdgeId2);
        cmdRemoveE = new JButton("Remove Edge");
        cmdRemoveE.setToolTipText("remove edge from flow network");
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
        txtSourceId.setToolTipText("source-vertex ID");
        source_sink.add(txtSourceId);
        cmdSetSource = new JButton("Set Source");
        cmdSetSource.setToolTipText("set source of flow network");
        source_sink.add(cmdSetSource);
        source_sink.add(Box.createRigidArea(new Dimension(10,0)));
        txtSinkId = new JTextField(3);
        txtSinkId.setToolTipText("sink-vertex ID");
        source_sink.add(txtSinkId);
        cmdSetSink = new JButton("Set Sink");
        cmdSetSink.setToolTipText("set sink of flow network");
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
        cmdDinic.setToolTipText("calculate maximum flow with Dinic algorithm");
        evaluate.add(cmdDinic);
        evaluate.add(Box.createRigidArea(new Dimension(10,0)));
        cmdGoldberg = new JButton("Goldberg-Tarjan");
        cmdGoldberg.setToolTipText("calculate maximum flow with Goldberg-Tarjan algorithm");
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
        cmdReset.setToolTipText("reset flow network to empty network");
        control.add(cmdReset);
        control.add(Box.createRigidArea(new Dimension(10,0)));
        cmdSave = new JButton("Save Flow Network");
        cmdSave.setToolTipText("save current flow network to file");
        control.add(cmdSave);
        control.add(Box.createRigidArea(new Dimension(10,0)));
        cmdLoad = new JButton("Load Flow Network");
        cmdLoad.setToolTipText("load flow network from demo example or from file");
        control.add(cmdLoad);
        inputMask.add(control);
        inputMask.add(Box.createRigidArea(new Dimension(0,25)));
        
        // Add everything
        panel1a.add(new JLabel( "Input:"), BorderLayout.NORTH);
        panel1a.add(inputMask,BorderLayout.CENTER);
    }

    //Output field
    private void createPanel1b(){
        panel1b = new JPanel();
        panel1b.setLayout(new BorderLayout());
        panel1b.setPreferredSize(new Dimension(500, 100));      
        
        //Add everything
        panel1b.add(new JLabel( "Output:"), BorderLayout.NORTH);
        txtPrompt  = new JTextArea(5,40);
        panel1b.add(txtPrompt, BorderLayout.CENTER);
    }

    //Display field
    private void createPanel2(){
        panel2 = new JPanel();
        panel2.setLayout(new BorderLayout());
        panel2.setPreferredSize( new Dimension(1000, 700));
        panel2.setMinimumSize( new Dimension(1000, 600));

        // Create Sub-Panels
        JPanel outputMask = new JPanel();
        outputMask.setLayout(new BoxLayout(outputMask, BoxLayout.PAGE_AXIS));
        outputMask.setOpaque(true);
        outputMask.setBackground(Color.WHITE);
        outputMask.add(Box.createRigidArea(new Dimension(0,25)));
        //Text area  
        txtDisplay = new JTextArea();
        txtDisplay.setText(helpMessage());
        JScrollPane txtOutputPanel = new JScrollPane(txtDisplay);	//initial help (might be removed again)
        txtOutputPanel.setPreferredSize(new Dimension(1000, 250));
        outputMask.add(txtOutputPanel);
        outputMask.add(Box.createRigidArea(new Dimension(0,10)));
        //Graphic area
        JPanel graphPanel = new JPanel();
        graphPanel.setLayout(new BorderLayout());
//        graphPanel.setLayout(new GridBagLayout());
        graphPanel.setOpaque(true);
        graphPanel.setBackground(Color.WHITE);
        graphPanel.setBorder(BorderFactory.createTitledBorder("Visualization"));
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        graph = new mxGraph();
        graphComponent = new mxGraphComponent(graph);
        graphComponent.setPreferredSize(new Dimension( 1000, 350 ));
        graphComponent.setEnabled(false);		//no moving of vertices/edge
        graphComponent.setConnectable(false);	//no connection of vertices
        graphPanel.add(graphComponent, BorderLayout.CENTER);
//        graphPanel.add(graphComponent, new GridBagConstraints());
//        Object parent = graph.getDefaultParent();
//        graph.getModel().beginUpdate();
//        try {
//                Object start = graph.insertVertex(parent, "start", "start", 100,
//                                100, 80, 30);
//                for (int i = 0; i < 5; i++) {
//                        Object a = graph.insertVertex(parent, "A" + i, "A" + i, 100,
//                                        100, 80, 30);
//                        graph.insertEdge(parent, null, "E" + i, start, a);
//
//                        Object b = graph.insertVertex(parent, "B" + i, "B" + i, 100,
//                                        100, 80, 30);
//                        graph.insertEdge(parent, null, "E" + i, a, b);
//                        start = a;
//                }
//        } finally {
//                graph.getModel().endUpdate();
//        }
//        morphGraph(graph, graphComponent);
        try {
			Thread.sleep(1);		//might be neclected
		} catch (Exception ex) {}
        this.revalidate();
        this.repaint();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        outputMask.add(graphPanel);
        outputMask.add(Box.createRigidArea(new Dimension(0,25)));
        
        panel2.add( new JLabel( "Display:" ), BorderLayout.NORTH );
        panel2.add(outputMask, BorderLayout.CENTER);
    }

    private void morphGraph(mxGraph graph, mxGraphComponent graphComponent) {
	    // define layout
//	    mxIGraphLayout layout = new mxFastOrganicLayout(graph);
    	mxHierarchicalLayout layout = new  mxHierarchicalLayout(graph);
    	layout.setOrientation(SwingConstants.WEST);
	    // layout using morphing
	    graph.getModel().beginUpdate();
	    try {
	            layout.execute(graph.getDefaultParent());
	    } finally {
//	            mxMorphing morph = new mxMorphing(graphComponent, 6, 1.5, 0);  //https://jgraph.github.io/mxgraph/docs/js-api/files/util/mxMorphing-js.html
////	    		mxMorphing morph = new mxMorphing(graphComponent);
//	            morph.addListener(mxEvent.DONE, new mxIEventListener() {
//	
//	                    @Override
//	                    public void invoke(Object arg0, mxEventObject arg1) {
//	                            graph.getModel().endUpdate();
//
//	                            // fitViewport();
//	                    }
//	
//	            });
//	            morph.startAnimation();
	    	graph.getModel().endUpdate();
	    	
	    	//Before you add a vertex/edge to graph, get the size of layout
	        double widthLayout = graphComponent.getLayoutAreaSize().getWidth();
	        double heightLayout = graphComponent.getLayoutAreaSize().getHeight();

	        //if you are done with adding vertices/edges,
	        //we need to determine the size of the graph

	        double width = graph.getGraphBounds().getWidth();
	        double height = graph.getGraphBounds().getHeight();

	        //set new geometry
	        graph.getModel().setGeometry(graph.getDefaultParent(), 
	                new mxGeometry((widthLayout - width)/2, (heightLayout - height)/2,
	                        widthLayout, heightLayout));
	    }
	
	}
   
   //Implement update reaction
   //TODO search for writing doc comments for overwritten functions
   
   public void update(Observable obs, Object obj) {
      if (obs == myFlowNet)
      {
         System.out.println(myFlowNet.getPrompt());
         if (myFlowNet.isUpdateGraph()) {
	         this.txtDisplay.setText(myFlowNet.displayFlowNetwork());
	         this.txtDisplay.setSelectionStart(0);	//Go to top of text area
	         this.txtDisplay.setSelectionEnd(0); 
	         this.updateGraph();
         }
         //display graph
         if (myFlowNet.isDrawGraph()) {
	         this.drawGraph();
//	         try {
//	 			Thread.sleep(500);
//	 		 } catch (Exception ex) {}
//	         this.revalidate();
//	         this.repaint();
         }
         this.txtPrompt.setText(myFlowNet.getPrompt());
      }
   }
   
   //Draw graph in panel
   private void drawGraph() {
	   LinkedList<Integer[]> edges = myFlowNet.getGraphData();
	   LinkedList<Integer> verticeIds = myFlowNet.getVertexIndices();
	   int startId = myFlowNet.getSource();
	   int endId = myFlowNet.getSink();
	   vertices = new HashMap<Integer,Object>();
	   
	   graph.removeCells(graph.getChildCells(graph.getDefaultParent()));
	   
	   graph.getModel().beginUpdate();
	   try {
		   //add vertices;
		   Object parent = graph.getDefaultParent();
		   ListIterator<Integer[]> listIterator = edges.listIterator();
			while (listIterator.hasNext()) {
				Integer[] nextEdge = listIterator.next();
				if (nextEdge.length > 0 && !vertices.containsKey(nextEdge[0])) {
					Object a;
					if (startId == nextEdge[0]) {
						a = graph.insertVertex(parent, nextEdge[0]+"", nextEdge[0]+"", 0, 0, 50, 30,"fillColor=green");
					} else if (endId == nextEdge[0]) {
						a = graph.insertVertex(parent, nextEdge[0]+"", nextEdge[0]+"", 0, 0, 50, 30,"fillColor=red");
					} else {
						a = graph.insertVertex(parent, nextEdge[0]+"", nextEdge[0]+"", 0, 0, 50, 30);
					}
					vertices.put(nextEdge[0], a);
				}
				if (nextEdge.length > 0 && !vertices.containsKey(nextEdge[1])) {
					Object a;
					if (endId == nextEdge[1]) {
						a = graph.insertVertex(parent, nextEdge[1]+"", nextEdge[1]+"", 0, 0, 50, 30,"fillColor=red");
					} else if (startId == nextEdge[1]) {
						a = graph.insertVertex(parent, nextEdge[1]+"", nextEdge[1]+"", 0, 0, 50, 30,"fillColor=green");
					} else {
						a = graph.insertVertex(parent, nextEdge[1]+"", nextEdge[1]+"", 0, 0, 50, 30);
					}
					vertices.put(nextEdge[1], a);
				}
			}
			//add edgeless vertices
			ListIterator<Integer> listIteratorV = verticeIds.listIterator();
			Object a;
			while (listIteratorV.hasNext()) {
				Integer vId = listIteratorV.next();
				if (!vertices.containsKey(vId)) {
					if (endId == vId) {
						a = graph.insertVertex(parent, vId+"", vId+"", 0, 0, 50, 30,"fillColor=red");
					} else if (startId == vId) {
						a = graph.insertVertex(parent, vId+"", vId+"", 0, 0, 50, 30,"fillColor=green");
					} else {
						a = graph.insertVertex(parent, vId+"", vId+"", 0, 0, 50, 30);
					}
					vertices.put(vId, a);
				}
			}
			//add edges;
		    listIterator = edges.listIterator();
			while (listIterator.hasNext()) {
				Integer[] nextEdge = listIterator.next();
				if (nextEdge.length > 0) {
					graph.insertEdge(parent, null, nextEdge[3]+"/"+nextEdge[2], vertices.get(nextEdge[0]), vertices.get(nextEdge[1]));
				} else {
					System.out.println("WRONG TUPEL FROM GRAPH");
				}
			}
	   } finally {
		   graph.getModel().endUpdate();
	   }
	   //rearrange components
	   morphGraph(graph, graphComponent);
   }
   
 //Draw graph in panel
   private void updateGraph() {
	   if (vertices != null) {
		   LinkedList<Integer[]> edges = myFlowNet.getGraphData();
		   System.out.println("UPDATE GRAPH");   
		   graph.getModel().beginUpdate();
		   try {
			   //remove edge and add again;
			   Object parent = graph.getDefaultParent();
			   ListIterator<Integer[]> listIterator = edges.listIterator();
				while (listIterator.hasNext()) {
					Integer[] nextEdge = listIterator.next();
					if (nextEdge.length > 0 && vertices.containsKey(nextEdge[0]) && vertices.containsKey(nextEdge[1])) {
						Object[] edgeList = graph.getEdgesBetween(vertices.get(nextEdge[0]), vertices.get(nextEdge[1]));
						if (edgeList.length > 0) {
							((mxGraphModel) graph.getModel()).remove(edgeList[0]);
							graph.removeCells(edgeList);
							graph.insertEdge(parent, null, nextEdge[3]+"/"+nextEdge[2], vertices.get(nextEdge[0]), vertices.get(nextEdge[1]));
						} else {
							System.out.println("EDGE LIST ZERO");
						}
					}
				}
	
		   } finally {
			   graph.getModel().endUpdate();
		   }
		   //rearrange components
		   morphGraph(graph, graphComponent);
	   } else {
		   System.out.println("VERTICES IS NULL");
	   }
   }
   
   //add action listener
   protected void setAddVertexListener(ActionListener l){
       this.cmdAddV.addActionListener(l);
   }
   protected void setRemoveVertexListener(ActionListener l){
       this.cmdRemoveV.addActionListener(l);
   }
   protected void setAddEdgeListener(ActionListener l){
       this.cmdAddE.addActionListener(l);
   }
   protected void setRemoveEdgeListener(ActionListener l){
       this.cmdRemoveE.addActionListener(l);
   }
   protected void setSourceListener(ActionListener l){
       this.cmdSetSource.addActionListener(l);
   }
   protected void setSinkListener(ActionListener l){
       this.cmdSetSink.addActionListener(l);
   }
   protected void setDinicListener(ActionListener l){
       this.cmdDinic.addActionListener(l);
   }
   protected void setGoldbergListener(ActionListener l){
       this.cmdGoldberg.addActionListener(l);
   }
   protected void setResetListener(ActionListener l){
       this.cmdReset.addActionListener(l);
   }
   protected void setSaveListener(ActionListener l){
       this.cmdSave.addActionListener(l);
   }
   protected void setLoadListener(ActionListener l){
       this.cmdLoad.addActionListener(l);
   }
   
   
   //return input values
   protected int getAddVertexId() {
	   try {
		   return Integer.parseInt(this.txtAddVertexId.getText());
	   } catch (Exception evt) {
		   return -1;
	   }
   }
   protected int getRemoveVertexId() {
	   try {
		   return Integer.parseInt(this.txtRemoveVertexId.getText());
	   } catch (Exception evt) {
		   return -1;
	   }
   }
   protected int getAddEdgeId1() {
	   try {
		   return Integer.parseInt(this.txtAddEdgeId1.getText());
	   } catch (Exception evt) {
		   return -1;
	   }
   }
   protected int getAddEdgeId2() {
	   try {
		   return Integer.parseInt(this.txtAddEdgeId2.getText());
	   } catch (Exception evt) {
		   return -1;
	   }
   }
   protected int getAddEdgeCap() {
	   try {
		   return Integer.parseInt(this.txtAddEdgeCap.getText());
	   } catch (Exception evt) {
		   return -1;
	   }
   }
   protected int getRemoveEdgeId1() {
	   try {
		   return Integer.parseInt(this.txtRemoveEdgeId1.getText());
	   } catch (Exception evt) {
		   return -1;
	   }
   }
   protected int getRemoveEdgeId2() {
	   try {
		   return Integer.parseInt(this.txtRemoveEdgeId2.getText());
	   } catch (Exception evt) {
		   return -1;
	   }
   }
   protected int getSourceId() {
	   try {
		   return Integer.parseInt(this.txtSourceId.getText());
	   } catch (Exception evt) {
		   return -1;
	   }
   }
   protected int getSinkId() {
	   try {
		   return Integer.parseInt(this.txtSinkId.getText());
	   } catch (Exception evt) {
		   return -1;
	   }
   }

	@Override
	//TODO search for writing doc comments for overwritten functions
	public void actionPerformed(ActionEvent object) {
		if (object.getSource() == help){
	        final JDialog frame = new JDialog(this, "Help Contents", true);
	        JTextArea helpText = new JTextArea(10,100);
	        String helpMsg = helpMessage();
	        helpText.setText(helpMsg);
	        helpText.setEditable(false);
	        frame.getContentPane().add(helpText);
	        frame.pack();
	        frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
	   }
	   if (object.getSource() == about){
	        final JDialog frame = new JDialog(this, "About MFA", true);
	        JTextArea helpText = new JTextArea(10,80);
	        String aboutMsg = aboutMessage();
	        helpText.setText(aboutMsg);
	        helpText.setEditable(false);
	        frame.getContentPane().add(helpText);
	        frame.pack();
	        frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
	   }	
	}
   
	private String helpMessage() {
		StringBuilder s = new StringBuilder();
		s.append("Getting Started" + NEWLINE);
		s.append("===============" + NEWLINE + NEWLINE);
		s.append("1. Create directed graph with <<Add Vertex>> and <<Add Edge>>" + NEWLINE);
		s.append("   > insert vertex identifier and edge capacity in text fields beside button and press button" + NEWLINE);
		s.append("   > correct graph with <<Remove Vertex>> and <<Remove Edge>> if necessary" + NEWLINE);
		s.append("2. Set source and sink vertex" + NEWLINE);
		s.append("   > insert vertex identifier in text fields and press button" + NEWLINE);
		s.append("3. Calculate maximum flow with <<Dinic>> or <<Goldberg-Tarjan>>" + NEWLINE);
		s.append("   > press respective button" + NEWLINE + NEWLINE);
		
		s.append("Further Features" + NEWLINE);
		s.append("================" + NEWLINE + NEWLINE);
		s.append("- Click on <<Reset Flow Network>> to set flow network to initial empty state" + NEWLINE);
		s.append("- Save current flow network to file system with <<Save Flow Network>>" + NEWLINE);
		s.append("- Load old flow network from demo example or file system with <<Load Flow Network>>" + NEWLINE);
		
		return s.toString();
	}
	
	private String aboutMessage() {
		StringBuilder s = new StringBuilder();
		s.append("Maximum Flow Algorithm Application" + NEWLINE + NEWLINE);
		s.append("Version: Ringberg Release (1.0.1)" + NEWLINE);
		s.append("Build id: 20160831-0100" + NEWLINE);
		s.append("(c) Copyright Chrisitan Gebhardt 2016. All rights reserved." + NEWLINE);
		s.append("This product includes software developed by other open source projects" + NEWLINE);
		s.append("including the  'Apache Software Foundation' and " + NEWLINE + "the 'JGraphX Swing Component - Java Graph Visualization Library'." + NEWLINE + NEWLINE);
		s.append("------------------------------------------------------------------------" + NEWLINE + NEWLINE);
		s.append("Contact Information" + NEWLINE + NEWLINE);
		s.append("Christian Gebhardt" + NEWLINE);
		s.append("Ludwigs-Maximilians-Universität München" + NEWLINE);
		s.append("Lenggrieser Str. 6" + NEWLINE);
		s.append("81371 München" + NEWLINE);
		s.append("Germany" + NEWLINE);
		s.append("Phone: +49 (0) 89 2180 3704" + NEWLINE);
		s.append("Email: gebhardt.christian@campus.lmu.de" + NEWLINE);

		return s.toString();
	}
}
