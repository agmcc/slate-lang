package com.github.agmcc.slate.bytecode;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public interface ExpressionGenerator extends Opcodes {

  Type getType(Scope scope);

  void push(MethodVisitor mv, Scope scope);

  default void pushAs(MethodVisitor mv, Scope scope, Type target) {
    push(mv, scope);

    final var type = getType(scope);

    if (!type.equals(target)) {
      if (type == Type.INT_TYPE && target == Type.DOUBLE_TYPE) {
        mv.visitInsn(I2D);
      } else if (type == Type.DOUBLE_TYPE && target == Type.INT_TYPE) {
        mv.visitInsn(D2I);
      } else {
        throw new UnsupportedOperationException(
            String.format(
                "Unable to convert type %s to %s",
                type.getInternalName(), target.getInternalName()));
      }
    }
  }
}
