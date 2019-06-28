package com.github.agmcc.slate.parser.bootstrap;

import com.github.agmcc.slate.parser.ErrorListener;
import com.github.agmcc.slate.parser.symbol.SymbolManager;
import com.google.common.reflect.ClassPath;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.inject.Inject;

@BootstrapScope
public class BootstrapParser {

  private SymbolManager symbolManager;

  private ReflectiveSymbolLoader reflectiveSymbolLoader;

  private ClassLoader classLoader;

  private ErrorListener errorListener;

  @Inject
  public BootstrapParser(
      final SymbolManager symbolManager,
      final ReflectiveSymbolLoader reflectiveSymbolLoader,
      final ClassLoader classLoader,
      final ErrorListener errorListener) {

    this.symbolManager = symbolManager;
    this.reflectiveSymbolLoader = reflectiveSymbolLoader;
    this.classLoader = classLoader;
    this.errorListener = errorListener;
  }

  public void loadGlobalSymbols() {
    symbolManager.pushScope();
    loadClasses().forEach(reflectiveSymbolLoader::loadSymbols);
  }

  @SuppressWarnings("UnstableApiUsage")
  private List<Class<?>> loadClasses() {
    try {
      return ClassPath.from(classLoader).getAllClasses().stream()
          .map(c -> loadClass(c.getName()))
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
    } catch (final IOException e) {
      e.printStackTrace();
    }
    return Collections.emptyList();
  }

  private Class<?> loadClass(final String name) {
    try {
      return classLoader.loadClass(name);
    } catch (final ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }
}
