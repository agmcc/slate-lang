package com.github.agmcc.slate.ast.statement;

import com.github.agmcc.slate.ast.Node;
import com.github.agmcc.slate.ast.Position;
import com.github.agmcc.slate.bytecode.Scope;
import java.util.List;
import java.util.function.Consumer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.objectweb.asm.MethodVisitor;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Block implements Statement {

  final List<Statement> statements;

  private Position position;

  private Scope scope;

  public Block(List<Statement> statements, Position position) {
    this.statements = statements;
    this.position = position;
  }

  @Override
  public void process(Consumer<Node> operation) {
    operation.accept(this);
    statements.forEach(s -> s.process(operation));
  }

  @Override
  public void generate(MethodVisitor mv, Scope scope) {
    final Scope blockScope = new Scope(scope);
    statements.forEach(s -> s.generate(mv, blockScope));
  }
}
