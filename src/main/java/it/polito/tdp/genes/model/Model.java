package it.polito.tdp.genes.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;

public class Model {

	private GenesDao dao;
	private Graph <String, DefaultWeightedEdge> grafo;

	public Model() {
		dao = new GenesDao();
	}
	
	public void creaGrafo()
	{
		grafo = new SimpleWeightedGraph <>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, this.dao.getVertici());
		for(Archi a1 : this.dao.getArchi())
		{
			for(Archi a2 : this.dao.getArchi())
			{
				if(a1.getL1()==a2.getL2()&&a1.getL2()==a2.getL1())
				{
					int peso = a1.getPeso()+a2.getPeso();
					Graphs.addEdgeWithVertices(this.grafo, a1.getL1(), a1.getL2(), peso);
				}
				
			}
			Graphs.addEdgeWithVertices(this.grafo, a1.getL1(), a1.getL2(), a1.getPeso());
		}
	}
	
	public int getNVertici()
	{
		return this.grafo.vertexSet().size();
	}
	public int getNArchi()
	{
		return this.grafo.edgeSet().size();
	}
	
	public List<String> getVertici(){
		return this.dao.getVertici();
	}
	
	public List<Vicini>getVerticiConnessi(String l)
	{
		List<Vicini> result  = new ArrayList<Vicini>();
		
		ConnectivityInspector<String,DefaultWeightedEdge> ci = 
				new ConnectivityInspector<>(this.grafo);
		for(String ss : ci.connectedSetOf(l))
		{
			//System.out.println("sono qui1");
			DefaultWeightedEdge arco = this.grafo.getEdge(l, ss);
			//System.out.println("sono qui2");
			int peso = (int) this.grafo.getEdgeWeight(arco);
			System.out.println("sono qui3");
			result.add(new Vicini(ss,peso));
		}
		return result;
	}
}