package com.github.agmcc.slate.ast.expression;

import com.github.agmcc.slate.ast.Node;
import com.github.agmcc.slate.bytecode.Variable;
import java.util.Map;
import java.util.function.Consumer;
import org.objectweb.asm.Type;

public interface IncrementDecrement extends Expression {

  String getText();

  @Override
  default void process(Consumer<Node> operation) {
    operation.accept(this);
  }

  @Override
  default Type getType(Map<String, Variable> varMap) {
    return varMap.get(getText()).getType();
  }
}
