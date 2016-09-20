import java.util.Optional;

/**
 * Created by Rob on 9/19/2016.
 */
public class DeathLocation extends BasicResponder {

    public DeathLocation(String pathToKeywords, String pathToDefault){
        super(pathToKeywords, pathToDefault);
    }

    @Override
    public String respondTo(String sentence) {
        String searchName = DeterminePerson.getPerson(sentence);
        Optional<String> searchResult = WikiKnowledge.getPersonDeathPlace(searchName, KnowledgeChance.DEFAULT_CHANCE_OF_FAILURE);

        if(searchResult.isPresent()){
            return searchName + " died in " + searchResult.get() +".";
        }else{
            return super.respondTo(sentence);
        }
    }
}
