import java.util.ArrayList;

//This section will be incorporated into the main program - I separated into its own file for the time being
//It can be incorporated into BiomaterialEnricher.java
//This is just a start on this section.  SPARQL query still needs to be created to take each PubMed ID to retrieve LLD information

public class SPARQL {
	public void SearchLLD()
    {
    		//create an Array List of PubMed IDs
    		ArrayList<String> PID = new ArrayList<String>();
    		
    		PID = getPIDs();  // collection of the PubMed IDs retrieved from getPIDs() method
    		
    			//go thru and capture each PubMed ID in the 'for' loop and then incorporate each PubMed ID (one-by-one)... 
    			    //...into the SPARQL query to retrieve LLD information
    			for(int x = 0; x < PID.size(); x++)
    			{
					PID.get(x);
					//still have to do this part using SPARQL...
    				//...read in the PubMed IDs from the ArrayList one-by-one and queries LLD with SPARQL for each PubMedID*/
					
					System.out.println(PID.get(x));  //displaying the list of PubMed IDs just to show that this part is working
    			}	
    }
}
