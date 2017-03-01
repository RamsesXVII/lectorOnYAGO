package prefilterSemplified;

import java.sql.SQLException;
import java.util.HashSet;

import dbInteractor.TypesDAO;
import phrasesFilter.SentencesSplitter;

public class TypeChecker {
	private SentencesSplitter sentenceUtility; //metodi di utility utili in questa fase
	private TypesDAO tdao;


	public TypeChecker() throws ClassNotFoundException, SQLException{
		this.sentenceUtility= new SentencesSplitter();
		this.tdao= new TypesDAO();

	}


	public boolean existTwoEntitiesSameType(String phrase) throws SQLException{

		HashSet<String>entitiesID=this.getAllEntitiesIdFromPhrase(phrase);
		HashSet<String>types=new HashSet<>(); 

		for(String entity:entitiesID ){
			if (entity.contains("'"))
				entity = entity.replaceAll("'","''");

			HashSet<String>typesOfEntity=new HashSet<String>();
			tdao.getTypesFromEntity(entity, typesOfEntity);

			for(String type:typesOfEntity)
				if(!types.add(type))
					if(this.typeIsNotRoot(type)){
						return true;}
		}
		return false;

	}


	private boolean typeIsNotRoot(String type) 
	{

		if(type.equals("wordnet_whole_100003553"))
			return false;
		if(type.equals("owl:Thing"))
			return false;
		if(type.equals("wordnet_causal_agent_100007347"))
			return false;
		if(type.equals("wordnet_object_100002684"))
			return false;
		if(type.equals("wordnet_organism_100004475"))
			return false;
		if(type.equals("yagoGeoEntity"))
			return false;
		if(type.equals("yagoLegalActor"))
			return false;
		if(type.equals("yagoLegalActorGeo"))
			return false;
		if(type.equals("wordnet_abstraction_100002137"))
			return false;
		if(type.equals("yagoPermanentlyLocatedEntity"))
			return false;
		if(type.equals("yagoGeoEntity"))
			return false;
		if(type.equals("wordnet_physical_entity_100001930"))
			return false;
		if(type.equals("wordnet_abstraction_100002137"))
			return false;
		if(type.equals("wordnet_artifact_100021939"))
			return false;
		if(type.equals("wordnet_organization_108008335"))
			return false;
		if(type.equals("wordnet_communicator_109610660"))
			return false;
		if(type.equals("wordnet_living_thing_100004258"))
			return false;
		if(type.equals("wordnet_person_100007846"))
			return false;


		
		
		

		return true;

	}


	public HashSet<String>getAllEntitiesIdFromPhrase(String phrase){
		int numberOfEntities=this.sentenceUtility.getNumberOfEntities(phrase);
		HashSet<String>allPhraseEntities=new HashSet<>();

		for(int i=1;i<=numberOfEntities;i++){
			int prev=this.sentenceUtility.getNthOccurenceOfCharacter(phrase, "[[", i);
			int next=this.sentenceUtility.getNthOccurenceOfCharacter(phrase, "]]", i);

			String id=phrase.substring(prev+2,next);
			allPhraseEntities.add(id.substring(0,id.indexOf("|")));

		}
		return allPhraseEntities;

	}

}

