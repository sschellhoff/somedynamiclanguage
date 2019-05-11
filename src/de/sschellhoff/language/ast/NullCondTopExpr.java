package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;

public class NullCondTopExpr extends Expr {
	public final Expr expr;

	public NullCondTopExpr(Expr expr) {
		this.expr = expr;
	}

	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitNullCondTopExpr(this);
	}
}
