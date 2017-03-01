package prefilterSemplified;

import java.sql.SQLException;

public class mainTypeFIlter {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		String phrase="The venue also hosted the [[2010_Lamar_Hunt_U.S._Open_Cup_Final|m.0ch38wd]] and [[2011_Lamar_Hunt_U.S._Open_Cup_Final|m.0gy1xj6]] tournament finals for the [[Lamar_Hunt_U.S._Open_Cup|m.01lkg2]] .";
		TypeChecker tc= new TypeChecker();
		System.out.println(tc.existTwoEntitiesSameType(phrase));
					
			
		
	}

}
