package com.github.agmcc.slate.cli;

import com.beust.jcommander.JCommander;
import com.github.agmcc.slate.DaggerCompilerComponent;
import com.github.agmcc.slate.SlateCompiler;
import javax.inject.Inject;

@CliScope
public class SlateCli {

  private JCommander jCommander;

  private Options options;

  private SlateCompiler slateCompiler;

  @Inject
  public SlateCli(
      final JCommander jCommander, final Options options, final SlateCompiler slateCompiler) {
    this.jCommander = jCommander;
    this.options = options;
    this.slateCompiler = slateCompiler;
  }

  public static void main(final String[] args) {
    final CliComponent cliComponent =
        DaggerCliComponent.builder()
            .args(args)
            .compilerComponent(DaggerCompilerComponent.create())
            .build();

    cliComponent.slateCli().run();
  }

  public void run() {
    System.out.println("Running");

    System.out.println("Options: " + options);

    if (options.isHelp()) {
      jCommander.usage();
      return;
    }

    final var result = slateCompiler.compile(null, null);

    result.getErrors().forEach(e -> System.out.println("Error: " + e));
  }
}
