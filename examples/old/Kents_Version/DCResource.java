package Kents_Version;

import java.util.ArrayList;
import com.hp.hpl.jena.rdf.model.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DCResource {
	
	private String URI;
	private Model dcModel;
	
	
	public DCResource(String URI){
		
		this.URI = URI;
		this.dcModel = ModelFactory.createDefaultModel();
		this.dcModel.read(URI);
		
	}
	
	public Model getRDF(){
		return dcModel;
	}
	
	public void setURIandRDF(String newURI){
		URI = newURI;
		dcModel = ModelFactory.createDefaultModel();
		dcModel.read(URI);
	}
	
	public String getURI(){
		return URI;
	}
	
    public ArrayList<String> extractBiomaterialObjects()
    {
    	ArrayList<String> keywords = new ArrayList<String>();
    	
    	// list the statements in the Model
    	StmtIterator iter = dcModel.listStatements();

    	// Iterate through the predicate, subject and object of each statement
    	while (iter.hasNext()) {
    			    Statement stmt      = iter.nextStatement();  // get next statement
    			    Resource  subject   = stmt.getSubject();     // get the subject
    			    Property  predicate = stmt.getPredicate();   // get the predicate
    			    RDFNode   object    = stmt.getObject();      // get the object

    			    if (predicate.toString().equals("http://www.w3.org/2000/01/rdf-schema#label")){
    			    	
    			    	String reg1 = "biomaterial";
    			    	Pattern pt1 = Pattern.compile(reg1);
    			    	Matcher mt1 = pt1.matcher(subject.toString());
    			    	if(mt1.find()){keywords.add(object.toString());}
    			    	
    			    	reg1 = "owl";
    			    	pt1 = Pattern.compile(reg1);
    			    	mt1 = pt1.matcher(subject.toString());
    			    	if(mt1.find()){keywords.add(object.toString());}
    			    	
    			    	
    			    }
    	}
    	
    	
    	return keywords;
    	
    }
    
    public ArrayList<String> extractPersonObjects()
    {
    	ArrayList<String> people = new ArrayList<String>();
    	
    	// list the statements in the Model
    	StmtIterator iter = dcModel.listStatements();

    	// Iterate through the predicate, subject and object of each statement
    	while (iter.hasNext()) {
    			    Statement stmt      = iter.nextStatement();  // get next statement
    			    Resource  subject   = stmt.getSubject();     // get the subject
    			    Property  predicate = stmt.getPredicate();   // get the predicate
    			    RDFNode   object    = stmt.getObject();      // get the object

    			    if (predicate.toString().equals("http://www.w3.org/2000/01/rdf-schema#label")){
    			    	
    			       	String reg1 = "(person)";
    			    	Pattern pt1 = Pattern.compile(reg1);
    			    	Matcher mt1 = pt1.matcher(subject.toString());
    			    	if(mt1.find()){people.add(object.toString());}
    			    	
    			    }
    	}
    	
    	
    	return people;
    	
    }
    
    public ArrayList<String> extractDatasetObjects()
    {
    	ArrayList<String> dataset = new ArrayList<String>();
    	
    	// list the statements in the Model
    	StmtIterator iter = dcModel.listStatements();

    	// Iterate through the predicate, subject and object of each statement
    	while (iter.hasNext()) {
    			    Statement stmt      = iter.nextStatement();  // get next statement
    			    Resource  subject   = stmt.getSubject();     // get the subject
    			    Property  predicate = stmt.getPredicate();   // get the predicate
    			    RDFNode   object    = stmt.getObject();      // get the object

    			    if (predicate.toString().equals("http://www.w3.org/2000/01/rdf-schema#label")){
    			    	
    			       	String reg1 = "dataset";
    			    	Pattern pt1 = Pattern.compile(reg1);
    			    	Matcher mt1 = pt1.matcher(subject.toString());
    			    	if(mt1.find()){dataset.add(object.toString());}
    			    
    			    }
    	}
    	
    	
    	return dataset;
    	
    }
    
    public ArrayList<String> extractDirObjects()
    {
    	ArrayList<String> dir = new ArrayList<String>();
    	
    	// list the statements in the Model
    	StmtIterator iter = dcModel.listStatements();

    	// Iterate through the predicate, subject and object of each statement
    	while (iter.hasNext()) {
    			    Statement stmt      = iter.nextStatement();  // get next statement
    			    Resource  subject   = stmt.getSubject();     // get the subject
    			    Property  predicate = stmt.getPredicate();   // get the predicate
    			    RDFNode   object    = stmt.getObject();      // get the object

    			    if (predicate.toString().equals("http://www.w3.org/2000/01/rdf-schema#label")){
    			    	
    			       	String reg1 = "dctheradir";
    			    	Pattern pt1 = Pattern.compile(reg1);
    			    	Matcher mt1 = pt1.matcher(subject.toString());
    			    	if(mt1.find()){dir.add(object.toString());}
 
    			    }
    	}
    	
    	
    	return dir;
    	
    }

}

	
