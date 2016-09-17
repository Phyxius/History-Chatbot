import org.wikipedia.Wiki;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Shea on 2016-09-17.
 */
public class WikiKnowledge {
    private static final Wiki wiki = new Wiki("en.wikipedia.org");
    private static final String INFO_BOX_FLAG_TEXT = "{{Infobox military conflict";
    private static final Pattern killedPattern = Pattern.compile("((killed|dead): (?<count1>[0-9,]+))|((?<count2>[0-9,]+) (\\w+)?(dead|killed))");
    private static final String[] killedPatternCountGroups = {"count1", "count2"};

    public static Optional<String> getBattleCasualties(String battleName, int chanceOfNotKnowing)
    {
        if (chanceOfNotKnowing > 0 && KnowledgeChance.doesKnowAbout(battleName, chanceOfNotKnowing))
            return Optional.empty();
        Optional<String> infoBoxText = extractBattleInfoBox(battleName);
        if (!infoBoxText.isPresent()) return Optional.empty();

        final Matcher matcher = killedPattern.matcher(infoBoxText.get());
        int totalKilled = 0;
        while (matcher.find())
        {
            for(String s : killedPatternCountGroups)
            {
                String group = matcher.group(s);
                if (group != null && group.length() > 0)
                    totalKilled += Integer.parseInt(group.replace(",",""));
            }
        }
        return Optional.of(roundNumberString("" + totalKilled));
    }

    /**
     * Attempts to extract the Battle infobox from the wiki page with the given name
     * @param battleName the name of the battle to try to extract
     * @return Optional.empty() if unsuccessful, otherwise an Optional containing the contents of the infobox
     */
    private static Optional<String> extractBattleInfoBox(String battleName)
    {
        String pageText;
        try {
            String[][] results;
            results = wiki.search(battleName, Wiki.MAIN_NAMESPACE);
            if (results.length < 1) return Optional.empty();
            pageText = wiki.getPageText(results[0][0]);
        } catch (IOException e) {
            return Optional.empty();
        }
        if (!pageText.contains(INFO_BOX_FLAG_TEXT)) return Optional.empty();
        final int beginInfoBoxIndex = pageText.indexOf(INFO_BOX_FLAG_TEXT);
        int endInfoBoxIndex = -1;
        int braceDepth = 0;
        for(int i = beginInfoBoxIndex+1; i < pageText.length() - 2; i++)
        {
            final String curSubstring = pageText.substring(i, i + 2);
            if (curSubstring.equals("{{"))
            {
                braceDepth++;
                //i++; //prevents double matching on "{{{"
            }
            else if (curSubstring.equals("}}"))
            {
                if (braceDepth == 0)
                {
                    endInfoBoxIndex = i;
                    break;
                }
                braceDepth--;
            }
        }
        if (endInfoBoxIndex < beginInfoBoxIndex) return Optional.empty();
        return Optional.of(pageText.substring(beginInfoBoxIndex, endInfoBoxIndex));
    }

    public static String roundNumberString(String numberString)
    {
        return numberString.substring(0, 1) + new String(new char[numberString.length() - 1]).replaceAll("\0", "0");
    }
}
