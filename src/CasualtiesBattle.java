import java.util.Optional;

/**
 * Created by Rob on 9/19/2016.
 */
public class CasualtiesBattle extends BasicResponder {

    public CasualtiesBattle(String pathTokeywords, String pathToDefault) {
        super(pathTokeywords, pathToDefault);
    }

    @Override
    public String respondTo(String sentence) {
        String searchStr = DetermineBattle.getBattleName(sentence);
        Optional<String> resultStr = WikiKnowledge.getBattleCasualties(searchStr, KnowledgeChance.DEFAULT_CHANCE_OF_FAILURE);

        if (resultStr.isPresent()) {
            return "The " + searchStr + " had " + resultStr.get() + " casualties.";
        } else {
            return super.respondTo(sentence);
        }

    }
}
