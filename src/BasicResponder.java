import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by Shea on 2016-09-15.
 */
public class BasicResponder implements Responder {
    protected final List<List<String>> keywords;
    protected final List<String> responses;

    public BasicResponder(String pathToKeywords, String pathToResponses) {
        keywords = new ArrayList<>();
        responses = new ArrayList<>();
        try {
            if (pathToResponses != null) {
                keywords.addAll(readLinesFromFile(pathToKeywords).stream()
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

    public BasicResponder(String pathSuffix)
    {
        this(Paths.get("keywords/", pathSuffix).toString(),
                Paths.get("responses/", pathSuffix).toString());
    }

    private static List<String> readLinesFromFile(String path) throws URISyntaxException, IOException {
        return Files.readAllLines(Paths.get(Responder.class.getResource(path).toURI()));
    }

    @Override
    public double getResponseConfidence(String sentence) {
        double maxConfidence = Double.MIN_VALUE;
        for (List<String> keywordList : keywords)
        {
            int foundWords = 0;
            for (String keyword : keywordList)
            {
                if (keyword.matches(".*(\\s|^)" + keyword + "(\\s|$).*")) foundWords++;
            }
            maxConfidence = Math.max(maxConfidence, (double)foundWords / keywordList.size());
        }
        return maxConfidence;
    }

    @Override
    public String respondTo(String sentence) {
        return responses.get(ThreadLocalRandom.current().nextInt(responses.size()));
    }
}
