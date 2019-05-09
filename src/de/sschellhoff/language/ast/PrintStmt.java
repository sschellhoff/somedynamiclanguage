package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;

public class PrintStmt extends Stmt {
	public final Expr expr;

	public PrintStmt(Expr expr) {
		this.expr = expr;
	}

	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitPrintStmt(this);
	}
}
