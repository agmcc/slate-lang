package com.github.agmcc.slate.ast.validator;

import com.github.agmcc.slate.ast.statement.VarDeclaration;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
public class Scope {

  private final Map<String, VarDeclaration> varMap = new HashMap<>();

  private final Scope parent;

  public void add(VarDeclaration variable) {
    varMap.put(variable.getVarName(), variable);
  }

  public Map<String, VarDeclaration> resolve() {
    final var resolved = new HashMap<>(varMap);
    if (parent != null) {
      resolved.putAll(parent.resolve());
    }
    return resolved;
  }
}
