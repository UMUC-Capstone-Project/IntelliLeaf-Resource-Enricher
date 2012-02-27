import java.util.ArrayList;

public class ResourceEnricherMain {

	
	public static void main(String[] args) {
		
		String DCThera = "http://dc-research.eu/rdf/";
		String input = args[0];
		ArrayList<String> pID = new ArrayList<String>();
		
		String URI = DCThera+input;
		
		DCResource Biomaterial = new DCResource(URI);
		
		BiomaterialEnricher EnrichIntel = new BiomaterialEnricher(Biomaterial);
		
		ArrayList<String> keywords = EnrichIntel.extractStringObjects();
		
		for (int i = 0; i < keywords.size(); i++){
			
			System.out.println("Query: "+keywords.get(i));
			
			pID = EnrichIntel.searchPubMed("esearch","pubmed",keywords.get(i),"10");
			
			for (int x = 0; x < pID.size(); x++){
			System.out.println(pID.get(x));
			}
			
			
		}
		
	}


}
