package com.github.agmcc.slate.ast.mapper;

import com.github.agmcc.slate.antlr.SlateParser.AssignmentStatementContext;
import com.github.agmcc.slate.antlr.SlateParser.BinaryOperationContext;
import com.github.agmcc.slate.antlr.SlateParser.CompilationUnitContext;
import com.github.agmcc.slate.antlr.SlateParser.DecimalLiteralContext;
import com.github.agmcc.slate.antlr.SlateParser.ExpressionContext;
import com.github.agmcc.slate.antlr.SlateParser.IntLiteralContext;
import com.github.agmcc.slate.antlr.SlateParser.ParenExpressionContext;
import com.github.agmcc.slate.antlr.SlateParser.PrintStatementContext;
import com.github.agmcc.slate.antlr.SlateParser.StatementContext;
import com.github.agmcc.slate.antlr.SlateParser.StringLiteralContext;
import com.github.agmcc.slate.antlr.SlateParser.VarDeclarationStatementContext;
import com.github.agmcc.slate.antlr.SlateParser.VarReferenceContext;
import com.github.agmcc.slate.ast.CompilationUnit;
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
import com.github.agmcc.slate.ast.statement.Assignment;
import com.github.agmcc.slate.ast.statement.Print;
import com.github.agmcc.slate.ast.statement.Statement;
import com.github.agmcc.slate.ast.statement.VarDeclaration;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.ParserRuleContext;

public class ParseTreeMapperImpl
    implements ParseTreeMapper<CompilationUnitContext, CompilationUnit> {

  @Override
  public CompilationUnit toAst(CompilationUnitContext ctx) {
    if (ctx == null) {
      return null;
    }

    return new CompilationUnit(
        ctx.statement().stream().map(this::toAst).collect(Collectors.toList()));
  }

  private Statement toAst(StatementContext ctx) {
    if (ctx instanceof VarDeclarationStatementContext) {
      var varDecAssignment = ((VarDeclarationStatementContext) ctx).varDeclaration().assignment();
      return new VarDeclaration(
          varDecAssignment.ID().getText(), toAst(varDecAssignment.expression()));
    } else if (ctx instanceof AssignmentStatementContext) {
      var assignmentCtx = ((AssignmentStatementContext) ctx).assignment();
      return new Assignment(assignmentCtx.ID().getText(), toAst(assignmentCtx.expression()));
    } else if (ctx instanceof PrintStatementContext) {
      return new Print(toAst(((PrintStatementContext) ctx).print().expression()));
    } else {
      throw new UnsupportedOperationException(error(ctx));
    }
  }

  private Expression toAst(ExpressionContext ctx) {
    if (ctx instanceof BinaryOperationContext) {
      return toAst((BinaryOperationContext) ctx);
    } else if (ctx instanceof IntLiteralContext) {
      return new IntLit(ctx.getText());
    } else if (ctx instanceof DecimalLiteralContext) {
      return new DecLit(ctx.getText());
    } else if (ctx instanceof StringLiteralContext) {
      var text = ctx.getText();
      return new StringLit(text.substring(1, text.length() - 1));
    } else if (ctx instanceof VarReferenceContext) {
      return new VarReference(ctx.getText());
    } else if (ctx instanceof ParenExpressionContext) {
      return toAst(((ParenExpressionContext) ctx).expression());
    } else {
      throw new UnsupportedOperationException(error(ctx));
    }
  }

  private BinaryExpression toAst(BinaryOperationContext ctx) {
    switch (ctx.operator.getText()) {
      case "+":
        return new AdditionExpression(toAst(ctx.left), toAst(ctx.right));
      case "-":
        return new SubtractionExpression(toAst(ctx.left), toAst(ctx.right));
      case "*":
        return new MultiplicationExpression(toAst(ctx.left), toAst(ctx.right));
      case "/":
        return new DivisionExpression(toAst(ctx.left), toAst(ctx.right));
      default:
        throw new UnsupportedOperationException(error(ctx));
    }
  }

  private String error(ParserRuleContext ctx) {
    return String.format("%s: %s", ctx.getClass().getCanonicalName(), ctx.getText());
  }
}
