package executor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


import dbInteractor.FactsRelationsDAO;
import harvestingNewFacts.FactHarvester;

public class NewFactsExtractor {

	public static void main(String[]args) throws ClassNotFoundException, SQLException, UnsupportedEncodingException, FileNotFoundException{
		Date start= new Date();
		System.out.println(start);
		
		//trova per le relazioni le frasi più significative
		FactsRelationsDAO fDao= new FactsRelationsDAO();
		Map<String,List<String>>phraseToRelations= fDao.populatePhraseRelationMapByProbability();
		
		//trova tutte le  entita presenti in yago
		HashSet<String>allEntities= new HashSet<>();
		FactHarvester fha= new FactHarvester();
		fha.getAllEntitiesFromTSV(allEntities);
		
		for(int i=1;i<=57;i++){
		try {
			FactHarvester fh= new FactHarvester("splitted/splitted"+i+".txt",phraseToRelations,allEntities);
			fh.harvestNewFacts(i);
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
