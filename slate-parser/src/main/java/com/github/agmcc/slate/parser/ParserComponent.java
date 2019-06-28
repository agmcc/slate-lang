package com.github.agmcc.slate.parser;

import com.github.agmcc.slate.parser.bootstrap.BootstrapParserComponent;
import com.github.agmcc.slate.parser.bootstrap.ClassLoaderModule;
import com.github.agmcc.slate.parser.symbol.SymbolManager;
import com.github.agmcc.slate.parser.visitor.CompilationUnitVisitor;
import dagger.Component;
import javax.inject.Singleton;

@Component(modules = {AntlrModule.class, ErrorHandlerModule.class})
@Singleton
public interface ParserComponent {

  SlateParser slateParser();

  CompilationUnitVisitor compilationUnitVisitor();

  SymbolManager symbolManager();

  BootstrapParserComponent bootstrapParserComponent(ClassLoaderModule module);
}
