lexer grammar SlateLexer;

// Whitespace
WS: [ \t\n\r]+ -> channel(HIDDEN);

// Keywords
VAR: 'var';
PRINT: 'print';
IF: 'if';
ELSE: 'else';
WHILE: 'while';

// Literals
INT_LIT: '0' | [1-9] (Digits? | '_'+ Digits);
DEC_LIT: Digits '.' Digits? | '.' Digits;
STRING_LIT: '\''~('\r'|'\n'|'\'')*'\'';
TRUE_LIT: 'true';
FALSE_LIT: 'false';

// Operators
ADD: '+';
SUB: '-';
MUL: '*';
DIV: '/';
LESS: '<';
GREATER: '>';
LESS_EQ: '<=';
GREATER_EQ: '>=';
EQUAL: '==';
NOT_EQUAL: '!=';
OR: '||';
AND: '&&';
ASSIGN: '=';
L_PAREN: '(';
R_PAREN: ')';
L_BRACE: '{';
R_BRACE: '}';

// Identifiers
ID: [_]*[a-z][A-Za-z0-9_]*;

fragment Digits: [0-9] ([0-9_]* [0-9])?;
