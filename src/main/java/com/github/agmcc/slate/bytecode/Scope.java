package com.github.agmcc.slate.bytecode;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.ToString;
import org.objectweb.asm.Type;

@ToString
public class Scope {

  private final AtomicInteger varIndex;

  private final Map<String, Variable> varMap;

  private final Map<String, Method> methodMap;

  public Scope(Scope parent) {
    if (parent != null) {
      varIndex = new AtomicInteger(parent.varIndex.get());
      varMap = new HashMap<>(parent.varMap);
      methodMap = new HashMap<>(parent.methodMap);
    } else {
      varIndex = new AtomicInteger();
      varMap = new HashMap<>();
      methodMap = new HashMap<>();
    }
  }

  public void add(Variable variable) {
    final String key = variable.getName();

    if (varMap.containsKey(key)) {
      throw new IllegalArgumentException(String.format(Errors.DUPLICATE_VAR_TEMPLATE, key));
    }

    varMap.put(key, variable);
  }

  public void add(Method method) {
    final String key = method.getName();

    if (methodMap.containsKey(key)) {
      throw new IllegalArgumentException(String.format(Errors.DUPLICATE_METHOD_TEMPLATE, key));
    }

    methodMap.put(key, method);
  }

  public Variable getVariable(String name) {
    return Optional.ofNullable(varMap.get(name))
        .orElseThrow(
            () -> new NoSuchElementException(String.format(Errors.MISSING_VAR_TEMPLATE, name)));
  }

  public Method getMethod(String name) {
    return Optional.ofNullable(methodMap.get(name))
        .orElseThrow(
            () -> new NoSuchElementException(String.format(Errors.MISSING_METHOD_TEMPLATE, name)));
  }

  public int getNextVarIndex(Type type) {
    return varIndex.getAndAdd(getIndexIncrement(type));
  }

  private int getIndexIncrement(Type type) {
    return type.equals(Type.DOUBLE_TYPE) ? 2 : 1;
  }

  private static class Errors {

    private static final String DUPLICATE_VAR_TEMPLATE = "Variable '%s' already exists in scope";

    private static final String DUPLICATE_METHOD_TEMPLATE = "Method '%s' already exists in scope";

    private static final String MISSING_VAR_TEMPLATE = "Variable '%s' doesn't exist in scope";

    private static final String MISSING_METHOD_TEMPLATE = "Method '%s' doesn't exist in scope";
  }
}
