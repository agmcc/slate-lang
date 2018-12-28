package com.github.agmcc.slate;

import static com.github.agmcc.slate.test.IntegrationTestUtils.getResourceClassLoader;
import static com.github.agmcc.slate.test.IntegrationTestUtils.getResourcePath;
import static com.github.agmcc.slate.test.IntegrationTestUtils.invoke;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SlateIT {

  private static final String SOURCE_EXT = ".slate";

  private static final String CLASS_EXT = ".class";

  private static ClassLoader classLoader;

  @BeforeAll
  static void setup() throws Exception {
    classLoader = getResourceClassLoader();
  }

  @Test
  void testHello() throws Exception {
    // Given
    final var src = getResourcePath("/Hello.slate").toString();

    final var expected = "Hello, Slate!\n";

    // When
    App.main(src);

    // Then
    assertTrue(Files.exists(getClassFilePath(src)));

    // When
    final var clazz = classLoader.loadClass("Hello");

    final var main = clazz.getMethod("main", String[].class);
    final var result = invoke(main, null, new Object[] {null});

    // Then
    assertNull(result.getReturnValue());
    assertEquals(expected, result.getStdOut());
  }

  @Test
  void testBlocks() throws Exception {
    // Given
    final var src = getResourcePath("/Blocks.slate").toString();

    final var expected = "1\n2\n3\n";

    // When
    App.main(src);

    // Then
    assertTrue(Files.exists(getClassFilePath(src)));

    // When
    final var clazz = classLoader.loadClass("Blocks");

    final var main = clazz.getMethod("main", String[].class);
    final var result = invoke(main, null, new Object[] {null});

    // Then
    assertNull(result.getReturnValue());
    assertEquals(expected, result.getStdOut());
  }

  @Test
  void testBlocks_invalid() throws Exception {
    // Given
    final var src = getResourcePath("/BlocksInvalid.slate").toString();

    final var expected = "No variable named 'inner' at [4,6]";

    // When
    final var e = assertThrows(ValidationException.class, () -> App.main(src));
    assertEquals(expected, e.getMessage());

    assertTrue(Files.notExists(getClassFilePath(src)));
  }

  @Test
  void testOperators() throws Exception {
    // Given
    final var src = getResourcePath("/Operators.slate").toString();

    final var expected = "4\n4.5\n2\n1.5\n90\n66.5\n12\n12.5\n46\n";

    // When
    App.main(src);

    // Then
    assertTrue(Files.exists(getClassFilePath(src)));

    // When
    final var clazz = classLoader.loadClass("Operators");

    final var main = clazz.getMethod("main", String[].class);
    final var result = invoke(main, null, new Object[] {null});

    // Then
    assertNull(result.getReturnValue());
    assertEquals(expected, result.getStdOut());
  }

  private Path getClassFilePath(String srcFile) {
    final var classPathStr = srcFile.replace(SOURCE_EXT, CLASS_EXT);
    return Paths.get(classPathStr);
  }
}
