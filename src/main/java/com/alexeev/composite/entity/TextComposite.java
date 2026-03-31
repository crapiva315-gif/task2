package com.alexeev.composite.entity;

import java.util.ArrayList;
import java.util.List;

public class TextComposite implements TextComponent {

  private final ComponentType type;
  private final List<TextComponent> children;

  public TextComposite(ComponentType type) {
    this.type = type;
    this.children = new ArrayList<>();
  }

  @Override
  public ComponentType getType() {
    return type;
  }

  @Override
  public void add(TextComponent component) {
    children.add(component);
  }

  @Override
  public List<TextComponent> getChildren() {
    return children;
  }

  @Override
  public String restore() {
    StringBuilder sb = new StringBuilder();
    switch (type) {
      case TEXT -> {
        for (int i = 0; i < children.size(); i++) {
          sb.append(children.get(i).restore());
          if (i < children.size() - 1) {
            sb.append("\n");
          }
        }
      }
      case PARAGRAPH -> {
        for (int i = 0; i < children.size(); i++) {
          sb.append(children.get(i).restore());
          if (i < children.size() - 1) {
            sb.append(" ");
          }
        }
      }
      case SENTENCE -> {
        for (int i = 0; i < children.size(); i++) {
          sb.append(children.get(i).restore());
          if (i < children.size() - 1) {
            sb.append(" ");
          }
        }
      }
      case LEXEME -> {
        for (TextComponent child : children) {
          sb.append(child.restore());
        }
      }
      default -> {
        for (TextComponent child : children) {
          sb.append(child.restore());
        }
      }
    }
    return sb.toString();
  }

  @Override
  public String toString() {
    return restore();
  }
}
