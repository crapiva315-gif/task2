package com.alexeev.composite.parser;

import com.alexeev.composite.entity.ComponentType;
import com.alexeev.composite.entity.TextComponent;
import com.alexeev.composite.entity.TextComposite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParagraphParser extends AbstractParser {

  private static final Logger logger = LoggerFactory.getLogger(ParagraphParser.class);
  private static final Pattern SENTENCE_SPLIT = Pattern.compile("[^.!?]*[.!?]+");

  @Override
  public TextComponent parse(String text) {
    TextComposite paragraph = new TextComposite(ComponentType.PARAGRAPH);
    String raw = text.strip();
    logger.debug("Parsing paragraph");
    Matcher matcher = SENTENCE_SPLIT.matcher(raw);
    boolean found = false;
    while (matcher.find()) {
      String sentenceText = matcher.group().strip();
      if (!sentenceText.isEmpty()) {
        found = true;
        if (next != null) {
          paragraph.add(next.parse(sentenceText));
        }
      }
    }
    if (!found && next != null) {
      paragraph.add(next.parse(raw));
    }
    return paragraph;
  }
}
