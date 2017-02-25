package dbInteractor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class TypesDAO {
	
	private Connection connect = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	public TypesDAO() throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager.getConnection("jdbc:mysql://localhost/lector?user=root&password=root");
	}

	public void getTypesFromEntity(String entity, HashSet<String> types) throws SQLException {
		preparedStatement = connect.prepareStatement("select type from lector.yagoTypesBig where entity='"+entity+"';");
		resultSet = preparedStatement.executeQuery();

		while(resultSet.next())
			types.add(resultSet.getString("type"));

	}

}
