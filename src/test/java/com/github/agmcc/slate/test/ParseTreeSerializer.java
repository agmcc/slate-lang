package com.github.agmcc.slate.test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public class ParseTreeSerializer extends StdSerializer<ParseTree> {

  ParseTreeSerializer() {
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
