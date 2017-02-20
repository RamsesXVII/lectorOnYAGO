package harvestingNewFacts;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashMap;
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

				for(String relation:relations){
					boolean factPresent=false;
					scoreDAO.getAllRelationsBeweenEntitiesDAO(relationsInYAGO, subj, obj);

					for(String relationInYAGO:relationsInYAGO){
						if (relationInYAGO.equals(relation))
							factPresent=true;
					}

					if(factPresent)
						this.fileInteractor.writeFile(phrase[0]+"\t"+phrase[1]+"\t"+phrase[2]+"\t"+relation, "harvestedFacts.tsv");
				}
			}
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
