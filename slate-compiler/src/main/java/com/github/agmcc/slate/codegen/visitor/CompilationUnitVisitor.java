package com.github.agmcc.slate.codegen.visitor;

import com.github.agmcc.slate.parser.ast.CompilationUnit;
import com.github.agmcc.slate.parser.ast.NodeVisitor;
import javax.inject.Inject;
import org.objectweb.asm.ClassWriter;

public class CompilationUnitVisitor implements NodeVisitor {

  private TypeDeclarationVisitor typeDeclarationVisitor;

  private ClassWriter cw;

  @Inject
  public CompilationUnitVisitor(
      final TypeDeclarationVisitor typeDeclarationVisitor, final ClassWriter cw) {
    this.typeDeclarationVisitor = typeDeclarationVisitor;
    this.cw = cw;
  }

  @Override
  public void visit(final CompilationUnit compilationUnit) {
    compilationUnit.getTypeDeclaration().accept(typeDeclarationVisitor);
    cw.visitEnd();
  }
}
