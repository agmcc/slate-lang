package com.github.agmcc.slate.parser.ast;

public interface NodeVisitor {

  default void visit(final CompilationUnit compilationUnit) {}

  default void visit(final TypeDeclaration typeDeclaration) {}

  default void visit(final ClassDeclaration classDeclaration) {}

  default void visit(final InterfaceDeclaration interfaceDeclaration) {}

  // TODO
  default void visit(final MethodDeclaration methodDeclaration) {}
}
