package executor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Date;

import IOUtility.FileInteractor;
import dbInteractor.RelPhraseCountBuilder;

public class BuildRelPhraseCountDB {
	public static void main(String []args){

		for(int i=1;i<=57;i++){
			Date a = new Date();
			System.out.println(a);

			try {
				RelPhraseCountBuilder mstest= new RelPhraseCountBuilder("splitted"+i+".txt");
				mstest.getYagoRelations(i);
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

			Date b = new Date();
			System.out.println(b);

			FileInteractor fi= new FileInteractor();
			try {
				fi.writeFile(a+"\n"+b, "metadata"+i+".txt");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		
		
		
	}
}
