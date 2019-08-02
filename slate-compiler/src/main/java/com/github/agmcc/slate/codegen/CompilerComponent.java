package com.github.agmcc.slate.codegen;

import com.github.agmcc.slate.codegen.visitor.CompilationUnitVisitor;
import dagger.Component;
import javax.inject.Singleton;
import org.objectweb.asm.ClassWriter;

@Component(modules = AsmModule.class)
@Singleton
public interface CompilerComponent {

  CompilationUnitVisitor compilationUnitVisitor();

  ClassWriter classWriter();
}
