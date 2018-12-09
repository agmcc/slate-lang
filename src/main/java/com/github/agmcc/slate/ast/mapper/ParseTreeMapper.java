package com.github.agmcc.slate.ast.mapper;

import com.github.agmcc.slate.ast.Node;
import org.antlr.v4.runtime.ParserRuleContext;

public interface ParseTreeMapper<T extends ParserRuleContext, U extends Node> {

  U toAst(T ctx);
}
