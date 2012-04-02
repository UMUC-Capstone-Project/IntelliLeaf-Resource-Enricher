/*LLDPubMedTermEnricher.java
 * 
 * Description: Obtains the pubMed IDs of related PubMed articles.  It then takes those IDs, searches each one using SPARQL for related resources.  Those resources,
 *  and their relationships, are then added to the Jena model, resultsModel.
 */

package com.intellileaf.dctheradir.enricher.model_processors;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.query.*;
import com.intellileaf.dctheradir.enricher.NS;
import com.intellileaf.dctheradir.enricher.PPT;

import java.util.*;

public class LLDPubMedTermEnricher implements KnowledgeBaseProcessor
{
	private Model resultModel = ModelFactory.createDefaultModel(); //initializes the model
	private List<String> pmids =  new ArrayList<String>(); //contains the PubMed IDs
	private String uri = ""; //Contains the DC-Thera resource URI
	
	public void setUri(String uri)
	{
		this.uri = uri;
	}
	
	public String getUri()
	{
		return uri;
	}

	public List<String> getPMIDs ()
	{
		return pmids;
	}

	public void setPMIDs ( List<String> pmids )
	{
		this.pmids = pmids;
	}

	public Model getResultModel ()
	{
		return resultModel;
	}

	@Override
	public void run ()
	{
		int autoDocCount = 1; //count for the AutoRelatedDocument_#
		int resultCount = 0; //used in counting the results returned in each table
		
		//sets the prefixes for the model
		resultModel.setNsPrefix("dcr", NS.DCR);
		resultModel.setNsPrefix("rdfs", NS.RDFS);
		resultModel.setNsPrefix("owl", NS.owl);
		
		//Creates the properties defining the related resources found in LLD and the resource for the DC-Thera resource
		Resource dcResource = resultModel.createResource(getUri());
		Property hasAutoRelatedDoc = null;
		
		for(int x = 0; x < pmids.size(); x++)
		{	
			//Sparql Statement to retrieve resources from LLD
			String sparqlQuery=
					"PREFIX pubmed: <http://linkedlifedata.com/resource/pubmed/> " +
					"PREFIX lifeskim: <http://linkedlifedata.com/resource/lifeskim/> " +
					"PREFIX skos: <http://www.w3.org/2004/02/skos/core#> " +

					"SELECT DISTINCT ?concept ?termLabel " +
						"WHERE {" +
						"   <https://linkedlifedata.com/resource/pubmed/id/" + pmids.get(x) + "> lifeskim:mentions ?concept." +
						"   ?concept skos:prefLabel ?termLabel." +
						" } " + 
						"";
			
			//Execution of the Sparql Query--Obtain results
			Query query = QueryFactory.create(sparqlQuery);
			QueryExecution qexec = QueryExecutionFactory.sparqlService("http://linkedlifedata.com/sparql", query);
			
			ResultSet results = qexec.execSelect();
			
			//Creation of resources/properties to populate the model
			Resource document = ResourceFactory.createResource(NS.DCR + "document/" + pmids.get(x)); //resource for pubMed article that was searched
			
			if(autoDocCount < 5)
				hasAutoRelatedDoc = ResourceFactory.createProperty(NS.DCR, "hasAutoRelatedDocument_" + autoDocCount); //resource showing that the article is an autorelatedDocument
			else
				hasAutoRelatedDoc = ResourceFactory.createProperty(NS.DCR, "hasAutoRelatedDocument_5");
			
			try
			{	
				resultCount = 0; 
				
				while((results.hasNext())&&(resultCount < 5))
				{	
					
					QuerySolution sol = results.nextSolution(); //obtains a line in the results
					
					// Obtains the termLabel and concept from each line of the results
					RDFNode termLabel = sol.get("termLabel");  
					RDFNode concept = sol.get("concept"); 
					
					String con = concept.toString();
					
					Resource lldConcept = ResourceFactory.createResource(con); //Resource for the concept that was returned
	            	
					//Populating the model with the relationships between LLD and the Dc-Thera resource
					resultModel.add(dcResource, hasAutoRelatedDoc, document);
	            	resultModel.add(document, PPT.label, termLabel);
	            	resultModel.add(document, PPT.lldUri, lldConcept);
	            	
	            	//Populating the model with the relationships describing the LLD resource
	            	resultModel.add(lldConcept, PPT.label, termLabel);
	            	
	            	resultCount++;
				}
			}
			finally
			{
				qexec.close();
			}
			
			autoDocCount++;
		}

	}

}

