package verifier;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

import IOUtility.TSVSentencesUtility;
import dbInteractor.FactsRelationsDAO;

public class TriplesCounter {
	private TSVSentencesUtility tSVSentencesUtility;
	private List<String[]> allRows;


	public TriplesCounter(String pathTofile) throws UnsupportedEncodingException, FileNotFoundException { 

		this.tSVSentencesUtility = new TSVSentencesUtility();
		this.allRows = tSVSentencesUtility.getAllSentencesFromTSV(pathTofile);
	}

	public int countEntitiesPresent(HashSet<String> allEntities) throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {
		int conta=0;
		FactsRelationsDAO fdao= new FactsRelationsDAO();

		for(String[] phrase : allRows)
		{
			String subj=fdao.extractID(phrase[0]);
			String obj=fdao.extractID(phrase[2]);

			if(allEntities.contains(subj)&&allEntities.contains(obj))
			{
				conta++;
			}

		}
		return conta;
	}
}
