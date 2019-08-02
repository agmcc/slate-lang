package com.github.agmcc.slate.codegen;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import org.objectweb.asm.ClassWriter;

@Module
class AsmModule {

  @Provides
  @Singleton
  ClassWriter provideClassWriter() {
    return new ClassWriter(ClassWriter.COMPUTE_FRAMES);
  }
}
