package com.intellileaf.dctheradir.enricher;

/**
 * A facility that contains common namespaces, in the form of string constants. Please note that the string fields
 * are defined in lower case here, despite the Java convention of using all-upper-case names for constants. That's 
 * because it is so common to use lower case for RDF/XML namespaces.  
 *
 * <dl><dt>date</dt><dd>Mar 8, 2012</dd></dl>
 * @author brandizi
 *
 */
public class NS
{
	public static final String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	public static final String DCR = "http://dc-research.eu#";
	public static final String RDFS = "http://www.w3.org/2000/01/rdf-schema#";
	public static final String TITLE = "http://purl.org/dc/elements/1.1/title";
	public static final String ABSTRACT = "http://purl.org/dc/elements/1.1/abstract";
	public static final String publication = "http://purl.obolibrary.org/obo/IAO_0000311/"; //Used for PubMed articles
}
