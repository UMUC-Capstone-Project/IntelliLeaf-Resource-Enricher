package com.intellileaf.dctheradir.enricher.model_processors;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;


import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.shared.JenaException;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.intellileaf.dctheradir.enricher.Resources;
import com.intellileaf.dctheradir.enricher.NS;

/**
 * Enriches  
 *
 * <dl><dt>date</dt><dd>Feb 28, 2012</dd></dl>
 *
 */
public class UniprotEnricher extends ResourceEnricher
{
	private String uri;
	private List<String> termLabels;
	private String organism;
	private Model resultModel;
	private static OntModel uniprotOnt = null;
	
	/**
	 * @return the URI of the biomateral from which to pick up terms.
	 */
	public String getUri ()
	{
		return uri;
	}

	/**
	 * @return the URI of the biomateral from which to pick up terms.
	 */
	public void setUri ( String uri )
	{
		this.uri = uri;
	}

	
	public List<String> getTermLabels ()
	{
		return termLabels;
	}
	

	public void setTermLabels ( List<String> termLabels )
	{
		this.termLabels = termLabels;
	}
	
	public String getOrganism ()
	{
		return organism;
	}

	public void setOrganism( String organism)
	{
		this.organism = organism;
	}
	
	public Model getResultModel ()
	{
		return resultModel;
	}

	@Override
	public void run ()
	{
		// TODO Auto-generated method stub
		
		boolean testURI;
		String term;
		String org;
		String url;
		
		for (int i = 0; i < termLabels.size(); i++){
			
		  term = termLabels.get(i).replaceAll(" ","%20");
		  
		  
		  if (this.organism != null){
		 
		  org = organism.replaceAll(" ","%20"); 	  
			  
		  url = "http://www.uniprot.org/uniprot/?query=" + term + "%20" + org + "&sort=score&limit=5&format=rdf"; // termLabel is eg. 'MAGE-3'
		  uniprotOnt =  ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_TRANS_INF);
		  try{
		  uniprotOnt.read ( url );
		  testURI = true;
		  }catch(JenaException e){testURI = false;}
		
		  }
		  else{
			  
			  url = "http://www.uniprot.org/uniprot/?query=" + term + "&sort=score&limit=5&format=rdf"; // termLabel is eg. 'MAGE-3'
			  uniprotOnt =  ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_TRANS_INF);
			  try{
			  uniprotOnt.read (url);
			  testURI = true;
			  }catch(JenaException e){testURI = false;}
			  
		  }
		
		System.out.println(url);
		
		if (testURI == true){
			
		
			ExtendedIterator<Individual> itr = uniprotOnt.listIndividuals ( uniprotOnt.getOntClass ("http://purl.uniprot.org/core/Protein"));
			while (itr.hasNext ()){
			
			
				//test code
				Resource onode = itr.next ();
				
				System.out.println(onode.toString());
	
			}
		}
		
		}
	
	/*
		String Sparql=
			"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
			"PREFIX uniprot: <http://purl.uniprot.org/core/> " +

				"SELECT distinct ?fullname ?term ?termLabel ?Organism " +
				"WHERE { " +
					   "<http://purl.uniprot.org/uniprot/P43355> " +
					   "  uniprot:classifiedWith ?term." +
					   "  uniprot:recommendedName ?name." +
					   "  ?name uniprot:fullName ?fullname." +
				 	   "  ?term rdfs:label ?termLabel." +
				      "} ";
		
		Query query = QueryFactory.create(Sparql);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(
		"http://www.uniprot.org/uniprot/?query=MAGE-3&sort=score&limit=5&format=rdf", query);
		
		ResultSet results = qexec.execSelect();
		
		ResultSetFormatter.out(System.out, results, query);
	
	*/

	}
	/**
	 * @return an empty array, cause this enricher is supposed to be called directly and not to be used by a generic invoker
	 * that evaluates enrichers on the basis of its input. 
	 */
	@Override
	public String[] getSupportedUriTypes ()
	{
		return new String[] { "" };
	}

}
