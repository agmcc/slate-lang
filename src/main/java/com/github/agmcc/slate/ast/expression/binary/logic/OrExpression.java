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
public class OrExpression implements LogicExpression {

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

    final var trueLabel = new Label();
    final var endLabel = new Label();

    left.push(mv, scope);
    mv.visitJumpInsn(IFGT, trueLabel); // if left true, push true and exit

    // if left false, push right
    right.push(mv, scope);
    mv.visitJumpInsn(IFGT, trueLabel); // if right true, push true and exit

    mv.visitInsn(ICONST_0); // right false, push false and exit
    mv.visitJumpInsn(GOTO, endLabel);

    mv.visitLabel(trueLabel);
    mv.visitInsn(ICONST_1);
    mv.visitJumpInsn(GOTO, endLabel);

    mv.visitLabel(endLabel);
  }
}
