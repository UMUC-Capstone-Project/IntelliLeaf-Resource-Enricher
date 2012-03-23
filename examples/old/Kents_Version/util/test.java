

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;


public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

			
    	Document dom = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	
		//Loop to loop through the term labels, search each one with Utils, and parse the resulting XML file
        //Adds the IDs to the list pmids and adds the statements to the model
      
        
        	String link = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id=21625591&retmode=xml";
        	
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
                
           
            Element Ele = dom.getDocumentElement();
            NodeList nl = Ele.getElementsByTagName("ArticleTitle");
                
            Element elId = (Element)nl.item(0);
            String title = (elId.getFirstChild().getNodeValue());
            
            NodeList n2 = Ele.getElementsByTagName("AbstractText");
                
            Element ab1 = (Element)n2.item(0);
            String abstrct = (ab1.getNodeValue());
            
            System.out.println(title);
            System.out.println(abstrct);
           
           
        }

}


