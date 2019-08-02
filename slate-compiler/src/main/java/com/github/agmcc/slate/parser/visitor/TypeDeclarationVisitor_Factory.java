package com.github.agmcc.slate.parser.visitor;

import dagger.internal.Factory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@Generated(value = "dagger.internal.codegen.ComponentProcessor", comments = "https://dagger.dev")
public final class TypeDeclarationVisitor_Factory implements Factory<TypeDeclarationVisitor> {
  private final Provider<MethodDeclarationVisitor> methodDeclarationVisitorProvider;

  public TypeDeclarationVisitor_Factory(
      final Provider<MethodDeclarationVisitor> methodDeclarationVisitorProvider) {
    this.methodDeclarationVisitorProvider = methodDeclarationVisitorProvider;
  }

  @Override
  public TypeDeclarationVisitor get() {
    return new TypeDeclarationVisitor(methodDeclarationVisitorProvider.get());
  }

  public static TypeDeclarationVisitor_Factory create(
      final Provider<MethodDeclarationVisitor> methodDeclarationVisitorProvider) {
    return new TypeDeclarationVisitor_Factory(methodDeclarationVisitorProvider);
  }

  public static TypeDeclarationVisitor newInstance(
      final MethodDeclarationVisitor methodDeclarationVisitor) {
    return new TypeDeclarationVisitor(methodDeclarationVisitor);
  }
}
