package de.lmu.ifi.mfa;

import java.io.File;

public interface IFlowNetwork {

	//Vertex
	public void addVertex(int vertexId);
	public void removeVertex(int vertexId);
	//Edge
	public void addEdge(int vertexId1, int vertexId2, int capacity);
	public void removeEdge(int vertexId1, int vertexId2);
	//Source/Sink
	public void setSource(int sourceId);
	public void setSink(int sinkId);
	//Evaluate
	public void dinic();
	public void goldbergTarjan();
	//Control
	public void resetNetwork();
	public void saveNetwork(File file);
	public void loadNetwork(File file);
	//Output
	public String getPrompt();
	public String displayFlowNetwork();
	
}
