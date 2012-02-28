/*ResourceData.java
 * 
 * The superclass of class BiomaterialEnricher, and  the future Enrichers.  All variables are stored in this class and you 
 * must use the get/set methods when you want to edit the variables in BiomaterialEnricher.
 */
import java.util.ArrayList;
import com.hp.hpl.jena.rdf.model.*;

public class ResourceData {
    
    private ArrayList<String> keywords; //Holds the keywords parsed from RDF
    private ArrayList<String> pIDList; //Holds the IDs parsed from XML
    private String eUtilLink; //Holds the EUtilLink
    private String inputURI; //Holds the biomaterial URI
    private Model model;
    
    public ResourceData(String input)
    {
        inputURI = input;
        
        //Initalizes variables
        keywords = new ArrayList<String>();
        pIDList = new ArrayList<String>();
        eUtilLink = "";
        model = ModelFactory.createDefaultModel();
    }
    
    //Called to set the resource URI
    public void setURI(String site)
    {
        inputURI = site;
    }
    
    //Called to retrieve the resource URI
    public String getURI()
    {
        
        return inputURI;
    }
    
    //Called to set the list of keywords to the ArrayList
    public void setKeywords(ArrayList kw)
    {
        keywords = kw;
    }
    
    //Called to retrieve the keywords list
    public ArrayList getKeywords()
    {
        
        return keywords;
    }
    
    //called to add the PIDs to the PID list
    public void setPIDs(ArrayList PIDs)
    {
    	pIDList.addAll(PIDs);
    }
    
    //Called to retrieve the list of PIDs
    public ArrayList getPIDs()
    {
        return pIDList;
    }
    
    //Called to set the E-utils link to the variable eUtilLink.
    public void setEUtil(String eUtil)
    {
        eUtilLink = eUtil;
    }
    
    //Called to retrieve the E-utils Link with the XML file
    public String getEUtil()
    {
        return eUtilLink;
    }
    
    //Retrieves the RDF model needed to use Jena
    public Model getRDFModel()
    {
      
        model.read(inputURI);
        
        return model;
    }
    
}
