package com.github.agmcc.slate.ast;

import com.github.agmcc.slate.ast.statement.Return;
import com.github.agmcc.slate.ast.statement.Statement;
import com.github.agmcc.slate.bytecode.MethodGenerator;
import com.github.agmcc.slate.bytecode.Scope;
import com.github.agmcc.slate.bytecode.Variable;
import java.util.List;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class MethodDeclaration implements Node, MethodGenerator {

  private final String name;

  private final List<Parameter> parameters;

  private final Type returnType;

  private final Statement statement;

  private Position position;

  @Override
  public void process(Consumer<Node> operation) {
    operation.accept(this);
    statement.process(operation);
  }

  @Override
  public void generate(ClassVisitor cv, Scope scope) {
    final var mv =
        cv.visitMethod(
            ACC_PUBLIC + ACC_STATIC,
            name,
            Type.getMethodDescriptor(
                returnType, parameters.stream().map(Parameter::getType).toArray(Type[]::new)),
            null,
            null);

    mv.visitCode();

    // Method scope inherits class scope
    final Scope methodScope = new Scope(scope);

    // Params
    parameters.forEach(
        p ->
            methodScope.add(
                new Variable(p.getValue(), p.getType(), methodScope.getNextVarIndex(p.getType()))));

    // Pass current scope
    statement.generate(mv, methodScope);

    // TODO: Implicit return needs to be more finessed
    new Return(null).generate(mv, methodScope);

    mv.visitMaxs(100, 100); // TODO: Use proper values
    mv.visitEnd();
  }
}
