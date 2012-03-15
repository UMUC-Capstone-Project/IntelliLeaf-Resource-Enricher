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
 * Uses PUBMED to search those publications that are related to a set of terms.
 *
 * <dl><dt>date</dt><dd>Feb 28, 2012</dd></dl>
 *
 */
public class PubMedTermSearch extends ResourceEnricher
{
	private List<String> termLabels;
	private List<Integer> pmids = new ArrayList<Integer>();
	private Model resultModel = ModelFactory.createDefaultModel();
	
	
	/**
	 * The terms to be searched.
	 */
	public List<String> getTermLabels ()
	{
		return termLabels;
	}

	
	/**
	 * The terms to be searched.
	 */
	public void setTermLabels ( List<String> termLabels )
	{
		this.termLabels = termLabels;
	}

	/**
	 * What is returned by {@link #run()}. 
	 */
	public List<Integer> getPMIDs ()
	{
		return pmids;
	}
	
	/**
	 * a set of statements that characterise the returned publications (eg, the tile, authors, etc). Uses the
	 * DCTHERA ontology for defining such statements. Moreover, it links the found publications to the URI passed
	 * via {@link #setUri(String)}. This is populated by {@link #run()}. 
	 *  
	 */
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
            }
            
        }
        
        
        int count = 1;
        
        for(int x = 0; x < 5; x++)
        { 
        	Resource biomaterial = resultModel.createResource(getUri());
        	Resource pubMedDoc = resultModel.createResource();
    		Property autoRelatedDoc = resultModel.createProperty(NS.DCR, "hasAutoRelatedDocument_" + count);
    		Property document = resultModel.createProperty(NS.DCR, "document_" + pmids.get(x));
    		Property identifier = resultModel.createProperty(NS.DCR, "identifier");
    		Property label = resultModel.createProperty(NS.RDFS, "label");

    		
    		resultModel.add(biomaterial,autoRelatedDoc, pubMedDoc);
    		
    		resultModel.add(pubMedDoc, document, NS.publication);
    		resultModel.add(pubMedDoc, document, "http://purl.obolibrary.org/obo/IAO_0000013");
    		resultModel.add(pubMedDoc, identifier, pubMedUri + pmids.get(x));

    		count++;
        }
        
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
