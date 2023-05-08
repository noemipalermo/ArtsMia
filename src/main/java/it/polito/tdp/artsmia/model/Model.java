package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	
	private Graph<ArtObject, DefaultWeightedEdge> graph;
	private List<ArtObject> allNodes;
	private ArtsmiaDAO dao;
	private Map<Integer, ArtObject> objectIdMap;
	
	
	
	public Model() {
		this.graph = new SimpleWeightedGraph<ArtObject, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.allNodes = new ArrayList<>();
		dao = new ArtsmiaDAO();
		this.objectIdMap = new HashMap<>();
		
	}
	
	private void loadNodes() {
		
//	 Verifico se ho già caricato i nodi nella lista
		if(this.allNodes.isEmpty())
			this.allNodes = dao.listObjects();
		
		if(this.objectIdMap.isEmpty()) {
			for(ArtObject a : this.allNodes )
				this.objectIdMap.put(a.getId(), a);
		}
		
	}
	
	public void buildGraph() {
		
		// Creazione grafo
		
		//1. Carico i nodi
		loadNodes();
		Graphs.addAllVertices(this.graph, this.allNodes);
		
		// Metodo 1 senza GROUP BY nel metodo del DAO -> Molto dispendioso perhcè fa 2 cicli for
		// SOLO SE IL DATABASE E' PICCOLO
		
//		for(ArtObject a1 : this.allNodes) {
//			for(ArtObject a2 : this.allNodes) {
//				int peso = dao.getWeight(a1.getId(), a2.getId());
//				// aggiungo i vertici con il peso corrispondente
//				Graphs.addEdgeWithVertices(this.graph, a1, a2, peso);
//			}
//		}
		

		// Metodo 2 con GROUP BY
		
		List<EdgeModel> allEdges = this.dao.getAllWeights(objectIdMap);
		for(EdgeModel e: allEdges) {
			Graphs.addEdgeWithVertices(this.graph, 
					e.getSource(), e.getTarget(), e.getWeight());
		}
		
		System.out.println("Il grafo continene "+this.graph.vertexSet().size()+ " nodi" );
		System.out.println("Il grafo continene "+this.graph.edgeSet().size()+ " archi" );
	}

	
	public Boolean isIdInGraph(Integer objId) {
		
		if(this.objectIdMap.get(objId)!=null) {
			return true;
		}else {
			return false;
		}
	}
	
	public Integer calcolaConnessa(Integer objId) {
		// Dati tutti i nodi connessi i sequenza, quanto è grande la componente connessa?
		
		// Esplora il grafo e da un set di nodi 
//		DepthFirstIterator<ArtObject, DefaultWeightedEdge> iterator = 
//				new DepthFirstIterator<>(this.graph, this.objectIdMap.get(objId));
//		
//		List<ArtObject> connected = new ArrayList<>();
//		
//		while(iterator.hasNext()) 
//			connected.add(iterator.next());
//		
		
//		return connected.size();
		
		
		// Prende un nodo e mi dice la dimensione della componente connessa
		ConnectivityInspector<ArtObject, DefaultWeightedEdge> inspector =
				new ConnectivityInspector<>(this.graph);
		Set<ArtObject> setConnected = inspector.connectedSetOf(this.objectIdMap.get(objId));

		return setConnected.size();
		
	}

	public boolean isGraphCreated() {
		if (this.graph.vertexSet().size()>0 )
			return true;
		else 
			return false;

	}
}
