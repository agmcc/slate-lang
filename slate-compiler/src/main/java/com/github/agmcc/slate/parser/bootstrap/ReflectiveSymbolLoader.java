package com.github.agmcc.slate.parser.bootstrap;

import com.github.agmcc.slate.parser.symbol.Field;
import com.github.agmcc.slate.parser.symbol.SymbolManager;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import javax.inject.Inject;

@BootstrapScope
class ReflectiveSymbolLoader {

  private SymbolManager symbolManager;

  @Inject
  public ReflectiveSymbolLoader(final SymbolManager symbolManager) {
    this.symbolManager = symbolManager;
  }

  public void loadSymbols(final Class<?> clazz) {
    loadAllFields(clazz);
  }

  private void loadAllFields(final Class<?> clazz) {
    Arrays.stream(clazz.getFields()).forEach(this::loadField);

    Arrays.stream(clazz.getDeclaredFields())
        .filter(f -> !Modifier.isPublic(f.getModifiers()))
        .forEach(this::loadField);
  }

  private void loadField(final java.lang.reflect.Field field) {
    final var name = getName(field);
    symbolManager.putField(
        name, Field.builder().name(name).modifiers(field.getModifiers()).build());
  }

  private String getName(final java.lang.reflect.Field field) {
    return String.join(".", field.getDeclaringClass().getCanonicalName(), field.getName());
  }
}
