import java.util.zip.CRC32;

/**
 * Created by Shea on 2016-09-17.
 */
public class KnowledgeChance {
    public static final int DEFAULT_CHANCE_OF_FAILURE = 10;
    /**
     * Determines (by pseudorandom chance) whether or not the chatbot should know
     * about a given subject. Whether or not the bot knows about the subject is
     * deterministic (assuming the chance of not knowing remains constant).
     * @param name the name of the subject (case-insenstive, and ignoring
     *             leading and trailing spaces)
     * @param chanceOfFailure the chance of failure (given as one-in-x, where x
     *                        is the value of the parameter)
     * @return true if the chatbot should know, false otherwise
     */
    public static boolean doesKnowAbout(String name, final int chanceOfFailure)
    {
        name = name.toLowerCase().replaceAll("^\\s+", "").replaceAll("\\s+$", "");
        final CRC32 checksum = new CRC32();
        checksum.update(name.getBytes());
        return checksum.getValue() % chanceOfFailure == 0;
    }

    public static boolean doesKnowAbout(String name)
    {
        return doesKnowAbout(name, DEFAULT_CHANCE_OF_FAILURE);
    }
}
