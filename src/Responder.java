/**
 * Created by Shea on 2016-09-15.
 */
public interface Responder {
    /**
     * Returns the confidence level (in the range 0 to 1, inclusive) that this
     * Responder is capable of responding to the sentence.
     *
     * @param sentence the sentence
     * @return the confidence level (between 0 and 1 inclusive)
     */
    double getResponseConfidence(String sentence);

    /**
     * Responds to the given sentence
     *
     * @param sentence the sentence to respond to
     * @return the response to the sentence
     */
    String respondTo(String sentence);
}
