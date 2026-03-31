package com.alexeev.composite;

import com.alexeev.composite.entity.TextComponent;
import com.alexeev.composite.exception.CustomException;
import com.alexeev.composite.parser.TextParser;
import com.alexeev.composite.reader.TextFileReader;
import com.alexeev.composite.service.TextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) throws CustomException {
    String filePath = "src/main/resources/text.txt";
    TextFileReader reader = new TextFileReader();
    TextParser parser = TextParser.createChain();
    TextService service = new TextService();

    String rawText = reader.read(filePath);
    TextComponent text = parser.parse(rawText);

    logger.info("=== RESTORED TEXT ===\n{}", text.restore());

    Map<String, Long> counts = service.countLettersAndSymbols(text);
    logger.info("=== LETTER & SYMBOL COUNT === Letters: {}, Symbols: {}",
            counts.get("letters"), counts.get("symbols"));

    int max = service.findMaxSentencesWithCommonWord(text);
    logger.info("=== TASK 1: Max sentences with a common word: {}", max);

    List<TextComponent> sorted = service.sortSentencesByLexemeCount(text);
    StringBuilder sb = new StringBuilder("=== TASK 2: Sentences sorted by lexeme count ===");
    for (TextComponent s : sorted) {
      sb.append("\n[").append(s.getChildren().size()).append(" lexemes] ").append(s.restore());
    }
    logger.info("{}", sb);

    TextComponent modified = service.swapFirstAndLastLexeme(text);
    logger.info("=== TASK 3: Swap first and last lexeme ===\n{}", modified.restore());

  }
}
