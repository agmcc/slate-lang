package com.github.agmcc.slate.ast;

import java.util.function.Consumer;

public interface Node {

  void process(Consumer<Node> operation);

  @SuppressWarnings("unchecked")
  default <T extends Node> void specificProcess(Class<T> clazz, Consumer<T> operation) {
    process(
        n -> {
          if (clazz.isAssignableFrom(n.getClass())) {
            operation.accept((T) n);
          }
        });
  }

  Position getPosition();

  void setPosition(Position position);
}
