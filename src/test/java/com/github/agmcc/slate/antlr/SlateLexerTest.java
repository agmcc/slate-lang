package com.github.agmcc.slate.antlr;

import static com.github.agmcc.slate.antlr.SlateLexer.ADD;
import static com.github.agmcc.slate.antlr.SlateLexer.AND;
import static com.github.agmcc.slate.antlr.SlateLexer.ASSIGN;
import static com.github.agmcc.slate.antlr.SlateLexer.DECREMENT;
import static com.github.agmcc.slate.antlr.SlateLexer.DEC_LIT;
import static com.github.agmcc.slate.antlr.SlateLexer.DIV;
import static com.github.agmcc.slate.antlr.SlateLexer.ELSE;
import static com.github.agmcc.slate.antlr.SlateLexer.EQUAL;
import static com.github.agmcc.slate.antlr.SlateLexer.FALSE_LIT;
import static com.github.agmcc.slate.antlr.SlateLexer.FOR;
import static com.github.agmcc.slate.antlr.SlateLexer.GREATER;
import static com.github.agmcc.slate.antlr.SlateLexer.GREATER_EQ;
import static com.github.agmcc.slate.antlr.SlateLexer.HIDDEN;
import static com.github.agmcc.slate.antlr.SlateLexer.ID;
import static com.github.agmcc.slate.antlr.SlateLexer.IF;
import static com.github.agmcc.slate.antlr.SlateLexer.INCREMENT;
import static com.github.agmcc.slate.antlr.SlateLexer.INT_LIT;
import static com.github.agmcc.slate.antlr.SlateLexer.LESS;
import static com.github.agmcc.slate.antlr.SlateLexer.LESS_EQ;
import static com.github.agmcc.slate.antlr.SlateLexer.L_BRACE;
import static com.github.agmcc.slate.antlr.SlateLexer.L_PAREN;
import static com.github.agmcc.slate.antlr.SlateLexer.MUL;
import static com.github.agmcc.slate.antlr.SlateLexer.NOT_EQUAL;
import static com.github.agmcc.slate.antlr.SlateLexer.OR;
import static com.github.agmcc.slate.antlr.SlateLexer.R_BRACE;
import static com.github.agmcc.slate.antlr.SlateLexer.R_PAREN;
import static com.github.agmcc.slate.antlr.SlateLexer.STRING_LIT;
import static com.github.agmcc.slate.antlr.SlateLexer.SUB;
import static com.github.agmcc.slate.antlr.SlateLexer.TRUE_LIT;
import static com.github.agmcc.slate.antlr.SlateLexer.VAR;
import static com.github.agmcc.slate.antlr.SlateLexer.WHILE;
import static com.github.agmcc.slate.antlr.SlateLexer.WS;
import static com.github.agmcc.slate.test.ANTLRUtils.getTokenTypes;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

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

  @ParameterizedTest
  @ValueSource(strings = {"1", "0", "1_000", "1_000_000"})
  void testIntLiterals(String src) {
    // Given // When
    final var actual = getTokenTypes(src);

    // Then
    assertEquals(List.of(INT_LIT), actual);
  }

  @ParameterizedTest
  @ValueSource(strings = {"1.0", "0.0", "1_000.00", "1000.00_00"})
  void testDecimalLiteral(String src) {
    // Given // When
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

  @Test
  void testBraces() {
    // Given
    final var src = "{}";

    // When
    final var actual = getTokenTypes(src);

    // Then
    assertEquals(List.of(L_BRACE, R_BRACE), actual);
  }

  /* Logical operators */

  @Test
  void testLess() {
    // Given
    final var src = "<";

    // When
    final var actual = getTokenTypes(src);

    // Then
    assertEquals(List.of(LESS), actual);
  }

  @Test
  void testLessEqual() {
    // Given
    final var src = "<=";

    // When
    final var actual = getTokenTypes(src);

    // Then
    assertEquals(List.of(LESS_EQ), actual);
  }

  @Test
  void testGreater() {
    // Given
    final var src = ">";

    // When
    final var actual = getTokenTypes(src);

    // Then
    assertEquals(List.of(GREATER), actual);
  }

  @Test
  void testGreaterEqual() {
    // Given
    final var src = ">=";

    // When
    final var actual = getTokenTypes(src);

    // Then
    assertEquals(List.of(GREATER_EQ), actual);
  }

  @Test
  void testEqual() {
    // Given
    final var src = "==";

    // When
    final var actual = getTokenTypes(src);

    // Then
    assertEquals(List.of(EQUAL), actual);
  }

  @Test
  void testNotEqual() {
    // Given
    final var src = "!=";

    // When
    final var actual = getTokenTypes(src);

    // Then
    assertEquals(List.of(NOT_EQUAL), actual);
  }

  @Test
  void testAnd() {
    // Given
    final var src = "&&";

    // When
    final var actual = getTokenTypes(src);

    // Then
    assertEquals(List.of(AND), actual);
  }

  @Test
  void testOr() {
    // Given
    final var src = "||";

    // When
    final var actual = getTokenTypes(src);

    // Then
    assertEquals(List.of(OR), actual);
  }

  @Test
  void testIf() {
    // Given
    final var src = "if";

    // When
    final var actual = getTokenTypes(src);

    // Then
    assertEquals(List.of(IF), actual);
  }

  @Test
  void testElse() {
    // Given
    final var src = "else";

    // When
    final var actual = getTokenTypes(src);

    // Then
    assertEquals(List.of(ELSE), actual);
  }

  @Test
  void testBooleanLiterals() {
    // Given
    final var src = "true false";

    // When
    final var actual = getTokenTypes(src);

    // Then
    assertEquals(List.of(TRUE_LIT, FALSE_LIT), actual);
  }

  @Test
  void testWhile() {
    // Given
    final var src = "while";

    // When
    final var actual = getTokenTypes(src);

    // Then
    assertEquals(List.of(WHILE), actual);
  }

  @Test
  void testFor() {
    // Given
    final var src = "for";

    // When
    final var actual = getTokenTypes(src);

    // Then
    assertEquals(List.of(FOR), actual);
  }

  @Test
  void testIncrement() {
    // Given
    final var src = "++";

    // When
    final var actual = getTokenTypes(src);

    // Then
    assertEquals(List.of(INCREMENT), actual);
  }

  @Test
  void testDecrement() {
    // Given
    final var src = "--";

    // When
    final var actual = getTokenTypes(src);

    // Then
    assertEquals(List.of(DECREMENT), actual);
  }
}
