package de.lmu.ifi.mfa;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import org.junit.*;

public class FlowNetworkTest {

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
		//three different edges
		FlowNetwork testNetwork = new FlowNetwork();
		testNetwork.addEdge(0,1,1,false);
		testNetwork.addEdge(1,2,1,false);
		testNetwork.addEdge(2,3,1,false);
		assertEquals(3, testNetwork.getGraphData().size());
		
		//two equal edges
		testNetwork = new FlowNetwork();
		testNetwork.addEdge(0,1,1,false);
		testNetwork.addEdge(1,2,1,false);
		testNetwork.addEdge(1,2,1,false);
		assertEquals(2, testNetwork.getGraphData().size());
		
		//edge to the same vertex
		testNetwork = new FlowNetwork();
		testNetwork.addEdge(0,1,1,false);
		testNetwork.addEdge(1,2,1,false);
		testNetwork.addEdge(2,2,1,false);
		assertEquals(2, testNetwork.getGraphData().size());
	}

	
	@Test
	public void testDinic() {
		//circle in network
		FlowNetwork testNetwork = new FlowNetwork();
		testNetwork.addEdge(0,1,3,false);
		testNetwork.addEdge(1,2,3,false);
		testNetwork.addEdge(1,0,1,false);
		testNetwork.addEdge(2,1,1,false);
		testNetwork.setSource(0,false);
	    testNetwork.setSink(2,false);
		assertEquals(3, testNetwork.dinic());
		
		testNetwork = new FlowNetwork();
		testNetwork.addEdge(0,1,3,false);
		testNetwork.addEdge(2,1,1,false);
		testNetwork.setSource(0,false);
	    testNetwork.setSink(1,false);
		assertEquals(3, testNetwork.dinic());
		
		testNetwork = getBigNetwork();
		testNetwork.setSource(1,false);
	    testNetwork.setSink(12,false);
	    int flow = testNetwork.dinic();
	    System.out.println("Flow: "+flow);
	    testNetwork.saveNetwork(new File("C:/Users/gebha/Desktop/bigGraph.mfa"));
	    assertEquals(8, flow);
	}
	
	
	private FlowNetwork getBigNetwork() {
		List<String> lines;
		Scanner scanner;
		int startId; int endId; int capacity;
		FlowNetwork testNetwork = new FlowNetwork();
		try {
			lines = Files.readAllLines(Paths.get("C:/Users/gebha/Desktop/bigGraph.txt"), Charset.forName("UTF-8"));
			for(String line:lines){
				  System.out.println(line);
				  scanner = new Scanner(line);
				  startId = scanner.nextInt();
				  endId = scanner.nextInt();
				  capacity = scanner.nextInt();
				  testNetwork.addEdge(startId, endId, capacity);
				}
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		return testNetwork;
	}
}
