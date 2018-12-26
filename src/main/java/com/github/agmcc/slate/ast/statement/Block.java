package com.github.agmcc.slate.ast.statement;

import com.github.agmcc.slate.ast.Node;
import com.github.agmcc.slate.ast.Position;
import com.github.agmcc.slate.bytecode.Variable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import lombok.*;
import org.objectweb.asm.MethodVisitor;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Block implements Statement {

  final List<Statement> statements;

  private Position position;

  @Override
  public void process(Consumer<Node> operation) {
    operation.accept(this);
    statements.forEach(s -> s.process(operation));
  }

  @Override
  public void generate(MethodVisitor mv, Map<String, Variable> varMap) {
    statements.forEach(s -> s.generate(mv, varMap));
  }
}
