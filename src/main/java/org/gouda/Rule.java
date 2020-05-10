package org.gouda;

import lombok.Value;

import java.util.function.Function;

@Value
public class Rule {
  String name;
  String sourcePath;
  String destinationPath;

  /**
   * Used for different types mapping
   */
  Function mapperFunction;

  public Rule(String name, String sourcePath, String destinationPath, Function mapperFunction) {
    this.name            = name;
    this.sourcePath      = sourcePath;
    this.destinationPath = destinationPath;
    this.mapperFunction  = mapperFunction;
  }

  public Rule(String name, String sourcePath, String destinationPath) {
    this.name            = name;
    this.sourcePath      = sourcePath;
    this.destinationPath = destinationPath;
    this.mapperFunction = null;
  }
}
