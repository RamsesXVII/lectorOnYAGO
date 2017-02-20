package dbInteractor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class ProbabilityBuilder1 {

	private ProbabilityScoreDAO scoreDAO;

	public ProbabilityBuilder1() throws ClassNotFoundException, SQLException, UnsupportedEncodingException, FileNotFoundException{

		this.scoreDAO = new ProbabilityScoreDAO();

	}

	public void getProbabilityAndScore() throws UnsupportedEncodingException, FileNotFoundException, IOException, SQLException {

		//prendo tutte le frasi
		Set<String> distinctPhrasesThatHaveWitness = new HashSet<>();
		scoreDAO.getAllDistinctPhrasesThatHaveWitness(distinctPhrasesThatHaveWitness);

		for(String phrase : distinctPhrasesThatHaveWitness) {

			List<RelPhraseCountEntry> relationsAndCountMatchedByPhrase = new LinkedList<>();

			try {
				scoreDAO.getRelationsPhraseCountCorrespondence(relationsAndCountMatchedByPhrase,phrase);
				
				double witnessCountTotal=0;
				
				for(RelPhraseCountEntry phraseRelationEntry:relationsAndCountMatchedByPhrase){
					witnessCountTotal+=phraseRelationEntry.getCount();
				}
				
				for(RelPhraseCountEntry phraseRelationEntry:relationsAndCountMatchedByPhrase){
					
					double witnessCount = phraseRelationEntry.getCount();
					
					double probability = witnessCount/witnessCountTotal;
					double score = Math.log(witnessCount) * probability;

					scoreDAO.insertRelationPhraseScore(phraseRelationEntry.getRelation(), phrase, score,probability);
				}

			} catch (Exception e){
				e.printStackTrace();
			}
		}
		scoreDAO.close();
	}
	
}