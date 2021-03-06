package Kents_Version;

import java.io.IOException;
import java.util.ArrayList;
import com.hp.hpl.jena.rdf.model.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;


public class BiomaterialEnricher {
    
    private DCResource Biomaterial;
    private static final String ReturnMax = "10";
    
    public BiomaterialEnricher(DCResource Biomaterial)
    {
        this.Biomaterial = Biomaterial;
    }
    
    public void FindRelatedResources()
    {
    	
    }
    
    
    public ArrayList<String> searchPubMed(String alg, String db, String keyword, String field)
    {
    	
    	ArrayList<String> pIDList = new ArrayList<String>(); //Holds the IDs parsed from XML

    	Document dom = null;
    	        
    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    	
    	String eURL = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/"+alg+".fcgi?db="+db+"&term="+
    				   keyword+"&RetMax="+ReturnMax+"&field="+field;
    	       
    	        try 
    	        {
    	            DocumentBuilder docb = dbf.newDocumentBuilder();
    	            
    	            //parses xml file found at this URL
    	            dom = docb.parse(eURL);
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
    	        
    	        //Loops through the "Id" node list and adds the PubIds to an ArrayList
    	        for(int x = 0; x < nl2.getLength(); x++)
    	        {
    	            Element elId = (Element)nl2.item(x);

    	            pIDList.add(elId.getFirstChild().getNodeValue()); 
    	        }
    	        
    	        
    	        return pIDList;
    	
    }
    
    
    public void SearchLLD()
    {
        /* Jason, Ryan, Tim:
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
    
public DCResource enrichModel(String subject, String predicate, String object){
	
		return Biomaterial;
	}
}
