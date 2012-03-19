import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.*;
import java.util.*;
import java.io.*;

public class sparql_util {


	public static void main(String[] args) {
		String sparqlQueryString1=  
			"SELECT ?s ?o "+
			"WHERE {"+
			"?s ?p ?o ."+
			"?o <bif:contains> \"Selectin\" ."+
			"}"+
			"limit 10";

			Query query = QueryFactory.create(sparqlQueryString1);
			QueryExecution qexec = QueryExecutionFactory.sparqlService("http://pubmed.bio2rdf.org/sparql", query);

			ResultSet results = qexec.execSelect();
			System.out.println("Query: Selectin");
			ResultSetFormatter.out(System.out, results, query);       

			qexec.close() ;	

	}

}

