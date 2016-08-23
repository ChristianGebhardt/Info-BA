import java.util.Observable;
import java.util.Observer;

import de.lmu.ifi.mfa.FlowNetwork;

public class TestObserver implements Observer {
	
	private FlowNetwork myFlowNet = null;
	
    public TestObserver(FlowNetwork flowNet) {
    	this.myFlowNet = flowNet;
    }
   
   public void update(Observable obs, Object obj) {
      if (obs == myFlowNet)
      {
         System.out.println(myFlowNet.getPrompt());
      }
   }
}
