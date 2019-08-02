package com.github.agmcc.slate.parser.ast;

import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class InterfaceDeclaration implements TypeDeclaration {
  // TODO: Body

  private InterfaceModifier modifier;

  private String name;

  private Set<String> superTypes;

  @Override
  public void accept(final NodeVisitor visitor) {
    visitor.visit(this);
  }
}
