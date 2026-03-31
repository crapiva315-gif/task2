package com.alexeev.composite.parser;

import com.alexeev.composite.entity.ComponentType;
import com.alexeev.composite.entity.TextComponent;
import com.alexeev.composite.entity.TextComposite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class TextParser extends AbstractParser {

  private static final Logger logger = LoggerFactory.getLogger(TextParser.class);
  private static final Pattern PARAGRAPH_SPLIT = Pattern.compile("(?m)(?:^[ \\t]*$)+");

  public static TextParser createChain() {
    TextParser textParser = new TextParser();
    ParagraphParser paragraphParser = new ParagraphParser();
    SentenceParser sentenceParser = new SentenceParser();
    LexemeParser lexemeParser = new LexemeParser();
    SymbolParser symbolParser = new SymbolParser();

    textParser.setNext(paragraphParser)
            .setNext(sentenceParser)
            .setNext(lexemeParser)
            .setNext(symbolParser);

    logger.info("Parser chain created: Text -> Paragraph -> Sentence -> Lexeme -> Symbol");
    return textParser;
  }

  @Override
  public TextComponent parse(String text) {
    TextComposite root = new TextComposite(ComponentType.TEXT);
    logger.info("Parsing text of length {}", text.length());

    String[] lines = text.split("\\r?\\n");
    StringBuilder current = new StringBuilder();
    for (String line : lines) {
      if (line.isBlank()) {
        if (!current.isEmpty()) {
          addParagraph(root, current.toString());
          current = new StringBuilder();
        }
      } else {
        if (!current.isEmpty()) {
          current.append(" ");
        }
        current.append(line.strip());
      }
    }
    if (!current.isEmpty()) {
      addParagraph(root, current.toString());
    }
    logger.info("Parsed {} paragraph(s)", root.getChildren().size());
    return root;
  }

  private void addParagraph(TextComposite root, String paragraphText) {
    if (next != null) {
      root.add(next.parse(paragraphText));
    }
  }
}
