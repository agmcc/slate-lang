package com.github.agmcc.slate.cli;

import com.github.agmcc.slate.CompilerComponent;
import dagger.BindsInstance;
import dagger.Component;

@Component(
    modules = {JCommanderModule.class, PropertiesModule.class},
    dependencies = CompilerComponent.class)
@CliScope
public interface CliComponent {

  SlateCli slateCli();

  @Component.Builder
  interface Builder {

    @BindsInstance
    Builder args(String[] args);

    Builder compilerComponent(CompilerComponent compilerComponent);

    CliComponent build();
  }
}
