package com.intellileaf.dctheradir.enricher.model_processors;
import java.io.FileNotFoundException;

import com.intellileaf.dctheradir.enricher.*;

//Test file to test our Code
public class Test {

	public static void main(String[] args) throws FileNotFoundException 
	{
		
		
		String input;
		
		String DCThera = "http://dc-research.eu/rdf/";
		if(args.length > 0){
		input = args[0];}
		
		else{
			input = "biomaterial/522";
			System.out.println("Please enter command line argument, using example URI");
		}
		
		BioMaterialEnricher object = new BioMaterialEnricher();
		boolean x;
		object.setUri(DCThera+input);
		x = object.isResourceSupported(object.getUri());
		
		if (x == true){
		
		object.run();
		
		}
		
		else{
			 System.out.println("URI is not a supported resource type");
		}
	}

}
