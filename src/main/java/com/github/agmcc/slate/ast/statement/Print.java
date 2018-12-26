package com.github.agmcc.slate.ast.statement;

import com.github.agmcc.slate.ast.Node;
import com.github.agmcc.slate.ast.Position;
import com.github.agmcc.slate.ast.expression.Expression;
import com.github.agmcc.slate.bytecode.Variable;
import java.io.PrintStream;
import java.util.Map;
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
public class Print implements Statement {

  private final Expression value;

  private Position position;

  @Override
  public void process(Consumer<Node> operation) {
    operation.accept(this);
    value.process(operation);
  }

  @Override
  public void generate(MethodVisitor mv, Map<String, Variable> varMap) {
    mv.visitFieldInsn(
        GETSTATIC,
        Type.getInternalName(System.class),
        "out",
        Type.getDescriptor(PrintStream.class));

    value.push(mv, varMap);

    mv.visitMethodInsn(
        INVOKEVIRTUAL,
        Type.getInternalName(PrintStream.class),
        "println",
        Type.getMethodDescriptor(Type.VOID_TYPE, value.getType(varMap)),
        false);
  }
}
