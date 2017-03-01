package executor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import factsValidatorByType.ExtractedFactsFilter;
import factsValidatorByType.RelationToDomainAndRangeBuilder;

public class HarvestedFactsValidator {
	public static void main(String[]args) throws IOException, ClassNotFoundException, SQLException{
		//meglio splittare e riavviare oltre le 150K fraasi
			Date a = new Date();
			System.out.println(a);
			
			HashMap<String,List<String>> relationToDomainAndRange= new HashMap<>();
			
			RelationToDomainAndRangeBuilder smap= new RelationToDomainAndRangeBuilder("yago_schema.tsv");
			smap.relationToDomainAndRangeBuilder(relationToDomainAndRange);
			
			ExtractedFactsFilter efilter= new ExtractedFactsFilter("harvestedFacts.tsv", relationToDomainAndRange);
			efilter.filterFacts();
			
			a = new Date();
			System.out.println(a);
		

	}

}
