parser grammar SlateParser;

options { tokenVocab=SlateLexer; }

compilationUnit: statement* EOF;

statement: varDeclaration # varDeclarationStatement
         | assignment # assignmentStatement
         | print # printStatement
         | block # blockStatement
         | condition # conditionStatement
         | whileLoop # whileLoopStatement
         | forLoop # forLoopStatement;

print: PRINT expression;

varDeclaration: VAR assignment;

assignment: ID ASSIGN expression;

block: L_BRACE statement* R_BRACE;

condition: IF expression trueStatement=statement (ELSE falseStatement=statement)?;

whileLoop: WHILE expression body=statement;

forLoop: FOR declaration=statement check=expression after=expression body=statement # forTraditional;

expression: left=expression operator=(DIV|MUL) right=expression # binaryOperation
          | left=expression operator=(ADD|SUB) right=expression # binaryOperation
          | left=expression operator=(GREATER|GREATER_EQ|EQUAL|NOT_EQUAL|LESS|LESS_EQ|AND|OR) right=expression # binaryOperation
          | L_PAREN expression R_PAREN # parenExpression
          | ID # varReference
          | STRING_LIT # stringLiteral
          | INT_LIT # intLiteral
          | DEC_LIT # decimalLiteral
          | (TRUE_LIT|FALSE_LIT) # booleanLiteral
          | INCREMENT ID # preIncrement
          | ID INCREMENT # postIncrement
          | DECREMENT ID # preDecrement
          | ID DECREMENT # postDecrement;
