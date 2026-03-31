package com.alexeev.composite.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TextFileReader {

  private static final Logger logger = LoggerFactory.getLogger(TextFileReader.class);

  public String read(String filePath) throws IOException {
    Path path = Path.of(filePath);
    String content = Files.readString(path);
    logger.info("Read {} bytes from '{}'", content.length(), filePath);
    return content;
  }
}
