package com.github.agmcc.slate.cli;

import com.beust.jcommander.IStringConverter;
import org.apache.logging.log4j.Level;

public class LogLevelConverter implements IStringConverter<Level> {

  @Override
  public Level convert(final String value) {
    return Level.getLevel(value.toUpperCase());
  }
}
