package executor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.uttesh.exude.exception.InvalidDataException;

import dbInteractor.FactsRelationsDAO;
import harvestingNewFacts.FactHarvester;

public class NewFactsExtractor {

	public static void main(String[]args) throws ClassNotFoundException, SQLException, IOException, InvalidDataException{
		Date start= new Date();
		System.out.println(start);

		//trova per le relazioni le frasi pi� significative
		FactsRelationsDAO fDao= new FactsRelationsDAO();
		fDao.setUseOnlyGeneralized(true); //selezionare
		Map<String,Set<String>>phraseToRelations= fDao.populatePhraseRelationMapByProbability();		

		//trova tutte le  entita presenti in yago
		HashSet<String>allEntities= new HashSet<>();
		FactHarvester fha= new FactHarvester();
		fha.getAllEntitiesFromTSV(allEntities);

		for(int i=1;i<=1;i++){
			try {
				FactHarvester fh= new FactHarvester("phrasesExtracted"+i+".tsv",phraseToRelations,allEntities);
				fh.harvestNewFacts(i,false);//use stoppedor stammed
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		Date end= new Date();
		System.out.println(end);  
	}  

}
