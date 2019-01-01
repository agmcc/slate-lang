package com.github.agmcc.slate.ast.validator;

import com.github.agmcc.slate.ast.CompilationUnit;
import com.github.agmcc.slate.ast.expression.BooleanLit;
import com.github.agmcc.slate.ast.expression.VarReference;
import com.github.agmcc.slate.ast.expression.binary.logic.LogicExpression;
import com.github.agmcc.slate.ast.statement.Assignment;
import com.github.agmcc.slate.ast.statement.Block;
import com.github.agmcc.slate.ast.statement.Condition;
import com.github.agmcc.slate.ast.statement.Statement;
import com.github.agmcc.slate.ast.statement.VarDeclaration;
import com.github.agmcc.slate.ast.statement.While;
import java.util.ArrayList;
import java.util.List;

public class Validator {

  private static final String DUPLICATE_VAR_ERROR_TEMPLATE = "Variable '%s' already declared at %s";

  private static final String MISSING_VAR_ERROR_TEMPLATE = "No variable named '%s' at %s";

  private static final String INVALID_CONDITION_ERROR_TEMPLATE =
      "Condition must be a boolean expression at %s";

  public List<Error> validate(CompilationUnit compilationUnit) {
    return new ArrayList<>(validateScope(compilationUnit.getStatements(), null));
  }

  private List<Error> validateScope(List<Statement> statements, Scope parent) {
    final var errors = new ArrayList<Error>();
    final var currentScope = new Scope(parent);

    for (var s : statements) {
      if (s instanceof Block) {
        errors.addAll(validateScope(((Block) s).getStatements(), currentScope));
        continue;
      }

      s.specificProcess(
          VarDeclaration.class,
          v -> {
            final var varName = v.getVarName();
            final var resolved = currentScope.resolve();
            if (resolved.containsKey(varName)) {
              errors.add(
                  new Error(
                      String.format(
                          DUPLICATE_VAR_ERROR_TEMPLATE,
                          varName,
                          resolved.get(varName).getPosition().getStart()),
                      v.getPosition().getStart()));
            } else {
              currentScope.add(v);
            }
          });

      s.specificProcess(
          VarReference.class,
          r -> {
            final var varName = r.getText();
            final var resolved = currentScope.resolve();
            if (!resolved.containsKey(varName)) {
              errors.add(
                  new Error(
                      String.format(
                          MISSING_VAR_ERROR_TEMPLATE, varName, r.getPosition().getStart()),
                      r.getPosition().getStart()));
            }
          });

      s.specificProcess(
          Assignment.class,
          a -> {
            final var varName = a.getVarName();
            final var resolved = currentScope.resolve();
            if (!resolved.containsKey(varName)) {
              errors.add(
                  new Error(
                      String.format(
                          MISSING_VAR_ERROR_TEMPLATE, varName, a.getPosition().getStart()),
                      a.getPosition().getStart()));
            }
          });

      s.specificProcess(
          Condition.class,
          c -> {
            final var expression = c.getExpression();

            if (expression instanceof VarReference) {
              final var variable =
                  currentScope.resolve().get(((VarReference) expression).getText());

              if (variable == null) {
                return;
              }

              final var value = variable.getValue();

              if (!(value instanceof BooleanLit || value instanceof LogicExpression)) {
                errors.add(
                    new Error(
                        String.format(
                            INVALID_CONDITION_ERROR_TEMPLATE, expression.getPosition().getStart()),
                        expression.getPosition().getStart()));
              }
            } else if (!(expression instanceof BooleanLit
                || expression instanceof LogicExpression)) {
              errors.add(
                  new Error(
                      String.format(
                          INVALID_CONDITION_ERROR_TEMPLATE, expression.getPosition().getStart()),
                      expression.getPosition().getStart()));
            }
          });

      s.specificProcess(
          While.class,
          w -> {
            final var expression = w.getExpression();

            if (expression instanceof VarReference) {
              final var variable =
                  currentScope.resolve().get(((VarReference) expression).getText());

              if (variable == null) {
                return;
              }

              final var value = variable.getValue();

              if (!(value instanceof BooleanLit || value instanceof LogicExpression)) {
                errors.add(
                    new Error(
                        String.format(
                            INVALID_CONDITION_ERROR_TEMPLATE, expression.getPosition().getStart()),
                        expression.getPosition().getStart()));
              }
            } else if (!(expression instanceof BooleanLit
                || expression instanceof LogicExpression)) {
              errors.add(
                  new Error(
                      String.format(
                          INVALID_CONDITION_ERROR_TEMPLATE, expression.getPosition().getStart()),
                      expression.getPosition().getStart()));
            }
          });
    }

    return errors;
  }
}
