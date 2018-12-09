package com.github.agmcc.slate.ast;

import com.github.agmcc.slate.ast.statement.Statement;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CompilationUnit implements Node {

  private final List<Statement> statements;

  private Position position;
}
