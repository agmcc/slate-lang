package com.github.agmcc.slate.ast.statement;

import com.github.agmcc.slate.ast.Node;
import com.github.agmcc.slate.ast.Position;
import com.github.agmcc.slate.ast.expression.Expression;
import com.github.agmcc.slate.bytecode.Scope;
import java.util.Optional;
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
public class Return implements Statement {

  private final Expression value;

  private Position position;

  @Override
  public void process(Consumer<Node> operation) {
    operation.accept(this);
    Optional.ofNullable(value).ifPresent(v -> v.process(operation));
  }

  @Override
  public void generate(MethodVisitor mv, Scope scope) {
    if (value != null) {
      value.push(mv, scope);
      mv.visitInsn(value.getType(scope).getOpcode(IRETURN));
    } else {
      mv.visitInsn(RETURN);
    }
  }
}
