package com.github.agmcc.slate.ast.expression;

import com.github.agmcc.slate.ast.Node;
import com.github.agmcc.slate.ast.Position;
import com.github.agmcc.slate.bytecode.Scope;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class BooleanLit implements Expression {

  private final String value;

  private Position position;

  @Override
  public void process(Consumer<Node> operation) {
    operation.accept(this);
  }

  @Override
  public Type getType(Scope scope) {
    return Type.BOOLEAN_TYPE;
  }

  @Override
  public void push(MethodVisitor mv, Scope scope) {
    mv.visitInsn(getBooleanOpcode(value));
  }

  private int getBooleanOpcode(String value) {
    if (value.equals(Boolean.TRUE.toString())) {
      return ICONST_1;
    } else if (value.equals(Boolean.FALSE.toString())) {
      return ICONST_0;
    } else {
      throw new UnsupportedOperationException("Invalid boolean value: " + value);
    }
  }
}
