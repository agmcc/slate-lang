package com.github.agmcc.slate.ast.expression.binary;

import com.github.agmcc.slate.ast.Node;
import com.github.agmcc.slate.ast.Position;
import com.github.agmcc.slate.ast.expression.Expression;
import com.github.agmcc.slate.bytecode.Variable;
import java.util.Map;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AdditionExpression implements BinaryExpression {

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
  public void push(MethodVisitor mv, Map<String, Variable> varMap) {
    final var type = getType(varMap);

    left.pushAs(mv, varMap, type);
    right.pushAs(mv, varMap, type);

    final var stringType = Type.getType(String.class);

    if (type.equals(stringType)) {
      final var leftType = left.getType(varMap);
      final var rightType = right.getType(varMap);

      if (!leftType.equals(stringType) || !rightType.equals(stringType)) {
        throw new UnsupportedOperationException("String concatenation only supports 2 Strings");
      }

      mv.visitMethodInsn(
          INVOKEVIRTUAL,
          Type.getInternalName(String.class),
          "concat",
          Type.getMethodDescriptor(stringType, rightType),
          false);
    } else {
      mv.visitInsn(type.getOpcode(IADD));
    }
  }
}
