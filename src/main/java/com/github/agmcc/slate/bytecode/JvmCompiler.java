package com.github.agmcc.slate.bytecode;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.ISTORE;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V1_6;

import com.github.agmcc.slate.ast.CompilationUnit;
import com.github.agmcc.slate.ast.statement.Assignment;
import com.github.agmcc.slate.ast.statement.Print;
import com.github.agmcc.slate.ast.statement.VarDeclaration;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

public class JvmCompiler {

  private final AtomicInteger localVarCount = new AtomicInteger();

  public byte[] compile(CompilationUnit compilationUnit, String className) {
    final var cw = new ClassWriter(COMPUTE_FRAMES);
    final var tcv = new TraceClassVisitor(cw, new PrintWriter(System.out));
    final var cv = new CheckClassAdapter(tcv);

    cv.visit(V1_6, ACC_PUBLIC, className, null, Type.getInternalName(Object.class), null);

    final var mv =
        cv.visitMethod(
            ACC_PUBLIC + ACC_STATIC,
            "main",
            Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(String[].class)),
            null,
            null);

    mv.visitCode();

    final var methodStart = new Label();
    final var methodEnd = new Label();

    mv.visitLabel(methodStart);

    // Variables
    final var varMap = new HashMap<String, Variable>();

    mv.visitLabel(methodEnd);

    compilationUnit.specificProcess(
        VarDeclaration.class,
        v -> {
          final var varType = Optional.ofNullable(v.getValue()).orElseThrow().getType(varMap);
          final var index = localVarCount.incrementAndGet();

          varMap.put(v.getVarName(), new Variable(varType, index));

          mv.visitLocalVariable(
              v.getVarName(), varType.getDescriptor(), null, methodStart, methodEnd, index);
        });

    // Statements
    compilationUnit
        .getStatements()
        .forEach(
            s -> {
              if (s instanceof VarDeclaration) {
                final var variable = varMap.get(((VarDeclaration) s).getVarName());

                ((VarDeclaration) s).getValue().pushAs(mv, varMap, variable.getType());

                mv.visitVarInsn(variable.getType().getOpcode(ISTORE), variable.getIndex());
              } else if (s instanceof Assignment) {
                final var variable = varMap.get(((Assignment) s).getVarName());

                ((Assignment) s).getValue().pushAs(mv, varMap, variable.getType());

                mv.visitVarInsn(variable.getType().getOpcode(ISTORE), variable.getIndex());
              } else if (s instanceof Print) {
                mv.visitFieldInsn(
                    GETSTATIC,
                    Type.getInternalName(System.class),
                    "out",
                    Type.getDescriptor(PrintStream.class));

                final var value = ((Print) s).getValue();

                value.push(mv, varMap);

                mv.visitMethodInsn(
                    INVOKEVIRTUAL,
                    Type.getInternalName(PrintStream.class),
                    "println",
                    Type.getMethodDescriptor(Type.VOID_TYPE, value.getType(varMap)),
                    false);
              } else {
                throw new UnsupportedOperationException(
                    "Unsupported statement: " + s.getClass().getCanonicalName());
              }
            });

    mv.visitInsn(RETURN);
    mv.visitMaxs(100, 100); // TODO: Use proper values
    mv.visitEnd();

    cv.visitEnd();

    return cw.toByteArray();
  }
}
