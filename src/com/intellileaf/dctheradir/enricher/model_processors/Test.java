package com.intellileaf.dctheradir.enricher.model_processors;
import java.io.FileNotFoundException;

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
			input = "http://dc-research.eu/rdf/biomaterial/522";
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
		
		//Test code
		 //finalModel.write(System.out, "TURTLE");
		
		
	}

}
