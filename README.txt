CS427 History Chatbot
By Robert Spidle and Shea Polansky

How to run: run the included .jar file in the terminal. Add the command line argument "echo" to force it to echo all inputs; this is useful for capturing output in a text file.
    -To reproduce the testing results, use "java -jar History-Chatbot.jar echo < testinput.txt > testoutput.txt"
        -Note: results may differ if the Wikipedia articles change
How to build: open the included IntelliJ project and build it, or manually build the .java files and copy the contents of the "language" folder into the same directory as the resulting .class files

Design notes:
The basic design is based around an interface called "responder", which provides two methods. The first method "getResponseConfidence" returns a Double between 0 and 1 (inclusive) which represents the confidence that responder has in its ability to respond correctly to the given sentence. The second method "respondTo" asks the Responder to actually generate a response to the given sentence. For ease of use, a class called BasicResponder was created, which accepts files containing a list of keywords and a list of responses (newline separated); the confidence is the percentage of keywords matched, and the response will be randomly selected from within the list of responses. The rest of the Responder classes inherit from this one to speed development. In order to hopefully curb nonsensical answers, a confidence threshold was implemented, below which the chatbot will give a generic "change the subject" response rather than answer with something that was probably completely unrelated to the question asked.
One of the challenges of designing a chatbot is giving it a knowledge base. Rather than a manually curated deep but specific knowledge base, we went with a different approach. We decided that we would parse Wikipedia for certain kinds of information on the fly. We feel this creates a more realistic chat bot, as most people won't know every detail of some subject, but will have a lot of general knowledge about that subject. In addition, to prevent our chatbot from simply knowing everything, we created a way of deterministically choosing subjects that the chatbot simply does not know. The "deterministic" part is important; if we simply used an RNG, the chatbot would randomly remember and forget things withing the span of a few seconds, which would be very strange. Instead, a checksum is computed based on the name of the subject, and with a given probability he will not know it. Because checksums do not change for a given input, we can deterministically cause it to forget certain subjects without hard-coding those subjects.
Additional realism features we added include a randomized delay between responses, to simulate a human typing. We also added a small chance of him mistyping and correcting himself after a short delay, again to better simulate a real, fallible human typing at a keyboard.

Incomplete list of subjects/topics known:
    -Historical Battles
        -Location
        -Outcome
        -Date
        -Casualty counts
    -Historical figures
        -{Birth,death} {date,place}
    -Personal topics
        -location
        -occupation
        -favorite food

Credits:
This project uses a slightly modified Wiki.java (Wikipedia API wrapper), taken from the following open source project: https://github.com/MER-C/wiki-java (License: GNU GPLv3)
