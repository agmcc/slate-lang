package com.github.agmcc.slate.compiler.visitor;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.V1_6;

import com.github.agmcc.slate.parser.ast.ClassDeclaration;
import com.github.agmcc.slate.parser.ast.InterfaceDeclaration;
import com.github.agmcc.slate.parser.ast.NodeVisitor;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;

public class TypeDeclarationVisitor implements NodeVisitor {

  private static final int VERSION = V1_6;

  private static final Map<String, Integer> modifiers = new HashMap<>();

  static {
    modifiers.put("PUBLIC", ACC_PUBLIC);
  }

  private ClassWriter cw;

  private MethodDeclarationVisitor methodDeclarationVisitor;

  @Inject
  public TypeDeclarationVisitor(
      final ClassWriter cw, final MethodDeclarationVisitor methodDeclarationVisitor) {
    this.cw = cw;
    this.methodDeclarationVisitor = methodDeclarationVisitor;
  }

  @Override
  public void visit(final ClassDeclaration classDeclaration) {
    cw.visit(
        VERSION,
        getClassModifiers(classDeclaration),
        getInternalName(classDeclaration.getName()),
        null,
        Type.getInternalName(Object.class),
        null);

    classDeclaration.getMethodDeclarations().forEach(m -> m.accept(methodDeclarationVisitor));
  }

  @Override
  public void visit(final InterfaceDeclaration interfaceDeclaration) {
    cw.visit(
        VERSION,
        getInterfaceModifiers(interfaceDeclaration),
        getInternalName(interfaceDeclaration.getName()),
        null,
        Type.getInternalName(Object.class),
        null);
  }

  private int getClassModifiers(final ClassDeclaration classDeclaration) {
    return Optional.ofNullable(classDeclaration.getModifier())
        .map(m -> modifiers.get(m.name()))
        .orElse(0);
  }

  private int getInterfaceModifiers(final InterfaceDeclaration interfaceDeclaration) {
    return ACC_INTERFACE
        | ACC_ABSTRACT
        | Optional.ofNullable(interfaceDeclaration.getModifier())
            .map(m -> modifiers.get(m.name()))
            .orElse(0);
  }

  private String getInternalName(final String canonicalName) {
    return canonicalName.replace(".", "/");
  }
}
