package com.github.agmcc.slate.cli;

import com.beust.jcommander.JCommander;
import com.github.agmcc.slate.App;
import com.github.agmcc.slate.cli.Arguments.LogLevel;
import java.util.Arrays;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;

public class SlateCli {

  private static final Logger logger = LogManager.getLogger(SlateCli.class);

  private static final String PROGRAM_NAME = "java -jar slate-lang-cli-standalone.jar";

  public static void main(final String[] args) {
    final Arguments arguments = new Arguments();

    final JCommander jCommander =
        JCommander.newBuilder().addObject(arguments).programName(PROGRAM_NAME).build();

    jCommander.parse(args);

    final LogLevel logLevel = arguments.getLogLevel();
    if (logLevel != null) {
      setLogLevel(Level.getLevel(logLevel.toString()));
    }

    logger.debug("Raw args: {}", Arrays.toString(args));
    logger.debug("Processed args: {}", arguments);

    final String[] files = arguments.getFiles().toArray(new String[0]);
    logger.info("Source files: {}", Arrays.toString(files));

    if (arguments.isHelp() || files.length == 0) {
      final StringBuilder sb = new StringBuilder();
      jCommander.usage(sb);
      logger.error(sb);
      return;
    }

    try {
      new App().compile(files);
    } catch (final Exception e) {
      logger.error(e);
    }
  }

  private static void setLogLevel(final Level logLevel) {
    logger.warn("Log level set to {}", logLevel);
    final LoggerContext context = LoggerContext.getContext(false);
    final Configuration config = context.getConfiguration();
    config.getRootLogger().setLevel(logLevel);
    context.updateLoggers();
  }
}
