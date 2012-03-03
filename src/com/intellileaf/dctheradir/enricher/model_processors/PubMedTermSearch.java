package com.intellileaf.dctheradir.enricher.model_processors;

import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * 
 * Uses PUBMED to search those publications that are related to a set of terms.
 *
 * <dl><dt>date</dt><dd>Feb 28, 2012</dd></dl>
 *
 */
public class PubMedTermSearch extends ResourceEnricher
{
	private List<String> termLabels;
	private List<Integer> pmids;
	private Model resultModel;
	
	
	/**
	 * The terms to be searched.
	 */
	public List<String> getTermLabels ()
	{
		return termLabels;
	}

	
	/**
	 * The terms to be searched.
	 */
	public void setTermLabels ( List<String> termLabels )
	{
		this.termLabels = termLabels;
	}

	/**
	 * What is returned by {@link #run()}. 
	 */
	public List<Integer> getPMIDs ()
	{
		return pmids;
	}
	
	/**
	 * a set of statements that characterise the returned publications (eg, the tile, authors, etc). Uses the
	 * DCTHERA ontology for defining such statements. Moreover, it links the found publications to the URI passed
	 * via {@link #setUri(String)}. This is populated by {@link #run()}. 
	 *  
	 */
	public Model getResultModel ()
	{
		return resultModel;
	}

	@Override
	public void run ()
	{
		// TODO Auto-generated method stub

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
