package com.github.agmcc.slate.parser.bootstrap;

import dagger.Module;
import dagger.Provides;
import java.net.URL;
import java.net.URLClassLoader;
import lombok.AllArgsConstructor;

@Module
@AllArgsConstructor
public class ClassLoaderModule {

  private URL[] classUrls;

  @Provides
  @BootstrapScope
  ClassLoader provideClassLoader() {
    return new URLClassLoader(classUrls, null);
  }
}
