package com.github.agmcc.slate.cli;

import static com.github.agmcc.slate.cli.Names.PROPERTIES_PATH;

import dagger.Module;
import dagger.Provides;
import java.util.Properties;
import javax.inject.Named;

@Module
class PropertiesModule {

  @Provides
  @CliScope
  @Named(PROPERTIES_PATH)
  String providePropertiesPath() {
    return "/app.properties";
  }

  @Provides
  @CliScope
  Properties provideProperties(final @Named(PROPERTIES_PATH) String propertiesPath) {
    final var props = new Properties();
    try {
      props.load(PropertiesModule.class.getResourceAsStream(propertiesPath));
    } catch (final Exception e) {
      throw new RuntimeException("Failed to load properties file: " + propertiesPath, e);
    }
    return props;
  }
}
