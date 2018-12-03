package com.github.agmcc.slate.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.IOException;
import org.antlr.v4.runtime.tree.ParseTree;

public final class SerializationUtils {

  private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

  private static final ObjectMapper YAML_MAPPER = new ObjectMapper(new YAMLFactory());

  static {
    final var module = new SimpleModule();
    module.addSerializer(ParseTree.class, new ParseTreeSerializer());
    JSON_MAPPER.registerModule(module);
  }

  private SerializationUtils() {
    /* Static Access */
  }

  public static String parseTreeToJson(ParseTree parseTree) {
    try {
      return JSON_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(parseTree);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String yamlToJson(String yaml) {
    try {
      final var obj = YAML_MAPPER.readValue(yaml, Object.class);
      return JSON_MAPPER.writeValueAsString(obj);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
