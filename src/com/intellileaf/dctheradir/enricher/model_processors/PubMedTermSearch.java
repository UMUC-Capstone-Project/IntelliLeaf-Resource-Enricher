/*PubMedTermSearch.java
 * 
 * Description: Takes the termLabels from the DC-Thera RDF.  It then searches pubMed with each one for related resources.  Once those resources are found
 * they, and their relationships, are added to a Jena result model.
 */
package com.intellileaf.dctheradir.enricher.model_processors;
import com.intellileaf.dctheradir.enricher.NS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.hp.hpl.jena.rdf.model.*;


public class PubMedTermSearch extends ResourceEnricher
{
	private List<String> termLabels; //holds the parsed keywords for the DC-Thera RDF file
	private List<Integer> pmids = new ArrayList<Integer>(); //holds the PubMed IDs
	private Model resultModel = ModelFactory.createDefaultModel(); //resultModel for the jena model
	
	//Retrieves the termLabels list (keywords)
	public List<String> getTermLabels ()
	{
		return termLabels;
	}
	
	//sets the termLabels variable
	public void setTermLabels ( List<String> termLabels )
	{
		this.termLabels = termLabels;
	}
	
	//Retrieves the PubMed IDs List
	public List<Integer> getPMIDs ()
	{
		return pmids;
	}
	
	//Retrieves the PubMed result model
	public Model getResultModel ()
	{
		return resultModel;
	}

	@Override
	public void run ()
	{
		String pubMedUri = "http://www.ncbi.nlm.nih.gov/pubmed/"; //Holds the URI base for the PubMed URI
		String link2 = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&retmode=xml&id=";
		int count = 1;
		
		//prefixes for buidling the model
		resultModel.setNsPrefix("dcr", NS.DCR);
		resultModel.setNsPrefix("rdfs", NS.RDFS);
		resultModel.setNsPrefix("obo", NS.obo);
		
		Resource purlDoc = ResourceFactory.createResource(NS.obo); //Creates resource for research Document Ontology
		Resource dcResource = resultModel.createResource(getUri()); //Creates resource for the DC-Thera resource
		
		//Creates all the properties used in the model
	   	Property identifier = ResourceFactory.createProperty(NS.dc, "identifier");
	   	Property type = ResourceFactory.createProperty(NS.rdf, "type");
	   	Property title = ResourceFactory.createProperty(NS.dc, "title");
	   	Property creator = ResourceFactory.createProperty(NS.dc, "creator");
	   	Property date = ResourceFactory.createProperty(NS.dc, "date");
	   	Property description = ResourceFactory.createProperty(NS.dc, "description");
	   	Property source = ResourceFactory.createProperty(NS.dc, "source");
  
		//Loop to loop through the term labels, search each one with Utils, and parse the resulting XML file
        //Adds the IDs to the list pmids and adds the statements to the model
        for(int x = 0; x < termLabels.size(); x++)
        {
        	//obtains the nodeList contain the Ids
            NodeList idList = parsePubMedIds(termLabels.get(x));
                
            //Loops through the "Id" nodelist and adds the PubIds to an ArrayList
            for(int y = 0; y < idList.getLength(); y++)
            {
            	Element idElement = (Element)idList.item(y);
            	
            	int id = Integer.parseInt(idElement.getFirstChild().getNodeValue());
            
            	 pmids.add(id); //adds the ID to the pmids list
        
            	 NodeList idElements = parseXmlElements(id); //Obtains elements for the PubMed ID
            	 
            	 	/* In this section, create the elements/nodes to parse out Abstract, title, authors, etc.
            	 	 * 
            	 	 */
            	 
                 //Creates the resource for the pubMed document, and the property to show its an autorelated document
            	 Resource document = ResourceFactory.createResource(NS.DCR + "document/" + id);
            	 Property hasAutoRelatedDoc = ResourceFactory.createProperty(NS.DCR, "hasAutoRelatedDocument_" + count);
            	 
            	 //Statements to add the resources and their relationships
            	 resultModel.add(dcResource, hasAutoRelatedDoc, document);
            	 resultModel.add(document, type, NS.obo + "IAO_0000013"); 
            	 resultModel.add(document, identifier, pubMedUri + id);

                 count++;

            }
            
        }

    }
	
	//called to parse out the NodList containing the IDs in the E-utils result file
	public NodeList parsePubMedIds(String term)
	{
		String eUtilsBase = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&term=";//holds E-utils Base
		String resultLink = ""; // holds the result link
		Document dom = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        resultLink = eUtilsBase.concat(term);
        	
        try 
        {
        	DocumentBuilder db = dbf.newDocumentBuilder();
                    
            //parses xml file found at this URL
            dom = db.parse(resultLink);
        } 
        catch (ParserConfigurationException pce) 
        {
            pce.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        catch (SAXException se)
        {
            se.printStackTrace();
        }
                
        //Creates element of xml file, then creates a node lists of all "IdLists" in file
       Element Ele = dom.getDocumentElement();
       NodeList nl = Ele.getElementsByTagName("IdList");
          
        //Within the IdLists, creates a nodeList of all the Id tags
        Element elIdList = (Element)nl.item(0);
        NodeList nl2 = elIdList.getElementsByTagName("Id");
            
        return nl2;
	}
	
	//Returns the NodeList containing the information on a specific PubMed articles
	public NodeList parseXmlElements(int id)
	{
		String eSearchBase = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&retmode=xml&id=";
		String resultLink = "";
		Document dom = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        resultLink = eSearchBase + id;
  
        try 
        {
        	DocumentBuilder db = dbf.newDocumentBuilder();
                    
            //parses xml file found at this URL
            dom = db.parse(resultLink);
        } 
        catch (ParserConfigurationException pce) 
        {
            pce.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        catch (SAXException se)
        {
            se.printStackTrace();
        }
        
        Element Ele = dom.getDocumentElement();
        NodeList nl = Ele.getElementsByTagName("PubmedArticle");

		return nl;
	}
	
	
	
	 //@return an empty array, cause this enricher is supposed to be called directly and not to be used by a generic invoker
	 //that evaluates enrichers on the basis of its input. 
	@Override
	public String[] getSupportedUriTypes ()
	{
		return new String[] { "" };
	}

}
