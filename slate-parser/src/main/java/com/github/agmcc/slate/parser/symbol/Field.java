package com.github.agmcc.slate.parser.symbol;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Field implements Symbol {

  private String name;

  private int modifiers;
}
