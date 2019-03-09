package com.github.agmcc.slate.ast.statement;

import com.github.agmcc.slate.ast.Node;
import com.github.agmcc.slate.ast.Position;
import com.github.agmcc.slate.ast.expression.Expression;
import com.github.agmcc.slate.bytecode.Scope;
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
public class ForTraditional implements For {

  private final VarDeclaration init;

  private final Expression expression;

  private final Expression update;

  private final Statement body;

  private Position position;

  @Override
  public void process(Consumer<Node> operation) {
    operation.accept(this);
    init.process(operation);
    expression.process(operation);
    update.process(operation);
    body.process(operation);
  }

  @Override
  public void generate(MethodVisitor mv, Scope scope) {
    final var checkLabel = new Label();
    final var trueLabel = new Label();
    final var endLabel = new Label();

    // Declare loop variable
    init.generate(mv, scope);

    // Check expression
    mv.visitLabel(checkLabel);
    expression.push(mv, scope);
    mv.visitJumpInsn(IFGT, trueLabel);

    // False - exit loop
    mv.visitJumpInsn(GOTO, endLabel);

    // True - execute body and apply update
    mv.visitLabel(trueLabel);
    body.generate(mv, scope);
    update.push(mv, scope);
    mv.visitInsn(POP); // discard return value for update expression
    mv.visitJumpInsn(GOTO, checkLabel);

    mv.visitLabel(endLabel);
  }
}
