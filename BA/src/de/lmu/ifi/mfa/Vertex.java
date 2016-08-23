package de.lmu.ifi.mfa;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.ListIterator;

public class Vertex implements Serializable {
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
	
	public Vertex(int id) {
		this.id = id;
		this.excess = 0;
		this.label = 0;
		this.layer = -1;
		neighbors = new LinkedList<Edge>();
		resNeighbors = null;
		this.predessors = 0;
		this.iteratorAugPath = 0;
		this.deadEnd = false;
	}
	
	public boolean addEdge(Vertex endVertex,int capacity) {
		if (!containsEdge(endVertex)) {
			neighbors.add(new Edge(this,endVertex,capacity));
			return true;
		} else {			
			return false;
		}
	}
	
	public boolean removeEdge(Vertex endVertex) {
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
	
	public void removeAllEdges() {
		ListIterator<Edge> listIterator = neighbors.listIterator();
		while (listIterator.hasNext()) {
			listIterator.next().getEndVertex().decrementPredessors();
		}
	}
	
	public boolean containsEdge(int vertexId) {
		ListIterator<Edge> listIterator = neighbors.listIterator();
		while (listIterator.hasNext()) {
			if (listIterator.next().getEndVertex().id() == vertexId) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsEdge(Vertex endVertex) {
		ListIterator<Edge> listIterator = neighbors.listIterator();
		while (listIterator.hasNext()) {
			if (listIterator.next().getEndVertex() == endVertex) {
				return true;
			}
		}
		return false;
	}
	
	public Edge getEdge(Vertex endVertex) {
		ListIterator<Edge> listIterator = neighbors.listIterator();
		while (listIterator.hasNext()) {
			Edge nextEdge = listIterator.next();
			if (nextEdge.getEndVertex() == endVertex) {
				return nextEdge;
			}
		}
		return null;
	}
	
	public LinkedList<Edge> getAllEdges() {
		return neighbors;
	}
	
	public LinkedList<Edge> getAllResEdges() {
		return resNeighbors;
	}
	
	public int id() {
		return this.id;
	}
	
	public void incrementPredessors() {
		this.predessors++;
	}
	
	public void decrementPredessors() {
		this.predessors--;
	}
	
	public int getPredessors() {
		return predessors;
	}
	
	public boolean resetFlow() {
		ListIterator<Edge> listIterator = neighbors.listIterator();
		while (listIterator.hasNext()) {
			Edge nextEdge = listIterator.next();
			if (!nextEdge.setFlow(0)) {
				return false;
			}
		}
		return true;
	}
	
	public int getOutFlow() {
		ListIterator<Edge> listIterator = neighbors.listIterator();
		int outFlow = 0;
		while (listIterator.hasNext()) {
			Edge nextEdge = listIterator.next();
			outFlow += nextEdge.getFlow();
		}
		return outFlow;
	}
	
	public int getInFlow() {
		ListIterator<Edge> listIterator = resNeighbors.listIterator();
		int inFlow = 0;
		while (listIterator.hasNext()) {
			Edge nextEdge = listIterator.next();
			inFlow += nextEdge.getFlow();
		}
		return inFlow;
	}
	
	public void setLayer(int layer) {
		this.layer = layer;
	}
	
	public int getLayer() {
		return this.layer;
	}
	
	public void resetLayer() {
		this.setLayer(-1);
	}
	
	public void setLabel(int label) {
		this.label = label;
	}
	
	public int getLabel() {
		return this.label;
	}
	
	public void resetLabel() {
		this.setLabel(0);
	}
	
	public void setExcess(int excess) {
		this.excess = excess;
	}
	
	public int getExcess() {
		return this.excess;
	}
	
	public void changeExcess(int deltaExcess) {
		this.excess += deltaExcess;
		this.increasedLabel = false;
	}
	
	public void resetExcess() {
		this.setExcess(0);
	}
	
	public void relabelVertex() {
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
	
	public void resetIncreasedLabel() {
		this.increasedLabel = false;
	}
	
	public boolean labelIncreased() {
		return increasedLabel;
	}
	
	public Vertex push_relabel() {
		Edge pushEdge = this.getNextEdge();
		Vertex newActiveVerex;
		if(pushEdge == null)
			System.out.println("FAILURE for vertex"+this.toString());
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
		return newActiveVerex;
	}
		
	public boolean isDead() {
		return this.deadEnd;
	}
	
	public void setDead(boolean isDead) {
		this.deadEnd = isDead;
	}
	
	public Edge getNextEdge() {
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
	
	public void setPreviousEdge() {
		if (!this.isDead()) {
			if (iteratorAugPath > 0 && iteratorAugPath <= neighbors.size()) {
				iteratorAugPath--;
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
	
	public void resetEdge() {
		this.iteratorAugPath = 0;
	}
	
	public void resetBlockings() {
		ListIterator<Edge> listIterator = neighbors.listIterator();
		while (listIterator.hasNext()) {
			listIterator.next().setBlocked(true);
		}
	}
	
	public void clearResNeighbors() {
		resNeighbors = new LinkedList<Edge>();
	}
	
	public void addEdgesToResGraph() {
		ListIterator<Edge> listIterator = neighbors.listIterator();
		while (listIterator.hasNext()) {
			Edge nextEdge = listIterator.next();
			nextEdge.getEndVertex().addResEdge(nextEdge);
		}
	}
	
	public void addResEdge(Edge resEdge) {
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
	
	public void printNeighbors() {
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
