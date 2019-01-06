package com.github.agmcc.slate.test;

import static com.github.agmcc.slate.test.FileUtils.normaliseLineSeparators;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class IntegrationTestUtils {

  private IntegrationTestUtils() {
    /* Static access */
  }

  public static ClassLoader getResourceClassLoader() throws Exception {
    final var resourceUri = IntegrationTestUtils.class.getResource("/").toURI();
    return new URLClassLoader(new URL[] {resourceUri.toURL()});
  }

  public static Path getResourcePath(String name) throws URISyntaxException {
    return Paths.get(IntegrationTestUtils.class.getResource(name).toURI());
  }

  public static InvocationResult invoke(Method method, Object object, Object[] args)
      throws Exception {
    try (final var os = new ByteArrayOutputStream();
        final var out = new PrintStream(os)) {
      final var defaultOut = System.out;
      System.setOut(out);
      final var result = method.invoke(object, args);
      System.setOut(defaultOut);
      return new InvocationResult(result, normaliseLineSeparators(os.toString()));
    }
  }
}
