package factsValidatorByType;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import IOUtility.FileInteractor;
import IOUtility.TSVSentencesUtility;
import dbInteractor.TypesDAO;

public class ExtractedFactsFilter {
	private TSVSentencesUtility tSVSentencesUtility;
	private List<String[]> allRows;
	private FileInteractor fileInteractor;
	private TypesDAO tdao;

	private HashMap<String, List<String>> relationToDomainAndRange;


	public ExtractedFactsFilter(String pathToFile,HashMap<String, List<String>> relationToDomainAndRange) throws UnsupportedEncodingException, FileNotFoundException, ClassNotFoundException, SQLException {
		this.tSVSentencesUtility = new TSVSentencesUtility();
		this.allRows = tSVSentencesUtility.getAllSentencesFromTSV(pathToFile);
		this.fileInteractor = new FileInteractor();

		this.tdao= new TypesDAO();
		this.relationToDomainAndRange=relationToDomainAndRange;

	}

	public void filterFacts() throws UnsupportedEncodingException, FileNotFoundException, IOException, SQLException {
		String relation=null;
		String domainRequired=null;
		String rangeRequired=null;
		String subj=null;
		String obj=null;
		HashSet<String>typesOfSubj=null;
		HashSet<String>typesOfObj=null;

		boolean domainIsSame;
		boolean rangeIsSame;

		int contaLinee=0;

		for(String[] phrase : allRows)
		{
			contaLinee++;
			if(contaLinee%1000==0)
				System.out.println(contaLinee);
			
			domainIsSame=false;
			rangeIsSame=false;

			relation=phrase[3];
			domainRequired=relationToDomainAndRange.get(relation).get(0);
			rangeRequired=relationToDomainAndRange.get(relation).get(1);

			subj=this.extractID(phrase[0]);
			obj=this.extractID(phrase[2]);
			
			
			if (subj.contains("'"))
				subj = subj.replaceAll("'","''");

			if (obj.contains("'"))
				obj = obj.replaceAll("'","''");

			typesOfSubj=new HashSet<String>();
			tdao.getTypesFromEntity(subj, typesOfSubj);
			

			typesOfObj=new HashSet<String>();
			tdao.getTypesFromEntity(obj, typesOfObj);

		
			domainIsSame=this.checkTypesCorrespondence(typesOfSubj,domainRequired);
			rangeIsSame=this.checkTypesCorrespondence(typesOfObj,rangeRequired);



			if(domainIsSame&&rangeIsSame)
				this.fileInteractor.writeFile(phrase[0]+"\t"+phrase[1]+"\t"+phrase[2]+"\t"+phrase[3], "validatedFacts.tsv");
			else{
				this.fileInteractor.writeFile(phrase[0]+"\t"+phrase[1]+"\t"+phrase[2]+"\t"+phrase[3], "refusedFacts.tsv");
				this.fileInteractor.writeFile(domainRequired, "refusedFacts.tsv");
				this.fileInteractor.writeFile(rangeRequired, "refusedFacts.tsv");
			}
		}
	}

	private boolean checkTypesCorrespondence(HashSet<String> entityTypes, String typeRequired) {
		for(String s:entityTypes)
			if(s.equals(typeRequired))
				return true;
		return false;
	}


	private String extractID(String string) {
		return string.substring(2,string.indexOf("|"));
	}

}


