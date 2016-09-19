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
    public Chatbot(Responder defaultResponder, double confidenceThreshold, Responder... responders)
    {
        this.responders = new ArrayList<>(Arrays.asList(responders));
        this.defaultResponder = defaultResponder;
        this.confidenceThreshold = confidenceThreshold;
    }

    @Override
    public double getResponseConfidence(String sentence) {
        return responders.parallelStream()
                .map(r -> r.getResponseConfidence(sentence))
                .max(Comparator.naturalOrder())
                .orElse(0.0);
    }

    @Override
    public String respondTo(String sentence) {
        return responders.parallelStream()
                .max((r1, r2) -> Double.compare(r1.getResponseConfidence(sentence), r2.getResponseConfidence(sentence)))
                .filter(r -> r.getResponseConfidence(sentence) > confidenceThreshold)
                .orElse(defaultResponder)
                .respondTo(sentence);
    }
}
