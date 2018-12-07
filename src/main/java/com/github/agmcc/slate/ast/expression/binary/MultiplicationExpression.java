package com.github.agmcc.slate.ast.expression.binary;

import com.github.agmcc.slate.ast.expression.Expression;
import lombok.Data;

@Data
public class MultiplicationExpression implements BinaryExpression {

  private final Expression left;

  private final Expression right;
}
