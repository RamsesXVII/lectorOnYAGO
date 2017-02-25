package prefilterSemplified;

import prefilter.ScoreGetter;

public class ScoreGetterSemplified implements ScoreGetter {
	private double threashold=0.8;

	private String[]listId={"such as","including","i\\.e\\.","like","both","for example","e\\.g\\."};

	private String[]numbersSymbol={" 1 "," 2 "," 3 "," 4 "," 5 "," 6 "," 7 "," 8 "," 9 "," 10 "};
	private String[]numberLiterals={"one","two","three","four","five","six","seven","eight","nine","ten"};

	private String[]listQuantifiers={"some","many","a lot","a lots","different","any","plenty","few"};

	private String entityRegex=".*\\[\\[[a-zA-Z0-9_()/,\\.&-]{0,70}\\|m.[a-zA-Z0-9_]*\\]\\].*";


	@Override
	public double getScore(String phrase){

		double globalVal=0;


		String firstPart=phrase.substring(0, phrase.length()/6);

		if(firstPart.contains("[")==false)
			return 0;

		globalVal+=this.containsListIdFollwedByEntity(phrase)*(0.4); //tiene conto di quella che matcha, quindi devono essercene almeno 2 complessivamente

		if(globalVal>=threashold)
			return globalVal;

		globalVal+=this.containsQuantifiers(phrase)*(0.15); 
		if(globalVal>=threashold)
			return globalVal;

		globalVal+=this.containsColon(phrase)*(0.4);
		if(globalVal>=threashold)
			return globalVal;

		globalVal+=this.containsNumberCommaPattern(phrase)*(0.4);
		if(globalVal>=threashold)
			return globalVal;

		globalVal+=this.getEntityCount(phrase)*(0.1);
		if(globalVal>=threashold)
			return globalVal;

		globalVal+=this.getCommasCount(phrase)*(0.05);
		if(globalVal>=threashold)
			return globalVal;

		return globalVal;
	}



	public int containsColon(String phrase){
		for(int i=0;i<phrase.length();i++){
			if(phrase.charAt(i)==':')
				return 1;
		}
		return 0;
	}

	public int containsListIdFollwedByEntity(String phrase) {
		String listIdRegex;

		for(String s :listId){
			listIdRegex=".*"+s+this.entityRegex;
			if(phrase.matches(listIdRegex))
				return 1;
		}
		return 0;	
	}


	public int containsQuantifiers(String phrase){
		for(String quantifiers:this.listQuantifiers){
			if (phrase.contains(quantifiers))
				return 1;
		}
		return 0;
	}

	public int containsNumberCommaPattern(String phrase){

		for(int i=0;i<10;i++){
			if (phrase.contains(" "+this.numberLiterals[i]+" ")||phrase.contains(this.numbersSymbol[i])){
				if(this.getCommasCount(phrase)>=(i-1)) //la numerazione parte da 0
					return 1;
			}
		}
		return 0;
	}

	public int getEntityCount(String phrase){
		int counter=0;
		for(int i=0;i<phrase.length();i++){
			if(phrase.charAt(i)=='[')
				counter++;
		}
		return counter/2;

	}

	public int getCommasCount(String phrase){
		int counter=0;
		for(int i=0;i<phrase.length();i++){
			if(phrase.charAt(i)==',')
				counter++;
		}
		return counter;
	}

	public double getThreashold() {
		return threashold;
	}

	public void setThreashold(double threashold) {
		this.threashold = threashold;
	}

}