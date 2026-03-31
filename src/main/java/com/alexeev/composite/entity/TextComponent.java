package com.alexeev.composite.entity;

import java.util.List;

public interface TextComponent {

  ComponentType getType();

  void add(TextComponent component);

  List<TextComponent> getChildren();

  String restore();
}
