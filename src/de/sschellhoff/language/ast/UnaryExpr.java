package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;
import de.sschellhoff.language.Token;

public class UnaryExpr extends Expr {
	public final Token op;
	public final Expr expr;

	public UnaryExpr(Token op, Expr expr) {
		this.op = op;
		this.expr = expr;
	}

	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitUnaryExpr(this);
	}
}
