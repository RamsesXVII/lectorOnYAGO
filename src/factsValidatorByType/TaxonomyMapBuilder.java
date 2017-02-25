package factsValidatorByType;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import IOUtility.TSVSentencesUtility;

public class TaxonomyMapBuilder {
	private TSVSentencesUtility tSVSentencesUtility;
	private List<String[]> allRows;

	public TaxonomyMapBuilder(String pathToTaxonomyTSV) throws UnsupportedEncodingException, FileNotFoundException{

		this.tSVSentencesUtility = new TSVSentencesUtility();
		this.allRows = tSVSentencesUtility.getAllSentencesFromTSV(pathToTaxonomyTSV);

	}

	public void wikiCatToWordNetOrGeoEntity(HashMap<String,String> wikicatToWordNet){
		for(String[] phrase : allRows)
		{
			if(phrase[1].contains("rdfs:subClassOf")){
			String wikicat=phrase[0];
			String wordnetOrGeoEntity=phrase[2]; //sono tutte o wikinet o geoEntity
			
			wikicatToWordNet.put(wikicat, wordnetOrGeoEntity);
			}


		}
	}

}
