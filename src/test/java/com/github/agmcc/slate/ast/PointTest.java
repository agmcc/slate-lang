package com.github.agmcc.slate.ast;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PointTest {

  static Stream<Arguments> pointsProvider() {
    return Stream.of(
        // Equal
        arguments(0, 0, 0, 0, 0),
        // Greater
        arguments(0, 1, 0, 0, 1),
        arguments(1, 0, 0, 0, 1),
        arguments(1, 1, 0, 0, 1),
        // Less
        arguments(0, 0, 0, 1, -1),
        arguments(0, 0, 1, 0, -1),
        arguments(0, 0, 1, 1, -1));
  }

  @ParameterizedTest
  @MethodSource("pointsProvider")
  void testCompareTo(int line1, int col1, int line2, int col2, int expected) {
    // Given
    final var a = new Point(line1, col1);
    final var b = new Point(line2, col2);

    // When
    final var actual = a.compareTo(b);

    // Then
    assertEquals(expected, actual);
  }
}
