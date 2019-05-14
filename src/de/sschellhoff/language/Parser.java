package de.sschellhoff.language;

import de.sschellhoff.language.ast.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;
    private ErrorWriter errorWriter;
    private boolean hadError = false;
    private int numOpenLoops = 0;

    public Parser(List<Token> tokens, ErrorWriter errorWriter) {
        this.tokens = tokens;
        this.errorWriter = errorWriter;
    }

    public List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(top_level_statement());
        }
        return statements;
    }

    public boolean hadError() {
        return this.hadError;
    }

    private boolean isInLoop() {
        return numOpenLoops > 0;
    }

    private Stmt top_level_statement() {
        if (peek().type == TokenType.IMPORT) {
            return importStmt();
        } else {
            return statement();
        }
    }

    private Stmt statement() {
        try {
            switch (peek().type) {
                case FUN:
                    return function_definition("function");
                case CLASS:
                    return class_definition();
                case RETURN:
                    return returnStmt();
                case IF:
                    return ifStmt();
                case WHILE:
                    return whileStmt();
                case FOR:
                    return forStmt();
                case PRINT:
                    return print();
                case VAR:
                    return varDecl();
                case BREAK:
                    return breakStmt();
                case CONTINUE:
                    return continueStmt();
                case LEFT_BRACE:
                    return block();
                default:
                    return expressionStatement();
            }
        } catch (ParseError error) {
            synchronize_error();
            return null;
        }
    }

    private Stmt importStmt() {
        Token keyword = consume_or_error(TokenType.IMPORT, "expected import statement");
        Token filename = consume_or_error(TokenType.STRING, "expected filename");
        match_or_error(TokenType.SEMICOLON, "expected ; at the end of the statement");
        return new ImportStmt(keyword, filename.literal.toString());
    }

    private FuncDefStmt function_definition(String function_type) {
        match_or_error(TokenType.FUN, "Expected function definition");
        Token name = consume_or_error(TokenType.IDENTIFIER, "Expected " + function_type + " name");
        consume_or_error(TokenType.LEFT_PAREN, "Expected ( after " +  function_type + " name.");
        List<Token> parameters = new ArrayList<>();
        if(!check(TokenType.RIGHT_PAREN)) {
            do {
                parameters.add(consume_or_error(TokenType.IDENTIFIER, "Expected parameter name."));
            } while(match(TokenType.COMMA));
        }
        consume_or_error(TokenType.RIGHT_PAREN, "Expected ) after parameter list");
        Stmt body = block();
        return new FuncDefStmt(name, parameters, body);
    }

    private Stmt class_definition() {
        match_or_error(TokenType.CLASS, "expected class definition");
        Token name = consume_or_error(TokenType.IDENTIFIER, "expected class name");
        VarExpr superclass = null;
        if(match(TokenType.COLON)) {
            match_or_error(TokenType.IDENTIFIER, "expected name of superclass");
            superclass = new VarExpr(previous());
        }
        match_or_error(TokenType.LEFT_BRACE, "expected class body");
        List<FuncDefStmt> methods = new ArrayList<>();
        while(!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            methods.add(function_definition("method"));
        }
        consume_or_error(TokenType.RIGHT_BRACE, "expected } to close the class body");
        return new ClassDeclStmt(name, superclass, methods);
    }

    private Stmt returnStmt() {
        Token keyword = consume_or_error(TokenType.RETURN, "expected return statement");
        Expr value = null;
        if(!check(TokenType.SEMICOLON)) {
            value = expression();
        }
        match_or_error(TokenType.SEMICOLON, "expected semicolon");
        return new ReturnStmt(keyword, value);
    }
    private Stmt block() {
        List<Stmt> statements = new ArrayList<>();
        match_or_error(TokenType.LEFT_BRACE, "");
        while (!isAtEnd() && peek().type != TokenType.RIGHT_BRACE) {
            statements.add(statement());
        }
        match_or_error(TokenType.RIGHT_BRACE, "");
        return new BlockStmt(statements);
    }

    private Stmt breakStmt() {
        match_or_error(TokenType.BREAK, "expected break statement");
        Token break_tok = previous();
        if(isInLoop()) {
            match_or_error(TokenType.SEMICOLON, "expected semicolon");
            return new BreakStmt();
        }
        make_error(break_tok, "break must be inside a loop");
        return null;
    }

    private Stmt continueStmt() {
        match_or_error(TokenType.CONTINUE, "expected continue statement");
        Token continue_tok = previous();
        if(isInLoop()) {
            match_or_error(TokenType.SEMICOLON, "expected semicolon");
            return new ContinueStmt();
        }
        make_error(continue_tok, "continue must be inside a loop");
        return null;
    }

    private Stmt ifStmt() {
        match_or_error(TokenType.IF, "expected if statement");
        Expr condition = expression();
        Stmt then_branch = block();
        Stmt else_branch = null;
        if(match(TokenType.ELSE)) {
            else_branch = block();
        }
        return new IfElseStmt(condition, then_branch, else_branch);
    }

    private Stmt whileStmt() {
        match_or_error(TokenType.WHILE, "expected while statement");
        Expr condition = expression();
        numOpenLoops++;
        Stmt body = block();
        numOpenLoops--;
        return new WhileStmt(condition, body);
    }

    private Stmt forStmt() {
        match_or_error(TokenType.FOR, "expected for statement");
        Stmt init = null;
        if(!match(TokenType.SEMICOLON)) {
            if(peek().type == TokenType.VAR) {
                init = varDecl();
            } else {
                init = expressionStatement();
            }
        }
        Expr cond = null;
        if(!match(TokenType.SEMICOLON)) {
            cond = expression();
            match_or_error(TokenType.SEMICOLON, "expected ; after expression");
        }
        Expr expr = null;
        if(peek().type != TokenType.LEFT_BRACE && peek().type != TokenType.SEMICOLON) {
            expr = expression();
        }
        numOpenLoops++;
        BlockStmt body = null;
        if(match(TokenType.SEMICOLON)) {
            body = new BlockStmt(new ArrayList<Stmt>());
        } else {
            body = (BlockStmt)block();
        }
        numOpenLoops--;
        List<Stmt> forStmts = new ArrayList<Stmt>();
        if (init != null) {
            forStmts.add(init);
        }
        if (expr != null) {
            body.stmts.add(new ExprStmt(expr));
        }
        forStmts.add(new WhileStmt(cond, body));
        return new BlockStmt(forStmts);
    }

    private Stmt varDecl() {
        match_or_error(TokenType.VAR, "exprected variable declaration");
        Token name = consume_or_error(TokenType.IDENTIFIER, "expected identifier");
        Expr initializer = null;
        if (match(TokenType.EQUAL)) {
            initializer = expression();
        }
        match_or_error(TokenType.SEMICOLON, "expected semicolon");

        return new VarDeclStmt(name, initializer);
    }

    private Stmt print() {
        match_or_error(TokenType.PRINT, "exprected print statement");
        Expr expr = expression();
        match_or_error(TokenType.SEMICOLON, "extecpted ; after statement");
        return new PrintStmt(expr);
    }

    private Stmt expressionStatement() {
        Expr expr = expression();
        match_or_error(TokenType.SEMICOLON, "expexted ; after expression");
        return new ExprStmt(expr);
    }

    private Expr expression() {
        return assignment();
    }

    private Expr assignment() {
        Expr expr = ternary();

        if (match(TokenType.EQUAL)) {
            Token equals = previous();
            Expr value = expression();

            if(expr instanceof VarExpr) {
                Token name = ((VarExpr)expr).name;
                return new AssignExpr(name, value);
            } else if(expr instanceof GetExpr) {
                GetExpr get = (GetExpr)expr;
                return new SetExpr(get.object, get.name, value);
            }
            make_error(equals, "invalid assignment target");
        }

        return expr;
    }

    private Expr ternary() {
        Expr condition = null_coalescing();

        while (match(TokenType.QUESTION_MARK)) {
            Expr then_part = expression();
            match_or_error(TokenType.COLON, "expected : as part of the ternary operator");
            Expr else_part = null_coalescing();
            return new TernaryExpr(condition, then_part, else_part);
        }

        return condition;
    }

    private Expr null_coalescing() {
        Expr lhs = or();
        while(match(TokenType.NULL_COALESCING)) {
            Token operator = previous();
            Expr rhs =  or();
            lhs = new BinaryExpr(operator, lhs, rhs);
        }
        return lhs;
    }

    private Expr or() {
        Expr lhs = and();

        while(match(TokenType.OR)) {
            Token operator = previous();
            Expr rhs = and();
            lhs = new BinaryExpr(operator, lhs, rhs);
        }

        return lhs;
    }

    private Expr and() {
        Expr lhs = equality();

        while(match(TokenType.AND)) {
            Token operator = previous();
            Expr rhs = equality();
            lhs = new BinaryExpr(operator, lhs, rhs);
        }

        return lhs;
    }

    private Expr equality() {
        Expr lhs = comparison();

        while ( match(TokenType.EXCL_EQUAL, TokenType.EQUAL_EQUAL) ) {
            Token op = previous();
            Expr rhs = comparison();
            lhs = new BinaryExpr(op, lhs, rhs);
        }

        return lhs;
    }

    private Expr comparison() {
        Expr lhs = addition();

        while ( match(TokenType.LESS, TokenType.LESS_EQUAL, TokenType.GREATER, TokenType.GREATER_EQUAL) ) {
            Token op = previous();
            Expr rhs = addition();
            lhs = new BinaryExpr(op, lhs, rhs);
        }

        return lhs;
    }

    private Expr addition() {
        Expr lhs = multiplication();

        while ( match(TokenType.PLUS, TokenType.MINUS) ) {
            Token op = previous();
            Expr rhs = multiplication();
            lhs = new BinaryExpr(op, lhs, rhs);
        }

        return lhs;
    }

    private Expr multiplication() {
        Expr lhs = unary();

        while ( match(TokenType.STAR, TokenType.SLASH, TokenType.MOD) ) {
            Token op = previous();
            Expr rhs = unary();
            lhs = new BinaryExpr(op, lhs, rhs);
        }

        return lhs;
    }

    private Expr unary() {
        if ( match(TokenType.MINUS, TokenType.EXCL_MARK) ) {
            Token op = previous();
            Expr expr = unary();
            return new UnaryExpr(op, expr);
        }

        return call();
    }

    private Expr call() {
        Expr expr = primary();
        boolean is_null_conditional = false;

        while(true) {
            if (match(TokenType.LEFT_PAREN)) {
                expr = finish_call(expr);
            } else if(match(TokenType.DOT)) {
                Token name = consume_or_error(TokenType.IDENTIFIER, "expected property");
                expr = new GetExpr(expr, name);
            } else if(match(TokenType.NULL_COND_OP)) {
                is_null_conditional = true;
                Token name = consume_or_error(TokenType.IDENTIFIER, "expected property");
                expr = new NullCondOpExpr(expr, name);
            } else {
                break;
            }
        }

        if(is_null_conditional) {
            expr = new NullCondTopExpr(expr);
        }
        return expr;
    }

    private Expr finish_call(Expr callee) {
        List<Expr> arguments = new ArrayList<>();
        if (!check(TokenType.RIGHT_PAREN)) {
            do {
                arguments.add(expression());
            } while(match(TokenType.COMMA));
        }
        Token paren = consume_or_error(TokenType.RIGHT_PAREN, "Expected ')' after argument list.");
        return new CallExpr(callee, paren, arguments);
    }

    private Expr primary() {
        if(match(TokenType.IDENTIFIER)) {
            return new VarExpr(previous());
        }
        if(match(TokenType.THIS)) {
            return new ThisExpr(previous());
        }
        if(match(TokenType.SUPER)) {
            Token keyword = previous();
            match_or_error(TokenType.DOT, "expected . after super");
            Token method = consume_or_error(TokenType.IDENTIFIER, "expected method name of superclass");
            return new SuperExpr(keyword, method);
        }
        if(match(TokenType.FALSE)) {
            return new LiteralExpr(false);
        }
        if(match(TokenType.TRUE)) {
            return new LiteralExpr(true);
        }
        if(match(TokenType.NULL)) {
            return new LiteralExpr(null);
        }
        if(match(TokenType.FLOAT, TokenType.INT, TokenType.STRING)) {
            return new LiteralExpr(previous().literal);
        }
        if(match(TokenType.LEFT_PAREN)) {
            Expr expr = expression();
            match_or_error(TokenType.RIGHT_PAREN, "Missing ) after expression");
            return new GroupingExpr(expr);
        }
        make_error("Expected expression");
        return null; // never triggered
    }

    private boolean match(TokenType... types) {
        for(TokenType type : types) {
            if ( check(type) ) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token advance() {
        if ( !isAtEnd() ) {
            current++;
        }
        return previous();
    }

    private boolean check(TokenType type) {
        if ( isAtEnd() ) {
            return false;
        }
        return peek().type == type;
    }

    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }

    private Token peek() {
        return this.tokens.get(current);
    }

    private Token previous() {
        return this.tokens.get(current - 1);
    }

    private void make_error(String msg) {
        make_error(peek(), msg);
    }

    private void make_error(Token token, String msg) {
        this.hadError = true;
        if ( token.type == TokenType.EOF ) {
            errorWriter.write(msg + " at the end of the file", token.line);
        } else {
            errorWriter.write(msg + " at [" + token.lexeme + "]", token.line);
        }
        throw new ParseError();
    }

    private void match_or_error(TokenType type, String errorMsg) {
        if(!match(type)) {
            make_error(errorMsg);
        }
    }

    private Token consume_or_error(TokenType type, String errorMsg) {
        if ( check(type)) return advance();

        make_error(errorMsg);
        return null;
    }

    private void synchronize_error() {
        advance();
        while ( !isAtEnd() ) {
            if ( previous().type == TokenType.SEMICOLON ) {
                return;
            }
            switch(peek().type) {
                case CLASS:
                case FUN:
                case IF:
                case IMPORT:
                case RETURN:
                case VAR:
                case WHILE:
                case FOR:
                case BREAK:
                case CONTINUE:
                    return;
            }
            advance();
        }
    }
}
