package com.github.agmcc.slate.ast.statement;

import com.github.agmcc.slate.ast.expression.Expression;
import lombok.Data;

@Data
public class VarDeclaration implements Statement {

  private final String varName;

  private final Expression value;
}
