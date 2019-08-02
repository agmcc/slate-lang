package com.github.agmcc.slate.parser.ast;

import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class CompilationUnit implements Node {

  private Set<ImportDeclaration> imports;

  private TypeDeclaration typeDeclaration;

  @Override
  public void accept(final NodeVisitor visitor) {
    visitor.visit(this);
  }
}
