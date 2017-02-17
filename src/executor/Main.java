package executor;
//package de.vogella.mysql.first.test;
//
//import java.util.LinkedList;
//import java.util.List;
//
//import IOUtility.FileInteractor;
//import IOUtility.TSVSentencesUtility;
//import testJDBC.MySQLAccess;
//
//public class Main {
//	public static void main(String[] args) throws Exception {
//		MySQLAccess dao = new MySQLAccess();
//
//		TSVSentencesUtility t = new TSVSentencesUtility();
//		List<String[]> allRows = t.getAllSentencesFromTSV("splitted1.txt");
//
//		FileInteractor f = new FileInteractor();
//
//		int linec=0;
//		int errore=0;
//		
//		for(String[] phrase : allRows)
//		{
//			List<String> a=new LinkedList<>();
//			
//			linec++;
//			if(linec%1000==0)
//			System.out.println(linec);
//			
//			String subj=extractID(phrase[0]);
//			String obj=extractID(phrase[2]);
//			String phraseSentence=phrase[1];
//
//			try{
//				a=dao.getYagoRelations(subj,obj);
//				
//			}catch(Exception e){
//				System.out.println("errore");
//				errore++;
//			}
//			for(String s:a){
//				try{
//					dao.persistRelPhraseCount(s, phraseSentence);
//				}catch(Exception e){
//					System.out.println("errore");
//
//					errore++;
//				}
//			}
//		}
//
//	}
//
//	private static String extractID(String string) {
//		return string.substring(2,string.indexOf("|"));
//	}
//
//}