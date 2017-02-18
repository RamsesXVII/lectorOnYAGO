package dbInteractor;

public class RelPhraseCountEntry {
	private String phrase;
	private String relation;
	private Integer count;
	
	public RelPhraseCountEntry(String phrase, String relation, String count) {
		this.phrase = phrase;
		this.relation = relation;
		this.count = new Integer(count);
	}
	
	public String getPhrase() {
		return phrase;
	}
	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	

}
