package com.github.agmcc.slate.ast.validator;

import com.github.agmcc.slate.ast.CompilationUnit;
import com.github.agmcc.slate.ast.expression.VarReference;
import com.github.agmcc.slate.ast.statement.Assignment;
import com.github.agmcc.slate.ast.statement.Block;
import com.github.agmcc.slate.ast.statement.Statement;
import com.github.agmcc.slate.ast.statement.VarDeclaration;
import java.util.ArrayList;
import java.util.List;

public class Validator {

  private static final String DUPLICATE_VAR_ERROR_TEMPLATE = "Variable '%s' already declared at %s";

  private static final String MISSING_VAR_ERROR_TEMPLATE = "No variable named '%s' at %s";

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
    }

    return errors;
  }
}
