package com.github.agmcc.slate.parser.visitor;

import com.github.agmcc.slate.parser.ErrorListener;
import com.github.agmcc.slate.parser.SlateParser.CompilationUnitContext;
import com.github.agmcc.slate.parser.SlateParserBaseVisitor;
import com.github.agmcc.slate.parser.ast.CompilationUnit;
import javax.inject.Inject;

public class CompilationUnitVisitor extends SlateParserBaseVisitor<CompilationUnit> {

  private TypeDeclarationVisitor typeDeclarationVisitor;

  private ErrorListener errorListener;

  @Inject
  public CompilationUnitVisitor(
      final TypeDeclarationVisitor typeDeclarationVisitor, final ErrorListener errorListener) {
    this.typeDeclarationVisitor = typeDeclarationVisitor;
    this.errorListener = errorListener;
  }

  @Override
  public CompilationUnit visitCompilationUnit(final CompilationUnitContext ctx) {
    // Package
    // Imports

    final var typeDeclaration = ctx.typeDeclaration().accept(typeDeclarationVisitor);

    return CompilationUnit.builder().typeDeclaration(typeDeclaration).build();
  }
}
