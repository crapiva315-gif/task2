package com.alexeev.composite.parser;

import com.alexeev.composite.entity.ComponentType;
import com.alexeev.composite.entity.TextComponent;
import com.alexeev.composite.entity.TextComposite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class LexemeParser extends AbstractParser {

  private static final Logger logger = LoggerFactory.getLogger(LexemeParser.class);
  private static final Pattern LEXEME_PATTERN = Pattern.compile("\\S+");

  @Override
  public TextComponent parse(String text) {

    TextComposite lexeme = new TextComposite(ComponentType.LEXEME);
    String token = text.strip();
    logger.debug("Parsing lexeme: '{}'", token);
    for (int i = 0; i < token.length(); i++) {
      if (next != null) {
        lexeme.add(next.parse(String.valueOf(token.charAt(i))));
      }
    }
    return lexeme;
  }
}
