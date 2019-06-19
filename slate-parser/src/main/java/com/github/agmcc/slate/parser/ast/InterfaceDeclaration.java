package com.github.agmcc.slate.parser.ast;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class InterfaceDeclaration implements TypeDeclaration {
  // TODO: Super-types, body

  private InterfaceModifier modifier;

  private String name;

  @Override
  public void accept(final NodeVisitor visitor) {
    visitor.visit(this);
  }
}
