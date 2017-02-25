package phrasesListExpansion;

import java.io.*;

import extractorStanford.FactsListExtractor;
import prefilter.ParallelFilter;

public class mainPhrasesListAnalysis {
	public static void main(String[] args) throws IOException, InterruptedException {
		
		//creo due file Accepted.tsv e Refused.tsv
		String pathToInputFile="evaluationcorpus.tsv";
		boolean useMetrics=false; //le frasi del file di input devono essere etichettate!!! (tab Y otab N)
		ParallelFilter pf = new ParallelFilter();
		pf.filterSentences(pathToInputFile,useMetrics);
		
		FactsListExtractor fe= new FactsListExtractor();
		fe.do_extractFacts();
	}

}
