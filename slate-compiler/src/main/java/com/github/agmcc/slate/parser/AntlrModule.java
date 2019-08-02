package com.github.agmcc.slate.parser;

import dagger.Module;
import dagger.Provides;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

@Module
@AllArgsConstructor
class AntlrModule {

  private String src;

  @Provides
  CharStream provideCharStream() {
    try {
      return CharStreams.fromFileName(src);
    } catch (final IOException e) {
      throw new RuntimeException("Failed to read path: " + src, e);
    }
  }

  @Provides
  SlateLexer provideLexer(final CharStream charStream) {
    return new SlateLexer(charStream);
  }

  @Provides
  TokenStream provideTokenStream(final SlateLexer lexer) {
    return new CommonTokenStream(lexer);
  }

  @Provides
  SlateParser provideParser(
      final TokenStream tokenStream, final ANTLRErrorListener antlrErrorListener) {
    final var parser = new SlateParser(tokenStream);

    parser.removeErrorListeners();
    parser.addErrorListener(antlrErrorListener);

    return parser;
  }
}
