import java.util.Optional;

/**
 * Created by Rob on 9/19/2016.
 */
public class OutcomeBattle extends BasicResponder {

    public OutcomeBattle(String pathToKeywords, String pathToDefault){
        super(pathToKeywords, pathToDefault);
    }

    @Override
    public String respondTo(String sentence) {
        String searchStr = DetermineBattle.getBattleName(sentence);
        Optional<String> resultStr = WikiKnowledge.getBattleResult(searchStr, KnowledgeChance.DEFAULT_CHANCE_OF_FAILURE);

        if(resultStr.isPresent()) {
            String aOrAn = "a";
            char firstLetter = resultStr.get().toLowerCase().charAt(0);
            if (firstLetter == 'a' || firstLetter == 'e' || firstLetter == 'i' || firstLetter == 'o' || firstLetter == 'u') aOrAn = "an";
            return "The outcome of the " + searchStr + "was " + aOrAn + " " + resultStr + ".";
        }else{
            return super.respondTo(sentence);
        }
    }
}
