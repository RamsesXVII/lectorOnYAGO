package testJDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class FactsRelationsDAO {

	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	public FactsRelationsDAO() throws ClassNotFoundException, SQLException{
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

	public void getAllFactsBeweenEntitiesDAO(List<String> yagoFactsBetweenEntities,String subj,String obj) throws SQLException {

		preparedStatement = connect.prepareStatement("select * from lector.yagofacts where subj='"+subj+"' and obj='"+obj+"';");
		resultSet = preparedStatement.executeQuery();

		while(resultSet.next())
			yagoFactsBetweenEntities.add(resultSet.getString("rel"));
	}

	public String extractID(String string) {
		return string.substring(2,string.indexOf("|"));
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
