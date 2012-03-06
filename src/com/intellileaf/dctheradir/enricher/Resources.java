package com.intellileaf.dctheradir.enricher;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
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
		if ( directoryModel != null )
			return directoryModel;
		
		// Load it from the dump file
		
		String URI = "http://artemisia.leafbioscience.com/dcthera_dump.rdf";
		
		InputStream input = new FileInputStream("./ontology/ontology.owl");
		
		directoryModel =  
				ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
				directoryModel.read(input, "RDF/XML-ABBREV"); 
				
		directoryModel.read(URI);

		//directoryModel.write(System.out);
		
		// Then return it
		return directoryModel;
	}

}
