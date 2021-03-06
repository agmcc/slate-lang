package com.github.agmcc.slate.ast.validator;

import com.github.agmcc.slate.ast.CompilationUnit;
import com.github.agmcc.slate.ast.MethodDeclaration;
import com.github.agmcc.slate.ast.expression.BooleanLit;
import com.github.agmcc.slate.ast.expression.DecLit;
import com.github.agmcc.slate.ast.expression.Expression;
import com.github.agmcc.slate.ast.expression.IncrementDecrement;
import com.github.agmcc.slate.ast.expression.IntLit;
import com.github.agmcc.slate.ast.expression.binary.logic.LogicExpression;
import com.github.agmcc.slate.ast.statement.Assignment;
import com.github.agmcc.slate.ast.statement.Block;
import com.github.agmcc.slate.ast.statement.ForTraditional;
import com.github.agmcc.slate.ast.statement.Statement;
import com.github.agmcc.slate.ast.statement.VarDeclaration;
import com.github.agmcc.slate.bytecode.Scope;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Validator {

  private static final String DUPLICATE_VAR_ERROR_TEMPLATE = "Variable '%s' already declared at %s";

  private static final String MISSING_VAR_ERROR_TEMPLATE = "No variable named '%s' at %s";

  private static final String INVALID_CONDITION_ERROR_TEMPLATE =
      "Condition must be a boolean expression at %s";

  private static final String INVALID_NUMERIC_TYPE_ERROR_TEMPLATE =
      "Variable '%s' must be a numeric type at %s";

  private static final String MISSING_VAR_DEC__ERROR_TEMPLATE = "Missing var declaration at %s";

  private static final String INVALID_AFTER_EXPRESSION = "Invalid for loop update expression %s";

  public List<Error> validate(CompilationUnit compilationUnit) {
    return validateMethods(compilationUnit.getMethods());
  }

  private List<Error> validateMethods(List<MethodDeclaration> methodDeclarations) {
    return methodDeclarations
        .stream()
        .map(m -> validateScope(List.of(m.getStatement()), null))
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  private List<Error> validateScope(List<Statement> statements, Scope parent) {
    final var errors = new ArrayList<Error>();
    final var currentScope = new Scope(parent);

    for (var s : statements) {
      if (s instanceof Block) {
        errors.addAll(validateScope(((Block) s).getStatements(), currentScope));
        continue;
      }

      //            s.specificProcess(
      //                Condition.class,
      //                c -> {
      //                  final var expression = c.getExpression();
      //
      //                  if (expression instanceof VarReference) {
      //                    final var variable =
      //                        currentScope.resolve().get(((VarReference) expression).getText());
      //
      //                    if (variable == null) {
      //                      return;
      //                    }
      //
      //                    final var value = variable.getValue();
      //
      //                    if (!isBooleanExpression(value)) {
      //                      errors.add(
      //                          new Error(
      //                              String.format(
      //                                  INVALID_CONDITION_ERROR_TEMPLATE,
      //       expression.getPosition().getStart()),
      //                              expression.getPosition().getStart()));
      //                    }
      //                  } else if (!isBooleanExpression(expression)) {
      //                    errors.add(
      //                        new Error(
      //                            String.format(
      //                                INVALID_CONDITION_ERROR_TEMPLATE,
      //       expression.getPosition().getStart()),
      //                            expression.getPosition().getStart()));
      //                  }
      //                });
      //
      //            s.specificProcess(
      //                While.class,
      //                w -> {
      //                  final var expression = w.getExpression();
      //
      //                  if (expression instanceof VarReference) {
      //                    final var variable =
      //                        currentScope.resolve().get(((VarReference) expression).getText());
      //
      //                    if (variable == null) {
      //                      return;
      //                    }
      //
      //                    final var value = variable.getValue();
      //
      //                    if (!isBooleanExpression(value)) {
      //                      errors.add(
      //                          new Error(
      //                              String.format(
      //                                  INVALID_CONDITION_ERROR_TEMPLATE,
      //       expression.getPosition().getStart()),
      //                              expression.getPosition().getStart()));
      //                    }
      //                  } else if (!isBooleanExpression(expression)) {
      //                    errors.add(
      //                        new Error(
      //                            String.format(
      //                                INVALID_CONDITION_ERROR_TEMPLATE,
      //       expression.getPosition().getStart()),
      //                            expression.getPosition().getStart()));
      //                  }
      //                });
      //
      //            s.specificProcess(
      //                IncrementDecrement.class,
      //                id -> {
      //                  final var varName = id.getText();
      //                  final var resolved = currentScope.resolve();
      //                  final var variable = resolved.get(varName);
      //                  if (variable == null) {
      //                    errors.add(
      //                        new Error(
      //                            String.format(
      //                                MISSING_VAR_ERROR_TEMPLATE, varName,
      // id.getPosition().getStart()),
      //                            id.getPosition().getStart()));
      //                    return;
      //                  }
      //
      //                  final var expression = variable.getValue();
      //                  if (!isNumericExpression(expression)) {
      //                    errors.add(
      //                        new Error(
      //                            String.format(
      //                                INVALID_NUMERIC_TYPE_ERROR_TEMPLATE,
      //                                varName,
      //                                id.getPosition().getStart()),
      //                            id.getPosition().getStart()));
      //                  }
      //                });

      s.specificProcess(
          ForTraditional.class,
          f -> {
            if (!(f.getInit() instanceof VarDeclaration)) {
              errors.add(
                  new Error(
                      String.format(
                          MISSING_VAR_DEC__ERROR_TEMPLATE, f.getInit().getPosition().getStart()),
                      f.getInit().getPosition().getStart()));
            }

            final var check = f.getExpression();
            if (!isBooleanExpression(check)) {
              errors.add(
                  new Error(
                      String.format(
                          INVALID_CONDITION_ERROR_TEMPLATE, check.getPosition().getStart()),
                      check.getPosition().getStart()));
            }

            final var after = f.getUpdate();
            if (!(after instanceof IncrementDecrement || after instanceof Assignment)) {
              errors.add(
                  new Error(
                      String.format(INVALID_AFTER_EXPRESSION, after.getPosition().getStart()),
                      after.getPosition().getStart()));
            }
          });
    }

    return errors;
  }

  private boolean isBooleanExpression(Expression expression) {
    return expression instanceof BooleanLit || expression instanceof LogicExpression;
  }

  private boolean isNumericExpression(Expression expression) {
    return expression instanceof IntLit || expression instanceof DecLit;
  }
}
