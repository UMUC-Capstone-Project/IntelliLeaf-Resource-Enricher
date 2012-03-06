package com.intellileaf.dctheradir.enricher.model_processors;
import java.io.FileNotFoundException;

import com.intellileaf.dctheradir.enricher.*;

//Test file to test our Code
public class Test {

	public static void main(String[] args) throws FileNotFoundException 
	{
		PubMedTermSearch object = new PubMedTermSearch();
		
		object.setUri("http://dc-research.eu/rdf/document/191");
		object.isResourceSupported(object.getUri());
		object.run();
		
		Resources.getDirectoryModel();
		
		
		
	}

}
