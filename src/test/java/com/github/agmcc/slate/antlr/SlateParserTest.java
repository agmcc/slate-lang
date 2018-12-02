package com.github.agmcc.slate.antlr;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import java.io.IOException;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

class SlateParserTest {

  @Test
  void testVarDeclaration() throws JSONException {
    // Given
    final var src = "var message = 'Hello, World!'";

    final var expected = yamlToJson(readResourceAsString("VarDeclaration.yml"));

    // When
    final var actual = parseTreeToJson(parseString(src));

    // Then
    // TODO: Use Hamcrest matchers
    JSONAssert.assertEquals(expected, actual, true);
  }

  @Test
  void testAssignment() throws JSONException {
    // Given
    final var src = "price = 9.95";

    final var expected = yamlToJson(readResourceAsString("Assignment.yml"));

    // When
    final var actual = parseTreeToJson(parseString(src));

    // Then
    JSONAssert.assertEquals(expected, actual, true);
  }

  @Test
  void testPrint() throws JSONException {
    // Given
    final var src = "print 100";

    final var expected = yamlToJson(readResourceAsString("Print.yml"));

    // When
    final var actual = parseTreeToJson(parseString(src));

    // Then
    JSONAssert.assertEquals(expected, actual, true);
  }

  @Test
  void testVarDeclarationComplex() throws JSONException {
    // Given
    final var src = "var sum = (10 - 6) * 4 / 2";

    final var expected = yamlToJson(readResourceAsString("VarDeclarationComplex.yml"));

    // When
    final var actual = parseTreeToJson(parseString(src));

    // Then
    JSONAssert.assertEquals(expected, actual, true);
  }

  // TODO: Put utility methods in separate classes

  private static ParserRuleContext parseString(String src) {
    final var lexer = new SlateLexer(CharStreams.fromString(src));
    final var tokenStream = new CommonTokenStream(lexer);
    final var parser = new SlateParser(tokenStream);
    return parser.compilationUnit();
  }

  private static String parseTreeToJson(ParseTree parseTree) {
    final var mapper = new ObjectMapper();

    final var module = new SimpleModule();
    module.addSerializer(ParseTree.class, new ParseTreeSerializer());
    mapper.registerModule(module);

    try {
      return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parseTree);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    return null;
  }

  private static class ParseTreeSerializer extends StdSerializer<ParseTree> {

    private ParseTreeSerializer() {
      this(null);
    }

    private ParseTreeSerializer(Class<ParseTree> t) {
      super(t);
    }

    @Override
    public void serialize(ParseTree value, JsonGenerator gen, SerializerProvider provider)
        throws IOException {
      generate(value, gen);
    }

    private void generate(ParseTree value, JsonGenerator gen) throws IOException {
      gen.writeStartObject();

      gen.writeStringField("class", value.getClass().getSimpleName());

      if (value instanceof TerminalNode) {
        gen.writeStringField("text", value.getText());
      }

      final var childCount = value.getChildCount();

      if (childCount > 0) {
        gen.writeFieldName("children");
        gen.writeStartArray();
        for (var i = 0; i < value.getChildCount(); i++) {
          var child = value.getChild(i);
          generate(child, gen);
        }
        gen.writeEndArray();
      }

      gen.writeEndObject();
    }
  }

  private static String yamlToJson(String yaml) {
    final var reader = new ObjectMapper(new YAMLFactory());
    Object obj = null;
    try {
      obj = reader.readValue(yaml, Object.class);
    } catch (IOException e) {
      e.printStackTrace();
    }

    final var writer = new ObjectMapper();
    try {
      return writer.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static String readResourceAsString(String path) {
    final var url = Resources.getResource(path);
    try {
      return Resources.toString(url, Charsets.UTF_8);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
