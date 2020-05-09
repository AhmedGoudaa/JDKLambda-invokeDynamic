package org.gouda;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Rules {

  @Singular
  List<Rule> rules;
}
