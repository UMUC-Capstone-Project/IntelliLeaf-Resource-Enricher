package com.intellileaf.dctheradir.enricher;

import java.io.FileNotFoundException;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * Miscellanea of utility functions.
 *
 * <dl><dt>date</dt><dd>Feb 28, 2012</dd></dl>
 *
 */
public class Utils
{
	/**
	 * Merges the source model into the dest model, ie, add all the statements in the source to the destination.
	 * 
	 * @param srcModel
	 * @param destModel
	 */
	public static void mergeGraphs ( Model srcModel, Model destModel )
	{
		destModel.add ( srcModel );

	}

	/**
	 * Invokes {@link #mergeGraphs(Model, Model)} using {@link Resources#getDirectoryModel()} as destination
	 * @param srcModel
	 * @throws FileNotFoundException 
	 */
	public static void mergeGraphs ( Model srcModel ) throws FileNotFoundException
	{
		mergeGraphs ( srcModel, Resources.getDirectoryModel () );
		
	}

}
