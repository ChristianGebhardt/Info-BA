package de.lmu.ifi.mfa;

import static org.junit.Assert.*;

import org.junit.*;

class FlowNetworkTest {

	@Test
	public void exampleNet() {
		FlowNetwork testNetwork = new FlowNetwork();
	    testNetwork.addEdge(0,1,8,false);
	    testNetwork.addEdge(0,2,1,false);
	    testNetwork.addEdge(1,2,2,false);
	    testNetwork.addEdge(1,3,4,false);
	    testNetwork.addEdge(1,4,1,false);
	    testNetwork.addEdge(2,4,4,false);
	    testNetwork.addEdge(2,5,1,false);
	    testNetwork.addEdge(3,2,1,false);
	    testNetwork.addEdge(3,5,2,false);
	    testNetwork.addEdge(4,5,6,false);
	    testNetwork.setSource(0,false);
	    testNetwork.setSink(5,false);
	    assertEquals(7, testNetwork.dinic());
	    assertEquals(7, testNetwork.goldbergTarjan());
	}
	
	
	@Test
	public void testAddEdge() {
		FlowNetwork testNetwork = new FlowNetwork();
		testNetwork.addEdge(0,1,1,false);
		testNetwork.addEdge(1,2,1,false);
		testNetwork.addEdge(2,3,1,false);
		assertEquals(3, testNetwork.getGraphData().size());
		
		testNetwork = new FlowNetwork();
		testNetwork.addEdge(0,1,1,false);
		testNetwork.addEdge(1,2,1,false);
		testNetwork.addEdge(1,2,1,false);
		assertEquals(2, testNetwork.getGraphData().size());
		
		testNetwork = new FlowNetwork();
		testNetwork.addEdge(0,1,1,false);
		testNetwork.addEdge(1,2,1,false);
		testNetwork.addEdge(2,2,1,false);
		assertEquals(2, testNetwork.getGraphData().size());
	}

}
