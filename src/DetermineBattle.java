/**
 * Created by Rob on 9/19/2016.
 */
public class DetermineBattle {

    /**
     * Function for extracting the name of  Battle from a sentence so it can be
     * used in the wiki knowledge class.
     *
     * @param sentence  The question being asked.
     * @return          The name of the battle in the question.
     */
    public static String getBattleName(String sentence) {
        String battleName = "";
        String[] decompSent = sentence.split(" ");

        for (int i = 0; i < decompSent.length; i++) {
            if (decompSent[i].equalsIgnoreCase("battle")) {
                battleName += decompSent[i] + " " + decompSent[i + 1] + " " + decompSent[i + 2].replaceAll("([.?!])", "");
                if ((i + 3) < decompSent.length) {
                    if (decompSent[i + 3].replaceAll("([.?!])", "").equalsIgnoreCase("take") || decompSent[i + 3].replaceAll("([.?!])", "").equalsIgnoreCase("happen")) {
                        break;
                    } else {
                        battleName += " " + decompSent[i + 3].replaceAll("([.?!])", "");
                        break;
                    }
                }
            }
        }
        return battleName;
    }
}
