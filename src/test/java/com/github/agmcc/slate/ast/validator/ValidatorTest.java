package com.github.agmcc.slate.ast.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.agmcc.slate.ast.CompilationUnit;
import com.github.agmcc.slate.ast.Point;
import com.github.agmcc.slate.ast.Position;
import com.github.agmcc.slate.ast.expression.StringLit;
import com.github.agmcc.slate.ast.expression.VarReference;
import com.github.agmcc.slate.ast.statement.Assignment;
import com.github.agmcc.slate.ast.statement.Print;
import com.github.agmcc.slate.ast.statement.VarDeclaration;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ValidatorTest {

  private static Validator validator;

  @BeforeAll
  static void setup() {
    validator = new Validator();
  }

  @Test
  void testValidate_duplicateVar() {
    // Given
    final var compilationUnit =
        new CompilationUnit(
            List.of(
                new VarDeclaration("animal", new StringLit("Dog"), Position.of(1, 0, 1, 18)),
                new VarDeclaration("animal", new StringLit("Cat"), Position.of(2, 0, 2, 18))));

    final var expected =
        List.of(new Error("Variable 'animal' already declared at [1,0]", new Point(2, 0)));

    // When
    final var actual = validator.validate(compilationUnit);

    // Then
    assertEquals(expected, actual);
  }

  @Test
  void testValidate_missingVarRef() {
    // Given
    final var compilationUnit =
        new CompilationUnit(
            List.of(new Print(new VarReference("message", Position.of(1, 6, 1, 13)))));

    final var expected =
        List.of(new Error("No variable named 'message' at [1,6]", new Point(1, 6)));

    // When
    final var actual = validator.validate(compilationUnit);

    // Then
    assertEquals(expected, actual);
  }

  @Test
  void testValidate_assignment_missingVar() {
    // Given
    final var compilationUnit =
        new CompilationUnit(
            List.of(
                new Assignment(
                    "message", new StringLit("Hello, World!"), Position.of(1, 0, 1, 25))));

    final var expected =
        List.of(new Error("No variable named 'message' at [1,0]", new Point(1, 0)));

    // When
    final var actual = validator.validate(compilationUnit);

    // Then
    assertEquals(expected, actual);
  }
}
