package com.github.agmcc.slate.ast.expression.binary;

import com.github.agmcc.slate.ast.expression.Expression;
import com.github.agmcc.slate.bytecode.Variable;
import java.util.Map;
import org.objectweb.asm.Type;

public interface BinaryExpression extends Expression {

  Expression getLeft();

  Expression getRight();

  default Type getType(Map<String, Variable> varMap) {
    final var leftType = getLeft().getType(varMap);
    final var rightType = getRight().getType(varMap);

    final Type stringType = Type.getType(String.class);

    if (leftType.equals(stringType) || rightType.equals(stringType)) {
      return stringType;
    }

    if (leftType != Type.INT_TYPE && leftType != Type.DOUBLE_TYPE) {
      throw new UnsupportedOperationException("Unsupported type: " + leftType);
    }

    if (rightType != Type.INT_TYPE && rightType != Type.DOUBLE_TYPE) {
      throw new UnsupportedOperationException("Unsupported type: " + leftType);
    }

    if (leftType == Type.INT_TYPE && rightType == Type.INT_TYPE) {
      return Type.INT_TYPE;
    } else {
      return Type.DOUBLE_TYPE;
    }
  }
}
