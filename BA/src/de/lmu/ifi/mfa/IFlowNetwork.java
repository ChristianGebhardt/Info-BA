package de.lmu.ifi.mfa;

import java.io.File;
import java.util.LinkedList;

public interface IFlowNetwork {

	//Vertex
	void addVertex(int vertexId);
	void removeVertex(int vertexId);
	//Edge
	void addEdge(int vertexId1, int vertexId2, int capacity);
	void removeEdge(int vertexId1, int vertexId2);
	//Source/Sink
	void setSource(int sourceId);
	int getSource();
	void setSink(int sinkId);
	int getSink();
	//Evaluate
	int dinic();
	int goldbergTarjan();
	//Control
	void resetNetwork();
	void saveNetwork(File file);
	void loadNetwork(File file);
	//Output
	String getPrompt();
	String displayFlowNetwork();
	LinkedList<Integer[]> getGraphData();
	LinkedList<Integer> getVertexIndices();
	//Update and draw
	void updateGraph();
	boolean isUpdateGraph();
	void drawGraph();
	boolean isDrawGraph();
}
