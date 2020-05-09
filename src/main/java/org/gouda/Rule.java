package org.gouda;

import lombok.Value;

@Value
public class Rule {
  String name;
  String sourcePath;
  String destinationPath;
}
