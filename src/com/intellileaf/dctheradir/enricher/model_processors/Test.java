package com.intellileaf.dctheradir.enricher.model_processors;
import java.io.FileNotFoundException;

import com.intellileaf.dctheradir.enricher.*;

//Test file to test our Code
public class Test {

	public static void main(String[] args) throws FileNotFoundException 
	{
		/*BioMaterialEnricher object = new BioMaterialEnricher();
		boolean x;
		object.setUri("http://dc-research.eu/rdf/biomaterial/522");
		x = object.isResourceSupported(object.getUri());
		System.out.println(x);
		//object.run();
		
		Resources.getDirectoryModel();
		*/
		
		BioMaterialEnricher object = new BioMaterialEnricher();
		
		object.run();
	}

}
