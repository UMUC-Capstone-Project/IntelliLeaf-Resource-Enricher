package com.intellileaf.dctheradir.enricher;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Resources
{
	private static OntModel directoryModel = null;
	
	
	
	/**
	 * The DCTHERA Directory model.
	 * @throws FileNotFoundException 
	 * 
	 */
	public static OntModel getDirectoryModel () throws FileNotFoundException
	{
		if ( directoryModel != null ){
			return directoryModel;
		}
		// Load it from the dump file
		
		String URI = "http://artemisia.leafbioscience.com/dcthera_dump.rdf";
		
		InputStream input = new FileInputStream("./ontology/dcthera_enrichment.owl");
		
		/* 
		 * ResourceEnricher.isResourceSupported() requires a minimum degree of inference, i.e., automatic reasoning. 
		 * Probably the minimum that we need in this application is OWL_DL_MEM_TRANS_INF (OWL_LITE_MEM_TRANS_INF if
		 * that's too slow during updates). Likely OWL_MEM is not enough.
		 * 
		 */
		directoryModel =  
				ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_TRANS_INF);
				directoryModel.read(input, "RDF/XML-ABBREV"); 
				
		directoryModel.read(URI);

		//directoryModel.write(System.out);
		
		// Then return it
		return directoryModel;
	}

}
