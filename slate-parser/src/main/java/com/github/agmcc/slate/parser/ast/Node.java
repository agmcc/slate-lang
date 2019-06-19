package com.github.agmcc.slate.parser.ast;

public interface Node {

  void accept(NodeVisitor visitor);
}
