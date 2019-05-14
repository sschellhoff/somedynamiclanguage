package de.sschellhoff.language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Scanner {
    private final ErrorWriter errorWriter;
    private final String source;
    private final List<Token> tokens = new ArrayList<Token>();
    private final HashMap<String, TokenType> reservedWords = new HashMap<>();

    private int start = 0;
    private int current = 0;
    private int line = 1;

    private boolean hadError = false;

    public Scanner(String source, ErrorWriter errorWriter) {
        this.source = source;
        this.errorWriter = errorWriter;
        reservedWords.put("true", TokenType.TRUE);
        reservedWords.put("false", TokenType.FALSE);
        reservedWords.put("null", TokenType.NULL);
        reservedWords.put("if", TokenType.IF);
        reservedWords.put("else", TokenType.ELSE);
        reservedWords.put("while", TokenType.WHILE);
        reservedWords.put("for", TokenType.FOR);
        reservedWords.put("fun", TokenType.FUN);
        reservedWords.put("var", TokenType.VAR);
        reservedWords.put("class", TokenType.CLASS);
        reservedWords.put("super", TokenType.SUPER);
        reservedWords.put("this", TokenType.THIS);
        reservedWords.put("import", TokenType.IMPORT);
        reservedWords.put("return", TokenType.RETURN);
        reservedWords.put("print", TokenType.PRINT);
        reservedWords.put("and", TokenType.AND);
        reservedWords.put("or", TokenType.OR);
        reservedWords.put("break", TokenType.BREAK);
        reservedWords.put("continue", TokenType.CONTINUE);
    }

    public List<Token> scan() {
        while ( !isAtEnd() ) {
            this.start = this.current;
            scanToken();
        }
        addToken(TokenType.EOF);
        return tokens;
    }

    public boolean hadError() {
        return hadError;
    }

    private void scanToken() {
        char c = advance();
        switch ( c ) {
            case '+':
                addToken(TokenType.PLUS);
                break;
            case '-':
                addToken(TokenType.MINUS);
                break;
            case '*':
                addToken(TokenType.STAR);
                break;
            case '/':
                if( match('/') ) {
                    while ( peek() != '\n' ) {
                        advance();
                    }
                } else {
                    addToken(TokenType.SLASH);
                }
                break;
            case '%':
                addToken(TokenType.MOD);
                break;
            case '(':
                addToken(TokenType.LEFT_PAREN);
                break;
            case ')':
                addToken(TokenType.RIGHT_PAREN);
                break;
            case '{':
                addToken(TokenType.LEFT_BRACE);
                break;
            case '}':
                addToken(TokenType.RIGHT_BRACE);
                break;
            case '[':
                addToken(TokenType.LEFT_BRACKET);
                break;
            case ']':
                addToken(TokenType.RIGHT_BRACKET);
                break;
            case '=':
                addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
                break;
            case '!':
                addToken(match('=') ? TokenType.EXCL_EQUAL : TokenType.EXCL_MARK);
                break;
            case ',':
                addToken(TokenType.COMMA);
                break;
            case ';':
                addToken(TokenType.SEMICOLON);
                break;
            case '<':
                addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
                break;
            case '>':
                addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
                break;
            case '.':
                addToken(TokenType.DOT);
                break;
            case '?':
                addToken(match('?') ? TokenType.NULL_COALESCING : match('.') ? TokenType.NULL_COND_OP : TokenType.QUESTION_MARK);
                break;
            case ':':
                addToken(TokenType.COLON);
                break;
            case '&':
                if(!match('&')) {
                    make_error("unexpected token", line);
                } else {
                    addToken(TokenType.AND);
                }
                break;
            case '|':
                if(!match('|')) {
                    make_error("unexpected token", line);
                } else {
                    addToken(TokenType.OR);
                }
                break;
            case '"':
                handleString();
                break;
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                break;
            default:
                if ( isDigit(c) ) {
                    handleNumber();
                } else if ( isAlpha(c) ) {
                    handleIdentifier();
                } else {
                    make_error("Unexpected token", line);
                }
        }
    }

    private boolean isAtEnd() {
        return this.source.length() <= current;
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        this.tokens.add(new Token(type, this.source.substring(start, current), literal, line));
    }

    private char advance() {
        this.current++;
        return this.source.charAt(this.current-1);
    }

    private boolean match(char c) {
        if ( !isAtEnd() && this.source.charAt( this.current ) == c ) {
            advance();
            return true;
        }
        return false;
    }

    private char peek() {
        if ( isAtEnd() ) {
            return '\0';
        }
        return this.source.charAt( this.current );
    }

    private char peekNext() {
        if ( current + 1 > this.source.length() ) {
            return '\0';
        } else {
            return this.source.charAt(current + 1);
        }
    }

    private void handleString() {
        while ( peek() != '"' && !isAtEnd()) {
            if ( peek() == '\n' ) {
                line++;
            }
            advance();
            if(isAtEnd()) {
                make_error("unterminated string!", line);
            }
            if ( peek() == '\\') {
                advance();
                if(isAtEnd()) {
                    make_error("unterminated string!", line);
                }
                advance();
            }
        }
        if(isAtEnd()) {
            make_error("unterminated string!", line);
        }
        advance();
        String value = this.source.substring(start + 1, current - 1);
        value = unescape(value, line);
        addToken(TokenType.STRING, value);
    }

    private void handleNumber() {
        while ( isDigit( peek() ) ) {
            advance();
        }
        if ( peek() == '.' && isDigit(peekNext()) ) {
            advance();
            while ( isDigit(peek()) ) {
                advance();
            }
            addToken(TokenType.FLOAT, Double.parseDouble(this.source.substring(start, current)));
        } else {
            addToken(TokenType.INT, Integer.parseInt(this.source.substring(start, current)));
        }
    }

    private void handleIdentifier() {
        while ( isAlphaNumeric( peek() )) {
            advance();
        }
        String text = this.source.substring(start, current);
        if ( reservedWords.containsKey(text)) {
            addToken(reservedWords.get(text), text);
        } else {
            addToken(TokenType.IDENTIFIER, text);
        }
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
    }

    private boolean isAlphaNumeric(char c) {
        return isDigit(c) || isAlpha(c);
    }

    private String unescape(String value, int line) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if(c == '\\') {
                i++;
                c = value.charAt(i);
                switch(c) {
                    case 'n':
                        c = '\n';
                        break;
                    case 't':
                        c = '\t';
                        break;
                    case '"':
                        c = '"';
                        break;
                    case '\\':
                        c = '\\';
                        break;
                    case 'r':
                        c = '\r';
                        break;
                    case 'b':
                        c = '\b';
                        break;
                    case 'a':
                        c = '\007';
                        break;
                    case 'e':
                        c = '\033';
                        break;
                    default:
                        make_error("invalid escape sequence '\\" + c + "'", line);
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }

    private void make_error(String msg, int line) {
        this.hadError = true;
        errorWriter.write(msg, line);
    }
}
