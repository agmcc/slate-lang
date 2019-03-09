package com.github.agmcc.slate.bytecode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.objectweb.asm.Type;

class ScopeTest {

  static Stream<Type> singleTypeProvider() {
    return Stream.of(Type.INT_TYPE, Type.BOOLEAN_TYPE, Type.getType(String.class));
  }

  static Stream<Type> doubleTypeProvider() {
    return Stream.of(Type.DOUBLE_TYPE);
  }

  @Test
  void testAddAndRetrieveVariable() {
    // Given
    final var variable = new Variable("a", Type.INT_TYPE, 0);
    final var scope = new Scope(null);

    // When
    scope.add(variable);
    final var saved = scope.getVariable(variable.getName());

    // Then
    assertNotNull(saved);
    assertEquals(variable, saved);
  }

  @Test
  void testAddAndRetrieveVariable_parent() {
    // Given
    final var parent = new Scope(null);
    final var variable = new Variable("a", Type.INT_TYPE, 0);
    parent.add(variable);

    // When
    final var scope = new Scope(parent);
    final var saved = scope.getVariable(variable.getName());

    // Then
    assertNotNull(saved);
    assertEquals(variable, saved);
  }

  @Test
  void testAddAndRetrieveVariable_grandParent() {
    // Given
    final var grandparent = new Scope(null);
    final var variable = new Variable("a", Type.INT_TYPE, 0);
    grandparent.add(variable);

    final var parent = new Scope(grandparent);

    // When
    final var scope = new Scope(parent);
    final var saved = scope.getVariable(variable.getName());

    // Then
    assertNotNull(saved);
    assertEquals(variable, saved);
  }

  @Test
  void testAddAndRetrieveVariable_grandParentMixed() {
    // Given
    final var grandparent = new Scope(null);

    final var parent = new Scope(grandparent);
    final var variable = new Variable("a", Type.INT_TYPE, 0);
    parent.add(variable);

    // When
    final var scope = new Scope(parent);
    final var saved = scope.getVariable(variable.getName());

    // Then
    assertNotNull(saved);
    assertEquals(variable, saved);
  }

  @Test
  void testAddVariable_alreadyExists() {
    // Given
    final var variable = new Variable("a", Type.INT_TYPE, 0);
    final var scope = new Scope(null);

    scope.add(variable);

    // When
    final var e = assertThrows(IllegalArgumentException.class, () -> scope.add(variable));
    assertEquals("Variable 'a' already exists in scope", e.getMessage());
  }

  @Test
  void testGetVariable_notExist() {
    // Given
    final var scope = new Scope(null);

    // When
    final var e = assertThrows(NoSuchElementException.class, () -> scope.getVariable("unknown"));
    assertEquals("Variable 'unknown' doesn't exist in scope", e.getMessage());
  }

  @Test
  void testAddAndRetrieveMethod() {
    // Given
    final var method = new Method("calc", Type.VOID_TYPE, "Test");
    final var scope = new Scope(null);

    // When
    scope.add(method);
    final var saved = scope.getMethod(method.getName());

    // Then
    assertNotNull(saved);
    assertEquals(method, saved);
  }

  @Test
  void testAddAndRetrieveMethod_parent() {
    // Given
    final var parent = new Scope(null);
    final var method = new Method("calc", Type.VOID_TYPE, "Test");
    parent.add(method);

    // When
    final var scope = new Scope(parent);
    final var saved = scope.getMethod(method.getName());

    // Then
    assertNotNull(saved);
    assertEquals(method, saved);
  }

  @Test
  void testAddAndRetrieveMethod_grandParent() {
    // Given
    final var grandparent = new Scope(null);
    final var method = new Method("calc", Type.VOID_TYPE, "Test");
    grandparent.add(method);

    final var parent = new Scope(grandparent);

    // When
    final var scope = new Scope(parent);
    final var saved = scope.getMethod(method.getName());

    // Then
    assertNotNull(saved);
    assertEquals(method, saved);
  }

  @Test
  void testAddAndRetrieveMethod_grandParentMixed() {
    // Given
    final var grandparent = new Scope(null);

    final var parent = new Scope(grandparent);
    final var method = new Method("calc", Type.VOID_TYPE, "Test");
    parent.add(method);

    // When
    final var scope = new Scope(parent);
    final var saved = scope.getMethod(method.getName());

    // Then
    assertNotNull(saved);
    assertEquals(method, saved);
  }

  @Test
  void testAddMethod_alreadyExists() {
    // Given
    final var method = new Method("calc", Type.VOID_TYPE, "Test");
    final var scope = new Scope(null);

    scope.add(method);

    // When
    final var e = assertThrows(IllegalArgumentException.class, () -> scope.add(method));
    assertEquals("Method 'calc' already exists in scope", e.getMessage());
  }

  @Test
  void testGetMethod_notExist() {
    // Given
    final var scope = new Scope(null);

    // When
    final var e = assertThrows(NoSuchElementException.class, () -> scope.getMethod("unknown"));
    assertEquals("Method 'unknown' doesn't exist in scope", e.getMessage());
  }

  @ParameterizedTest
  @MethodSource("singleTypeProvider")
  void testGetNextVarIndex_initialSinglePrecision(Type type) {
    // Given
    final var scope = new Scope(null);

    // When
    final var index = scope.getNextVarIndex(type);

    // Then
    assertEquals(0, index);
  }

  @ParameterizedTest
  @MethodSource("singleTypeProvider")
  void testGetNextVarIndex_multipleSinglePrecision(Type type) {
    // Given
    final var scope = new Scope(null);

    scope.getNextVarIndex(type);

    // When
    final var second = scope.getNextVarIndex(type);

    // Then
    assertEquals(1, second);
  }

  @ParameterizedTest
  @MethodSource("doubleTypeProvider")
  void testGetNextVarIndex_initialDoublePrecision(Type type) {
    // Given
    final var scope = new Scope(null);

    // When
    final var index = scope.getNextVarIndex(type);

    // Then
    assertEquals(0, index);
  }

  @ParameterizedTest
  @MethodSource("doubleTypeProvider")
  void testGetNextVarIndex_multipleDoublePrecision(Type type) {
    // Given
    final var scope = new Scope(null);
    scope.getNextVarIndex(type);

    // When
    final var second = scope.getNextVarIndex(type);

    // Then
    assertEquals(2, second);
  }
}
