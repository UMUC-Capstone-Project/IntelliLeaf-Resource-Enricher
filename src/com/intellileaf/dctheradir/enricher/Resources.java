package com.intellileaf.dctheradir.enricher;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class Resources
{
	private static OntModel directoryModel = null;
	
	/**
	 * The DCTHERA Directory model.
	 * 
	 */
	public static OntModel getDirectoryModel ()
	{
		if ( directoryModel != null )
			return directoryModel;
		
		// Load it from the dump file
		
		String URI = "http://artemisia.leafbioscience.com/dcthera_dump.rdf";
		Model dcModel = ModelFactory.createDefaultModel();
		dcModel.read(URI);

		// Then return it
		return directoryModel;
	}

}
