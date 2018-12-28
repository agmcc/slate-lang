package com.github.agmcc.slate;

import com.github.agmcc.slate.ast.validator.Error;
import java.util.List;
import java.util.stream.Collectors;

public class ValidationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ValidationException(List<Error> errors) {
    super(errors.stream().map(Error::getMessage).collect(Collectors.joining("\n")));
  }
}
