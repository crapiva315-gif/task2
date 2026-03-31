package com.alexeev.composite;

import com.alexeev.composite.entity.ComponentType;
import com.alexeev.composite.entity.TextComponent;
import com.alexeev.composite.parser.TextParser;
import com.alexeev.composite.service.TextService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TextHandlerTest {

    private TextParser parser;
    private TextService service;

    private static final String SAMPLE =
            "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. " +
            "It was popularised in the with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n\n" +
            "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. " +
            "The point of using Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable по английски.\n\n" +
            "It is a established fact that a reader will be of a page when looking at its layout.\n\n" +
            "Bye.";

    @BeforeEach
    void setUp() {
        parser = TextParser.createChain();
        service = new TextService();
    }

    @Test
    void testParseNotNull()  {
        TextComponent text = parser.parse(SAMPLE);
        assertNotNull(text);
    }

    @Test
    void testRootTypeIsText() {
        TextComponent text = parser.parse(SAMPLE);
        assertEquals(ComponentType.TEXT, text.getType());
    }

    @Test
    void testParagraphCount() {
        TextComponent text = parser.parse(SAMPLE);
        long paragraphs = text.getChildren().stream()
                .filter(c -> c.getType() == ComponentType.PARAGRAPH)
                .count();
        assertEquals(4, paragraphs);
    }

    @Test
    void testRestoreContainsOriginalWords() {
        TextComponent text = parser.parse(SAMPLE);
        String restored = text.restore();
        assertTrue(restored.contains("survived"));
        assertTrue(restored.contains("Ipsum"));
        assertTrue(restored.contains("Bye."));
    }

    @Test
    void testCountLettersAndSymbols() {
        TextComponent text = parser.parse("Hi! Go.");
        Map<String, Long> counts = service.countLettersAndSymbols(text);
        assertTrue(counts.get("letters") > 0);
        assertTrue(counts.get("symbols") >= counts.get("letters"));
    }

    @Test
    void testLetterCountSimple() {
        // "Hi!" has 2 letters, 3 symbols
        TextComponent text = parser.parse("Hi!");
        Map<String, Long> counts = service.countLettersAndSymbols(text);
        assertEquals(2, counts.get("letters"));
        assertEquals(3, counts.get("symbols"));
    }

    @Test
    void testSortSentencesByLexemeCount() {
        TextComponent text = parser.parse(SAMPLE);
        List<TextComponent> sorted = service.sortSentencesByLexemeCount(text);
        for (int i = 0; i < sorted.size() - 1; i++) {
            assertTrue(sorted.get(i).getChildren().size() <= sorted.get(i + 1).getChildren().size());
        }
    }

    @Test
    void testSwapFirstAndLastLexeme() {
        TextComponent text = parser.parse("Hello world.");
        service.swapFirstAndLastLexeme(text);
        String restored = text.restore();
        assertTrue(restored.contains("world"));
        assertTrue(restored.contains("Hello"));
    }

    @Test
    void testSwapPreservesLength() {
        String input = "Hello world.";
        TextComponent text = parser.parse(input);
        String before = text.restore();
        service.swapFirstAndLastLexeme(text);
        String after = text.restore();
        assertEquals(before.length(), after.length());
    }

    @Test
    void testFindMaxSentencesWithCommonWord() {
        TextComponent text = parser.parse(SAMPLE);
        int max = service.findMaxSentencesWithCommonWord(text);
        assertTrue(max >= 2, "Expected at least 2 sentences to share a word like 'it' or 'a'");
    }






}
