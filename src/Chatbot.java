import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Shea on 2016-09-19.
 */
public class Chatbot implements Responder{
    public final List<Responder> responders;
    public final Responder defaultResponder;
    public final double confidenceThreshold;

    /**
     * Constructs a new chatbot with the given parameters
     * @param defaultResponder the default responder that will be used if the
     *                         chatbot is asked to respond to a sentence
     *                         where none of its responders report confidence
     *                         greater than the confidence threshold
     * @param confidenceThreshold the confidence threshold below which the default
     *                            responder will be used
     * @param responders the list of responders that the chatbot will draw from
     */
    public Chatbot(Responder defaultResponder, double confidenceThreshold, Responder... responders)
    {
        this.responders = new ArrayList<>(Arrays.asList(responders));
        this.defaultResponder = defaultResponder;
        this.confidenceThreshold = confidenceThreshold;
    }

    @Override
    public double getResponseConfidence(String sentence) {
        return responders.parallelStream()
                .map(r -> r.getResponseConfidence(sentence.replaceAll("\\s+", " ")))
                .max(Comparator.naturalOrder())
                .orElse(0.0);
    }

    @Override
    public String respondTo(String sentence) {
        String condensedSentence = sentence.replaceAll("\\s+", " ");
        return responders.parallelStream()
                .max((r1, r2) -> Double.compare(r1.getResponseConfidence(condensedSentence), r2.getResponseConfidence(condensedSentence)))
                .filter(r -> r.getResponseConfidence(condensedSentence) > confidenceThreshold)
                .orElse(defaultResponder)
                .respondTo(sentence);
    }
}
