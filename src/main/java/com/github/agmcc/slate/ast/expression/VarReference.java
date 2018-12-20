package com.github.agmcc.slate.ast.expression;

import com.github.agmcc.slate.ast.Node;
import com.github.agmcc.slate.ast.Position;
import com.github.agmcc.slate.bytecode.Variable;
import java.util.Map;
import java.util.Optional;
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
public class VarReference implements Expression {

  private final String text;

  private Position position;

  @Override
  public void process(Consumer<Node> operation) {
    operation.accept(this);
  }

  @Override
  public Type getType(Map<String, Variable> varMap) {
    return varMap.get(text).getType();
  }

  @Override
  public void push(MethodVisitor mv, Map<String, Variable> varMap) {
    final var variable =
        Optional.ofNullable(varMap.get(text))
            .orElseThrow(() -> new RuntimeException("Missing variable: " + text));

    mv.visitVarInsn(getType(varMap).getOpcode(ILOAD), variable.getIndex());
  }
}
