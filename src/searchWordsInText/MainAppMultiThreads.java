package searchWordsInText;

import searchWordsInText.api.ISearchEngine;
import searchWordsInText.searchUtils.*;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.*;

public class MainAppMultiThreads {
    public static void main(String[] args) {

        String userPath = "acts";
        System.out.println("Here's a list of some Illinois legislation acts (Compiled Statutes):");
        File actsDirectory = new File(userPath);
        File[] actsFiles = actsDirectory.listFiles((dir, name) -> name.endsWith(".txt"));

        if (actsFiles == null || actsFiles.length == 0) {
            System.out.println("There's no *.txt files in the given directory.");
            return;
        }

        try (BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {
            for (int i = 0; i < actsFiles.length; i++) {
                System.out.println(i + 1 + ". " + actsFiles[i].getName());
            }

            System.out.println("\n" + "Search will be carried out in all acts.");
            /* using the method to choose whether to continue writing search results to file
            or to start over (with verification) */
            boolean clearOrNot = outFileClearOrContinue(userInput);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("searchResultMultiThreads.txt", !clearOrNot))) {
                searchWordsAndWriteToFile(actsFiles, writer, userInput, new RegExSearch());
            }
            System.out.println("Search results have been saved to \"searchResultMultiThreads.txt\" in the parent directory.");
        } catch (MalformedInputException e) {
            System.out.println("Failed to read the selected act - it has non-UTF-8 encoding.");
        } catch (IOException e) {
            System.out.println("Something went wrong while processing the selected file. Please restart the program.");
        }
    }

    public static void searchWordsAndWriteToFile (File[] actsFiles, BufferedWriter writer, BufferedReader reader,
                                                  ISearchEngine searchEngine) throws IOException {
        System.out.println("Input a word you're looking for. Type \"stop\" to end searching.");
        while (true) {
            ExecutorService exe = Executors.newFixedThreadPool(actsFiles.length);
            // getting a word from a user to search for
            String word = reader.readLine();
            // interrupting the cycle if "stop"-word is typed
            if (word.equals("stop")) {
                break;
            }
            // searching the typed word
            List<Future<Long>> searchTasksList = new ArrayList<>();
            for (File act : actsFiles) {
                searchTasksList.add(exe.submit(new SearchInFile(act, word, writer, searchEngine)));
            }

            long result = 0;
            try {
                while (searchTasksList.size() > 0) {
                    Iterator<Future<Long>> it = searchTasksList.iterator();
                    while (it.hasNext()) {
                        Future<Long> futureSearchTask = it.next();
                        if (futureSearchTask.isDone()) {
                            result += futureSearchTask.get();
                            it.remove();
                        }
                    }
                }
                writer.write("* * * * * The word \"" + word + "\" is found " + result + " time(s) in all acts. * * * * *"
                        + "\n\n");

            } catch (InterruptedException | ExecutionException e) {
                exe.shutdown();
                System.out.println("An error occurred during request processing. Try again.");
            }
            exe.shutdown();
        }
        writer.flush();
    }

    /**
     * Allows to ask whether User wants to clear search history or to continue writing search history to file.
     * @param reader BufferedReader passed to the method (for common use in try-with-resources block)
     * @return true if User want to clear search history
     * @throws IOException to process in the app
     */
    public static boolean outFileClearOrContinue (BufferedReader reader) throws IOException {
        System.out.println("Do you want to clear search history or continue writing search results to file? " +
                "Type \"yes\" to clear or \"no\" to continue writing and press Enter.");
        String userWantToClear = reader.readLine();
        while (!userWantToClear.equals("yes") && !userWantToClear.equals("no")) {
            System.out.println("You've typed something wrong, try again - yes/no:");
            userWantToClear = reader.readLine();
        }
        return userWantToClear.equals("yes");
    }
}

/**
 * This task is searching given word in a text file, returns the number of matching
 */
class SearchInFile implements Callable<Long> {
    File act;
    String word;
    BufferedWriter writer;
    ISearchEngine searchEngine;

    SearchInFile (File act, String word, BufferedWriter writer, ISearchEngine searchEngine) {
        this.word = word;
        this.writer = writer;
        this.act = act;
        this.searchEngine = searchEngine;
    }

    @Override
    public Long call() throws IOException {
        long result;
        String actText = Files.readString(act.toPath());
        result = searchEngine.search(actText, word);
        writer.write("\"" + act.getName() + "\" - " + word + " - " + result+"\n");
        return result;
    }
}