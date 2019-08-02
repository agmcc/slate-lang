package com.github.agmcc.slate.parser;

import com.github.agmcc.slate.parser.ast.CompilationUnit;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class ParserFacade {

  // TODO: Should this be injectable?
  public static CompilationUnit parseCompilationUnit(
      final String src, final List<String> classUrls) {
    //
    //    final var parserComponent =
    //        DaggerParserComponent.builder().antlrModule(new AntlrModule(src)).build();
    //
    //    final var parser = parserComponent.slateParser();
    //
    //    final CompilationUnitContext compilationUnitContext = parser.compilationUnit();
    //
    //    // Bootstrap
    //    // TODO: This code is in the wrong place as Bootstrap should only run once per compilation
    // not
    //    // per file
    //    final var bootstrapParserComponent =
    //        parserComponent.bootstrapParserComponent(new
    // ClassLoaderModule(getClassUrls(classUrls)));
    //
    //    bootstrapParserComponent.bootstrapParser().loadGlobalSymbols(compilationUnitContext);
    //
    //    // AST
    //    final var visitor = parserComponent.compilationUnitVisitor();
    //
    //    // TODO: Should vistor even have param or should ctx just accept it?
    //    final var cu = visitor.visitCompilationUnit(compilationUnitContext);

    return null;
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
