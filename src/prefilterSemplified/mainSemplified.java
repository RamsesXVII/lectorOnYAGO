package prefilterSemplified;

import java.util.Date;

public class mainSemplified {

	public static void main(String[] args) {
		Date a = new Date();
		System.out.println(a);
		
		String pathToInputFile= "segment08.tsv";
		boolean useMetrics=false; //se impostato a true allora le frasi del file di input devono essere etichettate!!! (tab Y otab N)
		
		ParallelFilterSemplified pf = new ParallelFilterSemplified();
		pf.filterSentences(pathToInputFile,useMetrics);
		
		
		a = new Date();
		System.out.println(a);

	}

}
