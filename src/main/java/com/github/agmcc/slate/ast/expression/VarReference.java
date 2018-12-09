package com.github.agmcc.slate.ast.expression;

import com.github.agmcc.slate.ast.Position;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class VarReference implements Expression {

  private final String text;

  private Position position;
}
