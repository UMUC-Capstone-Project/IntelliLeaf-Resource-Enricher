##DC-THERA Resource Enricher

A Java application designed to generate relationships between resources from the DC-RESEARCH website and scientific journal articles (Pubmed), proteins (Uniprot), and their associated relationships. 
For example, Pubmed articles are linked to associated LifeSkim concept terms, and proteins are linked to their Gene Ontology annotations, biological pathways (Reactome, Pathway Interaction DB),and other databases (KEGG, array express).

The resulting RDF graph provides novel relationships between resources and can graphically viewed using a number of tools such as RelFinder, Cytoscape, RDF viewer, etc.

While the application utilizes the DC-THERA ontology for some relationships and accepts a DC-RESEARCH URI as input, the program can be edited to input general biological terms and output a similar RDF graph.

#Authors
Marco Brandizi, Jason Earley, Nathaniel Gatewood, Ryan Gruber, Tim Wilson, and Kent Shefchek

The DC-THERA Resource Enricher was a capstone project for the University of Maryland, University College
Biotechnology/Bioinformatics Master's program


#Images

[Representation of the RDF graph created by the DC-THERA enricher](https://github.com/UMUC-Capstone-Project/IntelliLeaf-Resource-Enricher/blob/master/images/RDFGraph.JPG)

[Here is an example of results graphically viewed in RelFinder](https://github.com/UMUC-Capstone-Project/IntelliLeaf-Resource-Enricher/blob/master/images/example.JPG)

#External Links
[DC-THERA Web site](http://dc-research.eu/)

