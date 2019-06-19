package com.github.agmcc.slate.parser;

import com.github.agmcc.slate.parser.ast.CompilationUnit;

public class ParserFacade {

  // TODO: Should this be injectable?
  public static CompilationUnit parseCompilationUnit(final String src) {
    final var parserComponent =
        DaggerParserComponent.builder().antlrModule(new AntlrModule(src)).build();

    final var parser = parserComponent.slateParser();
    final var visitor = parserComponent.compilationUnitVisitor();

    return visitor.visitCompilationUnit(parser.compilationUnit());
  }
}
