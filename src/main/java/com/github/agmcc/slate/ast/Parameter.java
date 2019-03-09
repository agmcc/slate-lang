package com.github.agmcc.slate.ast;

import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.objectweb.asm.Type;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Parameter implements Node {

  private final Type type;

  private final String value;

  private Position position;

  @Override
  public void process(Consumer<Node> operation) {
    operation.accept(this);
  }
}
