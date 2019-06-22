package com.github.agmcc.slate.parser.visitor;

import com.github.agmcc.slate.parser.SlateParser.CompilationUnitContext;
import com.github.agmcc.slate.parser.SlateParser.ImportDeclarationContext;
import com.github.agmcc.slate.parser.SlateParserBaseVisitor;
import com.github.agmcc.slate.parser.ast.CompilationUnit;
import com.github.agmcc.slate.parser.ast.ImportDeclaration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;

public class CompilationUnitVisitor extends SlateParserBaseVisitor<CompilationUnit> {

  private TypeDeclarationVisitor typeDeclarationVisitor;

  private ImportDeclarationVisitor importDeclarationVisitor;

  @Inject
  public CompilationUnitVisitor(
      final TypeDeclarationVisitor typeDeclarationVisitor,
      final ImportDeclarationVisitor importDeclarationVisitor) {

    this.typeDeclarationVisitor = typeDeclarationVisitor;
    this.importDeclarationVisitor = importDeclarationVisitor;
  }

  @Override
  public CompilationUnit visitCompilationUnit(final CompilationUnitContext ctx) {
    return CompilationUnit.builder()
        .imports(getImports(ctx.importDeclaration()))
        .typeDeclaration(ctx.typeDeclaration().accept(typeDeclarationVisitor))
        .build();
  }

  private Set<ImportDeclaration> getImports(final List<ImportDeclarationContext> ctx) {
    return Optional.ofNullable(ctx)
        .map(
            ic ->
                ic.stream()
                    .map(i -> i.accept(importDeclarationVisitor))
                    .collect(Collectors.toSet()))
        .orElse(Collections.emptySet());
  }
}
