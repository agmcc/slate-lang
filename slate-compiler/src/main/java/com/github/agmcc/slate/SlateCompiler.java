package com.github.agmcc.slate;

import java.nio.file.Path;
import java.util.List;
import javax.inject.Inject;

/** Entry point for compiler api. */
@CompilerScope
public class SlateCompiler {

  @Inject
  public SlateCompiler() {}

  public CompilationResult compile(final List<Path> sources, final List<Path> classes) {
    System.out.println("Sources: " + sources);
    System.out.println("Classes: " + classes);

    /*
    Process source files to get parse tree
    run bootstrap parsing once
    run full AST parsing per file
    run compilation per file
    run io (write to file) per file
    return errors / diagnostics
     */

    // TODO: Stub implementation
    return CompilationResult.builder().errors(List.of("Line 1", "Line 4")).build();
  }
}
