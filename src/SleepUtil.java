/**
 * Created by Shea on 2016-09-19.
 */
public class SleepUtil {
    public static void interruptibleSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }
}
