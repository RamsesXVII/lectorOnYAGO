package evaluatingUtilityFactsExtracted;

import java.io.IOException;

import com.uttesh.exude.exception.InvalidDataException;

public class RelationHarvestedCounter {

	public static void main(String[] args) throws IOException, InvalidDataException {
		FactsHarvestedEvaluator fevaluator= new FactsHarvestedEvaluator("validatedFactsStoppedAndGeneralized.tsv");
		fevaluator.countFactsExtracterByRelation();
	}

}
