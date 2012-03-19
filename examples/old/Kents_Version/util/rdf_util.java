import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.*;
import java.util.*;
import java.io.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class rdf_util {

	public static void main(String[] args) {
		
		List<String> obj = new ArrayList<String>();
		
		 Scanner input = new Scanner(System.in);
	        
	     System.out.print("Enter URI: ");
	        
	     String userIn = input.nextLine();

		// create an empty Model
		Model model = ModelFactory.createDefaultModel();
		
		// read the RDF/XML file
		model.read(userIn);
		
		// write it to standard out
		//model.write(System.out);
		
		// list the statements in the Model
		StmtIterator iter = model.listStatements();
		
		System.out.println();

		// print out the predicate, subject and object of each statement
		while (iter.hasNext()) {
		    Statement stmt      = iter.nextStatement();  // get next statement
		    Resource  subject   = stmt.getSubject();     // get the subject
		    Property  predicate = stmt.getPredicate();   // get the predicate
		    RDFNode   object    = stmt.getObject();      // get the object

		    
		System.out.print(subject.toString());
		    System.out.print(" -> " + predicate.toString() + " -> ");
		    if (object instanceof Resource) {
		       System.out.print(object.toString()+"\n");
		    } else {
		        // object is a literal
		        System.out.print(" \"" + object.toString() + "\"\n");
		    }
		} 		 
		   
		/* for(int i = 0; i < (obj.size()); i++){
			
			String sparqlQueryString1=  
										"SELECT ?s ?o "+
										"WHERE {"+
										"?s ?p ?o ."+
										"?o <bif:contains> \""+obj.get(i)+"\" ."+
										"}"+
										"limit 10";
			
				      Query query = QueryFactory.create(sparqlQueryString1);
				      QueryExecution qexec = QueryExecutionFactory.sparqlService("http://pubmed.bio2rdf.org/sparql", query);

				      ResultSet results = qexec.execSelect();
				      System.out.println("Query: "+obj.get(i));
				      ResultSetFormatter.out(System.out, results, query);       

				     qexec.close() ;	
		} */
		
	}
}
