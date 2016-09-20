/**
 * Created by Rob on 9/19/2016.
 */
public class DetermineBattle {

    public static String getBattleName(String sentence) {
        String battleName = "";
        String[] decompSent = sentence.split(" ");

        for (int i = 0; i < decompSent.length; i++) {
            if (decompSent[i].equalsIgnoreCase("battle")) {
                battleName += decompSent[i] + " " + decompSent[i + 1] + " " + decompSent[i + 2];
                if ((i + 3) < decompSent.length) {
                    if (decompSent[i + 3].equalsIgnoreCase("take") || decompSent[i + 3].equalsIgnoreCase("happen")) {
                        break;
                    } else {
                        battleName += " " + decompSent[i + 3];
                        break;
                    }
                }
            }
        }
        return battleName;
    }
}
