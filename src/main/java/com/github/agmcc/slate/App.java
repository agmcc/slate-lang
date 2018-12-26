package com.github.agmcc.slate;

import com.github.agmcc.slate.antlr.SlateLexer;
import com.github.agmcc.slate.antlr.SlateParser;
import com.github.agmcc.slate.ast.mapper.ParseTreeMapperImpl;
import com.github.agmcc.slate.ast.validator.Validator;
import com.github.agmcc.slate.bytecode.JvmCompiler;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class App {

  private static final String SOURCE_EXTENSION = ".slate";

  private static final String CLASS_EXTENSION = ".class";

  public static void main(String... args) throws IOException {
    if (args.length == 0) {
      throw new IllegalArgumentException("Missing source file");
    }

    final var source = args[0];

    if (!source.endsWith(SOURCE_EXTENSION)) {
      throw new IllegalArgumentException(
          "Invalid source file extension: Expected " + SOURCE_EXTENSION);
    }

    // Parse
    final var lexer = new SlateLexer(CharStreams.fromFileName(source));
    final var tokenStream = new CommonTokenStream(lexer);
    final var parser = new SlateParser(tokenStream);
    final var compilationUnitContext = parser.compilationUnit();

    // Mapping
    final var mapper = new ParseTreeMapperImpl();
    mapper.setConsiderPosition(true);
    final var compilationUnit = mapper.toAst(compilationUnitContext);

    // Validate
    final var validator = new Validator();
    final var errors = validator.validate(compilationUnit);

    if (!errors.isEmpty()) {
      errors.forEach(e -> System.err.println(e.getMessage()));
      System.exit(-1);
    }

    // Compiler
    final var compiler = new JvmCompiler();

    final var className = source.substring(0, source.lastIndexOf("."));

    final var bytes = compiler.compile(compilationUnit, className);

    // Write file
    try (final var os =
        new BufferedOutputStream(new FileOutputStream(className.concat(CLASS_EXTENSION)))) {
      os.write(bytes);
    }
  }
}
