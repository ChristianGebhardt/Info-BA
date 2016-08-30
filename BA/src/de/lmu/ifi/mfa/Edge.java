package de.lmu.ifi.mfa;

import java.io.Serializable;

class Edge implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Vertex startVertex;
	private Vertex endVertex;
	private int capacity;
	private int flow;
	private boolean blocked;
	
	protected Edge(Vertex startVertex, Vertex endVertex, int capacity) {
		this.startVertex = startVertex;
		this.endVertex = endVertex;
		this.capacity = capacity;
		this.flow = 0;
		this.blocked = false;
	}
	
	protected Edge(Vertex startVertex, Vertex endVertex, int capacity, int flow) {
		this.startVertex = startVertex;
		this.endVertex = endVertex;
		this.capacity = capacity;
		this.flow = flow;
		this.blocked = false;
	}
	
	protected Vertex getStartVertex() {
		return startVertex;
	}
	
	protected Vertex getEndVertex() {
		return endVertex;
	}
	
	protected int getCapacity() {
		return capacity;
	}
	
	protected int getFlow() {
		return flow;
	}
		
	protected boolean setFlow(int flow) {
		if (flow > this.capacity || flow < 0) {
			return false;
		} else {
			this.flow = flow;
			return true;
		}
	}
	
	protected boolean isBlocked() {
		return blocked;
	}
	
	protected void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
	
	protected Vertex pushFlowForward() {	//to push in edge direction
		int previousExcess = endVertex.getExcess();
		int deltaFlow = 0;
		if(this.getStartVertex().getExcess() == -1) {	//initial push start vertex
			deltaFlow =capacity-flow;
			flow = capacity;
		} else {
			if (capacity-flow <= startVertex.getExcess()) {	//saturating push
				deltaFlow = capacity-flow;
			} else {										//non-saturating push
				deltaFlow =startVertex.getExcess();
				startVertex.setPreviousEdge();
			}
			flow += deltaFlow;
			startVertex.changeExcess(-deltaFlow);
		}
		endVertex.changeExcess(deltaFlow);
		System.out.println("Push-Forward "+deltaFlow+" on edge"+this.toString());
		if (previousExcess == 0 && deltaFlow > 0) {
			endVertex.setDead(false);
			return endVertex;
		}
		else {
			return null;
		}
	}
	
	protected Vertex pushFlowBackward() {	//to push in reverse edge direction
		int previousExcess = startVertex.getExcess();
		int deltaFlow = 0;
		if (flow <= endVertex.getExcess()) {	//saturating push
			deltaFlow = flow;
		} else {										//non-saturating push
			deltaFlow =endVertex.getExcess();
			startVertex.setPreviousEdge();
		}
		flow -= deltaFlow;
		if(startVertex.getExcess() >= 0) {	//avoid excess change on start vertex
			startVertex.changeExcess(deltaFlow);
		}
		endVertex.changeExcess(-deltaFlow);
		System.out.println("Push-Backward "+deltaFlow+" on edge"+this.toString());
		if (previousExcess == 0 && deltaFlow > 0) {
			startVertex.setDead(false);
			return startVertex;
		}
		else {
			return null;
		}
	}
	
	public String toString() {
		return "("+startVertex.id()+","+endVertex.id()+",c:"+capacity+",f:"+flow+")";
	}
}
