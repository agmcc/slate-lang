package com.github.agmcc.slate.cli;

import com.beust.jcommander.JCommander;
import dagger.Component;
import javax.inject.Singleton;

@Component(modules = {JCommanderModule.class, PropertiesModule.class})
@Singleton
public interface CliComponent {

  JCommander jCommander();
}
