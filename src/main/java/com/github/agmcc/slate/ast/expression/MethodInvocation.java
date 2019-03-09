package com.github.agmcc.slate.ast.expression;

import com.github.agmcc.slate.ast.Node;
import com.github.agmcc.slate.ast.Position;
import com.github.agmcc.slate.bytecode.Method;
import com.github.agmcc.slate.bytecode.Scope;
import java.util.List;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class MethodInvocation implements Expression {

  private final String name;

  private final List<Expression> arguments;

  private Position position;

  @Override
  public void process(Consumer<Node> operation) {}

  @Override
  public Type getType(Scope scope) {
    return scope.getMethod(name).getReturnType();
  }

  @Override
  public void push(MethodVisitor mv, Scope scope) {
    arguments.forEach(a -> a.push(mv, scope));

    final Method method = scope.getMethod(name);

    mv.visitMethodInsn(
        INVOKESTATIC,
        method.getOwner(),
        name,
        Type.getMethodDescriptor(
            method.getReturnType(),
            arguments.stream().map(a -> a.getType(scope)).toArray(Type[]::new)),
        false);
  }
}
