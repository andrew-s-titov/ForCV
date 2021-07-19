package searchWordsInText.test;

import searchWordsInText.api.ISearchEngine;
import searchWordsInText.searchUtilsDecorators.*;
import searchWordsInText.searchUtils.*;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SearchTest {
    ISearchEngine engine;

    String text1 = "Mayor May* -Maya #may!! mayy.y";

    @Order(1)
    @Test
    @DisplayName("Testing EasySearch")
    public void EasySearchTest() {
        engine = new EasySearch();
        Assertions.assertEquals(1, engine.search(text1, "May"));
    }

    @Order(2)
    @Test
    @DisplayName("Testing RegExSearch")
    public void RegExSearchTest() {
        engine = new RegExSearch();
        Assertions.assertEquals(1, engine.search(text1, "May"));
    }

    @Order(3)
    @Test
    @DisplayName("Testing SearchEngineCaseInsensitive")
    public void SearchEngineCaseInsensitive() {
        engine = new SearchEngineCaseInsensitive(new RegExSearch());
        Assertions.assertEquals(2, engine.search(text1, "May"));
        engine = new SearchEngineCaseInsensitive(new EasySearch());
        Assertions.assertEquals(2, engine.search(text1, "May"));
    }

    @Order(4)
    @Test
    @DisplayName("Проверяем работу SearchEnginePunctuationNormalizer(new RegExSearch)")
    public void SearchEnginePunctuationNormalizer() {
        engine = new SearchEnginePunctuationNormalizer(new RegExSearch());
        Assertions.assertEquals(1, engine.search(text1, "May"));
    }
}
