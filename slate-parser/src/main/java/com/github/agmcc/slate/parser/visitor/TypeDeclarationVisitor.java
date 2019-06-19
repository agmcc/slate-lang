package com.github.agmcc.slate.parser.visitor;

import com.github.agmcc.slate.parser.ErrorListener;
import com.github.agmcc.slate.parser.SlateParser.ClassDeclarationContext;
import com.github.agmcc.slate.parser.SlateParser.NormalInterfaceDeclarationContext;
import com.github.agmcc.slate.parser.SlateParserBaseVisitor;
import com.github.agmcc.slate.parser.ast.ClassDeclaration;
import com.github.agmcc.slate.parser.ast.ClassModifier;
import com.github.agmcc.slate.parser.ast.InterfaceDeclaration;
import com.github.agmcc.slate.parser.ast.InterfaceModifier;
import com.github.agmcc.slate.parser.ast.TypeDeclaration;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;

public class TypeDeclarationVisitor extends SlateParserBaseVisitor<TypeDeclaration> {

  private ErrorListener errorListener;

  private MethodDeclarationVisitor methodDeclarationVisitor;

  @Inject
  public TypeDeclarationVisitor(
      final ErrorListener errorListener, final MethodDeclarationVisitor methodDeclarationVisitor) {
    this.errorListener = errorListener;
    this.methodDeclarationVisitor = methodDeclarationVisitor;
  }

  @Override
  public TypeDeclaration visitClassDeclaration(final ClassDeclarationContext ctx) {
    return ClassDeclaration.builder()
        .modifier(getClassModifier(ctx))
        .name(ctx.TYPE_ID().getText())
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
        .name(ctx.TYPE_ID().getText())
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
}
