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

  @Test
  void testIfStatements() throws Exception {
    // Given
    final var src = getResourcePath("/If.slate").toString();

    final var expected =
        "5>3\n5>=5\n1<4\n1<=1\n3==3\n3!=2\n"
            + "3.1>3\n3.0>=3.0\n3.1>=3.0\n2.5<3.0\n2.5<=2.5\n2.5==2.5\n2.5!=3.0\n3.0!=2.5\n"
            + "1>0\n"
            + "true\nfalse\n"
            + "true\n";

    // When
    App.main(src);

    // Then
    assertTrue(Files.exists(getClassFilePath(src)));

    // When
    final var clazz = classLoader.loadClass("If");

    final var main = clazz.getMethod("main", String[].class);
    final var result = invoke(main, null, new Object[] {null});

    // Then
    assertNull(result.getReturnValue());
    assertEquals(expected, result.getStdOut());
  }

  @Test
  void testIfStatements_varRef() throws Exception {
    // Given
    final var src = getResourcePath("/IfVarRef.slate").toString();

    final var expected = "check\nvalid\n";

    // When
    App.main(src);

    // Then
    assertTrue(Files.exists(getClassFilePath(src)));

    // When
    final var clazz = classLoader.loadClass("IfVarRef");

    final var main = clazz.getMethod("main", String[].class);
    final var result = invoke(main, null, new Object[] {null});

    // Then
    assertNull(result.getReturnValue());
    assertEquals(expected, result.getStdOut());
  }

  @Test
  void testIfStatements_and() throws Exception {
    // Given
    final var src = getResourcePath("/IfAnd.slate").toString();

    final var expected = "3>1&&5<6\nfalse\nfalse\n3>1&&5<6&&4!=3\nfalse\n";

    // When
    App.main(src);

    // Then
    assertTrue(Files.exists(getClassFilePath(src)));

    // When
    final var clazz = classLoader.loadClass("IfAnd");

    final var main = clazz.getMethod("main", String[].class);
    final var result = invoke(main, null, new Object[] {null});

    // Then
    assertNull(result.getReturnValue());
    assertEquals(expected, result.getStdOut());
  }

  @Test
  void testIfStatements_or() throws Exception {
    // Given
    final var src = getResourcePath("/IfOr.slate").toString();

    final var expected = "true\ntrue\ntrue\nfalse\ntrue\ntrue\n";

    // When
    App.main(src);

    // Then
    assertTrue(Files.exists(getClassFilePath(src)));

    // When
    final var clazz = classLoader.loadClass("IfOr");

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
