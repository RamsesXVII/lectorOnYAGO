package executor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import harvestingNewFacts.FactHarvester;

public class NewFactsExtractor {
	public static void main(String[]args){
		Date start= new Date();
		System.out.println(start);
		
		for(int i=1;i<=57;i++){
		try {
			FactHarvester fh= new FactHarvester("splitted/splitted"+i+".txt");
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
