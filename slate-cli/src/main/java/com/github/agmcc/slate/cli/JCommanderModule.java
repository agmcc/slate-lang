package com.github.agmcc.slate.cli;

import com.beust.jcommander.JCommander;
import dagger.Module;
import dagger.Provides;
import java.util.Properties;
import javax.inject.Singleton;
import lombok.AllArgsConstructor;

@Module
@AllArgsConstructor
class JCommanderModule {

  private final Options options;

  @Provides
  @Singleton
  JCommander provideJCommander(final Properties properties) {
    return JCommander.newBuilder()
        .addObject(options)
        .programName(properties.getProperty("app-name"))
        .build();
  }
}
