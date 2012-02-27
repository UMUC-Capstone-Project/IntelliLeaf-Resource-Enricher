import java.util.ArrayList;


public class BiomaterialEnricher {
    
    ArrayList<String> keywords; //Holds the keywords parsed from RDF
    ArrayList<String> pIDList; //Holds the IDs parsed from XML
    String eUtilLink = ""; //Holds the EUtilLink
    String inputURI = ""; //Holds the biomaterial URI
    
    public BiomaterialEnricher()
    {
        
    }
    
    public void FindRelatedResources()
    {
        ParseRDFFile();
            	
        for(int x = 0; x < keywords.size(); x++)
        {
            SearchPubMed(keywords.get(x));  //this will use E-utils
            ParseXML();  //this will use Xerces
        }     
    }
    
    public void ParseRDFFile()
    {
        /*Kent's section:
         * Section of code to parse the RDF file from DC-Thera to obtain keywords
         *I think we need to save all the keywords to an instance (global)
         *ArrayList.  Should return an RDF file..more on how it is parsed later
         */
    }
    
    public void SearchPubMed(String keyword)
    {
        /* Section of the code constructing the E-Utils URI using the keywords
         * in the arrayList constructed in ParseRDFFile().  Takes in the parameter
         * 'keyword' from the ArrayList. Saved to an instance String variable.
         */
    }
    
    public void ParseXML()
    {
        /* Nate's section:
         * Section of the code that parses the XML File using Xerces. Saves to 
         * instance ArrayList
         */
    }
    
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
