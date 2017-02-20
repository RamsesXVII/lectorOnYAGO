package dbInteractor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class ProbabilityBuilder {

	private ProbabilityScoreDAO scoreDAO;

	public ProbabilityBuilder() throws ClassNotFoundException, SQLException, UnsupportedEncodingException, FileNotFoundException{

		this.scoreDAO = new ProbabilityScoreDAO();

	}

	public void getProbabilityScore() throws UnsupportedEncodingException, FileNotFoundException, IOException, SQLException {

		//prendo tutte le frasi
		Set<String> distinctPhrasesThatHaveWitness = new HashSet<>();
		scoreDAO.getAllDistinctPhrasesThatHaveWitness(distinctPhrasesThatHaveWitness);
//		System.out.println(distinctPhrasesThatHaveWitness.size());

		for(String phrase : distinctPhrasesThatHaveWitness) {

			List<String> relationsMatchedByPhrase = new LinkedList<>();

			try {
				scoreDAO.getRelationsPhraseCorrespondence(relationsMatchedByPhrase,phrase);
				
				for(String relation:relationsMatchedByPhrase){
					
					double witnessCount = scoreDAO.getWitnessCount(relation,phrase);
					
//					System.out.println("Rel: " + relation);
//					System.out.println("Frase: " + phrase);
//					System.out.println("Witness: " + witnessCount);
					
					double witnessCountTotal = scoreDAO.getWitnessCountSum(phrase);

					double probability = witnessCount/witnessCountTotal;
				//	System.out.println("P: " + probability);	
					double score = Math.log(witnessCount) * probability;
				//	System.out.println("S: " + score);	
					scoreDAO.insertRelationPhraseScore(relation, phrase, score, probability);
				}

			} catch (Exception e){
				e.printStackTrace();
			}
		}
		scoreDAO.close();
	}
	
}