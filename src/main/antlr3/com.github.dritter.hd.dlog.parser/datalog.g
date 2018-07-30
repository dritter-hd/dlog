grammar datalog;

options {
	output = AST;
	ASTLabelType=CommonTree;
}

tokens{
  PROGRAM;
  QUERY;
  BODY;
  RULE;
}

@header{package com.github.dritter.hd.dlog.parser;}

@lexer::header {package com.github.dritter.hd.dlog.parser;}

COMMENT
    :   '%' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    ;

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
    ;

STRING
    :  '"' ( ESC_SEQ | ~('\\'|'"') )* '"'
    ;

fragment
HEX_DIGIT : ('0'..'9'|'a'..'f'|'A'..'F') ;

fragment
ESC_SEQ
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    |   UNICODE_ESC
    |   OCTAL_ESC
    ;

fragment
OCTAL_ESC
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment
UNICODE_ESC
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
	;

INTEGER
	:	('+'|'-')? ('0'..'9')*
	;

DOUBLE
	:	('+'|'-')? ('0'..'9')* '.' ('0'..'9')+ (('E'|'e') ('+'|'-')? ('0'..'9')+)?
	;

CHARACTER
	:	'\'' (~('\'' | '\\') | ESC_SEQ) '\''
	;

BOOLEAN
	:	'true' | 'false'
	;

DATE
	:	('0'..'3')? ('0'..'9') '.' ('0'..'1')? ('0'..'9') '.' ('0'..'9') ('0'..'9') ('0'..'9') ('0'..'9')+
	;

IDENTIFIER
	:	('=c'|'<'|'='|'>'|'<='|'!='|'>='|'A'..'Z'|'a'..'z') ('-'|'0'..'9'|'A'..'Z'|'a'..'z')*
	;

fragment
parameter
	:	INTEGER | DOUBLE | CHARACTER | BOOLEAN | STRING | DATE | IDENTIFIER
	;

fragment
literal 
	: IDENTIFIER ( '(' parameter ( ',' parameter ) * ')' ) -> ^(IDENTIFIER parameter *)
	;

fragment
body
	: literal ( ',' literal ) * -> ^(BODY literal *)
	;

fragment
rule 	
	: literal ( ':-' body ) ? '.' -> ^(RULE literal body ? )
	;
	
fragment
query
	: '?-' body '.' -> ^(QUERY body)
	;
	
program 
	: rule * query ? -> ^(PROGRAM rule * query ?)
	;