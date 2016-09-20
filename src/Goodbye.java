/**
 * Created by Shea on 2016-09-19.
 */
public class Goodbye extends BasicResponder {
    public Goodbye() {
        super("goodbye.txt");
    }

    @Override
    public String respondTo(String sentence) {
        Output.printResponse(super.respondTo(sentence));
        System.exit(0);
        return null;
    }
}
