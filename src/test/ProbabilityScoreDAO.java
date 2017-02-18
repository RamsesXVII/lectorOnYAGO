package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Set;

public class ProbabilityScoreDAO {
	
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	public ProbabilityScoreDAO() throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager.getConnection("jdbc:mysql://localhost/lector?user=root&password=root");
	}
	
	public int getWitnessCount(String rel,String phrase) throws Exception {
		preparedStatement = connect.prepareStatement("select sum as s from lector.relPhraseCount where rel='"+rel+"' and phrase='"+phrase+"';");
		resultSet = preparedStatement.executeQuery();

		resultSet.next();
		String sum=resultSet.getString("s");

		Integer f = new Integer(sum);
		return f;
	}
	
	public void insertRelationPhraseScore(String relation,String phraseSentence, double score) throws SQLException {
		
		preparedStatement = connect.prepareStatement("insert into lector.relPhraseScore values('"+relation+"','"+phraseSentence+"','"+score+"');");
		preparedStatement.execute();
		
	}
	
	public void getAllFactsWithWitnessByPhrase(List<String> factWithWitness,String phraseSentence) throws SQLException {

		preparedStatement = connect.prepareStatement("select * from lector.relPhraseCount where phrase='"+phraseSentence+"';");
		resultSet = preparedStatement.executeQuery();

		while(resultSet.next())
			factWithWitness.add(resultSet.getString("rel"));
	}
	
	public void getAllPhrasesWithWitness(Set<String> phrasesWithWitness) throws SQLException {

		preparedStatement = connect.prepareStatement("select * from lector.relPhraseCount;");
		resultSet = preparedStatement.executeQuery();

		while(resultSet.next())
			phrasesWithWitness.add(resultSet.getString("phrase"));
	}
	
	// You need to close the resultSet
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
		} catch (Exception e) {

		}
	}

}
