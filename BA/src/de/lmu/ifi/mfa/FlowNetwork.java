package de.lmu.ifi.mfa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Observable;

public class FlowNetwork extends Observable implements IFlowNetwork, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String NEWLINE = System.getProperty("line.separator");
	
	private int sourceId;
	private int sinkId;
	private Graph graph;
	private int maxFlow;
	private String prompt;
	
	
	public FlowNetwork() {
		this.sourceId = -1;
		this.sinkId = -1;
		this.graph = new Graph();
		this.maxFlow = 0;
		this.prompt = "";
	}
	
	
	public void setSource(int sourceId) {
		if (sourceId >= 0) {
			this.sourceId = sourceId;
			graph.addVertex(sourceId);
			prompt = "Source vertex set to be vertex "+sourceId+".";
		} else {
			prompt = "Source identifier has to be a valid vertex label.";
		}
		setChanged();
	    notifyObservers();	
	}
	
	public void setSink(int sinkId) {
		if (sinkId >= 0) {
			this.sinkId = sinkId;
			graph.addVertex(sinkId);
			prompt = "Sink vertex set to be vertex "+sinkId+".";
		} else {
			prompt = "Sink identifier has to be a valid vertex label.";
		}
		setChanged();
	    notifyObservers();	
	}
	
	public void addVertex(int vertexId) {
		if (vertexId >= 0) {
			boolean success = graph.addVertex(vertexId);
			if (success) {
				prompt = "Vertex "+vertexId+" added to Graph.";
			} else {
				prompt = "Vertex "+vertexId+" already exists in Graph.";
			}
		} else {
			prompt = "Vertex identifier has to be a valid vertex label.";
		}
		setChanged();
	    notifyObservers();
	}
	
	public void removeVertex(int vertexId) {
		if (vertexId >= 0) {
			boolean success = graph.removeVertex(vertexId);
			if (success) {
				if(vertexId == sourceId)
					sourceId = -1;
				if(vertexId == sinkId)
					sinkId = -1;
				prompt = "Vertex "+vertexId+" removed from Graph.";
			} else {
				prompt = "Vertex "+vertexId+" cannot be removed from Graph.";
			}
		} else {
			prompt = "Vertex identifier has to be a valid vertex label.";
		}
		setChanged();
	    notifyObservers();
	}
	
	public void addEdge(int vertexId1, int vertexId2, int capacity) {
		if (vertexId1 >= 0 && vertexId2 >= 0) {
			boolean success = graph.addEdge(vertexId1, vertexId2, capacity);
			if (success) {
				prompt = "Edge ("+vertexId1+","+vertexId2+") added to Graph.";
			} else {
				prompt = "Edge ("+vertexId1+","+vertexId2+") not added to Graph.";
			}
			
		} else {
			prompt = "Vertex identifiers has to be a valid vertex label.";
		}
		setChanged();
	    notifyObservers();
	}
	
	public void removeEdge(int vertexId1, int vertexId2) {
		if (vertexId1 >= 0 && vertexId2 >= 0) {
			boolean success = graph.removeEdge(vertexId1, vertexId2);
			if (success) {
				prompt = "Edge ("+vertexId1+","+vertexId2+") removed from Graph.";
			} else {
				prompt = "Edge ("+vertexId1+","+vertexId2+") not removed from Graph.";
			}
			
		} else {
			prompt = "Vertex identifiers have to be a valid vertex label.";
		}
		setChanged();
	    notifyObservers();
	}
	
	public void dinic() {
		maxFlow = 0;
		graph.resetFlow();
		graph.buildResidualGraph();
		int distance = graph.buildLayeredNetwork(sourceId, sinkId);
		int deltaFlow = 0;
		
		while (distance > 0) {
			boolean pathFound = graph.searchAugmentingPath(sourceId, sinkId);
			System.out.println("Path found: "+pathFound);
			if(pathFound) {
				deltaFlow = graph.updateMinFlowIncrement();
				maxFlow += deltaFlow;
				System.out.println("Inrease flow by "+deltaFlow);
			} else {
				distance = graph.buildLayeredNetwork(sourceId, sinkId);
			}
			System.out.println("Distance is "+distance);
			try {
				Thread.sleep(1);
			} catch (Exception ex) {}
		}
		
		this.prompt = "Dinic: maximum flow F="+maxFlow+".";
		setChanged();
	    notifyObservers();
//		return maxFlow;
	}
	
	public void goldbergTarjan() {
		maxFlow = 0;
		graph.resetFlow();
		graph.buildResidualGraph();
		graph.resetExcess(sourceId);
		graph.initializeLabels(sourceId);
		
		int queueLength = graph.initialPush(sourceId, sinkId);
		while (queueLength>0) {
			System.out.println("Queue length: "+queueLength);
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			queueLength = graph.dischargeQueue();
		}
		maxFlow = graph.getOutFlow(sourceId)-graph.getInFlow(sourceId);
		
		this.prompt = "Goldberg-Tarjan: maximum flow F="+maxFlow+".";
		setChanged();
	    notifyObservers();
//		return maxFlow;
	}
	
	public void clearFlow() {
		this.prompt = "Function not implemented yet.";
		setChanged();
	    notifyObservers();
	}
	
	public void resetNetwork() {
		this.sourceId = -1;
		this.sinkId = -1;
		this.graph = new Graph();
		this.maxFlow = 0;
		this.prompt = "Flow network reset.";
		setChanged();
	    notifyObservers();
	}
	
	public void loadNetwork(File file) {
		try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            FlowNetwork loadNetwork = (FlowNetwork)ois.readObject();
            ois.close();
            fis.close();
            this.sourceId = loadNetwork.sourceId;
    		this.sinkId = loadNetwork.sinkId;
    		this.graph = loadNetwork.graph;
    		this.maxFlow = loadNetwork.maxFlow;
            this.prompt = "Flow network loaded ("+file.getName()+").";
		} catch (FileNotFoundException ex) {
        	this.prompt = "Flow network not loaded (FileNotFoundException).";
        } catch (IOException e) {
        	this.prompt = "Flow network not loaded (IOException).";
        } catch (ClassNotFoundException e) {
        	this.prompt = "Flow network not loaded (ClassNotFoundException).";
        }    
		
		setChanged();
	    notifyObservers();
	}
	
	public void saveNetwork(File file) {
		try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
            fos.close();
            this.prompt = "Flow network saved ("+file.getName()+").";
        } catch (FileNotFoundException ex) {
        	this.prompt = "Flow network not saved (FileNotFoundException).";
        } catch (IOException ex) {
        	this.prompt = "Flow network not saved (IOException).";
        }    
		
		setChanged();
	    notifyObservers();
	}
	
	public String getPrompt() {
		return prompt;
	}
	
	/**
     * Returns a string representation of this graph.
     *
     * @return the source vertex <em>s</em>, followed by the sink vertex <em>t</em>,
     *         followed by all the vertices and their adjacency lists
     */
	public String displayFlowNetwork() {
		StringBuilder s = new StringBuilder();
		s.append("FLOW NETWORK" + NEWLINE);
		s.append("============" + NEWLINE);
		if (this.sourceId >= 0) {
			s.append("Source vertex: " + this.sourceId + NEWLINE);
		} else {
			s.append("Source vertex: -" + NEWLINE);
		}
		if (this.sinkId >= 0) {
			s.append("Sink vertex: " + this.sinkId + NEWLINE);
		} else {
			s.append("Sink vertex: -" + NEWLINE);
		}
		if (this.maxFlow > 0) {
			s.append("Maximum flow: " + this.maxFlow + NEWLINE);
		} else {
			s.append("Maximum flow: -" + NEWLINE);
		}
        s.append("---------------------------------------------------------------"+NEWLINE);
        s.append(graph.toString());
        
        return s.toString();
	}

    
	public void showGraph() {
		graph.plotGraph();
	}
}
