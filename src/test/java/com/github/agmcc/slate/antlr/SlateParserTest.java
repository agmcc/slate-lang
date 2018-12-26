package com.github.agmcc.slate.antlr;

import com.github.agmcc.slate.test.ANTLRUtils;
import com.github.agmcc.slate.test.FileUtils;
import com.github.agmcc.slate.test.SerializationUtils;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

class SlateParserTest {

  @Test
  void testVarDeclaration() throws JSONException {
    // Given
    final var src = "var message = 'Hello, World!'";
    final var expected = loadJsonFromYml("VarDeclaration.yml");

    // When
    final var actual = jsonParseTree(src);

    // Then
    // TODO: Use Hamcrest matchers
    JSONAssert.assertEquals(expected, actual, true);
  }

  @Test
  void testAssignment() throws JSONException {
    // Given
    final var src = "price = 9.95";
    final var expected = loadJsonFromYml("Assignment.yml");

    // When
    final var actual = jsonParseTree(src);

    // Then
    JSONAssert.assertEquals(expected, actual, true);
  }

  @Test
  void testPrint() throws JSONException {
    // Given
    final var src = "print 100";
    final var expected = loadJsonFromYml("Print.yml");

    // When
    final var actual = jsonParseTree(src);

    // Then
    JSONAssert.assertEquals(expected, actual, true);
  }

  @Test
  void testVarDeclarationComplex() throws JSONException {
    // Given
    final var src = "var sum = (10 - 6) * 4 / 2";
    final var expected = loadJsonFromYml("VarDeclarationComplex.yml");

    // When
    final var actual = jsonParseTree(src);

    // Then
    JSONAssert.assertEquals(expected, actual, true);
  }

  // TODO: Block tests

  private String loadJsonFromYml(String resource) {
    return SerializationUtils.yamlToJson(FileUtils.readResourceAsString(resource));
  }

  private String jsonParseTree(String src) {
    return SerializationUtils.parseTreeToJson(ANTLRUtils.parseString(src));
  }
}
