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
public class IntLit implements Expression {

  private final String value;

  private Position position;

  @Override
  public void process(Consumer<Node> operation) {
    operation.accept(this);
  }

  @Override
  public Type getType(Map<String, Variable> varMap) {
    return Type.INT_TYPE;
  }

  @Override
  public void push(MethodVisitor mv, Map<String, Variable> varMap) {
    mv.visitLdcInsn(parseInt(value));
  }

  private int parseInt(String v) {
    return v.toLowerCase().startsWith("0b")
        ? Integer.parseInt(v.substring(2), 2)
        : Integer.decode(v);
  }
}
