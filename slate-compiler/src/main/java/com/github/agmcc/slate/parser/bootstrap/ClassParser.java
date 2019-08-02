package com.github.agmcc.slate.parser.bootstrap;

import com.google.common.reflect.ClassPath;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.inject.Inject;

@BootstrapScope
class ClassParser {

  private ClassLoader classLoader;

  private ReflectiveSymbolLoader reflectiveSymbolLoader;

  @Inject
  public ClassParser(
      final ClassLoader classLoader, final ReflectiveSymbolLoader reflectiveSymbolLoader) {
    this.classLoader = classLoader;
    this.reflectiveSymbolLoader = reflectiveSymbolLoader;
  }

  public void loadClassSymbols() {
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
      throw new RuntimeException(e);
    }
  }
}
