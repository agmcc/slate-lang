package com.github.agmcc.slate.ast.statement;

import com.github.agmcc.slate.ast.Node;
import com.github.agmcc.slate.ast.Position;
import com.github.agmcc.slate.ast.expression.Expression;
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

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Assignment implements Statement {

  private final String varName;

  private final Expression value;

  private Position position;

  @Override
  public void process(Consumer<Node> operation) {
    operation.accept(this);
    value.process(operation);
  }

  @Override
  public void generate(MethodVisitor mv, Map<String, Variable> varMap) {
    final var variable = varMap.get(varName);
    value.pushAs(mv, varMap, variable.getType());
    mv.visitVarInsn(variable.getType().getOpcode(ISTORE), variable.getIndex());
  }
}
