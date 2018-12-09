package com.github.agmcc.slate.ast;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PositionTest {

  @Test
  void testCompareTo_equal() {
    // Given
    final var a = Position.of(0, 0, 0, 0);
    final var b = Position.of(0, 0, 0, 0);

    // When
    final var actual = a.compareTo(b);

    // Then
    assertEquals(0, actual);
  }

  @Test
  void testCompareTo_greater() {
    // Given
    final var a = Position.of(0, 1, 0, 0);
    final var b = Position.of(0, 0, 0, 0);

    // When
    final var actual = a.compareTo(b);

    // Then
    assertEquals(1, actual);
  }

  @Test
  void testCompareTo_less() {
    // Given
    final var a = Position.of(0, 0, 0, 0);
    final var b = Position.of(0, 1, 0, 0);

    // When
    final var actual = a.compareTo(b);

    // Then
    assertEquals(-1, actual);
  }
}
