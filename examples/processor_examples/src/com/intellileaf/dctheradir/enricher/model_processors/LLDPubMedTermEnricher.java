package com.intellileaf.dctheradir.enricher.model_processors;

import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;

public class LLDPubMedTermEnricher implements KnowledgeBaseProcessor
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
