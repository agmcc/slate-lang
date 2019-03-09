package com.github.agmcc.slate.ast.statement;

import com.github.agmcc.slate.ast.Node;
import com.github.agmcc.slate.ast.Position;
import com.github.agmcc.slate.ast.expression.Expression;
import com.github.agmcc.slate.bytecode.Scope;
import com.github.agmcc.slate.bytecode.Variable;
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
public class VarDeclaration implements Statement {

  private final String varName;

  private final Expression value;

  private Position position;

  @Override
  public void process(Consumer<Node> operation) {
    operation.accept(this);
    Optional.ofNullable(value).orElseThrow().process(operation);
  }

  @Override
  public void generate(MethodVisitor mv, Scope scope) {
    final var type = Optional.ofNullable(value).orElseThrow().getType(scope);
    final var index = scope.getNextVarIndex(type);
    final var variable = new Variable(varName, type, index);

    scope.add(variable);

    value.pushAs(mv, scope, type);

    mv.visitVarInsn(type.getOpcode(ISTORE), index);
  }
}
