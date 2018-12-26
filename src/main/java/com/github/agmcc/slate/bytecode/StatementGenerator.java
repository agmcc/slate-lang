package com.github.agmcc.slate.bytecode;

import java.util.Map;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public interface StatementGenerator extends Opcodes {

  void generate(MethodVisitor mv, Map<String, Variable> varMap);
}
