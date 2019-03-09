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
public class DecLit implements Expression {

  private final String value;

  private Position position;

  @Override
  public void process(Consumer<Node> operation) {
    operation.accept(this);
  }

  @Override
  public Type getType(Scope scope) {
    return Type.DOUBLE_TYPE;
  }

  @Override
  public void push(MethodVisitor mv, Scope scope) {
    mv.visitLdcInsn(parseDouble(value));
  }

  private double parseDouble(String v) {
    final var stripped = v.replaceAll("_", "");
    return Double.parseDouble(stripped);
  }
}
