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
		
		System.out.print("Retrieving keywords from " + getUri() + " .....");
	    BioMaterialTermSelector bmTermSel = new BioMaterialTermSelector ();
	    bmTermSel.setUri ( this.getUri () );
	    bmTermSel.run();
	    System.out.println("Complete");
	    
	    System.out.print("Retrieving PubMed IDs and result file elements.....");
	    PubMedTermSearch pubMedSearch = new PubMedTermSearch ();
	    pubMedSearch.setUri ( this.getUri () );
	    pubMedSearch.setTermLabels ( bmTermSel.getTermLabels () );
	    pubMedSearch.run ();
	    System.out.println("Complete");
	    
	    // This contains a DCTHERA represenation of the publications found 
	  try 
	    {
		  	System.out.print("Merging PubMed model.....");
			Utils.mergeGraphs ( pubMedSearch.getResultModel () );
			System.out.println("Complete");
		} 
	    catch (FileNotFoundException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
		System.out.print("Retrieving LLD resources using the PubMed IDs.....");
	    LLDPubMedTermEnricher lldEnricher = new LLDPubMedTermEnricher ();
	    lldEnricher.setPMIDs ( pubMedSearch.getPMIDs () );
	    lldEnricher.setUri(this.getUri());
	    lldEnricher.run(); 
		System.out.println("Complete");
	    
	    try 
	    {	System.out.print("Merging LLD result model.....");
			Utils.mergeGraphs( lldEnricher.getResultModel () );
			System.out.println("Complete");
		} 
	    catch (FileNotFoundException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    System.out.print("Retrieving related UniProt resources.....");
	    UniprotEnricher uniProtEnricher = new UniprotEnricher ();
	    uniProtEnricher.setUri ( this.getUri () );
	    uniProtEnricher.setTermLabels ( bmTermSel.getTermLabels () );
	    uniProtEnricher.setOrganism ( bmTermSel.getOrganism () );
	    uniProtEnricher.run();
	   
	  
	    try 
	    {
			Utils.mergeGraphs ( uniProtEnricher.getResultModel () );
		} 
	    catch (FileNotFoundException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	    
	    //Test Code
	    Model model = ModelFactory.createDefaultModel();
	    
		model.setNsPrefix("dcr", NS.DCR);
		model.setNsPrefix("rdfs", NS.RDFS);
		model.setNsPrefix("owl", NS.owl);
		model.setNsPrefix("dc", NS.dc);
		
	    model.add(pubMedSearch.getResultModel());
	    //model.add(lldEnricher.getResultModel());
	   // model.add(uniProtEnricher.getResultModel());
	    System.out.println("------------------------------------------Merged Model Results-----------------------------------------------------");
	    model.write(System.out, "TURTLE");
	    //End Test Code
		
		
		
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
