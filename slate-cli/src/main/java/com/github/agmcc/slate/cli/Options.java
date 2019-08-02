package com.github.agmcc.slate.cli;

import com.beust.jcommander.Parameter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import lombok.Getter;
import lombok.ToString;
import org.apache.logging.log4j.Level;

@CliScope
@Getter
@ToString
class Options {

  @Inject
  Options() {}

  @Parameter(description = "source file(s)", validateWith = SourceValidator.class, required = true)
  private List<String> sources = new ArrayList<>();

  @Parameter(
      names = {"-c", "--classes"},
      description = "class dir(s)")
  private List<String> classes = new ArrayList<>();

  @Parameter(
      names = {"-o", "--output"},
      description = "output file directory")
  private Path output;

  @Parameter(
      names = {"-l", "--log"},
      description = "Log level",
      converter = LogLevelConverter.class)
  private Level logLevel;

  @Parameter(
      names = {"-?", "-h", "--help"},
      description = "Display usage",
      help = true)
  private boolean help;
}
