package com.github.agmcc.slate.parser.ast;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

// TODO
@Builder
@Getter
@ToString
public class MethodDeclaration implements Node {

  private String name;

  // TODO

  @Override
  public void accept(final NodeVisitor visitor) {
    visitor.visit(this);
  }
}
