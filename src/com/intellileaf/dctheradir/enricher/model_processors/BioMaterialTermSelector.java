package com.intellileaf.dctheradir.enricher.model_processors;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.intellileaf.dctheradir.enricher.Resources;

/**
 * 
 * Selects terms that characterise a Directory biomaterial. Inspect statements in {@link Resources#getDirectoryModel()} 
 * to do that. 
 *  
 * <dl><dt>date</dt><dd>Feb 28, 2012</dd></dl>
 *
 */
public class BioMaterialTermSelector implements KnowledgeBaseProcessor
{
	private List<String> termLabels = new ArrayList<String>();
	private String uri;
	
	/**
	 * @return the URI of the biomateral from which to pick up terms.
	 */
	public String getUri ()
	{
		return uri;
	}

	/**
	 * @return the URI of the biomateral from which to pick up terms.
	 */
	public void setUri ( String uri )
	{
		this.uri = uri;
	}
	
	/**
	 * @return the output found by {@link #run()}
	 */
	public List<String> getTermLabels ()
	{
		return termLabels;
	}


	@Override
	public void run ()
	{	
    	
    	Model model = ModelFactory.createDefaultModel();
    	model.read(uri);
    	
		StmtIterator iter = model.listStatements();

		// Iterate through the predicate, subject and object of each statement
		while (iter.hasNext()) 
		{
		    Statement stmt      = iter.nextStatement();  // get next statement
		    Resource  subject   = stmt.getSubject();     // get the subject
		    Property  predicate = stmt.getPredicate();   // get the predicate
		    RDFNode   object    = stmt.getObject();      // get the object

		    if (predicate.toString().equals("http://www.w3.org/2000/01/rdf-schema#label"))
		    {
		    	termLabels.add(object.toString());
		    }
		}
	}
	


	
}
