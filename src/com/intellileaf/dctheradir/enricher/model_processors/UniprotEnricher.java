package com.intellileaf.dctheradir.enricher.model_processors;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;

import java.io.FileNotFoundException;
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
                  
		String Sparql=
			"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
			"PREFIX uniprot: <http://purl.uniprot.org/core/> " +

				"SELECT distinct ?fullname ?term ?termLabel ?Organism " +
				"WHERE { " +
					   "<http://purl.uniprot.org/uniprot/P43355> " +
					   "  uniprot:classifiedWith ?term; " +
					   "  uniprot:recommendedName ?name . " +
					   "  ?name uniprot:fullName ?fullname . " +
				 	   "  ?term rdfs:label ?termLabel. " +
				      "} ";
		
		Query query = QueryFactory.create(Sparql);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(
		"http://www.uniprot.org/uniprot/?query=MAGE-3&sort=score&limit=5&format=rdf", query);
		
		ResultSet results = qexec.execSelect();
		
		ResultSetFormatter.out(System.out, results, query);
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
