package com.github.agmcc.slate.cli;

import dagger.Module;
import dagger.Provides;
import java.util.Properties;
import javax.inject.Singleton;
import lombok.AllArgsConstructor;

@Module
@AllArgsConstructor
class PropertiesModule {

  private String propertiesPath;

  @Provides
  @Singleton
  Properties provideProperties() {
    final var props = new Properties();
    try {
      props.load(PropertiesModule.class.getResourceAsStream(propertiesPath));
    } catch (final Exception e) {
      throw new RuntimeException("Failed to load properties file: " + propertiesPath, e);
    }
    return props;
  }
}
