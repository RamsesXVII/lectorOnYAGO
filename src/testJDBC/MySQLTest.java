package testJDBC;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import IOUtility.FileInteractor;
import IOUtility.TSVSentencesUtility;

public class MySQLTest {

	private TSVSentencesUtility tSVSentencesUtility;
	private List<String[]> allRows;
	private FactsRelationsDAO dao;
	private FileInteractor fileInteractor;

	public MySQLTest(String pathToFile) throws ClassNotFoundException, SQLException, UnsupportedEncodingException, FileNotFoundException{

		this.tSVSentencesUtility=new TSVSentencesUtility();
		this.allRows= tSVSentencesUtility.getAllSentencesFromTSV(pathToFile);
		this.dao= new FactsRelationsDAO();
		this.fileInteractor=new FileInteractor();

	}
	public void getYagoRelations(int i) throws UnsupportedEncodingException, FileNotFoundException, IOException {

		int linec=0;

		for(String[] phrase : allRows)
		{
			List<String> yagoFactsBetweenEntities=new LinkedList<>();

			linec++;
			if(linec%1000==0)
				System.out.println(linec);

			String subj=dao.extractID(phrase[0]);
			String obj=dao.extractID(phrase[2]);
			String phraseSentence=phrase[1];
			
			if (subj.contains("'"))
				subj = subj.replaceAll("'","''");
			
			if (obj.contains("'"))
				obj = obj.replaceAll("'","''");

			try{

				dao.getAllFactsBeweenEntitiesDAO(yagoFactsBetweenEntities,subj,obj);

				for(String relation:yagoFactsBetweenEntities){

					boolean phraseSentenceRelationIsAlreadyPresent=dao.checkPhraseRelationCorrispondence(relation,phraseSentence);

					if(phraseSentenceRelationIsAlreadyPresent){
						Integer sum=dao.getCurrentTimesAssociation(relation,phraseSentence);

						sum++;

						dao.updateAssociationCount(sum.toString(),relation,phraseSentence);

					}

					else{
						dao.insertNewPhraseRelationCorrispondence(relation,phraseSentence);
					}
				}


			}catch(Exception e){
				this.fileInteractor.writeFile(phrase[0]+"\t"+phrase[1]+"\t"+phrase[2]+"\t", "error"+i+".txt");
				e.printStackTrace();
			}
		}
		dao.close();

	}


}