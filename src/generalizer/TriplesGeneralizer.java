package generalizer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.uttesh.exude.exception.InvalidDataException;

import IOUtility.FileInteractor;
import IOUtility.TSVSentencesUtility;

public class TriplesGeneralizer {
	private String pathToFile;
	private TSVSentencesUtility tSVSentencesUtility;
	private List<String[]> allRows;
	private FileInteractor fileInteractor;
	private Generalizer generalizer;


	public TriplesGeneralizer(String pathToFile) throws IOException{
		this.setPathToFile(pathToFile);

		this.tSVSentencesUtility = new TSVSentencesUtility();
		this.allRows = tSVSentencesUtility.getAllSentencesFromTSV(pathToFile);

		this.generalizer= new Generalizer();
		this.fileInteractor=new FileInteractor();
	}


	public void generalizeTriples(int i) throws UnsupportedEncodingException, FileNotFoundException, IOException, InvalidDataException {
		String phraseSemplified="";
		for(String[] phrase : allRows)
		{
			phraseSemplified=this.generalizer.generalizePhraseWithOnlyGeneralization(phrase[1]);

			this.fileInteractor.writeFile(phrase[0]+"\t"+phraseSemplified+"\t"+phrase[2]+"\t"+phrase[1], "splittedGeneralized/generalized"+i+".tsv");
		}

	}

	public String getPathToFile() {
		return pathToFile;
	}


	public void setPathToFile(String pathToFile) {
		this.pathToFile = pathToFile;
	}

}
