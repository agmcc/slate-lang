package com.github.agmcc.slate.ast.expression.binary.logic;

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
public class AndExpression implements LogicExpression {

  private final Expression left;

  private final Expression right;

  private Position position;

  @Override
  public void process(Consumer<Node> operation) {
    operation.accept(this);
    left.process(operation);
    right.process(operation);
  }

  @Override
  public void push(MethodVisitor mv, Scope scope) {
    // TODO: Assuming left and right evaluate to boolean

    final var rightLabel = new Label();
    final var trueLabel = new Label();
    final var endLabel = new Label();

    left.push(mv, scope);
    mv.visitJumpInsn(IFGT, rightLabel);

    // left == false, skip right
    mv.visitInsn(ICONST_0); // push false
    mv.visitJumpInsn(GOTO, endLabel);

    // left == true, push right
    mv.visitLabel(rightLabel);
    right.push(mv, scope);
    mv.visitJumpInsn(IFGT, trueLabel); // push true if right == true

    mv.visitInsn(ICONST_0); // push false if right == false
    mv.visitJumpInsn(GOTO, endLabel);

    mv.visitLabel(trueLabel);
    mv.visitInsn(ICONST_1); // push true if right == true
    mv.visitJumpInsn(GOTO, endLabel);

    mv.visitLabel(endLabel);
  }
}
