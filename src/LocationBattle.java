import java.util.Optional;

/**
 * Created by Rob on 9/19/2016.
 */
public class LocationBattle extends BasicResponder {

    /**
     * Creates a special instance of BasicResponder that is used for determining
     * a battle's location.
     *
     * @param pathToKeywords  The path to the list of words that are used to
     *                        identify that the type of question being asked
     *                        fits for this class.
     * @param pathToDefault   The path to the list of default responses that
     *                        will be used if the answer cannot be found or
     *                        if we choose not to answer the question.
     */
    public LocationBattle(String pathToKeywords, String pathToDefault) {
        super(pathToKeywords, pathToDefault);
    }

    /**
     * Function for returning the result of the question asked. Will return
     * a default response if the data cannot be found or it is decided to
     * not answer the question.
     *
     * @param sentence  The question being asked.
     * @return          The answer to the question.
     */
    @Override
    public String respondTo(String sentence) {
        String searchStr = DetermineBattle.getBattleName(sentence);
        Optional<String> resultStr = WikiKnowledge.getBattleLocation(searchStr, KnowledgeChance.DEFAULT_CHANCE_OF_FAILURE);

        if (resultStr.isPresent()) {
            return "The " + searchStr + " took place in " + resultStr.get() + ".";
        } else {
            return super.respondTo(sentence);
        }

    }
}
