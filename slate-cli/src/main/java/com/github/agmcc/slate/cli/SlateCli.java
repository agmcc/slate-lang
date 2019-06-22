package com.github.agmcc.slate.cli;

import com.github.agmcc.slate.compiler.SlateCompiler;
import com.github.agmcc.slate.compiler.SlateFile;
import com.github.agmcc.slate.parser.ParserFacade;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Collectors;

public class SlateCli {

  public static void main(final String[] args) {

    final var options = new Options();

    final var cliComponent =
        DaggerCliComponent.builder()
            .jCommanderModule(new JCommanderModule(options))
            .propertiesModule(new PropertiesModule("/app.properties"))
            .build();

    final var jCommander = cliComponent.jCommander();

    jCommander.parse(args);
    System.out.println("Options: " + options);

    if (options.isHelp()) {
      jCommander.usage();
      return;
    }

    final var compilationUnits =
        options
            .getSources()
            .stream()
            .map(ParserFacade::parseCompilationUnit)
            .collect(Collectors.toList());

    compilationUnits.forEach(System.out::println);

    compilationUnits
        .stream()
        .map(SlateCompiler::compileCompilationUnit)
        .forEach(
            f -> writeToFile(f, Optional.ofNullable(options.getOutput()).orElse(Paths.get(""))));
  }

  private static void writeToFile(final SlateFile slateFile, final Path rootDir) {

    final var path = rootDir.resolve(slateFile.getFilePath());

    try {
      final var parentDir = path.getParent();
      if (parentDir != null) {
        Files.createDirectories(parentDir);
      }

      if (!Files.exists(path)) {
        Files.createFile(path);
      }
      Files.write(path, slateFile.getData());
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }
}
