package dbInteractor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import IOUtility.FileInteractor;
import IOUtility.TSVSentencesUtility;

public class RelPhraseCountBuilder {

	private TSVSentencesUtility tSVSentencesUtility;
	private List<String[]> allRows;
	private FactsRelationsDAO factRelDAO;
	private FileInteractor fileInteractor;

	public RelPhraseCountBuilder(String pathToFile) throws ClassNotFoundException, SQLException, UnsupportedEncodingException, FileNotFoundException{

		this.tSVSentencesUtility = new TSVSentencesUtility();
		this.allRows = tSVSentencesUtility.getAllSentencesFromTSV(pathToFile);
		this.factRelDAO = new FactsRelationsDAO();
		this.fileInteractor = new FileInteractor();

	}
	public void getYagoRelations(int i) throws UnsupportedEncodingException, FileNotFoundException, IOException {

		int linec=0;

		for(String[] phrase : allRows)
		{
			List<String> yagoFactsBetweenEntities=new LinkedList<>();

			linec++;
			if(linec%1000==0)
				System.out.println(linec);

			String subj=factRelDAO.extractID(phrase[0]);
			String obj=factRelDAO.extractID(phrase[2]);
			String phraseSentence=phrase[1];

			if (subj.contains("'"))
				subj = subj.replaceAll("'","''");

			if (obj.contains("'"))
				obj = obj.replaceAll("'","''");

			try{

				//se non ci sono fatti esistenti in YAGO tra le entità la lista non conterrà elementi
				factRelDAO.getAllRelationsBeweenEntitiesDAO(yagoFactsBetweenEntities,subj,obj);

				for(String relation:yagoFactsBetweenEntities){

					boolean phraseSentenceRelationIsAlreadyPresent=factRelDAO.checkPhraseRelationCorrispondence(relation,phraseSentence);

					if(phraseSentenceRelationIsAlreadyPresent){
						Integer sum=factRelDAO.getCurrentTimesAssociation(relation,phraseSentence);

						sum++;

						factRelDAO.updateAssociationCount(sum.toString(),relation,phraseSentence);

					}

					else{
						factRelDAO.insertNewPhraseRelationCorrispondence(relation,phraseSentence);
					}
				}


			}catch(Exception e){
				this.fileInteractor.writeFile(phrase[0]+"\t"+phrase[1]+"\t"+phrase[2]+"\t", "error"+i+".txt");
				e.printStackTrace();
			}
		}
		factRelDAO.close();

	}




}