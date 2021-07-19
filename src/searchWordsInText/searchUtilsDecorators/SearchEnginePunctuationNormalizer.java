package searchWordsInText.searchUtilsDecorators;

import searchWordsInText.api.ISearchEngine;

public class SearchEnginePunctuationNormalizer implements ISearchEngine {

    private final ISearchEngine searchEngine;

    public SearchEnginePunctuationNormalizer(ISearchEngine searchEngine) {
        this.searchEngine = searchEngine;
    }

    /**
     * Returns the number of a given word repetition in a given string (case sensitive). E.g., the word "May" will be
     * found 1 time in the string "Mayor May Maya may mayyy".
     * This Decorator helps resolve difficult issues like hyphen misuse, e.g. "mother-in--law".
     * @param text - String in which search is carried out
     * @param word to find
     * @return number of a given word repetition in a given text (case sensitive)
     */
    @Override
    public long search(String text, String word) {
        text = text.replaceAll("[\\x00-\\x2C\\x2E-\\x2F\\x3A-\\x40\\x5B-\\x60\\x7B-\\x7F]+", " ");
        text = text.replaceAll("(-){2,}?", "-");
        text = text.replaceAll(" -", " ");
        text = text.replaceAll("- ", " ");
        return searchEngine.search(text, word);
    }
}
