package executor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;

import com.sun.media.sound.SimpleSoundbank;

import harvestingNewFacts.FactHarvester;
import verifier.TriplesCounter;
/**
 * serve a contare quante frasi triple hanno soggetto e oggetto appartenenti a yagoTypes o yago_facts
 */
public class YagoFactsVerifier {

	public static void main(String [] args) throws UnsupportedEncodingException, FileNotFoundException{

		//soggetto e oggetto entrambi in yago facts o yago types(modifica metodo)
		int numberOfTriplesWithYAGOEntities = 0;
		
		HashSet<String>allEntities= new HashSet<>();
		FactHarvester fha= new FactHarvester();
		fha.getAllEntitiesFromTSV(allEntities);
		

		for(int i=1;i<=57;i++){
			System.out.println(i);
			try {
				TriplesCounter fh= new TriplesCounter("splitted/splitted"+i+".txt");
				numberOfTriplesWithYAGOEntities+=fh.countEntitiesPresent(allEntities);
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
		System.out.println(numberOfTriplesWithYAGOEntities);
	}
}


