package executor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Date;

import dbInteractor.ProbabilityBuilder;
import dbInteractor.ProbabilityBuilder1;

public class BuildProbabilityDB {
	public static void main(String []args){
		
			Date a = new Date();
			System.out.println(a);
			try {
				ProbabilityBuilder1 mstest= new ProbabilityBuilder1();
				mstest.getProbabilityScore();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			a = new Date();
			System.out.println(a);
		}
		
		
		
	
}
