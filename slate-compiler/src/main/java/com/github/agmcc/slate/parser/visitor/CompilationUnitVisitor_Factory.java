package com.github.agmcc.slate.parser.visitor;

import dagger.internal.Factory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@Generated(value = "dagger.internal.codegen.ComponentProcessor", comments = "https://dagger.dev")
public final class CompilationUnitVisitor_Factory implements Factory<CompilationUnitVisitor> {
  private final Provider<TypeDeclarationVisitor> typeDeclarationVisitorProvider;

  private final Provider<ImportDeclarationVisitor> importDeclarationVisitorProvider;

  public CompilationUnitVisitor_Factory(
      final Provider<TypeDeclarationVisitor> typeDeclarationVisitorProvider,
      final Provider<ImportDeclarationVisitor> importDeclarationVisitorProvider) {
    this.typeDeclarationVisitorProvider = typeDeclarationVisitorProvider;
    this.importDeclarationVisitorProvider = importDeclarationVisitorProvider;
  }

  @Override
  public CompilationUnitVisitor get() {
    return new CompilationUnitVisitor(
        typeDeclarationVisitorProvider.get(), importDeclarationVisitorProvider.get());
  }

  public static CompilationUnitVisitor_Factory create(
      final Provider<TypeDeclarationVisitor> typeDeclarationVisitorProvider,
      final Provider<ImportDeclarationVisitor> importDeclarationVisitorProvider) {
    return new CompilationUnitVisitor_Factory(
        typeDeclarationVisitorProvider, importDeclarationVisitorProvider);
  }

  public static CompilationUnitVisitor newInstance(
      final TypeDeclarationVisitor typeDeclarationVisitor,
      final ImportDeclarationVisitor importDeclarationVisitor) {
    return new CompilationUnitVisitor(typeDeclarationVisitor, importDeclarationVisitor);
  }
}
