import com.hp.hpl.jena.rdf.model.*;

public class DCResource {
	
	public String URI;
	public Model dcModel;
	
	
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
	
	public void enrichModel(String subject, String predicate, String object){
		
	}

}

	
