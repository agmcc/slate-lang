package com.github.agmcc.slate.compiler;

import com.github.agmcc.slate.parser.ast.CompilationUnit;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SlateCompiler {

  private static final String CLASS_EXT = ".class";

  public static SlateFile compileCompilationUnit(final CompilationUnit compilationUnit) {

    final var compiler = DaggerCompilerComponent.create();

    compilationUnit.accept(compiler.compilationUnitVisitor());

    return SlateFile.builder()
        .filePath(getFilePath(compilationUnit))
        .data(compiler.classWriter().toByteArray())
        .build();
  }

  private static Path getFilePath(final CompilationUnit compilationUnit) {
    final var path = Paths.get(compilationUnit.getTypeDeclaration().getName().replace(".", "/"));
    return path.resolveSibling(path.getFileName() + CLASS_EXT);
  }
}
