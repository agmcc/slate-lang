package com.github.agmcc.slate.ast;

import com.github.agmcc.slate.ast.base.Node;
import com.github.agmcc.slate.ast.statement.Statement;
import java.util.List;
import lombok.Data;

@Data
public class CompilationUnit implements Node {

  private final List<Statement> statements;
}
