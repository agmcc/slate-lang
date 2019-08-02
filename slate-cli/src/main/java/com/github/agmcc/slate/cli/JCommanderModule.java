package com.github.agmcc.slate.cli;

import com.beust.jcommander.JCommander;
import dagger.Module;
import dagger.Provides;
import java.util.Properties;

@Module
class JCommanderModule {

  @Provides
  @CliScope
  JCommander provideJCommander(
      final Options options, final Properties properties, final String[] args) {
    final var jCommander =
        JCommander.newBuilder()
            .addObject(options)
            .programName(properties.getProperty("app-name"))
            .build();

    jCommander.parse(args);

    return jCommander;
  }
}
