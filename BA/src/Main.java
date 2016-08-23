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
	        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	    } catch (Exception evt) {}
	    // Create an instance of the test application
	    myView = new MFAView(myNetwork);
		myNetwork.addObserver(myView);
		myController =new MFAController(myView, myNetwork);
	    
	    myView.pack();
	    myView.setVisible( true );
	    
	    
	    try {
	        Thread.sleep(2000);
	    } catch(InterruptedException ex) {
	        Thread.currentThread().interrupt();
	    }
	    myNetwork.addEdge(0,1,8);
	    myNetwork.addEdge(0,2,1);
	    myNetwork.addEdge(1,2,2);
	    myNetwork.addEdge(1,3,4);
	    myNetwork.addEdge(1,4,1);
	    myNetwork.addEdge(2,4,4);
	    myNetwork.addEdge(3,2,1);
	    myNetwork.addEdge(3,5,2);
	    myNetwork.addEdge(4,5,6);
	    
	    myNetwork.setSource(0);
	    myNetwork.setSink(5);

	}

}
