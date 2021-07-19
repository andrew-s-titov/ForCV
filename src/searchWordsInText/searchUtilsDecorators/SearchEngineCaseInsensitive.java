package searchWordsInText.searchUtilsDecorators;

import searchWordsInText.api.ISearchEngine;
import searchWordsInText.searchUtils.*;

public class SearchEngineCaseInsensitive implements ISearchEngine {

    private final ISearchEngine searchEngine;

    public SearchEngineCaseInsensitive (ISearchEngine searchEngine) {
        this.searchEngine = searchEngine;
    }

    /**
     * Returns the number of a given word repetition in a given string (case insensitive). E.g., the word "May" will be
     * found 2 times in the string "Mayor May Maya may mayyy".
     * @param text - String in which search is carried out
     * @param word to find
     * @return number of a given word repetition in a given text (case insensitive)
     */
    @Override
    public long search(String text, String word) {
        if (this.searchEngine instanceof EasySearch) {
            return searchEngine.search(text.toLowerCase(), word.toLowerCase());
        } else if (this.searchEngine instanceof RegExSearch){
            String newWord = "(?ui)"+word;
            return searchEngine.search(text, newWord);
        } else {
            // in case there's another search util
            return searchEngine.search(text, word);
        }
    }
}
