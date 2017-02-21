package dbInteractor;

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
	
	public void insertRelationPhraseScore(String relation,String phraseSentence, double score, double probability) throws SQLException {
		
		preparedStatement = connect.prepareStatement("insert into lector.relPhraseScore values('"+relation+"','"+phraseSentence+"','"+score+"','"+probability+"');");
		preparedStatement.execute();
		
	}
	
	public void getRelationsPhraseCorrespondence(List<String> relations,String phrase) throws SQLException {

		preparedStatement = connect.prepareStatement("select * from lector.relPhraseCount where phrase='"+phrase+"';");
		resultSet = preparedStatement.executeQuery();

		while(resultSet.next())
			relations.add(resultSet.getString("rel"));
	}
	
	public void getAllDistinctPhrasesThatHaveWitness(Set<String> phrasesWithWitness) throws SQLException {

		preparedStatement = connect.prepareStatement("select distinct phrase from lector.relPhraseCount;");
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

	public double getWitnessCountSum(String phrase) throws SQLException {
		preparedStatement = connect.prepareStatement("select sum(sum) as s from lector.relPhraseCount where phrase='"+phrase+"';");
		resultSet = preparedStatement.executeQuery();

		resultSet.next();
		String sum=resultSet.getString("s");

		Integer f = new Integer(sum);
		return f;
	}
	
	public void getRelationsPhraseCountCorrespondence(List<RelPhraseCountEntry> entries,String phrase) throws SQLException {

		preparedStatement = connect.prepareStatement("select * from lector.relPhraseCount where phrase='"+phrase+"';");
		resultSet = preparedStatement.executeQuery();

		while(resultSet.next())
			entries.add(new RelPhraseCountEntry(resultSet.getString("phrase"), resultSet.getString("rel"), resultSet.getString("sum")));
	}
	

}
