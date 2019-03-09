package com.github.agmcc.slate.bytecode;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.V1_6;

import com.github.agmcc.slate.ast.CompilationUnit;
import com.github.agmcc.slate.ast.MethodDeclaration;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;

public class JvmCompiler {

  public byte[] compile(CompilationUnit compilationUnit, String className) {
    final var cw = new ClassWriter(COMPUTE_FRAMES);

    cw.visit(V1_6, ACC_PUBLIC, className, null, Type.getInternalName(Object.class), null);

    final Scope classScope = new Scope(null);

    // Add methods to scope
    compilationUnit.specificProcess(
        MethodDeclaration.class,
        m -> classScope.add(new Method(m.getName(), m.getReturnType(), className)));

    compilationUnit.getMethods().forEach(m -> m.generate(cw, classScope));

    cw.visitEnd();

    return cw.toByteArray();
  }
}
