package com.github.agmcc.slate.cli;

import com.beust.jcommander.Parameter;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
class Arguments {

  @Parameter(description = "source file(s)", validateWith = SourceExtensionValidator.class)
  private List<String> files = new ArrayList<>();

  @Parameter(
      names = {"-?", "-h", "--help"},
      description = "Print this help message",
      help = true)
  private boolean help;
}
