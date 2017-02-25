package executor;

import java.io.IOException;
import java.util.Date;

import com.uttesh.exude.exception.InvalidDataException;

import generalizer.TriplesGeneralizer;

public class GeneralizeTriples {

	public static void main(String [] args) throws InvalidDataException{
		
		Date a ;

		for(int i=1;i<=57;i++){
			System.out.println(i);
			a =new Date();
			System.out.println(a);
			try {
				TriplesGeneralizer fh= new TriplesGeneralizer("splitted/splitted"+i+".txt");
				fh.generalizeTriples(i);
			} catch ( IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
		a =new Date();
		System.out.println(a);


	}}
