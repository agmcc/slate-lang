package com.github.agmcc.slate.parser.ast;

import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class ClassDeclaration implements TypeDeclaration {

  private ClassModifier modifier;

  private String name;

  private Set<String> superTypes;

  private List<MethodDeclaration> methodDeclarations;

  @Override
  public void accept(final NodeVisitor visitor) {
    visitor.visit(this);
  }
}
