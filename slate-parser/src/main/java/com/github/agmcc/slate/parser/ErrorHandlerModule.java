package com.github.agmcc.slate.parser;

import dagger.Module;
import dagger.Provides;
import org.antlr.v4.runtime.ANTLRErrorListener;

@Module
class ErrorHandlerModule {

  @Provides
  ANTLRErrorListener provideAntlrErrorListener() {
    return new ErrorListener();
  }
}
