package com.github.agmcc.slate.parser.ast;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class ClassDeclaration implements TypeDeclaration {

  // TODO: Super-types, body

  private ClassModifier modifier;

  private String name;

  private List<MethodDeclaration> methodDeclarations;

  @Override
  public void accept(final NodeVisitor visitor) {
    visitor.visit(this);
  }
}
