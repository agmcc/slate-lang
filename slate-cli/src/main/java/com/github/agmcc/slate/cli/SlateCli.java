package com.github.agmcc.slate.cli;

public class SlateCli {

  public static void main(final String[] args) {

    final var options = new Options();

    final var cliComponent =
        DaggerCliComponent.builder()
            .jCommanderModule(new JCommanderModule(options))
            .propertiesModule(new PropertiesModule("/app.properties"))
            .build();

    final var jCommander = cliComponent.jCommander();

    jCommander.parse(args);

    if (options.isHelp()) {
      jCommander.usage();
      return;
    }

    System.out.println("Source files: " + options.getSources());
    System.out.println("Class files: " + options.getClasses());
  }
}
