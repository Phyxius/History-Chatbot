import java.util.Optional;

/**
 * Created by Rob on 9/19/2016.
 */
public class BirthLocation extends BasicResponder {

    public BirthLocation(String pathToKeywords, String pathToDefault) {
        super(pathToKeywords, pathToDefault);
    }

    @Override
    public String respondTo(String sentence) {
        String searchName = DeterminePerson.getPerson(sentence);
        Optional<String> searchResult = WikiKnowledge.getPersonBirthPlace(searchName, KnowledgeChance.DEFAULT_CHANCE_OF_FAILURE);

        if (searchResult.isPresent()) {
            return searchName + " was born in " + searchResult.get() + ".";
        } else {
            return super.respondTo(sentence);
        }

    }
}
