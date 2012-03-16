package com.intellileaf.dctheradir.enricher.model_processors;

import java.util.ArrayList;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.query.*;
import com.intellileaf.dctheradir.enricher.NS;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LLDPubMedTermEnricher implements KnowledgeBaseProcessor
{
	private Model resultModel = ModelFactory.createDefaultModel();
	private List<Integer> pmids;
	private List<String> searchTerms;
	private String uri;
	
	public void setUri(String uri)
	{
		this.uri = uri;
	}
	
	public String getUri()
	{
		return uri;
	}

	public List<Integer> getPMIDs ()
	{
		return pmids;
	}

	public void setPMIDs ( List<Integer> pmids )
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
		
		resultModel.setNsPrefix("dcr", NS.DCR);
		resultModel.setNsPrefix("rdfs", NS.RDFS);
		resultModel.setNsPrefix("owl", NS.owl);
		
		
		int count = 1;

		// collection of the PubMed IDs retrieved from getPIDs() method
		//go thru and capture each PubMed ID in the 'for' loop and then incorporate each PubMed ID (one-by-one)...
		//...into the SPARQL query to retrieve LLD information
		
		for(int x = 0; x < pmids.size(); x++)
		{
			//PID.get(x);
			//still have to do this part using SPARQL...
			//...read in the PubMed IDs from the ArrayList one-by-one and queries LLD with SPARQL for each PubMedID*/
		
			String sparql1=
					"PREFIX pubmed: <http://linkedlifedata.com/resource/pubmed/> " +
					"PREFIX lifeskim: <http://linkedlifedata.com/resource/lifeskim/> " +
					"PREFIX skos: <http://www.w3.org/2004/02/skos/core#> " +

					"SELECT DISTINCT ?concept ?termLabel " +
						"WHERE {" +
						"   <https://linkedlifedata.com/resource/pubmed/id/" + pmids.get(x) + "> lifeskim:mentions ?concept." +
						"   ?concept skos:prefLabel ?termLabel." +
						" }";
			
			Query query = QueryFactory.create(sparql1);
			QueryExecution qexec = QueryExecutionFactory.sparqlService(
			"http://linkedlifedata.com/sparql", query);
			
			ResultSet results = qexec.execSelect();
			
			Resource dcResource = resultModel.createResource(getUri());
       	    Resource document = ResourceFactory.createResource(NS.DCR +  "document_" + pmids.get(count-1));
       	    
       	    Property hasAutoRelatedDoc = ResourceFactory.createProperty(NS.DCR, "hasAutoRelatedDocument");
       	    Property subPropertyOf = ResourceFactory.createProperty(NS.RDFS, "subPropertyOf");
    	    Property pubMedDoc = ResourceFactory.createProperty(NS.DCR + "hasAutoRelatedDocument_" + count);
     		Property label = ResourceFactory.createProperty(NS.RDFS, "label");
    		Property lldUri = ResourceFactory.createProperty(NS.owl, "samAs");
			
			try
			{
				for(;results.hasNext();)
				{
					QuerySolution sol = results.nextSolution();
					
					RDFNode termLabel = sol.get("termLabel");
					RDFNode concept = sol.get("concept");
					
	            	 
					resultModel.add(pubMedDoc, subPropertyOf, hasAutoRelatedDoc);
	            	resultModel.add(dcResource, pubMedDoc, document);
	            	
	            	resultModel.add(document, label, termLabel);
	            	resultModel.add(document, lldUri, concept);

				}
			}
			finally
			{
				qexec.close();
			}

			count++;
		}
		
		System.out.println("\n------------------------------------------LLD Model Results-----------------------------------------------------\n");
		resultModel.write(System.out, "TURTLE");
	}

}

