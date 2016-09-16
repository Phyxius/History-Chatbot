/**
 * Created by Shea on 2016-09-15.
 */
public interface Responder {
    double getResponseConfidence(String sentence);
    String respondTo(String sentence);
}
