package com.intellileaf.dctheradir.enricher.model_processors;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import com.hp.hpl.jena.rdf.model.Model;
import com.intellileaf.dctheradir.enricher.Resources;

public class DirectoryEnricher {

	public static void main(String[] args) throws FileNotFoundException 
	{
		String[] inputs;
		
		if(args.length > 0)
			inputs = args;
		else{
			/*
			 * WARNING: You've to hand the JVM enough memory to run this. 
			 * In Eclipse: Run Configurations -> DirectoryEnricher -> VM arguments: -Xms512m -Xmx2G -XX:PermSize=128m -XX:MaxPermSize=1G
			 * Via command line: java -Xms512M ... Test
			 */
			inputs = new String[] { 
				"http://dc-research.eu/rdf/biomaterial/522"/*, 
				"http://dc-research.eu/rdf/biomaterial/120", 
				"http://dc-research.eu/rdf/biomaterial/508",
				"http://dc-research.eu/rdf/biomaterial/526",
				"http://dc-research.eu/rdf/biomaterial/61"	*/			 
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
		
		//String outPath = "/Users/brandizi/Documents/Work/IntelliLeaf/capstone_project/dcthera_enrich_relfinder/Joseki-3.4.4/dcthera/dcthera_enriched.rdf";
		String outPath = "./rdf_output/test_output.rdf";
		  
	  try{
	  	finalModel.write ( 
	  		new BufferedWriter ( 
	  			new OutputStreamWriter ( new FileOutputStream ( outPath ), "UTF-8" )
	  		), 
	  		"RDF/XML-ABBREV"
	  	);
		}
	  catch (Exception e){
		  e.printStackTrace ();
		}
	  
	} // main
}
