package com.github.agmcc.slate.ast.expression;

import lombok.Data;

@Data
public class IntLit implements Expression {

  private final String value;
}
