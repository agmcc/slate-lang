package com.github.agmcc.slate.ast;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.github.agmcc.slate.ast.expression.DecLit;
import com.github.agmcc.slate.ast.expression.IntLit;
import com.github.agmcc.slate.ast.expression.VarReference;
import com.github.agmcc.slate.ast.expression.binary.AdditionExpression;
import com.github.agmcc.slate.ast.expression.binary.BinaryExpression;
import com.github.agmcc.slate.ast.expression.binary.DivisionExpression;
import com.github.agmcc.slate.ast.statement.Block;
import com.github.agmcc.slate.ast.statement.Print;
import com.github.agmcc.slate.ast.statement.Statement;
import com.github.agmcc.slate.ast.statement.VarDeclaration;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.Type;

class NodeTest {

  @Test
  void testProcess() {
    // Given
    final var compilationUnit =
        createCompilationUnitWithMethod(List.of(new Print(new DecLit("10.5"))));

    // When
    compilationUnit.process(n -> assertNull(n.getPosition()));
  }

  @Test
  void testSpecificProcess() {
    // Given
    final var compilationUnit =
        createCompilationUnitWithMethod(
            List.of(new Print(new DecLit("10.5"), Position.of(0, 0, 0, 0))));

    // When
    compilationUnit.specificProcess(Print.class, p -> assertNotNull(p.getPosition()));
  }

  @Test
  void testSpecificProcess_multipleMatches() {
    // Given
    final var addition = new AdditionExpression(new IntLit("1"), new IntLit("3"));

    // When
    final var matches = new HashSet<>();
    addition.specificProcess(IntLit.class, i -> matches.add(i.getValue()));

    // Then
    assertEquals(Set.of("1", "3"), matches);
  }

  @Test
  void testSpecificProcess_assignableTo() {
    // Given
    final var varDec =
        new VarDeclaration(
            "value",
            new AdditionExpression(
                new IntLit("1"), new DivisionExpression(new DecLit("2.2"), new VarReference("a"))));

    // When
    final var matches = new HashSet<>();
    varDec.specificProcess(BinaryExpression.class, b -> matches.add(b.getClass()));

    // Then
    assertEquals(Set.of(AdditionExpression.class, DivisionExpression.class), matches);
  }

  private CompilationUnit createCompilationUnitWithMethod(List<Statement> statements) {
    return new CompilationUnit(
        List.of(
            new MethodDeclaration(
                "test", Collections.emptyList(), Type.VOID_TYPE, new Block(statements))));
  }
}
