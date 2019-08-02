package com.github.agmcc.slate.parser.visitor;

import dagger.internal.Factory;
import javax.annotation.processing.Generated;

@Generated(value = "dagger.internal.codegen.ComponentProcessor", comments = "https://dagger.dev")
public final class ImportDeclarationVisitor_Factory implements Factory<ImportDeclarationVisitor> {
  private static final ImportDeclarationVisitor_Factory INSTANCE =
      new ImportDeclarationVisitor_Factory();

  @Override
  public ImportDeclarationVisitor get() {
    return new ImportDeclarationVisitor();
  }

  public static ImportDeclarationVisitor_Factory create() {
    return INSTANCE;
  }

  public static ImportDeclarationVisitor newInstance() {
    return new ImportDeclarationVisitor();
  }
}
