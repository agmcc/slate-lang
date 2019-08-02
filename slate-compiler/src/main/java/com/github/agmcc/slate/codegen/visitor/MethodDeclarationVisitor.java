package com.github.agmcc.slate.codegen.visitor;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.RETURN;

import com.github.agmcc.slate.parser.ast.MethodDeclaration;
import com.github.agmcc.slate.parser.ast.NodeVisitor;
import javax.inject.Inject;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;

public class MethodDeclarationVisitor implements NodeVisitor {

  private ClassWriter cw;

  @Inject
  public MethodDeclarationVisitor(final ClassWriter cw) {
    this.cw = cw;
  }

  @Override
  public void visit(final MethodDeclaration methodDeclaration) {
    // TODO: Stubbed
    final var mv =
        cw.visitMethod(
            ACC_PUBLIC | ACC_STATIC,
            methodDeclaration.getName(),
            Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(String[].class)),
            null,
            null);

    mv.visitCode();
    mv.visitMaxs(100, 100);
    mv.visitInsn(RETURN);
    mv.visitEnd();
  }
}
