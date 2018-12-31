package com.github.agmcc.slate.ast.expression;

import com.github.agmcc.slate.ast.Node;
import com.github.agmcc.slate.ast.Position;
import com.github.agmcc.slate.bytecode.Variable;
import java.util.Map;
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
  public Type getType(Map<String, Variable> varMap) {
    return Type.BOOLEAN_TYPE;
  }

  @Override
  public void push(MethodVisitor mv, Map<String, Variable> varMap) {
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
