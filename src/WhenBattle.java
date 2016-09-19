/**
 * Created by Rob on 9/19/2016.
 */
public class WhenBattle extends BasicResponder {

    public WhenBattle(String pathToKeywords, String pathToDefault){
        super(pathToKeywords, pathToDefault);
    }

    @Override
    public String respondTo(String sentence) {

        String searchStr = DeterminBattle.getBattleName(sentence);
        String returnString = WikiKnowledge.getBattleDate(searchStr, KnowledgeChance.DEFAULT_CHANCE_OF_FAILURE).orElse(super.respondTo(sentence));
        return returnString;
    }
}
