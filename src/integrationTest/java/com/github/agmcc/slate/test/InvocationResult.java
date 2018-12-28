package com.github.agmcc.slate.test;

import lombok.Data;

@Data
public class InvocationResult {

  private final Object returnValue;

  private final String stdOut;
}
