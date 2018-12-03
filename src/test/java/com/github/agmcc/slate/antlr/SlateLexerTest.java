package com.github.agmcc.slate.antlr;

import static com.github.agmcc.slate.antlr.SlateLexer.ADD;
import static com.github.agmcc.slate.antlr.SlateLexer.ASSIGN;
import static com.github.agmcc.slate.antlr.SlateLexer.DEC_LIT;
import static com.github.agmcc.slate.antlr.SlateLexer.DIV;
import static com.github.agmcc.slate.antlr.SlateLexer.HIDDEN;
import static com.github.agmcc.slate.antlr.SlateLexer.ID;
import static com.github.agmcc.slate.antlr.SlateLexer.INT_LIT;
import static com.github.agmcc.slate.antlr.SlateLexer.L_PAREN;
import static com.github.agmcc.slate.antlr.SlateLexer.MUL;
import static com.github.agmcc.slate.antlr.SlateLexer.R_PAREN;
import static com.github.agmcc.slate.antlr.SlateLexer.STRING_LIT;
import static com.github.agmcc.slate.antlr.SlateLexer.SUB;
import static com.github.agmcc.slate.antlr.SlateLexer.VAR;
import static com.github.agmcc.slate.antlr.SlateLexer.WS;
import static com.github.agmcc.slate.test.ANTLRUtils.getTokenTypes;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class SlateLexerTest {

  @Test
  void testWhitespace() {
    // Given
    final var src = "  \t\n\r";

    // When
    final var actual = getTokenTypes(src);

    // Then
    assertEquals(Collections.emptyList(), actual);
  }

  @Test
  void testWhitespaceInHiddenChannel() {
    // Given
    final var src = "  \t\n\r";

    // When
    final var actual = getTokenTypes(src, HIDDEN);

    // Then
    assertEquals(List.of(WS), actual);
  }

  @Test
  void testVar() {
    // Given
    final var src = "var";

    // When
    final var actual = getTokenTypes(src);

    // Then
    assertEquals(List.of(VAR), actual);
  }

  @Test
  void testIntLiteral() {
    // Given
    final var src = "1";

    // When
    final var actual = getTokenTypes(src);

    // Then
    assertEquals(List.of(INT_LIT), actual);
  }

  @Test
  void testDecimalLiteral() {
    // Given
    final var src = "1.0";

    // When
    final var actual = getTokenTypes(src);

    // Then
    assertEquals(List.of(DEC_LIT), actual);
  }

  @Test
  void testStringLiterals() {
    final var strings = List.of("''", "' '", "'Hello'", "'Hello world'", "'123'");

    strings.forEach(s -> assertEquals(List.of(STRING_LIT), getTokenTypes(s)));
  }

  @Test
  void testAdd() {
    // Given
    final var src = "+";

    // When
    final var actual = getTokenTypes(src);

    // Then
    assertEquals(List.of(ADD), actual);
  }

  @Test
  void testSub() {
    // Given
    final var src = "-";

    // When
    final var actual = getTokenTypes(src);

    // Then
    assertEquals(List.of(SUB), actual);
  }

  @Test
  void testMul() {
    // Given
    final var src = "*";

    // When
    final var actual = getTokenTypes(src);

    // Then
    assertEquals(List.of(MUL), actual);
  }

  @Test
  void testDiv() {
    // Given
    final var src = "/";

    // When
    final var actual = getTokenTypes(src);

    // Then
    assertEquals(List.of(DIV), actual);
  }

  @Test
  void testAssign() {
    // Given
    final var src = "=";

    // When
    final var actual = getTokenTypes(src);

    // Then
    assertEquals(List.of(ASSIGN), actual);
  }

  @Test
  void testParenthesis() {
    // Given
    final var src = "()";

    // When
    final var actual = getTokenTypes(src);

    // Then
    assertEquals(List.of(L_PAREN, R_PAREN), actual);
  }

  @ParameterizedTest
  @CsvSource({"apple", "_apple", "apple1", "aPPLE", "apple_", "app_le", "aP1_e32_"})
  void testIdentifiers(String src) {
    // Act
    final var actual = getTokenTypes(src);

    // Assert
    assertEquals(List.of(ID), actual);
  }
}
