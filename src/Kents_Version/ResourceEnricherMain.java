package Kents_Version;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResourceEnricherMain {

	
	public static void main(String[] args) {
		
		String input;
		
		String DCThera = "http://dc-research.eu/rdf/";
		if(args.length > 0){
		input = args[0];}
		
		else{
			input = "biomaterial/522";
			System.out.println("Please enter command line argument, using example URI");
		}
		
		//Test to see if input URI is resource type Biomaterial
		
		String reg1 = "biomaterial";
    	Pattern pt1 = Pattern.compile(reg1);
    	Matcher mt1 = pt1.matcher(input);
    	if(mt1.find()){
		
		ArrayList<String> pID = new ArrayList<String>();
		ArrayList<String> authID = new ArrayList<String>();
		
		String URI = DCThera+input;
		
		DCResource Biomaterial = new DCResource(URI);
		
		BiomaterialEnricher EnrichIntel = new BiomaterialEnricher(Biomaterial);
		
		ArrayList<String> keywords = Biomaterial.extractBiomaterialObjects();
		
		for (int i = 0; i < keywords.size(); i++){
			
			System.out.println("Query: "+keywords.get(i));
			
			pID = EnrichIntel.searchPubMed("esearch","pubmed",keywords.get(i),"");
			
			for (int x = 0; x < pID.size(); x++){
			System.out.println("http://www.ncbi.nlm.nih.gov/pubmed/"+pID.get(x));
			}	
		}
			
		ArrayList<String> people = Biomaterial.extractPersonObjects();
			
		for (int i = 0; i < people.size(); i++){
				
			System.out.println("Query: "+people.get(i));
				
			authID = EnrichIntel.searchPubMed("esearch","pubmed",people.get(i),"auth");
				
			for (int x = 0; x < authID.size(); x++){
			System.out.println("http://www.ncbi.nlm.nih.gov/pubmed/"+authID.get(x));
			}	
		}
		
    	}
		
    	//Test to see if input URI is resource type Person
    	String reg2 = "person";
    	Pattern pt2 = Pattern.compile(reg2);
    	Matcher mt2 = pt2.matcher(input);
    	if(mt2.find()){
    		
    	//place holder
    	}
    	
    	//Test to see if input URI is resource type Dataset
    	String reg3 = "dataset";
    	Pattern pt3 = Pattern.compile(reg3);
    	Matcher mt3 = pt3.matcher(input);
    	if(mt3.find()){
    		
    		//place holder
    	}
    	
    	
	}


}
