package com.github.agmcc.slate.parser.symbol;

import com.github.agmcc.slate.parser.ErrorListener;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.ToString;

@Singleton
@ToString(of = "scopes")
public class SymbolManager {

  private final Deque<Scope> scopes = new ArrayDeque<>();

  private final ErrorListener errorListener;

  @Inject
  public SymbolManager(final ErrorListener errorListener) {
    this.errorListener = errorListener;
  }

  public void pushScope() {
    scopes.addFirst(new Scope());
  }

  public void popScope() {
    scopes.pollFirst();
  }

  public Field getField(final String name) {
    return lookupSymbol(name, Field.class);
  }

  public Method getMethod(final String name) {
    return lookupSymbol(name, Method.class);
  }

  @SuppressWarnings("unchecked")
  private <T extends Symbol> T lookupSymbol(final String name, final Class<T> symbolClass) {

    for (final var scope : scopes) {
      final var lookupMap = scope.symbolMapMap.get(symbolClass);
      final var symbol = (T) lookupMap.get(name);

      if (symbol != null) {
        return symbol;
      }
    }
    // TODO
    throw new RuntimeException("Unable to find symbol: " + name);
  }

  public void putField(final String name, final Field field) {
    // Error handling
    scopes.peekFirst().fields.put(name, field);
  }

  @Getter
  @ToString(of = {"fields", "methods"})
  private class Scope {

    private Map<String, Field> fields = new HashMap<>();

    private Map<String, Method> methods = new HashMap<>();

    private Map<Class<? extends Symbol>, Map<String, ? extends Symbol>> symbolMapMap =
        new HashMap<>();

    private Scope() {
      symbolMapMap.put(Field.class, fields);
      symbolMapMap.put(Method.class, methods);
    }
  }
}
