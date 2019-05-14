package de.sschellhoff.language;

public enum TokenType {
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE, LEFT_BRACKET, RIGHT_BRACKET,
    PLUS, MINUS, STAR, SLASH, MOD,
    LESS, GREATER, LESS_EQUAL, GREATER_EQUAL, EQUAL, EQUAL_EQUAL, EXCL_MARK, EXCL_EQUAL,
    NULL_COALESCING, NULL_COND_OP,
    QUESTION_MARK, COLON,
    DOT, COMMA,
    SEMICOLON,
    IDENTIFIER,
    FLOAT, INT,
    STRING,
    AND, OR,
    IF, ELSE, WHILE, FOR, FUN, VAR, CLASS, SUPER, THIS, IMPORT, RETURN, NULL, TRUE, FALSE, PRINT,
    BREAK, CONTINUE,
    EOF
}
