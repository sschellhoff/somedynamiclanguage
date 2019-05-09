package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;

public class ExprStmt extends Stmt {
	public final Expr expr;

	public ExprStmt(Expr expr) {
		this.expr = expr;
	}

	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitExprStmt(this);
	}
}
