package com.github.agmcc.slate.parser.ast;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class CompilationUnit implements Node {

  private TypeDeclaration typeDeclaration;

  @Override
  public void accept(final NodeVisitor visitor) {
    visitor.visit(this);
  }
}
