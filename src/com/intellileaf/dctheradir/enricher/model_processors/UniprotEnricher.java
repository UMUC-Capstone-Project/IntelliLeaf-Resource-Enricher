package com.intellileaf.dctheradir.enricher.model_processors;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.shared.JenaException;
import com.intellileaf.dctheradir.enricher.NS;
import com.intellileaf.dctheradir.enricher.PPT;

/**
 * Enriches a Directory resource with annotations extracted from UniProt by doing term search.
 *
 * <dl><dt>date</dt><dd>Feb 28, 2012</dd></dl>
 *
 */
public class UniprotEnricher extends ResourceEnricher
{
	private String uri;
	private List<String> termLabels;
	private String organism;
	private Model resultModel = ModelFactory.createDefaultModel();
	private static OntModel uniprotOnt = null;
	
	//Returns the DC-Thera Resource URI
	public String getUri ()
	{
		return uri;
	}
	
	//Sets the DC-Thera Resource URI
	public void setUri ( String uri )
	{
		this.uri = uri;
	}

	//Returns the term labels parsed from the DC-Thera Resource RDF
	public List<String> getTermLabels ()
	{
		return termLabels;
	}
	
	//sets termLabels with the terms parsed from the resource RDF
	public void setTermLabels ( List<String> termLabels )
	{
		this.termLabels = termLabels;
	}
	
	public String getOrganism ()
	{
		return organism;
	}

	public void setOrganism( String organism)
	{
		this.organism = organism;
	}
	
	public Model getResultModel ()
	{
		return resultModel;
	}

	@Override
	public void run ()
	{
		
		boolean testURI;
		boolean protTriple;
		String term;
		String org;
		String url;
		int pcount = 0;
		int tcount;
		List<String> uniqueTerms = new ArrayList <String>();
		Property seeAlso = ResourceFactory.createProperty(NS.RDFS + "seeAlso");
		
		Resource dcResource = resultModel.createResource(getUri());
		
		for (int i = 0; i < termLabels.size(); i++)
		{
			
			//reset variables	
			uniqueTerms.clear();	 
			
			term = termLabels.get(i).replaceAll(" ","%20");
		  
		  
			if (this.organism != null)
			{
			 
				org = organism.replaceAll(" ","%20"); 	  
				  
				url = "http://www.uniprot.org/uniprot/?query=" + term + "%20" + org + "&sort=score&limit=5&format=rdf"; // termLabel is eg. 'MAGE-3'
				uniprotOnt =  ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_TRANS_INF);
				try
				{
					uniprotOnt.read ( url );
					testURI = true;
				}
				catch(JenaException e){testURI = false;}
			
			}
			else
			{
			  url = "http://www.uniprot.org/uniprot/?query=" + term + "&sort=score&limit=5&format=rdf"; // termLabel is eg. 'MAGE-3'
			  uniprotOnt =  ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_TRANS_INF);
			  try{
			  uniprotOnt.read (url);
			  testURI = true;
			  }catch(JenaException e){testURI = false;}
			  
			}
		
		
			if (testURI == true)
			{
				// The UniProt web service doesn't define Protein as a class and the inference type we use here is not able
				// to entail that Protein is a class from <s, rdf:type, Protein>. Therefore, listindividuals won't work.
				ResIterator itr = uniprotOnt.listResourcesWithProperty ( 
					uniprotOnt.getProperty	( NS.rdf + "type" ), 
					uniprotOnt.getResource ( "http://purl.uniprot.org/core/Protein" ) 
				);
				
				if ( itr != null ) while (itr.hasNext ())
				{
			
					protTriple = false;
					Resource protResource = itr.next ();
				
					pcount++;
					
					if (pcount < 5)
					{
						Property hasAutoRelatedProtein = ResourceFactory.createProperty(NS.DCR, "hasAutoRelatedProtein_"+ pcount);
						resultModel.add(dcResource, hasAutoRelatedProtein, protResource);}
					else
					{
						Property hasAutoRelatedProtein = ResourceFactory.createProperty(NS.DCR, "hasAutoRelatedProtein_5");
						resultModel.add(dcResource, hasAutoRelatedProtein, protResource);
					}
					
					
					/* Add other interesting links provided by the UniProt webservice, such as KEGG Pathways, Reactome, and Array Express
					 * Links
					 */
				
					NodeIterator riter = uniprotOnt.listObjectsOfProperty ( 
							protResource,
							uniprotOnt.getProperty	( NS.RDFS + "seeAlso" )
					);
					
					while(riter.hasNext()){
						RDFNode link = riter.next ();
						
						
						String regex = "http://purl.uniprot.org/kegg/";
				    	Pattern pt1 = Pattern.compile(regex);
				    	Matcher match = pt1.matcher(link.toString());
				    
				    	String regex2 = "http://purl.uniprot.org/reactome/";
				    	Pattern pt2 = Pattern.compile(regex2);
				    	Matcher match2 = pt2.matcher(link.toString());
				    	
				    	String regex3 = "http://purl.uniprot.org/arrayexpress/";
				    	Pattern pt3 = Pattern.compile(regex3);
				    	Matcher match3 = pt3.matcher(link.toString());
				    	
				    	String regex4 = "http://purl.uniprot.org/pathway-interaction-db/";
				    	Pattern pt4 = Pattern.compile(regex4);
				    	Matcher match4 = pt4.matcher(link.toString());
				    	
				    	if( (match.find()) || (match2.find()) || (match3.find()) || (match4.find()))
				    	{
				    		resultModel.add(protResource, seeAlso, link);
				    	}
				    		
						
						
					}
					
					
					
				
					/* The Sparql query returns both Go terms and Keywords.  Also, Go terms have multiple term labels, so 
					 * the query returns duplicate go terms for a single protein.  To resolve these issues, the program
					 * only pulls out terms that match http://purl.uniprot.org/go/.  A linked hash set is used to remove duplicates.
					 */
	
					String Sparql=
							"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
							"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
							"PREFIX uniprot: <http://purl.uniprot.org/core/> " +

							"SELECT distinct ?fullname ?term ?termLabel " +
							"WHERE { " +
							"<"+protResource.toString()+">" +
							"  uniprot:classifiedWith ?term;" +
							"  uniprot:recommendedName ?name." +
							"  ?name uniprot:fullName ?fullname." +
							"  ?term rdfs:label ?termLabel." +
							"} ";
		
					Query query = QueryFactory.create(Sparql);
					QueryExecution qexec = QueryExecutionFactory.sparqlService("http://linkedlifedata.com/sparql", query);
		
					ResultSet results = qexec.execSelect();
					tcount = 0;
		
					while (results.hasNext())
					{
					
						QuerySolution sol = results.nextSolution();
						RDFNode go = sol.get("term");
						
						String goTerm = go.toString();
						
						String reg5 = "http://purl.uniprot.org/go/";
				    	Pattern pt5 = Pattern.compile(reg5);
				    	Matcher mt5 = pt5.matcher(goTerm);
				    	
				    	if(mt5.find())
				    	{
			    		
				    		uniqueTerms.add(goTerm);
			    		
				    		Resource goResource = resultModel.createResource(goTerm);
			    		
				    		RDFNode pLabel = sol.get("fullname");
				    		RDFNode gLabel = sol.get("termLabel");
			    		
				    		if (protTriple == false)
				    		{
				    			resultModel.add(protResource, PPT.label, pLabel);
				    			protTriple = true;
				    		}
			    		
			    		resultModel.add(goResource,PPT.label,gLabel);
			    		
				    	}
					
					}
				
					qexec.close() ;
					//Remove duplicates from Go Terms and preserve order
				
					Set<String> uniqueGo = new LinkedHashSet<String>(uniqueTerms);
				
					Iterator<String> iterGo = uniqueGo.iterator();
				
					while (iterGo.hasNext())
					{
					
					Resource uniqueGoResource = resultModel.createResource(iterGo.next());
					
					tcount++;
					if (tcount < 5)
					{
		    		Property hasAutoRelatedTermClass = ResourceFactory.createProperty(NS.DCR, "hasAutoRelatedTermClass_"+ tcount);
					resultModel.add(protResource, hasAutoRelatedTermClass, uniqueGoResource);
					}
					else
					{
					Property hasAutoRelatedTermClass = ResourceFactory.createProperty(NS.DCR, "hasAutoRelatedTermClass_5");
					resultModel.add(protResource, hasAutoRelatedTermClass, uniqueGoResource);	
					}
					
					
					
				}		
				
				}
			}
		}
		
	}
	/**
	 * @return an empty array, cause this enricher is supposed to be called directly and not to be used by a generic invoker
	 * that evaluates enrichers on the basis of its input. 
	 */
	@Override
	public String[] getSupportedUriTypes ()
	{
		return new String[] { "" };
	}

}
