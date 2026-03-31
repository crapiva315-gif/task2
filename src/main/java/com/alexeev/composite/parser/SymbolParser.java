package com.alexeev.composite.parser;

import com.alexeev.composite.entity.ComponentType;
import com.alexeev.composite.entity.TextComponent;
import com.alexeev.composite.entity.TextLeaf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SymbolParser extends AbstractParser {

  private static final Logger logger = LoggerFactory.getLogger(SymbolParser.class);

  @Override
  public TextComponent parse(String text) {
    char ch = text.charAt(0);
    logger.debug("Parsing symbol: '{}'", ch);
    return new TextLeaf(ch, ComponentType.SYMBOL);
  }
}
