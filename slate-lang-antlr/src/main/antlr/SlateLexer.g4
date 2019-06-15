lexer grammar SlateLexer;

// Keywords
BREAK: 'break';
CLASS: 'class';
CONSTANT: 'const';
CONTINUE: 'continue';
ELSE: 'else';
ENUM: 'enum';
IF: 'if';
IMPORT: 'use';
INTERFACE: 'interface';
MATCH: 'match';
MUTABLE: 'mut';
NULL: 'null';
PACKAGE: 'in';
PUBLIC: 'pub';
RETURN: 'return';
SUPER: 'super';
THIS: 'this';
VARIABLE: 'var';
WHILE: 'while';

// Literals
DECIMAL_INTEGER_LITERAL: '0'
                       | NonZeroDigit Digits*
                       | NonZeroDigit Underscores Digits;

DECIMAL_FLOATING_POINT_LITERAL: Digits '.' Digits*
                              | '.' Digits;

REGULAR_STRING_LITERAL: '\'' StringCharacter* '\'';

BOOLEAN_LITERAL: 'true'
               | 'false';

// Separators
PERIOD: '.';
LEFT_BRACE: '{';
RIGHT_BRACE: '}';
COLON: ':';
SEMI: ';';
LEFT_BRACKET: '(';
RIGHT_BRACKET: ')';
COMMA: ',';

// Operators
NULLABLE: '?';
ARROW: '=>';
INCREMENT: '++';
DECREMENT: '--';
NOT: '!';
ADD: '+';
SUBTRACT: '-';
MULTIPLY: '*';
DIVIDE: '/';
LESS_EQUAL: '<=';
GREATER_EQUAL: '>=';
LESS: '<';
GREATER: '>';
AND: '&&';
OR: '||';
EQUAL: '==';
NOT_EQUAL: '!=';
ASSIGN: '=';
ADD_ASSIGN: '+=';
SUBTRACT_ASSIGN: '-=';
MULTIPLY_ASSIGN: '*=';
DIVIDE_ASSIGN: '/=';

// Identifiers
TYPE_ID: [A-Z][A-Za-z0-9_]*;
ID: [_]*[a-z][A-Za-z0-9_]*;

// Whitespace
WS: [ \t\n\r]+ -> channel(HIDDEN);

// Comments
COMMENT: '/*' .*? '*/' -> channel(HIDDEN);
LINE_COMMENT: '//' ~[\r\n]* -> channel(HIDDEN);

// Fragments

// Decimal
fragment NonZeroDigit: [0-9];

fragment Digits: Digit
               | Digit DigitsAndUnderscores* Digit;

fragment Digit: '0'
              | NonZeroDigit;

fragment DigitsAndUnderscores: DigitOrUnderscore DigitOrUnderscore*;

fragment DigitOrUnderscore: Digit
                          | '-';

fragment Underscores: '_'+;

// Strings
fragment StringCharacter: ~['\\\r\n];
