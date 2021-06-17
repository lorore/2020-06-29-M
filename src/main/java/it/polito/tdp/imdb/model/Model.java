package it.polito.tdp.imdb.model;

import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private Graph<Director, DefaultWeightedEdge> graph;
	private ImdbDAO dao;
	private Map<Integer, Director> idMap;
	private List<Director> soluzione;
	private double max;
	
	public Model() {
		dao=new ImdbDAO();
	}
	
	public String creaGrafo(Year anno) {
	graph=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	idMap=new HashMap<>();
	dao.getVertici(idMap, anno);
	Graphs.addAllVertices(graph, idMap.values());
	//System.out.println(graph.vertexSet().size());
	List<Adiacenza> archi=dao.getArchi(idMap, anno);
	for(Adiacenza a: archi) {
		Director d1=a.getD1();
		Director d2=a.getD2();
		DefaultWeightedEdge e=graph.getEdge(d1, d2);
		if(e==null) {
			Graphs.addEdge(graph, d1, d2, a.getPeso());
		}
	}
	//System.out.println(graph.edgeSet().size());
	String s="Num vertici: "+graph.vertexSet().size()+" num archi: "+graph.edgeSet().size();
return s;
	}
	
	public List<Director> getV(){
		List<Director> result=new ArrayList<>(graph.vertexSet());
		return result;
	}
	
	public List<RegistaAdiacente> getVicini(Director d){
		List<Director> vicini=Graphs.neighborListOf(graph, d);
		List<RegistaAdiacente> result=new ArrayList<>();
		for(Director v: vicini) {
			result.add(new RegistaAdiacente(v, graph.getEdgeWeight(graph.getEdge(v, d))));
		}
		
		Collections.sort(result);
		return result;
	}
	
	public List<Director> affini(Director d, double c){
		this.soluzione=new ArrayList<>();
		this.max=0.0;
		List<Director> parziale=new ArrayList<>();
		parziale.add(d);
		ricorsione(parziale, 1, c);
		if(this.soluzione.size()>0)
		return this.soluzione;
		
		return null;
	}
	
	private void ricorsione(List<Director> parziale, int livello, double c) {
		if(parziale.size()>=2) {
			if(this.getSommaPesiArchi(parziale)>c)
				return;
		}
		//condizione di terminazione
	
		Director ultimo=parziale.get(parziale.size()-1);
		List<Director> vicini=Graphs.neighborListOf(graph, ultimo);
		for(Director v: vicini) {
			if(!parziale.contains(v)) {
				parziale.add(v);
				if(parziale.size()>=2) {
					if(this.getSommaPesiArchi(parziale)<=c) {
						if(parziale.size()>this.max) {
							this.max=parziale.size();
							this.soluzione=new ArrayList<>(parziale);
							System.out.println(soluzione);
							return;
						}
						
					}
					}
				ricorsione(parziale, livello+1, c);
				parziale.remove(parziale.size()-1);
			}
		}
	}
	
	private double getSommaPesiArchi(List<Director> parziale) {
		double somma=0.0;
		
		for(int i=0; i<parziale.size()-1; i++) {
			somma+=graph.getEdgeWeight(graph.getEdge(parziale.get(i), parziale.get(i+1)));	
			
		}
		return somma;
		
	}
	
}
