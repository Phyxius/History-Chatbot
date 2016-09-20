import java.util.Scanner;

/**
 * Created by Shea on 2016-09-15.
 */

public class Main {
    public static void main(String[] args)
    {
        Chatbot chatbot = new Chatbot(new BasicResponder(null, "responses/defaultResponses.txt"), .5,
                new BirthDate("keywords/birthDate.txt", "responses/defaultResponses.txt"),
                new BirthLocation("keywords/birthLocation.txt", "responses/defaultResponses.txt"),
                new CasualtiesBattle("keywords/casualtiesBattle.txt", "responses/defaultResponses.txt"),
                new DeathDate("keywords/deathDate.txt", "responses/defaultResponses.txt"),
                new DeathLocation("keywords/deathLocation.txt", "responses/defaultResponses.txt"),
                new LocationBattle("keywords/locationBattle.txt", "responses/defaultResponses.txt"),
                new OutcomeBattle("keywords/outcomeBattle.txt", "responses/defaultResponses.txt"),
                new WhenBattle("keywords/whenBattle.txt", "responses/defaultResponses.txt"),
                new Goodbye());
        String[] responsePaths = {
                "age.txt",
                "appearance.txt",
                "catsOrDogs.txt",
                "degree.txt",
                "destination.txt",
                "favBattle.txt",
                "favHistFigure.txt",
                "favoriteTime.txt",
                "freeTime.txt",
                "greetings.txt",
                "introduction.txt",
                "location.txt",
                "occupation.txt",
                "otherName.txt",
                "school.txt",
                "siblings.txt"
        };
        for (String responsePath : responsePaths) {
            chatbot.responders.add(new BasicResponder(responsePath));
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to ChatRoom(TM).");
        SleepUtil.interruptibleSleep(500);
        System.out.print("Now connecting you to your friend... ");
        SleepUtil.interruptibleSleep(500);
        System.out.println("Done! Say 'hi'!");
        while (true)
        {
            System.out.print("> ");
            Output.printResponse(chatbot.respondTo(scanner.nextLine()));
        }
    }

}
