package com.intellileaf.dctheradir.enricher.model_processors;
import com.intellileaf.dctheradir.enricher.Resources;
import com.intellileaf.dctheradir.enricher.NS;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.RDF;



/**
 * 
 *
 */
public class PubMedTermSearch extends ResourceEnricher
{
	private List<String> termLabels;
	private List<Integer> pmids = new ArrayList<Integer>();
	private Model resultModel = ModelFactory.createDefaultModel();
	
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
        String eUtilsBase = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&term=";//holds E-utils Base
        String link = "";
		String pubMedUri = "http://www.ncbi.nlm.nih.gov/pubmed/"; //Holds the URI base for the PubMed URI
		int count = 1;
		
		//prefixes for buidling the model
		resultModel.setNsPrefix("dcr", NS.DCR);
		resultModel.setNsPrefix("rdfs", NS.RDFS);
		
    	Document dom = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	
		//Loop to loop through the term labels, search each one with Utils, and parse the resulting XML file
        //Adds the IDs to the list pmids and adds the statements to the model
        for(int x = 0; x < termLabels.size(); x++)
        {
        	link = eUtilsBase.concat(termLabels.get(x));
        	
        	try 
            {
            	DocumentBuilder db = dbf.newDocumentBuilder();
                    
                //parses xml file found at this URL
                dom = db.parse(link);
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
                
            //Loops through the "Id" nodelist and adds the PubIds to an ArrayList
            for(int y = 0; y < nl2.getLength(); y++)
            {
            	Element elId = (Element)nl2.item(y);
            	
            	int id = Integer.parseInt(elId.getFirstChild().getNodeValue());
            
            	 pmids.add(id);
         
               //Statements to create the Jena model statement with the selected PubMed ID
            	 Resource dcResource = resultModel.createResource(getUri());
            	 Resource pubMedDoc = ResourceFactory.createResource(NS.DCR + "hasAutoRelatedDocument_" + count);
            	 
            	 Property hasAutoRelatedDoc = ResourceFactory.createProperty(NS.DCR, "hasAutoRelatedDocument");
            	 Property document = ResourceFactory.createProperty(NS.DCR, "document_" + pmids.get(count-1));
            	 Property identifiedBy = ResourceFactory.createProperty(NS.DCR, "identifier");
            	 
            	 resultModel.add(dcResource, hasAutoRelatedDoc, pubMedDoc);
            	 resultModel.add(pubMedDoc, document, NS.pubDoc);
            	 resultModel.add(pubMedDoc, document, NS.researchDoc);
            	 resultModel.add(pubMedDoc, identifiedBy, pubMedUri + pmids.get(count-1));
            	 
                 count++;

            }
            
        }
        
        //prints model
        System.out.println("------------------------------------------PubMed Model Results-----------------------------------------------------\n");
        resultModel.write(System.out, "TURTLE");

    }

	
	private Property createProperty() {
		
		return null;
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
