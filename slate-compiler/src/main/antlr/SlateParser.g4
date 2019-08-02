parser grammar SlateParser;

options { tokenVocab=SlateLexer; }

/*
PACKAGES
*/

compilationUnit: packageDeclaration? importDeclaration* typeDeclaration EOF;

packageDeclaration: PACKAGE ID (PERIOD ID)*;

importDeclaration: singleTypeImportDeclaration;

singleTypeImportDeclaration: IMPORT typeName;

typeName: TYPE_ID
        | packageOrTypeName PERIOD TYPE_ID;

packageOrTypeName: ID
                 | packageOrTypeName PERIOD ID;

typeDeclaration: classDeclaration
               | interfaceDeclaration
               | enumDeclaration;

/*
CLASSES
*/

classDeclaration: classModifier? CLASS TYPE_ID superTypes? classBody;

classModifier: PUBLIC;

superTypes: COLON superTypeList;

superTypeList: TYPE_ID (COMMA TYPE_ID)*;

classBody: LEFT_BRACE classBodyDeclaration* RIGHT_BRACE;

classBodyDeclaration: classMemberDeclaration
                    | constructorDeclaration;

classMemberDeclaration: fieldDeclaration
                      | methodDeclaration;

fieldDeclaration: fieldModifier* type variableDeclaratorList;

fieldModifier: PUBLIC
             | STATIC
             | CONSTANT;

methodDeclaration: methodModifier? methodHeader methodBody;

methodHeader: methodDeclarator formalParameterList? result?;

formalParameterList: LEFT_BRACKET formalParameters RIGHT_BRACKET;

formalParameters: formalParameter (COMMA formalParameter)*
                | explicitType ID (COMMA ID)+;

formalParameter: explicitType ID;

result: COLON explicitType;

methodDeclarator: ID;

methodBody: statement;

methodModifier: PUBLIC;

constructorDeclaration: constructorModifier? constructorDeclarator constructorBody;

constructorModifier: PUBLIC;

constructorDeclarator: TYPE_ID formalParameterList?;

constructorBody: statement;

/*
INTERFACES
*/

interfaceDeclaration: normalInterfaceDeclaration;

normalInterfaceDeclaration: interfaceModifier? INTERFACE TYPE_ID superTypes? interfaceBody;

interfaceModifier: PUBLIC;

interfaceBody: LEFT_BRACE interfaceMemberDeclaration* RIGHT_BRACE;

interfaceMemberDeclaration: constantDeclaration
                          | interfaceMethodDeclaration;

constantDeclaration: TYPE_ID variableDeclaratorList;

interfaceMethodDeclaration: methodHeader methodBody?;

/*
ENUMS
*/

enumDeclaration: enumModifier? ENUM TYPE_ID superTypes? enumBody;

enumModifier: PUBLIC;

enumBody: LEFT_BRACE enumConstantList? RIGHT_BRACE;

enumConstantList: TYPE_ID (COMMA TYPE_ID)*;

/*
STATEMENTS
*/

statement: block
         | localVariableDeclaration
         | condition
         | whileLoop
         | match
         | returnStatement
         | breakStatement
         | continueStatement
         | expression;

block: LEFT_BRACE statementList? RIGHT_BRACE;

statementList: statement (SEMI statement)*;

breakStatement: BREAK;

continueStatement: CONTINUE;

returnStatement: RETURN expression?;

condition: IF expression statement (ELSE statement)?;

whileLoop: WHILE expression statement;

localVariableDeclaration: MUTABLE? type variableDeclaratorList;

type: explicitType
    | implicitType;

explicitType: TYPE_ID
            | TYPE_ID NULLABLE;

implicitType: VARIABLE
            | VARIABLE NULLABLE;

variableDeclaratorList: variableDeclarator (COMMA variableDeclarator)*;

variableDeclarator: ID (ASSIGN variableInitializer)?;

variableInitializer: expression;

match: MATCH matchHeader ARROW matchBody;

matchHeader: expression
           | block;

matchBody: LEFT_BRACE matchExpression+ RIGHT_BRACE;

matchExpression: matchValueList COLON expression;

matchValueList: matchValue (COMMA matchValue)*;

matchValue: expression
          | ELSE;

/*
EXPRESSIONS
*/

expression: primary
          | expression PERIOD expression
          | methodInvocation
          | creation
          | expression postfix=(INCREMENT | DECREMENT)
          | prefix=(INCREMENT | DECREMENT) expression
          | NOT expression
          | expression bop=(MULTIPLY | DIVIDE) expression
          | expression bop=(ADD | SUBTRACT) expression
          | expression bop=(LESS_EQUAL | GREATER_EQUAL | GREATER | LESS) expression
          | expression bop=(EQUAL | NOT_EQUAL) expression
          | expression AND expression
          | expression OR expression
          | expression bop=(ASSIGN
                          | ADD_ASSIGN
                          | SUBTRACT_ASSIGN
                          | MULTIPLY_ASSIGN
                          | DIVIDE_ASSIGN) expression;

primary: LEFT_BRACKET expression RIGHT_BRACKET # parenPrimary
       | THIS # thisPrimary
       | SUPER # superPrimary
       | literal # literalPrimary
       | ID # idPrimary
       | TYPE_ID # typeIdPrimary;

literal: integerLiteral
       | floatingPointLiteral
       | BOOLEAN_LITERAL
       | stringLiteral
       | NULL;

integerLiteral: DECIMAL_INTEGER_LITERAL;

floatingPointLiteral: DECIMAL_FLOATING_POINT_LITERAL;

stringLiteral: REGULAR_STRING_LITERAL;

creation: TYPE_ID LEFT_BRACKET argumentList? RIGHT_BRACKET;

argumentList: expression (COMMA expression)*;

methodInvocation: ID LEFT_BRACKET argumentList? RIGHT_BRACKET
                | THIS LEFT_BRACKET argumentList? RIGHT_BRACKET
                | SUPER LEFT_BRACKET argumentList? RIGHT_BRACKET;
