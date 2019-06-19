package com.github.agmcc.slate.parser;

import com.github.agmcc.slate.parser.visitor.CompilationUnitVisitor;
import dagger.Component;

@Component(modules = {AntlrModule.class, ErrorHandlerModule.class})
public interface ParserComponent {

  SlateParser slateParser();

  CompilationUnitVisitor compilationUnitVisitor();
}
