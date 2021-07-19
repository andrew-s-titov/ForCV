package searchWordsInText.searchUtils;

import searchWordsInText.api.ISearchEngine;

import java.util.regex.*;

public class RegExSearch implements ISearchEngine {

    /**
     * Returns the number of a given word repetition in a given string (case sensitive). E.g., the word "May" will be
     * found 1 time in the string "Mayor May Maya may mayyy".
     * @param text - String in which search is carried out
     * @param word to find
     * @return number of a given word repetition in a given text (case sensitive)
     */
    @Override
    public long search(String text, String word) {
        Pattern pattern = Pattern.compile("\\b"+word+"\\b");
        Matcher matcher = pattern.matcher(text);
        int result = 0;
        while (matcher.find()) {
            result++;
        }
        return result;
    }
}
