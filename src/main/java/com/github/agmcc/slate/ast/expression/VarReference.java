package com.github.agmcc.slate.ast.expression;

import lombok.Data;

@Data
public class VarReference implements Expression {

  private final String text;
}
