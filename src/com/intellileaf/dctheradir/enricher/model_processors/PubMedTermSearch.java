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
	private static String eUtilsBase = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&term=";//holds E-utils Base
	private static String eSearchBase = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&retmode=xml&id=";
	

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
		int count = 1;
		ArrayList<String> ids = new ArrayList<String>();

		//prefixes for buidling the model
		resultModel.setNsPrefix("dcr", NS.DCR);
		resultModel.setNsPrefix("rdfs", NS.RDFS);
		resultModel.setNsPrefix("obo", NS.obo);
		
		//Creates resources,Properties used in model
		Resource dcResource = resultModel.createResource(getUri()); 
		
	   	Property identifier = ResourceFactory.createProperty(NS.dc, "identifier");
	   	Property type = ResourceFactory.createProperty(NS.rdf, "type");
	   	Property title = ResourceFactory.createProperty(NS.dc, "title");
	   	Property creator = ResourceFactory.createProperty(NS.dc, "creator");
	   	Property date = ResourceFactory.createProperty(NS.dc, "date");
	   	Property description = ResourceFactory.createProperty(NS.dc, "description");
	   	Property source = ResourceFactory.createProperty(NS.dc, "source");
	   
		//Obtains a NodeList for each termLabel, obtains the IdList of PubMedIDs 
	   	for(int x = 0; x < termLabels.size(); x++)
	    {
	    
	   		NodeList idNodes = getIdNodeList(termLabels.get(x));

	   		ids.addAll(parseXmlElements(idNodes, "IdList"));
	            
	    }
	                
        //Loops through the "Id" nodelist, creates a NodeList for each article, obtains the Abstracts, titles, etc and adds it to the model
        for(int y = 0; y < ids.size(); y++)
        {
            int id = Integer.parseInt(ids.get(y));
            
            pmids.add(id);
        
            NodeList elementNodes = getArticleNodeList(id); 
            	 
            //Obtains the Abstracts, titles, etc.
            ArrayList<String> abst = parseXmlElements(elementNodes, "AbstractText");
            ArrayList<String> articleTitle = parseXmlElements(elementNodes, "ArticleTitle");
            ArrayList<String> pubYear = parseXmlElements(elementNodes, "Year");
            ArrayList <String> authors = parseXmlElements(elementNodes, "AuthorList");
            	 
            //Creates the resource for the pubMed document, and the property to show its an autorelated document
            Resource document = ResourceFactory.createResource(NS.DCR + "document/" + id);
            Property hasAutoRelatedDoc = ResourceFactory.createProperty(NS.DCR, "hasAutoRelatedDocument_" + count);
            	 
            //Statements to add the resources and their relationships
            resultModel.add(dcResource, hasAutoRelatedDoc, document);
            resultModel.add(document, type, NS.obo + "IAO_0000013"); 
            resultModel.add(document, identifier, pubMedUri + id);
            	 
            resultModel.add(document, title, articleTitle.get(0));
            resultModel.add(document, date, pubYear.get(0));
            resultModel.add(document, description, abst.get(0));
            	 
            for(int z = 0; z < authors.size(); z++)
            	resultModel.add(document, creator, authors.get(z));

            count++;

        }
	}

	//Called to retrieve the nodeList containing the PubMed IDs
	public NodeList getIdNodeList(String term)
	{
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
	public NodeList getArticleNodeList(int id)
	{
		
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
	
	public ArrayList<String> parseXmlElements(NodeList nl, String tagName)
	{
		
		ArrayList<String> results = new ArrayList<String>();
		
        try 
        {
        	if(tagName.matches("IdList"))
        	{
                
                for(int x = 0; x < nl.getLength(); x++)
                {
                	Element el3 = (Element)nl.item(x);
                	
                	results.add(el3.getFirstChild().getNodeValue());
                }
                
        	}
        	else if(tagName.matches("Year"))
            {
                Element elId = (Element)nl.item(0);
                NodeList nl2 = elId.getElementsByTagName("PubDate");
                Element el2 = (Element)nl2.item(0);
                
                NodeList nl3 = el2.getElementsByTagName(tagName);
                Element el3 = (Element)nl3.item(0);
                
            	results.add(el3.getFirstChild().getNodeValue());
            }
            else if(tagName.matches("AuthorList"))
            {
                
                 //Within the IdLists, creates a nodeList of all the Id tags
            	Element elId = (Element)nl.item(0);
                NodeList nl2 = elId.getElementsByTagName(tagName);
                
                Element el2 = (Element)nl2.item(0);
                NodeList nl3 = el2.getElementsByTagName("Author");
                
                
                for(int x = 0; x < nl3.getLength(); x++)
                {
                	Element el3 = (Element)nl3.item(x);
                	
                	NodeList nl4 = el3.getElementsByTagName("LastName");
                	Element el4 = (Element)nl4.item(0);
                	
                	NodeList nl5 = el3.getElementsByTagName("ForeName");
                	Element el5 = (Element)nl5.item(0);
                	
                	results.add(el4.getFirstChild().getNodeValue() + ", " + el5.getFirstChild().getNodeValue());
                }
            }
            else
            {
               
                Element elId = (Element)nl.item(0);
                NodeList nl2 = elId.getElementsByTagName(tagName);
                Element el2 = (Element)nl2.item(0);
                
            	results.add(el2.getFirstChild().getNodeValue());
            }
            
        }
        catch(NullPointerException npe)
        {
        	results.add("");
        }

   	 	
        return results;
		
	}



	 //@return an empty array, cause this enricher is supposed to be called directly and not to be used by a generic invoker
	 //that evaluates enrichers on the basis of its input. 
	@Override
	public String[] getSupportedUriTypes ()
	{
		return new String[] { "" };
	}

}