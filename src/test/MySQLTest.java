package test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import IOUtility.FileInteractor;
import IOUtility.TSVSentencesUtility;

public class MySQLTest {

	private TSVSentencesUtility tSVSentencesUtility;
	private List<String[]> allRows;
	private FactsRelationsDAO factRelDAO;
	private ProbabilityScoreDAO scoreDAO;
	private FileInteractor fileInteractor;

	public MySQLTest(String pathToFile) throws ClassNotFoundException, SQLException, UnsupportedEncodingException, FileNotFoundException{

		this.tSVSentencesUtility = new TSVSentencesUtility();
		this.allRows = tSVSentencesUtility.getAllSentencesFromTSV(pathToFile);
		this.factRelDAO = new FactsRelationsDAO();
		this.scoreDAO = new ProbabilityScoreDAO();
		this.fileInteractor = new FileInteractor();

	}
	public void getYagoRelations(int i) throws UnsupportedEncodingException, FileNotFoundException, IOException {

		int linec=0;

		for(String[] phrase : allRows)
		{
			List<String> yagoFactsBetweenEntities=new LinkedList<>();

			linec++;
			if(linec%1000==0)
				System.out.println(linec);

			String subj=factRelDAO.extractID(phrase[0]);
			String obj=factRelDAO.extractID(phrase[2]);
			String phraseSentence=phrase[1];
			
			if (subj.contains("'"))
				subj = subj.replaceAll("'","''");
			
			if (obj.contains("'"))
				obj = obj.replaceAll("'","''");

			try{

				factRelDAO.getAllFactsBeweenEntitiesDAO(yagoFactsBetweenEntities,subj,obj);

				for(String relation:yagoFactsBetweenEntities){

					boolean phraseSentenceRelationIsAlreadyPresent=factRelDAO.checkPhraseRelationCorrispondence(relation,phraseSentence);

					if(phraseSentenceRelationIsAlreadyPresent){
						Integer sum=factRelDAO.getCurrentTimesAssociation(relation,phraseSentence);

						sum++;

						factRelDAO.updateAssociationCount(sum.toString(),relation,phraseSentence);

					}

					else{
						factRelDAO.insertNewPhraseRelationCorrispondence(relation,phraseSentence);
					}
				}


			}catch(Exception e){
				this.fileInteractor.writeFile(phrase[0]+"\t"+phrase[1]+"\t"+phrase[2]+"\t", "error"+i+".txt");
				e.printStackTrace();
			}
		}
		factRelDAO.close();

	}
	
	public void getProbabilityScore(int i) throws UnsupportedEncodingException, FileNotFoundException, IOException, SQLException {
		
		Set<String> phrasesWithWitness = new HashSet<>();
		scoreDAO.getAllPhrasesWithWitness(phrasesWithWitness);
		
		for(String phraseSentence : phrasesWithWitness) {
			List<String> factWithWitness = new LinkedList<>();

			try {
				scoreDAO.getAllFactsWithWitnessByPhrase(factWithWitness,phraseSentence);
				for(String relation:factWithWitness){
					double witnessCount = scoreDAO.getWitnessCount(relation,phraseSentence);
					System.out.println("Rel: " + relation);
					System.out.println("Frase: " + phraseSentence);
					System.out.println("Witness: " + witnessCount);
					double witnessCountTotal = 0;
					for (String rel : factWithWitness) {
						witnessCountTotal =  witnessCountTotal + scoreDAO.getWitnessCount(rel,phraseSentence);
					}
					double p = witnessCount/witnessCountTotal;
					System.out.println("P: " + p);	
					double score = Math.log(witnessCount) * p;
					System.out.println("S: " + score);	
					scoreDAO.insertRelationPhraseScore(relation, phraseSentence, score);
				}

			} catch (Exception e){
				e.printStackTrace();
			}
		}
		scoreDAO.close();
	}


}