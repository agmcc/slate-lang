package com.github.agmcc.slate.ast.expression.binary;

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
import org.objectweb.asm.MethodVisitor;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SubtractionExpression implements BinaryExpression {

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

    mv.visitInsn(type.getOpcode(ISUB));
  }
}
