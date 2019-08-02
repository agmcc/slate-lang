package com.github.agmcc.slate.parser.ast;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ClassModifier {
  PUBLIC("pub");

  private static final Map<String, ClassModifier> MAP = new HashMap<>();

  static {
    Arrays.stream(ClassModifier.values()).forEach(v -> MAP.put(v.text, v));
  }

  private String text;

  public static ClassModifier fromText(final String text) {
    return MAP.get(text);
  }
}
