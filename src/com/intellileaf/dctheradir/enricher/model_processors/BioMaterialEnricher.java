package com.intellileaf.dctheradir.enricher.model_processors;

import com.intellileaf.dctheradir.enricher.Utils;

/**
 * 
 *  Does the enrichment of a Directory biomaterial. This contains a whole pipeline of several enrichers.
 *
 * <dl><dt>date</dt><dd>Feb 28, 2012</dd></dl>
 *
 */
public class BioMaterialEnricher extends ResourceEnricher
{

	@Override
	public void run ()
	{
    BioMaterialTermSelector bmTermSel = new BioMaterialTermSelector ();
    bmTermSel.setUri ( this.getUri () );
    bmTermSel.run();
    
    PubMedTermSearch pubMedSearch = new PubMedTermSearch ();
    pubMedSearch.setTermLabels ( bmTermSel.getTermLabels () );
    pubMedSearch.run ();

    LLDPubMedTermEnricher lldEnricher = new LLDPubMedTermEnricher ();
    lldEnricher.setPMIDs ( pubMedSearch.getPMIDs () );
    // or this version, see notes above: lldEnricher.setPubMedModel ( pubMedSearch.getPubMedModel() );
    lldEnricher.run(); 

    Utils.mergeGraphs( lldEnricher.getResultModel () );
    
    UniprotEnricher uniProtEnricher = new UniprotEnricher ();
    uniProtEnricher.setUri ( getUri () );
    
    Utils.mergeGraphs ( uniProtEnricher.getResultModel () );
	}

	/**
	 * returns {�"http://dc-research.eu/#dctheradir_492" }, ie, dcr:BioMaterial
	 */
	@Override
	public String[] getSupportedUriTypes ()
	{
		return new String[] { "http://dc-research.eu/#dctheradir_492" };
	}

}
