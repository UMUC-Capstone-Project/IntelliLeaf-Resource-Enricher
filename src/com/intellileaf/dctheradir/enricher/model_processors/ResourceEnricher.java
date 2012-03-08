package com.intellileaf.dctheradir.enricher.model_processors;

import java.io.FileNotFoundException;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.intellileaf.dctheradir.enricher.NS;
import com.intellileaf.dctheradir.enricher.Resources;

/**
 * A Biodirectory resource enricher. This is supposed to take the URI of a Directory resource and populate the 
 * directory itself with resources that are logically related to the input. The directory knowledge base is taken
 * from {@link Resources#getDirectoryModel()}. 
 *
 * <dl><dt>date</dt><dd>Feb 28, 2012</dd></dl>
 *
 */
public abstract class ResourceEnricher implements KnowledgeBaseProcessor
{
	private String uri;
	
	/**
	 * @return the URI of the resource that is enriched by {@link #run()}.
	 */
	public String getUri ()
	{
		return uri;
	}


	public void setUri ( String uri )
	{
		this.uri = uri;
	}

	/**
	 * @return a list of URIs that tells the OWL classes corresponding to the resource types that an enricher is able
	 * to enrich. For instance, for biomaterial it returns { "http://dc-research.eu/#dctheradir_492" }.
	 * 	  	  
	 */
	public abstract String[] getSupportedUriTypes (); 

	/**
	 * 
	 * @return true is this resource is supported by the resource enricher (ie, its type is a subclass of one of the classes
	 * returned by {@link #getSupportedUriTypes()}.
	 * 
	 * This relies on the basic reasoning capabilities that a Jena {@link OntModel} has by default.
	 * @throws FileNotFoundException 
	 * 
	 */
	public boolean isResourceSupported ( String uri ) throws FileNotFoundException
	{
		OntModel m = Resources.getDirectoryModel ();
		
		Individual resource = m.getIndividual ( uri );
		for ( NodeIterator itr = resource.listPropertyValues ( m.getProperty ( NS.rdf + "type" ) ); itr.hasNext (); )
		{
			RDFNode onode = itr.next ();
			if ( !onode.canAs ( OntClass.class ) ) continue; // Strange, skip it. TODO: log it too.
			OntClass clas = onode.as ( OntClass.class );
			
			for ( String typeUri: getSupportedUriTypes () )
				if ( clas.hasSuperClass ( m.getOntClass ( typeUri ) )) return true;
		}
		return false;
	}
	
	@Override
	public void run ()
	{
		
	}
	
	
	
}
