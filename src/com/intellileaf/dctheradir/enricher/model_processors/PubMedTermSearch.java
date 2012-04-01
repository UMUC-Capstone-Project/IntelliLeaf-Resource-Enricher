/*PubMedTermSearch.java
 * 
 * Description: Takes the termLabels from the DC-Thera RDF.  It then searches pubMed with each one for related resources.  Once those resources are found
 * they, and their relationships, are added to a Jena result model.
 */
package com.intellileaf.dctheradir.enricher.model_processors;
import com.intellileaf.dctheradir.enricher.NS;
import com.intellileaf.dctheradir.enricher.PPT;

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
	private List<String> pmids = new ArrayList<String>(); //holds the PubMed IDs
	private Model resultModel = ModelFactory.createDefaultModel(); //resultModel for the jena model
	private String eSearchBase = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?retmax=20&db=pubmed&term=";//holds E-utils Base
	private String eFetchBase = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&retmode=xml&id=";


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
	public List<String> getPMIDs ()
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

		//prefixes for buidling the model
		resultModel.setNsPrefix("dcr", NS.DCR);
		resultModel.setNsPrefix("rdfs", NS.RDFS);
		resultModel.setNsPrefix("obo", NS.obo);

		//Creates resources used in model
		Resource dcResource = resultModel.createResource(getUri()); 
		
		//Obtains a NodeList for each termLabel, obtains the IdList of PubMedIDs 
	   	for(int x = 0; x < termLabels.size(); x++)
	   		getPubmedIds(termLabels.get(x));

        //Loops through each pubMed ID, retrieving the elements and adding them to the model
        for(int y = 0; y < pmids.size(); y++)
        {
            
            NodeList elementNodes = getArticleNodeList(pmids.get(y));
            Element articleElement = (Element)elementNodes.item(0);
            
            if(articleElement.getNodeName().matches("PubmedArticle"))
            {
                //Obtains the Abstracts, titles, etc.
                ArrayList<String> articleAbstract = parseJournalArticleElements(elementNodes, "AbstractText");
                ArrayList<String> articleTitle = parseJournalArticleElements(elementNodes, "ArticleTitle");
                ArrayList<String> yearCreated = parseJournalArticleElements(elementNodes, "Year");
                ArrayList <String> articleAuthors = parseJournalArticleElements(elementNodes, "Author");
                ArrayList<String> articleJournal = parseJournalArticleElements(elementNodes, "Journal");
                	 
                //Creates the resource for the pubMed document, and the property to show its an autorelated document
                Resource document = ResourceFactory.createResource(NS.DCR + "document/" + pmids.get(y));
                
                
                if(count < 5){
                Property hasAutoRelatedDoc = ResourceFactory.createProperty(NS.DCR, "hasAutoRelatedDocument_" + count);
                resultModel.add(dcResource, hasAutoRelatedDoc, document);
                }
                else
                {
                Property hasAutoRelatedDoc = ResourceFactory.createProperty(NS.DCR, "hasAutoRelatedDocument_5");
                resultModel.add(dcResource, hasAutoRelatedDoc, document);
                }
                	 
                //Statements to add the resources and their relationships
                resultModel.add(document, PPT.type, NS.obo + "IAO_0000013"); 
                resultModel.add(document, PPT.identifier, pubMedUri + pmids.get(y));
                	 
                resultModel.add(document, PPT.title, articleTitle.get(0));
                resultModel.add(document, PPT.date, yearCreated.get(0));
                resultModel.add(document, PPT.description, articleAbstract.get(0));
                resultModel.add(document, PPT.source, articleJournal.get(0));
                	 
                for(int z = 0; z < articleAuthors.size(); z++)
                	resultModel.add(document, PPT.creator, articleAuthors.get(z));
                
            }
            else if(articleElement.getNodeName().matches("PubmedBookArticle"))
            {
            	ArrayList<String> bookTitle = parseBookChapterElements(elementNodes, "ArticleTitle");
            	ArrayList<String> bookAbstract = parseBookChapterElements(elementNodes, "AbstractText");
            	ArrayList<String> bookAuthors = parseBookChapterElements(elementNodes, "Author");
            	
                //Creates the resource for the pubMed document, and the property to show its an autorelated document
                Resource document = ResourceFactory.createResource(NS.DCR + "document/" + pmids.get(y));
                
                if(count < 5){
                    Property hasAutoRelatedDoc = ResourceFactory.createProperty(NS.DCR, "hasAutoRelatedDocument_" + count);
                    resultModel.add(dcResource, hasAutoRelatedDoc, document);
                    }
                    else
                    {
                    Property hasAutoRelatedDoc = ResourceFactory.createProperty(NS.DCR, "hasAutoRelatedDocument_5");
                    resultModel.add(dcResource, hasAutoRelatedDoc, document);
                    }
                
                resultModel.add(document, PPT.type, NS.obo + "IAO_0000311");
                resultModel.add(document, PPT.title, bookTitle.get(0));
                resultModel.add(document, PPT.description, bookAbstract.get(0));
                
                for(int z = 0; z < bookAuthors.size(); z++)
                	resultModel.add(document, PPT.creator, bookAuthors.get(z));
            }
            	 


            count++;

        }
        
	}
	
	public void getPubmedIds(String term)
	{
		String resultLink = ""; // holds the result link
		Document dom = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        
        try
        {
        	DocumentBuilder db = dbf.newDocumentBuilder();
        	
            resultLink = eSearchBase + term;
            
            //parses xml file found at this URL
            dom = db.parse(resultLink);
            
            //Creates element of xml file, then creates a node lists of all "IdLists" in file
            Element Ele = dom.getDocumentElement();

            NodeList nl = Ele.getElementsByTagName("IdList");
                  
            //Within the IdLists, creates a nodeList of all the Id tags
            Element IdElement = (Element)nl.item(0);
            NodeList idList = IdElement.getElementsByTagName("Id");
            
            for(int x = 0; x < idList.getLength(); x++)
            {
            	Element idEl2 = (Element)idList.item(x);
            	
            	String id = idEl2.getFirstChild().getNodeValue();
            	
            	if(checkDuplicates(id))
            		continue;
            	else
            		pmids.add(id);
            	
            }
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
          
        
	}
	
	public NodeList getArticleNodeList(String id)
	{
		String resultLink = ""; // holds the result link
		Document dom = null;
		NodeList nodeList = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        
        try
        {
        	DocumentBuilder db = dbf.newDocumentBuilder();
        	
        	resultLink = eFetchBase + id;
            
            //parses xml file found at this URL
            dom = db.parse(resultLink);
        	
    	    Element Ele = dom.getDocumentElement();
    	    NodeList childNodes = Ele.getChildNodes();

    	    nodeList = Ele.getElementsByTagName(childNodes.item(1).getNodeName());
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
        
        
        return nodeList;
	}
	
	//Parses out the elements of a Book Chapter
	public ArrayList<String> parseBookChapterElements(NodeList nl, String tagName)
	{
		ArrayList<String> results = new ArrayList<String>();
		
		try
		{
			if(tagName.matches("Author"))
			{
                //Within the IdLists, creates a nodeList of all the Id tags
				Element elId = (Element)nl.item(0);
               NodeList nl2 = elId.getElementsByTagName("AuthorList");
               
               Element el2 = (Element)nl2.item(0);
               NodeList nl3 = el2.getElementsByTagName(tagName);
               
               
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
				Element bTitleElement = (Element)nl.item(0);
					
				NodeList bTitleList = bTitleElement.getElementsByTagName(tagName);
				Element bTitleElement2 = (Element)bTitleList.item(0);
					
				results.add(bTitleElement2.getFirstChild().getNodeValue());
			}
		}
        catch(NullPointerException npe)
        {
        	results.add("");
        }
		
		
		return results;
	}

	
	//parses out elements of a JournalArticle 
	public ArrayList<String> parseJournalArticleElements(NodeList nl, String tagName)
	{

		ArrayList<String> results = new ArrayList<String>();

        try 
        {

        	if(tagName.matches("Year"))
            {
                Element elId = (Element)nl.item(0);
                NodeList nl2 = elId.getElementsByTagName("DateCreated");
                Element el2 = (Element)nl2.item(0);
                
                NodeList nl3 = el2.getElementsByTagName(tagName);
                Element el3 = (Element)nl3.item(0);
                
            	results.add(el3.getFirstChild().getNodeValue());
            }
            else if(tagName.matches("Author"))
            {
                
                 //Within the IdLists, creates a nodeList of all the Id tags
            	Element elId = (Element)nl.item(0);
                NodeList nl2 = elId.getElementsByTagName("AuthorList");
                
                Element el2 = (Element)nl2.item(0);
                NodeList nl3 = el2.getElementsByTagName(tagName);
                
                
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
            else if(tagName.matches("Journal"))
            {
                Element elId = (Element)nl.item(0);
                NodeList nl2 = elId.getElementsByTagName(tagName);
                Element el2 = (Element)nl2.item(0);
                
                NodeList nl3 = el2.getElementsByTagName("Title");
                Element el3 = (Element)nl3.item(0);
                
            	results.add(el3.getFirstChild().getNodeValue());
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

	
//Checks the ID to see if it already exists in the PubMed ID list
public boolean checkDuplicates(String pId)
{
	boolean found = false;

	for(int x = 0; x < pmids.size(); x++)
	{
		if(pId.matches(pmids.get(x)))
		{
			found = true;

			break;
		}
	}

	return found;

}

	 //@return an empty array, cause this enricher is supposed to be called directly and not to be used by a generic invoker
	 //that evaluates enrichers on the basis of its input. 
	@Override
	public String[] getSupportedUriTypes ()
	{
		return new String[] { "" };
	}

}