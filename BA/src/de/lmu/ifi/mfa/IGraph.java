package de.lmu.ifi.mfa;

public interface IGraph {

	//Vertex
	public boolean addVertex(int vertexId);
	public boolean removeVertex(int vertexId);
	public boolean containsVertex(int vertexId);
	//Edge
	public boolean addEdge(int vertexId1, int vertexId2, int capacity);
	public boolean removeEdge(int vertexId1, int vertexId2);
	//Evaluate (auxiliary functions)
	public boolean resetFlow();
	public boolean resetExcess(int startVertexId);
	public boolean initializeLabels(int startVertexId);
	public void buildResidualGraph();
	//Evaluate Dinic
	public int buildLayeredNetwork(int startVertexId, int endVertexId);
	public boolean searchAugmentingPath(int startVertexId, int endVertexId);
	public int updateMinFlowIncrement();
	//Evaluate Goldberg-Tarjan
	public int initialPush(int startVertexId, int endVertexId);
	public int dischargeQueue();
	//Get results
	public int getOutFlow(int vertexId);
	public int getInFlow(int vertexId);
	
}
