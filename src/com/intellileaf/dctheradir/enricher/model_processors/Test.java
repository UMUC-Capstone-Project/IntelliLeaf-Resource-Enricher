package com.intellileaf.dctheradir.enricher.model_processors;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

import com.hp.hpl.jena.rdf.model.Model;
import com.intellileaf.dctheradir.enricher.Resources;

//Test file to test our Code
public class Test {

	public static void main(String[] args) throws FileNotFoundException 
	{
		String[] inputs;
		
		if(args.length > 0)
			inputs = args;
		else{
			inputs = new String[] { 
				"http://dc-research.eu/rdf/biomaterial/522", 
				"http://dc-research.eu/rdf/biomaterial/120" /*
				"http://dc-research.eu/rdf/biomaterial/508",
				"http://dc-research.eu/rdf/biomaterial/526",
				"http://dc-research.eu/rdf/biomaterial/61" */				 
			};
		}
		
		for ( String input: inputs )
		{
			BioMaterialEnricher bmEnricher = new BioMaterialEnricher();
			bmEnricher.setUri(input);
			
			if ( bmEnricher.isResourceSupported(bmEnricher.getUri()) ){
				bmEnricher.run();
			}
			else{
				 System.out.println("URI is not a supported resource type");
			}
			
		} // for input
		
		Model finalModel = Resources.getDirectoryModel ();
		
		//Make sure that file name matches Biomaterial entered above
		
		  try{
		  	finalModel.write ( new BufferedWriter ( new FileWriter ( new File ( "./rdf_output/test_output.n3" ) ) ), "N3" );
			}
		  catch (Exception e){
			  e.printStackTrace ();
			}
		  
		  try{
			  	finalModel.write ( new BufferedWriter ( new FileWriter ( new File ( "./rdf_output/test_output.rdf" ) ) ) );
				}
			  catch (Exception e){
				  e.printStackTrace ();
				}
	}
}
