package com.github.agmcc.slate.parser.visitor;

import com.github.agmcc.slate.parser.SlateParser.MethodDeclarationContext;
import com.github.agmcc.slate.parser.SlateParserBaseVisitor;
import com.github.agmcc.slate.parser.ast.MethodDeclaration;
import javax.inject.Inject;

public class MethodDeclarationVisitor extends SlateParserBaseVisitor<MethodDeclaration> {

  @Inject
  public MethodDeclarationVisitor() {}

  @Override
  public MethodDeclaration visitMethodDeclaration(final MethodDeclarationContext ctx) {
    // TODO
    return MethodDeclaration.builder()
        .name(ctx.methodHeader().methodDeclarator().ID().getText())
        .build();
  }
}
