package com.github.agmcc.slate.ast.expression;

import com.github.agmcc.slate.ast.Position;
import com.github.agmcc.slate.bytecode.Scope;
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
public class PostIncrement implements IncrementDecrement {

  private final String text;

  private Position position;

  @Override
  public void push(MethodVisitor mv, Scope scope) {
    final var variable = scope.getVariable(text);

    mv.visitVarInsn(getType(scope).getOpcode(ILOAD), variable.getIndex());

    mv.visitIincInsn(variable.getIndex(), 1);
  }
}
