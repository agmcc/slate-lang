package com.github.agmcc.slate.parser.symbol;

import com.github.agmcc.slate.parser.ErrorListener;
import dagger.internal.Factory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@Generated(value = "dagger.internal.codegen.ComponentProcessor", comments = "https://dagger.dev")
public final class SymbolManager_Factory implements Factory<SymbolManager> {
  private final Provider<ErrorListener> errorListenerProvider;

  public SymbolManager_Factory(final Provider<ErrorListener> errorListenerProvider) {
    this.errorListenerProvider = errorListenerProvider;
  }

  @Override
  public SymbolManager get() {
    return new SymbolManager(errorListenerProvider.get());
  }

  public static SymbolManager_Factory create(final Provider<ErrorListener> errorListenerProvider) {
    return new SymbolManager_Factory(errorListenerProvider);
  }

  public static SymbolManager newInstance(final ErrorListener errorListener) {
    return new SymbolManager(errorListener);
  }
}
