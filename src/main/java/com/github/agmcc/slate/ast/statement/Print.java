package com.github.agmcc.slate.ast.statement;

import com.github.agmcc.slate.ast.expression.Expression;
import lombok.Data;

@Data
public class Print implements Statement {

  private final Expression value;
}
