package com.github.agmcc.slate.ast.statement;

import com.github.agmcc.slate.ast.expression.Expression;
import lombok.Data;

@Data
public class Assignment implements Statement {

  private final String varName;

  private final Expression value;
}
