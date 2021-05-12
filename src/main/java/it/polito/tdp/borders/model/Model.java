package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {

	BordersDAO dao;
	private SimpleGraph <Country, DefaultEdge> grafo;
	private Map<Integer, Country> idMap;
	private Map <Country, Country> visita;
	
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
	
	/**
	 * ESERCIZIO 2 --> VISITA DI UN GRAFO
	 * TROVO LA LISTA DI VERTICI RAGGIUNGIBILI A PARTIRE DA UN VERTICE
	 */
	public List<Country> trovaPercorso(Country c){
		
		BreadthFirstIterator<Country, DefaultEdge> bfv = new BreadthFirstIterator<>(grafo,c);
		visita = new HashMap<>();
		visita.put(c, null);
		
		bfv.addTraversalListener(new TraversalListener<Country, DefaultEdge>(){

			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> e) {
				// TODO Auto-generated method stub
				Country c1 = grafo.getEdgeSource(e.getEdge());
				Country c2 = grafo.getEdgeSource(e.getEdge());
	
					if (visita.containsKey(c1) && !visita.containsKey(c2)) {
						visita.put(c2, c1);
						
					}else if (visita.containsKey(c2) && !visita.containsKey(c1)) {
						visita.put(c1, c2);
					}
					
			}

			@Override
			public void vertexTraversed(VertexTraversalEvent<Country> e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void vertexFinished(VertexTraversalEvent<Country> e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		List<Country> percorso = new LinkedList<>();
		// SICCOME HO UN SOLO PARAMETRO DA CUI PARTIRE
		// PER TROVARE IL PERCORSO
		// TUTTE LE VOLTE CHE INCONTRO UN VERTICE LO AGGIUNGO
		while(bfv.hasNext()) {
			Country stato = bfv.next();
			percorso.add(stato);
		}
		
		return percorso;
		
		
	}

	public BordersDAO getDao() {
		return dao;
	}

	public void setDao(BordersDAO dao) {
		this.dao = dao;
	}

	public Map<Integer, Country> getIdMap() {
		return idMap;
	}

	public void setIdMap(Map<Integer, Country> idMap) {
		this.idMap = idMap;
	}
	
	
}
