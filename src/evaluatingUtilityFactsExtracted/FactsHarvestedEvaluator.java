package evaluatingUtilityFactsExtracted;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.uttesh.exude.exception.InvalidDataException;

import IOUtility.FileInteractor;
import IOUtility.TSVSentencesUtility;

public class FactsHarvestedEvaluator {
	private String pathToFile;
	private TSVSentencesUtility tSVSentencesUtility;
	private List<String[]> allRows;
	private FileInteractor fileInteractor;

	private Map<String,Integer>relationToCount;


	public FactsHarvestedEvaluator(String pathToFile) throws IOException{
		this.setPathToFile(pathToFile);

		this.tSVSentencesUtility = new TSVSentencesUtility();
		this.allRows = tSVSentencesUtility.getAllSentencesFromTSV(pathToFile);

		this.fileInteractor=new FileInteractor();

		this.relationToCount=new HashMap<String,Integer>();


	}

	public void countFactsExtracterByRelation() throws UnsupportedEncodingException, FileNotFoundException, IOException, InvalidDataException {
		for(String[] phrase : allRows)
		{
			if(this.relationToCount.containsKey(phrase[3])){
				int i=this.relationToCount.get(phrase[3]);
				this.relationToCount.put(phrase[3],i+1);
			}
			else
				this.relationToCount.put(phrase[3],1);

		}	
		for(String s:this.relationToCount.keySet())
		this.fileInteractor.writeFile(s+"\t"+this.relationToCount.get(s), "relationCounter.tsv");

	}

	public String getPathToFile() {
		return pathToFile;
	}

	public void setPathToFile(String pathToFile) {
		this.pathToFile = pathToFile;
	}
}
