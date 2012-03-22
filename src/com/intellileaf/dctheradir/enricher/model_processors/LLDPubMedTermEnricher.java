/*LLDPubMedTermEnricher.java
 * 
 * Description: Obtains the pubMed IDs of related PubMed articles.  It then takes those IDs, searches each one using SPARQL for related resources.  Those resources,
 *  and their relationships, are then added to the Jena model, resultsModel.
 */

package com.intellileaf.dctheradir.enricher.model_processors;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.query.*;
import com.intellileaf.dctheradir.enricher.NS;
import java.util.*;


public class LLDPubMedTermEnricher implements KnowledgeBaseProcessor
{
	private Model resultModel = ModelFactory.createDefaultModel(); //initializes the model
	private List<String> pmids; //contains the PubMed IDs
	private String uri; //Contains the DC-Thera resource URI
	
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
		//sets the prefixes for the model
		resultModel.setNsPrefix("dcr", NS.DCR);
		resultModel.setNsPrefix("rdfs", NS.RDFS);
		resultModel.setNsPrefix("owl", NS.owl);
		
		Resource dcResource = resultModel.createResource(getUri()); //Creates the resource for the DC-Thera resource
		
 		Property label = ResourceFactory.createProperty(NS.RDFS, "label"); //creates the resource for the labels found in the search
		Property lldUri = ResourceFactory.createProperty(NS.owl, "samAs"); //creates the resource for the concept returned in the search
		
		int count = 1;
		
		for(int x = 0; x < pmids.size(); x++)
		{	
			//Sparql Statement to retrieve data from LLD
			String sparql1=
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
			Query query = QueryFactory.create(sparql1);
			QueryExecution qexec = QueryExecutionFactory.sparqlService(
			"http://linkedlifedata.com/sparql", query);
			
			ResultSet results = qexec.execSelect();
			
			Resource document = ResourceFactory.createResource(NS.DCR + "document/" + pmids.get(x)); //resource for pubMed article that was searched
			Property hasAutoRelatedDoc = ResourceFactory.createProperty(NS.DCR, "hasAutoRelatedDocument_" + count); //resource showing that the article is an autorelatedDocument
	
			try
			{	
				int resCount = 0;
				
				for(;results.hasNext();)
				{	
					if(resCount < 5){break;}
					
					QuerySolution sol = results.nextSolution(); //obtains a line in the results
					
					RDFNode termLabel = sol.get("termLabel"); //obtains the result in the termLabel column in that line 
					RDFNode concept = sol.get("concept"); //obtains the result in the concept column in that line
					
					String con = concept.toString();
					
					Resource lldConcept = ResourceFactory.createResource(con); //Resource for the concept that was returned
	            	
					//Creating the model with the relationships
					resultModel.add(dcResource, hasAutoRelatedDoc, document);
	            	resultModel.add(document, label, termLabel);
	            	resultModel.add(document, lldUri, lldConcept);
	            	
	            	//statements for adding the separate LLD resource sections (commented out, still need to set result limit)
	            	resultModel.add(lldConcept, label, termLabel);
	            	
	            	resCount++;
				}
			}
			finally
			{
				qexec.close();
			}
			
			count++;
		}
		
	}

}

