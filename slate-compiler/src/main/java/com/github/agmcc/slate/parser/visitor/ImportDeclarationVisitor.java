package com.github.agmcc.slate.parser.visitor;

import com.github.agmcc.slate.parser.SlateParser.SingleTypeImportDeclarationContext;
import com.github.agmcc.slate.parser.SlateParserBaseVisitor;
import com.github.agmcc.slate.parser.ast.ImportDeclaration;
import javax.inject.Inject;

public class ImportDeclarationVisitor extends SlateParserBaseVisitor<ImportDeclaration> {

  @Inject
  public ImportDeclarationVisitor() {}

  @Override
  public ImportDeclaration visitSingleTypeImportDeclaration(
      final SingleTypeImportDeclarationContext ctx) {

    return ImportDeclaration.builder().typeName(ctx.typeName().getText()).build();
  }
}
