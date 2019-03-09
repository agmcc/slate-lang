package com.github.agmcc.slate.bytecode;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public interface StatementGenerator extends Opcodes {

  void generate(MethodVisitor mv, Scope scope);
}
