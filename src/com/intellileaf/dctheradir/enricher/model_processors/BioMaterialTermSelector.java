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
import com.intellileaf.dctheradir.enricher.NS;

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
		
		List<String> targetObjects = new ArrayList<String>();

		/** Iterate through the predicate, subject and object of each statement
		  * First iteration returns the Biomaterial label and each object as well as the
		  * object for the triple Biomaterial hasMoleculeType x and Biomaterial hasOrganismType x
		  * 
		  * 
		  */
		while (iter.hasNext()) 
		{
		    Statement stmt      = iter.nextStatement();  // get next statement
		    Resource  subject   = stmt.getSubject();     // get the subject
		    Property  predicate = stmt.getPredicate();   // get the predicate
		    RDFNode   object    = stmt.getObject();      // get the object

		    if (subject.toString().equals(this.getUri()) && (predicate.toString().equals(NS.RDFS+"label")))
		    {
		    	termLabels.add(object.toString());
		    }
		    
		    if (subject.toString().equals(this.getUri()) && (predicate.toString().equals(NS.DCR+"hasMoleculeType")))
		    {
		    	
		    	targetObjects.add(object.toString());
		    	
		    }
		    
		    if (subject.toString().equals(this.getUri()) && (predicate.toString().equals(NS.DCR+"hasOrganismType")))
		    {
		    	targetObjects.add(object.toString());
		    }
		    
		}
		
		/** Iterate through the predicate, subject and object of each statement
		  * Second iteration returns the Biomaterial MoleculeType and OrganismType labels 
		  * 
		  */
		
		
		for (int i = 0; i < targetObjects.size(); i++){
			
			StmtIterator iter2 = model.listStatements();
			
			while (iter2.hasNext()) 
			{
			    Statement stmt      = iter2.nextStatement();  // get next statement
			    Resource  subject   = stmt.getSubject();     // get the subject
			    Property  predicate = stmt.getPredicate();   // get the predicate
			    RDFNode   object    = stmt.getObject();      // get the object
			    
			    
			    if (subject.toString().equals(targetObjects.get(i)) && (predicate.toString().equals(NS.RDFS+"label")))
			    {
			    	termLabels.add(object.toString());
			    	
			    }
			    
			}
			
		}
		
		for (int i = 0; i < termLabels.size(); i++){
			
			System.out.println("Terms: "+ (termLabels.get(i)));
		}
		
		
		
		
	}
	


	
}
