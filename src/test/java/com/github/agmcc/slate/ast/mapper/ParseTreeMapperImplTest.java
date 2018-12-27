package com.github.agmcc.slate.ast.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.agmcc.slate.antlr.SlateParser.CompilationUnitContext;
import com.github.agmcc.slate.ast.CompilationUnit;
import com.github.agmcc.slate.ast.Position;
import com.github.agmcc.slate.ast.expression.DecLit;
import com.github.agmcc.slate.ast.expression.IntLit;
import com.github.agmcc.slate.ast.expression.StringLit;
import com.github.agmcc.slate.ast.expression.VarReference;
import com.github.agmcc.slate.ast.expression.binary.AdditionExpression;
import com.github.agmcc.slate.ast.expression.binary.DivisionExpression;
import com.github.agmcc.slate.ast.expression.binary.MultiplicationExpression;
import com.github.agmcc.slate.ast.expression.binary.SubtractionExpression;
import com.github.agmcc.slate.ast.statement.Assignment;
import com.github.agmcc.slate.ast.statement.Block;
import com.github.agmcc.slate.ast.statement.Print;
import com.github.agmcc.slate.ast.statement.VarDeclaration;
import com.github.agmcc.slate.test.ANTLRUtils;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ParseTreeMapperImplTest {

  private static ParseTreeMapper<CompilationUnitContext, CompilationUnit> mapper;

  @BeforeAll
  static void setup() {
    mapper = new ParseTreeMapperImpl();
  }

  @Test
  void testToAst_null() {
    // When
    final var actual = mapper.toAst(null);

    // Then
    assertNull(actual);
  }

  @Test
  void testToAst_uninitialised() {
    // Given
    final var compilationUnitCtx = new CompilationUnitContext(null, -1);

    // When
    final var actual = mapper.toAst(compilationUnitCtx);

    // Then
    assertNotNull(actual);
    final var statements = actual.getStatements();
    assertNotNull(statements);
    assertTrue(statements.isEmpty());
  }

  @Test
  void testToAst_varDeclaration() {
    // Given
    final var src = "var price = 9.95";

    final var expected =
        new CompilationUnit(List.of(new VarDeclaration("price", new DecLit("9.95"))));

    // When
    final var actual = mapper.toAst(ANTLRUtils.parseString(src));

    // Then
    assertEquals(expected, actual);
  }

  @Test
  void testToAst_varDeclaration_division() {
    // Given
    final var src = "var quotient = 100 / 2.5";

    final var expected =
        new CompilationUnit(
            List.of(
                new VarDeclaration(
                    "quotient", new DivisionExpression(new IntLit("100"), new DecLit("2.5")))));

    // When
    final var actual = mapper.toAst(ANTLRUtils.parseString(src));

    // Then
    assertEquals(expected, actual);
  }

  @Test
  void testToAst_assignment() {
    // Given
    final var src = "hours = 5";

    final var expected = new CompilationUnit(List.of(new Assignment("hours", new IntLit("5"))));

    // When
    final var actual = mapper.toAst(ANTLRUtils.parseString(src));

    // Then
    assertEquals(expected, actual);
  }

  @Test
  void testToAst_assignment_varReference() {
    // Given
    final var src = "a = b";

    final var expected = new CompilationUnit(List.of(new Assignment("a", new VarReference("b"))));

    // When
    final var actual = mapper.toAst(ANTLRUtils.parseString(src));

    // Then
    assertEquals(expected, actual);
  }

  @Test
  void testToAst_assignment_addition() {
    // Given
    final var src = "sum = 5 + 7";

    final var expected =
        new CompilationUnit(
            List.of(
                new Assignment("sum", new AdditionExpression(new IntLit("5"), new IntLit("7")))));

    // When
    final var actual = mapper.toAst(ANTLRUtils.parseString(src));

    // Then
    assertEquals(expected, actual);
  }

  @Test
  void testToAst_print() {
    // Given
    final var src = "print 'Hello, World!'";

    final var expected = new CompilationUnit(List.of(new Print(new StringLit("Hello, World!"))));

    // When
    final var actual = mapper.toAst(ANTLRUtils.parseString(src));

    // Then
    assertEquals(expected, actual);
  }

  @Test
  void testToAst_print_subtraction() {
    // Given
    final var src = "print 10 - 1";

    final var expected =
        new CompilationUnit(
            List.of(new Print(new SubtractionExpression(new IntLit("10"), new IntLit("1")))));

    // When
    final var actual = mapper.toAst(ANTLRUtils.parseString(src));

    // Then
    assertEquals(expected, actual);
  }

  @Test
  void testToAst_print_multiplication() {
    // Given
    final var src = "print 2 * 4";

    final var expected =
        new CompilationUnit(
            List.of(new Print(new MultiplicationExpression(new IntLit("2"), new IntLit("4")))));

    // When
    final var actual = mapper.toAst(ANTLRUtils.parseString(src));

    // Then
    assertEquals(expected, actual);
  }

  @Test
  void testToAst_multipleStatements() {
    // Given
    final var src = "var price = 10.00 var discounted = 7.99";

    final var expected =
        new CompilationUnit(
            List.of(
                new VarDeclaration("price", new DecLit("10.00")),
                new VarDeclaration("discounted", new DecLit("7.99"))));

    // When
    final var actual = mapper.toAst(ANTLRUtils.parseString(src));

    // Then
    assertEquals(expected, actual);
  }

  @Test
  void testToAst_multipleStatements_newline() {
    // Given
    final var src = "var dogs = 3\nprint 'Number of dogs: ' + dogs";

    final var expected =
        new CompilationUnit(
            List.of(
                new VarDeclaration("dogs", new IntLit("3")),
                new Print(
                    new AdditionExpression(
                        new StringLit("Number of dogs: "), new VarReference("dogs")))));

    // When
    final var actual = mapper.toAst(ANTLRUtils.parseString(src));

    // Then
    assertEquals(expected, actual);
  }

  @Test
  void testToAst_block() {
    // Given
    final var src = "{ var price = 9.95 }";

    final var expected =
        new CompilationUnit(
            List.of(
                new Block(
                    List.of(
                        new VarDeclaration(
                            "price",
                            new DecLit("9.95", Position.of(1, 14, 1, 18)),
                            Position.of(1, 2, 1, 18))),
                    Position.of(1, 0, 1, 20))),
            Position.of(1, 0, 1, 20));

    final var positionMapper = new ParseTreeMapperImpl(true);

    // When
    final var actual = positionMapper.toAst(ANTLRUtils.parseString(src));

    // Then
    assertEquals(expected, actual);
  }

  @Test
  void testToAst_block_emptyBody() {
    // Given
    final var src = "{}";

    final var expected = new CompilationUnit(List.of(new Block(Collections.emptyList())));

    // When
    final var actual = mapper.toAst(ANTLRUtils.parseString(src));

    // Then
    assertEquals(expected, actual);
  }

  @Test
  void testToAst_block_multiStatement() {
    // Given
    final var src = "{ a = 1 a = 2 }";

    final var expected =
        new CompilationUnit(
            List.of(
                new Block(
                    List.of(
                        new Assignment("a", new IntLit("1")),
                        new Assignment("a", new IntLit("2"))))));

    // When
    final var actual = mapper.toAst(ANTLRUtils.parseString(src));

    // Then
    assertEquals(expected, actual);
  }

  @Test
  void testToAst_positions() {
    // Given
    final var src = "var price = 9.95";

    final var expected =
        new CompilationUnit(
            List.of(
                new VarDeclaration(
                    "price",
                    new DecLit("9.95", Position.of(1, 12, 1, 16)),
                    Position.of(1, 0, 1, 16))),
            Position.of(1, 0, 1, 16));

    final var positionMapper = new ParseTreeMapperImpl(true);

    // When
    final var actual = positionMapper.toAst(ANTLRUtils.parseString(src));

    // Then
    assertEquals(expected, actual);
  }
}
