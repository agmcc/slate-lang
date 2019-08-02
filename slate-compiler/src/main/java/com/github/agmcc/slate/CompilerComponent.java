package com.github.agmcc.slate;

import dagger.Component;

@Component
@CompilerScope
public interface CompilerComponent {

  SlateCompiler slateCompiler();
}
