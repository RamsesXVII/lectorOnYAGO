package phrasesFilter;

import java.io.IOException;

public class main2 {

	public static void main(String[] args) {

		for(int i=3;i<=57;i++){
			SentencesSplitter s= new SentencesSplitter("file"+i+".tsv");

			try {
				s.splitSentences(i);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
