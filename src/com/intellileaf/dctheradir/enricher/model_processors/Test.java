package com.intellileaf.dctheradir.enricher.model_processors;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.intellileaf.dctheradir.enricher.Resources;

//Test file to test our Code
public class Test {

	public static void main(String[] args) throws FileNotFoundException 
	{
		
		
		String input;
		
		if(args.length > 0){
		input = args[0];}
		
		else{
			input = "http://dc-research.eu/rdf/biomaterial/235";
			System.out.println("Please enter command line argument, using example URI");
		}
		
		BioMaterialEnricher object = new BioMaterialEnricher();
		boolean x;
		object.setUri(input);
		
		x = object.isResourceSupported(object.getUri());
		
		if (x == true){
		
		object.run();
		
		}
		
		else{
			 System.out.println("URI is not a supported resource type");
		}
		
		Model finalModel = ModelFactory.createDefaultModel();
		
		finalModel = Resources.getDirectoryModel ();
		
		//Make sure that file name matches Biomaterial entered above
		
		  try{
			  
			  FileWriter fstream = new FileWriter("./rdf_output/biomaterial235.rdf", false);
			  BufferedWriter output = new BufferedWriter(fstream);
			  finalModel.write(output);
			  output.close();
			  }catch (Exception e){
			  System.err.println("Error: " + e.getMessage());
			  }
	
		
	}

}
