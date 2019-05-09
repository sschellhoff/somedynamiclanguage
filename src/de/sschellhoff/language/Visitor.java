package de.sschellhoff.language;

import de.sschellhoff.language.ast.AssignExpr;
import de.sschellhoff.language.ast.BinaryExpr;
import de.sschellhoff.language.ast.UnaryExpr;
import de.sschellhoff.language.ast.LiteralExpr;
import de.sschellhoff.language.ast.GroupingExpr;
import de.sschellhoff.language.ast.VarExpr;
import de.sschellhoff.language.ast.TernaryExpr;
import de.sschellhoff.language.ast.CallExpr;
import de.sschellhoff.language.ast.ExprStmt;
import de.sschellhoff.language.ast.PrintStmt;
import de.sschellhoff.language.ast.VarDeclStmt;
import de.sschellhoff.language.ast.BlockStmt;
import de.sschellhoff.language.ast.IfElseStmt;
import de.sschellhoff.language.ast.WhileStmt;
import de.sschellhoff.language.ast.BreakStmt;
import de.sschellhoff.language.ast.ContinueStmt;
import de.sschellhoff.language.ast.FuncDefStmt;
import de.sschellhoff.language.ast.ReturnStmt;
import de.sschellhoff.language.ast.ClassDeclStmt;

public interface Visitor<R> {
	R visitAssignExpr(AssignExpr expr);
	R visitBinaryExpr(BinaryExpr expr);
	R visitUnaryExpr(UnaryExpr expr);
	R visitLiteralExpr(LiteralExpr expr);
	R visitGroupingExpr(GroupingExpr expr);
	R visitVarExpr(VarExpr expr);
	R visitTernaryExpr(TernaryExpr expr);
	R visitCallExpr(CallExpr expr);
	R visitExprStmt(ExprStmt stmt);
	R visitPrintStmt(PrintStmt stmt);
	R visitVarDeclStmt(VarDeclStmt stmt);
	R visitBlockStmt(BlockStmt stmt);
	R visitIfElseStmt(IfElseStmt stmt);
	R visitWhileStmt(WhileStmt stmt);
	R visitBreakStmt(BreakStmt stmt);
	R visitContinueStmt(ContinueStmt stmt);
	R visitFuncDefStmt(FuncDefStmt stmt);
	R visitReturnStmt(ReturnStmt stmt);
	R visitClassDeclStmt(ClassDeclStmt stmt);
}
