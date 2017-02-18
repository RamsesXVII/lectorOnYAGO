//package testJDBC;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.Date;
//import java.util.LinkedList;
//import java.util.List;
//
//public class MySQLAccess {
//        private Connection connect = null;
//        private Statement statement = null;
//        private PreparedStatement preparedStatement = null;
//        private ResultSet resultSet = null;
//        
//        public MySQLAccess() throws ClassNotFoundException, SQLException{
//        	Class.forName("com.mysql.jdbc.Driver");
//            // Setup the connection with the DB
//            connect = DriverManager.getConnection("jdbc:mysql://localhost/lector?user=root&password=root");
//        	
//        }
//        public List<String> getYagoRelations(String subj,String obj) throws Exception {
//        	List<String>relations=new LinkedList<String>();
//        
//                 // This will load the MySQL driver, each DB has its own driver
//                 
//                 
//                 preparedStatement = connect.prepareStatement("select * from lector.yagofacts where subj='"+subj+"' and obj='"+obj+"';");
//                 resultSet = preparedStatement.executeQuery();
//                 while(resultSet.next())
//                	 relations.add(resultSet.getString("rel"));
//                	 
//
//                 return relations;
//         
//
// }
//        
//
//        public void persistRelPhraseCount(String rel,String phrase) throws Exception {
//           
//
//                        
//                        preparedStatement = connect.prepareStatement("select exists(select * from lector.relPhraseCount where rel='"+rel+"' and phrase='"+phrase+"') as isPresent;");
//                        resultSet = preparedStatement.executeQuery();
//                        
//                        resultSet.next();
//                        
//                        String isPresent = resultSet.getString("isPresent");
//                        
//                        if(isPresent.equals("1")){
//                        	
//                            preparedStatement = connect.prepareStatement("select sum as s from lector.relPhraseCount where rel='"+rel+"' and phrase='"+phrase+"';");
//                            resultSet = preparedStatement.executeQuery();
//                            
//                            resultSet.next();
//                            
//                            String sum=resultSet.getString("s");
//                           
//                            Integer f=new Integer(sum);
//                            f++;
//                            
//                            preparedStatement = connect.prepareStatement("update lector.relPhraseCount set sum='"+f.toString()+"' where rel='"+rel+"' and phrase='"+phrase+"';");
//                            preparedStatement.executeUpdate();
//                            
//                        }
//                        
//                        else{
//                        	 preparedStatement = connect.prepareStatement("insert into lector.relPhraseCount values('"+rel+"','"+phrase+"',1);");
//                             preparedStatement.execute();
//                        }
//
//  
//
//        }
//
//
//        // You need to close the resultSet
//        private void close() {
//                try {
//                        if (resultSet != null) {
//                                resultSet.close();
//                        }
//
//                        if (statement != null) {
//                                statement.close();
//                        }
//
//                        if (connect != null) {
//                                connect.close();
//                        }
//                } catch (Exception e) {
//
//                }
//        }
//
//}