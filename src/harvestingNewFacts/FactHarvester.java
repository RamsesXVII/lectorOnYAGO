package harvestingNewFacts;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import IOUtility.FileInteractor;
import IOUtility.TSVSentencesUtility;
import dbInteractor.FactsRelationsDAO;

public class FactHarvester {
	private FactsRelationsDAO scoreDAO;

	private TSVSentencesUtility tSVSentencesUtility;
	private List<String[]> allRows;
	private FileInteractor fileInteractor;

	private Map<String,Set<String>>phraseToRelation;
	private HashSet<String> allEntities;

	public FactHarvester(){

	}

	public FactHarvester(String pathToFile,Map<String,Set<String>>phraseToRelation,HashSet<String> allEntities) throws ClassNotFoundException, SQLException, IOException{
		this.scoreDAO = new FactsRelationsDAO();

		this.tSVSentencesUtility = new TSVSentencesUtility();
		this.allRows = tSVSentencesUtility.getAllSentencesFromTSV(pathToFile);
		this.fileInteractor = new FileInteractor();

		this.phraseToRelation=phraseToRelation;
		this.allEntities=allEntities;

	}

	public void harvestNewFacts(int i, boolean inputIsStopperOrStammed) throws SQLException, UnsupportedEncodingException, FileNotFoundException, IOException{

		for(String[] phrase : allRows)
		{
			if(phraseToRelation.containsKey(phrase[1])){

				Set<String>relations=phraseToRelation.get(phrase[1]);
				HashSet<String>relationsInYAGO=new HashSet<>();

				String subj=scoreDAO.extractID(phrase[0]);
				String obj=scoreDAO.extractID(phrase[2]);

				if(allEntities.contains(subj)&&allEntities.contains(obj)){

					for(String relation:relations){
						boolean factAlreadyPresent=false;

						if (subj.contains("'"))
							subj = subj.replaceAll("'","''");

						if (obj.contains("'"))
							obj = obj.replaceAll("'","''");

						scoreDAO.getAllRelationsBeweenEntitiesDAO(relationsInYAGO, subj, obj);

						for(String relationInYAGO:relationsInYAGO){ //attenzione: potrebbero non esserci relazione tra le entita

							if (relationInYAGO.equals(relation))
								factAlreadyPresent=true;

						}

						if(!factAlreadyPresent){// altrimenti prendeva solo quelli per cui c'era  gi� una relazione, perch� non erano 111K?
							if(inputIsStopperOrStammed)
								this.fileInteractor.writeFile(phrase[0]+"\t"+phrase[3]+"\t"+phrase[2]+"\t"+relation+"\t"+phrase[1], "harvestedFacts.tsv");
							else
								this.fileInteractor.writeFile(phrase[0]+"\t"+phrase[1]+"\t"+phrase[2]+"\t"+relation, "harvestedFacts.tsv");
						}
					}
				}
			}
		}
	}



	public void getAllEntitiesFromTSV(HashSet<String> allEntities) throws UnsupportedEncodingException, FileNotFoundException {
		TSVSentencesUtility  tSVSentencesU = new TSVSentencesUtility();
		List<String[]> allRow = tSVSentencesU.getAllSentencesFromTSV("uniqEntitiesFromYAGOTypes.tsv");

		for(String[] phrase : allRow){
			allEntities.add(phrase[0]);
		}
	}
}
