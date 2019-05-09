package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;

public class TernaryExpr extends Expr {
	public final Expr condition;
	public final Expr then_case;
	public final Expr else_case;

	public TernaryExpr(Expr condition, Expr then_case, Expr else_case) {
		this.condition = condition;
		this.then_case = then_case;
		this.else_case = else_case;
	}

	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitTernaryExpr(this);
	}
}
