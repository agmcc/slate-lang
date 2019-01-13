package com.github.agmcc.slate.ast.expression;

import com.github.agmcc.slate.ast.Position;
import com.github.agmcc.slate.bytecode.Variable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.objectweb.asm.MethodVisitor;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PreDecrement implements IncrementDecrement {

  private final String text;

  private Position position;

  @Override
  public void push(MethodVisitor mv, Map<String, Variable> varMap) {
    final var variable = varMap.get(text);

    mv.visitIincInsn(variable.getIndex(), -1);

    mv.visitVarInsn(getType(varMap).getOpcode(ILOAD), variable.getIndex());
  }
}
