package com.github.agmcc.slate.bytecode;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public interface MethodGenerator extends Opcodes {

  void generate(ClassVisitor cv, Scope scope);
}
