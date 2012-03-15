package com.intellileaf.dctheradir.enricher.model_processors;

import java.io.FileNotFoundException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.intellileaf.dctheradir.enricher.NS;
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
		Model model = ModelFactory.createDefaultModel();

	    BioMaterialTermSelector bmTermSel = new BioMaterialTermSelector ();
	    bmTermSel.setUri ( this.getUri () );
	    bmTermSel.run();
	    
	    PubMedTermSearch pubMedSearch = new PubMedTermSearch ();
	    pubMedSearch.setUri ( this.getUri () );
	    pubMedSearch.setTermLabels ( bmTermSel.getTermLabels () );
	    pubMedSearch.run ();
	    
	    // This contains a DCTHERA represenation of the publications found 
	  try 
	    {
			Utils.mergeGraphs ( pubMedSearch.getResultModel () );
		} 
	    catch (FileNotFoundException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
	    LLDPubMedTermEnricher lldEnricher = new LLDPubMedTermEnricher ();
	    lldEnricher.setPMIDs ( pubMedSearch.getPMIDs () );
	    lldEnricher.setUri(this.getUri());
	    lldEnricher.run(); 
	    
	    try 
	    {
			Utils.mergeGraphs( lldEnricher.getResultModel () );
		} 
	    catch (FileNotFoundException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    /*
	    model = lldEnricher.getResultModel().union(pubMedSearch.getResultModel());
	    model.add(pubMedSearch.getResultModel());
	    model.add(lldEnricher.getResultModel());
	    model.write(System.out, "TURTLE");
	    */
	    
	    /*UniprotEnricher uniProtEnricher = new UniprotEnricher ();
	    uniProtEnricher.setUri ( getUri () );
	    
	    try 
	    {
			Utils.mergeGraphs ( uniProtEnricher.getResultModel () );
		} 
	    catch (FileNotFoundException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
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
