package com.alexeev.composite.parser;

import com.alexeev.composite.entity.TextComponent;

public abstract class AbstractParser {

  protected AbstractParser next;

  public AbstractParser setNext(AbstractParser next) {
    this.next = next;
    return next;
  }

  public abstract TextComponent parse(String text);
}
