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
import org.objectweb.asm.Type;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class LessEqualExpression implements LogicExpression {

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
    final var type = getType(scope);

    left.pushAs(mv, scope, type);
    right.pushAs(mv, scope, type);

    final var trueLabel = new Label();
    final var endLabel = new Label();

    if (type.equals(Type.INT_TYPE)) {
      mv.visitJumpInsn(IF_ICMPLE, trueLabel);
    } else if (type.equals(Type.DOUBLE_TYPE)) {
      mv.visitInsn(DCMPL);
      mv.visitJumpInsn(IFGE, trueLabel);
    } else {
      throw new UnsupportedOperationException("Unsupported type: " + type);
    }

    // False - push 0 (false)
    mv.visitInsn(ICONST_0);
    mv.visitJumpInsn(GOTO, endLabel);

    // True - push 1 (true)
    mv.visitLabel(trueLabel);
    mv.visitInsn(ICONST_1);
    mv.visitLabel(endLabel);
  }
}
