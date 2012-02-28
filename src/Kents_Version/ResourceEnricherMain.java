package Kents_Version;

import java.util.ArrayList;

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


}
