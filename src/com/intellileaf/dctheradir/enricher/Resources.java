package com.intellileaf.dctheradir.enricher;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Resources
{
	private static OntModel directoryModel = null;
	
	private static Logger log = LoggerFactory.getLogger ( Resources.class );
	
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
		
		
		/* 
		 * ResourceEnricher.isResourceSupported() requires a minimum degree of inference, i.e., automatic reasoning. 
		 * Probably the minimum that we need in this application is OWL_DL_MEM_TRANS_INF (OWL_LITE_MEM_TRANS_INF if
		 * that's too slow during updates). Likely OWL_MEM is not enough.
		 * 
		 */
		directoryModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_TRANS_INF);
		
		log.info ( "Loading Ontologies" );
		
		// Let's go with manual loading, this is less flexible than automatic import-closure, but faster and more reliable. 
		// Moreover, auto-import would need to remap a few invalid URIs
		//
		directoryModel.getDocumentManager ().setProcessImports ( false );
		File ontoDir = new File ( "./ontology" );
		File owlFiles[] = ontoDir.listFiles ( new FileFilter() { public boolean accept ( File file ) {
				return file.getName ().toLowerCase ().endsWith ( ".owl" );
			}
		});
		
		for ( File owlFile: owlFiles ) {
			log.info ( "Loading ontology: '" + owlFile.getName () );
			String path = owlFile.getAbsolutePath ();
			path = path.replaceAll(" ","%20");
			directoryModel.read ( "file://" + path );
		}
			
		log.info ( "Ontologies loaded, now the Directory" );
		directoryModel.read(URI);

		//directoryModel.write(System.out);
		
		log.info ( "Knowledge base loaded!" );

		// Then return it
		return directoryModel;
	}

}
