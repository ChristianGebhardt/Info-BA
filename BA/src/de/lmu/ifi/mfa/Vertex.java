package de.lmu.ifi.mfa;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.ListIterator;

class Vertex implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private int excess;
	private int label;
	private int layer;
	private LinkedList<Edge> neighbors;
	private LinkedList<Edge> resNeighbors;
	private int predessors;
	private int iteratorAugPath;
	private boolean deadEnd;
	private boolean increasedLabel;
	
	protected Vertex(int id) {
		this.id = id;
		this.excess = 0;
		this.label = 0;
		this.layer = -1;
		neighbors = new LinkedList<Edge>();
		resNeighbors = new LinkedList<Edge>();
		this.predessors = 0;
		this.iteratorAugPath = 0;
		this.deadEnd = false;
	}
	
	protected boolean addEdge(Vertex endVertex,int capacity) {
		if (!containsEdge(endVertex)) {
			neighbors.add(new Edge(this,endVertex,capacity));
			return true;
		} else {			
			return false;
		}
	}
	
	protected boolean addResEdge(Vertex startVertex) {
		if (startVertex.containsEdge(this)) {
			resNeighbors.add(startVertex.getEdge(this));
			return true;
		} else {			
			return false;
		}
	}
	
	protected boolean removeEdge(Vertex endVertex) {
		if (containsEdge(endVertex)) {
			Edge oldEdge = getEdge(endVertex);
			if (oldEdge != null) {
				neighbors.remove(oldEdge);
				return true;
			} else {
				return false;
			}
			
		} else {			
			return false;
		}
	}
	
	protected boolean removeResEdge(Vertex startVertex) {
		if (startVertex.containsEdge(this)) {
			Edge oldEdge = startVertex.getEdge(this);
			if (oldEdge != null) {
				resNeighbors.remove(oldEdge);
				return true;
			} else {
				return false;
			}
			
		} else {			
			return false;
		}
	}
	
	protected boolean removeAllEdges() {
		ListIterator<Edge> listIterator = neighbors.listIterator();
		boolean success = true;
		while (listIterator.hasNext()) {
//			listIterator.next().getEndVertex().decrementPredessors();
			success= success && listIterator.next().getEndVertex().removeResEdge(this);
		}
		return success;
	}
	
	protected boolean removeAllResEdges() {
		ListIterator<Edge> listIterator = resNeighbors.listIterator();
		boolean success = true;
		while (listIterator.hasNext()) {
//			listIterator.next().getEndVertex().decrementPredessors();
			success= success && listIterator.next().getStartVertex().removeEdge(this);
		}
		return success;
	}
	
	protected boolean containsEdge(int vertexId) {
		ListIterator<Edge> listIterator = neighbors.listIterator();
		while (listIterator.hasNext()) {
			if (listIterator.next().getEndVertex().id() == vertexId) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean containsEdge(Vertex endVertex) {
		ListIterator<Edge> listIterator = neighbors.listIterator();
		while (listIterator.hasNext()) {
			if (listIterator.next().getEndVertex() == endVertex) {
				return true;
			}
		}
		return false;
	}
	
	protected Edge getEdge(Vertex endVertex) {
		ListIterator<Edge> listIterator = neighbors.listIterator();
		while (listIterator.hasNext()) {
			Edge nextEdge = listIterator.next();
			if (nextEdge.getEndVertex() == endVertex) {
				return nextEdge;
			}
		}
		return null;
	}
	
	protected LinkedList<Edge> getAllEdges() {
		return neighbors;
	}
	
	protected LinkedList<Edge> getAllResEdges() {
		return resNeighbors;
	}
	
	protected int id() {
		return this.id;
	}
	
	protected void incrementPredessors() {
		this.predessors++;
	}
	
	protected void decrementPredessors() {
		this.predessors--;
	}
	
	protected int getPredessors() {
		return predessors;
	}
	
	protected boolean resetFlow() {
		ListIterator<Edge> listIterator = neighbors.listIterator();
		while (listIterator.hasNext()) {
			Edge nextEdge = listIterator.next();
			if (!nextEdge.setFlow(0)) {
				return false;
			}
		}
		return true;
	}
	
	protected int getOutFlow() {
		ListIterator<Edge> listIterator = neighbors.listIterator();
		int outFlow = 0;
		while (listIterator.hasNext()) {
			Edge nextEdge = listIterator.next();
			outFlow += nextEdge.getFlow();
		}
		return outFlow;
	}
	
	protected int getInFlow() {
		ListIterator<Edge> listIterator = resNeighbors.listIterator();
		int inFlow = 0;
		while (listIterator.hasNext()) {
			Edge nextEdge = listIterator.next();
			inFlow += nextEdge.getFlow();
		}
		return inFlow;
	}
	
	protected void setLayer(int layer) {
		this.layer = layer;
	}
	
	protected int getLayer() {
		return this.layer;
	}
	
	protected void resetLayer() {
		this.setLayer(-1);
	}
	
	protected void setLabel(int label) {
		this.label = label;
	}
	
	protected int getLabel() {
		return this.label;
	}
	
	protected void resetLabel() {
		this.setLabel(0);
	}
	
	protected void setExcess(int excess) {
		this.excess = excess;
	}
	
	protected int getExcess() {
		return this.excess;
	}
	
	protected void changeExcess(int deltaExcess) {
		this.excess += deltaExcess;
		this.increasedLabel = false;
	}
	
	protected void resetExcess() {
		this.setExcess(0);
	}
	
	protected void relabelVertex() {
		int newLabel = Integer.MAX_VALUE;
		ListIterator<Edge> listIterator = neighbors.listIterator();
		while (listIterator.hasNext()) {	//check normal edges
			Edge nextEdge = listIterator.next();
			if (nextEdge.getEndVertex().getLabel()+1 < newLabel && nextEdge.getCapacity()-nextEdge.getFlow() > 0) {
				newLabel = nextEdge.getEndVertex().getLabel()+1;
			}
		}
		listIterator = resNeighbors.listIterator();
		while (listIterator.hasNext()) {	//check residual edges
			Edge nextEdge = listIterator.next();
			if (nextEdge.getStartVertex().getLabel()+1 < newLabel && nextEdge.getFlow() > 0) {
				newLabel = nextEdge.getStartVertex().getLabel()+1;
			}
		}
		this.label = newLabel;
		this.increasedLabel = true;
		System.out.println("Relabel vertex "+this.id+" to "+this.label);
	}
	
	protected void resetIncreasedLabel() {
		this.increasedLabel = false;
	}
	
	protected boolean labelIncreased() {
		return increasedLabel;
	}
	
	protected Vertex push_relabel() {
		Edge pushEdge = this.getNextEdge();
		Vertex newActiveVerex;
		if(pushEdge == null) {
			System.out.println("FAILURE for vertex"+this.toString());
			this.relabelVertex();
			this.setDead(false);
			newActiveVerex = null;
		} else {
			if (this == pushEdge.getStartVertex()) {			
				if (pushEdge.getStartVertex().getLabel() == pushEdge.getEndVertex().getLabel()+1 && pushEdge.getCapacity()>pushEdge.getFlow()) {
					newActiveVerex = pushEdge.pushFlowForward();
				} else if (this.isDead()) {
					this.relabelVertex();
					this.setDead(false);
					newActiveVerex = null;
				} else {
					//Do nothing
					newActiveVerex = null;
				}
			} else {
				if (pushEdge.getEndVertex().getLabel() == pushEdge.getStartVertex().getLabel()+1 && 0<pushEdge.getFlow()) {
					newActiveVerex = pushEdge.pushFlowBackward();
				} else if (this.isDead()) {
					this.relabelVertex();
					this.setDead(false);
					newActiveVerex = null;
				} else {
					//Do nothing
					newActiveVerex = null;
				}
			}
		}
		return newActiveVerex;
	}
		
	protected boolean isDead() {
		return this.deadEnd;
	}
	
	protected void setDead(boolean isDead) {
		this.deadEnd = isDead;
	}
	
	protected Edge getNextEdge() {
		if (this.isDead()) {
			return null;
		} else {
			if (iteratorAugPath >= 0 && iteratorAugPath < neighbors.size()) {
				iteratorAugPath++;
				System.out.println(this.toString());
				System.out.println(iteratorAugPath);
				if (iteratorAugPath == neighbors.size() && resNeighbors.size() == 0) {
					this.setDead(true);
				}
				return neighbors.get(iteratorAugPath-1);
			} else if (iteratorAugPath == neighbors.size()) {
				iteratorAugPath = -1;	
				System.out.println(this.toString());
				System.out.println(iteratorAugPath);
				if(1 == resNeighbors.size()) {
					this.setDead(true);
					System.out.println("ATTENTION: Vertex "+this.id+" set dead.");
				}
				return resNeighbors.get(-1*iteratorAugPath-1);
			} else if (-1*iteratorAugPath < resNeighbors.size()) {
				iteratorAugPath--;
				System.out.println(this.toString());
				System.out.println(iteratorAugPath);
				return resNeighbors.get(-1*iteratorAugPath-2);
			} else {
				iteratorAugPath--;
				this.setDead(true);
				System.out.println(this.toString());
				System.out.println(iteratorAugPath);
				return resNeighbors.get(-1*iteratorAugPath-2);
			}
			
		}
	}
	
	protected void setPreviousEdge() {
		if (!this.isDead()) {
			if (iteratorAugPath > 0 && iteratorAugPath <= neighbors.size()) {
				iteratorAugPath--;
				System.out.println(this.toString());
				System.out.println(iteratorAugPath);
			} else if (iteratorAugPath == -1) {
				iteratorAugPath = neighbors.size();
			} else if (iteratorAugPath < -1) {
				iteratorAugPath++;
			} else {
				System.out.println("FAILURE RESET ITTERATOR"+iteratorAugPath);
			}	
		} else {
			this.setDead(false);
			if (resNeighbors.size() > 0) {
				iteratorAugPath = -1*resNeighbors.size();
			} else if (resNeighbors.size() > 0) {
				iteratorAugPath = neighbors.size()-1;
			}
			
		}
	}
	
	protected void resetEdge() {
		this.iteratorAugPath = 0;
	}
	
	protected void resetBlockings() {
		ListIterator<Edge> listIterator = neighbors.listIterator();
		while (listIterator.hasNext()) {
			listIterator.next().setBlocked(true);
		}
	}
	
	protected void clearResNeighbors() {
		resNeighbors = new LinkedList<Edge>();
	}
	
	protected void addEdgesToResGraph() {
		ListIterator<Edge> listIterator = neighbors.listIterator();
		while (listIterator.hasNext()) {
			Edge nextEdge = listIterator.next();
			nextEdge.getEndVertex().addResEdge(nextEdge);
		}
	}
	
	protected void addResEdge(Edge resEdge) {
		resNeighbors.add(resEdge);
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("Vertex "+id+" (label "+this.label+"):  ");
		ListIterator<Edge> listIterator = neighbors.listIterator();
		while (listIterator.hasNext()) {
			Edge nextEdge = listIterator.next();
			if (nextEdge.getEndVertex() != null) {
				s.append(nextEdge.toString()+"  ");
			}
		}

		return s.toString();
	}
	
	protected LinkedList<Integer[]> getEdgeData() {
		LinkedList<Integer[]> vertexEdges = new LinkedList<Integer[]>();
		//iterate over edges
		ListIterator<Edge> listIterator = neighbors.listIterator();
		while (listIterator.hasNext()) {
			Integer[] data = new Integer[4];
			Edge currEdge = listIterator.next();
			data[0] = currEdge.getStartVertex().id();
			data[1] = currEdge.getEndVertex().id();
			data[2] = currEdge.getCapacity();
			data[3] = currEdge.getFlow();
			vertexEdges.add(data);
		}		
		return vertexEdges;
	}
	
	@Deprecated
	protected void printNeighbors() {
		System.out.print("Vertex "+this.id+" - Neighbors: ");
		ListIterator<Edge> listIterator = neighbors.listIterator();
		while (listIterator.hasNext()) {
			Edge nextEdge = listIterator.next();
			if (nextEdge.getEndVertex() != null) {
				System.out.print(nextEdge.getEndVertex().id()+"  ");
			}
		}
		System.out.println();
	}
}
