package extractorStanford;


import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import IOUtility.XMLParser;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;
import IOUtility.FileInteractor;


public class Extractor {
	private StanfordCoreNLP pipeline;

	public Extractor() {
		this.pipeline = new StanfordCoreNLP(PropertiesUtils.asProperties("annotators", "tokenize, ssplit, pos, lemma, ner, parse"));

	}

	public List<String> extractFacts(List<String> cleanPhraseAndEntitesList) {

		Map<String,List<List<String>>> relationsMap=new HashMap<>();
		List<String> relations=new ArrayList<>();

		List<String> phraseEntitiesList = new ArrayList<>();
		for (int i = 1; i<cleanPhraseAndEntitesList.size() ;i++) 
			phraseEntitiesList.add(cleanPhraseAndEntitesList.get(i));

		String cleanPhrase = cleanPhraseAndEntitesList.get(0);
		DependenciesExpert dependenciesExpert=new DependenciesExpert(cleanPhrase,pipeline);


		String phraseEnhancedDependenciesXML=null;
		Annotation document =new Annotation(cleanPhrase);
		pipeline.annotate(document);

		List<CoreMap> singlePhraseCoreMap =document.get(SentencesAnnotation.class);

		for(CoreMap sentence: singlePhraseCoreMap) {
			phraseEnhancedDependenciesXML= sentence.get(SemanticGraphCoreAnnotations.EnhancedPlusPlusDependenciesAnnotation.class).toString(SemanticGraph.OutputFormat.XML);
		}

		try {

			XMLParser x=new XMLParser(phraseEnhancedDependenciesXML);
			NodeList nodes = x.getNodesByTag("dep");

			DependenciesNavigator dependenciesNavigator = new DependenciesNavigator(nodes);

			int finalPosition = 0;
			Element finalElement = null;
			Set<String> typeNmod = dependenciesExpert.getTypesOfNmod(nodes);
			int k = 0;
			if(!typeNmod.isEmpty()) {
				for (String type : typeNmod) {

					Map<Element, List<Element>> governor2dependentsByNmod = dependenciesNavigator.governor2dependents(type);
					
					// ogni governor di un nmod va analizzato come frase
					for (Element governor : governor2dependentsByNmod.keySet()) {
						List<String> dependentsNmodName = new ArrayList<>();
						// per ora aggiunge solo un soggetto
						List<String> dependentsNsubjName = new ArrayList<>();
						List<Element> dependents = governor2dependentsByNmod.get(governor);
						if(dependents.size()>1){
							for(Element e : dependents){
								dependentsNmodName.add(e.getTextContent());
							}
							
	
							finalPosition = dependenciesNavigator.lowerPosition(dependents);
	//						System.out.println(finalPosition);
							finalElement = dependenciesNavigator.getGovernorByPosition(finalPosition);
	//						System.out.println(governor.getTextContent());
	//						System.out.println(k);
							k = k+1;
	//						if (type.equals("nmod:such_as")) {
	//							System.out.println("prova");
	//							finalPosition = dependenciesNavigator.lowerPositionDependent(finalElement, "case");		
	//						}
							if (finalElement!=null)
								finalPosition = dependenciesNavigator.lowerPositionDependent(finalElement, "compound");
							
							int startPosition = dependenciesNavigator.startPositionNsubj(governor);
							if(startPosition!=0)
								dependentsNsubjName.add(dependenciesNavigator.getElementByPosition(startPosition).getTextContent());
							
	//						VBN (was)born, (was) married
	//						VBD was, married
	//						VBZ is
	//						VBG including
							//inizia dalla parola dopo start position
//							boolean verbFound = false;
//							List<String> phraseNew = new ArrayList<>();
//							int positionVerb = 0;
//							for (int i = startPosition+1; i < finalPosition; i++) {
//								CoreLabel token = singlePhraseCoreMap.get(0).get(TokensAnnotation.class).get(i-1);
//								String word = token.get(PartOfSpeechAnnotation.class);
//								if (word.equals("VBG")||word.equals("VBN")||word.equals("VBD")||word.equals("VBZ")) {
//									Element finalVerb = dependenciesNavigator.getElementByPosition(i);
//									positionVerb = i;
//									System.out.println(positionVerb);
//								}
//							}
////								if (word.equals("VBG")) {
////							int positionVerb = i;
//							if (positionVerb!=0){
//
//								int positionAux = 0;
//								positionAux = dependenciesNavigator.lowerPositionDependent(dependenciesNavigator.getElementByPosition(positionVerb), "aux");
//								if(positionAux!=0 && positionAux<positionVerb) {
//									for (int j = positionAux; j < positionVerb; j++) {
//										String nameAux = dependenciesNavigator.getElementByPosition(j).getTextContent();
//										phraseNew.add(nameAux);
//									}
//									phraseNew.add(dependenciesNavigator.getElementByPosition(positionVerb).getTextContent());
//								}
//								int positionDobj = 0;
//								positionDobj = dependenciesNavigator.lowerPositionDependentNoGovernor(dependenciesNavigator.getElementByPosition(positionVerb), "dobj");
//								if (positionDobj!=0 && positionDobj<finalPosition) {
//									phraseNew.add(dependenciesNavigator.getElementByPosition(positionDobj).getTextContent());
//								}
//								int positionXcomp = 0;
//								positionXcomp = dependenciesNavigator.lowerPositionDependentNoGovernor(dependenciesNavigator.getElementByPosition(positionVerb), "xcomp");
//								if (positionXcomp!=0 && positionXcomp<finalPosition) {
//									phraseNew.add(dependenciesNavigator.getElementByPosition(positionXcomp).getTextContent());
//								}
//								int positionCop = 0;
//								positionCop = dependenciesNavigator.positionGovernor(dependenciesNavigator.getElementByPosition(positionVerb), "cop");
//								if (positionCop!=0) {
//									positionAux = 0;
//									positionAux = dependenciesNavigator.lowerPositionDependent(dependenciesNavigator.getElementByPosition(positionCop), "aux");
//									if(positionAux!=0 && positionAux<=positionVerb) {
//										for (int j = positionAux; j <= positionCop; j++) {
//											String nameAux = dependenciesNavigator.getElementByPosition(j).getTextContent();
//											phraseNew.add(nameAux);
//										}
////										phraseNew.add(dependenciesNavigator.getElementByPosition(positionVerb).getTextContent());
//									}
//								}
////								verbFound = true;
//							}
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
							
//								}
//								if (word.equals("VBN")) {
//									int positionVerb = i;
//									int positionAux = 0;
//									System.out.println("VERB "+positionVerb);
//									positionAux = dependenciesNavigator.lowerPositionDependent(dependenciesNavigator.getElementByPosition(positionVerb), "aux");
//									System.out.println("AUX  "+positionAux);
//									if(positionAux!=0 && positionAux<positionVerb){
//										String nameAux= dependenciesNavigator.getElementByPosition(positionAux).getTextContent();
//										phraseNew.add(nameAux);
//										phraseNew.add(dependenciesNavigator.getElementByPosition(positionVerb).getTextContent());
//									}
//									else
//										phraseNew.add(dependenciesNavigator.getElementByPosition(positionVerb).getTextContent());
//									verbFound = true;
//								}
//							}
//							if (verbFound==false) {
//								for (int i = startPosition+1; i < finalPosition; i++) {
//									CoreLabel token = singlePhraseCoreMap.get(0).get(TokensAnnotation.class).get(i-1);
//									String word = token.get(PartOfSpeechAnnotation.class);
//	
//									if(word.equals("VBD")||word.equals("VBZ")){
//										int positionVerb = i;
//										phraseNew.add(dependenciesNavigator.getElementByPosition(positionVerb).getTextContent());
//									}
//								}
//							}
	
	
							dependenciesExpert= new DependenciesExpert(dependentsNmodName,phraseEntitiesList,dependentsNsubjName,startPosition,finalPosition,nodes);
	
							Set<String> entityDependenciesNmod = dependenciesExpert.getEntityDependenciesList();
							List<String> entityNsubjList = dependenciesExpert.getEntityNsubjList();
							List<String> phraseFromStartToEndPosition = dependenciesExpert.getRelationEntities();
							List<String> relationalPhrase = createRelationalPhrase(startPosition,finalPosition,singlePhraseCoreMap,dependenciesNavigator);
							
							FileInteractor fileInteractor= new FileInteractor();
							
							String relation=fileInteractor.writeFactsExtractedOnFile2(cleanPhrase,phraseEntitiesList,relationalPhrase,entityDependenciesNmod,entityNsubjList);
	
	//						FileInteractor fileInteractor= new FileInteractor();
	//														/						frase pulita [	entitnellafrase		
	//						String relation2=fileInteractor.writeFactsExtractedOnFile(cleanPhrase,phraseEntitiesList,phraseFromStartToEndPosition,entityDependenciesList,entityNsubjList);
	//
	//						List<List<String>> subjDep = new ArrayList<>();
	//						relationsMap.put(relation, subjDep);
	//						relations.add(relation);
	
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return relations;
	}
	private List<String> createRelationalPhrase(int startPosition, int finalPosition, List<CoreMap> singlePhraseCoreMap, DependenciesNavigator dependenciesNavigator) {
//		boolean verbFound = false;
		List<String> phraseNew = new ArrayList<>();
		int positionVerb = 0;
		for (int i = startPosition+1; i < finalPosition; i++) {
			CoreLabel token = singlePhraseCoreMap.get(0).get(TokensAnnotation.class).get(i-1);
			String word = token.get(PartOfSpeechAnnotation.class);
			if (word.equals("VBG")||word.equals("VBN")||word.equals("VBD")||word.equals("VBZ")) {
				Element finalVerb = dependenciesNavigator.getElementByPosition(i);
				if(!finalVerb.getTextContent().equals("including"))
					positionVerb = i;
//				System.out.println(positionVerb);
			}
		}

		int positionAux = 0;
		if(positionVerb>0) {
//			System.out.println("VERB "+positionVerb);
//			Element e = dependenciesNavigator.getElementByPosition(positionVerb);
			positionAux = dependenciesNavigator.lowerPositionDependent(dependenciesNavigator.getElementByPosition(positionVerb), "aux");
//			System.out.println("AUX  "+positionAux);
			if(positionAux!=0 && positionAux<positionVerb){
				String nameAux= dependenciesNavigator.getElementByPosition(positionAux).getTextContent();
				phraseNew.add(nameAux);
				phraseNew.add(dependenciesNavigator.getElementByPosition(positionVerb).getTextContent());
			}
			else
				phraseNew.add(dependenciesNavigator.getElementByPosition(positionVerb).getTextContent());
	//		verbFound = true;
			int positionPrep = 0;
			boolean find = false;
			for (int i = positionVerb+1; i < finalPosition && find==false; i++) {
				CoreLabel token = singlePhraseCoreMap.get(0).get(TokensAnnotation.class).get(i-1);
				String word = token.get(PartOfSpeechAnnotation.class);
				if (word.equals("IN")) {
					Element prep = dependenciesNavigator.getElementByPosition(i);
					positionPrep = i;
					for (int j = positionVerb+1; j <= positionPrep; j++) {
						phraseNew.add(dependenciesNavigator.getElementByPosition(j).getTextContent());
					}
					find = true;
	//				System.out.println(positionVerb);
				}
			}
		}
		return phraseNew;
	}
}