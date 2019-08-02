package com.github.agmcc.slate.parser.visitor;

import dagger.internal.Factory;
import javax.annotation.processing.Generated;

@Generated(value = "dagger.internal.codegen.ComponentProcessor", comments = "https://dagger.dev")
public final class MethodDeclarationVisitor_Factory implements Factory<MethodDeclarationVisitor> {
  private static final MethodDeclarationVisitor_Factory INSTANCE =
      new MethodDeclarationVisitor_Factory();

  @Override
  public MethodDeclarationVisitor get() {
    return new MethodDeclarationVisitor();
  }

  public static MethodDeclarationVisitor_Factory create() {
    return INSTANCE;
  }

  public static MethodDeclarationVisitor newInstance() {
    return new MethodDeclarationVisitor();
  }
}
