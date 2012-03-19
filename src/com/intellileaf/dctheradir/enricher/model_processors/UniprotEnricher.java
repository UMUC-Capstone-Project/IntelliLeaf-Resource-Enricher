package com.intellileaf.dctheradir.enricher.model_processors;

import com.hp.hpl.jena.rdf.model.Model;


/**
 * Enriches  
 *
 * <dl><dt>date</dt><dd>Feb 28, 2012</dd></dl>
 *
 */
public class UniprotEnricher extends ResourceEnricher
{
	private String uri;
	private String termLabels;
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

	
	public String getTermLabels ()
	{
		return termLabels;
	}

	public void setTermLabels ( String termLabels )
	{
		this.termLabels = termLabels;
	}
	
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
