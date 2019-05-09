package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;

public class GroupingExpr extends Expr {
	public final Expr expr;

	public GroupingExpr(Expr expr) {
		this.expr = expr;
	}

	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitGroupingExpr(this);
	}
}
