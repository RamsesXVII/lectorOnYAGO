package prefilter;

import java.util.Date;

public class main1 {

	public static void main(String[] args) {
		
		Date a = new Date();
		System.out.println(a);
		
		String pathToInputFile="560000lines.tsv";
		boolean useMetrics=false; //se impostato a true allora le frasi del file di input devono essere etichettate!!! (tab Y otab N)
		
		ParallelFilter pf = new ParallelFilter();
		pf.filterSentences(pathToInputFile,useMetrics);
		
		
		a = new Date();
		System.out.println(a);
		

	}

}
