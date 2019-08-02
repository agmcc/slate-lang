package com.github.agmcc.slate.parser.bootstrap;

import com.github.agmcc.slate.parser.SlateParser.ClassDeclarationContext;
import com.github.agmcc.slate.parser.SlateParser.CompilationUnitContext;
import com.github.agmcc.slate.parser.SlateParser.FieldDeclarationContext;
import com.github.agmcc.slate.parser.SlateParser.FieldModifierContext;
import com.github.agmcc.slate.parser.SlateParserBaseVisitor;
import com.github.agmcc.slate.parser.symbol.Field;
import com.github.agmcc.slate.parser.symbol.Modifiers;
import com.github.agmcc.slate.parser.symbol.SymbolManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.antlr.v4.runtime.tree.TerminalNode;

// TODO: Maybe this should be stateful?
@BootstrapScope
class SourceVisitor extends SlateParserBaseVisitor<Void> {

  private SymbolManager symbolManager;

  @Inject
  public SourceVisitor(final SymbolManager symbolManager) {
    this.symbolManager = symbolManager;
  }

  @Override
  public Void visitClassDeclaration(final ClassDeclarationContext ctx) {
    return super.visitClassDeclaration(ctx);
  }

  @Override
  public Void visitFieldDeclaration(final FieldDeclarationContext ctx) {

    final var classDeclaration =
        (ClassDeclarationContext) ctx.getParent().getParent().getParent().getParent();

    final var typeSimpleName = classDeclaration.TYPE_ID().getText();

    final var compilationUnit = (CompilationUnitContext) classDeclaration.getParent().getParent();

    final var packageName =
        Optional.ofNullable(compilationUnit.packageDeclaration())
            .map(p -> p.ID().stream().map(TerminalNode::getText).collect(Collectors.joining(".")))
            .orElse("");

    final var modifiers = getFieldModifiers(ctx.fieldModifier());

    ctx.variableDeclaratorList()
        .variableDeclarator()
        .forEach(
            v -> {
              final var fieldSimpleName = v.ID().getText();
              final var canonicalFieldName =
                  String.join(".", packageName, typeSimpleName, fieldSimpleName);

              symbolManager.putField(
                  canonicalFieldName,
                  Field.builder().name(canonicalFieldName).modifiers(modifiers).build());
            });

    return null;
  }

  private int getFieldModifiers(final List<FieldModifierContext> cxts) {
    // TODO: Default values and prevent incompatible modifiers e.g. PUBLIC + PRIVATE
    // TODO: Validation
    return cxts.stream()
        .map(
            c -> {
              if (c.PUBLIC() != null) {
                return Modifiers.PUBLIC;
              } else if (c.STATIC() != null) {
                return Modifiers.STATIC;
              } else if (c.CONSTANT() != null) {
                return Modifiers.CONSTANT;
              } else {
                throw new RuntimeException("Unknown field modifier: " + c.getText());
              }
            })
        .reduce(0, (a, b) -> a |= b);
  }
}
