package com.github.agmcc.slate.parser.bootstrap;

import com.github.agmcc.slate.parser.SlateParser.CompilationUnitContext;
import com.github.agmcc.slate.parser.symbol.SymbolManager;
import javax.inject.Inject;

@BootstrapScope
public class BootstrapParser {

  private SymbolManager symbolManager;

  private SourceVisitor sourceVisitor;

  private ClassParser classParser;

  @Inject
  public BootstrapParser(
      final SymbolManager symbolManager,
      final SourceVisitor sourceVisitor,
      final ClassParser classParser) {

    this.symbolManager = symbolManager;
    this.sourceVisitor = sourceVisitor;
    this.classParser = classParser;
  }

  // TODO: Might need a separate parser for this?
  public void loadGlobalSymbols(final CompilationUnitContext ctx) {
    symbolManager.pushScope();

    ctx.accept(sourceVisitor);

    classParser.loadClassSymbols();
  }
}
