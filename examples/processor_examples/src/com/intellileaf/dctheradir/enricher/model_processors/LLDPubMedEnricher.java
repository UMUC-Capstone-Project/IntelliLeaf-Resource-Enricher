package com.intellileaf.dctheradir.enricher.model_processors;

import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * Enrich publications with information found in Linked Life Data. 
 *
 * <dl><dt>date</dt><dd>Feb 29, 2012</dd></dl>
 *
 */
public class LLDPubMedEnricher implements KnowledgeBaseProcessor
{
	private Model resultModel;
	private List<Integer> pmids;

	public List<Integer> getPMIDs ()
	{
		return pmids;
	}

	public void setPMIDs ( List<Integer> pmids )
	{
		this.pmids = pmids;
	}
	
	/**
	 * a set of statements that assciate LLD-retrieved information to documents the returned publications 
	 * (eg, associated MESH terms). Uses the DCTHERA ontology for defining such statements. 
	 * This is populated by {@link #run()}. 
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

}
