import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.*;
import org.w3c.dom.*;

import org.xml.sax.SAXException;


public class XercesTest {
    
    public static void main(String args[])
    {
        Scanner input = new Scanner(System.in);
        
        System.out.print("Enter E-Util URL: ");
        String eURL = input.nextLine();
        
        ArrayList<String> pmedIds = new ArrayList<String>(); //Array List to hold pmids
	Document dom = null;
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
       
        try 
        {
            DocumentBuilder db = dbf.newDocumentBuilder();
            
            //parses xml file found at this URL
            dom = db.parse(eURL);
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
        
        
        //Prints out ArrayList
        for(int y = 0; y < pmedIds.size(); y++)
        {
            System.out.println(pmedIds.get(y));
        }
        
    }

    
}
