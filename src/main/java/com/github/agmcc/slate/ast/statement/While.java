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
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class While implements Statement {

  private final Expression expression;

  private final Statement body;

  private Position position;

  @Override
  public void process(Consumer<Node> operation) {
    operation.accept(this);
    expression.process(operation);
    body.process(operation);
  }

  @Override
  public void generate(MethodVisitor mv, Map<String, Variable> varMap) {
    final var startLabel = new Label();
    final var bodyLabel = new Label();
    final var endLabel = new Label();

    // Check condition
    mv.visitLabel(startLabel);
    expression.push(mv, varMap);
    mv.visitJumpInsn(IFGT, bodyLabel);

    // If false, exit
    mv.visitJumpInsn(GOTO, endLabel);

    // If true, execute body and jump back to condition
    mv.visitLabel(bodyLabel);
    body.generate(mv, varMap);
    mv.visitJumpInsn(GOTO, startLabel);

    mv.visitLabel(endLabel);
  }
}
