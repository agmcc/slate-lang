package com.github.agmcc.slate.ast.validator;

import com.github.agmcc.slate.ast.Point;
import lombok.Data;

@Data
public class Error {

  private final String message;

  private final Point point;
}
