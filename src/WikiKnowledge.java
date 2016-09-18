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
    private static final String BATTLE_INFO_BOX_FLAG_TEXT = "{{Infobox military conflict";
    private static final String PERSON_INFO_BOX_FLAG_TEXT = "{{Infobox"; //because there are many different variants for people
    private static final Pattern REF_STRIP_PATTERN = Pattern.compile("<ref>.+</ref>|<ref name=.+/>"); //strips out ref tags that may throw off matching
    private static final Pattern LINK_STRIP_PATTERN = Pattern.compile("\\[{2}(.+?\\|)?(?<text>.+?)\\]{2}");
    private static final Pattern COMMENT_STRIP_PATTERN = Pattern.compile("<!--.+-->");
    private static final Pattern BATTLE_KILLED_PATTERN =
            Pattern.compile("((killed|dead): (?<count1>[0-9,]+))|((?<count2>[0-9,]+) (\\w+)?(dead|killed))");
    private static final Pattern BATTLE_LOCATION_PATTERN =
            Pattern.compile("\\|\\s*place\\s*=\\s*?(?<location>[\\w,' ]+)?");
    private static final Pattern BATTLE_DATE_PATTERN =
            Pattern.compile("\\|\\s*date\\s*=\\s*.*?(?<month>January|February|March|April|May|June|July|August|September|October|November|December).+?(?<year>\\d{3,4}( ?BC(E?))?)");
    private static final String[] KILLED_PATTERN_COUNT_GROUPS = {"count1", "count2"};
    private static final Pattern BATTLE_RESULT_PATTERN = Pattern.compile("\\|\\s*result\\s*=\\s*(?<result>.+?)([,.]|\n)");
    private static final Pattern PERSON_BIRTHDATE_PATTERN = Pattern.compile("\\|\\s*birth_date\\s*=\\s*.*?((?<birthdate1>\\d+s? BC(E)?)|.+?(?<birthdate2>\\d{4}s?))");
    private static final String[] PERSON_BIRTHDATE_GROUPS = {"birthdate1", "birthdate2"};
    private static final Pattern PERSON_DEATHHDATE_PATTERN = Pattern.compile("\\|\\s*death_date\\s*=\\s*.*?((?<deathdate1>\\d+s? BC(E)?)|.+?(?<deathdate2>\\d{4}s?))");
    private static final String[] PERSON_DEATHDATE_GROUPS = {"deathdate1", "deathdate2"};

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
            for (String s : KILLED_PATTERN_COUNT_GROUPS) {
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

    public static Optional<String> getPersonBirthDate(String personName, int chanceOfNotKnowing)
    {
        return getPersonDate(personName, chanceOfNotKnowing, PERSON_BIRTHDATE_PATTERN, PERSON_BIRTHDATE_GROUPS);
    }

    public static Optional<String> getPersonDeathDate(String personName, int chanceOfNotKnowing)
    {
        return getPersonDate(personName, chanceOfNotKnowing, PERSON_DEATHHDATE_PATTERN, PERSON_DEATHDATE_GROUPS);
    }

    private static Optional<String> getPersonDate(String personName, int chanceOfNotKnowing, Pattern findPattern, String[] findPatternGroups)
    {
        if (chanceOfNotKnowing > 0 && !KnowledgeChance.doesKnowAbout(personName, chanceOfNotKnowing))
            return Optional.empty();
        Optional<String> infoBoxText = extractPersonInfoBox(personName).map(WikiKnowledge::stripMarkup);
        if (!infoBoxText.isPresent()) return Optional.empty();
        Matcher matcher = findPattern.matcher(infoBoxText.get());
        if (!matcher.find()) return Optional.empty();
        for (String group : findPatternGroups) {
            if (matcher.group(group) != null && !matcher.group(group).equals(""))
                return Optional.of(matcher.group(group));
        }
        return Optional.empty();
    }

    /**
     * Attempts to extract the Battle infobox from the wiki page with the given name
     *
     * @param battleName the name of the battle to try to extract
     * @return Optional.empty() if unsuccessful, otherwise an Optional containing the contents of the infobox
     */
    private static Optional<String> extractBattleInfoBox(String battleName) {
        return extractInfoBox(battleName, BATTLE_INFO_BOX_FLAG_TEXT);
    }

    private static Optional<String> extractPersonInfoBox(String personName)
    {
        return extractInfoBox(personName, PERSON_INFO_BOX_FLAG_TEXT);
    }

    /**
     * Attempts to extract the page infobox from the wiki page with the given name
     *
     * @param pageName the name of the battle to try to extract
     * @param infoBoxFlagText the text to look for that signals the beginning of an infobox
     * @return Optional.empty() if unsuccessful, otherwise an Optional containing the contents of the infobox
     */
    private static Optional<String> extractInfoBox(String pageName, String infoBoxFlagText)
    {
        String pageText;
        try {
            String[][] results;
            results = wiki.search(pageName, Wiki.MAIN_NAMESPACE);
            if (results.length < 1) return Optional.empty();
            pageText = wiki.getPageText(results[0][0]);
        } catch (IOException e) {
            return Optional.empty();
        }
        if (!pageText.contains(infoBoxFlagText)) return Optional.empty();
        final int beginInfoBoxIndex = pageText.indexOf(infoBoxFlagText);
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
