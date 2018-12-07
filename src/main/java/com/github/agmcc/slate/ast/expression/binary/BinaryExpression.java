package com.github.agmcc.slate.ast.expression.binary;

import com.github.agmcc.slate.ast.expression.Expression;

public interface BinaryExpression extends Expression {

  Expression getLeft();

  Expression getRight();
}
