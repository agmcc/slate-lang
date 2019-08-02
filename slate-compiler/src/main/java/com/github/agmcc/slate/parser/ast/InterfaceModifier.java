package com.github.agmcc.slate.parser.ast;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum InterfaceModifier {
  PUBLIC("pub");

  private static final Map<String, InterfaceModifier> MAP = new HashMap<>();

  static {
    Arrays.stream(InterfaceModifier.values()).forEach(v -> MAP.put(v.text, v));
  }

  private String text;

  public static InterfaceModifier fromText(final String text) {
    return MAP.get(text);
  }
}
