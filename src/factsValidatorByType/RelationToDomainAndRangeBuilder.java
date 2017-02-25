package factsValidatorByType;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import IOUtility.TSVSentencesUtility;

public class RelationToDomainAndRangeBuilder {

	private TSVSentencesUtility tSVSentencesUtility;
	private List<String[]> allRows;

	public RelationToDomainAndRangeBuilder(String pathToSchema) throws UnsupportedEncodingException, FileNotFoundException{

		this.tSVSentencesUtility = new TSVSentencesUtility();
		this.allRows = tSVSentencesUtility.getAllSentencesFromTSV(pathToSchema);

	}

	public void relationToDomainAndRangeBuilder(HashMap<String,List<String>> relationToDomainAndRange){
		for(String[] phrase : allRows)
		{
			String relation=phrase[0];
			relation=relation.replaceAll("<", "");
			relation=relation.replaceAll(">", "");


			if(phrase[1].contains("rdfs:domain")){
				String domain=phrase[2];
				domain=domain.replaceAll("<", "");
				domain=domain.replaceAll(">", "");

				LinkedList<String>list=new LinkedList<String>();
				list.add(domain);

				relationToDomainAndRange.put(relation,list);
			}

			if(phrase[1].equals("rdfs:range")){
				String range=phrase[2];
				range=range.replaceAll("<", "");
				range=range.replaceAll(">", "");

				relationToDomainAndRange.get(relation).add(range);

			}

		}
	}
}
