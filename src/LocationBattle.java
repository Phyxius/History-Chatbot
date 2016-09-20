import java.util.Optional;

/**
 * Created by Rob on 9/19/2016.
 */
public class LocationBattle extends BasicResponder{

    public LocationBattle(String pathToKeywords, String pathToDefault){
        super(pathToKeywords, pathToDefault);
    }

    @Override
    public String respondTo(String sentence) {
        String searchStr = DetermineBattle.getBattleName(sentence);
        Optional<String> resultStr = WikiKnowledge.getBattleLocation(searchStr, KnowledgeChance.DEFAULT_CHANCE_OF_FAILURE);

        if(resultStr.isPresent()){
            return "The " +searchStr + " took place in " + resultStr.get() + ".";
        }else{
            return super.respondTo(sentence);
        }

    }
}
