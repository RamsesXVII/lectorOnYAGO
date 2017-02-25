package factsValidatorByType;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import IOUtility.TSVSentencesUtility;
//USELESS TODO DELETE
public class EntityToTypesBuilder {

	private TSVSentencesUtility tSVSentencesUtility;
	private List<String[]> allRows;

	public EntityToTypesBuilder(String pathToYagoTypes) throws UnsupportedEncodingException, FileNotFoundException{

		this.tSVSentencesUtility = new TSVSentencesUtility();
		this.allRows = tSVSentencesUtility.getAllSentencesFromTSV(pathToYagoTypes);

	}

	public void linkEntitiesToTypes(HashMap<String,List<String>> entityToTypesMap){
		System.out.println("start");
		int i=0;
		for(String[] phrase : allRows)
		{
			i++;
			if(i%10000==0)
				System.out.println(i);
			String entity=phrase[0];
			String type=phrase[2];
			
			if(entityToTypesMap.containsKey(entity)){
				entityToTypesMap.get(entity).add(type);
			}
			else{
				LinkedList<String> types=new LinkedList<>();
				types.add(type);
				types.add("owl:Thing"); //tutto � figlio di thing
				entityToTypesMap.put(entity,types);
				
			}		

		}
	}
}
