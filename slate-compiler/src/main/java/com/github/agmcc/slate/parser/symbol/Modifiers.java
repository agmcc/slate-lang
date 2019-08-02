package com.github.agmcc.slate.parser.symbol;

/**
 * Modifiers for slate language.
 *
 * @see java.lang.reflect.Modifier
 */
public final class Modifiers {

  // Java compatible
  public static final int PUBLIC = 0x00000001;

  public static final int PRIVATE = 0x00000002;

  public static final int STATIC = 0x00000008;

  public static final int FINAL = 0x00000010;

  // Slate specific
  public static final int CONSTANT = 0x00010000;

  private Modifiers() {
    /* Static access */
  }
}
