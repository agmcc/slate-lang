package com.github.agmcc.slate.bytecode;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.DSTORE;
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
    final var variables = new HashMap<String, Variable>();

    mv.visitLabel(methodEnd);

    final var typeHelper = new TypeHelper();

    compilationUnit.specificProcess(
        VarDeclaration.class,
        v -> {
          final var varType = typeHelper.toType(v.getValue(), variables);
          final var index = localVarCount.incrementAndGet();

          variables.put(v.getVarName(), new Variable(varType, index));

          mv.visitLocalVariable(
              v.getVarName(), varType.getDescriptor(), null, methodStart, methodEnd, index);
        });

    // Statements
    compilationUnit
        .getStatements()
        .forEach(
            s -> {
              if (s instanceof VarDeclaration) {
                final var variable = variables.get(((VarDeclaration) s).getVarName());
                final var type = variable.getType();
                final var index = variable.getIndex();
                typeHelper.push(((VarDeclaration) s).getValue(), mv, variables);
                if (type == Type.INT_TYPE) {
                  mv.visitVarInsn(ISTORE, index);
                } else if (type == Type.DOUBLE_TYPE) {
                  mv.visitVarInsn(DSTORE, index);
                } else if (type.equals(Type.getType(String.class))) {
                  mv.visitVarInsn(ASTORE, index);
                } else {
                  throw new UnsupportedOperationException(
                      "Unsupported variable declaration: " + s.getClass().getCanonicalName());
                }
              } else if (s instanceof Assignment) {
                final var variable = variables.get(((Assignment) s).getVarName());
                final var type = variable.getType();
                final var index = variable.getIndex();
                typeHelper.push(((Assignment) s).getValue(), mv, variables);
                if (type == Type.INT_TYPE) {
                  mv.visitVarInsn(ISTORE, index);
                } else if (type == Type.DOUBLE_TYPE) {
                  mv.visitVarInsn(DSTORE, index);
                } else if (type.equals(Type.getType(String.class))) {
                  mv.visitVarInsn(ASTORE, index);
                } else {
                  throw new UnsupportedOperationException(
                      "Unsupported assignment declaration: " + s.getClass().getCanonicalName());
                }
              } else if (s instanceof Print) {
                mv.visitFieldInsn(
                    GETSTATIC,
                    Type.getInternalName(System.class),
                    "out",
                    Type.getDescriptor(PrintStream.class));

                final var printValue = ((Print) s).getValue();

                typeHelper.push(printValue, mv, variables);

                final var valueType = typeHelper.toType(printValue, variables);

                mv.visitMethodInsn(
                    INVOKEVIRTUAL,
                    Type.getInternalName(PrintStream.class),
                    "println",
                    Type.getMethodDescriptor(Type.VOID_TYPE, valueType),
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
