package com.intellileaf.dctheradir.enricher.model_processors;

import java.util.List;

import com.intellileaf.dctheradir.enricher.Resources;

/**
 * 
 * Selects terms that characterise a Directory biomaterial. Inspect statements in {@link Resources#getDirectoryModel()} 
 * to do that. 
 *  
 * <dl><dt>date</dt><dd>Feb 28, 2012</dd></dl>
 *
 */
public class BioMaterialTermSelector implements KnowledgeBaseProcessor
{
	private List<String> termLabels;
	private String uri;
	
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
	
	/**
	 * @return the output found by {@link #run()}
	 */
	public List<String> getTermLabels ()
	{
		return termLabels;
	}


	@Override
	public void run ()
	{
		// TODO Auto-generated method stub


	}
	


	
}
