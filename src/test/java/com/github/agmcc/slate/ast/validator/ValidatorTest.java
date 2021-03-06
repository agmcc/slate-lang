package com.github.agmcc.slate.ast.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.agmcc.slate.ast.CompilationUnit;
import com.github.agmcc.slate.ast.MethodDeclaration;
import com.github.agmcc.slate.ast.Point;
import com.github.agmcc.slate.ast.Position;
import com.github.agmcc.slate.ast.expression.BooleanLit;
import com.github.agmcc.slate.ast.expression.IntLit;
import com.github.agmcc.slate.ast.expression.PostIncrement;
import com.github.agmcc.slate.ast.expression.StringLit;
import com.github.agmcc.slate.ast.expression.VarReference;
import com.github.agmcc.slate.ast.expression.binary.logic.GreaterExpression;
import com.github.agmcc.slate.ast.statement.Assignment;
import com.github.agmcc.slate.ast.statement.Block;
import com.github.agmcc.slate.ast.statement.Condition;
import com.github.agmcc.slate.ast.statement.Print;
import com.github.agmcc.slate.ast.statement.Statement;
import com.github.agmcc.slate.ast.statement.VarDeclaration;
import com.github.agmcc.slate.ast.statement.While;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.Type;

@Disabled
class ValidatorTest {

  private static Validator validator;

  @BeforeAll
  static void setup() {
    validator = new Validator();
  }

  @Test
  void testValidate_assignment_blockValid() {
    // Given
    final var compilationUnit =
        createCompilationUnit(
            List.of(
                new VarDeclaration("outer", new IntLit("5"), Position.of(1, 1, 1, 15)),
                new Block(
                    List.of(new Assignment("outer", new IntLit("10"), Position.of(3, 4, 3, 10))))));

    final var expected = Collections.emptyList();

    // When
    final var actual = validator.validate(compilationUnit);

    // Then
    assertEquals(expected, actual);
  }

  @Test
  void testValidate_assignment_blockInvalid() {
    // Given
    final var compilationUnit =
        createCompilationUnit(
            List.of(
                new Block(
                    List.of(
                        new VarDeclaration("inner", new IntLit("5"), Position.of(2, 4, 2, 10)))),
                new Assignment("inner", new IntLit("10"), Position.of(4, 1, 4, 15))));

    final var expected = List.of(new Error("No variable named 'inner' at [4,1]", new Point(4, 1)));

    // When
    final var actual = validator.validate(compilationUnit);

    // Then
    assertEquals(expected, actual);
  }

  @Test
  void testValidate_varDeclaration_nestedBlocksValid() {
    // Given
    final var compilationUnit =
        createCompilationUnit(
            List.of(
                new Block(
                    List.of(
                        new Block(
                            List.of(
                                new VarDeclaration(
                                    "inner", new IntLit("5"), Position.of(2, 4, 2, 10))))))));

    final var expected = Collections.emptyList();

    // When
    final var actual = validator.validate(compilationUnit);

    // Then
    assertEquals(expected, actual);
  }

  @Test
  void testValidate_print_nestedBlocksValid() {
    // Given
    final var compilationUnit =
        createCompilationUnit(
            List.of(
                new VarDeclaration("x", new IntLit("1")),
                new Block(
                    List.of(
                        new VarDeclaration("y", new IntLit("2")),
                        new Block(
                            List.of(
                                new VarDeclaration("z", new IntLit("3")),
                                new Print(new VarReference("x")),
                                new Print(new VarReference("y")),
                                new Print(new VarReference("z"))))))));

    final var expected = Collections.emptyList();

    // When
    final var actual = validator.validate(compilationUnit);

    // Then
    assertEquals(expected, actual);
  }

  @Test
  void testValidate_print_nestedBlocksInvalid() {
    // Given
    final var compilationUnit =
        createCompilationUnit(
            List.of(
                new Block(
                    List.of(
                        new Print(new VarReference("x", Position.of(2, 4, 2, 10))),
                        new Block(List.of(new VarDeclaration("x", new IntLit("1"))))))));

    final var expected = List.of(new Error("No variable named 'x' at [2,4]", new Point(2, 4)));

    // When
    final var actual = validator.validate(compilationUnit);

    // Then
    assertEquals(expected, actual);
  }

  @Test
  void testValidate_condition() {
    // Given
    final var compilationUnit =
        createCompilationUnit(
            List.of(
                new Condition(
                    new BooleanLit("true", Position.of(1, 3, 1, 7)),
                    new Print(new StringLit("TRUE", Position.of(1, 8, 1, 13))),
                    null,
                    Position.of(1, 1, 1, 13))));

    final var expected = Collections.emptyList();

    // When
    final var actual = validator.validate(compilationUnit);

    // Then
    assertEquals(expected, actual);
  }

  @Test
  void testValidate_condition_invalidExpression() {
    // Given
    final var compilationUnit =
        createCompilationUnit(
            List.of(
                new Condition(
                    new IntLit("1", Position.of(1, 3, 1, 4)),
                    new Print(new StringLit("TRUE", Position.of(1, 8, 1, 13))),
                    null,
                    Position.of(1, 1, 1, 13))));

    final var expected =
        List.of(new Error("Condition must be a boolean expression at [1,3]", new Point(1, 3)));

    // When
    final var actual = validator.validate(compilationUnit);

    // Then
    assertEquals(expected, actual);
  }

  @Test
  void testValidate_condition_varRef() {
    // Given
    final var compilationUnit =
        createCompilationUnit(
            List.of(
                new VarDeclaration("check", new BooleanLit("true"), Position.of(1, 1, 1, 6)),
                new Condition(
                    new VarReference("check", Position.of(2, 3, 1, 4)),
                    new Print(new StringLit("TRUE", Position.of(2, 8, 1, 13))),
                    null,
                    Position.of(2, 1, 1, 13))));

    final var expected = Collections.emptyList();

    // When
    final var actual = validator.validate(compilationUnit);

    // Then
    assertEquals(expected, actual);
  }

  @Test
  void testValidate_condition_invalidVarRef() {
    // Given
    final var compilationUnit =
        createCompilationUnit(
            List.of(
                new VarDeclaration("check", new IntLit("1"), Position.of(1, 1, 1, 6)),
                new Condition(
                    new VarReference("check", Position.of(2, 3, 1, 4)),
                    new Print(new StringLit("TRUE", Position.of(2, 8, 1, 13))),
                    null,
                    Position.of(2, 1, 1, 13))));

    final var expected =
        List.of(new Error("Condition must be a boolean expression at [2,3]", new Point(2, 3)));

    // When
    final var actual = validator.validate(compilationUnit);

    // Then
    assertEquals(expected, actual);
  }

  @Test
  void testValidate_condition_logic() {
    // Given
    final var compilationUnit =
        createCompilationUnit(
            List.of(
                new Condition(
                    new GreaterExpression(
                        new IntLit("5"), new IntLit("3"), Position.of(2, 3, 1, 4)),
                    new Print(new StringLit("TRUE", Position.of(2, 8, 1, 13))),
                    null,
                    Position.of(2, 1, 1, 13))));

    final var expected = Collections.emptyList();

    // When
    final var actual = validator.validate(compilationUnit);

    // Then
    assertEquals(expected, actual);
  }

  @Test
  void testValidate_whileLoop() {
    // Given
    final var compilationUnit =
        createCompilationUnit(
            List.of(
                new While(
                    new BooleanLit("true", Position.of(1, 6, 1, 10)),
                    new Print(new StringLit("Looping", Position.of(2, 6, 2, 13))),
                    Position.of(1, 1, 2, 13))));

    final var expected = Collections.emptyList();

    // When
    final var actual = validator.validate(compilationUnit);

    // Then
    assertEquals(expected, actual);
  }

  // TODO: More while loop tests

  @Test
  void testValidate_incrementDecrement() {
    // Given
    final var compilationUnit =
        createCompilationUnit(
            List.of(
                new VarDeclaration("count", new IntLit("5"), Position.of(1, 1, 1, 13)),
                new VarDeclaration(
                    "result", new PostIncrement("count"), Position.of(2, 1, 2, 20))));

    final var expected = Collections.emptyList();

    // When
    final var actual = validator.validate(compilationUnit);

    // Then
    assertEquals(expected, actual);
  }

  @Test
  void testValidate_incrementDecrement_notNumeric() {
    // Given
    final var compilationUnit =
        createCompilationUnit(
            List.of(
                new VarDeclaration("count", new BooleanLit("true"), Position.of(1, 1, 1, 13)),
                new VarDeclaration(
                    "result",
                    new PostIncrement("count", Position.of(2, 13, 2, 20)),
                    Position.of(2, 1, 2, 20))));

    final var expected =
        List.of(new Error("Variable 'count' must be a numeric type at [2,13]", new Point(2, 13)));

    // When
    final var actual = validator.validate(compilationUnit);

    // Then
    assertEquals(expected, actual);
  }

  private CompilationUnit createCompilationUnit(List<Statement> statements) {
    return new CompilationUnit(
        Collections.singletonList(
            new MethodDeclaration(
                "test", Collections.emptyList(), Type.VOID_TYPE, new Block(statements))));
  }
}
