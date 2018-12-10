package com.github.agmcc.slate.ast.validator;

import com.github.agmcc.slate.ast.CompilationUnit;
import com.github.agmcc.slate.ast.expression.VarReference;
import com.github.agmcc.slate.ast.statement.Assignment;
import com.github.agmcc.slate.ast.statement.VarDeclaration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Validator {

  private static final String DUPLICATE_VAR_ERROR_TEMPLATE = "Variable '%s' already declared at %s";

  private static final String MISSING_VAR_ERROR_TEMPLATE = "No variable named '%s' at %s";

  private static final String VAR_BEFORE_DECLARATION_ERROR_TEMPLATE =
      "Cannot refer to variable '%s' before it's declared";

  public List<Error> validate(CompilationUnit compilationUnit) {
    final var errors = new ArrayList<Error>();

    final var variables = new HashMap<String, VarDeclaration>();

    // Duplicate variables
    compilationUnit.specificProcess(
        VarDeclaration.class,
        v -> {
          if (variables.containsKey(v.getVarName())) {
            errors.add(
                new Error(
                    String.format(
                        DUPLICATE_VAR_ERROR_TEMPLATE,
                        v.getVarName(),
                        variables.get(v.getVarName()).getPosition().getStart()),
                    v.getPosition().getStart()));
          } else {
            variables.put(v.getVarName(), v);
          }
        });

    // Variables referenced before declaration
    compilationUnit.specificProcess(
        VarReference.class,
        r -> {
          if (!variables.containsKey(r.getText())) {
            errors.add(
                new Error(
                    String.format(
                        MISSING_VAR_ERROR_TEMPLATE, r.getText(), r.getPosition().getStart()),
                    r.getPosition().getStart()));
          } else if (r.getPosition().compareTo(variables.get(r.getText()).getPosition()) < 0) {
            errors.add(
                new Error(
                    String.format(VAR_BEFORE_DECLARATION_ERROR_TEMPLATE, r.getText()),
                    r.getPosition().getStart()));
          }
        });

    // Variable assignment before declaration
    compilationUnit.specificProcess(
        Assignment.class,
        a -> {
          if (!variables.containsKey(a.getVarName())) {
            errors.add(
                new Error(
                    String.format(
                        MISSING_VAR_ERROR_TEMPLATE, a.getVarName(), a.getPosition().getStart()),
                    a.getPosition().getStart()));
          } else {
            errors.add(
                new Error(
                    String.format(VAR_BEFORE_DECLARATION_ERROR_TEMPLATE, a.getVarName()),
                    a.getPosition().getStart()));
          }
        });

    return errors;
  }
}
