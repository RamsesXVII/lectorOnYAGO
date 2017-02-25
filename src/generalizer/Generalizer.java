package generalizer;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import com.uttesh.exude.ExudeData;
import com.uttesh.exude.exception.InvalidDataException;
import com.uttesh.exude.stopping.StoppingParser;

public class Generalizer {


	private HashSet<String>jobs;
	private HashSet<String>cardinals;
	private HashSet<String>nationalities;
	private HashSet<String>numbers;
	private HashSet<String>states;

	public Generalizer() throws FileNotFoundException, IOException{
		this.jobs= new HashSet<String>();
		this.populateMap(jobs,"generalizatorDictionaries/jobs.txt");

		this.cardinals= new HashSet<String>();
		this.populateMap(cardinals,"generalizatorDictionaries/cardinals.txt");

		this.nationalities= new HashSet<String>();
		this.populateMap(nationalities,"generalizatorDictionaries/nationalities.txt");

		this.numbers= new HashSet<String>();
		this.populateMap(numbers,"generalizatorDictionaries/numbers.txt");

		this.states= new HashSet<String>();
		this.populateMap(states,"generalizatorDictionaries/states.txt");

	}

	private void populateMap(HashSet<String> map, String pathToDictionary) throws FileNotFoundException, IOException {
		try  {
			BufferedReader br = new BufferedReader(new FileReader(pathToDictionary));
			String line;
			while ((line = br.readLine()) != null) {
				map.add(line.toLowerCase().replaceAll(" ", ""));
			}}catch(Exception e){

			}
	}

	public String generalizePhraseWithOnlyGeneralization(String output) throws InvalidDataException {

		String stammed=output.toLowerCase().replaceAll(",", "");
		
		String stringToGeneralize=this.getGeneralitiesContained(stammed,jobs);
		if(stringToGeneralize!=null)
			stammed=stammed.replaceAll(stringToGeneralize, "JOB");

		stringToGeneralize=this.getGeneralitiesContained(stammed,cardinals);
		if(stringToGeneralize!=null)
			stammed=stammed.replaceAll(stringToGeneralize, "CAR");

		stringToGeneralize=this.getGeneralitiesContained(stammed,nationalities);
		if(stringToGeneralize!=null)
			stammed=stammed.replaceAll(stringToGeneralize, "NAT");

		stringToGeneralize=this.getGeneralitiesContained(stammed,numbers);
		if(stringToGeneralize!=null)
			stammed=stammed.replaceAll(stringToGeneralize, "NUM");

		stringToGeneralize=this.getGeneralitiesContained(stammed,states);
		if(stringToGeneralize!=null)
			stammed=stammed.replaceAll(stringToGeneralize, "STA");

		if(stammed!=null&&stammed.length()>0){
			String toReturn=stammed;
		return toReturn;
			
		}
		else
			return output;
	}

	public String generalizePhraseWithGeneralizationAndStopping(String output) throws InvalidDataException {
		StoppingParser sparse=StoppingParser.getInstance();
		String stammed = sparse.filterStoppingWords(output.toLowerCase());
		sparse.reset();
		
		String stringToGeneralize=this.getGeneralitiesContained(stammed,jobs);
		if(stringToGeneralize!=null)
			stammed=stammed.replaceAll(stringToGeneralize, "JOB");

		stringToGeneralize=this.getGeneralitiesContained(stammed,cardinals);
		if(stringToGeneralize!=null)
			stammed=stammed.replaceAll(stringToGeneralize, "CAR");

		stringToGeneralize=this.getGeneralitiesContained(stammed,nationalities);
		if(stringToGeneralize!=null)
			stammed=stammed.replaceAll(stringToGeneralize, "NAT");

		stringToGeneralize=this.getGeneralitiesContained(stammed,numbers);
		if(stringToGeneralize!=null)
			stammed=stammed.replaceAll(stringToGeneralize, "NUM");

		stringToGeneralize=this.getGeneralitiesContained(stammed,states);
		if(stringToGeneralize!=null)
			stammed=stammed.replaceAll(stringToGeneralize, "STA");

		if(stammed!=null&&stammed.length()>0){
		String toReturn=this.orderAlphabetically(stammed);
		return toReturn;
			
		}
		else
			return output;
	}
	
	private String orderAlphabetically(String output) {
		String sentence = output;
		StringTokenizer tokenizer = new StringTokenizer(sentence);
		List<String> words = new ArrayList<String>();
		while(tokenizer.hasMoreTokens()) {
			words.add(tokenizer.nextToken());
		}

		Collections.sort(words, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareToIgnoreCase(o2);
			}
		});

		String outputOrdered="";
		for(String s:words)
			outputOrdered=outputOrdered+" "+s;

		if(outputOrdered.length()>0) //non ha stoppato tutto
			return outputOrdered.substring(1);
		else
			return null;
	}

	private String getGeneralitiesContained(String output, HashSet<String> generalitiesSet) {

		StringTokenizer defaultTokenizer = new StringTokenizer(output);
		while (defaultTokenizer.hasMoreTokens())
		{
			String token=defaultTokenizer.nextToken();
			if(generalitiesSet.contains(token))
				return token;
		}
		return null;

	}

	public ExudeData get(){
		return ExudeData.getInstance();
	}
}
