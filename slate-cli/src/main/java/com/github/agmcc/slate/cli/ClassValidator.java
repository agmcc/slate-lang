package com.github.agmcc.slate.cli;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class ClassValidator implements IParameterValidator {

  private static final String CLASS_EXTENSION = ".class";

  @Override
  public void validate(final String name, final String value) throws ParameterException {
    if (!value.endsWith(CLASS_EXTENSION)) {

      throw new ParameterException(
          String.format(
              "Invalid class file '%s' (Expected extension '%s')", value, CLASS_EXTENSION));
    }
  }
}
