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

  @Test
  void testBlock() throws JSONException {
    // Given
    final var src = "{ print x }";
    final var expected = loadJsonFromYml("Block.yml");

    // When
    final var actual = jsonParseTree(src);

    // Then
    JSONAssert.assertEquals(expected, actual, true);
  }

  @Test
  void testBlock_empty() throws JSONException {
    // Given
    final var src = "{}";
    final var expected = loadJsonFromYml("Block_empty.yml");

    // When
    final var actual = jsonParseTree(src);

    // Then
    JSONAssert.assertEquals(expected, actual, true);
  }

  @Test
  void testBlock_multipleStatements() throws JSONException {
    // Given
    final var src = "{ var a = 1 a = 2}";
    final var expected = loadJsonFromYml("Block_multi.yml");

    // When
    final var actual = jsonParseTree(src);

    // Then
    JSONAssert.assertEquals(expected, actual, true);
  }

  @Test
  void testConditionStatement_if() throws JSONException {
    // Given
    final var src = "if true a = 1";
    final var expected = loadJsonFromYml("Condition.yml");

    // When
    final var actual = jsonParseTree(src);

    // Then
    JSONAssert.assertEquals(expected, actual, true);
  }

  @Test
  void testConditionStatement_if_else() throws JSONException {
    // Given
    final var src = "if true a = 1 else a = 2";
    final var expected = loadJsonFromYml("Condition_else.yml");

    // When
    final var actual = jsonParseTree(src);

    // Then
    JSONAssert.assertEquals(expected, actual, true);
  }

  @Test
  void testConditionStatement_if_else_if_else() throws JSONException {
    // Given
    final var src = "if true a = 1 else if false a = 2 else a = 3";
    final var expected = loadJsonFromYml("Condition_else_if.yml");

    // When
    final var actual = jsonParseTree(src);

    // Then
    JSONAssert.assertEquals(expected, actual, true);
  }

  @Test
  void testWhileLoop_statement() throws JSONException {
    // Given
    final var src = "while true print 'Looping'";
    final var expected = loadJsonFromYml("While.yml");

    // When
    final var actual = jsonParseTree(src);

    // Then
    JSONAssert.assertEquals(expected, actual, true);
  }

  @Test
  void testWhileLoop_block() throws JSONException {
    // Given
    final var src = "while true { print 'Looping' }";
    final var expected = loadJsonFromYml("While_block.yml");

    // When
    final var actual = jsonParseTree(src);

    // Then
    JSONAssert.assertEquals(expected, actual, true);
  }

  private String loadJsonFromYml(String resource) {
    return SerializationUtils.yamlToJson(FileUtils.readResourceAsString(resource));
  }

  private String jsonParseTree(String src) {
    return SerializationUtils.parseTreeToJson(ANTLRUtils.parseString(src));
  }
}
