package com.github.agmcc.slate.cli;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class SourceValidator implements IParameterValidator {

  private static final String SOURCE_EXTENSION = ".slate";

  @Override
  public void validate(final String name, final String value) throws ParameterException {
    if (!value.endsWith(SOURCE_EXTENSION)) {

      throw new ParameterException(
          String.format(
              "Invalid source file '%s' (Expected extension '%s')", value, SOURCE_EXTENSION));
    }
  }
}
