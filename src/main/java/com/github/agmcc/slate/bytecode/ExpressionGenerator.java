package com.github.agmcc.slate.bytecode;

import java.util.Map;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public interface ExpressionGenerator extends Opcodes {

  Type getType(Map<String, Variable> varMap);

  void push(MethodVisitor mv, Map<String, Variable> varMap);

  default void pushAs(MethodVisitor mv, Map<String, Variable> varMap, Type target) {
    push(mv, varMap);

    final var type = getType(varMap);

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
