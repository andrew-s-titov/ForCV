package searchWordsInText.searchUtils;

import searchWordsInText.api.ISearchEngine;

public class EasySearch implements ISearchEngine {

    private final String symbols = "\"'`!?@№#$%:;^&*_-=+\\|/.,?~()<>{}[]\t\n\r ";

    /**
     * Returns the number of a given word repetition in a given string (case sensitive). E.g., the word "May" will be
     * found 1 time in the string "Mayor May Maya may mayyy".
     * @param text - String in which search is carried out
     * @param word to find
     * @return number of a given word repetition in a given text (case sensitive)
     */
    @Override
    public long search(String text, String word) {
        int position = 0;
        int result = 0;

        while (true) {
            position = text.indexOf(word, position);
            if (position == -1) {
                break;
            } else {
                if (position == 0) {
                     if (checkCharIsNotALetter(text, position+word.length())) {
                         result++;
                     }
                } else if (position == text.length()-word.length()) {
                    if (checkCharIsNotALetter(text, position-1)) {
                        result++;
                    }
                } else {
                    if (checkCharIsNotALetter(text, position+word.length()) & checkCharIsNotALetter(text, position-1)) {
                        result++;
                    }
                }
                position = position+word.length();
            }
        }

        return result;
    }

    /**
     * Checks whether the given character in the string is a ASCII letter or not.
     * @param text - String in which search is carried out
     * @param position of the character in the string
     * @return true - if the character is a ASCII letter, false - if not
     */
    private boolean checkCharIsNotALetter(String text, int position) {
        char ch = text.charAt(position);
        boolean result = false;
        for (int i = 0; i < this.symbols.length(); i++) {
            char temp = this.symbols.charAt(i);
            if (ch == temp) {
                result = true;
                break;
            }
        }
        return result;
    }
}
