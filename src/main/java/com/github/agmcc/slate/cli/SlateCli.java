package com.github.agmcc.slate.cli;

import com.beust.jcommander.JCommander;
import com.github.agmcc.slate.App;
import java.io.IOException;

public class SlateCli {

  private static final String PROGRAM_NAME = "java -jar slate-lang-cli-standalone.jar";

  public static void main(final String[] args) throws IOException {
    final Arguments arguments = new Arguments();

    final JCommander jCommander =
        JCommander.newBuilder().addObject(arguments).programName(PROGRAM_NAME).build();

    jCommander.parse(args);

    if (arguments.isHelp() || arguments.getFiles().isEmpty()) {
      jCommander.usage();
      return;
    }

    final String[] files = arguments.getFiles().toArray(new String[0]);

    new App().compile(files);
  }
}
