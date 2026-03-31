package com.alexeev.composite.parser;

import com.alexeev.composite.entity.ComponentType;
import com.alexeev.composite.entity.TextComponent;
import com.alexeev.composite.entity.TextComposite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SentenceParser extends AbstractParser {

  private static final Logger logger = LoggerFactory.getLogger(SentenceParser.class);
  private static final Pattern LEXEME_SPLIT = Pattern.compile("\\S+");

  @Override
  public TextComponent parse(String text) {
    TextComposite sentence = new TextComposite(ComponentType.SENTENCE);
    String raw = text.strip();
    logger.debug("Parsing sentence: '{}'", raw);
    Matcher matcher = LEXEME_SPLIT.matcher(raw);
    while (matcher.find()) {
      String token = matcher.group();
      if (next != null) {
        sentence.add(next.parse(token));
      }
    }
    return sentence;
  }
}
