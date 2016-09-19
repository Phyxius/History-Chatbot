/**
 * Created by Rob on 9/19/2016.
 */
public class CasualtiesBattle extends BasicResponder {

    public CasualtiesBattle(String pathTokeywords, String pathToDefault){
        super(pathTokeywords, pathToDefault);
    }

    @Override
    public String respondTo(String sentence) {
        String searchStr = DeterminBattle.getBattleName(sentence);
        String resultStr = WikiKnowledge.getBattleCasualties(searchStr, KnowledgeChance.DEFAULT_CHANCE_OF_FAILURE).orElse(super.respondTo(sentence));
        return resultStr;
    }
}
