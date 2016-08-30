import javax.swing.UIManager;

import de.lmu.ifi.mfa.FlowNetwork;
import de.lmu.ifi.mfa_gui.MFAController;
import de.lmu.ifi.mfa_gui.MFAView;

public class Main {

	static FlowNetwork myNetwork;
	static MFAController myController;
	static MFAView myView;
	
	public static void main(String[] args) {
		//Create Model
		myNetwork = new FlowNetwork();
		
		//Create View
		try {
	        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");	//windows appearance
	    } catch (Exception evt) {}
	    // Create an instance of the test application
	    myView = new MFAView(myNetwork);
		myNetwork.addObserver(myView);
		myController =new MFAController(myView, myNetwork);
		
	    
	    
	  
//	    myNetwork.addEdge(0,1,8,false);
//	    myNetwork.addEdge(0,2,1,false);
//	    myNetwork.addEdge(1,2,2,false);
//	    myNetwork.addEdge(1,3,4,false);
//	    myNetwork.addEdge(1,4,1,false);
//	    myNetwork.addEdge(2,4,4,false);
//	    myNetwork.addEdge(2,5,1,false);
//	    myNetwork.addEdge(3,2,1,false);
//	    myNetwork.addEdge(3,5,2,false);
//	    myNetwork.addEdge(4,5,6,false);
//	    myNetwork.setSource(0,false);
//	    myNetwork.setSink(5,false);
//	    myNetwork.updateGraph();
//	    myNetwork.drawGraph();

	}

}
