package searchWordsInText;

import searchWordsInText.api.ISearchEngine;
import searchWordsInText.searchUtils.*;
import utils.InputCheck;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;

public class MainApp {
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

            // using the method for act number selection in the correct range (with verification)
            int actNumber = selectAct(actsFiles.length);
            File selectedActFile = actsFiles[actNumber-1];
            System.out.println("You've selected \"" + actNumber + ". " + selectedActFile.getName() + "\".");

            /* using the method to choose whether to continue writing search results to file
            or to start over (with verification) */
            boolean clearOrNot = outFileClearOrContinue(userInput);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("searchResult.txt", !clearOrNot))) {
                searchWordsAndWriteToFile (selectedActFile, new RegExSearch(), writer, userInput);
            }
            System.out.println("Search results have been saved to \"searchResult.txt\" in the parent directory.");
        } catch (MalformedInputException e) {
            System.out.println("Failed to read the selected act - it has non-UTF-8 encoding.");
        } catch (IOException e) {
            System.out.println("Something went wrong while processing the selected file. Please restart the program.");
        }
    }

    public static void searchWordsAndWriteToFile (File act, ISearchEngine searchEngine, BufferedWriter writer,
                                                  BufferedReader reader) throws IOException {
        System.out.println("Input a word you're looking for. Type \"stop\" to end searching.");
        long result;
        while (true) {
            // getting a word from a user to search for
            String word = reader.readLine();
            // interrupting the cycle if "stop"-word is typed
            if (word.equals("stop")) {
                break;
            }
            // searching the typed word
            String actText = Files.readString(act.toPath());
            result = searchEngine.search(actText, word);
            // writing the result to a text file
            writer.write("\"" + act.getName() + "\" - " + word + " - " + result+"\n");
        }
        writer.flush();
    }

    /**
     * Allows to select an index number from a certain list, restricting the user from typing NaN symbols or number out
     * of list size.
     * @param listSize size of a list (array size)
     * @return an index number selected by a user. It's not an index of array, should be decremented by 1 when accessing
     * an array to avoid ArrayIndexOutOfBoundsException.
     */
    public static int selectAct(int listSize) {
        System.out.println("\nSelect an act - type the index number and press Enter.");
        InputCheck inputActNumber = new InputCheck("Act number must be typed, try again.");
        int actNumber = inputActNumber.checkIntInput();
        while (actNumber <= 0 || actNumber > listSize) {
            System.out.println("There's no act with that index number in the list! Try again.");
            actNumber = inputActNumber.checkIntInput();
        }
        return actNumber;

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
