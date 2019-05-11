package de.sschellhoff.language;

import de.sschellhoff.language.ast.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Resolver implements Visitor<Void> {
    private final ErrorWriter errorWriter;
    private final Interpreter interpreter;
    private final Stack<HashMap<String, Boolean>> scopes = new Stack<>();
    private FunctionType currentFunction = FunctionType.NONE;
    private ClassType currentClass = ClassType.NONE;
    private boolean hadError = false;

    public Resolver(Interpreter interpreter, ErrorWriter errorWriter) {
        this.interpreter = interpreter;
        this.errorWriter = errorWriter;
    }

    private void beginScope() {
        scopes.push(new HashMap<>());
    }

    private void endScope() {
        scopes.pop();
    }

    public void resolve(List<Stmt> stmts) {
        for(Stmt stmt : stmts) {
            resolve(stmt);
        }
    }

    private void resolve(Stmt stmt) {
        stmt.accept(this);
    }

    private void resolve(Expr expr) {
        expr.accept(this);
    }

    private void declare(Token name) {
        if(scopes.empty()) {
            return;
        }
        Map<String, Boolean> scope = scopes.peek();
        if(scope.containsKey(name.lexeme)) {
            make_error(name, "A variable with the same name was already declared in this scope");
        }
        scope.put(name.lexeme, false);
    }

    private void define(Token name) {
        if(scopes.empty()) {
            return;
        }
        scopes.peek().put(name.lexeme, true);
    }

    private void resolveLocal(Expr expr, Token name) {
        for(int i = scopes.size() - 1; i >= 0; i--) {
            if(scopes.get(i).containsKey(name.lexeme)) {
                interpreter.resolve(expr, scopes.size() - 1 - i);
                return;
            }
        }
    }

    private void resolveFunction(FuncDefStmt stmt, FunctionType type) {
        FunctionType enclosingFunction = currentFunction;
        currentFunction = type;
        beginScope();
        for(Token param : stmt.parameters) {
            declare(param);
            define(param);
        }
        resolve(stmt.body);
        endScope();
        currentFunction = enclosingFunction;
    }

    private void make_error(Token token, String msg) {
        this.hadError = true;
        if(token.type == TokenType.EOF) {
            errorWriter.write(msg + " at the end of the file", token.line);
        } else {
            errorWriter.write(msg + " at [" + token.lexeme + "]", token.line);
        }
    }

    public boolean hadError() {
        return this.hadError;
    }
    @Override
    public Void visitAssignExpr(AssignExpr expr) {
        resolve(expr.value);
        resolveLocal(expr, expr.name);
        return null;
    }

    @Override
    public Void visitBinaryExpr(BinaryExpr expr) {
        resolve(expr.lhs);
        resolve(expr.rhs);
        return null;
    }

    @Override
    public Void visitUnaryExpr(UnaryExpr expr) {
        resolve(expr.expr);
        return null;
    }

    @Override
    public Void visitLiteralExpr(LiteralExpr expr) {
        // nothing todo here
        return null;
    }

    @Override
    public Void visitGroupingExpr(GroupingExpr expr) {
        resolve(expr.expr);
        return null;
    }

    @Override
    public Void visitVarExpr(VarExpr expr) {
        if(!scopes.empty() && scopes.peek().get(expr.name.lexeme) == Boolean.FALSE) {
            make_error(expr.name, "cannot initialize a variable to itself");
        }
        resolveLocal(expr, expr.name);
        return null;
    }

    @Override
    public Void visitTernaryExpr(TernaryExpr expr) {
        resolve(expr.condition);
        resolve(expr.then_case);
        resolve(expr.else_case);
        return null;
    }

    @Override
    public Void visitCallExpr(CallExpr expr) {
        resolve(expr.callee);
        for(Expr argument : expr.arguments) {
            resolve(argument);
        }
        return null;
    }

    @Override
    public Void visitGetExpr(GetExpr expr) {
        resolve(expr.object);
        return null;
    }

    @Override
    public Void visitSetExpr(SetExpr expr) {
        resolve(expr.value);
        resolve(expr.object);
        return null;
    }

    @Override
    public Void visitThisExpr(ThisExpr expr) {
        if(currentClass == ClassType.NONE) {
            make_error(expr.keyword, "You cannot use if outside of classes");
            return null;
        }
        resolveLocal(expr, expr.keyword);
        return null;
    }

    @Override
    public Void visitNullCondOpExpr(NullCondOpExpr expr) {
        resolve(expr.object);
        return null;
    }

    @Override
    public Void visitNullCondTopExpr(NullCondTopExpr expr) {
        resolve(expr.expr);
        return null;
    }

    @Override
    public Void visitExprStmt(ExprStmt stmt) {
        resolve(stmt.expr);
        return null;
    }

    @Override
    public Void visitPrintStmt(PrintStmt stmt) {
        resolve(stmt.expr);
        return null;
    }

    @Override
    public Void visitVarDeclStmt(VarDeclStmt stmt) {
        declare(stmt.name);
        if(stmt.initializer != null) {
            resolve(stmt.initializer);
        }
        define(stmt.name);
        return null;
    }

    @Override
    public Void visitBlockStmt(BlockStmt stmt) {
        beginScope();
        resolve(stmt.stmts);
        endScope();
        return null;
    }

    @Override
    public Void visitIfElseStmt(IfElseStmt stmt) {
        resolve(stmt.condition);
        resolve(stmt.thenBranch);
        if(stmt.elseBranch != null) {
            resolve(stmt.elseBranch);
        }
        return null;
    }

    @Override
    public Void visitWhileStmt(WhileStmt stmt) {
        resolve(stmt.condition);
        resolve(stmt.body);
        return null;
    }

    @Override
    public Void visitBreakStmt(BreakStmt stmt) {
        // nothing todo here
        return null;
    }

    @Override
    public Void visitContinueStmt(ContinueStmt stmt) {
        // nothing todo here
        return null;
    }

    @Override
    public Void visitFuncDefStmt(FuncDefStmt stmt) {
        declare(stmt.name);
        define(stmt.name);
        resolveFunction(stmt, FunctionType.FUNCTION);
        return null;
    }

    @Override
    public Void visitReturnStmt(ReturnStmt stmt) {
        if(currentFunction == FunctionType.NONE) {
            make_error(stmt.keyword, "You cannot return from top-level code");
        }
        if(stmt.value != null) {
            if(currentFunction == FunctionType.INITIALIZER) {
                make_error(stmt.keyword, "You cannot return an explicit value from a constructor");
            }
            resolve(stmt.value);
        }
        return null;
    }

    @Override
    public Void visitClassDeclStmt(ClassDeclStmt stmt) {
        ClassType enclosingClass = currentClass;
        currentClass = ClassType.CLASS;
        declare(stmt.name);
        beginScope();
        scopes.peek().put("this", true);
        for(FuncDefStmt method : stmt.methods) {
            FunctionType declaration = FunctionType.METHOD;
            if(method.name.lexeme.equals("init")) {
                declaration = FunctionType.INITIALIZER;
            }
            resolveFunction(method, declaration);
        }
        define(stmt.name);
        endScope();
        currentClass = enclosingClass;
        return null;
    }

    @Override
    public Void visitImportStmt(ImportStmt stmt) {
        // NOTHING TODO HERE
        return null;
    }
}
