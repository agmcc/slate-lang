package com.github.agmcc.slate.cli;

import com.beust.jcommander.JCommander;
import com.github.agmcc.slate.DaggerCompilerComponent;
import com.github.agmcc.slate.SlateCompiler;
import javax.inject.Inject;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

@CliScope
public class SlateCli {

  private static final Logger LOGGER = LogManager.getLogger(SlateCli.class);

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
    setLogLevel(options.getLogLevel());

    LOGGER.debug("Options: {}", options);

    if (options.isHelp()) {
      final StringBuilder sb = new StringBuilder();
      jCommander.usage(sb);
      LOGGER.error(sb.toString());
      return;
    }

    final var result = slateCompiler.compile(null, null);

    result.getErrors().forEach(LOGGER::error);
  }

  private void setLogLevel(final Level logLevel) {
    if (logLevel == null) {
      return;
    }

    final var context = LoggerContext.getContext(false);
    final var rootLogger = context.getConfiguration().getRootLogger();

    rootLogger.setLevel(Level.INFO);
    context.updateLoggers();
    LOGGER.info("Log level set to {}", logLevel);

    rootLogger.setLevel(logLevel);
    context.updateLoggers();
  }
}
