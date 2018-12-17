package com.github.agmcc.slate.bytecode;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.D2I;
import static org.objectweb.asm.Opcodes.DADD;
import static org.objectweb.asm.Opcodes.DDIV;
import static org.objectweb.asm.Opcodes.DLOAD;
import static org.objectweb.asm.Opcodes.DMUL;
import static org.objectweb.asm.Opcodes.DSUB;
import static org.objectweb.asm.Opcodes.I2D;
import static org.objectweb.asm.Opcodes.IADD;
import static org.objectweb.asm.Opcodes.IDIV;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.IMUL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.ISUB;

import com.github.agmcc.slate.ast.expression.DecLit;
import com.github.agmcc.slate.ast.expression.Expression;
import com.github.agmcc.slate.ast.expression.IntLit;
import com.github.agmcc.slate.ast.expression.StringLit;
import com.github.agmcc.slate.ast.expression.VarReference;
import com.github.agmcc.slate.ast.expression.binary.AdditionExpression;
import com.github.agmcc.slate.ast.expression.binary.BinaryExpression;
import com.github.agmcc.slate.ast.expression.binary.DivisionExpression;
import com.github.agmcc.slate.ast.expression.binary.MultiplicationExpression;
import com.github.agmcc.slate.ast.expression.binary.SubtractionExpression;
import java.util.Map;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class TypeHelper {

  public Type toType(Expression expression, Map<String, Variable> variables) {
    if (expression instanceof IntLit) {
      return Type.INT_TYPE;
    } else if (expression instanceof DecLit) {
      return Type.DOUBLE_TYPE;
    } else if (expression instanceof StringLit) {
      return Type.getType(String.class);
    } else if (expression instanceof BinaryExpression) {
      final var binaryExpression = (BinaryExpression) expression;

      final var leftType = toType(binaryExpression.getLeft(), variables);
      final var rightType = toType(binaryExpression.getRight(), variables);

      final var stringType = Type.getType(String.class);
      if (leftType.equals(stringType) || rightType.equals(stringType)) {
        return stringType;
      }

      if (leftType != Type.INT_TYPE && leftType != Type.DOUBLE_TYPE) {
        throw new UnsupportedOperationException("Unsupported type: " + leftType);
      }

      if (rightType != Type.INT_TYPE && rightType != Type.DOUBLE_TYPE) {
        throw new UnsupportedOperationException("Unsupported type: " + leftType);
      }

      if (leftType == Type.INT_TYPE && rightType == Type.INT_TYPE) {
        return Type.INT_TYPE;
      } else {
        return Type.DOUBLE_TYPE;
      }
    } else if (expression instanceof VarReference) {
      return variables.get(((VarReference) expression).getText()).getType();
    } else {
      final var className = expression != null ? expression.getClass().getCanonicalName() : null;
      throw new UnsupportedOperationException("Unsupported type: " + className);
    }
  }

  public void push(
      Expression expression, MethodVisitor mv, Map<String, Variable> variables, Type targetType) {
    push(expression, mv, variables);
    final var type = toType(expression, variables);
    if (!type.equals(targetType)) {
      if (type == Type.INT_TYPE && targetType == Type.DOUBLE_TYPE) {
        mv.visitInsn(I2D);
      } else if (type == Type.DOUBLE_TYPE && targetType == Type.INT_TYPE) {
        mv.visitInsn(D2I);
      } else {
        throw new UnsupportedOperationException(
            String.format(
                "Unable to convert type %s to %s",
                type.getInternalName(), targetType.getInternalName()));
      }
    }
  }

  public void push(Expression expression, MethodVisitor mv, Map<String, Variable> variables) {
    if (expression instanceof IntLit) {
      final var valueStr = ((IntLit) expression).getValue();
      final var value =
          valueStr.toLowerCase().startsWith("0b")
              ? Integer.parseInt(valueStr.substring(2), 2)
              : Integer.decode(valueStr);
      mv.visitLdcInsn(value);
    } else if (expression instanceof DecLit) {
      mv.visitLdcInsn(Double.parseDouble(((DecLit) expression).getValue()));
    } else if (expression instanceof StringLit) {
      mv.visitLdcInsn(((StringLit) expression).getValue());
    } else if (expression instanceof MultiplicationExpression) {
      final var multi = (MultiplicationExpression) expression;
      push(multi.getLeft(), mv, variables, toType(expression, variables));
      push(multi.getRight(), mv, variables, toType(expression, variables));
      final var type = toType(multi, variables);
      if (type == Type.INT_TYPE) {
        mv.visitInsn(IMUL);
      } else if (type == Type.DOUBLE_TYPE) {
        mv.visitInsn(DMUL);
      } else {
        throw new UnsupportedOperationException("Unsupported multiplication type: " + type);
      }
    } else if (expression instanceof DivisionExpression) {
      final var div = (DivisionExpression) expression;
      push(div.getLeft(), mv, variables, toType(expression, variables));
      push(div.getRight(), mv, variables, toType(expression, variables));
      final var type = toType(div, variables);
      if (type == Type.INT_TYPE) {
        mv.visitInsn(IDIV);
      } else if (type == Type.DOUBLE_TYPE) {
        mv.visitInsn(DDIV);
      } else {
        throw new UnsupportedOperationException("Unsupported division type: " + type);
      }
    } else if (expression instanceof AdditionExpression) {
      final var add = (AdditionExpression) expression;
      push(add.getLeft(), mv, variables, toType(expression, variables));
      push(add.getRight(), mv, variables, toType(expression, variables));
      final var type = toType(add, variables);
      if (type == Type.INT_TYPE) {
        mv.visitInsn(IADD);
      } else if (type == Type.DOUBLE_TYPE) {
        mv.visitInsn(DADD);
      } else if (type.equals(Type.getType(String.class))) {
        // TODO: Use StringBuilder#append
        if (!toType(((AdditionExpression) expression).getLeft(), variables)
                .equals(Type.getType(String.class))
            || !toType(((AdditionExpression) expression).getRight(), variables)
                .equals(Type.getType(String.class))) {
          throw new UnsupportedOperationException("String concatenation only supports 2 Strings");
        }

        mv.visitMethodInsn(
            INVOKEVIRTUAL,
            Type.getInternalName(String.class),
            "concat",
            Type.getMethodDescriptor(
                Type.getType(String.class),
                toType(((AdditionExpression) expression).getRight(), variables)),
            false);

      } else {
        throw new UnsupportedOperationException("Unsupported addition type: " + type);
      }
    } else if (expression instanceof SubtractionExpression) {
      final var div = (SubtractionExpression) expression;
      push(div.getLeft(), mv, variables, toType(expression, variables));
      push(div.getRight(), mv, variables, toType(expression, variables));
      final var type = toType(div, variables);
      if (type == Type.INT_TYPE) {
        mv.visitInsn(ISUB);
      } else if (type == Type.DOUBLE_TYPE) {
        mv.visitInsn(DSUB);
      } else {
        throw new UnsupportedOperationException("Unsupported subtraction type: " + type);
      }
    } else if (expression instanceof VarReference) {
      final var varName = ((VarReference) expression).getText();
      final Variable variable;
      if (variables.containsKey(varName)) {
        variable = variables.get(varName);
      } else {
        throw new RuntimeException("Missing variable: " + varName);
      }
      if (variable.getType() == Type.INT_TYPE) {
        mv.visitVarInsn(ILOAD, variable.getIndex());
      } else if (variable.getType() == Type.DOUBLE_TYPE) {
        mv.visitVarInsn(DLOAD, variable.getIndex());
      } else if (variable.getType().equals(Type.getType(String.class))) {
        mv.visitVarInsn(ALOAD, variable.getIndex());
      } else {
        throw new UnsupportedOperationException(
            "Unsupported push var reference: " + expression.getClass().getCanonicalName());
      }
    } else {
      throw new UnsupportedOperationException(
          "Unsupported push: " + expression.getClass().getCanonicalName());
    }
  }
}
