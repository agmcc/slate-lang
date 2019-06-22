package com.github.agmcc.slate.parser.ast;

import java.util.Set;

public interface TypeDeclaration extends Node {

  String getName();

  Set<String> getSuperTypes();
}
