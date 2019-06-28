package com.github.agmcc.slate.parser.bootstrap;

import dagger.Subcomponent;

@Subcomponent(modules = ClassLoaderModule.class)
@BootstrapScope
public interface BootstrapParserComponent {

  BootstrapParser bootstrapParser();
}
