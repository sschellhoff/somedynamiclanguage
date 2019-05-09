package de.sschellhoff.language;

import de.sschellhoff.language.ast.*;

import java.util.List;

public class PrintVisitor implements Visitor<String> {

    public void print(List<Stmt> stmts) {
        for(Stmt stmt : stmts) {
            System.out.println(stmt.accept(this));
        }
    }

    private String parenthesize(String name, Expr... exprs) {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(name);
        for (Expr expr : exprs) {
            if (expr == null) {
                sb.append(" null");
            } else {
                sb.append(" ");
                sb.append(expr.accept(this));
            }
        }
        sb.append(")");

        return sb.toString();
    }

    @Override
    public String visitAssignExpr(AssignExpr expr) {
        return parenthesize("assign " + expr.name.lexeme, expr.value);
    }

    @Override
    public String visitBinaryExpr(BinaryExpr expr) {
        return parenthesize(expr.op.lexeme, expr.lhs, expr.rhs);
    }

    @Override
    public String visitUnaryExpr(UnaryExpr expr) {
        return parenthesize(expr.op.lexeme, expr.expr);
    }

    @Override
    public String visitLiteralExpr(LiteralExpr expr) {
        if (expr.value == null) {
            return "null";
        } else {
            return expr.value.toString();
        }
    }

    @Override
    public String visitGroupingExpr(GroupingExpr expr) {
        return parenthesize("group", expr.expr);
    }

    @Override
    public String visitVarDeclStmt(VarDeclStmt stmt) {
        return parenthesize(stmt.name.lexeme, stmt.initializer);
    }

    @Override
    public String visitBlockStmt(BlockStmt stmt) {
        StringBuilder sb = new StringBuilder();
        sb.append("(block");
        for(Stmt st : stmt.stmts) {
            sb.append(" ");
            sb.append(st.accept(this));
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String visitIfElseStmt(IfElseStmt stmt) {
        StringBuilder sb = new StringBuilder();
        sb.append("(if ");
        sb.append(stmt.condition.accept(this));
        sb.append(stmt.thenBranch.accept(this));
        if(stmt.elseBranch != null) {
            sb.append(stmt.elseBranch.accept(this));
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String visitWhileStmt(WhileStmt stmt) {
        StringBuilder sb = new StringBuilder();
        sb.append("(while ");
        sb.append(stmt.condition.accept(this));
        sb.append(stmt.body.accept(this));
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String visitBreakStmt(BreakStmt stmt) {
        return parenthesize("break");
    }

    @Override
    public String visitContinueStmt(ContinueStmt stmt) {
        return parenthesize("continue");
    }

    @Override
    public String visitFuncDefStmt(FuncDefStmt stmt) {
        StringBuilder sb = new StringBuilder();
        sb.append("(func def " + stmt.name.lexeme + " ");
        for(Token parameter : stmt.parameters) {
            sb.append(" ");
            sb.append(parameter.lexeme);
        }
        sb.append(stmt.body.accept(this));
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String visitReturnStmt(ReturnStmt stmt) {
        return parenthesize("return", stmt.value);
    }

    @Override
    public String visitClassDeclStmt(ClassDeclStmt stmt) {
        return null;
    }

    @Override
    public String visitVarExpr(VarExpr expr) {
        return parenthesize(expr.name.lexeme);
    }

    @Override
    public String visitTernaryExpr(TernaryExpr expr) {
        return parenthesize("?:", expr.condition, expr.then_case, expr.else_case);
    }

    @Override
    public String visitCallExpr(CallExpr expr) {
        StringBuilder sb = new StringBuilder();
        sb.append("(call ");
        sb.append(expr.callee.accept(this));
        for(Expr argument : expr.arguments) {
            sb.append(" ");
            sb.append(argument.accept(this));
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String visitExprStmt(ExprStmt stmt) {
        return parenthesize("expr", stmt.expr);
    }

    @Override
    public String visitPrintStmt(PrintStmt stmt) {
        return parenthesize("print", stmt.expr);
    }
}
