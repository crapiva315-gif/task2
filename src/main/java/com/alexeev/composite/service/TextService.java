package com.alexeev.composite.service;

import com.alexeev.composite.entity.ComponentType;
import com.alexeev.composite.entity.TextComponent;
import com.alexeev.composite.entity.TextComposite;
import com.alexeev.composite.entity.TextLeaf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TextService {

  private static final Logger logger = LoggerFactory.getLogger(TextService.class);

  public Map<String, Long> countLettersAndSymbols(TextComponent text) {
    long letters = 0;
    long symbols = 0;
    for (TextLeaf leaf : collectLeaves(text)) {
      char c = leaf.getValue();
      symbols++;
      if (Character.isLetter(c)) {
        letters++;
      }
    }
    logger.info("Letters: {}, Symbols: {}", letters, symbols);
    Map<String, Long> result = new HashMap<>();
    result.put("letters", letters);
    result.put("symbols", symbols);
    return result;
  }

  public int findMaxSentencesWithCommonWord(TextComponent text) {
    List<TextComponent> sentences = collectByType(text, ComponentType.SENTENCE);
    Map<String, List<Integer>> wordToSentences = new HashMap<>();

    for (int i = 0; i < sentences.size(); i++) {
      List<String> words = extractWords(sentences.get(i));
      for (String word : words) {
        String lower = word.toLowerCase();
        wordToSentences.computeIfAbsent(lower, k -> new ArrayList<>()).add(i);
      }
    }

    int max = 0;
    for (Map.Entry<String, List<Integer>> entry : wordToSentences.entrySet()) {
      long distinct = entry.getValue().stream().distinct().count();
      if (distinct > max) {
        max = (int) distinct;
      }
    }
    logger.info("Max sentences sharing a common word: {}", max);
    return max;
  }


  public List<TextComponent> sortSentencesByLexemeCount(TextComponent text) {
    List<TextComponent> sentences = collectByType(text, ComponentType.SENTENCE);
    sentences.sort(Comparator.comparingInt(s -> s.getChildren().size()));
    logger.info("Sorted {} sentences by lexeme count", sentences.size());
    return sentences;
  }


  public TextComponent swapFirstAndLastLexeme(TextComponent text) {
    List<TextComponent> sentences = collectByType(text, ComponentType.SENTENCE);
    for (TextComponent sentence : sentences) {
      List<TextComponent> lexemes = sentence.getChildren();
      int size = lexemes.size();
      if (size >= 2) {
        TextComponent first = lexemes.get(0);
        TextComponent last = lexemes.get(size - 1);

        String firstStr = first.restore();
        String lastStr = last.restore();

        String newFirst = adjustLength(lastStr, firstStr.length());
        String newLast = adjustLength(firstStr, lastStr.length());

        replaceLeaves((TextComposite) first, newFirst);
        replaceLeaves((TextComposite) last, newLast);

        logger.debug("Swapped lexemes: '{}' <-> '{}'", firstStr, lastStr);
      }
    }
    return text;
  }

  private List<TextLeaf> collectLeaves(TextComponent component) {
    List<TextLeaf> leaves = new ArrayList<>();
    gatherLeaves(component, leaves);
    return leaves;
  }

  private void gatherLeaves(TextComponent component, List<TextLeaf> acc) {
    if (component instanceof TextLeaf leaf) {
      acc.add(leaf);
    } else {
      for (TextComponent child : component.getChildren()) {
        gatherLeaves(child, acc);
      }
    }
  }

  private List<TextComponent> collectByType(TextComponent root, ComponentType type) {
    List<TextComponent> result = new ArrayList<>();
    gatherByType(root, type, result);
    return result;
  }

  private void gatherByType(TextComponent component, ComponentType type, List<TextComponent> acc) {
    if (component.getType() == type) {
      acc.add(component);
      return;
    }
    for (TextComponent child : component.getChildren()) {
      gatherByType(child, type, acc);
    }
  }

  private List<String> extractWords(TextComponent sentence) {
    List<String> words = new ArrayList<>();
    for (TextComponent lexeme : sentence.getChildren()) {
      StringBuilder sb = new StringBuilder();
      for (TextLeaf leaf : collectLeaves(lexeme)) {
        if (Character.isLetter(leaf.getValue())) {
          sb.append(leaf.getValue());
        }
      }
      if (!sb.isEmpty()) {
        words.add(sb.toString());
      }
    }
    return words;
  }

  private String adjustLength(String source, int targetLength) {
    if (source.length() == targetLength) {
      return source;
    }
    if (source.length() > targetLength) {
      return source.substring(0, targetLength);
    }
    return source + " ".repeat(targetLength - source.length());
  }

  private void replaceLeaves(TextComposite lexeme, String newContent) {
    lexeme.getChildren().clear();
    for (int i = 0; i < newContent.length(); i++) {
      lexeme.add(new TextLeaf(newContent.charAt(i), ComponentType.SYMBOL));
    }
  }
}
