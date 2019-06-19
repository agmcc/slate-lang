package com.github.agmcc.slate.compiler;

import com.github.agmcc.slate.parser.ast.CompilationUnit;

public class SlateCompiler {

  private static final String CLASS_EXT = ".class";

  public static SlateFile compileCompilationUnit(final CompilationUnit compilationUnit) {

    final var compiler = DaggerCompilerComponent.create();

    compilationUnit.accept(compiler.compilationUnitGenerator());

    return SlateFile.builder()
        .fileName(compilationUnit.getTypeDeclaration().getName().concat(CLASS_EXT))
        .data(compiler.classWriter().toByteArray())
        .build();
  }
}
