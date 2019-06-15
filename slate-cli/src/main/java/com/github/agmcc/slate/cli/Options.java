package com.github.agmcc.slate.cli;

import com.beust.jcommander.Parameter;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
class Options {

  @Parameter(description = "source file(s)", validateWith = SourceValidator.class, required = true)
  private List<String> sources = new ArrayList<>();

  @Parameter(
      names = "--classes",
      validateWith = ClassValidator.class,
      description = "class file(s)")
  private List<String> classes = new ArrayList<>();

  @Parameter(
      names = {"-?", "-h", "--help"},
      description = "Display usage",
      help = true)
  private boolean help;
}
