import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Created by Shea on 2016-09-15.
 */
public class BasicResponder implements Responder {
    protected final List<List<String>> keywords;
    protected final List<String> responses;
    public double confidenceMultiplier = 1.0;

    public BasicResponder(String pathToKeywords, String pathToResponses) {
        keywords = new ArrayList<>();
        responses = new ArrayList<>();
        try {
            if (pathToKeywords != null) {
                keywords.addAll(readLinesFromFile(pathToKeywords).stream()
                        .map(String::toLowerCase)
                        .map(s -> s.split(" "))
                        .map(Arrays::asList)
                        .collect(Collectors.toList()));
            }
            responses.addAll(readLinesFromFile(pathToResponses));
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public BasicResponder(String pathSuffix) {
        this("keywords/" + pathSuffix,
                "responses/" + pathSuffix);
    }

    private static List<String> readLinesFromFile(String path) throws URISyntaxException, IOException {
        InputStream in = BasicResponder.class.getResourceAsStream(path);
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        return r.lines().collect(Collectors.toList());
    }

    @Override
    public double getResponseConfidence(String sentence) {
        double maxConfidence = Double.MIN_VALUE;
        sentence = sentence.toLowerCase().replaceAll("[.,?!]", "");
        for (List<String> keywordList : keywords) {
            int foundWords = 0;
            for (String keyword : keywordList) {
                if (sentence.matches(".*(\\s|^)" + keyword + "(\\s|$).*"))
                    foundWords++;
            }
            maxConfidence = Math.max(maxConfidence, (double) foundWords / keywordList.size());
        }
        return maxConfidence * confidenceMultiplier;
    }

    @Override
    public String respondTo(String sentence) {
        return responses.get(ThreadLocalRandom.current().nextInt(responses.size()));
    }
}
