package com.github.agmcc.slate.ast;

import java.util.List;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CompilationUnit implements Node {

  private final List<MethodDeclaration> methods;

  private Position position;

  @Override
  public void process(Consumer<Node> operation) {
    operation.accept(this);
    methods.forEach(s -> s.process(operation));
  }
}
