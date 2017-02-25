package dbInteractor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.uttesh.exude.exception.InvalidDataException;

import generalizer.Generalizer;

public class FactsRelationsDAO {   

	private Generalizer generalizer;

	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	boolean useStoppedAndGeneralized=false;
	boolean useOnlyGeneralized=false;

	private boolean useNotModifiedWords=false;



	public FactsRelationsDAO() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException{
		this.generalizer=new Generalizer();
		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager.getConnection("jdbc:mysql://localhost/lector?user=root&password=root");
	}

	public void insertNewPhraseRelationCorrispondence(String relation, String phraseSentence) throws SQLException {
		preparedStatement = connect.prepareStatement("insert into lector.relPhraseCount values('"+relation+"','"+phraseSentence+"',1);");
		preparedStatement.execute();

	}
	public void updateAssociationCount(String sum, String relation, String phraseSentence) throws SQLException {
		preparedStatement = connect.prepareStatement("update lector.relPhraseCount set sum='"+sum+"' where rel='"+relation+"' and phrase='"+phraseSentence+"';");
		preparedStatement.executeUpdate();

	}
	public Integer getCurrentTimesAssociation(String relation, String phraseSentence) throws SQLException {
		preparedStatement = connect.prepareStatement("select sum as s from lector.relPhraseCount where rel='"+relation+"' and phrase='"+phraseSentence+"';");
		resultSet = preparedStatement.executeQuery();

		resultSet.next();

		String sum= resultSet.getString("s");

		return new Integer(sum);
	}

	public boolean checkPhraseRelationCorrispondence(String relation, String phraseSentence) throws SQLException {

		preparedStatement = connect.prepareStatement("select exists(select * from lector.relPhraseCount where rel='"+relation+"' and phrase='"+phraseSentence+"') as isPresent;");
		resultSet = preparedStatement.executeQuery();

		resultSet.next();

		String isPresent = resultSet.getString("isPresent");

		if(isPresent.equals("1"))
			return true;
		else
			return false;
	}

	public void getAllRelationsBeweenEntitiesDAO(Set<String> yagoFactsBetweenEntities,String subj,String obj) throws SQLException {

		preparedStatement = connect.prepareStatement("select * from lector.yagofacts where subj='"+subj+"' and obj='"+obj+"';");
		resultSet = preparedStatement.executeQuery();

		while(resultSet.next())
			yagoFactsBetweenEntities.add(resultSet.getString("rel"));
	}

	public void getAllEntities(HashSet<String> yagoEntities) throws SQLException {

		preparedStatement = connect.prepareStatement("select distinct subj from lector.yagofacts;");
		resultSet = preparedStatement.executeQuery();

		while(resultSet.next())
			yagoEntities.add(resultSet.getString("subj"));

		preparedStatement = connect.prepareStatement("select distinct obj from lector.yagofacts;");
		resultSet = preparedStatement.executeQuery();

		while(resultSet.next())
			yagoEntities.add(resultSet.getString("obj"));
	}

	public void getAllRelationsFromScoredFacts(Set<String> relations) throws SQLException {

		preparedStatement = connect.prepareStatement("select distinct rel from lector.relPhraseScore;");
		resultSet = preparedStatement.executeQuery();

		while(resultSet.next())
			relations.add(resultSet.getString("rel"));
	}

	public String extractID(String string) {
		return string.substring(2,string.indexOf("|"));
	}

	public void getAllFactWithScoreAndProb(List<String> yagoFactsBetweenEntities,String subj,String obj) throws SQLException {

		preparedStatement = connect.prepareStatement("select * from lector.yagofacts where subj='"+subj+"' and obj='"+obj+"';");
		resultSet = preparedStatement.executeQuery();

		while(resultSet.next())
			yagoFactsBetweenEntities.add(resultSet.getString("rel"));
	}

	public void getMostRelevantPhrasesForRelation(String relation,Map<String,Set<String>> phraseToRelation) throws SQLException, InvalidDataException {
		double threashold=0.5;

		preparedStatement = connect.prepareStatement("select rel, phrase, score from relPhraseScore where rel='"+relation+"' and probability>='"+threashold+"'order by score desc limit 20;");

		resultSet = preparedStatement.executeQuery();

		String phrase;

		while(resultSet.next()){
			phrase=resultSet.getString("phrase");

			if(phraseToRelation.containsKey(phrase))
				phraseToRelation.get(phrase).add(relation);
			else{

				Set<String>relationsList=new HashSet<String>();
				relationsList.add(relation);
				phraseToRelation.put(phrase, relationsList);
			}
		}
	}

	public Map<String,Set<String>> populatePhraseRelationMapByProbability() throws SQLException, InvalidDataException{
		Set<String> relations= new HashSet<String>();
		this.getAllRelationsFromScoredFacts(relations);

		Map<String,Set<String>> phraseToRelation=new  HashMap<String,Set<String>>();

		for(String relation:relations){

			if(this.useOnlyGeneralized)
				this.getMostRelevantPhrasesForRelationGeneralizedOrStopped(relation, phraseToRelation);	

			if(this.useStoppedAndGeneralized)
				this.getMostRelevantPhrasesForRelationGeneralizedOrStopped(relation, phraseToRelation);

			if(this.useNotModifiedWords)
				this.getMostRelevantPhrasesForRelation(relation, phraseToRelation);
		}
		return phraseToRelation;
	}

	private void getMostRelevantPhrasesForRelationGeneralizedOrStopped(String relation,Map<String, Set<String>> phraseToRelation) throws SQLException, InvalidDataException {
		int phraseAdded=0;
		double threashold=0.5;

		preparedStatement = connect.prepareStatement("select rel, phrase, score from relPhraseScore where rel='"+relation+"' and probability>='"+threashold+"'order by score desc limit 40;");

		resultSet = preparedStatement.executeQuery();

		String phrase;

		while(resultSet.next()&&(phraseAdded<20)){
			phraseAdded++;
			
			if(this.useStoppedAndGeneralized)
				phrase=this.generalizer.generalizePhraseWithGeneralizationAndStopping(resultSet.getString("phrase"));
			
			else
				phrase=this.generalizer.generalizePhraseWithOnlyGeneralization(resultSet.getString("phrase"));

			if(phraseToRelation.containsKey(phrase))
				phraseToRelation.get(phrase).add(relation);
			else{

				Set<String>relationsList=new HashSet<String>();
				relationsList.add(relation);
				phraseToRelation.put(phrase, relationsList);
			}
		}
	}

	public void setUseStoppedAndGeneralized(boolean useStoppedAndGeneralized) {
		this.useStoppedAndGeneralized = useStoppedAndGeneralized;
	}

	public void setUseOnlyGeneralized(boolean useOnlyGeneralized) {
		this.useOnlyGeneralized = useOnlyGeneralized;
	}
	

	public void setUseNotModifiedWords(boolean useNotModifiedWords) {
		this.useNotModifiedWords = useNotModifiedWords;
	}

	public void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {}
	}
}
