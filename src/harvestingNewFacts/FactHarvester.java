package harvestingNewFacts;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import IOUtility.FileInteractor;
import IOUtility.TSVSentencesUtility;
import dbInteractor.FactsRelationsDAO;

public class FactHarvester {
	private FactsRelationsDAO scoreDAO;

	private TSVSentencesUtility tSVSentencesUtility;
	private List<String[]> allRows;
	private FileInteractor fileInteractor;


	public FactHarvester(String pathToFile) throws ClassNotFoundException, SQLException, UnsupportedEncodingException, FileNotFoundException{
		this.scoreDAO = new FactsRelationsDAO();

		this.tSVSentencesUtility = new TSVSentencesUtility();
		this.allRows = tSVSentencesUtility.getAllSentencesFromTSV(pathToFile);
		this.fileInteractor = new FileInteractor();

	}

	public void harvestNewFacts(int i) throws SQLException, UnsupportedEncodingException, FileNotFoundException, IOException{

		HashSet<String>allEntities= new HashSet<>();
		//this.scoreDAO.getAllEntities(allEntities);
		this.getAllEntitiesFromTSV(allEntities);

		Map<String,List<String>>phraseToRelation=this.populatePhraseRelationMapByProbability();

		for(String[] phrase : allRows)
		{
			if(phraseToRelation.containsKey(phrase[1])){

				List<String>relations=phraseToRelation.get(phrase[1]);
				List<String>relationsInYAGO=new LinkedList<>();

				String subj=scoreDAO.extractID(phrase[0]);
				String obj=scoreDAO.extractID(phrase[2]);

				if (subj.contains("'"))
					subj = subj.replaceAll("'","''");

				if (obj.contains("'"))
					obj = obj.replaceAll("'","''");

				if(allEntities.contains(subj)&&allEntities.contains(obj)){

					for(String relation:relations){
						boolean factAlreadyPresent=false;
						scoreDAO.getAllRelationsBeweenEntitiesDAO(relationsInYAGO, subj, obj);

						for(String relationInYAGO:relationsInYAGO){ //attenzione: potrebbero non esserci relazione tra le entita

							if (relationInYAGO.equals(relation))
								factAlreadyPresent=true;

						}

						if(!factAlreadyPresent)// altrimenti prendeva solo quelli per cui c'era  già una relazione, perchè non erano 111K?
							this.fileInteractor.writeFile(phrase[0]+"\t"+phrase[1]+"\t"+phrase[2]+"\t"+relation, "harvestedFacts.tsv");
					}
				}
			}
		}
	}



	private void getAllEntitiesFromTSV(HashSet<String> allEntities) throws UnsupportedEncodingException, FileNotFoundException {
		TSVSentencesUtility  tSVSentencesU = new TSVSentencesUtility();
		List<String[]> allRow = tSVSentencesU.getAllSentencesFromTSV("uniqueEntities.tsv");
		for(String[] phrase : allRow){
			allEntities.add(phrase[0]);
		}
		
	}

	private Map<String,List<String>> populatePhraseRelationMapByProbability() throws SQLException{
		List<String> relations= new LinkedList<String>();
		this.scoreDAO.getAllRelationsFromScoredFacts(relations);

		Map<String,List<String>> phraseToRelation=new  HashMap<String,List<String>>();

		for(String relation:relations){
			this.scoreDAO.getMostRelevantPhrasesForRelation(relation, phraseToRelation);			
		}

		return phraseToRelation;
	}

}
