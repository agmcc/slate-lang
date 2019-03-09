package com.github.agmcc.slate.bytecode;

import lombok.Data;
import org.objectweb.asm.Type;

@Data
public class Method {

  private final String name;

  private final Type returnType;

  private final String owner;
}
