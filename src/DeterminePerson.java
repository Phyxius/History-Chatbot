import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rob on 9/19/2016.
 */
public class DeterminePerson {

    public static String getPerson(String sentence){
        String[] decompSent = sentence.split(" ");
        List<String> name = new ArrayList<>();
        boolean partOfName = false;

        for(int i = 0; i < decompSent.length; i++){
            if(decompSent[i].equalsIgnoreCase("was") || decompSent[i].equalsIgnoreCase("did")){
                partOfName = true;
                continue;
            }else if(decompSent[i].equalsIgnoreCase("born") || decompSent[i].equalsIgnoreCase("die")){
                break;
            }

            if(partOfName){
                name.add(decompSent[i]);
            }

        }


        String ret = "";
        for(String s: name){
            s = s.substring(0,1).toUpperCase() + s.substring(1);
            ret += " " + s;
        }
        return ret.replaceAll("^ ", "");

    }
}
