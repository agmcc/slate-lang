package com.github.agmcc.slate.bytecode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.agmcc.slate.ast.CompilationUnit;
import com.github.agmcc.slate.ast.expression.BooleanLit;
import com.github.agmcc.slate.ast.expression.DecLit;
import com.github.agmcc.slate.ast.expression.IntLit;
import com.github.agmcc.slate.ast.expression.StringLit;
import com.github.agmcc.slate.ast.expression.VarReference;
import com.github.agmcc.slate.ast.expression.binary.AdditionExpression;
import com.github.agmcc.slate.ast.expression.binary.DivisionExpression;
import com.github.agmcc.slate.ast.expression.binary.MultiplicationExpression;
import com.github.agmcc.slate.ast.expression.binary.SubtractionExpression;
import com.github.agmcc.slate.ast.expression.binary.logic.AndExpression;
import com.github.agmcc.slate.ast.expression.binary.logic.GreaterExpression;
import com.github.agmcc.slate.ast.expression.binary.logic.OrExpression;
import com.github.agmcc.slate.ast.statement.Assignment;
import com.github.agmcc.slate.ast.statement.Block;
import com.github.agmcc.slate.ast.statement.Condition;
import com.github.agmcc.slate.ast.statement.Print;
import com.github.agmcc.slate.ast.statement.VarDeclaration;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class JvmCompilerTest {

  private static final String CLASS_NAME = "Test";

  private static JvmCompiler compiler;

  @BeforeAll
  static void setup() {
    compiler = new JvmCompiler();
  }

  /* Var Declaration */

  @Test
  void testCompile_varDeclaration_nullExpression() {
    // Given
    final var compilationUnit = new CompilationUnit(List.of(new VarDeclaration("count", null)));

    // When Then
    assertThrows(NoSuchElementException.class, () -> compiler.compile(compilationUnit, CLASS_NAME));
  }

  @ParameterizedTest
  @ValueSource(strings = {"5", "-5", "0146", "0X123Face", "0x123", "0b1111", "0B1111", "1_000_000"})
  void testCompile_varDeclaration_int_valid(String value) {
    // Given
    final var compilationUnit =
        new CompilationUnit(List.of(new VarDeclaration("count", new IntLit(value))));

    // When
    final var actual = compiler.compile(compilationUnit, CLASS_NAME);

    // Then
    assertNotNull(actual);
  }

  @ParameterizedTest
  @ValueSource(strings = {"abc", "15.5", "10L", "10f"})
  void testCompile_varDeclaration_int_invalidFormat(String value) {
    // Given
    final var compilationUnit =
        new CompilationUnit(List.of(new VarDeclaration("count", new IntLit(value))));

    // When Then
    assertThrows(NumberFormatException.class, () -> compiler.compile(compilationUnit, CLASS_NAME));
  }

  @Test
  void testCompile_varDeclaration_int_nullValue() {
    // Given
    final var compilationUnit =
        new CompilationUnit(List.of(new VarDeclaration("count", new IntLit(null))));

    // When Then
    assertThrows(NullPointerException.class, () -> compiler.compile(compilationUnit, CLASS_NAME));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "5",
        "-5",
        "5.0",
        "-5.0",
        "5.5",
        "5f",
        "-5f",
        "5.0f",
        "-5.0f",
        "1_000.00",
        "100.00_00"
      })
  void testCompile_varDeclaration_decimal_valid(String value) {
    // Given
    final var compilationUnit =
        new CompilationUnit(List.of(new VarDeclaration("price", new DecLit(value))));

    // When
    final var actual = compiler.compile(compilationUnit, CLASS_NAME);

    // Then
    assertNotNull(actual);
  }

  @ParameterizedTest
  @ValueSource(strings = {"abc", "15L", "0146.5", "0X123Face.123"})
  void testCompile_varDeclaration_decimal_invalidFormat(String value) {
    // Given
    final var compilationUnit =
        new CompilationUnit(List.of(new VarDeclaration("price", new IntLit(value))));

    // When Then
    assertThrows(NumberFormatException.class, () -> compiler.compile(compilationUnit, CLASS_NAME));
  }

  @Test
  void testCompile_varDeclaration_decimal_nullValue() {
    // Given
    final var compilationUnit =
        new CompilationUnit(List.of(new VarDeclaration("price", new DecLit(null))));

    // When Then
    assertThrows(NullPointerException.class, () -> compiler.compile(compilationUnit, CLASS_NAME));
  }

  @Test
  void testCompile_varDeclaration_string_valid() {
    // Given
    final var compilationUnit =
        new CompilationUnit(List.of(new VarDeclaration("message", new StringLit("Hello"))));

    // When
    final var actual = compiler.compile(compilationUnit, CLASS_NAME);

    // Then
    assertNotNull(actual);
  }

  @Test
  void testCompile_varDeclaration_string_nullValue() {
    // Given
    final var compilationUnit =
        new CompilationUnit(List.of(new VarDeclaration("message", new StringLit(null))));

    // When Then
    assertThrows(
        IllegalArgumentException.class, () -> compiler.compile(compilationUnit, CLASS_NAME));
  }

  @Test
  void testCompile_varDeclaration_addition() {
    // Given
    final var compilationUnit =
        new CompilationUnit(
            List.of(
                new VarDeclaration(
                    "result", new AdditionExpression(new IntLit("1"), new IntLit("3")))));

    // When
    final var actual = compiler.compile(compilationUnit, CLASS_NAME);

    // Then
    assertNotNull(actual);
  }

  @Test
  void testCompile_varDeclaration_addition_mixedTypes() {
    // Given
    final var compilationUnit =
        new CompilationUnit(
            List.of(
                new VarDeclaration(
                    "result", new AdditionExpression(new IntLit("1"), new DecLit("3.5")))));

    // When
    final var actual = compiler.compile(compilationUnit, CLASS_NAME);

    // Then
    assertNotNull(actual);
  }

  @Test
  void testCompile_varDeclaration_addition_string() {
    // Given
    final var compilationUnit =
        new CompilationUnit(
            List.of(
                new VarDeclaration(
                    "result",
                    new AdditionExpression(new StringLit("Hello, "), new StringLit("World!")))));

    // When
    final var actual = compiler.compile(compilationUnit, CLASS_NAME);

    // Then
    assertNotNull(actual);
  }

  @Test
  void testCompile_varDeclaration_subtraction() {
    // Given
    final var compilationUnit =
        new CompilationUnit(
            List.of(
                new VarDeclaration(
                    "result", new SubtractionExpression(new IntLit("10"), new IntLit("3")))));

    // When
    final var actual = compiler.compile(compilationUnit, CLASS_NAME);

    // Then
    assertNotNull(actual);
  }

  @Test
  void testCompile_varDeclaration_subtraction_mixedTypes() {
    // Given
    final var compilationUnit =
        new CompilationUnit(
            List.of(
                new VarDeclaration(
                    "result", new SubtractionExpression(new IntLit("10"), new DecLit("3")))));

    // When
    final var actual = compiler.compile(compilationUnit, CLASS_NAME);

    // Then
    assertNotNull(actual);
  }

  @Test
  void testCompile_varDeclaration_multiplication() {
    // Given
    final var compilationUnit =
        new CompilationUnit(
            List.of(
                new VarDeclaration(
                    "result", new MultiplicationExpression(new IntLit("10"), new IntLit("2")))));

    // When
    final var actual = compiler.compile(compilationUnit, CLASS_NAME);

    // Then
    assertNotNull(actual);
  }

  @Test
  void testCompile_varDeclaration_multiplication_mixedTypes() {
    // Given
    final var compilationUnit =
        new CompilationUnit(
            List.of(
                new VarDeclaration(
                    "result",
                    new MultiplicationExpression(new DecLit("10.0101"), new IntLit("2")))));

    // When
    final var actual = compiler.compile(compilationUnit, CLASS_NAME);

    // Then
    assertNotNull(actual);
  }

  @Test
  void testCompile_varDeclaration_division() {
    // Given
    final var compilationUnit =
        new CompilationUnit(
            List.of(
                new VarDeclaration(
                    "result", new DivisionExpression(new IntLit("10"), new IntLit("2")))));

    // When
    final var actual = compiler.compile(compilationUnit, CLASS_NAME);

    // Then
    assertNotNull(actual);
  }

  @Test
  void testCompile_varDeclaration_division_mixedTypes() {
    // Given
    final var compilationUnit =
        new CompilationUnit(
            List.of(
                new VarDeclaration(
                    "result", new DivisionExpression(new IntLit("10"), new DecLit("0.5")))));

    // When
    final var actual = compiler.compile(compilationUnit, CLASS_NAME);

    // Then
    assertNotNull(actual);
  }

  /* Assignment */

  // TODO: Assignment null expression

  @Test
  void testCompile_assignment_int_valid() {
    // Given
    final var compilationUnit =
        new CompilationUnit(
            List.of(
                new VarDeclaration("value", new IntLit("0")),
                new Assignment("value", new IntLit("1"))));

    // When
    final var actual = compiler.compile(compilationUnit, CLASS_NAME);

    // Then
    assertNotNull(actual);
  }

  @Test
  void testCompile_assignment_int_convertType() {
    // Given
    final var compilationUnit =
        new CompilationUnit(
            List.of(
                new VarDeclaration("value", new DecLit("1.5")),
                new Assignment("value", new IntLit("1"))));

    // When
    final var actual = compiler.compile(compilationUnit, CLASS_NAME);

    // Then
    assertNotNull(actual);
  }

  @Test
  void testCompile_assignment_missingVarName() {
    // Given
    final var compilationUnit =
        new CompilationUnit(List.of(new Assignment("value", new IntLit("1"))));

    // When Then
    assertThrows(NullPointerException.class, () -> compiler.compile(compilationUnit, CLASS_NAME));
  }

  @Test
  void testCompile_assignment_validVarRef() {
    // Given
    final var compilationUnit =
        new CompilationUnit(
            List.of(
                new VarDeclaration("a", new StringLit("Hello")),
                new VarDeclaration("b", new StringLit("Goodbye")),
                new Assignment("a", new VarReference("b"))));

    // When
    final var actual = compiler.compile(compilationUnit, CLASS_NAME);

    // Then
    assertNotNull(actual);
  }

  @Test
  void testCompile_assignment_invalidVarRef() {
    // Given
    final var compilationUnit =
        new CompilationUnit(
            List.of(
                new VarDeclaration("a", new StringLit("Hello")),
                new Assignment("a", new VarReference("unknown"))));

    // When Then
    final var e =
        assertThrows(RuntimeException.class, () -> compiler.compile(compilationUnit, CLASS_NAME));

    assertEquals("Missing variable: unknown", e.getMessage());
  }

  @Test
  void testCompile_assignment_invalidConversion() {
    // Given
    final var compilationUnit =
        new CompilationUnit(
            List.of(
                new VarDeclaration("value", new StringLit("Hello")),
                new Assignment("value", new IntLit("1"))));

    // When Then
    final var e =
        assertThrows(
            UnsupportedOperationException.class,
            () -> compiler.compile(compilationUnit, CLASS_NAME));

    assertEquals("Unable to convert type I to java/lang/String", e.getMessage());
  }

  /* Print */

  // TODO: Print null expression

  @Test
  void testCompile_print_int_valid() {
    // Given
    final var compilationUnit = new CompilationUnit(List.of(new Print(new IntLit("10"))));

    // When
    final var actual = compiler.compile(compilationUnit, CLASS_NAME);

    // Then
    assertNotNull(actual);
  }

  /* Block */

  @Test
  void testCompile_block() {
    // Given
    final var compilationUnit =
        new CompilationUnit(
            List.of(
                new VarDeclaration("outer", new IntLit("3")),
                new Block(List.of(new VarDeclaration("inner1", new IntLit("5")))),
                new Block(List.of(new VarDeclaration("inner2", new DecLit("2.5"))))));

    // When
    final var actual = compiler.compile(compilationUnit, CLASS_NAME);

    // Then
    assertNotNull(actual);
  }

  /* Booleans */

  @Test
  void testCompile_boolean_true() {
    // Given
    final var compilationUnit =
        new CompilationUnit(List.of(new VarDeclaration("valid", new BooleanLit("true"))));

    // When
    final var actual = compiler.compile(compilationUnit, CLASS_NAME);

    // Then
    assertNotNull(actual);
  }

  @Test
  void testCompile_boolean_false() {
    // Given
    final var compilationUnit =
        new CompilationUnit(List.of(new VarDeclaration("valid", new BooleanLit("false"))));

    // When
    final var actual = compiler.compile(compilationUnit, CLASS_NAME);

    // Then
    assertNotNull(actual);
  }

  @Test
  void testCompile_boolean_invalid() {
    // Given
    final var compilationUnit =
        new CompilationUnit(List.of(new VarDeclaration("valid", new BooleanLit("invalid"))));

    // When Then
    final var e =
        assertThrows(
            UnsupportedOperationException.class,
            () -> compiler.compile(compilationUnit, CLASS_NAME));

    assertEquals("Invalid boolean value: invalid", e.getMessage());
  }

  @Test
  void testCompile_varDeclaration_logicExpression() {
    // Given
    final var compilationUnit =
        new CompilationUnit(
            List.of(
                new VarDeclaration(
                    "valid", new GreaterExpression(new IntLit("5"), new IntLit("3")))));

    // When
    final var actual = compiler.compile(compilationUnit, CLASS_NAME);

    // Then
    assertNotNull(actual);
  }

  @Test
  void testCompile_logicalAnd_booleanLiterals() {
    // Given
    final var compilationUnit =
        new CompilationUnit(
            List.of(
                new Condition(
                    new AndExpression(new BooleanLit("true"), new BooleanLit("true")),
                    new Print(new StringLit("msg")))));

    // When
    final var actual = compiler.compile(compilationUnit, CLASS_NAME);

    // Then
    assertNotNull(actual);
  }

  @Test
  void testCompile_logicalAnd_boolean_logical() {
    // Given
    final var compilationUnit =
        new CompilationUnit(
            List.of(
                new Condition(
                    new AndExpression(
                        new BooleanLit("true"),
                        new GreaterExpression(new IntLit("5"), new IntLit("3"))),
                    new Print(new StringLit("msg")))));

    // When
    final var actual = compiler.compile(compilationUnit, CLASS_NAME);

    // Then
    assertNotNull(actual);
  }

  @Test
  void testCompile_logicalAnd_varRef() {
    // Given
    final var compilationUnit =
        new CompilationUnit(
            List.of(
                new VarDeclaration("check", new BooleanLit("true")),
                new Condition(
                    new AndExpression(new BooleanLit("true"), new VarReference("check")),
                    new Print(new StringLit("msg")))));

    // When
    final var actual = compiler.compile(compilationUnit, CLASS_NAME);

    // Then
    assertNotNull(actual);
  }

  @Test
  void testCompile_logicalOr_boolean() {
    // Given
    final var compilationUnit =
        new CompilationUnit(
            List.of(
                new Condition(
                    new OrExpression(new BooleanLit("true"), new BooleanLit("false")),
                    new Print(new StringLit("msg")))));

    // When
    final var actual = compiler.compile(compilationUnit, CLASS_NAME);

    // Then
    assertNotNull(actual);
  }
}
