package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	ItunesDAO dao;
	Graph<Track, DefaultEdge> graph;

	public Model() {
		this.dao = new ItunesDAO();
		this.graph = new SimpleGraph<>(DefaultEdge.class);
	}
	
	public List<Genre> genreAlfabetico(){
		return this.dao.genreAlfabetico();
	}
	
	public Integer getPlaylistByTrack(int trackId) {
		return this.dao.getPlaylistByTrack(trackId).size();
	}
	
	public Graph creaGrafo(int genreId, int min, int max) {
		this.graph = new SimpleGraph<>(DefaultEdge.class);
		
		Graphs.addAllVertices(this.graph, this.dao.getVertices(genreId, min, max));
		
		for(Track t1 : this.graph.vertexSet()) {
			for(Track t2 : this.graph.vertexSet()) {
				if(t1.getTrackId()!= t2.getTrackId()) {
					
					List<Playlist> lista1 = this.dao.getPlaylistByTrack(t1.getTrackId());
					List<Playlist> lista2 = this.dao.getPlaylistByTrack(t2.getTrackId());
					
					if(lista1.size()== lista2.size()) {
						Graphs.addEdgeWithVertices(this.graph, t1, t2);
					}
				}
			}
		}
		
		return this.graph;
	}
	
	public List<Set<Track>> calcolaConnessa() {
		
		DepthFirstIterator<Track, DefaultEdge> iterator = new DepthFirstIterator<>(this.graph);
		
		List<Track> compConnessa = new ArrayList();
		
		while(iterator.hasNext()) {
			compConnessa.add(iterator.next());
		}

		ConnectivityInspector<Track,DefaultEdge> inspector = new ConnectivityInspector<>(this.graph);
		List<Set<Track>> setConnesso = inspector.connectedSets();
		

		return setConnesso;
		

	}
	
	
}
