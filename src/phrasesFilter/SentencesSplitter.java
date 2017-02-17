package phrasesFilter;

import java.io.IOException;

import java.util.List;
import IOUtility.*;
import relationIdentifier.RelationalIdentifier;

public class SentencesSplitter {
	private String pathTofile;
	private RelationalIdentifier rIdentifier;

	public SentencesSplitter(String  pathToTSVInputFile){
		this.pathTofile=pathToTSVInputFile;
		this.rIdentifier=new RelationalIdentifier();

	}

	public void splitSentences(int j) throws IOException{
		int contaRiga=1;

		TSVSentencesUtility t = new TSVSentencesUtility();
		List<String[]> allRows = t.getAllSentencesFromTSV(pathTofile);

		FileInteractor f = new FileInteractor();

		for(String[] phrase : allRows)
		{
		//	System.out.println(contaRiga);
			if(phrase[3]!=null )
			{	
				int entitiesCount= getNumberOfEntities(phrase[3]);

				if(entitiesCount>1)
				{

					for(int i=1;i<entitiesCount;i++){

						try{
							String phraseWithEntities=(phrase[3].substring(getNthOccurenceOfCharacter(phrase[3], "[[", i), getNthOccurenceOfCharacter(phrase[3], "]]", i+1)+2));
							String phraseWithOutEntities=this.getPhraseBetweenEntities(phraseWithEntities);

							String firstEntity=this.getFirstEntity(phraseWithEntities);
							String secondEntity=this.getSecondEntity(phraseWithEntities);

							String phraseToWrite=firstEntity+"\t"+phraseWithOutEntities+"\t"+secondEntity;

							if(this.rIdentifier.isRelational(phraseWithOutEntities))
								f.writeFile(phraseToWrite,"splitted"+j+".txt");
						}catch(StringIndexOutOfBoundsException e){}
							//	f.writeFile((phrase[3].substring(getNthOccurenceOfCharacter(phrase[3], "[[", i), getNthOccurenceOfCharacter(phrase[3], "]]", i+1)+2)),"splitted.txt");
						}
					}
				}
		//		contaRiga++;
			}
		}

		public int getNthOccurenceOfCharacter(String str, String substr, int n) {
			int pos = str.indexOf(substr);
			while (--n > 0 && pos != -1)
				pos = str.indexOf(substr, pos + 1);
			return pos;
		}


		public int getNumberOfEntities(String string){
			int counter = 0;

			for( int i=0; i<string.length(); i++ ) {
				if(string.charAt(i)=="[".charAt(0)) {
					counter++;
				} 
			}
			return counter/2;
		}

		private String getSecondEntity(String phraseWithEntities) {
			return phraseWithEntities.substring(this.getNthOccurenceOfCharacter(phraseWithEntities, "[[", 2),this.getNthOccurenceOfCharacter(phraseWithEntities, "]]", 2)+2);

		}

		private String getFirstEntity(String phraseWithEntities) {
			return phraseWithEntities.substring(0,this.getNthOccurenceOfCharacter(phraseWithEntities, "]]", 1)+2);

		}

		private String getPhraseBetweenEntities(String phraseWithEntities) {
			return phraseWithEntities.substring(this.getNthOccurenceOfCharacter(phraseWithEntities, "]]", 1)+2, this.getNthOccurenceOfCharacter(phraseWithEntities, "[[", 2));
		}


	}
