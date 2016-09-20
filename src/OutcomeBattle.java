import java.util.Optional;

/**
 * Created by Rob on 9/19/2016.
 */
public class OutcomeBattle extends BasicResponder {

    /**
     * Creates a special instance of BasicResponder that is used for determining
     * a battle's outcome.
     *
     * @param pathToKeywords  The path to the list of words that are used to
     *                        identify that the type of question being asked
     *                        fits for this class.
     * @param pathToDefault   The path to the list of default responses that
     *                        will be used if the answer cannot be found or
     *                        if we choose not to answer the question.
     */
    public OutcomeBattle(String pathToKeywords, String pathToDefault) {
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
        Optional<String> resultStr = WikiKnowledge.getBattleResult(searchStr, KnowledgeChance.DEFAULT_CHANCE_OF_FAILURE);

        if (resultStr.isPresent()) {
            String aOrAn = "a";
            char firstLetter = resultStr.get().toLowerCase().charAt(0);
            if (firstLetter == 'a' || firstLetter == 'e' || firstLetter == 'i' || firstLetter == 'o' || firstLetter == 'u')
                aOrAn = "an";
            return "The outcome of the " + searchStr + "was " + aOrAn + " " + resultStr.get() + ".";
        } else {
            return super.respondTo(sentence);
        }
    }
}
