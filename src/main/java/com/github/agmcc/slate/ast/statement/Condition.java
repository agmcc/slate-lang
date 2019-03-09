package com.github.agmcc.slate.ast.statement;

import com.github.agmcc.slate.ast.Node;
import com.github.agmcc.slate.ast.Position;
import com.github.agmcc.slate.ast.expression.Expression;
import com.github.agmcc.slate.bytecode.Scope;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Condition implements Statement {

  private final Expression expression;

  private final Statement trueStatement;

  private Statement falseStatement;

  private Position position;

  @Override
  public void process(Consumer<Node> operation) {
    operation.accept(this);
    expression.process(operation);
    trueStatement.process(operation);
    if (falseStatement != null) {
      falseStatement.process(operation);
    }
  }

  @Override
  public void generate(MethodVisitor mv, Scope scope) {
    final var trueLabel = new Label();
    final var endLabel = new Label();

    // Assume VarRefs have already been validated
    expression.push(mv, scope);
    mv.visitJumpInsn(IFGT, trueLabel);

    if (falseStatement != null) {
      falseStatement.generate(mv, scope);
      mv.visitJumpInsn(GOTO, endLabel); // double check if without else works when false
    }

    mv.visitLabel(trueLabel);
    trueStatement.generate(mv, scope);
    mv.visitLabel(endLabel);
  }
}
