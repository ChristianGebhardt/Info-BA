
import de.lmu.ifi.mfa.*;

public class testClass {

//	static Graph testGraph;
	static FlowNetwork myNetwork;
	static TestObserver myObserver;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		testGraph = new Graph();
//		testGraph.addVertex(0);
//		testGraph.addVertex(2);
//		testGraph.addVertex(3);
//		testGraph.addVertex(6);
//		
//		testGraph.plotGraph();
		
		myNetwork = new FlowNetwork();
		myObserver = new TestObserver(myNetwork);
		myNetwork.addObserver(myObserver);
		myNetwork.setSource(1);
		myNetwork.addVertex(2);
		myNetwork.removeVertex(3);
		myNetwork.removeVertex(2);
		myNetwork.addEdge(2, 3, 1);
		myNetwork.addEdge(2, 5, 1);
		myNetwork.addEdge(3, 5, 1);
		myNetwork.addEdge(7, 8, 1);
		myNetwork.showGraph();
		myNetwork.removeEdge(3, 5);
		myNetwork.removeEdge(2, 5);
		myNetwork.removeVertex(5);
		myNetwork.showGraph();
		myNetwork.resetNetwork();
		myNetwork.addEdge(2, 3, 1);
		myNetwork.addEdge(2, 5, 1);
		myNetwork.showGraph();
	}
}
