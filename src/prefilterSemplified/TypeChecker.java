package prefilterSemplified;

import bsh.This;
import phrasesFilter.SentencesSplitter;

public class TypeChecker {
	private SentencesSplitter sentenceUtility; //metodi di utility utili in questa fase

	public TypeChecker(){
		this.sentenceUtility= new SentencesSplitter();
	}

	public boolean existTypePatter(String phrase){
		int numberOfEntities=this.sentenceUtility.getNumberOfEntities(phrase);

		for(int i=1;i<=numberOfEntities;i++){

			//	String phraseBetweenEntities=(phrase.substring(this.sentenceUtility.getNthOccurenceOfCharacter(phrase, "]]", i), this.sentenceUtility.getNthOccurenceOfCharacter(phrase, "[[", i+1)));

			//System.out.println(phraseBetweenEntities);

			int prev=this.sentenceUtility.getNthOccurenceOfCharacter(phrase, "[[", i);
			int next=this.sentenceUtility.getNthOccurenceOfCharacter(phrase, "]]", i);
			System.out.println(phrase.substring(prev,next+2));
			System.out.println(this.sentenceUtility.getNthOccurenceOfCharacter(phrase, phrase.substring(prev,next+2), 1));
			System.out.println(this.getNumberOfOccurenciesOfEntity(phrase, phrase.substring(prev,next+2)));
			/*		if(next-prev<10){
				String entityPrev=this.getEntityPrev(phrase,prev,i);
				String entityNext=this.getentityNext(phrase,prev,i);
			}*/

			//	System.out.println(this.getEntityIDPrev(prev, phrase));

		}
		return true;
	}

	public int getNumberOfOccurenciesOfEntity(String phrase,String entity){
		int count=0;
		for(int i=0;i<100;i++)
			if(this.sentenceUtility.getNthOccurenceOfCharacter(phrase,entity, i)==-1)
				break;
			else
				count++;
		return count;
	}

}

