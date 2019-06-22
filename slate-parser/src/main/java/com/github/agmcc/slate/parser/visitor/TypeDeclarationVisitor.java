package com.github.agmcc.slate.parser.visitor;

import com.github.agmcc.slate.parser.SlateParser.ClassDeclarationContext;
import com.github.agmcc.slate.parser.SlateParser.CompilationUnitContext;
import com.github.agmcc.slate.parser.SlateParser.NormalInterfaceDeclarationContext;
import com.github.agmcc.slate.parser.SlateParser.SuperTypesContext;
import com.github.agmcc.slate.parser.SlateParserBaseVisitor;
import com.github.agmcc.slate.parser.ast.ClassDeclaration;
import com.github.agmcc.slate.parser.ast.ClassModifier;
import com.github.agmcc.slate.parser.ast.InterfaceDeclaration;
import com.github.agmcc.slate.parser.ast.InterfaceModifier;
import com.github.agmcc.slate.parser.ast.TypeDeclaration;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

public class TypeDeclarationVisitor extends SlateParserBaseVisitor<TypeDeclaration> {

  private MethodDeclarationVisitor methodDeclarationVisitor;

  @Inject
  public TypeDeclarationVisitor(final MethodDeclarationVisitor methodDeclarationVisitor) {
    this.methodDeclarationVisitor = methodDeclarationVisitor;
  }

  @Override
  public TypeDeclaration visitClassDeclaration(final ClassDeclarationContext ctx) {
    return ClassDeclaration.builder()
        .modifier(getClassModifier(ctx))
        .name(getCanonicalName(getPackageName(ctx), ctx.TYPE_ID().getText()))
        .superTypes(getSuperTypes(ctx.superTypes()))
        .methodDeclarations(
            ctx.classBody()
                .classBodyDeclaration()
                .stream()
                .map(
                    c ->
                        c.classMemberDeclaration()
                            .methodDeclaration()
                            .accept(methodDeclarationVisitor))
                .collect(Collectors.toList()))
        .build();
  }

  @Override
  public TypeDeclaration visitNormalInterfaceDeclaration(
      final NormalInterfaceDeclarationContext ctx) {
    return InterfaceDeclaration.builder()
        .modifier(getInterfaceModifier(ctx))
        .name(getCanonicalName(getPackageName(ctx), ctx.TYPE_ID().getText()))
        .superTypes(getSuperTypes(ctx.superTypes()))
        .build();
  }

  private ClassModifier getClassModifier(final ClassDeclarationContext ctx) {
    return Optional.ofNullable(ctx.classModifier())
        .map(m -> ClassModifier.fromText(m.getText()))
        .orElse(null);
  }

  private InterfaceModifier getInterfaceModifier(final NormalInterfaceDeclarationContext ctx) {
    return Optional.ofNullable(ctx.interfaceModifier())
        .map(m -> InterfaceModifier.fromText(m.getText()))
        .orElse(null);
  }

  private Set<String> getSuperTypes(final SuperTypesContext ctx) {
    return Optional.ofNullable(ctx)
        .map(
            c ->
                c.superTypeList()
                    .TYPE_ID()
                    .stream()
                    .map(TerminalNode::getText)
                    .collect(Collectors.toSet()))
        .orElse(Collections.emptySet());
  }

  private String getPackageName(final ParserRuleContext ctx) {
    if (ctx.getParent() == null
        || !(ctx.getParent().getParent() instanceof CompilationUnitContext)) {
      return null;
    }

    return Optional.ofNullable(
            ((CompilationUnitContext) ctx.getParent().getParent()).packageDeclaration())
        .map(p -> p.ID().stream().map(TerminalNode::getText).collect(Collectors.joining(".")))
        .orElse(null);
  }

  private String getCanonicalName(final String packageName, final String name) {
    return Optional.ofNullable(packageName).map(p -> String.join(".", p, name)).orElse(name);
  }
}
