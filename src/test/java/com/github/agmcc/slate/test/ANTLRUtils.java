package com.github.agmcc.slate.test;

import static org.antlr.v4.runtime.Lexer.DEFAULT_TOKEN_CHANNEL;

import com.github.agmcc.slate.antlr.SlateLexer;
import com.github.agmcc.slate.antlr.SlateParser;
import java.util.List;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public final class ANTLRUtils {

  private ANTLRUtils() {
    /* Static Access */
  }

  public static List<Integer> getTokenTypes(String src) {
    return getTokenTypes(src, DEFAULT_TOKEN_CHANNEL);
  }

  public static List<Integer> getTokenTypes(String src, int channel) {
    return new SlateLexer(CharStreams.fromString(src))
        .getAllTokens()
        .stream()
        .filter(t -> t.getChannel() == channel)
        .map(Token::getType)
        .collect(Collectors.toList());
  }

  public static ParserRuleContext parseString(String src) {
    final var lexer = new SlateLexer(CharStreams.fromString(src));
    final var tokenStream = new CommonTokenStream(lexer);
    final var parser = new SlateParser(tokenStream);
    return parser.compilationUnit();
  }
}
