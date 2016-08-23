package de.lmu.ifi.mfa;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.ListIterator;

public class Graph implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String NEWLINE = System.getProperty("line.separator");
	
	private Map<Integer,Vertex> vertices;
	//For Dinic algorithm
	private LinkedList<Edge> augmentingPath;
	//For Goldberg-Tarjan algorithm
	private LinkedList<Vertex>  queue;
	private Vertex startVertex;
	private Vertex endVertex;
	
	public Graph() {
		this.vertices = new LinkedHashMap<Integer,Vertex>();
	}
	
	public boolean addVertex(int id) {
		if (!vertices.containsKey(id)) {
			vertices.put(id,new Vertex(id));
			return true;
		} else {			
			return false;
		}	
	}
	
	public boolean removeVertex(int id) {
		if (vertices.containsKey(id)) {
			if (vertices.get(id).getPredessors() == 0) {
				vertices.get(id).removeAllEdges();
				vertices.remove(id);
				return true;
			} else {
				return false;
			}		
		} else {
			return false;
		}	
	}
	
	public boolean containsVertex(int id) {
		return vertices.containsKey(id);
	}
	
	private Vertex getVertex(int id) {
		return vertices.get(id);
	}
	
	public boolean addEdge(int vertexId1, int vertexId2, int capacity) {
		this.addVertex(vertexId1);
		this.addVertex(vertexId2);
		Vertex startVertex = this.getVertex(vertexId1);
		Vertex endVertex = this.getVertex(vertexId2);
		startVertex.addEdge(endVertex,capacity);
		endVertex.incrementPredessors();
		return true;
	}
	
	public boolean removeEdge(int vertexId1, int vertexId2) {
		Vertex startVertex = this.getVertex(vertexId1);
		Vertex endVertex = this.getVertex(vertexId2);
		if (startVertex != null && endVertex != null) {
			boolean success = startVertex.removeEdge(endVertex);
			if (success) {
				endVertex.decrementPredessors();
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public boolean resetFlow() {
		for (Map.Entry<Integer, Vertex> entry : vertices.entrySet())
        {
			if (!entry.getValue().resetFlow()) {
				return false;
			}
        }
		return true;
	}
	
	public boolean resetExcess(int startVertexId) {
		for (Map.Entry<Integer, Vertex> entry : vertices.entrySet())
        {
			entry.getValue().resetExcess();
			if (entry.getValue().id() == startVertexId) {
				entry.getValue().setExcess(-1);
			}
        }
		return true;
	}
	
	public boolean initializeLabels(int startVertexId) {
		for (Map.Entry<Integer, Vertex> entry : vertices.entrySet())
        {
			entry.getValue().resetLabel();
			if (entry.getValue().id() == startVertexId) {
				entry.getValue().setLabel(this.vertices.size());
			}
        }
		return true;
	}
	
	
	public void buildResidualGraph() {
		for (Map.Entry<Integer, Vertex> entry : vertices.entrySet())
        {
			entry.getValue().clearResNeighbors();
			entry.getValue().setDead(false);
        }
		for (Map.Entry<Integer, Vertex> entry : vertices.entrySet())
        {
			entry.getValue().addEdgesToResGraph();
        }
	}
	
	//Dinic
	public int buildLayeredNetwork(int startVertexId, int endVertexId) {
		for (Map.Entry<Integer, Vertex> entry : vertices.entrySet())
        {
			entry.getValue().resetLayer();
			entry.getValue().resetBlockings();
			entry.getValue().resetEdge();
			entry.getValue().setDead(false);
        }
		int layerNbr = 0;
		Vertex startVertex = this.getVertex(startVertexId);
		Vertex endVertex = this.getVertex(endVertexId);
		Vertex tempVertex = null;
		LinkedList<Edge> tempEdges = null;
		LinkedList<Vertex> currentLayer = new LinkedList<Vertex>();
		LinkedList<Vertex> nextLayer = new LinkedList<Vertex>();
		
		startVertex.setLayer(0);	
		currentLayer.add(startVertex);
		
		while(!currentLayer.isEmpty()) {
			layerNbr++;
			while(!currentLayer.isEmpty()) {
				tempVertex = currentLayer.removeFirst();
				tempEdges = tempVertex.getAllEdges();		//check all edges
				ListIterator<Edge> listIterator = tempEdges.listIterator();
				while (listIterator.hasNext()) {
					Edge checkEdge = listIterator.next();
					if (checkEdge.getCapacity() > checkEdge.getFlow()) {
						Vertex checkVertex = checkEdge.getEndVertex();
						if (checkVertex.getLayer() == -1) {
							checkEdge.setBlocked(false);
							checkVertex.setLayer(layerNbr);
							checkVertex.setDead(false);
							nextLayer.add(checkVertex);	
						}
					}
				}
				tempEdges = tempVertex.getAllResEdges();	//check all residual edges
				listIterator = tempEdges.listIterator();
				while (listIterator.hasNext()) {
					Edge checkEdge = listIterator.next();
					if (0 < checkEdge.getFlow()) {
						Vertex checkVertex = checkEdge.getStartVertex();
						if (checkVertex.getLayer() == -1) {
							checkEdge.setBlocked(false);
							checkVertex.setLayer(layerNbr);
							checkVertex.setDead(false);
							nextLayer.add(checkVertex);	
						}
					}
				}
			}
			
			if (nextLayer.contains(endVertex)) {
				ListIterator<Vertex> listIterator = nextLayer.listIterator();
				while (listIterator.hasNext()) {
					listIterator.next().resetLayer();
				}
				endVertex.setLayer(layerNbr);
				return layerNbr;
			} else {
				currentLayer = nextLayer;
				nextLayer = new LinkedList<Vertex>();
			}
		}
		return -1;
	}
	
	public boolean searchAugmentingPath(int startVertexId, int endVertexId) {
		augmentingPath = new LinkedList<Edge>();
		Vertex activeVertex = getVertex(startVertexId);
		Vertex startVertex = getVertex(startVertexId);
		Vertex endVertex = getVertex(endVertexId);
		while(activeVertex != endVertex) {
			if(!activeVertex.isDead()) {
				Edge newEdge = activeVertex.getNextEdge();
				if(activeVertex == newEdge.getStartVertex()) {			//normal edge		//can be simplified???
					if (newEdge.getEndVertex().getLayer() == activeVertex.getLayer()+1) {
						activeVertex = newEdge.getEndVertex();
						augmentingPath.add(newEdge);
					}
				} else if(activeVertex == newEdge.getEndVertex()) {	//residual edge
					if (newEdge.getStartVertex().getLayer() == activeVertex.getLayer()+1) {
						activeVertex = newEdge.getStartVertex();
						augmentingPath.add(newEdge);
					}
				} else {
					//Failure
					System.out.println("FAILURE searchAugmentingPath");
				}
			} else {
				System.out.println("Dead vertex:"+activeVertex.toString());
				if (activeVertex == startVertex) {
					augmentingPath = null;
					return false;
				}
				Edge lastEdge = augmentingPath.removeLast();
				lastEdge.setBlocked(true);
				if(activeVertex == lastEdge.getEndVertex()) {			//normal edge
					activeVertex = lastEdge.getStartVertex();
				} else if(activeVertex == lastEdge.getStartVertex()) {	//residual edge
					activeVertex = lastEdge.getEndVertex();
				} else {
					//Failure
					System.out.println("FAILURE searchAugmentingPath");
				}
			}
		}
		return true;
	}
	
	public int updateMinFlowIncrement() {
		int deltaFlow = 0;
		if (augmentingPath.size()<1) {
			System.out.println("FAILURE in undateMinFlowIncrement");
			return 0;
		}
		ListIterator<Edge> listIterator = augmentingPath.listIterator();
		Edge currentEdge = listIterator.next();
		Vertex startVertex = currentEdge.getStartVertex();
		Vertex endVertex = currentEdge.getEndVertex();
		deltaFlow = currentEdge.getCapacity()-currentEdge.getFlow();
		while (listIterator.hasNext()) {
			currentEdge = listIterator.next();
			startVertex = endVertex;
			if (startVertex == currentEdge.getStartVertex()) {		//normal edge
				endVertex = currentEdge.getEndVertex();
				if (deltaFlow > currentEdge.getCapacity()-currentEdge.getFlow()) {
					deltaFlow = currentEdge.getCapacity()-currentEdge.getFlow();
				}
			} else if (startVertex == currentEdge.getEndVertex()) {	//residual edge
				endVertex = currentEdge.getStartVertex();
				if (deltaFlow > currentEdge.getFlow()) {
					deltaFlow = currentEdge.getFlow();
				}
			} else {
				//Failure
				System.out.println("FAILURE updateMinFlowIncrement");
			}
		}
		//DeleteLater
		System.out.println("Endvertex :"+endVertex.id());
		
		//Block edges, reset augmenting path and update flow
		listIterator = augmentingPath.listIterator();
		currentEdge = listIterator.next();
		startVertex = currentEdge.getStartVertex();
		endVertex = currentEdge.getEndVertex();
		if (deltaFlow == currentEdge.getCapacity()-currentEdge.getFlow()) {
			currentEdge.setBlocked(true);
			System.out.println("Edge blocked:"+currentEdge.toString());
		} else {
			startVertex.setPreviousEdge();
			System.out.println("Edge reset:"+currentEdge.toString());
		}
		currentEdge.setFlow(currentEdge.getFlow()+deltaFlow);
		while (listIterator.hasNext()) {
			currentEdge = listIterator.next();
			startVertex = endVertex;
			if (startVertex == currentEdge.getStartVertex()) {		//normal edge
				endVertex = currentEdge.getEndVertex();
				if (deltaFlow == currentEdge.getCapacity()-currentEdge.getFlow()) {
					currentEdge.setBlocked(true);
					System.out.println("Edge blocked:"+currentEdge.toString());
				} else {
					currentEdge.getStartVertex().setPreviousEdge();
					System.out.println("Edge reset:"+currentEdge.toString());
				}
				currentEdge.setFlow(currentEdge.getFlow()+deltaFlow);
			} else if (startVertex == currentEdge.getEndVertex()) {	//residual edge
				endVertex = currentEdge.getStartVertex();
				if (deltaFlow == currentEdge.getFlow()) {
					currentEdge.setBlocked(true);
					System.out.println("Edge blocked:"+currentEdge.toString());
				} else {
					currentEdge.getEndVertex().setPreviousEdge();
					System.out.println("Edge reset:"+currentEdge.toString());
				}
				currentEdge.setFlow(currentEdge.getFlow()-deltaFlow);
			} else {
				//Failure
				System.out.println("FAILURE updateMinFlowIncrement");
			}
		}
		augmentingPathToString();
		return deltaFlow;
	}
	
	
	//Goldberg-Tarjan
	public int initialPush(int startVertexId, int endVertexId) {
		startVertex = vertices.get(startVertexId);
		endVertex = vertices.get(endVertexId);
		queue = new LinkedList<Vertex>();
		LinkedList<Edge> startEdges = startVertex.getAllEdges();
		ListIterator<Edge> listIterator = startEdges.listIterator();
		while (listIterator.hasNext()) {
			Vertex newVertex = listIterator.next().pushFlowForward();
			if (newVertex != endVertex) {
				queue.add(newVertex);
			}
		}
		
		//return queue length
		return queue.size();
	}
	
	public int dischargeQueue() {
		//Discharge head vertex
		Vertex headVertex = queue.removeFirst();
		headVertex.resetEdge();
		while (headVertex.getExcess()>0 && !headVertex.labelIncreased()) {
			Vertex newVertex = headVertex.push_relabel();
			if (newVertex != null && newVertex != endVertex) {
				queue.add(newVertex);
			}
		}
		System.out.println("Vertex "+headVertex.id()+" - Excess: "+headVertex.getExcess());
		if (headVertex.getExcess() > 0) {
			headVertex.resetIncreasedLabel();
			queue.add(headVertex);
		}
		System.out.println(queue.toString());
		//return queue length
		return queue.size();
	}
	
	
	public int getOutFlow(int vertexId) {
		return vertices.get(vertexId).getOutFlow();
	}
	
	public int getInFlow(int vertexId) {
		return vertices.get(vertexId).getInFlow();
	}

	
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (Map.Entry<Integer, Vertex> entry : vertices.entrySet())
        {
			s.append(entry.getValue().toString()+NEWLINE);
        }
		return s.toString();
	}
	
	public void plotGraph() {
		for (Map.Entry<Integer, Vertex> entry : vertices.entrySet()) {
			entry.getValue().printNeighbors();
		}
	}
	
	//Auxilary function
	public void augmentingPathToString() {
		if (augmentingPath == null) {
			System.out.println("Empty path");
		} else {
			StringBuilder s = new StringBuilder();
			s.append("Augmenting Path: ");
			
			ListIterator<Edge> listIterator = augmentingPath.listIterator();
			while (listIterator.hasNext()) {
				s.append(listIterator.next().toString());
			}
			System.out.println(s.toString());
		}
	}
}
