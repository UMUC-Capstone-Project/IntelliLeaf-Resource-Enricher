/* BiomaterialEnricher
 * 
 * Biomaterial Enricher containing the code to parse a biomaterial RDF, create E-Utils Link,
 * and parse the E-Utils results with Xerces.  Still need the Sparql queriees for LLD (and maybe Uniprot, Bio2RDF).
 *  
 *  BiomaterialEnricher is a subclass of ResourceData, which holds all the variables used in the calculations
 */
import java.util.ArrayList;
import com.hp.hpl.jena.rdf.model.*;

import java.io.IOException;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import com.hp.hpl.jena.rdf.model.*;


public class BiomaterialEnricher extends ResourceData {
    

    //Constructor--Initalizes superclass by passing the URI input to ResourceData
	//calls findRelatedResources()
    public BiomaterialEnricher(String input)
    {
        super(input);
        
        findRelatedResources();
    }
    
    //links methods together
    public void findRelatedResources()
    {
        ParseRDFFile();
    	
    	ArrayList<String> searchTerms = new ArrayList<String>();
    	searchTerms = getKeywords();
    	
    	for(int x = 0; x < getKeywords().size(); x++)
    	{
    		SearchPubMed(searchTerms.get(x));
    		ParseXML();
    	}
    	
    	ArrayList<String> pubID = new ArrayList<String>();
    	pubID = getPIDs();
    	for(String word: pubID)
    		System.out.println(word);
    }
    
    
    //Parses RDF File using Jena
    public void ParseRDFFile()
    {
    	ArrayList<String> keywords = new ArrayList<String>();
    	
    	Model model = getRDFModel();
    	
		StmtIterator iter = model.listStatements();

		// Iterate through the predicate, subject and object of each statement
		while (iter.hasNext()) 
		{
		    Statement stmt      = iter.nextStatement();  // get next statement
		    Resource  subject   = stmt.getSubject();     // get the subject
		    Property  predicate = stmt.getPredicate();   // get the predicate
		    RDFNode   object    = stmt.getObject();      // get the object

		    if (predicate.toString().equals("http://www.w3.org/2000/01/rdf-schema#label"))
		    {
		    	keywords.add(object.toString());
		    }
		}
		
		setKeywords(keywords); //calls method setKeywords() in ResourceData
    }
    
    //Creates a E-Utils Link using the keywords
    public void SearchPubMed(String keyword)
    {
        String base = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&term=";
        String link = "";
        
        link = base.concat(keyword);
        
        setEUtil(link); //Saves E-Util link by calling setEUtil() method in resourcedata
        
    }
    
    //Parses XML File using Xerces
    public void ParseXML()
    {
    	ArrayList<String> pmedIds = new ArrayList<String>(); //Array List to hold pmids
    	
    	Document dom = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
           
        try 
        {
        	DocumentBuilder db = dbf.newDocumentBuilder();
                
            //parses xml file found at this URL
            dom = db.parse(getEUtil());
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
        for(int x = 0; x < nl2.getLength(); x++)
        {
        	Element elId = (Element)nl2.item(x);

            pmedIds.add(elId.getFirstChild().getNodeValue()); 
        }
        
        setPIDs(pmedIds); //Calls setPIDs() in ResourceData to save Pub Med IDs
    }
    
    //Searches LLD using Sparql
    public void SearchLLD()
    {
        /*Jason, Ryan, Tim:
         * 
         * reads in the PubMed IDs from the ArrayList to and queries LLD with
         * SPARQL
         */
    }
    
    public void SearchUniProt()
    {
        /* Jason, Ryan, Tim:
         * Reads in the keywords/pubmedIDs(not sure which one) and queries UniProt
         * using Sparql
         */
    }
    
}
