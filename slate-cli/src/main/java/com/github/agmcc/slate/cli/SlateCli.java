package com.github.agmcc.slate.cli;

import com.github.agmcc.slate.compiler.SlateCompiler;
import com.github.agmcc.slate.compiler.SlateFile;
import com.github.agmcc.slate.parser.ParserFacade;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

    if (options.isHelp()) {
      jCommander.usage();
      return;
    }

    System.out.println("Sources: " + options.getSources());
    System.out.println("Classes: " + options.getClasses());

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
        .forEach(SlateCli::writeToFile);
  }

  private static void writeToFile(final SlateFile slateFile) {
    try (final var os = new BufferedOutputStream(new FileOutputStream(slateFile.getFileName()))) {
      os.write(slateFile.getData());
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }
}
