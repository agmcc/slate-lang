package com.github.agmcc.slate.antlr;

import com.github.agmcc.slate.test.ANTLRUtils;
import com.github.agmcc.slate.test.FileUtils;
import com.github.agmcc.slate.test.SerializationUtils;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

class SlateParserTest {

  private static final String DEFAULT_METHOD_TEMPLATE = "test { %s return }";

  @Test
  void testVarDeclaration() throws JSONException {
    // Given
    final var src = createMethodSrc("var message = 'Hello, World!'");
    final var expected = loadJsonFromYml("antlr/vardec/var_declaration.yml");

    // When
    final var actual = jsonParseTree(src);

    // Then
    JSONAssert.assertEquals(expected, actual, true);
  }

  @Test
  void testAssignment() throws JSONException {
    // Given
    final var src = createMethodSrc("price = 9.95");
    final var expected = loadJsonFromYml("antlr/assignment/assignment.yml");

    // When
    final var actual = jsonParseTree(src);

    // Then
    JSONAssert.assertEquals(expected, actual, true);
  }

  @Test
  void testPrint() throws JSONException {
    // Given
    final var src = createMethodSrc("print 100");
    final var expected = loadJsonFromYml("antlr/print/print.yml");

    // When
    final var actual = jsonParseTree(src);

    // Then
    JSONAssert.assertEquals(expected, actual, true);
  }

  @Test
  void testVarDeclarationComplex() throws JSONException {
    // Given
    final var src = createMethodSrc("var sum = (10 - 6) * 4 / 2");
    final var expected = loadJsonFromYml("antlr/vardec/var_declaration_complex.yml");

    // When
    final var actual = jsonParseTree(src);

    // Then
    JSONAssert.assertEquals(expected, actual, true);
  }

  @Test
  void testBlock() throws JSONException {
    // Given
    final var src = createMethodSrc("print x"); // Block in method src
    final var expected = loadJsonFromYml("antlr/block/block.yml");

    // When
    final var actual = jsonParseTree(src);

    // Then
    JSONAssert.assertEquals(expected, actual, true);
  }

  @Test
  void testBlock_empty() throws JSONException {
    // Given
    final var src = createMethodSrc(""); // Block in method src
    final var expected = loadJsonFromYml("antlr/block/block_empty.yml");

    // When
    final var actual = jsonParseTree(src);

    // Then
    JSONAssert.assertEquals(expected, actual, true);
  }

  @Test
  void testBlock_multipleStatements() throws JSONException {
    // Given
    final var src = createMethodSrc("var a = 1 a = 2"); // Block in method src
    final var expected = loadJsonFromYml("antlr/block/block_multi.yml");

    // When
    final var actual = jsonParseTree(src);

    // Then
    JSONAssert.assertEquals(expected, actual, true);
  }

  @Test
  void testConditionStatement_if() throws JSONException {
    // Given
    final var src = createMethodSrc("if true a = 1");
    final var expected = loadJsonFromYml("antlr/condition/condition.yml");

    // When
    final var actual = jsonParseTree(src);

    // Then
    JSONAssert.assertEquals(expected, actual, true);
  }

  @Test
  void testConditionStatement_if_else() throws JSONException {
    // Given
    final var src = createMethodSrc("if true a = 1 else a = 2");
    final var expected = loadJsonFromYml("antlr/condition/condition_else.yml");

    // When
    final var actual = jsonParseTree(src);

    // Then
    JSONAssert.assertEquals(expected, actual, true);
  }

  @Test
  void testConditionStatement_if_else_if_else() throws JSONException {
    // Given
    final var src = createMethodSrc("if true a = 1 else if false a = 2 else a = 3");
    final var expected = loadJsonFromYml("antlr/condition/condition_else_if.yml");

    // When
    final var actual = jsonParseTree(src);

    // Then
    JSONAssert.assertEquals(expected, actual, true);
  }

  @Test
  void testWhileLoop_statement() throws JSONException {
    // Given
    final var src = createMethodSrc("while true print 'Looping'");
    final var expected = loadJsonFromYml("antlr/while/while.yml");

    // When
    final var actual = jsonParseTree(src);

    // Then
    JSONAssert.assertEquals(expected, actual, true);
  }

  @Test
  void testWhileLoop_block() throws JSONException {
    // Given
    final var src = createMethodSrc("while true { print 'Looping' }");
    final var expected = loadJsonFromYml("antlr/while/while_block.yml");

    // When
    final var actual = jsonParseTree(src);

    // Then
    JSONAssert.assertEquals(expected, actual, true);
  }

  @Test
  void testForTraditional() throws JSONException {
    // Given
    final var src = createMethodSrc("for var i = 0 i < 5 i++ print i");
    final var expected = loadJsonFromYml("antlr/for/for_traditional.yml");

    // When
    final var actual = jsonParseTree(src);

    // Then
    JSONAssert.assertEquals(expected, actual, true);
  }

  @Test
  void testForTraditional_block() throws JSONException {
    // Given
    final var src = createMethodSrc("for var i = 0 i < 5 i++ { print i }");
    final var expected = loadJsonFromYml("antlr/for/for_traditional_block.yml");

    // When
    final var actual = jsonParseTree(src);

    // Then
    JSONAssert.assertEquals(expected, actual, true);
  }

  /*
  Helper methods
   */

  private String loadJsonFromYml(String resource) {
    return SerializationUtils.yamlToJson(FileUtils.readResourceAsString(resource));
  }

  private String jsonParseTree(String src) {
    return SerializationUtils.parseTreeToJson(ANTLRUtils.parseString(src));
  }

  private String createMethodSrc(String body) {
    return String.format(DEFAULT_METHOD_TEMPLATE, body);
  }
}
