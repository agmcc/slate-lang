package com.github.agmcc.slate.ast.mapper;

import com.github.agmcc.slate.antlr.SlateParser.AssignmentStatementContext;
import com.github.agmcc.slate.antlr.SlateParser.BinaryOperationContext;
import com.github.agmcc.slate.antlr.SlateParser.BlockStatementContext;
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
import com.github.agmcc.slate.ast.Position;
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
import com.github.agmcc.slate.ast.statement.*;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParseTreeMapperImpl
    implements ParseTreeMapper<CompilationUnitContext, CompilationUnit> {

  private boolean considerPosition;

  @Override
  public CompilationUnit toAst(CompilationUnitContext ctx) {
    if (ctx == null) {
      return null;
    }

    return new CompilationUnit(
        ctx.statement().stream().map(this::toAst).collect(Collectors.toList()), toPosition(ctx));
  }

  private Statement toAst(StatementContext ctx) {
    if (ctx instanceof VarDeclarationStatementContext) {
      final var varDecAssignment =
          ((VarDeclarationStatementContext) ctx).varDeclaration().assignment();
      return new VarDeclaration(
          varDecAssignment.ID().getText(), toAst(varDecAssignment.expression()), toPosition(ctx));
    } else if (ctx instanceof AssignmentStatementContext) {
      var assignmentCtx = ((AssignmentStatementContext) ctx).assignment();
      return new Assignment(
          assignmentCtx.ID().getText(), toAst(assignmentCtx.expression()), toPosition(ctx));
    } else if (ctx instanceof PrintStatementContext) {
      return new Print(toAst(((PrintStatementContext) ctx).print().expression()), toPosition(ctx));
    } else if (ctx instanceof BlockStatementContext) {
      final var statements = ((BlockStatementContext) ctx).block().statement();
      return new Block(
          statements.stream().map(this::toAst).collect(Collectors.toList()), toPosition(ctx));
    } else {
      throw new UnsupportedOperationException(getErrorMsg(ctx));
    }
  }

  private Expression toAst(ExpressionContext ctx) {
    if (ctx instanceof BinaryOperationContext) {
      return toAst((BinaryOperationContext) ctx);
    } else if (ctx instanceof IntLiteralContext) {
      return new IntLit(ctx.getText(), toPosition(ctx));
    } else if (ctx instanceof DecimalLiteralContext) {
      return new DecLit(ctx.getText(), toPosition(ctx));
    } else if (ctx instanceof StringLiteralContext) {
      var text = ctx.getText();
      return new StringLit(text.substring(1, text.length() - 1), toPosition(ctx));
    } else if (ctx instanceof VarReferenceContext) {
      return new VarReference(ctx.getText(), toPosition(ctx));
    } else if (ctx instanceof ParenExpressionContext) {
      return toAst(((ParenExpressionContext) ctx).expression());
    } else {
      throw new UnsupportedOperationException(getErrorMsg(ctx));
    }
  }

  private BinaryExpression toAst(BinaryOperationContext ctx) {
    switch (ctx.operator.getText()) {
      case "+":
        return new AdditionExpression(toAst(ctx.left), toAst(ctx.right), toPosition(ctx));
      case "-":
        return new SubtractionExpression(toAst(ctx.left), toAst(ctx.right), toPosition(ctx));
      case "*":
        return new MultiplicationExpression(toAst(ctx.left), toAst(ctx.right), toPosition(ctx));
      case "/":
        return new DivisionExpression(toAst(ctx.left), toAst(ctx.right), toPosition(ctx));
      default:
        throw new UnsupportedOperationException(getErrorMsg(ctx));
    }
  }

  private Position toPosition(ParserRuleContext ctx) {
    if (!considerPosition) {
      return null;
    }

    final var start = ctx.getStart();
    final var stop = ctx.getStop();
    final var stopLength = stop.getType() == Token.EOF ? 0 : stop.getText().length();

    return Position.of(
        start.getLine(),
        start.getCharPositionInLine(),
        stop.getLine(),
        stop.getCharPositionInLine() + stopLength);
  }

  private String getErrorMsg(ParserRuleContext ctx) {
    return String.format("%s: %s", ctx.getClass().getCanonicalName(), ctx.getText());
  }
}
