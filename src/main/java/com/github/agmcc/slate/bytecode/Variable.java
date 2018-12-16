package com.github.agmcc.slate.bytecode;

import lombok.Data;
import org.objectweb.asm.Type;

@Data
public class Variable {

  private final Type type;

  private final int index;
}
