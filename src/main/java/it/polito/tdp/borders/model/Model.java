package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {

	BordersDAO dao;
	private SimpleGraph <Country, DefaultEdge> grafo;
	private Map<Integer, Country> idMap;
	
	public Model() {
		
		dao = new BordersDAO();
		idMap = new HashMap<Integer, Country>();
		dao.loadAllCountries(idMap);
	}

	public List<Border> getCountryPairs(int anno) {
		return dao.getCountryPairs(anno, idMap);
	}
	
	public void creaGrafo(int anno) {
		grafo = new SimpleGraph <Country, DefaultEdge>(DefaultEdge.class);
		
		// Aggiungo i vertici che rispettano un vincolo 
		// --> infatti mi sono creata metodo apposito nel dao
		Graphs.addAllVertices(grafo, dao.getVertici(anno, idMap));
		
		// Aggiungo gli archi
		for (Border b: dao.getCountryPairs(anno, idMap)) {
			if(this.grafo.containsVertex(b.getStato1()) && 
					this.grafo.containsVertex(b.getStato2())){
						
				// recupero l'arco
				DefaultEdge e = this.grafo.getEdge(b.getStato1(), b.getStato2());
				
				// se l'arco non esiste già
				if (e == null) {
					
					// aggiungo l'arco che ha questi due vertici
					Graphs.addEdgeWithVertices(grafo, b.getStato1(), b.getStato2());
				}
			}
		}
		
	}
	public List<Country> getVertici (int anno, Map<Integer, Country> idMap){
		return dao.getVertici(anno, idMap);
	}
	
	
	
	public int getNumeroVertici() {
		if (this.grafo!=null) {
			return this.grafo.vertexSet().size();
		}
		return 0;
	}
	
	public int getNumeroArchi() {
		if (this.grafo!=null) {
			return this.grafo.edgeSet().size();
		}
		return 0;
	}
	
	/**
	 * METODO PER STAMPARE ELENCO DI VERTICI
	 * OGNUNO CON IL SUO GRADO (VERTICI ADIACENTI)
	 */
	public String getGradoVertici() {
		String s = "";
		for(Country c : this.grafo.vertexSet()) {
			s += c.getStateName() + ": " + this.grafo.degreeOf(c) + " stato/i confinante/i\n";
		}
		return s;
	}
	
	/**
	 * METODO PER TROVARE IL NUMERO DI COMPONENTI CONNESSE
	 * (CIOE' IL NUMERO DI SOTTO-GRAFI)
	 * --> ritorno .size() perchè voglio il numero delle componenti connesse
	 */
	public int getNumeroComponentiConnesse() {
		ConnectivityInspector<Country, DefaultEdge> ci = new ConnectivityInspector<>(grafo);
		return ci.connectedSets().size();
	}
}
