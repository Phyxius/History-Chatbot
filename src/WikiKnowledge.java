import org.wikipedia.Wiki;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Shea on 2016-09-17.
 */
public class WikiKnowledge {
    private static final Wiki wiki;
    private static final String INFO_BOX_FLAG_TEXT = "{{Infobox military conflict";
    private static final Pattern REF_STRIP_PATTERN = Pattern.compile("<ref>.+</ref>|<ref name=.+/>"); //strips out ref tags that may throw off matching
    private static final Pattern LINK_STRIP_PATTERN = Pattern.compile("\\[{2}(.+?\\|)?(?<text>.+?)\\]{2}");
    private static final Pattern COMMENT_STRIP_PATTERN = Pattern.compile("<!--.+-->");
    private static final Pattern BATTLE_KILLED_PATTERN =
            Pattern.compile("((killed|dead): (?<count1>[0-9,]+))|((?<count2>[0-9,]+) (\\w+)?(dead|killed))");
    private static final Pattern BATTLE_LOCATION_PATTERN =
            Pattern.compile("\\|\\s*place\\s*=\\s*?(?<location>[\\w,' ]+)?");
    private static final Pattern BATTLE_DATE_PATTERN =
            Pattern.compile("\\|\\s*date\\s*=\\s*.*?(?<month>January|February|March|April|May|June|July|August|September|October|November|December).+?(?<year>\\d{3,4}( ?BC(E?))?)");
    private static final String[] killedPatternCountGroups = {"count1", "count2"};
    private static final Pattern BATTLE_RESULT_PATTERN = Pattern.compile("\\|\\s*result\\s*=\\s*(?<result>.+?)([,.]|\n)");

    static {
        wiki = new Wiki("en.wikipedia.org");
        wiki.setLogLevel(Level.OFF);
    }

    public static Optional<String> getBattleCasualties(String battleName, int chanceOfNotKnowing) {
        if (chanceOfNotKnowing > 0 && !KnowledgeChance.doesKnowAbout(battleName, chanceOfNotKnowing))
            return Optional.empty();
        Optional<String> infoBoxText = extractBattleInfoBox(battleName).map(WikiKnowledge::stripMarkup);
        if (!infoBoxText.isPresent()) return Optional.empty();
        final Matcher matcher = BATTLE_KILLED_PATTERN.matcher(infoBoxText.get());
        int totalKilled = 0;
        while (matcher.find()) {
            for (String s : killedPatternCountGroups) {
                String group = matcher.group(s);
                if (group != null && group.length() > 0)
                    totalKilled += Integer.parseInt(group.replace(",", ""));
            }
        }
        if (totalKilled < 1) return Optional.empty();
        return Optional.of(roundNumberString("" + totalKilled));
    }

    public static Optional<String> getBattleLocation(String battleName, int chanceOfNotKnowing) {
        if (chanceOfNotKnowing > 0 && !KnowledgeChance.doesKnowAbout(battleName, chanceOfNotKnowing))
            return Optional.empty();
        Optional<String> infoBoxText = extractBattleInfoBox(battleName).map(WikiKnowledge::stripMarkup);
        if (!infoBoxText.isPresent()) return Optional.empty();
        Matcher matcher = BATTLE_LOCATION_PATTERN.matcher(infoBoxText.get().replaceAll("[\\[\\]]", ""));
        if (!matcher.find()) return Optional.empty();
        String location = matcher.group("location");
        if (location == null || location.equals("")) return Optional.empty();
        return Optional.of(location);
    }

    public static Optional<String> getBattleDate(String battleName, int chanceOfNotKnowing) {
        if (chanceOfNotKnowing > 0 && !KnowledgeChance.doesKnowAbout(battleName, chanceOfNotKnowing))
            return Optional.empty();
        Optional<String> infoBoxText = extractBattleInfoBox(battleName).map(WikiKnowledge::stripMarkup);
        if (!infoBoxText.isPresent()) return Optional.empty();
        Matcher matcher = BATTLE_DATE_PATTERN.matcher(infoBoxText.get());
        if (!matcher.find()) return Optional.empty();
        if (matcher.group("year") == null || matcher.group("year").equals(""))
            return Optional.empty();
        return Optional.of((matcher.group("month") != null ?
                matcher.group("month") + " " : "") + matcher.group("year"));
    }

    public static Optional<String> getBattleResult(String battleName, int chanceOfNotKnowing)
    {
        if (chanceOfNotKnowing > 0 && !KnowledgeChance.doesKnowAbout(battleName, chanceOfNotKnowing))
            return Optional.empty();
        Optional<String> infoBoxText = extractBattleInfoBox(battleName).map(WikiKnowledge::stripMarkup);
        if (!infoBoxText.isPresent()) return Optional.empty();
        //strip refs and links
        Matcher matcher = BATTLE_RESULT_PATTERN.matcher(infoBoxText.get());
        if (!matcher.find())
            return Optional.empty();
        return Optional.of(matcher.group("result"));
    }

    /**
     * Attempts to extract the Battle infobox from the wiki page with the given name
     *
     * @param battleName the name of the battle to try to extract
     * @return Optional.empty() if unsuccessful, otherwise an Optional containing the contents of the infobox
     */
    private static Optional<String> extractBattleInfoBox(String battleName) {
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
        for (int i = beginInfoBoxIndex + 1; i < pageText.length() - 2; i++) {
            final String curSubstring = pageText.substring(i, i + 2);
            if (curSubstring.equals("{{")) {
                braceDepth++;
                //i++; //prevents double matching on "{{{"
            } else if (curSubstring.equals("}}")) {
                if (braceDepth == 0) {
                    endInfoBoxIndex = i;
                    break;
                }
                braceDepth--;
            }
        }
        if (endInfoBoxIndex < beginInfoBoxIndex) return Optional.empty();
        return Optional.of(pageText.substring(beginInfoBoxIndex, endInfoBoxIndex));
    }

    public static String roundNumberString(String numberString) {
        return numberString.substring(0, 1) + new String( //one liner way of generating a string with n zeroes, because Java
                new char[numberString.length() - 1]).replaceAll("\0", "0");
    }

    private static String stripMarkup(String text)
    {
        text = REF_STRIP_PATTERN.matcher(text).replaceAll("");
        text = LINK_STRIP_PATTERN.matcher(text).replaceAll("${text}");
        text = COMMENT_STRIP_PATTERN.matcher(text).replaceAll("");
        return text;
    }
}
