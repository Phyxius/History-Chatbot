/**
 * Created by Rob on 9/19/2016.
 */
public class DeterminePerson {

    public static String getPerson(String sentence){
        String[] decompSent = sentence.split(" ");
        String name = "";
        boolean partOfName = false;

        for(int i = 0; i < decompSent.length; i++){
            if(decompSent[i].equalsIgnoreCase("was") || decompSent[i].equalsIgnoreCase("did")){
                partOfName = true;
                continue;
            }else if(decompSent[i].equalsIgnoreCase("born") || decompSent[i].equalsIgnoreCase("die")){
                break;
            }

            if(partOfName){
                name +=  " " + decompSent[i];
            }

        }
        return name.replaceAll("^ ", "");
    }
}
