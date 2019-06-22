package com.github.agmcc.slate.parser.ast;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class ImportDeclaration {

  private String typeName;
}
