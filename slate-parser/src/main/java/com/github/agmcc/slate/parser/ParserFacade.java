package com.github.agmcc.slate.parser;

import com.github.agmcc.slate.parser.ast.CompilationUnit;
import com.github.agmcc.slate.parser.bootstrap.BootstrapParser;
import com.github.agmcc.slate.parser.bootstrap.ClassLoaderModule;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class ParserFacade {

  // TODO: Should this be injectable?
  public static CompilationUnit parseCompilationUnit(
      final String src, final List<String> classUrls) {

    final var parserComponent =
        DaggerParserComponent.builder().antlrModule(new AntlrModule(src)).build();

    final BootstrapParser bootstrapParser =
        parserComponent
            .bootstrapParserComponent(new ClassLoaderModule(getClassUrls(classUrls)))
            .bootstrapParser();

    bootstrapParser.loadGlobalSymbols();

    System.out.println("Symbols: " + parserComponent.symbolManager());

    final var parser = parserComponent.slateParser();
    final var visitor = parserComponent.compilationUnitVisitor();

    final var cu = visitor.visitCompilationUnit(parser.compilationUnit());

    return cu;
  }

  private static URL[] getClassUrls(final List<String> classUrls) {
    return classUrls.stream()
        .map(
            s -> {
              try {
                return Paths.get(s).toUri().toURL();
              } catch (MalformedURLException e) {
                return null;
              }
            })
        .filter(Objects::nonNull)
        .toArray(URL[]::new);
  }
}
