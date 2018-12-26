parser grammar SlateParser;

options { tokenVocab=SlateLexer; }

compilationUnit: statement* EOF;

statement: varDeclaration # varDeclarationStatement
         | assignment # assignmentStatement
         | print # printStatement
         | block # blockStatement;

print: PRINT expression;

varDeclaration: VAR assignment;

assignment: ID ASSIGN expression;

block: L_BRACE statement* R_BRACE;

expression : left=expression operator=(DIV|MUL) right=expression # binaryOperation
           | left=expression operator=(ADD|SUB) right=expression # binaryOperation
           | L_PAREN expression R_PAREN # parenExpression
           | ID # varReference
           | STRING_LIT # stringLiteral
           | INT_LIT # intLiteral
           | DEC_LIT # decimalLiteral ;
