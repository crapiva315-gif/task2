package com.alexeev.composite.entity;

import java.util.Collections;
import java.util.List;

public class TextLeaf implements TextComponent {

  private final char value;
  private final ComponentType type;

  public TextLeaf(char value, ComponentType type) {
    this.value = value;
    this.type = type;
  }

  @Override
  public ComponentType getType() {
    return type;
  }

  @Override
  public void add(TextComponent component) {

  }

  @Override
  public List<TextComponent> getChildren() {
    return Collections.emptyList();
  }

  @Override
  public String restore() {
    return String.valueOf(value);
  }

  public char getValue() {
    return value;
  }

  @Override
  public String toString() {
    return restore();
  }
}
