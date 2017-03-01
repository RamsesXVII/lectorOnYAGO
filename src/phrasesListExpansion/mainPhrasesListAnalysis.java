package phrasesListExpansion;

import java.io.*;

import extractorStanford.FactsListExtractor;

public class mainPhrasesListAnalysis {
	public static void main(String[] args) throws IOException, InterruptedException {
		
		FactsListExtractor fe= new FactsListExtractor();
		fe.do_extractFacts();
	}
}
