package de.lmu.ifi.mfa;

import java.util.LinkedList;

interface IGraph {

	//Vertex
	boolean addVertex(int vertexId);
	boolean removeVertex(int vertexId);
	boolean containsVertex(int vertexId);
	//Edge
	boolean addEdge(int vertexId1, int vertexId2, int capacity);
	boolean removeEdge(int vertexId1, int vertexId2);
	//Evaluate (auxiliary functions)
	boolean resetFlow();
	boolean resetExcess(int startVertexId);
	boolean initializeLabels(int startVertexId);
	void buildResidualGraph();
	//Evaluate Dinic
	int buildLayeredNetwork(int startVertexId, int endVertexId);
	boolean searchAugmentingPath(int startVertexId, int endVertexId);
	int updateMinFlowIncrement();
	//Evaluate Goldberg-Tarjan
	int initialPush(int startVertexId, int endVertexId);
	int dischargeQueue();
	//Get results
	int getOutFlow(int vertexId);
	int getInFlow(int vertexId);
	LinkedList<Integer[]> getGraphData();
	LinkedList<Integer> getVertexIndices();
}
