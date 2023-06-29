package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private FoodDao dao;
	private Graph<String, DefaultWeightedEdge> grafo;
	private List<String> vertici;
	private List<Arco> archi;
	
	//ricorsione variabili
	private List<String> cammino;
	private int pesoMax;
	
	
	public Model() {
		dao  = new FoodDao();
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.archi = new ArrayList<Arco>();
		
	}
	
	public void loadNodes(double calorie) {
		
		if (this.vertici.isEmpty()) {
			this.vertici = dao.getVertici(calorie);
		}
	}
	
	public void clearGraph() {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.vertici = new ArrayList<>();
	}
	
	
	public void creaGrafo(double calorie) {
		
		clearGraph();
		loadNodes(calorie);
		
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		//archi 
		if (this.archi.isEmpty()) {
			this.archi = dao.getArchi(calorie);
		}
		
		for (Arco a : this.archi) {
			
			Graphs.addEdgeWithVertices(this.grafo, a.getSource(), a.getTarget(), a.getPeso());
		}
		
		
	}
	
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Adiacente> getAdiacenti(String nodo) {
		
		List<Adiacente> adiacenti = new ArrayList<Adiacente>();
		
		for (String vicini : Graphs.neighborListOf(this.grafo, nodo)) {
			double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(vicini, nodo));
			adiacenti.add(new Adiacente(vicini, (int) peso));
		}
		
		
		return adiacenti;
	}

	public List<String> getVertici() {
		// TODO Auto-generated method stub
		return this.vertici;
	}
	
	
	//PUNTO 2 : RICORSIONE
	
	//metodo di inizializzazione 
	public List<String> getCammino(int N, String nodo) {
		
		this.pesoMax = 0;
		//inizializzare il cammino e creare la lista delle soluzioni parziali
		this.cammino = new ArrayList<String>();
		List<String> parziale = new ArrayList<String>();
		parziale.add(nodo);
		
		//soluzioni possibili --> tutti i nodi vicini al nodo selezionato in input
		List<String> solPossibili = Graphs.neighborListOf(this.grafo, nodo);
		
		//invocare metodo ricorsivo
		ricorsione(parziale, solPossibili, N, 1);
		
		return cammino;
		
	}
	
	private void ricorsione(List<String> parziale, List<String> soluzioniPaossibili, int N, int livello) {
		
		//condizioni terminazione
		if (livello == N) {
			
			//contorllo da fare su peso massimo (!! IN QUESO PUNTO E NON IN ALTRI )
			int peso = getPesoParziale(parziale);
			
			if (peso > this.pesoMax) {
				this.pesoMax = peso;
				this.cammino = new ArrayList<String>(parziale);
			}
			
			return;
		}
	
			
		for (String nodo : soluzioniPaossibili) {
			if (!parziale.contains(nodo)) {
				parziale.add(nodo);
				
				//creare una nuova lista di nodi adiacenti
				List<String> nuoviAdiacenti = Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1));
				
				//invocare la ricorsione
				ricorsione(parziale, nuoviAdiacenti, N, livello + 1);
				
				//backTrackin
				parziale.remove(parziale.size()-1);
			}
		}
		
	}
	
	public int getPesoParziale(List<String> parziale) {
		
		int peso  =0;
		
		for (int i =1; i < parziale.size(); i++) {
			
			double nodoArco = this.grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(i-1), parziale.get(i)));
			
			peso += (int)nodoArco;
		}
		return peso;
	}
	
	public int getPesoMax() {
		return this.pesoMax;
	}
	
}
