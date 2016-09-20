import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Thread.sleep;

/**
 * Created by Rob on 9/17/2016.
 */
public class Output {

    protected static String[] response(String givenResponse)
    {
        String[] output = new String[2];
        String[] words = givenResponse.split(" ");

        Random rand = ThreadLocalRandom.current();
        double errorChance = 0.15;
        int index = -1;

        double mistake = rand.nextDouble();
        if(mistake < errorChance)
        {
            index = rand.nextInt(words.length);
            //System.out.println("Typo");
            //index = 8;
        }

        String typoWord = "";
        if(index > -1) typoWord = wordTypo(words[index]);

        String typoStr = "";
        for(int i = 0; i < words.length; i++)
        {
            if(i == index)
            {
                typoStr += " " + typoWord;

            }
            else
            {
                typoStr += " " + words[i];
            }
        }

        output[0] = typoStr;

        if(index > -1 && words[index].replaceAll("^ ", "").equals(typoWord.replaceAll("^ ", "")))
        {
            output[1] = null;
        }
        else if(index > -1 && !(words[index].replaceAll("^ ", "").equals(typoWord.replaceAll("^ ", ""))))
        {
            output[1] = words[index];
        }
        else
        {
            output[1] = null;
        }

        return output;
    }

    protected static String wordTypo(String word)
    {
        return word.replaceAll( "([\\w'!.\\-]+)([\\w'!.\\-])([\\w'!.\\-])", "$1$3$2");
    }

    public static void printResponse(String responseStr)
    {
        String[] finalResponse = response(responseStr);

        System.out.println(finalResponse[0].replaceAll("^ ",""));

        if(finalResponse[1] != null)
            try {
                sleep(2500);
                System.out.println(finalResponse[1] + "*");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
}
