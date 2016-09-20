import java.util.Optional;

/**
 * Created by Rob on 9/19/2016.
 */
public class WhenBattle extends BasicResponder {

    public WhenBattle(String pathToKeywords, String pathToDefault){
        super(pathToKeywords, pathToDefault);
    }

    @Override
    public String respondTo(String sentence) {

        String searchStr = DetermineBattle.getBattleName(sentence);
        Optional<String> returnString = WikiKnowledge.getBattleDate(searchStr, KnowledgeChance.DEFAULT_CHANCE_OF_FAILURE);

        if(returnString.isPresent()){
            return "The " + searchStr + " took place in " + returnString.get() + ".";
        }else{
            return super.respondTo(sentence);
        }
    }
}
